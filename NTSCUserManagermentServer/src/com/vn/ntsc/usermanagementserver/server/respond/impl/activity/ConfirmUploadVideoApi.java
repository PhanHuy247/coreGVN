/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ChatImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.VideoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.image.ConfirmUploadImageData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.entity.impl.video.ConfirmUploadVideoData;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import eazycommon.constant.Constant;
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
public class ConfirmUploadVideoApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String videoId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
//            String category = Util.getStringParam(obj, "video_cat");
//            Long auto = Util.getLongParam(obj, "auto_approved_video");
            Long uploadTime = Util.getLongParam(obj, ParamKey.TIME);
            int videoType = Constant.VIDEO_TYPE_VALUE.VIDEO_PUBLIC;
            int status = Util.getIntParam(obj, "auto_approved_video");
            int flag = Constant.FLAG.ON;            
            //1. insert to image collection
            Video video = new Video(userId, videoId, videoType, status, flag,uploadTime);
            VideoDAO.insertVideo(video);

            
            List<String> listFavourited = new ArrayList<>();
            listFavourited = FavoritedDAO.getFavoristIdList(userId);
            Tool.removeBlackList(listFavourited, userId);
            //5. set result
//            int point = UserInforManager.getPoint(userId);
//            int point = 0;
            ConfirmUploadVideoData data = new ConfirmUploadVideoData(status);
            data.lstFav = listFavourited;
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
