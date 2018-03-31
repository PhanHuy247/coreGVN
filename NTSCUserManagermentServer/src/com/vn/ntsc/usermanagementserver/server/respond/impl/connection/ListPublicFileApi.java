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
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.PublicFile;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ListPublicFileApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Long type = (Long) obj.get(ParamKey.TYPE);
            List<PublicFile> listPbFile = new LinkedList<>();
            
            List<PublicFile> listPbImage = new ArrayList<>();
            List<PublicFile> listPbVideo = new ArrayList<>();
            List<PublicFile> listPbAudio = new ArrayList<>();
            if (friendId != null && !friendId.equals(userId)) {
                boolean checker = UserDAO.checkUser(friendId);
                if (checker) {
                    if(friendId.isEmpty() || userId==null){
                        listPbImage = PbImageDAO.getTimelineImage(friendId, false);
                        listPbVideo = PbVideoDAO.getTimelineVideo(friendId, false);
                        listPbAudio = PbAudioDAO.getTimelineAudio(friendId, false);
                    }else if (!BlockUserManager.isBlock(userId, friendId)) {
                        listPbImage = PbImageDAO.getTimelineImage(friendId, false);
                        listPbVideo = PbVideoDAO.getTimelineVideo(friendId, false);
                        listPbAudio = PbAudioDAO.getTimelineAudio(friendId, false);
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                        return result;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                    return result;
                }
            } else {
                listPbImage = PbImageDAO.getTimelineImage(userId, true);
                listPbVideo = PbVideoDAO.getTimelineVideo(userId, true);
                listPbAudio = PbAudioDAO.getTimelineAudio(userId, true);
            }
            
            if(type == null || type == 0){
                listPbFile.addAll(listPbImage);
                listPbFile.addAll(listPbVideo);
                listPbFile.addAll(listPbAudio);
            }else if(type == 1){
                listPbFile.addAll(listPbImage);
            }else if(type == 2){
                listPbFile.addAll(listPbVideo);
                listPbFile.addAll(listPbAudio);
            }
            
            Collections.sort(listPbFile);
                
            Integer startIndex = skip.intValue();
            Integer endIndex = skip.intValue() + take.intValue();
            if (endIndex > listPbFile.size()) {
                endIndex = listPbFile.size();
            }
            List<PublicFile> data = listPbFile.subList(startIndex, endIndex);
            result = new ListEntityRespond(ErrorCode.SUCCESS, data);
            
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
