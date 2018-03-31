/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.dao.impl.AudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserGiftDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbVideoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.VideoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BuzzFileData;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.DeleteBuzzData;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;

/**
 *
 * @author RuAc0n
 */
public class DeleteBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject jsonObj, Date time) {
        Respond result = new ListEntityRespond();
        List<DeleteBuzzData> listData = new ArrayList<>();
        try {
            JSONArray dataObj = (JSONArray) jsonObj.get(ParamKey.DATA);
            
            for(Object objArr: dataObj){
                JSONObject obj = (JSONObject) objArr;
                String userId = Util.getStringParam(obj, ParamKey.USER_ID);
                Long buzzType = Util.getLongParam(obj, ParamKey.BUZZ_TYPE);
                Long isStatus = Util.getLongParam(obj, ParamKey.IS_STATUS);
                String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
                int isAvatar = Constant.FLAG.OFF;

                if (buzzType != null) {
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ && isStatus == Constant.FLAG.ON) {
                        UserActivityDAO.removeStatus(userId);
                    }
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS) {
                        String imageId = Util.getStringParam(obj, ParamKey.FILE_ID);
                        if (imageId != null) {
                            ImageDAO.updateFlag(imageId, Constant.FLAG.OFF);
                        }
                        Util.addDebugLog("imageId---------------------------" + imageId);
                        Util.addDebugLog("buzzId---------------------------" + buzzId);
                        if (PbImageDAO.checkPbImageExist(userId, imageId)) {
                            Util.addDebugLog("imageId---------------------------" + userId);
                            PbImageDAO.removePublicImage(userId, buzzId);
                            UserDAO.removePbImage(userId);
                            boolean checkAvatar = UserDAO.checkAvatar(userId, imageId);
                            if (checkAvatar) {
                                UserDAO.removeAvatar(userId);
                                isAvatar = Constant.FLAG.ON;
                            }
                        }
                    }
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS) {
                        String videoId = Util.getStringParam(obj, ParamKey.FILE_ID);
                        if (videoId != null) {
                            VideoDAO.updateFlag(videoId, Constant.FLAG.OFF);
                        }
                        if (PbVideoDAO.checkPbVideoExist(userId, videoId)) {
                            PbVideoDAO.removePublicVideoByVideoId(userId, videoId);
                            UserDAO.removePbVideo(userId);
                        }
                    }
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS) {
                        String audioId = Util.getStringParam(obj, ParamKey.FILE_ID);
                        if (audioId != null) {
                            AudioDAO.updateFlag(audioId, Constant.FLAG.OFF);
                        }
                        if (PbAudioDAO.checkPbAudioExist(userId, audioId)) {
                            PbAudioDAO.removePublicAudioByAudioId(userId, audioId);
                            UserDAO.removePbAudio(userId);
                        }
                    }
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ) {
                        String giftId = Util.getStringParam(obj, ParamKey.BUZZ_VALUE);
                        UserGiftDAO.removeGift(userId, giftId);
                    }
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS) {
                        List<BuzzFileData> listChild = BuzzDetailDAO.getChildBuzz(buzzId);
                        for (BuzzFileData item : listChild) {
                            if (item.buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS) {
                                ImageDAO.updateFlag(item.fileId, Constant.FLAG.OFF);
                                if (PbImageDAO.checkPbImageExist(userId, item.fileId)) {
                                    PbImageDAO.removePublicImage(userId, item.buzzId);
                                    UserDAO.removePbImage(userId);
                                }
                            }
                            if (item.buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS) {
                                VideoDAO.updateFlag(item.fileId, Constant.FLAG.OFF);
                                if (PbVideoDAO.checkPbVideoExist(userId, item.fileId)) {
                                    PbVideoDAO.removePublicVideoByVideoId(userId, item.fileId);
                                    UserDAO.removePbVideo(userId);
                                }
                            }
                            if (item.buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS) {
                                AudioDAO.updateFlag(item.fileId, Constant.FLAG.OFF);
                                if (PbAudioDAO.checkPbAudioExist(userId, item.fileId)) {
                                    PbAudioDAO.removePublicAudioByAudioId(userId, item.fileId);
                                    UserDAO.removePbAudio(userId);
                                }
                            }
                        }
                    }
                    UserBuzzDAO.remove(buzzId);
                }

                UserDAO.removeBuzz(userId);
                listData.add(new DeleteBuzzData(isAvatar));
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS,listData );
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
