/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.settingdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
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
import test.db.DBTest;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.Role;

/**
 *
 * @author duyetpt
 */
public class RoleDB {

    private static DB db;
    private static DBCollection coll;

    static {
        try {
            db = DBTest.mongo.getDB(SettingdbKey.DB_NAME);
            coll = db.getCollection(SettingdbKey.ROLE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String insert(String name) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.ROLE.NAME, name);
            DBObject obj = coll.findOne(insertObj);
            if (obj != null) {
                throw new EazyException(ErrorCode.EXISTS_DATA);
            }
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.ROLE.ID).toString();
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String name) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE.NAME, name);
            BasicDBObject neObj = new BasicDBObject("$ne", oId);
            findObj.append(SettingdbKey.ROLE.ID, neObj);
            DBObject object = coll.findOne(findObj);
            if (object != null) {
                throw new EazyException(ErrorCode.EXISTS_DATA);
            }
            findObj = new BasicDBObject(SettingdbKey.ROLE.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.ROLE.NAME, name);;
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean remove(String id) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ROLE.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer specialFlag = (Integer) obj.get(SettingdbKey.ROLE.SPECIAL_FLAG);
                if (specialFlag != null && specialFlag == Constant.FLAG.ON) {
                    throw new EazyException(ErrorCode.UNKNOWN_ERROR);
                }
                coll.remove(findObj);
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void removeByName(String name) {
        BasicDBObject obj = new BasicDBObject(SettingdbKey.ROLE.NAME, name);
        coll.remove(obj);
    }

    public static List<IEntity> getAll(Integer flag) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            DBCursor cursor = null;
            if (flag != null) {
                BasicDBObject neObj = new BasicDBObject("$ne", flag.intValue());
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE.SPECIAL_FLAG, neObj);
                cursor = coll.find(findObj);
            } else {
                cursor = coll.find();
            }
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.ROLE.ID).toString();
                String name = obj.getString(SettingdbKey.ROLE.NAME);
                result.add(new Role(id, name));
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static IEntity getDetail(String id) throws EazyException {
        IEntity result = new Role(null, null);
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ROLE.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String name = (String) obj.get(SettingdbKey.ROLE.NAME);
                result = new Role(id, name);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getRoleId(int specialFlag) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.ROLE.SPECIAL_FLAG, specialFlag);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String id = ((ObjectId) obj.get(SettingdbKey.ROLE.ID)).toString();
                result = id;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addSpecialFlag(String name, Integer flag) throws EazyException {
        boolean result = false;
        try {
            // command search
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.ROLE.NAME, name);
            DBObject dboject = coll.findOne(findObject);

            if (dboject != null) {
                BasicDBObject updateObj = new BasicDBObject(SettingdbKey.ROLE.SPECIAL_FLAG, flag);
                BasicDBObject setOBj = new BasicDBObject("$set", updateObj);
                coll.update(findObject, setOBj);
            }
        } catch (Exception ex) {
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
