/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class TransactionStatisticDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getStatisticDB().getCollection( StatisticdbKey.TRANSACTION_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean update(String day, String hour, double money, int type,String applicationName) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
            BasicDBObject checkerObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String moneyField;
                String timeField;
                if(type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION){
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES;
                }else if(type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION){
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES;               
                }else{
                    moneyField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY;
                    timeField = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES; 
                }
                BasicDBObject updateObj = new BasicDBObject(moneyField, money);
                updateObj.append(timeField, 1);
                
                BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                coll.update(findObj, incObj);
            }else{
                BasicDBObject updateQuery = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
                BasicDBObject checkerElement = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
                if(type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.IOS_TIMES, 1);
                }else if(type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.ANDROID_TIMES, 1);
                }else{
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.AMAZON_TIMES, 1);
                }
                BasicDBObject checker = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", checker );
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

}
