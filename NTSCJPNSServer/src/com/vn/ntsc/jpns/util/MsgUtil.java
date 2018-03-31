/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.util;

import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.dao.impl.NotificationDAO;
import com.vn.ntsc.jpns.dao.impl.UserDAO;
import com.vn.ntsc.jpns.server.BlockUserManager;
import com.vn.ntsc.jpns.dao.impl.User;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import eazycommon.constant.Constant;

/**
 *
 * @author tuannxv00804
 */
public class MsgUtil {

    private static final String APS = "aps";
    private static final String LOCARGS = "loc-args";
    private static final String NOTI_OWNER_ID = "ownerid";
    private static final String NOTITYPE = "noti_type";
    private static final String LOCKEY = "loc-key";
    private static final String ALERT = "alert";
    private static final String SOUND = "sound";

    private static final String RING_SOUND_FILE = "ring.wav";
    private static final String DEFAULT_SOUND = "default";

    private static final String ALERT_OFF = "alert_off";
    private static final String SOUND_OFF = "sound_off";
    private static final String BADGE = "badge";
    
    private static final String PUSH_CONTENT = "push_content";
    private static final String NOTI_NUM = "noti_num";
    private static final String THUMNAIL_URL = "thumnail_url";

    public static JSONObject iosPayload_reset_pwd(String locKey, String locArgs, String ownerName, String ownerId, boolean isOff) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }
        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");
