/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ChatImageDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.image.ConfirmUploadImageData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class ConfirmUploadImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            String category = Util.getStringParam(obj, "img_cat");
            Long auto = Util.getLongParam(obj, "auto_approved_img");
//            String ip = Util.getStringParam(obj, ParamKey.IP);
            int imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC;
            int avaFlag = Constant.FLAG.OFF;
            int appFlag = Constant.FLAG.OFF;
            int status = Constant.REVIEW_STATUS_FLAG.PENDING;
            int isAva = Constant.FLAG.OFF;
            int flag = Constant.FLAG.ON;            
            if (category.equals(Constant.IMAGE_KIND_STRING.BACKSTAGE)) {
                BackstageDAO.addBackStage(userId, imageId, auto.intValue());
                if (auto.intValue() == Constant.FLAG.ON) {
                    UserDAO.addBackstage(userId);
                    if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BACKSTAGE_NOTI)) {
                        Notification noti = new Notification();
                        noti.notiImageId = imageId;
                        String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BACKSTAGE_NOTI);
                        NotificationCleaner.put(notificationId, time.getTime());
                    }
                }
                imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE;
            } else if (category.equals(Constant.IMAGE_KIND_STRING.CHAT_IMAGE)) {
                ChatImageDAO.addChatImage(userId, imageId);
                imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_CHAT;
                appFlag = Constant.FLAG.ON;
                status = Constant.REVIEW_STATUS_FLAG.APPROVED;
            } else {
                if (category.equals(Constant.IMAGE_KIND_STRING.AVATAR)) {
                    ImageDAO.removeAvatar(userId);                    
                    if (auto == Constant.FLAG.ON) {
                        isAva = Constant.FLAG.ON;
                        UserDAO.updateAvatar(userId, imageId, time);
                    }
                    avaFlag = Constant.FLAG.ON;
                }
                flag = Constant.FLAG.OFF;
            }
            //1. insert to image collection
            if (auto == Constant.FLAG.ON) {
                appFlag = Constant.FLAG.ON;
                status = Constant.REVIEW_STATUS_FLAG.APPROVED;
            }
            Integer gender = (Integer) UserDAO.getUserInfor(userId, ParamKey.GENDER);
            Image image = new Image(userId, imageId, imageType, status, avaFlag, appFlag, flag, time.getTime(), Constant.FLAG.ON, gender);
            image.deniedFlag = Constant.FLAG.OFF;
            ImageDAO.insertImage(image);
//            if (category.equals(Constant.AVATAR)) {
//                if (auto == Constant.YES) {
//                    ImageDAO.updateAvatarFlag(userId, imageId, Constant.YES);
//                }
//            }
//            ImageDAO.updateAvatarFlag(userId, imageId, Constant.YES);
            if (auto == Constant.FLAG.ON) {
                ImageDAO.updateReviewTime(imageId, time.getTime());
            }
            //5. set result
            int point = UserInforManager.getPoint(userId);
            ConfirmUploadImageData data = new ConfirmUploadImageData(auto, imageType, (long) isAva, point);
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
