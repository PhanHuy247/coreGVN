/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao;

import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.util.Util;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.LogdbKey;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.ChatlogdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import java.util.ArrayList;
import eazycommon.dao.CommonDAO;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.Price;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class DatabaseLoader {

    private static final DB logDB;
    private static final DB cashDB;
    private static final DB statisticDB;
    private static final DB userDB;
    private static final DB settingDB;
    private static final DB buzzDB;
    private static final DB staticDB;
    private static DB dbExtension;

    static {
        userDB = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        statisticDB = CommonDAO.mongo.getDB(StatisticdbKey.DB_NAME);
        cashDB = CommonDAO.mongo.getDB(CashdbKey.DB_NAME);
        logDB = CommonDAO.mongo.getDB(LogdbKey.DB_NAME);
        settingDB = CommonDAO.mongo.getDB(SettingdbKey.DB_NAME);
        buzzDB = CommonDAO.mongo.getDB(BuzzdbKey.DB_NAME);
        staticDB = CommonDAO.mongo.getDB(StaticFiledbKey.DB_NAME);
        dbExtension = CommonDAO.mongo.getDB(ChatlogdbKey.DB_EXTENSION);
    }

    public static DB getStaticDB() {
        return staticDB;
    }

    public static DB getSettingDB() {
        return settingDB;
    }

    public static DB getLogDB() {
        return logDB;
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

    public static DB getBuzzDB() {
        return buzzDB;
    }
     public static DB getChatLogExtensionDB() {
        return dbExtension;
    }

    private static void initSetting() {
        try {
            DBCollection coll = settingDB.getCollection(SettingdbKey.DISTANCE_SETTING_COLLECTION);

            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                Double localDistance = (Double) searchObj.get(SettingdbKey.DISTANCE_SETTING.LOCAL_BUZZ);
                Setting.LOCAL_DISTANCE = localDistance;
                Setting.DISTANCE = (ArrayList<Double>) searchObj.get(SettingdbKey.DISTANCE_SETTING.DISTANCE);
                Setting.DISTANCE.remove(2);
            }

            coll = settingDB.getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
            searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                Integer back = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_BACKSTAGE_TIME);
                Setting.BACKSTAGE_TIME = back;
                Integer image = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_VIEW_IMAGE_TIME);
                if (image != null) {
                    Setting.VIEW_IMAGE_TIME = image;
                }
                Integer video = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_WATCH_VIDEO_TIME);
                if (video != null) {
                    Setting.WATCH_VIDEO_TIME = video;
                }
                Integer audio = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.UNLOCK_LISTEN_AUDIO_TIME);
                if (audio != null) {
                    Setting.LISTEN_AUDIO_TIME = audio;
                }
                Integer autoHide = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_IMAGE);
                Setting.AUTO_HIDE_REPORTED_IMAGE = autoHide;
                Integer autoHideVideo = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_VIDEO);
                Setting.AUTO_HIDE_REPORTED_VIDEO = autoHideVideo;
                Integer autoHideAudio = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_AUDIO);
                Setting.AUTO_HIDE_REPORTED_AUDIO = autoHideAudio;
                Integer useInfo = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_USER_INFO);
                if (useInfo != null) {
                    Setting.AUTO_APPROVE_REVIEW_USER = useInfo;
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    private static void initActionPrice() {
        DBCollection coll = settingDB.getCollection(SettingdbKey.POINT_SETTING_COLLECTION);

        DBCursor cursor = coll.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String type = (String) obj.get(SettingdbKey.POINT_SETTING.TYPE);
            Integer malePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT);
            Integer potentialCustomerMalePrice = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT);
            Integer femalePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT);
            Integer potentialCustomerFemalePrice = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT);
            Integer malePartnerPoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT);
//            Integer potentialCustomerMalePartnerPrice = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT);           
            Integer femalePartnerPoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT);
//            Integer potentialCustomerFemalePartnerPrice = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT);

            Price price = new Price(malePoint, femalePoint, malePartnerPoint, femalePartnerPoint,
                    potentialCustomerMalePrice, potentialCustomerFemalePrice);
