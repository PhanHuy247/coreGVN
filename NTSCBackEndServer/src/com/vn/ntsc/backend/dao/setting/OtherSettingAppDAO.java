/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.setting;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.setting.OtherSettingApp;

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

    public static List<OtherSettingApp> getAllOtherSetting() throws EazyException {
        List<OtherSettingApp> result = new ArrayList<>();
        try {
            DBCursor cursor = coll.find();
             while (cursor.hasNext()) {
                 OtherSettingApp other = new OtherSettingApp();
                 DBObject obj = cursor.next();
                 Integer applicationId = (Integer) obj.get(SettingdbKey.OTHER_SETTING_APP.APP_ID);
                 Boolean force = (Boolean) obj.get(SettingdbKey.OTHER_SETTING_APP.FORCE_UPDATING);
                 String urlWeb = (String) obj.get(SettingdbKey.OTHER_SETTING_APP.URL_WEB);
                 String appVer = (String) obj.get(SettingdbKey.OTHER_SETTING_APP.APP_VERSION);
                 other.applicationId = applicationId;
                 other.forceUpdating = force;
                 other.urlWeb = urlWeb;
                 other.appVersion = appVer;
                 result.add(other);
             }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public  static boolean updateOtherSettingByAppId(Long applicationId,Boolean force,String urlWeb, String appVer) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.OTHER_SETTING_APP.APP_ID, applicationId);
            DBObject updateObject = new BasicDBObject(SettingdbKey.OTHER_SETTING_APP.FORCE_UPDATING, force);
            updateObject.put(SettingdbKey.OTHER_SETTING_APP.URL_WEB, urlWeb);
            updateObject.put(SettingdbKey.OTHER_SETTING_APP.APP_VERSION, appVer);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
