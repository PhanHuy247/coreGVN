/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.PointPacketDAO;
import eazycommon.constant.mongokey.BuzzdbKey;

/**
 *
 * @author RuAc0n
 */
public class DBLoader {

    private static final DB settingDB;
    private static final DB cmCodeDB;
    private static final DB stampDB;
    private static final DB logDB;
    private static final DB cashDB;
    private static final DB statisticDB;
    private static final DB staticFileDB;
    private static final DB userDB;
    private static final DB buzzDB;
    
    static {
        userDB = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        statisticDB = CommonDAO.mongo.getDB(StatisticdbKey.DB_NAME);
        cashDB = CommonDAO.mongo.getDB(CashdbKey.DB_NAME);
        logDB = CommonDAO.mongo.getDB(LogdbKey.DB_NAME);
        stampDB = CommonDAO.mongo.getDB(StampdbKey.DB_NAME);
        cmCodeDB = CommonDAO.mongo.getDB(CMcodedbKey.DB_NAME);
        settingDB = CommonDAO.mongo.getDB( SettingdbKey.DB_NAME );        
        staticFileDB = CommonDAO.mongo.getDB( StaticFiledbKey.DB_NAME);       
        buzzDB = CommonDAO.mongo.getDB( BuzzdbKey.DB_NAME );
    }

    public static DB getSettingDB() {
        return settingDB;
    }

    public static DB getCmCodeDB() {
        return cmCodeDB;
    }

    public static DB getStampDB() {
        return stampDB;
    }

    public static DB getLogDB() {
        return logDB;
    }

    public static DB getBuzzDB() {
        return buzzDB;
    }

    public static DB getCashDB() {
        return cashDB;
    }

    public static DB getStatisticDB() {
        return statisticDB;
    }

    public static DB getUserDB() {
        return userDB;
    }
    
    public static DB getStaticFileDB() {
        return staticFileDB;
    }
    
    public static void init() {
        try {

            initStickerCategory();
            initDistanceSetting();
            initOtherSetting();
            initShakeChatSetting();
            initPointSetting();
            initCommunicationSetting();
            initConectionPointSetting();

            initSetting();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void initSetting() {
        try {
            DBCollection coll = settingDB.getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);

            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                Integer auto = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_IMAGE);
                Setting.auto_approved_image = auto;
                Integer autoAppVideo = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_VIDEO);
                Setting.auto_approved_video = autoAppVideo;
                String turnOffSafaryVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION);
                if(turnOffSafaryVersion != null)
                    Setting.turnOffSafaryVersion = turnOffSafaryVersion;
                Boolean turnOffSafary = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY);
                if(turnOffSafary != null)
                    Setting.turnOffSafary = turnOffSafary;
                Boolean loginByMocom = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM);
                if(loginByMocom != null)
                    Setting.turnOffLoginByMocom = loginByMocom;
                Boolean extendedUserInfo = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO);
                if(extendedUserInfo != null)
                    Setting.turnOffExtendedUserInfo = extendedUserInfo;
                Boolean showNews = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.SHOW_NEWS);
                if(showNews != null)
                    Setting.turnOffShowNews = showNews;
                Boolean getFreePoint = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.GET_FREE_POINT);
                if(getFreePoint != null)
                    Setting.turnOffGetFreePoint = getFreePoint;
                
                String turnOffBrowserVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID_VERSION);
                if(turnOffBrowserVersion != null)
                    Setting.turnOffBrowserAndroidVersion = turnOffBrowserVersion;
                Boolean turnOffBrowser = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_BROWSER_ANDROID);
                if(turnOffBrowser != null)
                    Setting.turnOffBrowserAndroid = turnOffBrowser;
                Boolean loginByMocomAndroid = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.LOGIN_BY_MOCOM_ANDROID);
                if(loginByMocomAndroid != null)
                    Setting.turnOffLoginByMocomAndroid = loginByMocomAndroid;
                Boolean extendedUserInfoAndroid = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.EXTENDED_USER_INFO_ANDROID);
                if(extendedUserInfoAndroid != null)
                    Setting.turnOffExtendedUserInfoAndroid = extendedUserInfoAndroid;
                Boolean showNewsAndroid = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.SHOW_NEWS_ANDROID);
                if(showNewsAndroid != null)
                    Setting.turnOffShowNewsAndroid = showNewsAndroid;
                Boolean getFreePointAndroid = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.GET_FREE_POINT_ANDROID);
                if(getFreePointAndroid != null)
                    Setting.turnOffGetFreePointAndroid = getFreePointAndroid;
//                String enterpriseTurnOffSafaryVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY_VERSION);
//                if(enterpriseTurnOffSafaryVersion != null)
//                    Setting.enterpriseturnOffSafaryVersion = enterpriseTurnOffSafaryVersion;
//                Boolean enterpriseTurnOffSafary = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.ENTERPRISE_TURN_OFF_SAFARY);
//                if(enterpriseTurnOffSafary != null)
//                    Setting.enterpriseTurnOffSafary = enterpriseTurnOffSafary;
//                Boolean enterpriseLoginByMocom = (Boolean) searchObj.get(SettingdbKey.OTHER_SETTING.ENTERPRISE_LOGIN_BY_MOCOM);
//                if(enterpriseLoginByMocom != null)
//                    Setting.enterpriseTurnOffLoginByMocom = enterpriseLoginByMocom;

                Setting.android_usable_version = searchObj.getString(SettingdbKey.OTHER_SETTING.ANDROID_USABLE_VERSION);
                Setting.ios_non_enterprise_usable_version = searchObj.getString(SettingdbKey.OTHER_SETTING.IOS_NON_ENTERPRISE_USABLE_VERSION);
                Setting.ios_enterprise_usable_version = searchObj.getString(SettingdbKey.OTHER_SETTING.IOS_ENTERPRISE_USABLE_VERSION);
                
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
       
    }

    private static void initDistanceSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.DISTANCE_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.DISTANCE_SETTING_COLLECTION);
            BasicDBObject insertObj = new BasicDBObject();
            BasicDBList list = new BasicDBList();
            list.add(50.0);
            list.add(100.0);
            list.add(500.0);
            list.add(2000.0);
            insertObj.append(SettingdbKey.DISTANCE_SETTING.DISTANCE, list);
            insertObj.append(SettingdbKey.DISTANCE_SETTING.LOCAL_BUZZ, 200.0);
            coll.insert(insertObj);
        }
    }

    private static void initStickerCategory() throws EazyException {
        if (!stampDB.collectionExists(StampdbKey.STICKER_CATEGORY_COLLECTION)) {
            DBCollection coll = stampDB.getCollection(StampdbKey.STICKER_CATEGORY_COLLECTION);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, "Default Category");
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, "Default Category");
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, "Default Category");
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, "Default Category");
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, 0);
            obj.append(StampdbKey.STICKER_CATEGORY.ORDER, 1);
            obj.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, 0);
            coll.insert(obj);
        }
    }

    private static void initShakeChatSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.SHAKE_CHAT_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.SHAKE_CHAT_SETTING_COLLECTION);
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.TYPE, 2);
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.INTERES_IN, 0);
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.DISTANCE, 0);
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.LOWER_AGE, 18);
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.UPPER_AGE, 120);
            insertObj.append(SettingdbKey.SHAKE_CHAT_SETTING.ETHINICITY, 0);
            coll.insert(insertObj);
        }
    }

    private static void initOtherSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.OTHER_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
            BasicDBObject insertObj = new BasicDBObject();
//            insertObj.append(DBParamKey.OTHER_SETTING.WINK_BOMB_NUMBER, 100);
//            insertObj.append(DBParamKey.OTHER_SETTING.LOOK_TIME, 6);
//            insertObj.append(DBParamKey.OTHER_SETTING.UNLOCK_CHECK_TIME, 24);
//            insertObj.append(DBParamKey.OTHER_SETTING.UNLOCK_FAVORITED_TIME, 24);
            insertObj.append(SettingdbKey.OTHER_SETTING.UNLOCK_BACKSTAGE_TIME, 24);
            insertObj.append(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_IMAGE, 1);
            insertObj.append(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_IMAGE, 1);
            insertObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY, false);
            insertObj.append(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION, "1.5");
            coll.insert(insertObj);
        }
    }

    private static void initPointSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.POINT_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.POINT_SETTING_COLLECTION);
            for (String type : DAOKeys.one_object_minus_list) {

                // one_object_minus_list --> list type add untradable point 
                BasicDBObject insertObj = new BasicDBObject();

                insertObj.append(SettingdbKey.POINT_SETTING.TYPE, type);
                insertObj.append(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT, 1);
                insertObj.append(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT, 2);

                coll.insert(insertObj);
            }

            for (String type : DAOKeys.one_object_add_list) {

                // one_object_add_list --> list type add untradable point or tradable point
                BasicDBObject insertObj = new BasicDBObject();

                insertObj.append(SettingdbKey.POINT_SETTING.TYPE, type);
                insertObj.append(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT, 1);
                insertObj.append(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT, 2);
                coll.insert(insertObj);
            }            
            
        }
    }
    
    private static void initCommunicationSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.COMMUNICATION_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.COMMUNICATION_SETTING_COLLECTION);
            for(String type : DAOKeys.communication_type_list){
                for(String pairs: DAOKeys.pairs_list){
                    BasicDBObject insertObj = new BasicDBObject();
                    insertObj.append(SettingdbKey.COMMUNICATION_SETTING.TYPE, type);
                    insertObj.append(SettingdbKey.COMMUNICATION_SETTING.CALLER_RECEIVER, pairs);
                    
                    insertObj.append(SettingdbKey.COMMUNICATION_SETTING.CALLER, 1);
                    
                    insertObj.append(SettingdbKey.COMMUNICATION_SETTING.RECEIVER, 1); 
                    
                    coll.insert(insertObj);
                }
            }
        }
    }
    
    private static void initConectionPointSetting() throws EazyException {
        if (!settingDB.collectionExists(SettingdbKey.CONNECTION_POINT_SETTING_COLLECTION)) {
            DBCollection coll = settingDB.getCollection(SettingdbKey.CONNECTION_POINT_SETTING_COLLECTION);
            for(String type : DAOKeys.connection_type_list){
                for(String pairs: DAOKeys.pairs_list){
                    BasicDBObject insertObj = new BasicDBObject();
                    insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.TYPE, type);
                    insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.SENDER_RECEIVER, pairs);

                    insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.SENDER, 1);

                    insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER, 1); 
                    if(!pairs.equals(DAOKeys.female_female)){
                        insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER, 1);
                        insertObj.append(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER, 1);
                    }
                    coll.insert(insertObj);
                }
            }
        }
    }    
    

}
