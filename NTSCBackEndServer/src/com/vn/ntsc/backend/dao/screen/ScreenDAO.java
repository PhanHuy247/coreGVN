/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.screen;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.screen.Screen;

/**
 *
 * @author RuAc0n
 */
public class ScreenDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.SCREEN_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String name, String path, String title, int flag, String groupId, String controller, int order) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.SCREEN.TITLE, title);
            insertObj.put(SettingdbKey.SCREEN.FLAG, flag);
            insertObj.put(SettingdbKey.SCREEN.NAME, name);
            insertObj.put(SettingdbKey.SCREEN.PATH, path);
            insertObj.put(SettingdbKey.SCREEN.GROUP_ID, groupId);
            insertObj.put(SettingdbKey.SCREEN.CONTROLLER, controller);
            insertObj.put(SettingdbKey.SCREEN.ORDER, order);
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.SCREEN.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String name, String path, String title, int flag, String groupId, String controller, int order) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.SCREEN.TITLE, title);
            updateObj.put(SettingdbKey.SCREEN.FLAG, flag);
            updateObj.put(SettingdbKey.SCREEN.NAME, name);
            updateObj.put(SettingdbKey.SCREEN.PATH, path);
            updateObj.put(SettingdbKey.SCREEN.GROUP_ID, groupId);
            updateObj.put(SettingdbKey.SCREEN.CONTROLLER, controller);
            updateObj.put(SettingdbKey.SCREEN.ORDER, order);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean remove(String id) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.ID, oId);
            coll.remove(findObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getAll(Long flg) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.SCREEN.ORDER, 1);
            if (flg != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.FLAG, flg.intValue());
                cursor = coll.find(findObj).sort(sort);
            } else {
                cursor = coll.find().sort(sort);
            }

            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.SCREEN.ID).toString();
                String title = obj.getString(SettingdbKey.SCREEN.TITLE);
                Integer flag = obj.getInt(SettingdbKey.SCREEN.FLAG);
                String name = obj.getString(SettingdbKey.SCREEN.NAME);
                String path = obj.getString(SettingdbKey.SCREEN.PATH);
                String groupId = obj.getString(SettingdbKey.SCREEN.GROUP_ID);
                String controller = obj.getString(SettingdbKey.SCREEN.CONTROLLER);
                Integer order = obj.getInt(SettingdbKey.SCREEN.ORDER);
                result.add(new Screen(id, title, flag, name, path, groupId, controller, order));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> removeByGroupId(String groupId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.GROUP_ID, groupId);
            DBCursor cursor = coll.find(findObj);

            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.SCREEN.ID).toString();
                result.add(id);
            }
            coll.remove(findObj);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static List<IEntity> getScreenByGroupId(String groupId) throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.GROUP_ID, groupId);
            BasicDBObject sort = new BasicDBObject(SettingdbKey.SCREEN.ORDER, 1);
            DBCursor cursor = coll.find(findObj).sort(sort);

            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                Integer flag = obj.getInt(SettingdbKey.SCREEN.FLAG);
                if(flag == Constant.FLAG.ON){
                    String id = obj.getObjectId(SettingdbKey.SCREEN.ID).toString();
                    String title = obj.getString(SettingdbKey.SCREEN.TITLE);
                    String name = obj.getString(SettingdbKey.SCREEN.NAME);
                    String path = obj.getString(SettingdbKey.SCREEN.PATH);
                    String controller = obj.getString(SettingdbKey.SCREEN.CONTROLLER);
                    Integer order = obj.getInt(SettingdbKey.SCREEN.ORDER);
                    result.add(new Screen(id, title, flag, name, path, groupId, controller, order));
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getGroupId(String id) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.SCREEN.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null)
                result = (String) obj.get(SettingdbKey.SCREEN.GROUP_ID);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
}
