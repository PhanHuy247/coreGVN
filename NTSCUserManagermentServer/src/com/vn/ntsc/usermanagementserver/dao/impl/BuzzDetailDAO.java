/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BuzzFileData;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class BuzzDetailDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getBuzzDB().getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static String getImageId(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String imageId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
            result = imageId;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getStreamId(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String shareId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
            result = shareId;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static Integer getType(String buzzId) throws EazyException {
        Integer result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            Integer type = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            result = type;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    public static String getText(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String type = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
            result = type;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    
    public static List<BuzzFileData> getChildBuzz(String buzzId) throws EazyException{
        List<BuzzFileData> result = new ArrayList<>();
        try{
            List<BuzzFileData> listChild = new ArrayList<>();
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, buzzId);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                String id = ((ObjectId) dbO.get(BuzzdbKey.BUZZ_DETAIL.ID)).toString();
                Integer buzzType = (Integer) dbO.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                String fileId = (String) dbO.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                
                BuzzFileData item = new BuzzFileData();
                item.buzzId = id;
                item.buzzType = buzzType;
                item.fileId = fileId;
                
                listChild.add(item);
            }
            result = listChild;
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<String> getListVideoBuzz(String fileId, Integer buzzType){
        List<String> result = new ArrayList<>();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            findObj.append(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, buzzType);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject dbO = cursor.next();
                String id = ((ObjectId) dbO.get(BuzzdbKey.BUZZ_DETAIL.ID)).toString();
                result.add(id);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static void updateFlagBuzz(String fileId, Integer buzzType, Integer flag){
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            findObj.append(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, buzzType);
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(BuzzdbKey.BUZZ_DETAIL.FLAG, flag);
            coll.update(findObj, new BasicDBObject("$set", updateObj));
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static String getReportedBuzz(String fileId){
        String result = "";
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String buzzId = obj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                result = buzzId;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
