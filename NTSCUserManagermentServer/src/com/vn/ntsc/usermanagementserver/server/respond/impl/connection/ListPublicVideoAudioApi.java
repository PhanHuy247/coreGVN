/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbVideoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.audio.PublicAudio;
import com.vn.ntsc.usermanagementserver.entity.impl.image.PublicImage;
import com.vn.ntsc.usermanagementserver.entity.impl.video.PublicVideo;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ListPublicVideoAudioApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
          ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            List<PublicVideo> listPbVideo;
            List<PublicAudio> listPbAudio;
            List<Object> listFinal = new ArrayList<Object>();
            if (friendId != null) {
                boolean checker = UserDAO.checkUser(friendId);
                if (checker) {
                    if(friendId.isEmpty() || userId==null){
                        listPbVideo = PbVideoDAO.getPublicVideo(friendId, skip, take);
                        listPbAudio = PbAudioDAO.getPublicAudio(friendId, skip, take);
                        listFinal.addAll(listPbVideo);
                        listFinal.addAll(listPbAudio);
                        result = new ListEntityRespond( ErrorCode.SUCCESS, listFinal);
                    }else if (!BlockUserManager.isBlock(userId, friendId)) {
                        listPbVideo = PbVideoDAO.getPublicVideo(friendId, skip, take);
                        listPbAudio = PbAudioDAO.getPublicAudio(friendId, skip, take);
                        listFinal.addAll(listPbVideo);
                        listFinal.addAll(listPbAudio);
                        result = new ListEntityRespond( ErrorCode.SUCCESS, listFinal);
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                }
            } else {
                listPbVideo = PbVideoDAO.getPublicVideo(userId, skip, take);
                listPbAudio = PbAudioDAO.getPublicAudio(friendId, skip, take);
                listFinal.addAll(listPbVideo);
                listFinal.addAll(listPbAudio);
                result = new ListEntityRespond( ErrorCode.SUCCESS, listFinal);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
