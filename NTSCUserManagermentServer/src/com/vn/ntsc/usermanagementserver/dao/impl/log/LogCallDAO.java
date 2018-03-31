/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.LogdbKey;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LogCallData;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;

/**
 *
 * @author RuAc0n
 */
public class LogCallDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.LOG_CALL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    
    
    public static List<LogCallData> getCallLog(String userId, int type, List<String> blockUsers, int skip, int take) {
        List<LogCallData> result = new ArrayList<>();
        try {
            BasicDBObject obj = new BasicDBObject();
            if (type == Constant.REQUEST_CALL_TYPE.MY_REQUEST_CALL) {
                obj.append(LogdbKey.LOG_CALL.REQUEST_ID, userId);
            } else if (type == Constant.REQUEST_CALL_TYPE.PARTNER_REQUEST_CALL) {
                obj.append(LogdbKey.LOG_CALL.PARTNER_ID, userId);
            }
            BasicDBObject sortObj = new BasicDBObject(LogdbKey.LOG_CALL.START_TIME, -1);
            DBCursor cursor = coll.find(obj).sort(sortObj);
//            DBCursor cursor = coll.find(obj).skip(skip).batchSize(take);
            List<LogCallData> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject objDb = cursor.next();
                LogCallData data = new LogCallData();

                if (type == Constant.REQUEST_CALL_TYPE.MY_REQUEST_CALL) {
                    data.partnerId = (String) objDb.get(LogdbKey.LOG_CALL.PARTNER_ID);
                } else if (type == Constant.REQUEST_CALL_TYPE.PARTNER_REQUEST_CALL) {
                    data.partnerId = (String) objDb.get(LogdbKey.LOG_CALL.REQUEST_ID);
                }
                if(!DeactivateUserManager.isDeactivateUser(data.partnerId) && !blockUsers.contains(data.partnerId)){
                    data.callType = (Integer) objDb.get(LogdbKey.LOG_CALL.CALL_TYPE);
                    data.duration = new Long((Integer) objDb.get(LogdbKey.LOG_CALL.DURATION));
                    data.partnerRes = (Integer) objDb.get(LogdbKey.LOG_CALL.PARTNER_RESPOND);
                    data.startTime = (String) objDb.get(LogdbKey.LOG_CALL.START_TIME);

                    list.add(data);
                }
            }
            
            if (skip < list.size()) {
                int startIndex = skip;
                int endIndex = startIndex + take;
                if (endIndex > list.size()) {
                    endIndex = list.size();
                }
                result  = list.subList((int) startIndex, (int) endIndex);
//                UserActivityDAO.getListStatus(respondList);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
