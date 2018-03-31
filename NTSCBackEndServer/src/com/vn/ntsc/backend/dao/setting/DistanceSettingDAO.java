/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.setting;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.setting.DistanceSetting;

/**
 *
 * @author RuAc0n
 */
public class DistanceSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.DISTANCE_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean update(DistanceSetting ds) throws EazyException {
        boolean result = false;
        try {
            DBObject searchObj = coll.findOne();
            BasicDBObject updateObj = new BasicDBObject();
            BasicDBList list = new BasicDBList();
            list.add(ds.near);
            list.add(ds.city);
            list.add(ds.stage);
            list.add(ds.country);
            updateObj.append(SettingdbKey.DISTANCE_SETTING.DISTANCE, list);
            updateObj.append(SettingdbKey.DISTANCE_SETTING.LOCAL_BUZZ, ds.localBuzz);
            if (searchObj == null) {
                coll.insert(updateObj);
            } else {
                coll.update(searchObj, updateObj);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static IEntity get() throws EazyException {
        IEntity result = null;
        try {
            BasicDBObject searchObj = (BasicDBObject) coll.findOne();
            if (searchObj != null) {
                DistanceSetting ds = new DistanceSetting();
                               
                BasicDBList distance = (BasicDBList) searchObj.get(SettingdbKey.DISTANCE_SETTING.DISTANCE);
                ds.near = (double) distance.get(0);
                ds.city = (double) distance.get(1);
                ds.stage = (double) distance.get(2);
                ds.country = (double)distance.get(3);
                
                Double localBuzz = (Double) searchObj.getDouble(SettingdbKey.DISTANCE_SETTING.LOCAL_BUZZ);
                ds.localBuzz = localBuzz;
                
                result = ds;

            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
