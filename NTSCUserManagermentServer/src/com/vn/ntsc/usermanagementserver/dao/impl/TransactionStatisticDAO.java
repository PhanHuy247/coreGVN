/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StatisticdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author DuongLTD
 */
public class TransactionStatisticDAO {

    private static DBCollection coll;

    private static final String BITCAH_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY;
    private static final String BITCAH_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES;
    
    private static final String CREDIT_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY;
    private static final String CREDIT_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES;
    
    private static final String POINT_BACK_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY;
    private static final String POINT_BACK_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES;
    
    private static final String C_CHECK_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY;
    private static final String C_CHECK_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES;
    
    private static final String CONBONI_MONEY_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY;
    private static final String CONBONI_TIME_FIELD = StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR + ".$." + StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES;
    
    
    static{
         try{
             coll = DatabaseLoader.getStatisticDB().getCollection( StatisticdbKey.TRANSACTION_STATISTIC_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static boolean update(String day, String hour, double money, int type) throws EazyException {
        boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
            BasicDBObject checkerObj = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String moneyField = null;
                String timeField = null;
                if(type == Constant.PAYMENT_TYPE.BISCASH){
                    moneyField = BITCAH_MONEY_FIELD;
                    timeField = BITCAH_TIME_FIELD;
                }else if(type == Constant.PAYMENT_TYPE.CREDIT){
                    moneyField = CREDIT_MONEY_FIELD;
                    timeField = CREDIT_TIME_FIELD;            
                }else if(type == Constant.PAYMENT_TYPE.POINT_BACK){
                    moneyField = POINT_BACK_MONEY_FIELD;
                    timeField = POINT_BACK_TIME_FIELD;
                }else if(type == Constant.PAYMENT_TYPE.C_CHECK){
                    moneyField = C_CHECK_MONEY_FIELD;
                    timeField = C_CHECK_TIME_FIELD;
                }else if(type == Constant.PAYMENT_TYPE.CONBONI){
                    moneyField = CONBONI_MONEY_FIELD;
                    timeField = CONBONI_TIME_FIELD;
                }
                if(moneyField != null && timeField != null){
                    BasicDBObject updateObj = new BasicDBObject(moneyField, money);
                    updateObj.append(timeField, 1);

                    BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                    coll.update(findObj, incObj);
                }
            }else{
                BasicDBObject updateQuery = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.DAY, day);
                BasicDBObject checkerElement = new BasicDBObject();
                if(type == Constant.PAYMENT_TYPE.BISCASH){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.BITCASH_TIMES, 1);
                }else if(type == Constant.PAYMENT_TYPE.CREDIT){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CREDIT_TIMES, 1);
                }else if(type == Constant.PAYMENT_TYPE.POINT_BACK){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.POINT_BACK_TIMES, 1);
                }else if(type == Constant.PAYMENT_TYPE.C_CHECK){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.C_CHECK_TIMES, 1);
                }else if(type == Constant.PAYMENT_TYPE.CONBONI){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_MONEY, money);
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.CONBONI_TIMES, 1);
                }
                if(!checkerElement.isEmpty()){
                    checkerElement.append(StatisticdbKey.TRANSACTION_STATISTIC.HOUR, hour);
                    BasicDBObject checker = new BasicDBObject(StatisticdbKey.TRANSACTION_STATISTIC.LIST_HOUR, checkerElement);
                    BasicDBObject updateCommand = new BasicDBObject("$push", checker );
                    coll.update(updateQuery, updateCommand, true, false);
                }
            }
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }        
    
}
