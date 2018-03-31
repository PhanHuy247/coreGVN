/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.admin.AdminSetting;

/**
 *
 * @author RuAc0n
 */
public class AdminSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.ADMIN_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean insert(int timezone) throws EazyException {
        boolean result = false;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.ADMIN_SETTING.TIME_ZONE, timezone);
            DBObject obj = coll.findOne();
            if(obj != null)
                coll.update(obj, insertObj);
            else
                coll.insert(insertObj);
            result = false;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static AdminSetting get() throws EazyException {
        try {
            DBObject obj = coll.findOne();
            if(obj != null){
                Integer timezone = (Integer) obj.get(SettingdbKey.ADMIN_SETTING.TIME_ZONE);
                return new AdminSetting(timezone);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return null;
    }       
}
