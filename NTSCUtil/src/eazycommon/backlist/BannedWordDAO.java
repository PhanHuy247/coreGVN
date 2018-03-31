/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.backlist;

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
import org.bson.types.ObjectId;


/**
 *
 * @author RuAc0n
 */
public class BannedWordDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.BANNED_WORD_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static String insert(String word, int flag) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, word);
            DBObject obj = coll.findOne(findObj);
            if(obj == null){
                DBObject insertObj = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, word);
                insertObj.put(SettingdbKey.BANNED_WORD.FLAG, flag);
                coll.insert(insertObj);
                result = insertObj.get(SettingdbKey.BANNED_WORD.ID).toString();
            }
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
            DBObject query = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, word);
            query.put(SettingdbKey.BANNED_WORD.ID, new BasicDBObject("$ne", oId));
            DBObject obj = coll.findOne(query);
            if(obj != null)
                throw new EazyException(ErrorCode.EXISTS_DATA);
            DBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, word);
            updateObj.put(SettingdbKey.BANNED_WORD.FLAG, flag);
            coll.update(findObj, updateObj);
            result = true;
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
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
            DBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.ID, oId);
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
            DBCursor cursor;
//            BasicDBObject sort = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, 1);
            if (flag != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, flag);
//                cursor = coll.find(findObj).sort(sort);
                cursor = coll.find(findObj);
            } else {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, new BasicDBObject("$exists", true));
//                cursor = coll.find(findObj).sort(sort);
                cursor = coll.find(findObj);
            }
            int size = cursor.size();
            if (skip != null && take != null) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.BANNED_WORD.ID).toString();
                String word = obj.getString(SettingdbKey.BANNED_WORD.WORD);
                Integer flg = obj.getInt(SettingdbKey.BANNED_WORD.FLAG);
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
        BasicDBObject query = new BasicDBObject(SettingdbKey.BANNED_WORD.VERSION, new BasicDBObject("$exists", true));
        DBObject obj = coll.findOne(query);

        if (obj == null) {
            BasicDBObject insertObj = new BasicDBObject(SettingdbKey.BANNED_WORD.VERSION, plus);
            coll.insert(insertObj);
            return true;
        } else {
            BasicDBObject addter = new BasicDBObject("$inc",
                    new BasicDBObject(SettingdbKey.BANNED_WORD.VERSION, plus));

            coll.update(obj, addter);
            return true;
        }
    }

    public static int getLastestVersion() throws EazyException {
        int result = 0;
        try {
            BasicDBObject query = new BasicDBObject(SettingdbKey.BANNED_WORD.VERSION, new BasicDBObject("$exists", true));
            BasicDBObject obj = (BasicDBObject) coll.findOne(query);
            if (obj != null) {
                result = obj.getInt(SettingdbKey.BANNED_WORD.VERSION);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
     public static List<String> getList(Integer flag) throws EazyException {
        List<String> result = new ArrayList();
        try {
            DBCursor cursor;
//            BasicDBObject sort = new BasicDBObject(SettingdbKey.BANNED_WORD.WORD, 1);
            //if (flag != null) {
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, 1);
//                cursor = coll.find(findObj).sort(sort);
                cursor = coll.find(findObj);
//            } else {
//                BasicDBObject findObj = new BasicDBObject(SettingdbKey.BANNED_WORD.FLAG, new BasicDBObject("$exists", true));
////                cursor = coll.find(findObj).sort(sort);
//                cursor = coll.find(findObj);
//            }

            List<String> list = new ArrayList<>();
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                //String id = obj.getObjectId(SettingdbKey.BANNED_WORD.ID).toString();
                String word = obj.getString(SettingdbKey.BANNED_WORD.WORD);
                //Integer flg = obj.getInt(SettingdbKey.BANNED_WORD.FLAG);
                list.add(word);
            }
            result = list;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
