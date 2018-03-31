/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.setting;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.setting.UploadSetting;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class UploadSettingDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.UPLOAD_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static UploadSetting getUploadSetting() throws EazyException {
        UploadSetting result = new UploadSetting();
        try{
            DBObject searchObj = coll.findOne();
            if (searchObj != null) {
                result = UploadSetting.createUploadSetting(searchObj);
            }
        }catch( Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateUploadSetting(UploadSetting up) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_FILE_SIZE, up.fileSize);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_SIZE, up.totalFileSize);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_IMAGE_FILE, up.imageNumber);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_VIDEO_FILE, up.videoNumber);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_AUDIO_FILE, up.audioNumber);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_TOTAL_FILE_UPLOAD, up.totalFileUpload);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_FILE_PER_ALBUM, up.maxFilePerAlbum);
            updateObj.append(SettingdbKey.UPLOAD_SETTING.MAX_LENGTH_BUZZ, up.maxLengthBuzz);
            if(up.defaultAudioImg != null && !up.defaultAudioImg.equals("")){
                updateObj.append(SettingdbKey.UPLOAD_SETTING.DEFAULT_AUDIO_IMG, up.defaultAudioImg);
            }
            if(up.shareHasDeletedImg != null && !up.shareHasDeletedImg.equals("")){
                updateObj.append(SettingdbKey.UPLOAD_SETTING.SHARE_HAS_DELETED_IMG, up.shareHasDeletedImg);
            }
            
            DBObject searchObj = coll.findOne();
            if(searchObj == null){
                coll.insert(updateObj);
            }else{
                DBObject setObject = new BasicDBObject("$set", updateObj);
                coll.update(searchObj, setObject);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
