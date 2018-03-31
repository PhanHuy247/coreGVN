/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbVideoDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AddBuzzData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author RuAc0n
 */
public class AddBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String shareId = Util.getStringParam(obj, ParamKey.SHARE_ID);
            String tagList = Util.getStringParam(obj, ParamKey.TAG_LIST);
            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
            UserActivityDAO.updateLastActivity(userId, activityTime);
            Long buzzType = Util.getLongParam(obj, ParamKey.BUZZ_TYPE);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String buzzVal = Util.getStringParam(obj, ParamKey.BUZZ_VALUE);
            Long isApp = Util.getLongParam(obj, ParamKey.IS_APPROVED_IMAGE);
            Long privacy = Util.getLongParam(obj, ParamKey.PRIVACY);
            
            List<String> data = Util.getListString(obj, ParamKey.IMG_LIST);
            List<String> dataVideo = Util.getListString(obj, ParamKey.VID_LIST);
            List<String> dataAudio = Util.getListString(obj, ParamKey.AUDIO_LIST);
            String imgId = "";
            String videoId = "";
            String audioId = "";
            String coverId = "";
            Long imgApp = new Long("0");
            Long videoApp = new Long("0");
            Long audioApp = new Long("0");
            Util.addDebugLog("data--------------------------- "+data);
            Util.addDebugLog("dataVideo--------------------------- "+dataVideo);
            Util.addDebugLog("shareId--------------------------- "+shareId);
            Util.addDebugLog("tagList--------------------------- "+tagList);
            
            if(data != null)
                for (int i = 0; i < data.size(); i++) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(data.get(i));
                    imgId = (String) json.get("data");
                    imgApp = (Long) json.get("is_app");
                    
                    if(buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                        PbImageDAO.addPublicImage(userId, imgId, buzzId, imgApp.intValue(), privacy.intValue());
                        if(isApp == Constant.FLAG.ON){
                            UserDAO.addPbImage(userId);
                        }
                        ImageDAO.updateFlag(imgId, Constant.FLAG.ON);
                    }
                }
            if(dataVideo != null){
                for (int i = 0; i < dataVideo.size(); i++) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(dataVideo.get(i));
                    videoId = (String) json.get("data");
                    videoApp = (Long) json.get("is_app");
                    
                    if(buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                        PbVideoDAO.addPublicVideo(userId, videoId, buzzId, videoApp.intValue(), privacy.intValue());
                        if(isApp == Constant.FLAG.ON){
                            UserDAO.addPbVideo(userId);
                        }
                    }
                }
            }
             if(dataAudio != null){
                for (int i = 0; i < dataAudio.size(); i++) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(dataAudio.get(i));
                    audioId = (String) json.get("data");
                    audioApp = (Long) json.get("is_app");
                    coverId = (String) json.get("cover");
                    
                    if(buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                        PbAudioDAO.addPublicAudio(userId, audioId, buzzId, audioApp.intValue(),coverId, privacy.intValue());
                        if(isApp == Constant.FLAG.ON){
                            UserDAO.addPbAudio(userId);
                        }
                    }
                }
            }
            Util.addDebugLog("imgId--------------------------- "+imgId);
            Util.addDebugLog("imgApp--------------------------- "+imgApp);
            
            
            if (buzzType != null) {
                if (buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS) {
                    if(isApp == Constant.FLAG.ON){
                        UserActivityDAO.updateStatus(userId, buzzVal);
                        UserDAO.addBuzz(userId);
                        UserBuzzDAO.add(buzzId, userId);
                    }
                }else if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS) {
                    String imageId = imgId;
                    PbImageDAO.addPublicImage(userId, imageId, buzzId, imgApp.intValue(), privacy.intValue());
                    if(isApp == Constant.FLAG.ON){
                        UserDAO.addPbImage(userId);
                        UserDAO.addBuzz(userId);
                        UserBuzzDAO.add(buzzId, userId);

                    }
                    ImageDAO.updateFlag(imageId, Constant.FLAG.ON);
                }else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS) {
                    PbVideoDAO.addPublicVideo(userId, videoId, buzzId, videoApp.intValue(), privacy.intValue());
                    if(isApp == Constant.FLAG.ON){
                        UserDAO.addPbVideo(userId);
                        UserDAO.addBuzz(userId);
                        UserBuzzDAO.add(buzzId, userId);

                    }
//                    ImageDAO.updateFlag(imageId, Constant.FLAG.ON);
                }else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS) {
                    PbAudioDAO.addPublicAudio(userId, audioId, buzzId, audioApp.intValue(),coverId, privacy.intValue());
                    if(isApp == Constant.FLAG.ON){
                        UserDAO.addPbAudio(userId);
                        UserDAO.addBuzz(userId);
                        UserBuzzDAO.add(buzzId, userId);

                    }
//                    ImageDAO.updateFlag(imageId, Constant.FLAG.ON);
                }else if (buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS){
                    if(isApp == Constant.FLAG.ON){
                        UserDAO.addBuzz(userId);
                        UserBuzzDAO.add(buzzId, userId);

                    }
                }
            }

            String buzzOwner = null;
            Integer isNoti = null;
            Integer isNotiComment = null;
            List<String> listFavourited = new ArrayList<>();
            if(isApp == Constant.FLAG.ON){
                if(buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                    Long isAppComment = Util.getLongParam(obj, "comment_app");
                    if(isAppComment != null && isAppComment == Constant.FLAG.ON){
                        if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI)) {
//                            Notification ownerNoti = new Notification();
//                            ownerNoti.notiBuzzId = buzzId;
//                            String notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_COMMENT_NOTI);
                            isNotiComment = Constant.FLAG.ON;
//                            NotificationCleaner.put(notificationId, time.getTime());
                        } else {
                            isNotiComment = Constant.FLAG.OFF;
                        }
                    }
                    if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BUZZ_NOTI)) {
//                        Notification noti = new Notification();
//                        noti.notiImageId = imgId;
//                        noti.notiBuzzId = buzzId;
//                        String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_BUZZ_NOTI);
//                        NotificationCleaner.put(notificationId, time.getTime());
                        isNoti = Constant.FLAG.ON;
                    } else {
                        isNoti = Constant.FLAG.OFF;
                    }
                }
                else if(buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                    if (NotificationSettingDAO.checkUserNotification(userId, Constant.NOTIFICATION_TYPE_VALUE.APPROVED_TEXT_BUZZ_NOTI)) {
//                        Notification ownerNoti = new Notification();
//                        ownerNoti.notiBuzzId = buzzId;
//                        String notificationId = NotificationDAO.addNotification(userId, ownerNoti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.APPROVED_TEXT_BUZZ_NOTI);
                        isNoti = Constant.FLAG.ON;
//                        NotificationCleaner.put(notificationId, time.getTime());
                    }else{
                        isNoti = Constant.FLAG.OFF;
                    }
                }
                buzzOwner = UserDAO.getUserName(userId);
                listFavourited = FavoritedDAO.getFavoristIdList(userId);
                Set<String> hs = new HashSet<>();
                hs.addAll(listFavourited);
                listFavourited.clear();
                listFavourited.addAll(hs);
                Tool.removeBlackList(listFavourited, userId);
                
                Notification noti = new Notification();
                
                // ADD NOTIFY TO FAVORISTED LIST
                if(shareId != null){
                    NotificationSettingDAO.listUserNotification(listFavourited, Constant.NOTIFICATION_TYPE_VALUE.SHARE_MUSIC);
                    JSONParser parser = new JSONParser();
                    if(tagList != null){
                        JSONArray listTag = (JSONArray)parser.parse(tagList);
                        listFavourited.clear();
                        listFavourited.addAll(listTag);
                        noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.SHARE_MUSIC;
                    }
                }else{
                    NotificationSettingDAO.listUserNotification(listFavourited, Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI);
                    noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.NEW_BUZZ_FROM_FAVORIST_NOTI;
                }
                noti.fromNotiUserId = userId;
                noti.notiBuzzId = buzzId;
                noti.notiUserName = buzzOwner;
                List<String> notificationIds = NotificationDAO.addNotifications(listFavourited, noti, time.getTime(), noti.notiType);
                for(String notificationId : notificationIds){
                    NotificationCleaner.put(notificationId, time.getTime());
                }
            }
            result = new EntityRespond(ErrorCode.SUCCESS, new AddBuzzData(isNoti, listFavourited, buzzOwner, isNotiComment));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }
    
    public static void updateBuzzData(){
        
    }

}
