/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.backlist.SizedListData;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.IEntity;
import com.vn.ntsc.otherservice.entity.impl.BannedWord;

/**
 *
 * @author RuAc0n
 */
public class ReplaceWordDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getSettingDB().getCollection(SettingdbKey.REPLACE_WORD_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String insert(String word, int flag) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, word);
            insertObj.put(SettingdbKey.REPLACE_WORD.FLAG, flag);
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.REPLACE_WORD.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String word, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, word);
            updateObj.put(SettingdbKey.REPLACE_WORD.FLAG, flag);
            coll.update(findObj, updateObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delete(String id) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.ID, oId);
            coll.remove(findObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData getList(Integer flag, Long skip, Long take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, 1);
            if (flag != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.FLAG, flag.intValue());
                cursor = coll.find(findObj).sort(sort);
            } else {
                cursor = coll.find().sort(sort);
            }
            int size = cursor.size();
            if (skip != null && take != null) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.REPLACE_WORD.ID).toString();
                String word = obj.getString(SettingdbKey.REPLACE_WORD.WORD);
                Integer flg = obj.getInt(SettingdbKey.REPLACE_WORD.FLAG);
                list.add(new BannedWord(id, word, flg));
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean changeVersion(int plus) {
        BasicDBObject query = new BasicDBObject(SettingdbKey.REPLACE_WORD.VERSION, new BasicDBObject("$exists", true));
        DBObject obj = coll.findOne(query);

        if (obj == null) {
            BasicDBObject insertObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.VERSION, plus);
            coll.insert(insertObj);
            return true;
        } else {
            BasicDBObject addter = new BasicDBObject("$inc",
                    new BasicDBObject(SettingdbKey.REPLACE_WORD.VERSION, plus));

            coll.update(obj, addter);
            return true;
        }
    }

    public static int getLastestVersion() throws EazyException {
        int result = 0;
        try {
            BasicDBObject query = new BasicDBObject(SettingdbKey.REPLACE_WORD.VERSION, new BasicDBObject("$exists", true));
            BasicDBObject obj = (BasicDBObject) coll.findOne(query);
            if (obj != null) {
                result = obj.getInt(SettingdbKey.REPLACE_WORD.VERSION);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
