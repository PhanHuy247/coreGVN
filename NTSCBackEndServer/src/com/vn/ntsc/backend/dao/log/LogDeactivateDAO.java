/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
import com.vn.ntsc.backend.entity.impl.log.LogDeactivate;

/**
 *
 * @author RuAc0n
 */
public class LogDeactivateDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_DEACTIVATE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static SizedListData listLog(List<String> listId, String fromTime, String toTime,
            Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(LogdbKey.LOG_DEACTIVATE.USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_DEACTIVATE.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_DEACTIVATE.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_DEACTIVATE.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_DEACTIVATE.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_DEACTIVATE.TIME, or);
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
                LogDeactivate ll = new LogDeactivate();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(LogdbKey.LOG_DEACTIVATE.USER_ID);
                if(uId != null){
                    ll.userId = uId;
                    String time = dbO.getString(LogdbKey.LOG_DEACTIVATE.TIME);
                    ll.time = time;
                    String comment = dbO.getString(LogdbKey.LOG_DEACTIVATE.COMMENT);
                    ll.comment = comment;
                    String ip = dbO.getString(LogdbKey.LOG_DEACTIVATE.IP);
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
