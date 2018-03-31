/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class DistanceSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getSettingDB().getCollection(SettingdbKey.DISTANCE_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static double getNearDistance() throws EazyException {
        double result = 0;
        try {
            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
//                DistanceSetting ds = new DistanceSetting();
                               
                BasicDBList distance = (BasicDBList) searchObj.get(SettingdbKey.DISTANCE_SETTING.DISTANCE);
                result = (double) distance.get(0);
//                ds.city = (double) distance.get(1);
//                ds.stage = (double) distance.get(2);
//                ds.country = (double)distance.get(3);
//                
//                Double localBuzz = (Double) searchObj.getDouble(SettingdbKey.DISTANCE_SETTING.LOCAL_BUZZ);
//                ds.localBuzz = localBuzz;
//                
//                result = ds;

            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
