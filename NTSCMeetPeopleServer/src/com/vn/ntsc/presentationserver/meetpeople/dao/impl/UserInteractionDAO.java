/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.dao.impl;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import java.util.LinkedList;
import java.util.List;
import org.bson.types.ObjectId;
import static com.vn.ntsc.presentationserver.meetpeople.dao.impl.UserDAO.db;

/**
 *
 * @author Administrator
 */
public class UserInteractionDAO {
    private static DB dbInteraction;
    private static DBCollection coll;
    static{
        try{
            db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.USER_INTERACTION_COLLECTION);
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static boolean checkExistInteractionList(String userId){
        DBObject find = new BasicDBObject(UserdbKey.USER_INTERACTION.ID, new ObjectId(userId));
        DBObject obj = coll.findOne(find);
        return obj != null;
    }
    
    public static boolean checkExistInteraction(String userId, String friendId){
//        DBObject split = new BasicDBObject("$unwind", "$"+UserdbKey.USER_INTERACTION.INTERACTION_LIST);
//        DBObject query = new BasicDBObject();
//        query.put(UserdbKey.USER_INTERACTION.INTERACTION_LIST+"."+UserdbKey.USER_INTERACTION.USER_ID, friendId);
//        query.put(UserdbKey.USER_INTERACTION.ID, new ObjectId(userId));
//        DBObject find = new BasicDBObject("$match", query);
//        AggregationOutput output = coll.aggregate(split, find);
//        CommandResult result = output.getCommandResult();
//        if (result.getObjectId(UserdbKey.USER_INTERACTION.ID) != null){
//            return true;
//        }
//        return false;
        
        DBObject elemMatchObj = new BasicDBObject();
        elemMatchObj.put("$elemMatch", new BasicDBObject(UserdbKey.USER_INTERACTION.USER_ID, friendId));
        DBObject findObj = new BasicDBObject();
        findObj.put(UserdbKey.USER_INTERACTION.ID, new ObjectId(userId));
        findObj.put(UserdbKey.USER_INTERACTION.INTERACTION_LIST, elemMatchObj);
        DBObject obj = coll.findOne(findObj);
        return obj != null;
    }
    
    public static void updateInteraction(String userId, String friendId){
        DBObject find = new BasicDBObject(UserdbKey.USER_INTERACTION.ID, new ObjectId(userId));
        DBObject query = new BasicDBObject(UserdbKey.USER_INTERACTION.INTERACTION_LIST, new BasicDBObject(UserdbKey.USER_INTERACTION.USER_ID, friendId));
        DBObject command = new BasicDBObject("$addToSet", query);
        coll.update(find, command);
    }
    
    public static void addInteraction(String userId, String friendId){
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject(UserdbKey.USER_INTERACTION.USER_ID, friendId));
        
        DBObject obj = new BasicDBObject();
        obj.put(UserdbKey.USER_INTERACTION.ID, new ObjectId(userId));
        obj.put(UserdbKey.USER_INTERACTION.INTERACTION_LIST, list);
        
        coll.insert(obj);
    }
    
    public static List<String> getListInteraction(String uId){
        List<String> list = new LinkedList<>();
        if (uId == null)
            return list;
        
        DBObject find = new BasicDBObject(UserdbKey.USER_INTERACTION.ID, new ObjectId(uId));
        DBObject obj = coll.findOne(find);
        if (obj != null){
            BasicDBList listId = (BasicDBList) obj.get(UserdbKey.USER_INTERACTION.INTERACTION_LIST);
            for(Object objId : listId){
                String id = (String) ((BasicDBObject) objId).get(UserdbKey.USER_INTERACTION.USER_ID);
                list.add(id);
            }
        }
        return list;
    }
}
