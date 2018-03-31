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
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.util.Util;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class BuzzViewDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.BUZZ_VIEW_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static Boolean isViewed(String buzzId, String viewFrom){
        Boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject viewObj = new BasicDBObject();
                viewObj.append(BuzzdbKey.BUZZ_VIEW.VIEW_FROM, viewFrom);
                
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.BUZZ_VIEW.ID, id);
                findObj.append(BuzzdbKey.BUZZ_VIEW.VIEW_LIST, new BasicDBObject("$elemMatch", viewObj));
                DBObject obj = coll.findOne(findObj);
                if(obj != null){
                    result = true;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static void addView(String buzzId, String userId, String ip){
        try{
            
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static void addBuzzViewInfo(String buzzId, String userId, String ip){
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_VIEW.ID, id);
                
                BasicDBObject viewElement = new BasicDBObject();
                if(userId != null){
                    viewElement.append(BuzzdbKey.BUZZ_VIEW.VIEW_FROM, userId);
                    viewElement.append(BuzzdbKey.BUZZ_VIEW.TYPE, "id");
                }else{
                    viewElement.append(BuzzdbKey.BUZZ_VIEW.VIEW_FROM, ip);
                    viewElement.append(BuzzdbKey.BUZZ_VIEW.TYPE, "ip");
                }
                BasicDBObject view = new BasicDBObject(BuzzdbKey.BUZZ_VIEW.VIEW_LIST, viewElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", view );
                coll.update(updateQuery, updateCommand, true, false);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static Integer count(String buzzId){
        Integer result = 0;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.BUZZ_VIEW.ID, id);
                DBObject obj = coll.findOne(findObj);
                if(obj != null){
                    BasicDBList viewBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_VIEW.VIEW_LIST);
                    result = viewBuzz.size();
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Map<String, Integer> getListView(List<Buzz> lBuzz){
        Map<String, Integer> result = new TreeMap<>();
        try{
            BasicDBList arr = new BasicDBList();
            for (Buzz lBuzz1 : lBuzz) {
                Boolean isValid = ObjectId.isValid(lBuzz1.buzzId);
                if(isValid){
                    ObjectId id = new ObjectId(lBuzz1.buzzId);
                    arr.add(id);
                }
            }
            BasicDBObject obj = new BasicDBObject("$in", arr);
            BasicDBObject query = new BasicDBObject(BuzzdbKey.BUZZ_VIEW.ID, obj);
            DBCursor cur = coll.find(query);
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                String buzzId = dbO.get(BuzzdbKey.BUZZ_VIEW.ID).toString();
                BasicDBList viewBuzz = (BasicDBList) dbO.get(BuzzdbKey.BUZZ_VIEW.VIEW_LIST);
                Integer count = viewBuzz.size();
                result.put(buzzId, count);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
