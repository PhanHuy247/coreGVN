/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import com.vn.ntsc.buzzserver.Setting;

/**
 *
 * @author RuAc0n
 */
public class DAO {
    
    private static DB buzzDB;
    private static DB userDB;
    private static DB settingDB;

    public static void init() {
        try {
            buzzDB = CommonDAO.mongo.getDB(BuzzdbKey.DB_NAME);
            userDB = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            settingDB = CommonDAO.mongo.getDB(SettingdbKey.DB_NAME);
            initSetting();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }    
    
    public static DB getBuzzDB(){
        return buzzDB;
    }
    
    public static DB getUserDB(){
        return userDB;
    }
    
    public static DB getSettingDB(){
        return settingDB;
    }
    private static void initSetting() {
        try {
            DBCollection coll =  settingDB.getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
            DBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                Integer buzz = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_BUZZ);
                if(buzz != null)
                    Setting.auto_approve_buzz = buzz;
                Integer comment = (Integer) searchObj.get(SettingdbKey.OTHER_SETTING.AUTO_APPROVED_COMMENT);
                if(comment != null)
                    Setting.auto_approve_comment = comment;
            }
            
            DBCollection uploadColl =  settingDB.getCollection(SettingdbKey.UPLOAD_SETTING_COLLECTION);
            DBObject dataObj = (BasicDBObject) uploadColl.findOne();
            if (dataObj != null) {
                String deletedShareImg = (String) dataObj.get(SettingdbKey.UPLOAD_SETTING.SHARE_HAS_DELETED_IMG);
                Setting.share_has_deleted_img = deletedShareImg;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }    

}