//        int status = 0;

        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);
        return out;
    }

    public static JSONObject iosPayload_backstage(String locKey, String ownerId, String buzzId, String imageId, boolean isOff) {
        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.NOTI_IMAGE_ID, imageId);
        if (buzzId != null) {
            data.put(ParamKey.NOTI_BUZZ_ID, buzzId);
        }

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        // aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//         if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            
//            }
//         }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_image(String locKey, String ownerId, String buzzId, String imageId, boolean isOff,String avaUrl,String textBuzz,String thumnailImage, Integer gender) {
        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        if(imageId != null)
            data.put(ParamKey.NOTI_IMAGE_ID, imageId);
        if (buzzId != null) {
            data.put(ParamKey.NOTI_BUZZ_ID, buzzId);
        }
        
        data.put("user_name", locArg);
        if(avaUrl != null){
            data.put("ava_url", avaUrl);
        }
        if(textBuzz != null){
            data.put("text_buzz", textBuzz);
        }
        if(thumnailImage != null){
            data.put("thumnail_url", thumnailImage);
        }
        if(gender != null){
            data.put("gender", gender);
        }
        
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_buzz_ForFavoristed(String locKey, String fromName, String ownerId, String buzzId) {
        JSONArray locArg = new JSONArray();
        if (fromName != null) {
            locArg.add(fromName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.NOTI_BUZZ_ID, buzzId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(SOUND, DEFAULT_SOUND);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_buzz_ForFavoristed_new(String locKey, String fromName, String ownerId, String buzzId, boolean isOff) {
        JSONArray locArg = new JSONArray();
        if (fromName != null) {
            locArg.add(fromName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.NOTI_BUZZ_ID, buzzId);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background

        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);
        return out;
    }

    public static JSONObject iosPayload_buzz(String locKey, String fromName, String ownerId, String buzzId, boolean isOff,String avaUrl,String textBuzz,String thumnailImage,Integer gender) {
        JSONArray locArg = new JSONArray();
        if (fromName != null) {
            locArg.add(fromName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.NOTI_BUZZ_ID, buzzId);
        
        data.put("user_name", locArg);
        if(avaUrl != null){
            data.put("ava_url", avaUrl);
        }
        if(textBuzz != null){
            data.put("text_buzz", textBuzz);
        }
        if(thumnailImage != null){
            data.put("thumnail_url", thumnailImage);
        }
        if(gender != null){
            data.put("gender", gender);
        }
        
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);
        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//        Util.addDebugLog("=================status: " + status);
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        int status = 0;
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload(String locKey, String locArgs, String userid, String ownerId) {
        JSONArray locArg = new JSONArray();

        if (locKey != null
                && (locKey.equals(API.NOTI_DAILY_BONUS))) {
            try {
                int point = Integer.parseInt(locArgs);
                locArg.add(point);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        } else {
            if (locArgs != null) {
                locArg.add(locArgs);
            }
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        if (locKey != null && locKey.equals(API.NOTI_PING)) {
            aps.put(SOUND, RING_SOUND_FILE);
        } else {
            aps.put(SOUND, DEFAULT_SOUND);
        }

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_ping(String locKey, String locArgs, String userid, String ownerId, boolean isOff, int typecall) {
        JSONArray locArg = new JSONArray();

        if (locKey != null
                && (locKey.equals(API.NOTI_DAILY_BONUS))) {
            try {
                int point = Integer.parseInt(locArgs);
                locArg.add(point);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        } else {
            if (locArgs != null) {
                locArg.add(locArgs);
            }
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);
        
        //HUNGDT #7185
        data.put("typecall", typecall);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, RING_SOUND_FILE);
        }
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number); 
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_update_user(String locKey, String locArgs, String userid, String ownerId, boolean isOff,String avaUrl,Integer gender) {
        JSONArray locArg = new JSONArray();

        if (locKey != null
                && (locKey.equals(API.NOTI_DAILY_BONUS))) {
            try {
                int point = Integer.parseInt(locArgs);
                locArg.add(point);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        } else {
            if (locArgs != null) {
                locArg.add(locArgs);
            }
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);
        data.put("user_name", locArg);
        if(avaUrl != null){
            data.put("ava_url", avaUrl);
        }
        if(gender != null){
            data.put("gender", gender);
        }

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number); 
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//         int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_alert_online(String locKey, String locArgs, String userid, String ownerId, boolean isOff,String avaUrl, Integer gender) {
        JSONArray locArg = new JSONArray();

        if (locKey != null
                && (locKey.equals(API.NOTI_DAILY_BONUS))) {
            try {
                int point = Integer.parseInt(locArgs);
                locArg.add(point);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        } else {
            if (locArgs != null) {
                locArg.add(locArgs);
            }
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);
        data.put("user_name", locArgs);
        data.put("ava_url", avaUrl);
        if(gender != null){
            data.put("gender", gender);
        }
            

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }
//
        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
//        noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//        aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_daily_bonus(String locKey, String locArgs, String userid, String ownerId, boolean isOff) {
        JSONArray locArg = new JSONArray();

        if (locKey != null
                && (locKey.equals(API.NOTI_DAILY_BONUS))) {
            try {
                int point = Integer.parseInt(locArgs);
                locArg.add(point);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        } else {
            if (locArgs != null) {
                locArg.add(locArgs);
            }
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
        if (locKey != null && locKey.equals(API.NOTI_PING)) {
            aps.put(SOUND, RING_SOUND_FILE);
        } else {
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);

        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//         int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));

//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        aps.put(BADGE, noti_number);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload(String locKey, String locArgs, String userid, int badge, String ownerId) {
        JSONArray locArg = new JSONArray();

        if (locArgs != null) {
            locArg.add(locArgs);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(ParamKey.BADGE, badge);
        aps.put(SOUND, DEFAULT_SOUND);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_chat(String locKey, String locArgs, String userid, String ownerId,String urlAva, boolean isOff, Integer gender) throws EazyException {
        if(!NotificationSettingDAO.checkUserNotification(userid, Constant.NOTIFICATION_TYPE_VALUE.CHAT)){
            return null;
        }
        JSONArray locArg = new JSONArray();

        if (locArgs != null) {
            locArg.add(locArgs);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);
        if(urlAva != null)
            data.put(THUMNAIL_URL, urlAva);
        if(gender != null)
            data.put("gender", gender);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(ParamKey.BADGE, badge);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
//        if (locKey != null && locKey.equals(API.NOTI_PING)) {
//            aps.put(SOUND, RING_SOUND_FILE);
//        } else {
//            aps.put(SOUND, DEFAULT_SOUND);
//        }
////
//        int noti_number=0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
//        noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//        aps.put(BADGE, noti_number);

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);
//        
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        Util.addDebugLog("=================line: " + line);
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload_auto_message(String locKey, String locArgs, String userid, int badge, String ownerId, boolean isOff) {
        JSONArray locArg = new JSONArray();

        if (locArgs != null) {
            locArg.add(locArgs);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(ParamKey.BADGE, badge);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }
//         int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPayload(String locKey, String locArgs, String buzzID, String ownerName, String ownerId) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_BUZZ_ID, buzzID);
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(SOUND, DEFAULT_SOUND);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    //ThanhDD
    public static JSONObject iosPayload_noti_comment_your_buzz(String locKey, String locArgs, String buzzID, String ownerName, 
            String ownerId, boolean isOff,String avaUrl,String contentBuzz,String thumnailImage,Integer gender) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_BUZZ_ID, buzzID);
        data.put(NOTI_OWNER_ID, ownerId);
        if(avaUrl != null)
            data.put("ava_url", avaUrl);
        if(contentBuzz != null)
            data.put("text_buzz", contentBuzz);
        if(thumnailImage != null)
            data.put("thumnail_url", thumnailImage);
        if(gender != null){
            data.put("gender", gender);
        }
        data.put("user_name", locArgs);

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }
    
     public static JSONObject iosPayload_noti_sub_comment_your_buzz(String locKey, String locArgs, String buzzID, String ownerName, 
            String ownerId, boolean isOff,String avaUrl,String contentBuzz,String thumnailImage,String userIdCmtParent, 
            String cmtIdParent,String avaCmtParent,String valueCmtParent, String userNameCmtParent, Integer gender) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_BUZZ_ID, buzzID);
        data.put(NOTI_OWNER_ID, ownerId);
        if(avaUrl != null)
            data.put("ava_url", avaUrl);
        if(contentBuzz != null)
            data.put("text_buzz", contentBuzz);
        if(thumnailImage != null)
            data.put("thumnail_url", thumnailImage);
        data.put("user_name", locArgs);
        data.put("user_id_cmt_parent", userIdCmtParent);
        data.put("cmt_id_parent", cmtIdParent);
        data.put("ava_cmt_parent", avaCmtParent);
        data.put("value_cmt_parent", valueCmtParent);
        data.put("user_name_cmt_parent", userNameCmtParent);
        if(gender != null){
            data.put("gender", gender);
        }

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    // Add by Khanhdd
    public static JSONObject iosPayloadNotiNewBuzzForFavorited(String api, String fromUsername, String fromUserid, String toUserid, String buzzId, boolean isOnPush,String avaUrl,String contentBuzz,String thumnailImage, Integer gender,String streamId) {
        JSONArray locArg = new JSONArray();

        if (fromUsername != null) {
            locArg.add(fromUsername);
        }
        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        if (api.equals("noti_share_music")) {
            String shareMusic = fromUsername + ParamKey.INVITE_LISTEN_MUSIC;
            alert.put(LOCKEY, shareMusic);
        } else if (api.equals(API.NOTI_LIVESTREAM_FROM_FAVOURIST)) {
            String liveStreamFavourist = API.NOTI_LIVESTREAM_FROM_FAVOURIST;
            alert.put(LOCKEY, liveStreamFavourist);
        } else if (api.equals(API.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST)) {
            String tagLiveStream = fromUsername + ParamKey.TAG_LIVESTEAM;
            alert.put(LOCKEY, tagLiveStream);
        } else {
            alert.put(LOCKEY, api);
        }

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(api));
        if (fromUserid != null) {
            data.put(ParamKey.NOTI_USER_ID, fromUserid);
        }
        data.put(NOTI_OWNER_ID, toUserid);
        data.put(ParamKey.NOTI_BUZZ_ID, buzzId);
        data.put(ParamKey.USER_NAME, fromUsername);
        if (avaUrl != null) {
            data.put("ava_url", avaUrl);
        }
        if (contentBuzz != null) {
            data.put("text_buzz", contentBuzz);
        }
        if (thumnailImage != null) {
            data.put("thumnail_url", thumnailImage);
        }
        if (gender != null) {
            data.put("gender", gender);
        }
        if (streamId != null) {
            data.put("stream_id", streamId);
        }
        JSONObject aps = new JSONObject();
        if (isOnPush) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            Util.addDebugLog("=================status on: ");
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }
        int noti_number = 0;
       
        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, toUserid);
        newRq.put(ParamKey.API_NAME, "is_alive");
        Long numberNoti = totalNoti(toUserid);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);

        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);

        JSONObject out = new JSONObject();
        out.put(APS, aps);
        Util.addDebugLog("Test for out JSONObject " + out.toJSONString());
        return out;
    }

    public static JSONObject iosPayload(String locKey, String locArgs, String buzzID, String ownerName, String ownerId, boolean isOff) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_BUZZ_ID, buzzID);
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }    
//         int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }
    
    public static JSONObject iosPayloadForLikeBuzz(String locKey, String locArgs, String buzzID, String ownerName, String ownerId, boolean isOff,String avaUrl,String contentBuzz,String thumnailImage, Integer gender) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        if (ownerName != null) {
            locArg.add(ownerName);
        }
        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_BUZZ_ID, buzzID);
        data.put(NOTI_OWNER_ID, ownerId);
        data.put("user_name", locArgs);
        if(avaUrl != null)
            data.put("ava_url", avaUrl);
        if(contentBuzz != null)
            data.put("text_buzz", contentBuzz);
        if(thumnailImage != null)
            data.put("thumnail_url", thumnailImage);
        if(gender != null)
            data.put("gender", gender);

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//        if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//        }    
//         int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }


    //thanhdd: funcion tinh tong so noti
    public static Long totalNoti(String userId) {
        Long total = 0L;
        try {
//            Util.addDebugLog("----------JSONObject total:" + obj.toJSONString());
//            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
//            MyPageData data = new MyPageData();
//            //data.backstageNumber = new Long(UserDAO.getBackStageNumber(userId));
//            //data.buzzNumber = new Long(UserDAO.getBuzzNumber(userId));
//            Helper.getAttentionNumber(userId, data);
//            List<String> blockUsers = BlockUserManager.getBlackList(userId);
//            Long readTime = UserDAO.getNotificationReadTime(userId);
//            Util.addDebugLog("------------------------------------");
//            Util.addDebugLog("Read Like Notify Time" + readTime);
//            data.notiLikeNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI, blockUsers, readTime));
//            Long readNewsTime = UserDAO.getNewsNotificationReadTime(userId);
//            Util.addDebugLog("Read News Notify Time" + readNewsTime);
//            data.notiNewsNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS, blockUsers, readNewsTime));
//            Util.addDebugLog("------------------notiNewsNumber:"+data.notiNewsNumber);
//            data.notiNumber =  Helper.getNotificationNumber(userId);
//            
//            total = data.notiNumber+ data.notiNewsNumber+data.notiLikeNumber;
//            Util.addDebugLog("------------------------------------");
//            Util.addDebugLog("-------TotalNoti:"+total );

            JSONObject newRq = new JSONObject();
            newRq.put(ParamKey.USER_ID, userId);
            newRq.put(ParamKey.API_NAME, API.GET_MY_PAGE_INFOR);
            JSONObject chat = new JSONObject();
            chat.put(ParamKey.USER_ID, userId);
            chat.put(ParamKey.API_NAME, API.TOTAL_UNREAD);

            String resultChatLog = InterCommunicator.sendRequest(chat.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
            Long chatNumber = Long.parseLong(resultChatLog);
            Util.addDebugLog("-------chat number-----:" + chatNumber);
            Long notiNumber = 0L;
            Long notiLike = 0L;
            Long notiNews = 0L;

            String umsStr = InterCommunicator.sendRequest(newRq.toJSONString(), Config.UMSServerIP, Config.UMSPort);
            JSONObject jo = (JSONObject) new JSONParser().parse(umsStr);
            Long code = (Long) jo.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                // buzz number
                JSONObject usersObj = (JSONObject) jo.get(ParamKey.DATA);
                notiLike = (Long) usersObj.get("noti_like_num");
                Util.addDebugLog("-------notiLike number-----:" + notiLike);
                notiNumber = (Long) usersObj.get("noti_num");
                Util.addDebugLog("-------notiNumber number-----:" + notiNumber);
                notiNews = (Long) usersObj.get("noti_news_num");
                Util.addDebugLog("-------notiNews number-----:" + notiNews);
                total = chatNumber + notiLike + notiNumber + notiNews;
            }
            Util.addDebugLog("-------TotalNoti TOTAL-----:" + total);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return total;
    }

    public static JSONObject iosPayload_point(String locKey, String locArgs, String point, String userid, String ownerId) {
        JSONArray locArg = new JSONArray();
        locArg.add(locArgs);
        try {
            int pointI = Integer.parseInt(point);
            locArg.add(pointI);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(ParamKey.NOTI_USER_ID, userid);
        data.put(NOTI_OWNER_ID, ownerId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(SOUND, DEFAULT_SOUND);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosAutoPush(String locKey, String auto_push_id, String auto_push_title, String ownerId) {
        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put("auto_msg_id", auto_push_id);
        data.put("auto_title", auto_push_title);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(SOUND, DEFAULT_SOUND);

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }

    public static JSONObject iosPushFromFreepage(String locKey, String content, String url, String ownerId, String pushId) {
//        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
//        alert.put(ParamKey.LOCARGS, locArg);
//        alert.put(ParamKey.LOCKEY, locKey);
        alert.put("body", content);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.CONTENT, content);
        data.put(ParamKey.URL, url);
        data.put("push_id", pushId);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(SOUND, DEFAULT_SOUND);

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//         if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//         }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);
        return out;
    }

    public static JSONObject pushNewNotification(Long type, String content, String url, String ownerId, String pushId, boolean isOff) {
//        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
//        alert.put(ParamKey.LOCARGS, locArg);
//        alert.put(ParamKey.LOCKEY, locKey);
        alert.put("body", content);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, type);
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.CONTENT, content);
        data.put(ParamKey.URL, url);
        data.put("push_id", pushId);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//         if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//         }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);
        return out;
    }

    public static JSONObject iosPushFromFreepage_new(String locKey, String content, String url, String ownerId, String pushId, boolean isOff) {
//        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
//        alert.put(ParamKey.LOCARGS, locArg);
//        alert.put(ParamKey.LOCKEY, locKey);
        alert.put("body", content);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.CONTENT, content);
        data.put(ParamKey.URL, url);
        data.put("push_id", pushId);

//        JSONObject aps = new JSONObject();
//        aps.put(ALERT, alert);
//        aps.put(ParamKey.DATA, data);
//        aps.put(SOUND, DEFAULT_SOUND);
        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }

        int noti_number = 0;
