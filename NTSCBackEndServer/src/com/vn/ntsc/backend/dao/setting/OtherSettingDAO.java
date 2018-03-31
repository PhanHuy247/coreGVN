/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.setting;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.setting.OtherSetting;
import com.vn.ntsc.backend.entity.impl.setting.VersionSetting;

/**
 *
 * @author RuAc0n
 */
public class OtherSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean update(OtherSetting os) throws EazyException {
        boolean result = false;
        try {
            DBObject searchObj = coll.findOne();
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(SettingdbKey.OTHER_SETTING.UNLOCK_BACKSTAGE_TIME, os.unlockBackstageTime);
            updateObj.append(SettingdbKey.OTHER_SETTING.UNLOCK_VIEW_IMAGE_TIME, os.unlockViewImageTime);
            updateObj.append(SettingdbKey.OTHER_SETTING.UNLOCK_WATCH_VIDEO_TIME, os.unlockWatchVideoTime);
            updateObj.append(SettingdbKey.OTHER_SETTING.UNLOCK_LISTEN_AUDIO_TIME, os.unlockListenAudioTime);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_IMAGE, os.autoApproveImage);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_VIDEO, os.autoApproveVideo);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_BUZZ, os.autoApproveBuzz);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_COMMENT, os.autoApproveComment);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_USER_INFO, os.autoApproveUserInfo);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_IMAGE, os.autoHideReportedImage);
            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_VIDEO, os.autoHideReportedVideo);
//            updateObj.append(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_AUDIO, os.autoHideReportedAudio);
            updateObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY, os.turnOffSafary);
            updateObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION, os.turnOffSafaryVersion);
            updateObj.append(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM, os.loginByMocom);
            updateObj.append(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO, os.turnOffExtendedUserInfo);
            updateObj.append(SettingdbKey.OTHER_SETTING.SHOW_NEWS, os.turnOffShowNews);
            updateObj.append(SettingdbKey.OTHER_SETTING.GET_FREE_POINT, os.getFreePoint);
            updateObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID, os.turnOffBrowser);
            updateObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID_VERSION, os.turnOffBrowserAndroidVersion);
            updateObj.append(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM_ANDROID, os.loginByMocomAndroid);
            updateObj.append(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO_ANDROID, os.turnOffExtendedUserInfoAndroid);
            updateObj.append(SettingdbKey.OTHER_SETTING.SHOW_NEWS_ANDROID, os.turnOffShowNewsAndroid);
            updateObj.append(SettingdbKey.OTHER_SETTING.GET_FREE_POINT_ANDROID, os.getFreePointAndroid);
