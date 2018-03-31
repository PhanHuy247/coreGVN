/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.server.Setting;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.constant.mongokey.ChatlogdbKey;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import java.util.ArrayList;

/**
 *
 * @author Rua
 */
public class DBManager {
    
    private static final DB settingDB;
    private static final DB buzzDB;
    private static final DB chatDB;
    private static final DB chatExtensionDB;
    private static final DB cmCodeDB;
    private static final DB staticFileDB;
    private static final DB stampDB;
    private static final DB logDB;
    private static final DB cashDB;
    private static final DB statisticDB;
    private static final DB userDB;
    
    static {
        userDB = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        statisticDB = CommonDAO.mongo.getDB(StatisticdbKey.DB_NAME);
        cashDB = CommonDAO.mongo.getDB(CashdbKey.DB_NAME);
        logDB = CommonDAO.mongo.getDB(LogdbKey.DB_NAME);
        stampDB = CommonDAO.mongo.getDB(StampdbKey.DB_NAME);
        staticFileDB = CommonDAO.mongo.getDB(StaticFiledbKey.DB_NAME);
        cmCodeDB = CommonDAO.mongo.getDB(CMcodedbKey.DB_NAME);
        chatExtensionDB = CommonDAO.mongo.getDB(ChatlogdbKey.DB_EXTENSION);
        chatDB = CommonDAO.mongo.getDB( ChatlogdbKey.DB_NAME );        
        buzzDB = CommonDAO.mongo.getDB(BuzzdbKey.DB_NAME);
        settingDB = CommonDAO.mongo.getDB( SettingdbKey.DB_NAME );        
    }

    public static DB getSettingDB() {
        return settingDB;
    }

    public static DB getBuzzDB() {
        return buzzDB;
    }

    public static DB getChatDB() {
        return chatDB;
    }

    public static DB getChatExtensionDB() {
        return chatExtensionDB;
    }

    public static DB getCmCodeDB() {
        return cmCodeDB;
    }

    public static DB getStaticFileDB() {
        return staticFileDB;
    }

    public static DB getStampDB() {
        return stampDB;
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
    
    public static void initSetting() {
        try {
            DBCollection coll = settingDB.getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
            DBObject searchObj = coll.findOne();
            if (searchObj != null) {
                Integer autoHide = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_IMAGE);
                Setting.AUTO_HIDE_REPORTED_IMAGE = autoHide;
                Integer autoHideVideo = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_HIDE_REPORTED_VIDEO);
                Setting.AUTO_HIDE_REPORTED_VIDEO = autoHideVideo;
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
}
