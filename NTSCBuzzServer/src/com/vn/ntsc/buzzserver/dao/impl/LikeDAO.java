/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import java.util.TreeMap;

/**
 *
 * @author DuongLTD
 */
public class LikeDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.BUZZ_LIKE_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static void getLikeBuzz(Map<String, Buzz> mBuzz,String currentUserId ) throws EazyException{
        try{
            List<ObjectId> listBuzzId = new ArrayList<>();
            for (Map.Entry pairs : mBuzz.entrySet()) {
                listBuzzId.add(new ObjectId(pairs.getKey().toString()));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.BUZZ_LIKE.ID).in(listBuzzId).get();
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_LIKE.ID);
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject liker = (BasicDBObject) likeBuzz1;
                        String likeId = liker.getString(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = liker.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(currentUserId) && flag == Constant.FLAG.ON){
                            Buzz buzz = mBuzz.get(id.toString());
                            buzz.isLike = Constant.FLAG.ON;
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static Map<String, Buzz> getMapLikeBuzz(Map<String, Buzz> mBuzz,String currentUserId ) throws EazyException{
        Map<String, Buzz> result = new TreeMap<>();
        try{
            Map<String, String> map = new TreeMap<>();
            List<ObjectId> listBuzzId = new ArrayList<>();
            for (Map.Entry pairs : mBuzz.entrySet()) {
                Buzz item = (Buzz) pairs.getValue();
                map.put(item.buzzId, pairs.getKey().toString());
                listBuzzId.add(new ObjectId(item.buzzId));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.BUZZ_LIKE.ID).in(listBuzzId).get();
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_LIKE.ID);
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject liker = (BasicDBObject) likeBuzz1;
                        String likeId = liker.getString(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = liker.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(currentUserId) && flag == Constant.FLAG.ON){
                            String fileId = map.get(id.toString());
                            
                            Buzz buzz = mBuzz.get(fileId);
                            buzz.isLike = Constant.FLAG.ON;
                            
                            mBuzz.put(fileId, buzz);
                        }
                    }
                }
            }
            result = mBuzz;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, Integer> getLikeValue(List<Buzz> listBuzz,String currentUserId ) throws EazyException{
        Map<String, Integer> result = new TreeMap<>();
        try{
            List<ObjectId> listBuzzId = new ArrayList<>();
            for (Buzz buzz : listBuzz) {
                listBuzzId.add(new ObjectId(buzz.buzzId));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.BUZZ_LIKE.ID).in(listBuzzId).get();
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_LIKE.ID);
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                Integer isLike = Constant.FLAG.OFF;
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject liker = (BasicDBObject) likeBuzz1;
                        String likeId = liker.getString(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = liker.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(currentUserId) && flag == Constant.FLAG.ON){
                            isLike = Constant.FLAG.ON;
                        }
                    }
                }
                result.put(id.toString(), isLike);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int checkLike(String buzzId,String currentUserId ) throws EazyException{
        int result = Constant.FLAG.OFF;
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject liker = (BasicDBObject) likeBuzz1;
                        String likeId = (String) liker.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = (Integer) liker.get(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(currentUserId)&& flag == Constant.FLAG.ON){
                            result = Constant.FLAG.ON;
                            break;
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static boolean addLike(String buzzId, String friendId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            BasicDBObject likeObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.USER_ID, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", likeObj);
            findObj.append(BuzzdbKey.BUZZ_LIKE.LIKE_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList listLike = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(listLike != null){
                    for (Object listLike1 : listLike) {
                        BasicDBObject like = (BasicDBObject) listLike1;
                        String likeId = like.getString(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        if(likeId.equals(friendId)){
                            Integer flag = (Integer) like.get(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                            if(flag == Constant.FLAG.ON){
                                String field = BuzzdbKey.BUZZ_LIKE.LIKE_LIST + ".$." + BuzzdbKey.BUZZ_LIKE.LIKE_FLAG;
                                BasicDBObject updateObj = new BasicDBObject(field, Constant.FLAG.OFF);
                                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                                coll.update(findObj, setObj);
                            }else{
                                String field = BuzzdbKey.BUZZ_LIKE.LIKE_LIST + ".$." + BuzzdbKey.BUZZ_LIKE.LIKE_FLAG;
                                BasicDBObject updateObj = new BasicDBObject(field, Constant.FLAG.ON);
                                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                                coll.update(findObj, setObj);
                            }
                        }
                    }

                }
            }else{
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
                BasicDBObject likeElement = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.USER_ID, friendId);
                likeElement.append(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG, Constant.FLAG.ON);
                BasicDBObject like = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.LIKE_LIST, likeElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", like );
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isLikeBuzz(String buzzId, String userId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject like = (BasicDBObject) likeBuzz1;
                        String likeId = like.getString(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = like.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(userId) && flag == Constant.FLAG.ON){
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkUserLikeExist(String buzzId, String userId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject like = (BasicDBObject) likeBuzz1;
                        String likeId = (String) like.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        if(likeId.equals(userId)){
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<String> getLikeList(String buzzId, String userId, String buzzOwner) throws EazyException{
        List<String> result = new ArrayList<>();
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject like = (BasicDBObject) likeBuzz1;
                        String likeId = (String) like.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = like.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(!likeId.equals(userId) && !likeId.equals(buzzOwner) && flag == Constant.FLAG.ON){
                            result.add(likeId);
                        }
                    }
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkLikeExist(String buzzId, String userId) throws EazyException{
        boolean result = false;
         try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if(likeBuzz != null){
                    for (Object likeBuzz1 : likeBuzz) {
                        BasicDBObject like = (BasicDBObject) likeBuzz1;
                        String likeId = (String) like.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = (Integer) like.get(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if(likeId.equals(userId) && flag == Constant.FLAG.ON){
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void deleteBuzz(String buzzId){
        try{
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject deleteObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID,id);
            coll.remove(deleteObj);
        }catch( Exception ex ) {
            Util.addErrorLog(ex);                
        }
    }

}

