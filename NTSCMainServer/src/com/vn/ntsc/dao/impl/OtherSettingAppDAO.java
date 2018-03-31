/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.dao.DBManager;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author namhv
 */
public class OtherSettingAppDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.OTHER_SETTING_APP_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    public static DBObject getOtherSettingApp(Long appId) throws EazyException {
        DBObject findObj = new BasicDBObject();
        DBObject dBObject = new BasicDBObject();
        try {
            findObj = new BasicDBObject(SettingdbKey.OTHER_SETTING_APP.APP_ID, appId);
            dBObject = coll.findOne(findObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return dBObject;
    }
}
