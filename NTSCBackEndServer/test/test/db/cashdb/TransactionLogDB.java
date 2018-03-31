/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.cashdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import test.db.DBTest;

/**
 *
 * @author duyetpt
 */
public class TransactionLogDB {

    private static DB db;
    private static DBCollection coll;
    static List<String> list;

    static {
        try {
//            DAO.init();
            db = DBTest.mongo.getDB(CashdbKey.DB_NAME);
            coll = db.getCollection(CashdbKey.TRANSACTION_LOG_COLLECTION);
            list = new ArrayList<>();
        } catch (Exception ex) {
        }
    }

    public static String addTransaction(String userId, String time, int point, double price, double totalPrice, String identifier, String transactionId, int productType, long timePurchase, String ip) throws EazyException {
        String result = "";
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productType);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, timePurchase);
            insertObj.append(CashdbKey.TRANSACTION_LOG.UNIQUE_IDENTIFIER, identifier);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, totalPrice);
            if (productType == 0) {
                insertObj.append(CashdbKey.TRANSACTION_LOG.TRANSACTION_ID, transactionId);
            }
            insertObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            insertObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            coll.insert(insertObj);
            result = insertObj.get("_id").toString();
            list.add(result);
        } catch (Exception ex) {
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove() {
        for (String id : list) {
            coll.remove(new BasicDBObject("_id", new ObjectId(id)));
        }
    }

    public static void main(String[] args) throws EazyException {
        for (int i = 0; i < 50; i++) {
            addTransaction("user" + i / 5, "20140711151200", 500, i, i * 5, "dfjsldfjowiueowru83", "79283798798", 0, new Date().getTime(), "localhost");
        }
    }

    public static void addTransactions(String userId, int count) throws EazyException {
        for (int i = 0; i < count; i++) {
            addTransaction(userId, "20140711151200", 500, i, i * 5, "dfjsldfjowiueowru83", "79283798798", 0, new Date().getTime(), "localhost");
        }
    }
}