//            updateObj.append(SettingdbKey.OTHER_SETTING.MAX_LENGTH_BUZZ, os.maxLengthBuzz);
//            updateObj.append(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY_VERSION, os.enterpriseTurnOffSafaryVersion);
//            updateObj.append(SettingdbKey.OTHER_SETTING.ENTERPRISE_LOGIN_BY_MOCOM, os.enterpriseLoginByMocom);
//            updateObj.append(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY, os.enterpriseTurnOffSafary);
//            updateObj.append(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION, os.androidUsableVersion);
//            updateObj.append(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION, os.iosEnterpriseVersion);
//            updateObj.append(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION, os.iosNonEnterpriseVersion);
            DBObject setObject = new BasicDBObject("$set", updateObj);
            coll.update(searchObj, setObject);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean update(VersionSetting vs) throws EazyException {
        boolean result = false;
        try {
            DBObject searchObj = coll.findOne();
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION, vs.androidUsableVersion);
            updateObj.append(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION, vs.iosEnterpriseVersion);
            updateObj.append(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION, vs.iosNonEnterpriseVersion);
            DBObject setObject = new BasicDBObject("$set", updateObj);
            coll.update(searchObj, setObject);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static IEntity getOtherSeting() throws EazyException {
        IEntity result = null;
        try {
            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                OtherSetting os = new OtherSetting();

                int back = searchObj.getInt(SettingdbKey.OTHER_SETTING.UNLOCK_BACKSTAGE_TIME);
                os.unlockBackstageTime = back;
                
                Integer image = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_VIEW_IMAGE_TIME);
                os.unlockViewImageTime = image;
                
                Integer video = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_WATCH_VIDEO_TIME);
                os.unlockWatchVideoTime = video;
                
                Integer audio = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_LISTEN_AUDIO_TIME);
                os.unlockListenAudioTime = audio;
                
                int auto = searchObj.getInt(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_IMAGE);
                os.autoApproveImage = auto;
                
                int autoApprovedVideo = searchObj.getInt(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_VIDEO);
                os.autoApproveVideo = autoApprovedVideo;

                Integer buzz = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_BUZZ);
                os.autoApproveBuzz = buzz;
                Integer comment = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_COMMENT);
                os.autoApproveComment = comment;
                Integer userInfo = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_USER_INFO);
                os.autoApproveUserInfo = userInfo;
                int hide = searchObj.getInt(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_IMAGE);
                os.autoHideReportedImage = hide;
                int hideVideo = searchObj.getInt(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_VIDEO);
                os.autoHideReportedVideo = hideVideo;
                int hideAudio = searchObj.getInt(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_AUDIO);
                os.autoHideReportedAudio = hideAudio;
                String turnOffSafaryVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION);
                os.turnOffSafaryVersion = turnOffSafaryVersion;
                Boolean turnOffSafary = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY);
                os.turnOffSafary = turnOffSafary;
                Boolean loginByMocom = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM);
                os.loginByMocom = loginByMocom;
                Boolean extendedUserInfo = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO);
                os.turnOffExtendedUserInfo = extendedUserInfo;
                Boolean showNews = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.SHOW_NEWS);
                os.turnOffShowNews = showNews;
                
                Boolean getFreePoint = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.GET_FREE_POINT);
                os.getFreePoint = getFreePoint;
                String turnOffBrowserAndroidVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID_VERSION);
                os.turnOffBrowserAndroidVersion = turnOffBrowserAndroidVersion;
                Boolean turnOffBrowserAndroid = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID);
                os.turnOffBrowser = turnOffBrowserAndroid;
                Boolean loginByMocomAndroid = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM_ANDROID);
                os.loginByMocomAndroid = loginByMocomAndroid;
                Boolean extendedUserInfoAndroid = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO_ANDROID);
                os.turnOffExtendedUserInfoAndroid = extendedUserInfoAndroid;
                Boolean showNewsAndroid = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.SHOW_NEWS_ANDROID);
                os.turnOffShowNewsAndroid = showNewsAndroid;
                
                Boolean getFreePointAndroid = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.GET_FREE_POINT_ANDROID);
                os.getFreePointAndroid = getFreePointAndroid;
                
//                Integer maxLengthBuzz = (Integer) searchObj.getInt(SettingdbKey.OTHER_SETTING.MAX_LENGTH_BUZZ);
//                os.maxLengthBuzz = maxLengthBuzz;
                
//                String enterpriseTurnOffSafaryVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY_VERSION);
//                os.enterpriseTurnOffSafaryVersion = enterpriseTurnOffSafaryVersion;
//                Boolean enterpriseTurnOffSafary = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY);
//                os.enterpriseTurnOffSafary = enterpriseTurnOffSafary;
//                Boolean enterpriseLoginByMocom = (Boolean) searchObj.getBoolean(SettingdbKey.OTHER_SETTING.ENTERPRISE_LOGIN_BY_MOCOM);
//                os.enterpriseLoginByMocom = enterpriseLoginByMocom;
//                String androidUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION);
//                os.androidUsableVersion = androidUsableVersion;
//                String iosEnterpriseUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION);
//                os.iosEnterpriseVersion = iosEnterpriseUsableVersion;
//                String iosNonEnterPriseUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION);
//                os.iosNonEnterpriseVersion = iosNonEnterPriseUsableVersion;
                
                result = os;

            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static IEntity getVersionSeting() throws EazyException {
        IEntity result = null;
        try {
            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                VersionSetting os = new VersionSetting();

                String androidUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION);
                os.androidUsableVersion = androidUsableVersion;
                String iosEnterpriseUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION);
                os.iosEnterpriseVersion = iosEnterpriseUsableVersion;
                String iosNonEnterPriseUsableVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION);
                os.iosNonEnterpriseVersion = iosNonEnterPriseUsableVersion;
                
                result = os;

            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
