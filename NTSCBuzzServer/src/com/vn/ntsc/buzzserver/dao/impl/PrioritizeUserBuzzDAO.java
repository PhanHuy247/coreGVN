/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.PrioritizeUserBuzzSetting;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;

/**
 *
 * @author Phan Huy
 */
public class PrioritizeUserBuzzDAO {
     private static DBCollection coll;

    static {
        try {
            coll = DAO.getSettingDB().getCollection(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static PrioritizeUserBuzzSetting getPrioritizeSetting() throws EazyException {
        PrioritizeUserBuzzSetting up = new PrioritizeUserBuzzSetting();
        try {
            DBObject searchObj = coll.findOne();
            if(searchObj != null){
                up.skipBuzz = (Integer)searchObj.get(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.SKIP_BUZZ);
                up.takeBuzz = (Integer)searchObj.get(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.TAKE_BUZZ);
                up.listBuzzId = (List)searchObj.get(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.LIST_BUZZ_ID);
                up.listUserId = (List)searchObj.get(SettingdbKey.PRIORITIZE_USER_BUZZ_SETTING.LIST_USER_ID);
            }
           
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return up;
    }
}
