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
import com.vn.ntsc.backend.entity.impl.setting.PrioritizeUserBuzzSetting;
import com.vn.ntsc.backend.entity.impl.setting.UploadSetting;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phan Huy
 */
public class PrioritizeUserBuzzDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    public static boolean updateUploadSetting(PrioritizeUserBuzzSetting up) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.SKIP_BUZZ, up.skipBuzz);
            updateObj.append(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.TAKE_BUZZ, up.takeBuzz);
            
            updateObj.append(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.LIST_BUZZ_ID, up.listBuzzId);
            updateObj.append(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.LIST_USER_ID, up.listUserId);
            updateObj.append("time", Util.currentTime());
            
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
