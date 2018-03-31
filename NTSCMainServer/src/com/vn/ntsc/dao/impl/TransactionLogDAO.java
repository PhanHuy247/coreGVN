/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.Transaction;

/**
 *
 * @author RuAc0n
 */
public class TransactionLogDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getCashDB().getCollection(CashdbKey.TRANSACTION_LOG_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean addTransaction(String userId, String time, int point, double price, double totalPrice, String identifier, String transactionId, int productType, long timePurchase, String ip, boolean isRealTransaction, String applicationId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productType);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, timePurchase);
            insertObj.append(CashdbKey.TRANSACTION_LOG.UNIQUE_IDENTIFIER, identifier);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, totalPrice);
            if (productType == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                insertObj.append(CashdbKey.TRANSACTION_LOG.TRANSACTION_ID, transactionId);
            }
            insertObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            insertObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            insertObj.append(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION, isRealTransaction);
            insertObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, false);
            insertObj.append(CashdbKey.TRANSACTION_LOG.APPLICATION_ID, applicationId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADDED_TO_TOTAL, 1);            
            coll.insert(insertObj);
            UserDAO.increaseFieldDouble(userId, UserdbKey.USER.TOTAL_PURCHASE, price);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateTransaction(String userId, String internalTransactionId, String time, int point, double price, double totalPrice, String identifier, String transactionId, int productType, long timePurchase, String ip, boolean isRealTransaction, String applicationId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObject = new BasicDBObject();
            findObject.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            findObject.append(CashdbKey.TRANSACTION_LOG.INTERNAL_TRANSACTION_ID, internalTransactionId);
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productType);
            updateObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, timePurchase);
            updateObj.append(CashdbKey.TRANSACTION_LOG.UNIQUE_IDENTIFIER, identifier);
            updateObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            updateObj.append(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, totalPrice);
            if (productType == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                updateObj.append(CashdbKey.TRANSACTION_LOG.TRANSACTION_ID, transactionId);
            }
            updateObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            updateObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            updateObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            updateObj.append(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION, isRealTransaction);
            updateObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            updateObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, false);
            updateObj.append(CashdbKey.TRANSACTION_LOG.APPLICATION_ID, applicationId);

            BasicDBObject setObject = new BasicDBObject("$set", updateObj);
            coll.update(findObject, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addTransaction(String userId, String time, int point, double price, int productType, long timePurchase, String internalTransactionId, String ip, String applicationId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productType);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, timePurchase);
            insertObj.append(CashdbKey.TRANSACTION_LOG.INTERNAL_TRANSACTION_ID, internalTransactionId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            insertObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            insertObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, false);
            insertObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, false);
            insertObj.append(CashdbKey.TRANSACTION_LOG.APPLICATION_ID, applicationId);

            coll.insert(insertObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isPurchaseByUserId(String userId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            findObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            DBObject obj = coll.findOne(findObj);
            result = obj != null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isTransactionExist(String identifier, String transactionId, int productType) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(CashdbKey.TRANSACTION_LOG.UNIQUE_IDENTIFIER, identifier);
            if (productType == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION) {
                findObj.append(CashdbKey.TRANSACTION_LOG.TRANSACTION_ID, transactionId);
            }
            DBObject obj = coll.findOne(findObj);
            result = obj != null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Transaction> getTransactionInfor() throws EazyException {
        List<Transaction> result = new ArrayList<Transaction>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                Transaction tran = new Transaction();
                BasicDBObject obj = (BasicDBObject) cur.next();
                String userId = obj.getString(CashdbKey.TRANSACTION_LOG.USER_ID);
                tran.userId = userId;
                Integer point = obj.getInt(CashdbKey.TRANSACTION_LOG.POINT);
                tran.point = point;
                String time = obj.getString(CashdbKey.TRANSACTION_LOG.TIME);
                tran.time = time;
                result.add(tran);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static double getTotalPrice(String userId) throws EazyException {
        double result = 0;
        try {
            DBObject findObject = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            DBObject sortObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, -1);
            DBCursor cur = coll.find(findObject).sort(sortObj);
            if (cur != null && cur.size() != 0) {
                DBObject obj = cur.next();
                Double totalPrice = (Double) obj.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                if (totalPrice != null) {
                    result = totalPrice;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
