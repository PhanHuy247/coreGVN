/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Tag;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;

/**
 *
 * @author hoangnh
 */
public class BuzzTagDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.BUZZ_TAG_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static Boolean addTagList(String buzzId, List<String> tagList) throws EazyException {
        Boolean result = false;
        try {
            Util.addDebugLog("===========addTag========");
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBList arr = new BasicDBList();
                for(String str: tagList){
                    BasicDBObject item = new BasicDBObject();
                    item.append(BuzzdbKey.BUZZ_TAG.USER_ID, str);
                    item.append(BuzzdbKey.BUZZ_TAG.TAG_FLAG, Constant.FLAG.ON);
                    arr.add(item);
                }
                BasicDBObject insertObj = new BasicDBObject();
                insertObj.append(BuzzdbKey.BUZZ_TAG.ID, id);
                insertObj.append(BuzzdbKey.BUZZ_TAG.TAG_LIST, arr);
                Util.addDebugLog("===========insertObj========"+insertObj);
                coll.insert(insertObj);
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Boolean addTag(String buzzId, String userId){
        Boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_TAG.ID, id);
                
                BasicDBObject item = new BasicDBObject();
                item.append(BuzzdbKey.BUZZ_TAG.USER_ID, userId);
                item.append(BuzzdbKey.BUZZ_TAG.TAG_FLAG, Constant.FLAG.ON);
                
                BasicDBObject updateCommand = new BasicDBObject("$addToSet", new BasicDBObject(BuzzdbKey.BUZZ_TAG.TAG_LIST, item) );
                coll.update(updateQuery, updateCommand, true, false);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Boolean updateTag(String buzzId, JSONArray tagList) throws EazyException {
        Boolean result = false;
        try {
            Util.addDebugLog("===========updateTag========");
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_TAG.ID, id);
                
                
                BasicDBList arr = new BasicDBList();
                for (int i = 0; i < tagList.size(); i++) {
                    String userId = (String) tagList.get(i);
                    BasicDBObject item = new BasicDBObject();
                    item.append(BuzzdbKey.BUZZ_TAG.USER_ID, userId);
                    item.append(BuzzdbKey.BUZZ_TAG.TAG_FLAG, Constant.FLAG.ON);
                    arr.add(item);
                }
                BasicDBObject insertObj = new BasicDBObject();
                insertObj.append(BuzzdbKey.BUZZ_TAG.TAG_LIST, arr);

                BasicDBObject updateCommand = new BasicDBObject("$set", insertObj );
                coll.update(updateQuery, updateCommand, true, false);
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, List<Tag>> getListTag(List<Buzz> lBuzz) throws EazyException{
        Map<String, List<Tag>> resutl = new TreeMap<>();
        try{
            for (Buzz lBuzz1 : lBuzz) {
                Boolean isValid = ObjectId.isValid(lBuzz1.buzzId);
                List<Tag> listTag = new ArrayList<>();
                if(isValid){
                    ObjectId id = new ObjectId(lBuzz1.buzzId);
                    DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_TAG.ID, id);
                    DBObject obj = coll.findOne(query);
                    
                    if(obj != null){
                        BasicDBList tag = (BasicDBList) obj.get(BuzzdbKey.BUZZ_TAG.TAG_LIST);
                        if(tag != null){
                            for(Object item : tag){
                                BasicDBObject temp = (BasicDBObject) item;
                                String userId = (String) temp.get(BuzzdbKey.BUZZ_TAG.USER_ID);
                                Integer flag = temp.getInt(BuzzdbKey.BUZZ_TAG.TAG_FLAG);
                                if(flag == Constant.FLAG.ON){
                                    Tag tagData = new Tag();
                                    tagData.userId = userId;
                                    tagData.flag = flag;
                                    listTag.add(tagData);
                                }
                            }
                        }
                    }
                }
                resutl.put(lBuzz1.buzzId, listTag);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }
    
    public static List<Tag> getTag(String buzzId) throws EazyException{
        List<Tag> resutl = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_TAG.ID, id);
            DBObject obj = coll.findOne(query);

            if(obj != null){
                BasicDBList tag = (BasicDBList) obj.get(BuzzdbKey.BUZZ_TAG.TAG_LIST);
                if(tag != null){
                    for(Object item : tag){
                        BasicDBObject temp = (BasicDBObject) item;
                        String userId = (String) temp.get(BuzzdbKey.BUZZ_TAG.USER_ID);
                        Integer flag = temp.getInt(BuzzdbKey.BUZZ_TAG.TAG_FLAG);
                        if(flag == Constant.FLAG.ON){
                            Tag tagData = new Tag();
                            tagData.userId = userId;
                            tagData.flag = flag;
                            resutl.add(tagData);
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }

    public static List<String> getListBuzzTaggedByUserId(String userId) {
        List<String> lstId = new ArrayList<>();
        DBObject query = new BasicDBObject();
        query.put(BuzzdbKey.BUZZ_TAG.USER_ID, userId);
        query.put(BuzzdbKey.BUZZ_TAG.TAG_FLAG, 1);
        DBObject elemMatch = new BasicDBObject("$elemMatch", query);
        DBObject findObject = new BasicDBObject(BuzzdbKey.BUZZ_TAG.TAG_LIST ,elemMatch);
        DBCursor cursor = coll.find(findObject);
//        Util.addDebugLog("------------ get tagged buzz query : "+findObject);
        while(cursor.hasNext()){
            DBObject obj = cursor.next();
            lstId.add(obj.get(BuzzdbKey.BUZZ_TAG.ID).toString());
        }
        return lstId;
    }
}
