/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.statistic;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CMcodedbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.statistic.CMCodeStatistics;
import com.vn.ntsc.backend.entity.impl.statistic.InstallationStatistic;

/**
 *
 * @author DuongLTD
 */
public class CMCodeStatisticDAO {

    private static DBCollection coll;


    static{
         try{
             coll = DBManager.getCmCodeDB().getCollection( CMcodedbKey.CM_CODE_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
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
            BasicDBObject timesObj = new BasicDBObject();
            BasicDBObject moneyObj = new BasicDBObject();
            if(type == Constant.DEVICE_TYPE.IOS){
                timesObj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_MONEY, 1);
                moneyObj.append(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_MONEY, money);
            }
            else{
                timesObj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_TIMES, 1);
                moneyObj.append(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_MONEY, money);
            }
            BasicDBObject updateObj = new BasicDBObject("$inc", timesObj);
            updateObj.append("$inc", moneyObj);
            coll.update(findObj, updateObj, true, false);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static  List<IEntity> statistic(String fromDay, String toDay) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try{
            BasicDBObject findObject = new BasicDBObject();
            if (fromDay != null && toDay != null) {
                BasicDBList ands = new BasicDBList();
//                lowerDay += "000000";
//                upperDay += "999999";
                BasicDBObject gte = new BasicDBObject("$gte", fromDay);
                BasicDBObject lt = new BasicDBObject("$lt", toDay);
                ands.add(new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.DAY, lt));
                ands.add(new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.DAY, gte));
                findObject.append("$and", ands);
            } else {
                if (fromDay != null) {
//                    lowerDay += "000000";
                    BasicDBObject gte = new BasicDBObject("$gte", fromDay);
                    findObject.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, gte);
                }

