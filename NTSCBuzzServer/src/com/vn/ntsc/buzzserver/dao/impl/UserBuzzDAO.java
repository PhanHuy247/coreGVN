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
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author DuongLTD
 */
public class UserBuzzDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.USER_BUZZ_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static List<Buzz> getListBuzz(List<String> listUser) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            List<ObjectId> listId = new ArrayList<>();
            for (String listUser1 : listUser) {
                listId.add(new ObjectId(listUser1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.USER_BUZZ.ID).in(listId).get();
//            query.put(BuzzdbKey.USER_BUZZ.STREAM_STATUS, new BasicDBObject("$ne", Constant.STREAM_STATUS.PENDING));
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if(listBuzz != null){
                    for(int i = listBuzz.size()-1; i >-1;i --){
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        Integer appFlag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        String streamStatus = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.STREAM_STATUS);
                        if(appFlag != null && !Constant.STREAM_STATUS.PENDING.equals(streamStatus) && !Constant.STREAM_STATUS.RECORDING.equals(streamStatus)){
                            Integer privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                            if((flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG)&& appFlag == Constant.REVIEW_STATUS_FLAG.APPROVED && (privacy == null || privacy != Constant.POST_MODE.ONLY_ME)){
                                String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                                Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                                Buzz buzz = new Buzz();
                                buzz.buzzId = buzzId;
                                buzz.lastAct = buzzTime;
                                result.add(buzz);
                            }
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

    public static List<Buzz> getListLocalBuzz(List<String> listUser, String currentUserId) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            List<ObjectId> listId = new ArrayList<>();
            for (String listUser1 : listUser) {
                listId.add(new ObjectId(listUser1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.USER_BUZZ.ID).in(listId).get();
            
            BasicDBObject data = new BasicDBObject();
            BasicDBList buzzTypeValue = new BasicDBList();
            buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.TEXT_STATUS);
            buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS);
            buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS);
            buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.MULTI_STATUS);
            buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.STREAM_STATUS);
            data.append("$in", buzzTypeValue);
            query.put(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_TYPE, data);

            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                if(listBuzz != null){
                    int count = 0;
                    for(int i = listBuzz.size()-1; i >-1; i --){
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                            Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                            if(isApp != null){
                                if((userId.equals(currentUserId) && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED){
                                    Integer buzzType = (Integer) buzzObj.getInt(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                                    if(buzzType >= 10){
                                        Buzz buzz = new Buzz();
                                        buzz.buzzId = buzzId;
                                        buzz.lastAct = buzzTime;
                                        result.add(buzz);
                                        count ++;
                                    }
                                }
                            }
                            
                        }
                        if (count == 30)
                            break;
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<Buzz> getListLocalBuzz(List<String> listUser) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            List<ObjectId> listId = new ArrayList<>();
            for (String listUser1 : listUser) {
                listId.add(new ObjectId(listUser1));
            }
            DBObject query = QueryBuilder.start(BuzzdbKey.USER_BUZZ.ID).in(listId).get();
//            BasicDBList list = new BasicDBList();
//            list.add(Constant.POST_MODE.FRIEND);
//            list.add(Constant.POST_MODE.ONLY_ME);
//            BasicDBObject in = new BasicDBObject("$nin", list);
//            query.put(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.PRIVACY, in);
            
            DBCursor cursor = coll.find(query).sort(new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_TIME, -1));
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                if(listBuzz != null){
                    int count = 0;
                    for(int i = listBuzz.size()-1; i >-1; i --){
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                            Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                            String streamStatus = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.STREAM_STATUS);
                            if(isApp != null && !Constant.STREAM_STATUS.PENDING.equals(streamStatus) && !Constant.STREAM_STATUS.RECORDING.equals(streamStatus)){
                                Integer privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                                if(isApp == Constant.REVIEW_STATUS_FLAG.APPROVED && privacy == Constant.POST_MODE.EVERYONE){
                                    Buzz buzz = new Buzz();
                                    buzz.buzzId = buzzId;
                                    buzz.lastAct = buzzTime;
                                    result.add(buzz);
                                    count ++;
                                }
                            }
                            
                        }
                        if (count == 30)
                            break;
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getListBuzz(String userId, boolean ownedBuzz, List<String> friendList) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                Boolean isFriend = friendList.contains(userId);
                if(listBuzz != null){
                    for (Object listBuzz1 : listBuzz) {
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz1;
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        String streamStatus = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.STREAM_STATUS);
                        if(isApp != null && !Constant.STREAM_STATUS.PENDING.equals(streamStatus) && !Constant.STREAM_STATUS.RECORDING.equals(streamStatus)){
                            if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                                if((ownedBuzz && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED ){
                                    Integer privacy = 0;
                                    if(buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY) != null){
                                        privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                                    }
                                    Boolean isShow = false;
                                    if(ownedBuzz){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.FRIEND && isFriend){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.EVERYONE){
                                        isShow = true;
                                    }
                                    if(isShow){
                                        String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                                        Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                                        Buzz buzz = new Buzz();
                                        buzz.buzzId = buzzId;
                                        buzz.lastAct = buzzTime;
                                        result.add(buzz);
                                    }
                                    
                                }
                            }
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
    
    public static int getBuzzNumber(String userId) throws EazyException{
        int result = 0;
        try{
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if(listBuzz != null){
                    for (Object listBuzz1 : listBuzz) {
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz1;
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        Integer buzzType = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            if(buzzType != null && buzzType != Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ && isApp == Constant.REVIEW_STATUS_FLAG.APPROVED ){
                                result ++;
                            }
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

    public static boolean delBuzz(String userId,String buzzId) throws EazyException{
        boolean result = false;
        try{
            ObjectId id = new ObjectId(userId);
//            DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
//            BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
//            BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", buzzer );
//            coll.update(findObj, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }                       
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static boolean updateBuzzActivity(String buzzId, String userId, long time, Integer isApp, Integer buzzType, Long privacy, String streamStatus) throws EazyException{
        boolean result = false;
         try{
            Integer postMode = 0;
            if(privacy != null){
                postMode = privacy.intValue();
            }
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                boolean check = false;
                
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if(listBuzz != null){
                    for (Object listBuzz1 : listBuzz) {
                        BasicDBObject buzzer = (BasicDBObject) listBuzz1;
                        String buId = (String) buzzer.get(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                        if(buId.equals(buzzId)){
                            check = true;
                            break;
                        }
                    }
                }
                if(check){
                    BasicDBObject buzzerObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                    BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzerObj);
                    findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
                    String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.BUZZ_TIME;
                    BasicDBObject updateObj = new BasicDBObject(field, time);
                    if(buzzType != null && buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS && streamStatus != null){
                        updateObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.STREAM_STATUS, streamStatus);
                    }
                    BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                    coll.update(findObj, setObj);
                }else{
                    BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TIME,time);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.POST_TIME, time);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.PRIVACY, postMode);
                    if(isApp != null)
                        buzzElement.append(BuzzdbKey.USER_BUZZ.APPROVED_FLAG, isApp);
                    if(buzzType != null){
                        buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TYPE, buzzType);
                        if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                            buzzElement.append(BuzzdbKey.USER_BUZZ.STREAM_STATUS, Constant.STREAM_STATUS.PENDING);
                        }
                    }
                    BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
                    BasicDBObject updateCommand = new BasicDBObject("$push", buzzer );
                    coll.update(findObj, updateCommand);
                }
            }else{
                BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TIME,time);
                buzzElement.append(BuzzdbKey.USER_BUZZ.POST_TIME, time);
                buzzElement.append(BuzzdbKey.USER_BUZZ.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);   
                buzzElement.append(BuzzdbKey.USER_BUZZ.PRIVACY, postMode);
                if(isApp != null)
                    buzzElement.append(BuzzdbKey.USER_BUZZ.APPROVED_FLAG, isApp);
                if(buzzType != null){
                    buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TYPE, buzzType);
                    if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                        buzzElement.append(BuzzdbKey.USER_BUZZ.STREAM_STATUS, Constant.STREAM_STATUS.PENDING);
                    }
                }
                BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", buzzer );
                coll.update(findObj, updateCommand, true, false);
            }
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateApproveFlag(String userId, String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
//            DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
//            BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
//            BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", buzzer );
//            coll.update(findObj, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, flag);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

    public static int getApprovedFlag(String userId, String buzzId) throws EazyException {
        int result = Constant.REVIEW_STATUS_FLAG.DENIED;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                for (Object buzz : listBuzz) {
                    BasicDBObject buzzObj = (BasicDBObject) buzz;
                    String buId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                    if(buId.equals(buzzId)){
                        result = buzzObj.getInt(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getListAllBuzz(List<String> friendList, String currentUserId, List<String> blockList) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            List<ObjectId> listId = new ArrayList<>();
            for (String userId : blockList) {
                listId.add(new ObjectId(userId));
            }
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.USER_BUZZ.ID, new BasicDBObject("$nin", listId));
//            findObj.append(BuzzdbKey.USER_BUZZ.STREAM_STATUS, new BasicDBObject("$ne", Constant.STREAM_STATUS.PENDING));
            
            DBCursor cursor = coll.find(findObj).sort(new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_TIME, -1));
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                Boolean isCurrentUser = userId.equals(currentUserId);
                Boolean isFriend = friendList.contains(userId);
                if(listBuzz != null){
                    int count = 0;
                    for(int i = listBuzz.size()-1; i >-1; i --){
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                            Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                            String streamStatus = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.STREAM_STATUS);
                            if(isApp != null && !Constant.STREAM_STATUS.PENDING.equals(streamStatus) && !Constant.STREAM_STATUS.RECORDING.equals(streamStatus)){
                                if((isCurrentUser && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED){
                                    Integer privacy = 0;
                                    if(buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY) != null){
                                        privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                                    }
                                    Boolean isShow = false;
                                    if(privacy == Constant.POST_MODE.EVERYONE){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.FRIEND && (isFriend || isCurrentUser)){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.ONLY_ME && isCurrentUser){
                                        isShow = true;
                                    }
                                    
                                    Integer buzzType = (Integer) buzzObj.getInt(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                                    if(buzzType >= 10 && isShow){
                                        Buzz buzz = new Buzz();
                                        buzz.buzzId = buzzId;
                                        buzz.lastAct = buzzTime;
                                        result.add(buzz);
                                        count ++;
                                    }
                                }
                            }
                            
                        }
                        if (count == 30)
                            break;
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getListStreamBuzz(List<String> friendList, String currentUserId, Boolean isFav) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            DBObject sortObject = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_TIME, -1);
            DBCursor cursor;
            if (isFav != null && isFav){
                List<ObjectId> lstId = new ArrayList<>();
                for (String id : friendList){
                    lstId.add(new ObjectId(id));
                }
                DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, new BasicDBObject("$in", lstId));
                cursor = coll.find(findObj).sort(sortObject);
            }
            else {
                cursor = coll.find().sort(sortObject);
            }
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                Boolean isCurrentUser = userId.equals(currentUserId);
                Boolean isFriend = friendList.contains(userId);
                if(listBuzz != null){
                    int count = 0;
                    for(int i = listBuzz.size()-1; i >-1; i --){
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                            Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG); 
                            String streamStatus = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.STREAM_STATUS);
                            if(isApp != null && Constant.STREAM_STATUS.ON.equals(streamStatus)){
                                if((isCurrentUser && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED){
                                    Integer privacy = 0;
                                    if(buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY) != null){
                                        privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                                    }
                                    Boolean isShow = false;
                                    if(privacy == Constant.POST_MODE.EVERYONE){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.FRIEND && (isFriend || isCurrentUser)){
                                        isShow = true;
                                    }else if(privacy == Constant.POST_MODE.ONLY_ME && isCurrentUser){
                                        isShow = true;
                                    }
                                    
                                    Integer buzzType = (Integer) buzzObj.getInt(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                                    if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS && isShow){
                                        Buzz buzz = new Buzz();
                                        buzz.buzzId = buzzId;
                                        buzz.lastAct = buzzTime;
                                        result.add(buzz);
                                        count ++;
                                    }
                                }
                            }
                            
                        }
                        if (count == 30)
                            break;
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getImageVideoAudioBuzz (List<String> friendList, String currentUserId) throws EazyException{
        List<Buzz> result = new ArrayList<>();
        try{
            Boolean isValid = ObjectId.isValid(currentUserId);
            if(isValid){
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.USER_BUZZ.ID, new ObjectId(currentUserId));

                DBCursor cursor = coll.find(findObj).sort(new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_TIME, -1));
                while(cursor.hasNext()){
                    DBObject obj = cursor.next();
                    BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                    String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                    Boolean isCurrentUser = userId.equals(currentUserId);
                    Boolean isFriend = friendList.contains(userId);
                    if(listBuzz != null){
                        int count = 0;
                        for(int i = listBuzz.size()-1; i >-1; i --){
                            BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                            Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                            if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                                String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                                Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                                Integer isApp = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                                if(isApp != null){
                                    if((isCurrentUser && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED){
                                        Integer privacy = 0;
                                        if(buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY) != null){
                                            privacy = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.PRIVACY);
                                        }
                                        Boolean isShow = false;
                                        if(privacy == Constant.POST_MODE.EVERYONE){
                                            isShow = true;
                                        }else if(privacy == Constant.POST_MODE.FRIEND && (isFriend || isCurrentUser)){
                                            isShow = true;
                                        }else if(privacy == Constant.POST_MODE.ONLY_ME && isCurrentUser){
                                            isShow = true;
                                        }

                                        Integer buzzType = (Integer) buzzObj.getInt(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                                        if(buzzType >= 10 && isShow && buzzType != Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                                            Buzz buzz = new Buzz();
                                            buzz.buzzId = buzzId;
                                            buzz.lastAct = buzzTime;
                                            result.add(buzz);
                                            count ++;
                                        }
                                    }
                                }

                            }
                            if (count == 30)
                                break;
                        }
                    }
                }
            }else{
                throw new EazyException(ErrorCode.INVALID_ACCOUNT);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateStreamBuzzStatus(List<Buzz> listBuzz, String status){
        try{
            for(Buzz item: listBuzz){
                
                ObjectId id = new ObjectId(item.userId);
                BasicDBObject updateQuery = new BasicDBObject();
                updateQuery.append(BuzzdbKey.USER_BUZZ.ID, id);
                updateQuery.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_ID, item.buzzId);
                
                BasicDBObject obj = new BasicDBObject();
                obj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"$"+BuzzdbKey.USER_BUZZ.BUZZ_ID, status);
                
                BasicDBObject updateCommand = new BasicDBObject();
                updateCommand.append("$set", obj);
                
                
                coll.update(updateQuery, updateCommand, true, false);
            }
            
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
}

