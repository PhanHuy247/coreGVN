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

/**
 *
 * @author RuAc0n
 */
public class AdminStringDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.ADMIN_STRING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean insert(String str) throws EazyException {
        boolean result = false;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.ADMIN_STRING.STRING, str);
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
    
    public static String get() throws EazyException {
        String result = null;
        try {
            DBObject obj = coll.findOne();
            if(obj != null){
                result = (String) obj.get(SettingdbKey.ADMIN_STRING.STRING);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }       
}