                if (toDay != null) {
//                    upperDay += "999999";
                    BasicDBObject lte = new BasicDBObject("$lte", toDay);
                    findObject.append(CMcodedbKey.CM_CODE_STATISTIC.DAY, lte);
                }
            }

            BasicDBObject sortObj = new BasicDBObject(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE, 1);
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int count = 0;
            int size = cursor.size();
            CMCodeStatistics ccs = new CMCodeStatistics();
            InstallationStatistic stObj = new InstallationStatistic();
            Set<String> iosUnique = new HashSet<>();
            Set<String> androidUnique = new HashSet<>();
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                String cmCode = (String) obj.get(CMcodedbKey.CM_CODE_STATISTIC.CM_CODE);
                if(ccs.cmCode == null || !ccs.cmCode.equals(cmCode)){
                    if(ccs.cmCode != null){
                        stObj.androidUniqueNumber = androidUnique.size();
                        stObj.iosUniqueNumber = iosUnique.size();
                        ccs.installationStatistic = stObj;
                        result.add(ccs);
                    }
                    ccs = new CMCodeStatistics();
                    stObj = new InstallationStatistic();
                    iosUnique.clear();
                    androidUnique.clear();
                    ccs.cmCode = cmCode;
                    Integer andRegTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_REGISTER_TIMES);
                    if(andRegTime != null)
                        ccs.androidRegTimes = andRegTime;
                    Integer andLoginTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_LOGIN_TIMES);
                    if(andLoginTime != null)
                        ccs.androidLoginTimes = andLoginTime;
                    Integer andPurTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_TIMES);
                    if(andPurTime != null)
                        ccs.androidPurchaseTimes = andPurTime;
                    Double andPurMoney = (Double) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_MONEY);
                    if(andPurMoney != null)
                        ccs.androidPurchaseMoney = andPurMoney;

                    Integer iosRegTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_REGISTER_TIMES);
                    if(iosRegTime != null)
                        ccs.iosRegTimes = iosRegTime;
                    Integer iosLoginTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_LOGIN_TIMES);
                    if(iosLoginTime != null)
                        ccs.iosLoginTimes = iosLoginTime;
                    Integer iosPurTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_TIMES);
                    if(iosPurTime != null)
                        ccs.iosPurchaseTimes = iosPurTime;
                    Double iosPurMoney = (Double) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_MONEY);
                    if(iosPurMoney != null)
                        ccs.iosPurchaseMoney = iosPurMoney;
                    
                    Integer iosInstallTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_TIMES);
                    if(iosInstallTime != null)
                        stObj.iosInstallationTimes = iosInstallTime;
                    Integer androidInstallTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_TIMES);
                    if(androidInstallTime != null)
                        stObj.androidInstallationTimes = androidInstallTime;

                    BasicDBList iosList = (BasicDBList) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_LIST);
                    if(iosList != null && !iosList.isEmpty()){
                        for(Object dObj : iosList){
                            BasicDBObject bObj = (BasicDBObject) dObj;
                            String unique = bObj.getString(CMcodedbKey.CM_CODE_STATISTIC.UNIQUE_NUMBER);
                            iosUnique.add(unique);
                        }
                    }
                    BasicDBList adroidList = (BasicDBList) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_LIST);
                    if(adroidList != null && !adroidList.isEmpty()){
                        for(Object dObj : adroidList){
                            BasicDBObject bObj = (BasicDBObject) dObj;
                            String unique = bObj.getString(CMcodedbKey.CM_CODE_STATISTIC.UNIQUE_NUMBER);
                            androidUnique.add(unique);
                        }
                    }
                    
                }else{
                    Integer andRegTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_REGISTER_TIMES);
                    if(andRegTime != null)
                        ccs.androidRegTimes += andRegTime;
                    Integer andLoginTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_LOGIN_TIMES);
                    if(andLoginTime != null)
                        ccs.androidLoginTimes += andLoginTime;
                    Integer andPurTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_TIMES);
                    if(andPurTime != null)
                        ccs.androidPurchaseTimes += andPurTime;
                    Double andPurMoney = (Double) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_PURCHASE_MONEY);
                    if(andPurMoney != null)
                        ccs.androidPurchaseMoney += andPurMoney;
                    
                    Integer iosRegTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_REGISTER_TIMES);
                    if(iosRegTime != null)
                        ccs.iosRegTimes += iosRegTime;
                    Integer iosLoginTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_LOGIN_TIMES);
                    if(iosLoginTime != null)
                        ccs.iosLoginTimes += iosLoginTime;
                    Integer iosPurTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_TIMES);
                    if(iosPurTime != null)
                        ccs.iosPurchaseTimes += iosPurTime;
                    Double iosPurMoney = (Double) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_PURCHASE_MONEY);
                    if(iosPurMoney != null)
                        ccs.iosPurchaseMoney += iosPurMoney;  
                    
                    Integer iosInstallTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_TIMES);
                    if(iosInstallTime != null)
                        stObj.iosInstallationTimes += iosInstallTime;
                    Integer androidInstallTime = (Integer) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_TIMES);
                    if(androidInstallTime != null)
                        stObj.androidInstallationTimes += androidInstallTime;

                    BasicDBList iosList = (BasicDBList) obj.get(CMcodedbKey.CM_CODE_STATISTIC.IOS_INSTALL_LIST);
                    if(iosList != null && !iosList.isEmpty()){
                        for(Object dObj : iosList){
                            BasicDBObject bObj = (BasicDBObject) dObj;
                            String unique = bObj.getString(CMcodedbKey.CM_CODE_STATISTIC.UNIQUE_NUMBER);
                            iosUnique.add(unique);
                        }
                    }
                    BasicDBList adroidList = (BasicDBList) obj.get(CMcodedbKey.CM_CODE_STATISTIC.ANDROID_INSTALL_LIST);
                    if(adroidList != null && !adroidList.isEmpty()){
                        for(Object dObj : adroidList){
                            BasicDBObject bObj = (BasicDBObject) dObj;
                            String unique = bObj.getString(CMcodedbKey.CM_CODE_STATISTIC.UNIQUE_NUMBER);
                            androidUnique.add(unique);
                        }
                    }
                }
                
                count++;
                if(count == size){
                    stObj.androidUniqueNumber = androidUnique.size();
                    stObj.iosUniqueNumber = iosUnique.size();
                    ccs.installationStatistic = stObj;
                    result.add(ccs);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
//    public List<CmCodeStatistics> statistic(String month,String id, Long type) throws DaoException {
//        List<CmCodeStatistics> result = new ArrayList<CmCodeStatistics>();
//        try{
//            if(type == 0){
//                result = statisticByAfficiate(month, id);
//            }else if(type == 1){
//                result = statisticByMediaId(month, id);
//            }else if(type == 2){
//                result = statisticByCmCode(month, id);
//            }
//        }catch(MongoException ex){
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }catch(Exception ex){
//           
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
    
//    private List<CmCodeStatistics> statisticByAfficiate(String month,String affId) throws DaoException {
//        List<CmCodeStatistics> result = new ArrayList<CmCodeStatistics>();
//        try{
//            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.LOG_CM_CODE.MONTH, month);
//            findObj.append(CMcodedbKey.LOG_CM_CODE.AFFICIATE_ID, affId);
//            BasicDBObject sortObj = new BasicDBObject(CMcodedbKey.LOG_CM_CODE.DAY, 1);
//            DBCursor cursor = coll.find(findObj).sort(sortObj);
//            int count = 0;
//            int size = cursor.size();
//            while(cursor.hasNext()){
//                CmCodeStatistics ccs = new CmCodeStatistics();
//                BasicDBObject obj = (BasicDBObject) cursor.next();
//                String time = obj.getString(CMcodedbKey.LOG_CM_CODE.DAY);
//                if(ccs.time == null || !ccs.time.equals(time)){
//                    if(ccs.time != null)
//                        result.add(ccs);
//                    ccs = new CmCodeStatistics();
//                    ccs.time = time;
//                    Integer andRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_REGISTER_TIMES);
//                    if(andRegTime != null)
//                        ccs.androidRegTimes = andRegTime;
//                    Integer andLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_LOGIN_TIMES);
//                    if(andLoginTime != null)
//                        ccs.androidLoginTimes = andLoginTime;
//                    Integer andPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_TIMES);
//                    if(andPurTime != null)
//                        ccs.androidPurchaseTimes = andPurTime;
//                    Double andPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_MONEY);
//                    if(andPurMoney != null)
//                        ccs.androidPurchaseMoney = andPurMoney;
//
//                    Integer iosRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_REGISTER_TIMES);
//                    if(iosRegTime != null)
//                        ccs.iosRegTimes = iosRegTime;
//                    Integer iosLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_LOGIN_TIMES);
//                    if(iosLoginTime != null)
//                        ccs.iosLoginTimes = iosLoginTime;
//                    Integer iosPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_TIMES);
//                    if(iosPurTime != null)
//                        ccs.iosPurchaseTimes = iosPurTime;
//                    Double iosPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_MONEY);
//                    if(iosPurMoney != null)
//                        ccs.iosPurchaseMoney = iosPurMoney;
//                }else{
//                    Integer andRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_REGISTER_TIMES);
//                    if(andRegTime != null)
//                        ccs.androidRegTimes += andRegTime;
//                    Integer andLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_LOGIN_TIMES);
//                    if(andLoginTime != null)
//                        ccs.androidLoginTimes += andLoginTime;
//                    Integer andPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_TIMES);
//                    if(andPurTime != null)
//                        ccs.androidPurchaseTimes += andPurTime;
//                    Double andPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_MONEY);
//                    if(andPurMoney != null)
//                        ccs.androidPurchaseMoney += andPurMoney;
//
//                    Integer iosRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_REGISTER_TIMES);
//                    if(iosRegTime != null)
//                        ccs.iosRegTimes += iosRegTime;
//                    Integer iosLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_LOGIN_TIMES);
//                    if(iosLoginTime != null)
//                        ccs.iosLoginTimes += iosLoginTime;
//                    Integer iosPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_TIMES);
//                    if(iosPurTime != null)
//                        ccs.iosPurchaseTimes += iosPurTime;
//                    Double iosPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_MONEY);
//                    if(iosPurMoney != null)
//                        ccs.iosPurchaseMoney += iosPurMoney;   
//                }
//                count++;
//                if(count == size)
//                    result.add(ccs);
//            }
//        }catch(MongoException ex){
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }catch(Exception ex){
//           
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
//    
//    private List<CmCodeStatistics> statisticByMediaId(String month,String mediaId) throws DaoException {
//        List<CmCodeStatistics> result = new ArrayList<CmCodeStatistics>();
//        try{
//            BasicDBObject findObj = new BasicDBObject(CMcodedbKey.LOG_CM_CODE.MONTH, month);
//            findObj.append(CMcodedbKey.LOG_CM_CODE.MEIDA_ID, mediaId);
//            BasicDBObject sortObj = new BasicDBObject(CMcodedbKey.LOG_CM_CODE.DAY, 1);
//            DBCursor cursor = coll.find(findObj).sort(sortObj);
//            int count = 0;
//            int size = cursor.size();
//            while(cursor.hasNext()){
//                CmCodeStatistics ccs = new CmCodeStatistics();
//                BasicDBObject obj = (BasicDBObject) cursor.next();
//                String time = obj.getString(CMcodedbKey.LOG_CM_CODE.DAY);
//                if(ccs.time == null || !ccs.time.equals(time)){
//                    if(ccs.time != null)
//                        result.add(ccs);
//                    ccs = new CmCodeStatistics();
//                    ccs.time = time;
//                    Integer andRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_REGISTER_TIMES);
//                    if(andRegTime != null)
//                        ccs.androidRegTimes = andRegTime;
//                    Integer andLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_LOGIN_TIMES);
//                    if(andLoginTime != null)
//                        ccs.androidLoginTimes = andLoginTime;
//                    Integer andPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_TIMES);
//                    if(andPurTime != null)
//                        ccs.androidPurchaseTimes = andPurTime;
//                    Double andPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_MONEY);
//                    if(andPurMoney != null)
//                        ccs.androidPurchaseMoney = andPurMoney;
//
//                    Integer iosRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_REGISTER_TIMES);
//                    if(iosRegTime != null)
//                        ccs.iosRegTimes = iosRegTime;
//                    Integer iosLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_LOGIN_TIMES);
//                    if(iosLoginTime != null)
//                        ccs.iosLoginTimes = iosLoginTime;
//                    Integer iosPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_TIMES);
//                    if(iosPurTime != null)
//                        ccs.iosPurchaseTimes = iosPurTime;
//                    Double iosPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_MONEY);
//                    if(iosPurMoney != null)
//                        ccs.iosPurchaseMoney = iosPurMoney;
//                }else{
//                    Integer andRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_REGISTER_TIMES);
//                    if(andRegTime != null)
//                        ccs.androidRegTimes += andRegTime;
//                    Integer andLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_LOGIN_TIMES);
//                    if(andLoginTime != null)
//                        ccs.androidLoginTimes += andLoginTime;
//                    Integer andPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_TIMES);
//                    if(andPurTime != null)
//                        ccs.androidPurchaseTimes += andPurTime;
//                    Double andPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.ANDROID_PURCHASE_MONEY);
//                    if(andPurMoney != null)
//                        ccs.androidPurchaseMoney += andPurMoney;
//
//                    Integer iosRegTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_REGISTER_TIMES);
//                    if(iosRegTime != null)
//                        ccs.iosRegTimes += iosRegTime;
//                    Integer iosLoginTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_LOGIN_TIMES);
//                    if(iosLoginTime != null)
//                        ccs.iosLoginTimes += iosLoginTime;
//                    Integer iosPurTime = obj.getInt(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_TIMES);
//                    if(iosPurTime != null)
//                        ccs.iosPurchaseTimes += iosPurTime;
//                    Double iosPurMoney = obj.getDouble(CMcodedbKey.LOG_CM_CODE.IOS_PURCHASE_MONEY);
//                    if(iosPurMoney != null)
//                        ccs.iosPurchaseMoney += iosPurMoney;   
//                }
//                count++;
//                if(count == size)
//                    result.add(ccs);
//            }
//        }catch(MongoException ex){
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }catch(Exception ex){
//           
//            throw new DaoException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
}