//            System.out.println("DatabaseLoader --> type :" + type + ", price :" + price.toString());
            try {
                ActionManager.put(ActionType.valueOf(type), price);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }

        coll = settingDB.getCollection(SettingdbKey.COMMUNICATION_SETTING_COLLECTION);
        try {
            for (String type : DAOKeys.communication_type_list) {
                JSONObject data = new JSONObject();
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.COMMUNICATION_SETTING.TYPE, type);
                cursor = coll.find(findObj);
                if (cursor.size() > 0) {
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        String pairs = (String) obj.get(SettingdbKey.COMMUNICATION_SETTING.CALLER_RECEIVER);

                        String[] elements = pairs.split("_");

                        JSONObject dataJson = new JSONObject();

                        JSONObject receiverJson = new JSONObject();
                        Integer value = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.RECEIVER);
                        if (value == null) {
                            value = 0;
                        }
                        receiverJson.put(DAOKeys.name, elements[1]);
                        receiverJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.RECEIVER, receiverJson);

                        JSONObject potentialReceiverJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
                        if (value == null) {
                            value = 0;
                        }
                        potentialReceiverJson.put(DAOKeys.name, elements[1]);
                        potentialReceiverJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER, potentialReceiverJson);

                        JSONObject callerJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.CALLER);
                        if (value == null) {
                            value = 0;
                        }
                        callerJson.put(DAOKeys.name, elements[0]);
                        callerJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.CALLER, callerJson);

                        JSONObject potentialCallerJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER);
                        if (value == null) {
                            value = 0;
                        }
                        potentialCallerJson.put(DAOKeys.name, elements[0]);
                        potentialCallerJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER, potentialCallerJson);

                        data.put(pairs, dataJson);

                    }
                } else {
                    for (String pairs : DAOKeys.pairs_list) {
                        String[] elements = pairs.split("_");

                        JSONObject dataJson = new JSONObject();

                        JSONObject receiverJson = new JSONObject();
                        receiverJson.put(DAOKeys.name, elements[1]);
                        receiverJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.RECEIVER, receiverJson);

                        JSONObject potentialReceiverJson = new JSONObject();
                        potentialReceiverJson.put(DAOKeys.name, elements[1]);
                        potentialReceiverJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER, potentialReceiverJson);

                        JSONObject callerJson = new JSONObject();
                        callerJson.put(DAOKeys.name, elements[0]);
                        callerJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.CALLER, callerJson);

                        JSONObject potentialCallerJson = new JSONObject();
                        potentialCallerJson.put(DAOKeys.name, elements[0]);
                        potentialCallerJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER, potentialCallerJson);

                        data.put(pairs, dataJson);
                    }
                }
                ActionManager.communicationPointSetting.put(type, data);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        coll = settingDB.getCollection(SettingdbKey.CONNECTION_POINT_SETTING_COLLECTION);
        try {
            for (String type : DAOKeys.connection_type_list) {
                JSONObject data = new JSONObject();
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.CONNECTION_POINT_SETTING.TYPE, type);
                cursor = coll.find(findObj);
                if (cursor.size() > 0) {
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        String pairs = (String) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER_RECEIVER);

                        String[] elements = pairs.split("_");

                        JSONObject dataJson = new JSONObject();

                        JSONObject receiverJson = new JSONObject();
                        Integer value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER);
                        if (value == null) {
                            value = 0;
                        }
                        receiverJson.put(DAOKeys.name, elements[1]);
                        receiverJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER, receiverJson);

                        JSONObject potentialReceiverJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
                        if (value == null) {
                            value = 0;
                        }
                        potentialReceiverJson.put(DAOKeys.name, elements[1]);
                        potentialReceiverJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER, potentialReceiverJson);

                        JSONObject callerJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER);
                        if (value == null) {
                            value = 0;
                        }
                        callerJson.put(DAOKeys.name, elements[0]);
                        callerJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.SENDER, callerJson);

                        JSONObject potentialCallerJson = new JSONObject();
                        value = (Integer) obj.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER);
                        if (value == null) {
                            value = 0;
                        }
                        potentialCallerJson.put(DAOKeys.name, elements[0]);
                        potentialCallerJson.put(DAOKeys.value, value);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER, potentialCallerJson);

                        data.put(pairs, dataJson);

                    }
                } else {
                    for (String pairs : DAOKeys.pairs_list) {
                        String[] elements = pairs.split("_");

                        JSONObject dataJson = new JSONObject();

                        JSONObject receiverJson = new JSONObject();
                        receiverJson.put(DAOKeys.name, elements[1]);
                        receiverJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER, receiverJson);

                        JSONObject potentialReceiverJson = new JSONObject();
                        potentialReceiverJson.put(DAOKeys.name, elements[1]);
                        potentialReceiverJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER, potentialReceiverJson);

                        JSONObject callerJson = new JSONObject();
                        callerJson.put(DAOKeys.name, elements[0]);
                        callerJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.SENDER, callerJson);

                        JSONObject potentialCallerJson = new JSONObject();
                        potentialCallerJson.put(DAOKeys.name, elements[0]);
                        potentialCallerJson.put(DAOKeys.value, 0);
                        dataJson.put(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER, potentialCallerJson);

                        data.put(pairs, dataJson);
                    }
                }
                ActionManager.connectionPointSetting.put(type, data);
            }
            System.out.println("connect : " + ActionManager.connectionPointSetting.toJSONString());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void init() {
        initSetting();
        initActionPrice();
    }
}
