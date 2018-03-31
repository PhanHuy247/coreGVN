/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.admin.ApplicationDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogPurchase;
import com.vn.ntsc.backend.entity.impl.user.User;

/**
 *
 * @author RuAc0n
 */
public class TransactionLogDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCashDB().getCollection(CashdbKey.TRANSACTION_LOG_COLLECTION);
            coll.createIndex(new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    // LongLT 8/8/2016
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

    // LongLT 8/8/2016
    public static boolean addTransaction(String userId, String time, int point, double price, double totalMoney,
            int productionType, long timePurchase, String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, totalMoney);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, productionType);
            insertObj.append(CashdbKey.TRANSACTION_LOG.PRICE, price);
            insertObj.append(CashdbKey.TRANSACTION_LOG.POINT, point);
            insertObj.append(CashdbKey.TRANSACTION_LOG.TIME, time);
            insertObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, true);
            insertObj.append(CashdbKey.TRANSACTION_LOG.IP, ip);
            insertObj.append(CashdbKey.TRANSACTION_LOG.ADDED_TO_TOTAL, 1);
            coll.insert(insertObj);
            UserDAO.increaseTotalPurchase(userId, price);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData listLog(List<String> listId, String fromTime, String toTime, Long success, Long purchaseType, Long addbyadmin, Long payment_type,
            Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(CashdbKey.TRANSACTION_LOG.USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, lte);
                ands[1] = new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(CashdbKey.TRANSACTION_LOG.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(CashdbKey.TRANSACTION_LOG.TIME, lte);
                }
            }
            if (success != null) {
                boolean isSuccess = success.intValue() == 1;
                findObject.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, isSuccess);
            }
            // thanhdd add
            if (addbyadmin != null) {
                boolean isaddbyadmin = addbyadmin.intValue() == 1;
                findObject.append(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG, isaddbyadmin);
            }
            //fix #6691 10/20/2017
            if (payment_type != null) {
                findObject.append(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE, payment_type);
            }

            if (purchaseType != null) {
                boolean isReal = purchaseType.intValue() == 1;
                if (isReal) {
                    BasicDBList ors = new BasicDBList();
                    ors.add(new BasicDBObject(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION, new BasicDBObject("$exists", false)));
                    ors.add(new BasicDBObject(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION, isReal));
                    findObject.append("$or", ors);
                } else {
                    findObject.append(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION, isReal);
                }
            }

            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(CashdbKey.TRANSACTION_LOG.TIME, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if (!isCsv) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                LogPurchase ll = new LogPurchase();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(CashdbKey.TRANSACTION_LOG.USER_ID);
                ll.userId = uId;
                //User u = UserDAO.getUserInfor(uId);
                String applicationId = "1";
//                if (u.applicationId != null) {
//                    applicationId = u.applicationId;
//                }
                if (dbO.getString(CashdbKey.TRANSACTION_LOG.APPLICATION_ID) != null) {
                    applicationId = dbO.getString(CashdbKey.TRANSACTION_LOG.APPLICATION_ID);
                }
                Util.addDebugLog("listLog applicationId==========" + applicationId);
                ll.application_id = applicationId;
                ll.applicationName = ApplicationDAO.getUniqueNameById(applicationId);
                String time = dbO.getString(CashdbKey.TRANSACTION_LOG.TIME);
                ll.time = time;
                String ip = dbO.getString(CashdbKey.TRANSACTION_LOG.IP);
                ll.ip = ip;
                int point = dbO.getInt(CashdbKey.TRANSACTION_LOG.POINT);
                ll.point = point;
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                ll.price = price;
                Double totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                ll.totalPrice = totalP;
                int isReal = 1;
                Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
                if (purType != null) {
                    isReal = purType ? 1 : 0;
                }
                ll.purchaseType = isReal;

                int isSuccess = 1;
                Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
                if (successFlag != null) {
                    isSuccess = successFlag ? 1 : 0;
                }
                ll.success = isSuccess;
                //thanhdd add
//                int isaddbyadmin = 1;
                int isaddbyadmin = -1;

                Boolean addbyadminFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.ADD_BY_ADMIN_FLAG);
                if (addbyadminFlag != null) {
                    isaddbyadmin = addbyadminFlag ? 1 : 0;
                }
                ll.addbyadmin = isaddbyadmin;

                int production_type = dbO.getInt(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE);
                ll.production_type = production_type;
                //end
                list.add(ll);
            }
            result = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData listLogTotalMoneyBuyPoint(String userId) throws EazyException {
        SizedListData result = new SizedListData();
        List<IEntity> list = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            }
            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            }

            while (cursor.hasNext()) {
                LogPurchase ll = new LogPurchase();
                //BasicDBObject dbO = (BasicDBObject) cursor.next();
                DBObject dbO = cursor.next();

                String uId = (String) dbO.get(CashdbKey.TRANSACTION_LOG.USER_ID);
                ll.userId = uId;
                String time = (String) dbO.get(CashdbKey.TRANSACTION_LOG.TIME);
                ll.time = time;
                //String ip = (String)dbO.get(CashdbKey.TRANSACTION_LOG.IP);
                //ll.ip = ip;
                Integer point = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.POINT);
                ll.point = point;
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                ll.price = price;
                Double totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                ll.totalPrice = totalP;
                int isReal = 1;
                Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
                if (purType != null) {
                    isReal = purType ? 1 : 0;
                }
                ll.purchaseType = isReal;
                Integer production_type = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE);
                ll.production_type = production_type;
                int isSuccess = 1;
                Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
                if (successFlag != null) {
                    isSuccess = successFlag ? 1 : 0;
                }
                ll.success = isSuccess;

                list.add(ll);
            }

            result = new SizedListData(null, list);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //thanhdd add 07/10/2016
    public static SizedListData listLogTotalPrice(String userId, int... productionType) throws EazyException {
        SizedListData result = new SizedListData();
        List<IEntity> list = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            }
            findObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            }
