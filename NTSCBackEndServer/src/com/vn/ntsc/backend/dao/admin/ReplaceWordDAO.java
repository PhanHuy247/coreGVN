/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.bannedword.BannedWord;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;

/**
 *
 * @author RuAc0n
 */
public class ReplaceWordDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.REPLACE_WORD_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String insert(String word, int flag, Date dateTime) throws EazyException {
        String result = null;
        try {
            
            DBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, word);
            DBObject obj = coll.findOne(findObj);
            if (obj==null){
                DBObject insertObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, word);
                insertObj.put(SettingdbKey.REPLACE_WORD.FLAG, flag);
                insertObj.put(SettingdbKey.REPLACE_WORD.TIME, dateTime);
                coll.insert(insertObj);
                result = insertObj.get(SettingdbKey.REPLACE_WORD.ID).toString();
            } else {
                result="exist";
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, String word, int flag, Date dateTime) throws EazyException {
        boolean result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, word);
            updateObj.put(SettingdbKey.REPLACE_WORD.FLAG, flag);
            updateObj.put(SettingdbKey.REPLACE_WORD.TIME, dateTime);
            coll.update(findObj, updateObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateDateTime(String id, String word, Date dateTime) throws EazyException {
        boolean result = false;
        try {
            //ObjectId oId = new ObjectId(id);
//            DBObject findObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.ID, oId);
            
//            findObj.put(SettingdbKey.REPLACE_WORD.TIME, "")
            DBObject clause1 = new BasicDBObject(SettingdbKey.REPLACE_WORD.TIME, "");
            DBObject clause2 = new BasicDBObject(SettingdbKey.REPLACE_WORD.TIME, null);
            BasicDBList or = new BasicDBList();
            or.add(clause1);
            or.add(clause2);
            DBObject findObj = new BasicDBObject("$or", or);

            DBObject updateObj = new BasicDBObject(SettingdbKey.REPLACE_WORD.WORD, word);
            updateObj.put(SettingdbKey.REPLACE_WORD.TIME, dateTime);
            Util.addDebugLog("=========VAO DAY=========="+updateObj.toString());
            Util.addDebugLog("=========findObj=========="+findObj.toString());
            coll.update(findObj, updateObj);
            Util.addDebugLog("=========SUCCESS==========");
            result = true;
        } catch (Exception ex) {
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
    //Added by Khanhdd
        public static SizedListData getList(Integer flag, Long skip, Long take, String search_word) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBCursor cursor;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.BANNED_WORD.TIME, -1);
            if (flag != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, flag);
                //cursor = coll.find(findObj).sort(sort);
                if(search_word != null && !search_word.isEmpty()) {
                    findObj.append(SettingdbKey.REPLACE_WORD.WORD, search_word);
                
                }
                cursor = coll.find(findObj).sort(sort);
            } else {
                
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, new BasicDBObject("$exists", true));
                //cursor = coll.find(findObj).sort(sort);
                if(search_word != null && !search_word.isEmpty()) {
                    findObj.append(SettingdbKey.REPLACE_WORD.WORD, search_word);
                }
                cursor = coll.find(findObj).sort(sort);
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
        
    public static SizedListData getList(Integer flag, Long skip, Long take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.REPLACE_WORD.TIME, -1);
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
                if((obj.get(SettingdbKey.REPLACE_WORD.WORD)==null)){
                    continue;
                }
                String id = obj.getObjectId(SettingdbKey.REPLACE_WORD.ID).toString();
                String word = obj.getString(SettingdbKey.REPLACE_WORD.WORD);
                Integer flg = obj.getInt(SettingdbKey.REPLACE_WORD.FLAG);
                Date date = obj.getDate(SettingdbKey.REPLACE_WORD.TIME);
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
