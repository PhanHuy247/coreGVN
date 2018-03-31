/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author RuAc0n
 */
public class BlockDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.BLOCK_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addBlockList(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.BLOCK.BLOCK_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addBlockedList(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.BLOCK.BE_BLOCKED_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeBlock(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.BLOCK.BLOCK_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeBlocked(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.BLOCK.BE_BLOCKED_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$pull", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getBlockList(String userId, long skip, long take) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList blockList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BLOCK_LIST);
                if (blockList != null && !blockList.isEmpty()) {
                    if (skip > blockList.size()) {
                        return result;
                    }
                    long startIndex = skip;
                    long endIndex = startIndex + take;
                    if (endIndex > blockList.size()) {
                        endIndex = blockList.size();
                    }
                    if (skip == 0 && take == 0) {
                        for (int i = 0; i < blockList.size(); i++) {
                            result.add(blockList.get(i).toString());
                        }
                    } else {
                        //get friend Id
                        for (long i = startIndex; i < endIndex; i++) {
                            result.add(blockList.get((int) i).toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<String> listBlockUsers(String userId) throws EazyException{
        List<String> result = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList blockList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BLOCK_LIST);
                if (blockList!=null)
                    for (int i = 0; i<blockList.size(); i++){
                        result.add(blockList.get(i).toString());
                    }
            }
        }
        catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getBlackList(String userId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList blockList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BLOCK_LIST);
                if (blockList != null && !blockList.isEmpty()) {
                    for (Object blockList1 : blockList) {
                        result.add(blockList1.toString());
                    }
                }
                BasicDBList blockedList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BE_BLOCKED_LIST);
                if (blockedList != null && !blockedList.isEmpty()) {
                    for (Object blockedList1 : blockedList) {
                        result.add(blockedList1.toString());
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static HashMap<String, String> getBlockList(String userId) throws EazyException {
        HashMap<String, String> result = new HashMap<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList blockList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BLOCK_LIST);
                if (blockList != null && !blockList.isEmpty()) {
                    for (Object blockList1 : blockList) {
                        result.put(blockList1.toString(), blockList1.toString());
                    }
                }
                BasicDBList blockedList = (BasicDBList) respondObj.get(UserdbKey.BLOCK.BE_BLOCKED_LIST);
                if (blockedList != null && !blockedList.isEmpty()) {
                    for (Object blockedList1 : blockedList) {
                        result.put(blockedList1.toString(), blockedList1.toString());
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static HashMap<String, ConcurrentLinkedQueue<String>> getAll() throws EazyException {
        HashMap<String, ConcurrentLinkedQueue<String>> result = new HashMap<>();
        try {
            DBCursor cursor = coll.find();
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                String userId = obj.get(UserdbKey.BLOCK.ID).toString();
                ConcurrentLinkedQueue<String> blackList = new ConcurrentLinkedQueue<>();
                BasicDBList blockList = (BasicDBList) obj.get(UserdbKey.BLOCK.BLOCK_LIST);
                if (blockList != null && !blockList.isEmpty()) {
                    for (Object blockList1 : blockList) {
                        blackList.add(blockList1.toString());
                    }
                }
                BasicDBList blockedList = (BasicDBList) obj.get(UserdbKey.BLOCK.BE_BLOCKED_LIST);
                if (blockedList != null && !blockedList.isEmpty()) {
                    for (Object blockedList1 : blockedList) {
                        blackList.add(blockedList1.toString());
                    }
                }
                result.put(userId, blackList);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isBlock(String userId, String friendId) {
        boolean result = false;
        ObjectId id = new ObjectId(userId);
        BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BLOCK.ID, id);
        DBObject obj = coll.findOne(updateQuery);
        if (obj != null) {
            BasicDBList blockList = (BasicDBList) obj.get(UserdbKey.BLOCK.BLOCK_LIST);
            if (blockList != null && !blockList.isEmpty()) {
                for (Object blockList1 : blockList) {
                    String bId = blockList1.toString();
                    if (bId.equals(friendId)) {
                        return true;
                    }
                }
            }
            BasicDBList blockedList = (BasicDBList) obj.get(UserdbKey.BLOCK.BE_BLOCKED_LIST);
            if (blockedList != null && !blockedList.isEmpty()) {
                for (Object blockedList1 : blockedList) {
                    String bId = blockedList1.toString();
                    if (bId.equals(friendId)) {
                        return true;
                    }
                }
            }
        }
        return result;
    }
}