//            SizedListData sld = null;
            if (productionType.length > 0) {
                return calculateTotalPriceByType(cursor, productionType[0]);
            } else {
                return calculateTotalPriceByMultiType(cursor);
            }

//            if(sld != null){
//                List logPurchaseList = sld.ll;
//                if(logPurchaseList!=null){
//                    for(Object o : logPurchaseList){
//                        LogPurchase ll = (LogPurchase) o; 
//                    }
//                }
//            }
//            
//            while (cursor.hasNext()) {
//                LogPurchase ll = new LogPurchase();
//                //BasicDBObject dbO = (BasicDBObject) cursor.next();
//                DBObject dbO = cursor.next();
//
//                String uId = (String) dbO.get(CashdbKey.TRANSACTION_LOG.USER_ID);
//                ll.userId = uId;
//                String time = (String) dbO.get(CashdbKey.TRANSACTION_LOG.TIME);
//                ll.time = time;
//                //String ip = (String)dbO.get(CashdbKey.TRANSACTION_LOG.IP);
//                //ll.ip = ip;
//                Integer point = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.POINT);
//                ll.point = point;
//                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
//                ll.price = price;
//                Double totalP = 0.0;
//                if (dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE) != null) {
//                    totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
//                }
//
//                ll.totalPrice = totalP;
//                int isReal = 1;
//                Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
//                if (purType != null) {
//                    isReal = purType ? 1 : 0;
//                }
//                ll.purchaseType = isReal;
//                Util.addDebugLog("========productionType.length====="+productionType.length);
//                if (productionType.length > 0) {
//                    Util.addDebugLog("========productionType.length > 0=====");
//                    ll.production_type = productionType[0];
//                    Util.addDebugLog("========productionType====="+ll.production_type);
//                }
//                
//                int isSuccess = 1;
//                Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
//                if (successFlag != null) {
//                    isSuccess = successFlag ? 1 : 0;
//                }
//                ll.success = isSuccess;
//
//                list.add(ll);
//            }
//            LogPurchase l1 = null;
//            //Util.addDebugLog("====totalPrice Size:"+list.size());
//            if (list.size() > 0) {
//                l1 = (LogPurchase) list.get(0);
//            }
//            if (l1 != null) {
//                result = new SizedListData(null, list);
//                result.totalPrice = l1.totalPrice;
//            } else {
//                result.totalPrice = 0.0;
//            }
//
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        // return result;
    }

    public static void updateTotalPurchaseToUser() throws EazyException {
        int count = 0;
        try {
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, new BasicDBObject("$ne", 1));
            findObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            DBCursor cursor;
            cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            Util.addDebugLog("updateTotalPurchaseToUser =========================== NOT added to total " + cursor.size());        
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                String userID = (String) dbO.get(LogdbKey.LOG_POINT.USER_ID); 
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                UserDAO.increaseTotalPurchase(userID, price);
                dbO.put(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, 1);      
                coll.save(dbO);
                count++;
                if(count%300 == 0){
                    Util.addDebugLog("updateTotalPurchaseToUser ==================== " + count) ;
                    Thread.sleep(2000);
                }
                
            }         
            Util.addDebugLog("updateTotalPurchaseToUser =========================== DONE");    
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        // return result;
    }

    // LongLT Add 09 Oct 2016
    public static SizedListData calculateTotalPriceByMultiType(DBCursor cursor) {
        SizedListData result = new SizedListData();
        List<IEntity> list = new ArrayList<>();

        while (cursor.hasNext()) {
            LogPurchase ll = new LogPurchase();
            //BasicDBObject dbO = (BasicDBObject) cursor.next();
            DBObject dbO = cursor.next();

            Integer pt = dbO.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE) != null ? (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE) : -1;
            ll.production_type = pt;

//            if (pt != productType) {
//                continue;
//            }
            String uId = (String) dbO.get(CashdbKey.TRANSACTION_LOG.USER_ID);
            ll.userId = uId;
            String time = (String) dbO.get(CashdbKey.TRANSACTION_LOG.TIME);
            ll.time = time;
            //String ip = (String)dbO.get(CashdbKey.TRANSACTION_LOG.IP);
            //ll.ip = ip;
            Integer point = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.POINT);
            ll.point = point;
            Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
            ll.price = price;

            int isReal = 1;
            Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
            if (purType != null) {
                isReal = purType ? 1 : 0;
            }
            ll.purchaseType = isReal;

            int isSuccess = 1;
            Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
            if (successFlag != null) {
                isSuccess = successFlag ? 1 : 0;
            }
            ll.success = isSuccess;

            result.totalPrice += ll.price;

            list.add(ll);
        }
        result.ll = list;

        return result;
    }

    // LongLT Add 09 Oct 2016
    public static SizedListData calculateTotalPriceByType(DBCursor cursor, int productType) {
        SizedListData result = new SizedListData();
        List<IEntity> list = new ArrayList<>();

        while (cursor.hasNext()) {
            LogPurchase ll = new LogPurchase();
            //BasicDBObject dbO = (BasicDBObject) cursor.next();
            DBObject dbO = cursor.next();

            Integer pt = dbO.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE) != null ? (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.PRODUCTION_TYPE) : -1;
            ll.production_type = pt;

            if (pt != productType) {
                continue;
            }

            String uId = (String) dbO.get(CashdbKey.TRANSACTION_LOG.USER_ID);
            ll.userId = uId;
            String time = (String) dbO.get(CashdbKey.TRANSACTION_LOG.TIME);
            ll.time = time;
            //String ip = (String)dbO.get(CashdbKey.TRANSACTION_LOG.IP);
            //ll.ip = ip;
            Integer point = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.POINT);
            ll.point = point;
            Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
            ll.price = price;

            int isReal = 1;
            Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
            if (purType != null) {
                isReal = purType ? 1 : 0;
            }
            ll.purchaseType = isReal;

            int isSuccess = 1;
            Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
            if (successFlag != null) {
                isSuccess = successFlag ? 1 : 0;
            }
            ll.success = isSuccess;

            result.totalPrice += ll.price;

            list.add(ll);
        }
        result.ll = list;

        return result;
    }

    //Edit ThanhDD 03/10/2016 #4789
    public static List<IEntity> listLog(String userId) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            findObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            DBCursor cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            while (cursor.hasNext()) {
                LogPurchase ll = new LogPurchase();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(CashdbKey.TRANSACTION_LOG.USER_ID);
                ll.userId = uId;
                String time = dbO.getString(CashdbKey.TRANSACTION_LOG.TIME);
                ll.time = time;
                String ip = dbO.getString(CashdbKey.TRANSACTION_LOG.IP);
                ll.ip = ip;
                int point = dbO.getInt(CashdbKey.TRANSACTION_LOG.POINT);
                ll.point = point;
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                ll.price = price;
                Double totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                ll.totalPrice = totalP;

                result.add(ll);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //Edit ThanhDD 03/10/2016 #4311
    public static SizedListData listLogTotalPoint(String userId) throws EazyException {
        SizedListData result = new SizedListData();
        List<IEntity> list = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            }
            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));
            }

            while (cursor.hasNext()) {
                LogPurchase ll = new LogPurchase();
                //BasicDBObject dbO = (BasicDBObject) cursor.next();
                DBObject dbO = cursor.next();

                String uId = (String) dbO.get(CashdbKey.TRANSACTION_LOG.USER_ID);
                ll.userId = uId;
                String time = (String) dbO.get(CashdbKey.TRANSACTION_LOG.TIME);
                ll.time = time;
                //String ip = (String)dbO.get(CashdbKey.TRANSACTION_LOG.IP);
                //ll.ip = ip;
                Integer point = (Integer) dbO.get(CashdbKey.TRANSACTION_LOG.POINT);
                ll.point = point;
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                ll.price = price;
                Double totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                ll.totalPrice = totalP;
                int isReal = 1;
                Boolean purType = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.IS_REAL_TRANSACTION);
                if (purType != null) {
                    isReal = purType ? 1 : 0;
                }
                ll.purchaseType = isReal;

                int isSuccess = 1;
                Boolean successFlag = (Boolean) dbO.get(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG);
                if (successFlag != null) {
                    isSuccess = successFlag ? 1 : 0;
                }
                ll.success = isSuccess;

                list.add(ll);
            }
            result = new SizedListData(null, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean getListTransaction(Long fromTime, Long toTime, Double to_money, Double from_money, List<String> purchase) {
        boolean result = false;
        List<String> list = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject();

            BasicDBList ands = new BasicDBList();
//            BasicDBObject objGt = new BasicDBObject();
//            BasicDBObject objLt = new BasicDBObject();
////            if (fromTime != null && toTime != null) {
////                objGt.append("$gt", fromTime);
////                objLt.append("$lt", toTime);
////                ands.add(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, objGt));
////                ands.add(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, objLt));
////            } else {
////                if (fromTime != null) {
////                    objGt.append("$gt", fromTime);
////                    findObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, objGt);
////                }
////                if (toTime != null) {
////                    objLt.append("$lt", toTime);
////                    findObj.append(CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, objLt);
////                }
////            }
            query(ands, findObj, CashdbKey.TRANSACTION_LOG.TIME_PURCHASE, fromTime, toTime);
            query(ands, findObj, CashdbKey.TRANSACTION_LOG.TOTAL_PRICE, from_money, to_money);
            if (!ands.isEmpty()) {
                findObj.append("$and", ands);
            }
//            System.out.println(findObj.toString());
            result = findObj.isEmpty();
            list = coll.distinct(CashdbKey.TRANSACTION_LOG.USER_ID, findObj);
            purchase.addAll(list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return result;
    }

    // ThanhDD 04/10/2016 #4311
    public static List<IEntity> getListAllTransaction(String userId) {
        List<IEntity> result = new ArrayList<IEntity>();

        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.TRANSACTION_LOG.USER_ID, userId);
            findObj.append(CashdbKey.TRANSACTION_LOG.SUCCESS_FLAG, true);
            DBCursor cursor = coll.find(findObj).sort(new BasicDBObject(CashdbKey.TRANSACTION_LOG.TIME, -1));

            while (cursor.hasNext()) {
                LogPurchase ll = new LogPurchase();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(CashdbKey.TRANSACTION_LOG.USER_ID);
                ll.userId = uId;
                String time = dbO.getString(CashdbKey.TRANSACTION_LOG.TIME);
                ll.time = time;
                String ip = dbO.getString(CashdbKey.TRANSACTION_LOG.IP);
                ll.ip = ip;
                int point = dbO.getInt(CashdbKey.TRANSACTION_LOG.POINT);
                if (point > 0) {
                    ll.point = point;
                }
                Double price = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.PRICE);
                ll.price = price;
                Double totalP = (Double) dbO.get(CashdbKey.TRANSACTION_LOG.TOTAL_PRICE);
                ll.totalPrice = totalP;

                result.add(ll);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return result;
    }

    private static void query(BasicDBList ands, BasicDBObject obj, String key, Object from, Object to) {
        BasicDBObject objGt = new BasicDBObject();
        BasicDBObject objLt = new BasicDBObject();
        if (from != null && to != null) {
            objGt.append("$gte", from);
            objLt.append("$lte", to);
            ands.add(new BasicDBObject(key, objGt));
            ands.add(new BasicDBObject(key, objLt));
        } else {
            if (from != null) {
                objGt.append("$gte", from);
                obj.append(key, objGt);
            }
            if (to != null) {
                objLt.append("$lte", to);
                obj.append(key, objLt);
            }
        }
    }

    public static List<String> getAllTrader() {
        List<String> list = coll.distinct(CashdbKey.TRANSACTION_LOG.USER_ID);
        return list;
    }

}
