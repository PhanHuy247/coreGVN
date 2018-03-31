/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class CMCodeStatisticDAO {

    private static DBCollection coll;


    static{
         try{
             coll = DBLoader.getCmCodeDB().getCollection( CMcodedbKey.CM_CODE_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean registerUpdate(String day, String cmCode, int type) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, cmCode);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            BasicDBObject updateObj = new BasicDBObject();
            if(type == Constant.DEVICE_TYPE.IOS)
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_REGISTER_TIMES, 1);
            else
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_REGISTER_TIMES, 1);
            BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
            coll.update(findObj, incObj, true, false);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean installUpdate(String day, String cmCode, int type, long time) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, cmCode);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            BasicDBObject updateObj = new BasicDBObject();
            if(type == Constant.DEVICE_TYPE.IOS)
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_TIMES, 1);
            else
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_TIMES, 1);
            BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
            coll.update(findObj, incObj, true, false);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean installUpdate(String day, String cmCode, int type, String uniqueNumber, long time) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, cmCode);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            BasicDBObject element = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.TIME_INSTALL, time);
            element.append(CMcodedbKey.CM_CODE_STATISTIC.UNIQUE_NUMBER, uniqueNumber);
            BasicDBObject list;
            if(type == Constant.DEVICE_TYPE.IOS)
                list = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_LIST, element);
            else
                list = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_LIST, element);
            BasicDBObject updateCommand = new BasicDBObject("$push", list);
            coll.update(findObj, updateCommand);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean loginUpdate(String day, String cmCode, int type) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, cmCode);
            BasicDBObject updateObj = new BasicDBObject();
            if(type == Constant.DEVICE_TYPE.IOS)
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_LOGIN_TIMES, 1);
            else
                updateObj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_LOGIN_TIMES, 1);
            BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
            coll.update(findObj, incObj, true, false);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean purchaseUpdate(String day, String cmCode, int type, double money) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, cmCode);
            findObj.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, day);
            BasicDBObject obj = new BasicDBObject();
            if(type == Constant.DEVICE_TYPE.IOS){
                obj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_TIMES, 1);
                obj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_MONEY, money);
            }
            else{
                obj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_TIMES, 1);
                obj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_MONEY, money);
            }
            BasicDBObject updateObj = new BasicDBObject("$inc", obj);
            coll.update(findObj, updateObj, true, false);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
}
