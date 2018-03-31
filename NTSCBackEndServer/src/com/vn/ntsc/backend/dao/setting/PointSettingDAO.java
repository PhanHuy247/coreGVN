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
import java.util.Iterator;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author RuAc0n
 */
public class PointSettingDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.POINT_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean update(JSONObject json) throws EazyException {
        boolean result = false;
        try {
            Iterator iter = json.keySet().iterator();
            while (iter.hasNext()) {
                String type = (String) iter.next();
                if (DAOKeys.one_object_minus_list.contains(type) || DAOKeys.one_object_add_list.contains(type)) {
                    BasicDBObject findObj = new BasicDBObject(SettingdbKey.POINT_SETTING.TYPE, type);
                    JSONObject obj = (JSONObject) json.get(type);
                    BasicDBObject element = new BasicDBObject();

                    Long maleTradablePoint = (Long) obj.get(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT);
                    if (maleTradablePoint != null) {
                        element.put(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT, maleTradablePoint.intValue());
                    }
                    Long potentialCustomerRequestMale = (Long) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT);
                    if (potentialCustomerRequestMale != null) {
                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT, potentialCustomerRequestMale.intValue());
                    }
                    Long femaleTradablePoint = (Long) obj.get(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT);
                    if (femaleTradablePoint != null) {
                        element.put(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT, femaleTradablePoint.intValue());
                    }
                    Long potentialCustomerRequestFemale = (Long) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT);
                    if (potentialCustomerRequestFemale != null) {
                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT, potentialCustomerRequestFemale.intValue());
                    }
                    Long malePartnerTradablePoint = (Long) obj.get(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT);
                    if (malePartnerTradablePoint != null) {
                        element.put(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT, malePartnerTradablePoint.intValue());
                    }
//                    Long potentialCustomerPartnerMale = (Long) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT);
//                    if (potentialCustomerPartnerMale != null) {
//                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT, potentialCustomerPartnerMale.intValue());
//                    }
                    Long femalePartnerTradablePoint = (Long) obj.get(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT);
                    if (femalePartnerTradablePoint != null) {
                        element.put(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT, femalePartnerTradablePoint.intValue());
                    }
//                    Long potentialCustomerPartnerFemale = (Long) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT);
//                    if (potentialCustomerPartnerFemale != null) {
//                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT, potentialCustomerPartnerFemale.intValue());
//                    }
                    BasicDBObject updateObject = new BasicDBObject("$set", element);
                    coll.update(findObj, updateObject, true, false);
                }
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static JSONObject get() throws EazyException {
        JSONObject result = new JSONObject();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                JSONObject element = new JSONObject();
                DBObject obj = cursor.next();

                String type = (String) obj.get(SettingdbKey.POINT_SETTING.TYPE);
                Integer maleTradablePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT);
                if (maleTradablePoint != null) {
                    element.put(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT, maleTradablePoint);
                }
                Integer potentialCustomerRequestMale = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT);
                if (potentialCustomerRequestMale != null) {
                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT, potentialCustomerRequestMale);
                }
                
                Integer femaleTradablePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT);
                if (femaleTradablePoint != null) {
                    element.put(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT, femaleTradablePoint);
                }
                Integer potentialCustomerRequestFemale = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT);
                if (potentialCustomerRequestFemale != null) {
                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT, potentialCustomerRequestFemale);
                }
                
                Integer malePartnerTradablePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT);
                if (malePartnerTradablePoint != null) {
                    element.put(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT, malePartnerTradablePoint);
                }
//                Integer potentialCustomerPartnerMale = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT);
//                if (potentialCustomerPartnerMale != null) {
//                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT, potentialCustomerPartnerMale);
//                }
                
                Integer femalePartnerTradablePoint = (Integer) obj.get(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT);
                if (femalePartnerTradablePoint != null) {
                    element.put(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT, femalePartnerTradablePoint);
                }
//                Integer potentialCustomerPartnerFemale = (Integer) obj.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT);
//                if (potentialCustomerPartnerFemale != null) {
//                        element.put(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT, potentialCustomerPartnerFemale);
//                }
                
                result.put(type, element);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
