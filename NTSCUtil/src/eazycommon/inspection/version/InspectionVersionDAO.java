/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.inspection.version;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.backlist.DBManager;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InspectionVersionDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.OTHER_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String getIOSTurnOffSafaryVersion() throws EazyException {
        String turnOffSafaryVersion = "0.0";
        try {
            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {

                turnOffSafaryVersion = (String) searchObj.get(SettingdbKey.OTHER_SETTING.TURN_OFF_SAFARY_VERSION);

            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        if (turnOffSafaryVersion == null) {
            turnOffSafaryVersion = "0.0";
        }
        return turnOffSafaryVersion;
    }
    
}