//        List<String> blockList = BlockUserManager.getBlackList(ownerId);
//        int andGAlert = Params.getNotiType(locKey);

        //noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
        //aps.put(BADGE, noti_number);
        // Thanhdd: edit for push off and total noti
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");

//        String line = InterCommunicator.sendRequest(newRq.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
//        
//        JSONObject obj = null;
//        try {
//            if (line!=null){
//                obj=(JSONObject) new JSONParser().parse(line);
//            }
//            
//        } catch (Exception e) {
//            Util.addErrorLog(e);
//        }
//        Long status = 0L;
//         if (obj!=null){
//            if (obj.get(ParamKey.DATA)!=null){
//                status =  (Long)obj.get(ParamKey.DATA);
//            }
//         }
//        int status = 0;
        //Nếu client đang chạy background thì trả tổng
        //Nếu client đang active thì trả số thành phần (noti_like)
//        if (status==0) { //Background
        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
//        } else { //Active
//            Util.addDebugLog("=================status active: " + status);
//            noti_number = NotificationDAO.getNotificationNumberByType(ownerId, andGAlert, blockList);
//            Util.addDebugLog("=================noti_number active: " + noti_number);
//        }
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);
        return out;
    }
    
    public static JSONObject iosPayload_miss_call(String locKey, String locArgs, String userid, int badge, String ownerId, String push_content ) {
        JSONArray locArg = new JSONArray();
        if (locArgs != null) {
            locArg.add(locArgs);
        }

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);
        alert.put(PUSH_CONTENT, push_content);
        
        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
        int numberNotiUnread =0;
        if (user!=null && user.userId!=null){
            try {
                numberNotiUnread =  getNotificationNumber(user.userId);
                Util.addDebugLog("===========numberNotiUnread: " + numberNotiUnread);
            } catch (EazyException ex) {
                Logger.getLogger(MsgUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        if (userid != null) {
            data.put(ParamKey.NOTI_USER_ID, userid);
        }
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(NOTI_NUM, numberNotiUnread);

        JSONObject aps = new JSONObject();
        aps.put(ALERT, alert);
        aps.put(ParamKey.DATA, data);
        aps.put(ParamKey.BADGE, badge);
        aps.put(SOUND, DEFAULT_SOUND);
//        aps.put(SOUND, PUSH_SOUND_FILE);// LongLT #21084

        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }
    public static int getNotificationNumber(String userId) throws EazyException {
        List<String> blockUser = BlockUserManager.getBlackList(userId);
//        Collection<String> deactiveUsers = BlackListManager.toList();
        Long readTime = UserDAO.getNotificationReadTime(userId);
        return NotificationDAO.getNotificationNumber(userId, readTime, blockUser);
    }
    
    public static JSONObject iosPayloadForRecordingFile(String locKey, String ownerId, String avaUrl, String buzzId, String imageId, boolean isOff, Integer gender) {
        JSONArray locArg = new JSONArray();

        JSONObject alert = new JSONObject();
        alert.put(LOCARGS, locArg);
        alert.put(LOCKEY, locKey);

        JSONObject data = new JSONObject();
        data.put(NOTITYPE, Params.getNotiType(locKey));
        data.put(NOTI_OWNER_ID, ownerId);
        data.put(ParamKey.NOTI_IMAGE_ID, imageId);
        if(avaUrl != null)
            data.put("ava_url", avaUrl);
        if (buzzId != null) {
            data.put(ParamKey.NOTI_BUZZ_ID, buzzId);
        }
        if (gender != null){
            data.put("gender", gender);
        }

        JSONObject aps = new JSONObject();
        if (isOff) {
            aps.put(ALERT_OFF, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND_OFF, DEFAULT_SOUND);
        } else {
            aps.put(ALERT, alert);
            aps.put(ParamKey.DATA, data);
            aps.put(SOUND, DEFAULT_SOUND);
        }


        int noti_number = 0;

        User user = null;
        try {
            user = UserDAO.getUserInfor(ownerId);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }

        JSONObject newRq = new JSONObject();
        newRq.put(ParamKey.USER_ID, user.userId);
        newRq.put(ParamKey.API_NAME, "is_alive");


        Long numberNoti = totalNoti(user.userId);
        noti_number = (Integer.parseInt(numberNoti.toString()));
        Util.addDebugLog("=================total noti_number: " + noti_number);
        Util.addDebugLog("==========ADD BADGE:============ " + noti_number);
        aps.put(BADGE, noti_number);
        JSONObject out = new JSONObject();
        out.put(APS, aps);

        return out;
    }
}
