/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author DuongLTD
 */
public class RequestDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.REQUEST_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }

    public static boolean isRequestExist(String requestId, String reciverId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject obj = new BasicDBObject(UserdbKey.REQUEST.REQUEST_ID, requestId);
            obj.append(UserdbKey.REQUEST.RECIVER_ID, reciverId);
            DBObject doc = coll.findOne(obj);
            if (doc != null) {
                result = true;
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean completeRequest(String requestId, String reciverId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject obj = new BasicDBObject(UserdbKey.REQUEST.REQUEST_ID, requestId);
            obj.append(UserdbKey.REQUEST.RECIVER_ID, reciverId);
            Date date = new Date();
            obj.append(UserdbKey.REQUEST.TIME_REQUEST, date.getTime());
            DBObject resultO = coll.findOne(obj);
            if (resultO == null) {
                coll.insert(obj);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeRequest(String reciverId, String requestId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject obj = new BasicDBObject(UserdbKey.REQUEST.RECIVER_ID, reciverId);
            obj.append(UserdbKey.REQUEST.REQUEST_ID, requestId);
            DBCursor cursor = coll.find(obj);
            while (cursor.hasNext()) {
                DBObject request = cursor.next();
                coll.remove(request);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getRequestList(String userId) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.REQUEST.RECIVER_ID, userId);
            BasicDBObject sort = new BasicDBObject(UserdbKey.REQUEST.TIME_REQUEST, -1);
            DBCursor requestList = coll.find(updateQuery).sort(sort);
            while (requestList.hasNext()) {
                DBObject request = requestList.next();
                String requestId = (String) request.get(UserdbKey.REQUEST.REQUEST_ID);
                result.add(requestId);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
