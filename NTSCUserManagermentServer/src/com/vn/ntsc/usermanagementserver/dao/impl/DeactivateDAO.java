/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author DuongLTD
 */
public class DeactivateDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.DEACTIVATE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addDeactiveUser(String userId, String comment, long time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.DEACTIVATE.ID, id);
            DBObject find = coll.findOne(obj);
            if(find == null){
                obj.append(UserdbKey.DEACTIVATE.DEACTIVATE_TIME, time);
                if(comment != null)
                    obj.append(UserdbKey.DEACTIVATE.DEACTIVATE_COMMENT, comment);
                obj.append(UserdbKey.DEACTIVATE.FLAG, Constant.FLAG.ON);
                coll.insert(obj);
            }else{
                BasicDBObject update = new BasicDBObject();
                update.append(UserdbKey.DEACTIVATE.DEACTIVATE_TIME, time);
                if(comment != null)
                    update.append(UserdbKey.DEACTIVATE.DEACTIVATE_COMMENT, comment);
                update.append(UserdbKey.DEACTIVATE.FLAG, Constant.FLAG.ON);
                BasicDBObject set = new BasicDBObject("$set", update);
                coll.update(find, set);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean activate(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.DEACTIVATE.ID, id);
            DBCursor cur = coll.find(obj);
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.DEACTIVATE.FLAG);
                if (flag == Constant.FLAG.ON) {
                    DBObject updatObject = new BasicDBObject("$set", new BasicDBObject(UserdbKey.DEACTIVATE.FLAG, Constant.FLAG.OFF));
                    coll.update(dbO, updatObject);
                }
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isUserDeactivate(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.DEACTIVATE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer flag = (Integer) obj.get(UserdbKey.DEACTIVATE.FLAG);
                if (flag == Constant.FLAG.ON) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getListDeactivate() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.DEACTIVATE.FLAG);
                if (flag == Constant.FLAG.ON) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.DEACTIVATE.ID)).toString();
                    result.add(userId);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
