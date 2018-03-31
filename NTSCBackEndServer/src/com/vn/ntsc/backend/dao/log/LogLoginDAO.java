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
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogLogin;

/**
 *
 * @author RuAc0n
 */
public class LogLoginDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_LOGIN_COLLECTION);
            coll.createIndex(new BasicDBObject(LogdbKey.LOG_LOGIN.USER_ID, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String getLastTimeLogin(String userId) throws EazyException {
        String result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.LOG_LOGIN.USER_ID, userId);
            BasicDBObject sort = new BasicDBObject(LogdbKey.LOG_LOGIN.TIME, -1);
            DBCursor cursor = coll.find(findObj).sort(sort).limit(1);
            if(cursor.size() > 0){
                DBObject obj = cursor.next();
                result = (String) obj.get(LogdbKey.LOG_LOGIN.TIME);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static SizedListData listLog(List<String> listId, String fromTime, String toTime,
            Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(LogdbKey.LOG_LOGIN.USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_LOGIN.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_LOGIN.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_LOGIN.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_LOGIN.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_LOGIN.TIME, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if(!isCsv)
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            List<IEntity> list = new ArrayList<IEntity>();
            while (cursor.hasNext()) {
                LogLogin ll = new LogLogin();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(LogdbKey.LOG_LOGIN.USER_ID);
                if(uId != null){
                    ll.userId = uId;
                    String time = dbO.getString(LogdbKey.LOG_LOGIN.TIME);
                    ll.time = time;
                    String ip = dbO.getString(LogdbKey.LOG_LOGIN.IP);
                    ll.ip = ip;

                    list.add(ll);
                }
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
