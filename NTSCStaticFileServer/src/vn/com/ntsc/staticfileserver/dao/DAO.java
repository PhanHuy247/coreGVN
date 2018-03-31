/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.server.Setting;

/**
 *
 * @author RuAc0n
 */
public class DAO {
    
    private static DB statisticDB;
    private static DB userDB;
    private static DB settingDB;
    private static DB buzzDB;
    private static DB stampDB;
    
    public static void init() {
        try {
            statisticDB = CommonDAO.mongo.getDB(StaticFiledbKey.DB_NAME);
            userDB = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            settingDB = CommonDAO.mongo.getDB(SettingdbKey.DB_NAME);
            buzzDB = CommonDAO.mongo.getDB(BuzzdbKey.DB_NAME);
            stampDB = CommonDAO.mongo.getDB(StampdbKey.DB_NAME);
            initSetting();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    } 

    public static DB getStatisticDB() {
        return statisticDB;
    }

    public static DB getUserDB() {
        return userDB;
    }
    
    public static DB getBuzzDB(){
        return buzzDB;
    }
    
    public static DB getStampDB(){
        return stampDB;
    }
    
    private static void initSetting() {
        try {
            DBCollection coll =  settingDB.getCollection(SettingdbKey.UPLOAD_SETTING_COLLECTION);
            DBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                Integer maxFileSize = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_FILE_SIZE);
                Setting.max_file_size = maxFileSize;

                Integer totalFileSize = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_SIZE);
                Setting.total_file_size = totalFileSize;

                Integer imageFileNumber = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_IMAGE_FILE);
                Setting.max_image_number = imageFileNumber;

                Integer videoFileNumber = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_VIDEO_FILE);
                Setting.max_video_number = videoFileNumber;

                Integer audioFileNumber = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_AUDIO_FILE);
                Setting.max_audio_number = audioFileNumber;
                
                Integer totalFileUpload = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_UPLOAD);
                Setting.total_file_upload = totalFileUpload;
                
                String defaultAudioImg = (String) searchObj.get(SettingdbKey.UPLOAD_SETTING.DEFAULT_AUDIO_IMG);
                Setting.default_audio_img = defaultAudioImg;
                
                Integer maxFilePerAlbum  = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_FILE_PER_ALBUM);
                Setting.max_file_per_album = maxFilePerAlbum;
                
                String deletedShareImg = (String) searchObj.get(SettingdbKey.UPLOAD_SETTING.SHARE_HAS_DELETED_IMG);
                Setting.share_has_deleted_img = deletedShareImg;
                
                Integer maxLengthBuzz = (Integer) searchObj.get(SettingdbKey.UPLOAD_SETTING.MAX_LENGTH_BUZZ);
                Setting.max_length_buzz = maxLengthBuzz;
            }else{
                DBObject dbObject = new BasicDBObject();
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_FILE_SIZE, 100000);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_SIZE, 500000);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_IMAGE_FILE, 20);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_VIDEO_FILE, 20);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_AUDIO_FILE, 20);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_UPLOAD, 20);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.DEFAULT_AUDIO_IMG, "");
                dbObject.put(SettingdbKey.UPLOAD_SETTING.SHARE_HAS_DELETED_IMG, "");
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_FILE_PER_ALBUM, 30);
                dbObject.put(SettingdbKey.UPLOAD_SETTING.MAX_LENGTH_BUZZ, 60000);
                
                coll.insert(dbObject);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }    
}
