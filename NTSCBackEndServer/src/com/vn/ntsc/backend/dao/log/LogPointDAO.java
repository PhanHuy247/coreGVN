/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.admin.ApplicationDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.SystemAccount;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogPoint;
import com.vn.ntsc.backend.entity.impl.log.LogPurchase;
import com.vn.ntsc.backend.entity.impl.user.Point;
import com.vn.ntsc.backend.entity.impl.user.User;

/**
 *
 * @author RuAc0n
 */
public class LogPointDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_POINT_COLLECTION);
            coll.createIndex(new BasicDBObject(LogdbKey.LOG_POINT.USER_ID, 1));
            coll.createIndex(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static SizedListData listLog(String userId,int... options) throws EazyException {        
        List<IEntity> result = new ArrayList<>();
        SizedListData data = new SizedListData(null, result);
        try {
            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(LogdbKey.LOG_POINT.USER_ID, userId);
            }

            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            }
            
            List<IEntity> listSysAcc = UserDAO.getAllSystemAcc();
            IEntity user = null;
            SystemAccount account = null;
            
            if(options.length >0){
                cursor.skip(options[0]);
                Util.addDebugLog("listLog =============================== SKIP " + options[0]);
            }
            
            if(options.length >1){
                cursor.limit(options[1]);
                Util.addDebugLog("listLog =============================== TAKE " + options[1]);
            }
            
            data.total = cursor.count();
            Util.addDebugLog("listLog =============================== SIZE " + cursor.size());
            Util.addDebugLog("listLog =============================== COUNT " + cursor.count());
            while (cursor.hasNext()) {
                LogPoint lp = new LogPoint();
                DBObject obj = cursor.next();
                Integer type = (Integer) obj.get(LogdbKey.LOG_POINT.TYPE);
                lp.type = type;
                Integer saleType = (Integer) obj.get(LogdbKey.LOG_POINT.SALE_TYPE);
                lp.saleType = saleType;
                Integer freePointType = (Integer) obj.get(LogdbKey.LOG_POINT.FREE_POINT_TYPE);
                lp.freePointType = freePointType;
                String time = (String) obj.get(LogdbKey.LOG_POINT.TIME);
                lp.time = time;
                String partnerId = (String) obj.get(LogdbKey.LOG_POINT.PARTNER_ID);;
                lp.partnerId = partnerId;
                //thanhdd edit 17/11/2016
                int isAdmin = 0;
                for (int i = 0; i < listSysAcc.size(); i++) {
                    user = listSysAcc.get(i);
                    if (user instanceof SystemAccount) {
                        account = (SystemAccount) user;
                    }
                    if (lp.partnerId != null) {
                        if (lp.partnerId.equals(account.id)) {
                            isAdmin = 1;
                            break;
                        }
                    }
                }
                lp.isAdmin = isAdmin;
                Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
                lp.point = point;
                Integer beforePoint = (Integer) obj.get(LogdbKey.LOG_POINT.BEFORE_POINT);
                lp.beforePoint = beforePoint;
                Integer afterPoint = (Integer) obj.get(LogdbKey.LOG_POINT.AFTER_POINT);
                lp.afterPoint = afterPoint;
                String ip = (String) obj.get(LogdbKey.LOG_POINT.IP);
                lp.ip = ip;
                result.add(lp);
                Util.addDebugLog("listLog =============================== LogPoint ADDEDD " + lp.toJsonObject());
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return data;
    }

    public static boolean addLog(String userId, int type, String partnerId, Date time, String ip, Point point) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_POINT.USER_ID, userId);
            addObj.append(LogdbKey.LOG_POINT.TYPE, type);
            if (partnerId != null) {
                addObj.append(LogdbKey.LOG_POINT.PARTNER_ID, partnerId);
            }
            addObj.append(LogdbKey.LOG_POINT.BEFORE_POINT, point.beforePoint);
            addObj.append(LogdbKey.LOG_POINT.AFTER_POINT, point.afterPoint);
            addObj.append(LogdbKey.LOG_POINT.POINT, point.point);
            addObj.append(LogdbKey.LOG_POINT.TIME, DateFormat.format(time));
            addObj.append(LogdbKey.LOG_POINT.ORIGINAL_TIME, time.getTime());
            addObj.append(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, 1);
            if (ip != null) {
                addObj.append(LogdbKey.LOG_POINT.IP, ip);
            }
            coll.insert(addObj);
            if (point.point > 0) {
                UserDAO.increaseTotalPoint(userId, point.point);
            }

            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData listLog(List<String> listId, String fromTime, String toTime,
            Long type, Long fromPoint, Long toPoint, Long saleType, Long freePointId, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (type != null) {
                findObject.append(LogdbKey.LOG_POINT.TYPE, type.intValue());
            }
            if (saleType != null) {
                findObject.append(LogdbKey.LOG_POINT.SALE_TYPE, saleType.intValue());
            }
            if (freePointId != null) {
                findObject.append(LogdbKey.LOG_POINT.FREE_POINT_TYPE, freePointId.intValue());
            }
            if (fromPoint != null && toPoint != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromPoint);
                BasicDBObject lte = new BasicDBObject("$lte", toPoint);
                ands[0] = new BasicDBObject(LogdbKey.LOG_POINT.POINT, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_POINT.POINT, gte);
                findObject.append("$and", ands);
            } else {
                if (fromPoint != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromPoint);
                    findObject.append(LogdbKey.LOG_POINT.POINT, gte);
                }
                if (toPoint != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toPoint);
                    findObject.append(LogdbKey.LOG_POINT.POINT, lte);
                }
            }
            if (listId != null) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < listId.size(); i++) {
                    list.add(listId.get(i));
                }
                BasicDBObject inObj = new BasicDBObject("$in", list);
                findObject.append(LogdbKey.LOG_POINT.USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_POINT.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_POINT.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_POINT.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_POINT.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_POINT.ORIGINAL_TIME, or);
            } else if (sort == 2) {
                sortObj.append(LogdbKey.LOG_POINT.POINT, or);
            }
            DBCursor cursor;
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
                LogPoint lp = new LogPoint();
                DBObject obj = cursor.next();
                String userId = (String) obj.get(LogdbKey.LOG_POINT.USER_ID);
                lp.userId = userId;
                User u = UserDAO.getUserInfor(userId);
                String applicationId = "1";
                if (u.applicationId != null) {
                    applicationId = u.applicationId;
                }
                lp.application_id = applicationId;
                lp.applicationName = ApplicationDAO.getUniqueNameById(applicationId);
                Integer tpe = (Integer) obj.get(LogdbKey.LOG_POINT.TYPE);
                lp.type = tpe;
                Integer saleTye = (Integer) obj.get(LogdbKey.LOG_POINT.SALE_TYPE);
                lp.saleType = saleTye;
                Integer freePointType = (Integer) obj.get(LogdbKey.LOG_POINT.FREE_POINT_TYPE);
                lp.freePointType = freePointType;
                String time = (String) obj.get(LogdbKey.LOG_POINT.TIME);
                lp.time = time;
                String partnerId = (String) obj.get(LogdbKey.LOG_POINT.PARTNER_ID);
                lp.partnerId = partnerId;
                Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
                lp.point = point;
                Integer beforePoint = (Integer) obj.get(LogdbKey.LOG_POINT.BEFORE_POINT);
                lp.beforePoint = beforePoint;
                Integer afterPoint = (Integer) obj.get(LogdbKey.LOG_POINT.AFTER_POINT);
                lp.afterPoint = afterPoint;
                String ip = (String) obj.get(LogdbKey.LOG_POINT.IP);
                lp.ip = ip;
                list.add(lp);
            }
            result = new SizedListData(size, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void updateTotalPointToUser() throws EazyException {
        int count = 0;
        try {

            BasicDBObject findObj = new BasicDBObject();

            findObj.append(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, new BasicDBObject("$ne", 1));
            findObj.append(LogdbKey.LOG_POINT.POINT, new BasicDBObject("$gt", 0));
            Util.addDebugLog("updateTotalPointToUser =========================== query " + findObj.toString());
            DBCursor cursor;
            cursor = coll.find(findObj).sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            Util.addDebugLog("updateTotalPointToUser =========================== NOT added to total " + cursor.size());
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userID = (String) obj.get(LogdbKey.LOG_POINT.USER_ID);
                Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
                if (point > 0) {
                    UserDAO.increaseTotalPoint(userID, point);
                    obj.put(LogdbKey.LOG_POINT.ADDED_TO_TOTAL, 1);
                    coll.save(obj);
                    count++;
                    if (count % 300 == 0) {
                        Util.addDebugLog("updateTotalPointToUser ==================== " + count);
                        Thread.sleep(2000);
                    }
                }
            }
            Util.addDebugLog("updateTotalPointToUser =========================== DONE ");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    //thanhdd fixbug 4294
    public static SizedListData listLogTotalPoint(String userId) throws EazyException {
        SizedListData result = new SizedListData();
        try {

            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(LogdbKey.LOG_POINT.USER_ID, userId);
            }
            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            }
            List<IEntity> list = new ArrayList<>();
            int totalPoint = 0;
            while (cursor.hasNext()) {
                LogPoint lp = new LogPoint();
                DBObject obj = cursor.next();
                String userId1 = (String) obj.get(LogdbKey.LOG_POINT.USER_ID);
                lp.userId = userId1;
                Integer tpe = (Integer) obj.get(LogdbKey.LOG_POINT.TYPE);
                lp.type = tpe;
                Integer saleTye = (Integer) obj.get(LogdbKey.LOG_POINT.SALE_TYPE);
                lp.saleType = saleTye;
                Integer freePointType = (Integer) obj.get(LogdbKey.LOG_POINT.FREE_POINT_TYPE);
                lp.freePointType = freePointType;
                String time = (String) obj.get(LogdbKey.LOG_POINT.TIME);
                lp.time = time;
                String partnerId = (String) obj.get(LogdbKey.LOG_POINT.PARTNER_ID);
                lp.partnerId = partnerId;
                Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
                lp.point = point;
                Integer beforePoint = (Integer) obj.get(LogdbKey.LOG_POINT.BEFORE_POINT);
                lp.beforePoint = beforePoint;
                Integer afterPoint = (Integer) obj.get(LogdbKey.LOG_POINT.AFTER_POINT);
                lp.afterPoint = afterPoint;
                String ip = (String) obj.get(LogdbKey.LOG_POINT.IP);
                lp.ip = ip;
                if (lp.point > 0) {
                    list.add(lp);
                    totalPoint += lp.point;
                }

            }
            result = new SizedListData(null, list);
            result.totalPoint = totalPoint;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //khanhdd 5258
    public static int getTotalPoint(String userId) {
        BasicDBObject findObj = new BasicDBObject();
        if (userId != null) {
            findObj.append(LogdbKey.LOG_POINT.USER_ID, userId);
        }
        DBCursor cursor;
        if (!findObj.isEmpty()) {
            cursor = coll.find(findObj).sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
        } else {
            cursor = coll.find().sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
        }
        List<IEntity> list = new ArrayList<>();
        int totalPoint = 0;
        while (cursor.hasNext()) {
            LogPoint lp = new LogPoint();
            DBObject obj = cursor.next();
            String userId1 = (String) obj.get(LogdbKey.LOG_POINT.USER_ID);
            lp.userId = userId1;
            Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
            lp.point = point;
            if (lp.point > 0) {
                list.add(lp);
                totalPoint += lp.point;
            }
        }
        return totalPoint;
    }

    //thanhdd fixbug 4294
    public static SizedListData listLogMoneyTradePoint(String userId) throws EazyException {
        SizedListData result = new SizedListData();
        try {

            BasicDBObject findObj = new BasicDBObject();
            if (userId != null) {
                findObj.append(LogdbKey.LOG_POINT.USER_ID, userId);
            }
            DBCursor cursor;
            if (!findObj.isEmpty()) {
                cursor = coll.find(findObj).sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            } else {
                cursor = coll.find().sort(new BasicDBObject(LogdbKey.LOG_POINT.ORIGINAL_TIME, -1));
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                LogPoint lp = new LogPoint();
                DBObject obj = cursor.next();
                //String userId = (String) obj.get(LogdbKey.LOG_POINT.USER_ID);
                //lp.userId = userId;
                Integer tpe = (Integer) obj.get(LogdbKey.LOG_POINT.TYPE);
                lp.type = tpe;
                Integer saleTye = (Integer) obj.get(LogdbKey.LOG_POINT.SALE_TYPE);
                lp.saleType = saleTye;
                Integer freePointType = (Integer) obj.get(LogdbKey.LOG_POINT.FREE_POINT_TYPE);
                lp.freePointType = freePointType;
                String time = (String) obj.get(LogdbKey.LOG_POINT.TIME);
                lp.time = time;
                String partnerId = (String) obj.get(LogdbKey.LOG_POINT.PARTNER_ID);
                lp.partnerId = partnerId;
                Integer point = (Integer) obj.get(LogdbKey.LOG_POINT.POINT);
                lp.point = point;
                Integer beforePoint = (Integer) obj.get(LogdbKey.LOG_POINT.BEFORE_POINT);
                lp.beforePoint = beforePoint;
                Integer afterPoint = (Integer) obj.get(LogdbKey.LOG_POINT.AFTER_POINT);
                lp.afterPoint = afterPoint;
                String ip = (String) obj.get(LogdbKey.LOG_POINT.IP);
                lp.ip = ip;
                if (lp.type == LogdbKey.PURPOSE_TYPE.TRADE_PONT_TO_MONEY) {
                    list.add(lp);
                }
                Util.addDebugLog(ip);
            }
            result = new SizedListData(null, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
