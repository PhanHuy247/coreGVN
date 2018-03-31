/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.*;
import com.mongodb.DBCollection;
import com.vn.ntsc.buzzserver.Setting;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import eazycommon.constant.mongokey.BuzzdbKey.BUZZ_DETAIL;
import org.json.simple.JSONArray;

/**
 *
 * @author DuongLTD
 */
public class BuzzDetailDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static Map<String, Buzz> getListBuzz(List<Buzz> lBuzz, String currentUserId) throws EazyException {
        Map<String, Buzz> result = new TreeMap<>();
        Map<String, Long> mBuzz = new TreeMap<>();
        try {
            List<ObjectId> lBuzzId = new ArrayList<>();
            for (Buzz lBuzz1 : lBuzz) {
                String id = lBuzz1.buzzId;
                Long lastAct = lBuzz1.lastAct;
                lBuzzId.add(new ObjectId(id));
                mBuzz.put(id, lastAct);
            }
            for (ObjectId id : lBuzzId) {
                DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                BasicDBObject data = new BasicDBObject();
                BasicDBList buzzTypeValue = new BasicDBList();
                buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ);
                buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ);
                buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ);
                buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.VIDEO_BUZZ);
                buzzTypeValue.add(Constant.BUZZ_TYPE_VALUE.MULTI_BUZZ);
                data.append("$nin", buzzTypeValue);
                query.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, data);
                query.put(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS, new BasicDBObject("$ne", "pending"));
//                Util.addDebugLog("query=================="+query);
                DBObject obj = coll.findOne(query);
                if (obj != null) {
                    Buzz buzz = new Buzz();
                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    buzz.userId = userId;

                    buzz.isLike = Constant.FLAG.OFF;

//                    Integer seenNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER);
//                    if(seenNum != null)
//                        buzz.seenNum = new Long(seenNum);
//                    else
//                        buzz.seenNum = new Long(0);
                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = new Long(likeNum);
                    } else {
                        buzz.likeNum = new Long(0);
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = new Long(cmtNum);
                    } else {
                        buzz.cmtNum = new Long(0);
                    }

                    ObjectId buzzId = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                    buzz.buzzId = buzzId.toString();
                    Long lastAct = mBuzz.get(buzzId.toString());
                    buzz.lastAct = lastAct;

                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = new Long(buzzType);

                    Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                    Date d = new Date(buzzTime);
                    buzz.buzzTime = DateFormat.format(d);
                    Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    buzz.isApp = isApp;
                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    if (buzzType == 1) {
                        buzz.buzzVal = buzzVal;
                    } else {
                        if (isApp == 1) {
                            buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                        } else {
                            buzz.buzzVal = buzzVal;
                        }
                    }
                    String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                    buzz.fileId = fileId;
                    String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                    buzz.coverId = coverId;
                    Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    if(privacy != null)
                        buzz.privacy = privacy.intValue();
                    String streamId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
                    if (streamId != null)
                        buzz.streamId = streamId;
                    Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                    if (viewNumber != null){
                        buzz.viewNumber = viewNumber;
                    }
                    Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                    if (curentView != null){
                        buzz.curentView = curentView;
                    }
                    Integer duration = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.DURATION);
                    if (duration != null){
                        buzz.duration = duration;
                    }
                    Long streamEnd = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME);
                    if (streamEnd != null)
                        buzz.streamEndTime = streamEnd;
                    Long streamStart = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME);
                    if (streamStart != null)
                        buzz.streamStartTime = streamStart;
                    String streamStatus = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                    if(streamStatus != null)
                        buzz.streamStatus = streamStatus;
                    Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                    if(shareNumber != null){
                        buzz.shareNumber = shareNumber;
                    }else{
                        buzz.shareNumber = 0;
                    }
                    
                    Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                    if(buzzRegion != null){
                        buzz.buzzRegion = buzzRegion.intValue();
                    }
                    
                    String shareId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_ID);
                    if(shareId != null && !shareId.equals("") && ObjectId.isValid(shareId)){
                        buzz.shareId = shareId;
                        Buzz detail = getShareDetail(shareId);
                        if(detail.buzzId != null){
                            if(detail.isApp == Constant.FLAG.ON && detail.flag == Constant.FLAG.ON){
                                buzz.shareDetail = detail;
                                buzz.shareDetail.listChild = BuzzDetailDAO.getChildBuzz(shareId, null);
                                buzz.shareDetail.childNum = buzz.shareDetail.listChild.size();
                            }else{
                                detail.fileId = Setting.share_has_deleted_img;
                                detail.buzzType = Long.valueOf(Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS);
                                
                                buzz.shareDetail = detail;
                                buzz.shareDetail.listChild = new ArrayList<>();
                                buzz.shareDetail.childNum = 0;
                            }
                        }
                    }
                        
                    result.put(buzzId.toString(), buzz);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, List<Buzz>> getListChildBuzz(List<Buzz> lBuzz, String currentUser) throws EazyException{
        Map<String, List<Buzz>> resutl = new TreeMap<>();
        try{
            List<String> listBuzzId = new ArrayList<>();
            for (Buzz lBuzz1 : lBuzz) {
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, lBuzz1.buzzId);
//                findObj.append(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, Constant.FLAG.ON);
//                DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, lBuzz1.buzzId);
                DBCursor cursor = coll.find(findObj);
                List<Buzz> listBuzz = new ArrayList<>();
                while(cursor.hasNext()){
                    DBObject obj = cursor.next();
                    
                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    Integer flag = (Integer) obj.get(BuzzdbKey.USER_BUZZ.FLAG);
                    Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    
                    if((flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG) && (isApp == Constant.FLAG.ON || userId.equals(currentUser)) ){
                        Buzz buzz = new Buzz();
                    
                        buzz.userId = userId;

                        ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                        buzz.buzzId = id.toString();

                        buzz.isLike = Constant.FLAG.OFF;

                        Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                        if (likeNum != null) {
                            buzz.likeNum = new Long(likeNum);
                        } else {
                            buzz.likeNum = new Long(0);
                        }

                        Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                        if (cmtNum != null) {
                            buzz.cmtNum = new Long(cmtNum);
                        } else {
                            buzz.cmtNum = new Long(0);
                        }

    //                    Long lastAct = mBuzz.get(buzzId.toString());
    //                    buzz.lastAct = lastAct;

                        Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                        buzz.buzzType = new Long(buzzType);

                        Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                        Date d = new Date(buzzTime);
                        buzz.buzzTime = DateFormat.format(d);
                        
                        buzz.isApp = isApp;
                        String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                        if (buzzType == 1) {
                            buzz.buzzVal = buzzVal;
                        } else {
                            if (isApp == 1) {
                                buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                            } else {
                                buzz.buzzVal = buzzVal;
                            }
                        }
                        String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                        buzz.fileId = fileId;
                        String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                        buzz.coverId = coverId;

                        Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                        if(privacy != null)
                            buzz.privacy = privacy.intValue();
                        
                        Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                        if(buzzRegion != null){
                            buzz.buzzRegion = buzzRegion.intValue();
                        }
                        
                        Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                        if(shareNumber != null){
                            buzz.shareNumber = shareNumber;
                        }else{
                            buzz.shareNumber = 0;
                        }
                        
                        Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                        if (viewNumber != null){
                            buzz.viewNumber = viewNumber;
                        }
                        Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                        if (curentView != null){
                            buzz.curentView = curentView;
                        }
                        listBuzz.add(buzz);
                    }
                    
                    
                }
                resutl.put(lBuzz1.buzzId, listBuzz);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }
    
    public static List<Buzz> getChildBuzz(String buzzId, String currentUser) throws EazyException{
        List<Buzz> listBuzz = new ArrayList<>();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, buzzId);
//            findObj.append(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, Constant.FLAG.ON);
//            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, buzzId);
            DBCursor cursor = coll.find(findObj);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                Integer flag = (Integer) obj.get(BuzzdbKey.USER_BUZZ.FLAG);
                Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                
                if((flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG) && (isApp == Constant.FLAG.ON || userId.equals(currentUser))){
                    Buzz buzz = new Buzz();

                    
                    Util.addDebugLog("===========userId========"+userId);
                    buzz.userId = userId;

                    ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                    buzz.buzzId = id.toString();

                    buzz.isLike = Constant.FLAG.OFF;

                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = new Long(likeNum);
                    } else {
                        buzz.likeNum = new Long(0);
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = new Long(cmtNum);
                    } else {
                        buzz.cmtNum = new Long(0);
                    }

    //                    Long lastAct = mBuzz.get(buzzId.toString());
    //                    buzz.lastAct = lastAct;

                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = new Long(buzzType);

                    Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                    Date d = new Date(buzzTime);
                    buzz.buzzTime = DateFormat.format(d);
                    
                    buzz.isApp = isApp;
                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    if (buzzType == 1) {
                        buzz.buzzVal = buzzVal;
                    } else {
                        if (isApp == 1) {
                            buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                        } else {
                            buzz.buzzVal = buzzVal;
                        }
                    }
                    String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                    buzz.fileId = fileId;
                    String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                    buzz.coverId = coverId;
                    
                    Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                    if(buzzRegion != null){
                        buzz.buzzRegion = buzzRegion.intValue();
                    }
                        
                    Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    if(privacy != null)
                        buzz.privacy = privacy.intValue();
                    Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                    if(shareNumber != null){
                        buzz.shareNumber = shareNumber;
                    }else{
                        buzz.shareNumber = 0;
                    }
                    listBuzz.add(buzz);
                }
                
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return listBuzz;
    }
    
    public static Map<String, Buzz> getListStreamBuzz(List<Buzz> lBuzz, String currentUserId) throws EazyException {
        Map<String, Buzz> result = new TreeMap<>();
        Map<String, Long> mBuzz = new TreeMap<>();
        try {
            List<ObjectId> lBuzzId = new ArrayList<>();
            for (Buzz lBuzz1 : lBuzz) {
                String id = lBuzz1.buzzId;
                Long lastAct = lBuzz1.lastAct;
                lBuzzId.add(new ObjectId(id));
                mBuzz.put(id, lastAct);
            }
            for (ObjectId id : lBuzzId) {
                DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                query.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, Constant.BUZZ_TYPE_VALUE.STREAM_STATUS);
                query.put(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS, "on");
//                Util.addDebugLog("query=================="+query);
                DBObject obj = coll.findOne(query);
                if (obj != null) {
                    Buzz buzz = new Buzz();
                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    buzz.userId = userId;

                    buzz.isLike = Constant.FLAG.OFF;

//                    Integer seenNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER);
//                    if(seenNum != null)
//                        buzz.seenNum = new Long(seenNum);
//                    else
//                        buzz.seenNum = new Long(0);
                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = new Long(likeNum);
                    } else {
                        buzz.likeNum = new Long(0);
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = new Long(cmtNum);
                    } else {
                        buzz.cmtNum = new Long(0);
                    }

                    ObjectId buzzId = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                    buzz.buzzId = buzzId.toString();
                    Long lastAct = mBuzz.get(buzzId.toString());
                    buzz.lastAct = lastAct;

                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = new Long(buzzType);

                    Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                    Date d = new Date(buzzTime);
                    buzz.buzzTime = DateFormat.format(d);
                    Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    buzz.isApp = isApp;
                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    if (buzzType == 1) {
                        buzz.buzzVal = buzzVal;
                    } else {
                        if (isApp == 1) {
                            buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                        } else {
                            buzz.buzzVal = buzzVal;
                        }
                    }
                    String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                    buzz.fileId = fileId;
                    String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                    buzz.coverId = coverId;
                    Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    if(privacy != null)
                        buzz.privacy = privacy.intValue();
                    
                    Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                    if(buzzRegion != null){
                        buzz.buzzRegion = buzzRegion.intValue();
                    }
                    
                    String streamId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
                    if (streamId != null)
                        buzz.streamId = streamId;
                    Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                    if (viewNumber != null){
                        buzz.viewNumber = viewNumber;
                    }
                    Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                    if (curentView != null){
                        buzz.curentView = curentView;
                    }
                    Integer duration = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.DURATION);
                    if (duration != null){
                        buzz.duration = duration;
                    }
                    Long streamEnd = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME);
                    if (streamEnd != null)
                        buzz.streamEndTime = streamEnd;
                    Long streamStart = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME);
                    if (streamStart != null)
                        buzz.streamStartTime = streamStart;
                    String streamStatus = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                    if(streamStatus != null)
                        buzz.streamStatus = streamStatus;
                    Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                    if(shareNumber != null){
                        buzz.shareNumber = shareNumber;
                    }else{
                        buzz.shareNumber = 0;
                    }
                    result.put(buzzId.toString(), buzz);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getChildBuzzWithImageId(String buzzId,String imagId) throws EazyException{
        List<Buzz> listBuzz = new ArrayList<>();
        try{
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, buzzId);
            query.put(BuzzdbKey.BUZZ_DETAIL.FILE_ID, imagId);
            DBCursor cursor = coll.find(query);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                
                Integer flag = (Integer) obj.get(BuzzdbKey.USER_BUZZ.FLAG);
                if(flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG){
                    Buzz buzz = new Buzz();

                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    Util.addDebugLog("===========userId========"+userId);
                    buzz.userId = userId;

                    ObjectId id = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                    buzz.buzzId = id.toString();

                    buzz.isLike = Constant.FLAG.OFF;

                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = new Long(likeNum);
                    } else {
                        buzz.likeNum = new Long(0);
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = new Long(cmtNum);
                    } else {
                        buzz.cmtNum = new Long(0);
                    }

    //                    Long lastAct = mBuzz.get(buzzId.toString());
    //                    buzz.lastAct = lastAct;

                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = new Long(buzzType);

                    Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                    Date d = new Date(buzzTime);
                    buzz.buzzTime = DateFormat.format(d);
                    Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    buzz.isApp = isApp;
                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    if (buzzType == 1) {
                        buzz.buzzVal = buzzVal;
                    } else {
                        if (isApp == 1) {
                            buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                        } else {
                            buzz.buzzVal = buzzVal;
                        }
                    }
                    String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                    buzz.fileId = fileId;
                    String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                    buzz.coverId = coverId;
                    
                    Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                    if(buzzRegion != null){
                        buzz.buzzRegion = buzzRegion.intValue();
                    }
                        
                    Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    if(privacy != null)
                        buzz.privacy = privacy.intValue();
                    listBuzz.add(buzz);
                }
                
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return listBuzz;
    }
    
    public static String addChildBuzz(String userId, String buzzVal, int buzzType, long time, int isApp, String ip, String parentId, String fileId, String cover, long region) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE, buzzVal);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, buzzType);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME, time);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.POST_TIME, time);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, isApp);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.IP, ip);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.PARENT_ID, parentId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.COVER_ID, cover);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION, region);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }



    public static String addBuzz(String userId, String buzzVal, int buzzType, long time, int isApp, String ip, String fileId, long privacy, String cover, String streamId, String shareId, long region) throws EazyException {
        String result = null;
        try {
            Util.addDebugLog("===========addBuzz========");
            DBObject insertObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE, buzzVal);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE, buzzType);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME, time);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.POST_TIME, time);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, isApp);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.IP, ip);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.PRIVACY, privacy);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION, region);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.COVER_ID, cover);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.STREAM_ID, streamId);
            insertObj.put(BuzzdbKey.BUZZ_DETAIL.SHARE_ID, shareId);
            if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                insertObj.put(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS, "pending");
            }
            Util.addDebugLog("===========insertObj========"+insertObj);
            coll.insert(insertObj);
            ObjectId id = (ObjectId) insertObj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Boolean updateBuzz(String userId, String buzzId, long time, Integer affFlag, String fileId, Integer viewNumber, Integer currentView, String streamStatus) throws EazyException{
        Boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                updateQuery.append(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
                
                BasicDBObject obj = new BasicDBObject();
                if(fileId != null)
                    obj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID ,fileId);
                if(viewNumber != null)
                    obj.append(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER, viewNumber);
                if(currentView != null)
                    obj.append(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW, currentView);
                
                obj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS, streamStatus);
                if(streamStatus.toLowerCase().trim().equals("off")){
                    obj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_ID, "");
                    obj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME, time);
                }
                if(streamStatus.toLowerCase().trim().equals("on")){
                    obj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME, time);
                }

                BasicDBObject updateCommand = new BasicDBObject("$set", obj );
                coll.update(updateQuery, updateCommand, true, false);
                result = true;
            }else{
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Boolean updateView(String userId, String buzzId, Integer viewNumber, Integer currentView, Integer duration) throws EazyException{
        Boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                updateQuery.append(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
                
                BasicDBObject obj = new BasicDBObject();
                obj.append(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER, viewNumber);
                obj.append(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW, currentView);
                obj.append(BuzzdbKey.BUZZ_DETAIL.DURATION, duration);

                BasicDBObject updateCommand = new BasicDBObject("$set", obj );
                coll.update(updateQuery, updateCommand, true, false);
                result = true;
            }else{
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Boolean updateBuzzTag(String userId, String buzzId, JSONArray tagList) throws EazyException{
        Boolean result = false;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                updateQuery.append(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
                
                BasicDBObject obj = new BasicDBObject();
//                obj.append(BuzzdbKey.BUZZ_DETAIL. ,tagList);

                BasicDBObject updateCommand = new BasicDBObject("$set", obj );
                coll.update(updateQuery, updateCommand, true, false);
                result = true;
            }else{
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getBuzzId(String fileId) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String buzzId = obj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
            result = buzzId;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getImageId(String buzzId) throws EazyException {
        String result = null;
        try {
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, buzzId);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            Integer type = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            if (type == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ) {
                String imageId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                result = imageId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getUserId(String buzzId) throws EazyException {
        String result = null;
        try {
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                DBObject obj = coll.findOne(findObj);
                if (obj == null) {
                    throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
                }
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                result = userId;
            }else{
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkBuzzExist(String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer buzzFlag = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.FLAG);
                if (buzzFlag == Constant.FLAG.ON) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getBuzzType(String buzzId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            result = (int) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getBuzzStreamStatus(String buzzId) throws EazyException {
        String result = Constant.STREAM_STATUS.OFF;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            result = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApprovedFlag(String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Buzz getBuzzAndAddNumberOfShare(String buzzId) throws EazyException{
        Buzz buzz = null;
        try{
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(BuzzdbKey.BUZZ_DETAIL.ID, id);
                
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER, 1);

                BasicDBObject incObj = new BasicDBObject("$inc", updateObj);
                DBObject obj = coll.findAndModify(findObj, incObj);
                if (obj != null) {
                    buzz = new Buzz();
                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    buzz.userId = userId;

                    buzz.isLike = Constant.FLAG.OFF;
                    
                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = likeNum.longValue();
                    } else {
                        buzz.likeNum = new Long(0);
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = cmtNum.longValue();
                    } else {
                        buzz.cmtNum = new Long(0);
                    }

                    buzz.buzzId = buzzId;

                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = new Long(buzzType);

                    Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                    Date d = new Date(buzzTime);
                    buzz.buzzTime = DateFormat.format(d);
                    Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    buzz.isApp = isApp;
                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    if (buzzType == 1) {
                        buzz.buzzVal = buzzVal;
                    } else {
                        if (isApp == 1) {
                            buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                        } else {
                            buzz.buzzVal = buzzVal;
                        }
                    }
                    String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                    buzz.fileId = fileId;

                    String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
                    buzz.parentId = parentId;

                    String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                    buzz.coverId = coverId;

                    Long privacy = new Long("0");
                    if(obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                        privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    }
                    buzz.privacy = privacy.intValue();

                    String streamId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
                    if(streamId != null)
                        buzz.streamId = streamId;

                    Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                    if(viewNumber != null)
                        buzz.viewNumber = viewNumber;
                    Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                    if (curentView != null){
                        buzz.curentView = curentView;
                    }
                    Integer duration = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.DURATION);
                    if (duration != null){
                        buzz.duration = duration;
                    }
                    Long streamEnd = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME);
                    if(streamEnd != null)
                        buzz.streamEndTime = streamEnd;
                    Long streamStart = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME);
                    if (streamStart != null)
                        buzz.streamStartTime = streamStart;
                    String streamStatus = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                    if(streamStatus != null)
                        buzz.streamStatus = streamStatus;

                    Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                    if(shareNumber != null){
                        buzz.shareNumber = shareNumber;
                    }else{
                        buzz.shareNumber = 0;
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return buzz;
    }

    public static boolean updateFlag(String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean increaseFieldList(String buzzId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(fieldname, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean decreaseFieldList(String buzzId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            //update command
            DBObject obD = coll.findOne(updateQuery);
            Integer point = 0;
            if (obD != null) {
                point = (Integer) obD.get(fieldname);
                if (point == null) {
                    point = 0;
                }
            }
            if (num > point) {
                num = point;
            }
            BasicDBObject obj = new BasicDBObject(fieldname, (0 - num));
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addReport(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.REPORT_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean addLike(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removeLike(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean addSeen(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removeSeen(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean addComment(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removeComment(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static Buzz getBuzzDetail(String buzzId) throws EazyException {
        Buzz result = new Buzz();
        try {
            Util.addDebugLog("buzID======================================"+buzzId);
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            query.put(BuzzdbKey.BUZZ_DETAIL.FLAG, 1);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                Buzz buzz = new Buzz();
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.userId = userId;

                buzz.isLike = Constant.FLAG.OFF;

//                Integer seenNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER);
//                if(seenNum != null)
//                    buzz.seenNum = seenNum.longValue() + 1;
//                else
//                    buzz.seenNum = new Long(1);
                Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                if (likeNum != null) {
                    buzz.likeNum = likeNum.longValue();
                } else {
                    buzz.likeNum = new Long(0);
                }

                Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                if (cmtNum != null) {
                    buzz.cmtNum = cmtNum.longValue();
                } else {
                    buzz.cmtNum = new Long(0);
                }

                buzz.buzzId = buzzId;

                Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                buzz.buzzType = new Long(buzzType);

                Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                Date d = new Date(buzzTime);
                buzz.buzzTime = DateFormat.format(d);
                Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                buzz.isApp = isApp;
                String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                if (buzzType == 1) {
                    buzz.buzzVal = buzzVal;
                } else {
                    if (isApp == 1) {
                        buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                    } else {
                        buzz.buzzVal = buzzVal;
                    }
                }
                String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                buzz.fileId = fileId;
                
//                String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
//                buzz.parentId = parentId;
                
                String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                buzz.coverId = coverId;
                
                String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
                buzz.parentId = parentId;
                if(parentId == null){
                    Long privacy = new Long("0");
                    if(obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                        privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    }
                    buzz.privacy = privacy.intValue();
                }else{
                    ObjectId pId = new ObjectId(parentId);
                    DBObject pQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, pId);
                    DBObject parentObj = coll.findOne(pQuery);
                    if (parentObj != null) {
                        Long parentPrivacy = new Long("0");
                        if(parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                            parentPrivacy = (Long) parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                        }
                        buzz.privacy = parentPrivacy.intValue();
                    }
                }
                
                Long buzzRegion = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_REGION);
                if(buzzRegion != null){
                    buzz.buzzRegion = buzzRegion.intValue();
                }
                
                String streamId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
                if(streamId != null)
                    buzz.streamId = streamId;
                
                Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                if(viewNumber != null)
                    buzz.viewNumber = viewNumber;
                Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                if (curentView != null){
                    buzz.curentView = curentView;
                }
                Integer duration = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.DURATION);
                if (duration != null){
                    buzz.duration = duration;
                }
                Long streamEnd = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME);
                if(streamEnd != null)
                    buzz.streamEndTime = streamEnd;
                Long streamStart = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME);
                if (streamStart != null)
                    buzz.streamStartTime = streamStart;
                String streamStatus = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                if(streamStatus != null)
                    buzz.streamStatus = streamStatus;
                
                Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                if(shareNumber != null){
                    buzz.shareNumber = shareNumber;
                }else{
                    buzz.shareNumber = 0;
                }
                
                String shareId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_ID);
                if(shareId != null && !shareId.equals("") && ObjectId.isValid(shareId)){
                    buzz.shareId = shareId;
                    Buzz detail = getShareDetail(shareId);
                    if(detail.buzzId != null){
                        if(detail.isApp == Constant.FLAG.ON && detail.flag == Constant.FLAG.ON){
                            buzz.shareDetail = detail;
                            buzz.shareDetail.listChild = BuzzDetailDAO.getChildBuzz(shareId, null);
                            buzz.shareDetail.childNum = buzz.shareDetail.listChild.size();
                        }else{
                            detail.fileId = Setting.share_has_deleted_img;
                            detail.buzzType = Long.valueOf(Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS);

                            buzz.shareDetail = detail;
                            buzz.shareDetail.listChild = new ArrayList<>();
                            buzz.shareDetail.childNum = 0;
                        }
                    }
                }
                result = buzz;
            } else {
                Util.addDebugLog("errornotfound======================================"+buzzId);
//                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Buzz getShareDetail(String buzzId) throws EazyException {
        Buzz result = new Buzz();
        try {
            Util.addDebugLog("getShareDetail======================================"+buzzId);
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
//            query.put(BuzzdbKey.BUZZ_DETAIL.FLAG, 1);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                Buzz buzz = new Buzz();
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.userId = userId;

                buzz.isLike = Constant.FLAG.OFF;

                Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                if (likeNum != null) {
                    buzz.likeNum = likeNum.longValue();
                } else {
                    buzz.likeNum = new Long(0);
                }

                Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                if (cmtNum != null) {
                    buzz.cmtNum = cmtNum.longValue();
                } else {
                    buzz.cmtNum = new Long(0);
                }

                buzz.buzzId = buzzId;

                Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                buzz.buzzType = new Long(buzzType);

                Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                Date d = new Date(buzzTime);
                buzz.buzzTime = DateFormat.format(d);
                
                Integer flag = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.FLAG);
                buzz.flag = flag;
                
                Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                buzz.isApp = isApp;
                String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                if (buzzType == 1) {
                    buzz.buzzVal = buzzVal;
                } else {
                    if (isApp == 1) {
                        buzz.buzzVal = Util.replaceBannedWord(buzzVal);
                    } else {
                        buzz.buzzVal = buzzVal;
                    }
                }
                String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                buzz.fileId = fileId;
                
//                String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
//                buzz.parentId = parentId;
                
                String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                buzz.coverId = coverId;
                
                String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
                buzz.parentId = parentId;
                if(parentId == null){
                    Long privacy = new Long("0");
                    if(obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                        privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    }
                    buzz.privacy = privacy.intValue();
                }else{
                    ObjectId pId = new ObjectId(parentId);
                    DBObject pQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, pId);
                    DBObject parentObj = coll.findOne(pQuery);
                    if (parentObj != null) {
                        Long parentPrivacy = new Long("0");
                        if(parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                            parentPrivacy = (Long) parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                        }
                        buzz.privacy = parentPrivacy.intValue();
                    }
                }
                
                String streamId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_ID);
                if(streamId != null)
                    buzz.streamId = streamId;
                
                Integer viewNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.VIEW_NUMBER);
                if(viewNumber != null)
                    buzz.viewNumber = viewNumber;
                Integer curentView = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.CURRENT_VIEW);
                if (curentView != null){
                    buzz.curentView = curentView;
                }
                Integer duration = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.DURATION);
                if (duration != null){
                    buzz.duration = duration;
                }
                Long streamEnd = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_END_TIME);
                if(streamEnd != null)
                    buzz.streamEndTime = streamEnd;
                Long streamStart = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_START_TIME);
                if (streamStart != null)
                    buzz.streamStartTime = streamStart;
                String streamStatus = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                if(streamStatus != null)
                    buzz.streamStatus = streamStatus;
                
                Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                if(shareNumber != null){
                    buzz.shareNumber = shareNumber;
                }else{
                    buzz.shareNumber = 0;
                }
                
                result = buzz;
            } else {
                Util.addDebugLog("errornotfound======================================"+buzzId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, Buzz> getMediaBuzzDetail(List<String> fileList){
        Map<String, Buzz> result = new TreeMap<>();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, new BasicDBObject("$in", fileList));
            DBCursor cursor = coll.find(findObj);
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                
                Buzz buzz = new Buzz();
                ObjectId buzzId = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                buzz.buzzId = buzzId.toString();
                
                String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                buzz.isLike = Constant.FLAG.OFF;
                
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.userId = userId;
                
                buzz.fileId = fileId;
                
                Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                if (likeNum != null) {
                    buzz.likeNum = new Long(likeNum);
                } else {
                    buzz.likeNum = new Long(0);
                }

                Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                if (cmtNum != null) {
                    buzz.cmtNum = new Long(cmtNum);
                } else {
                    buzz.cmtNum = new Long(0);
                }
                
                Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                buzz.buzzType = new Long(buzzType);
                
                Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                Date d = new Date(buzzTime);
                buzz.buzzTime = DateFormat.format(d);
                
                Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                buzz.isApp = isApp;
                
                String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
                buzz.coverId = coverId;
                
                String parentId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.PARENT_ID);
                if(parentId == null){
                    Long privacy = new Long("0");
                    if(obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                        privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                    }
                    buzz.privacy = privacy.intValue();
                }else{
                    ObjectId id = new ObjectId(parentId);
                    DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                    DBObject parentObj = coll.findOne(query);
                    if (parentObj != null) {
                        Long parentPrivacy = new Long("0");
                        if(parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
                            parentPrivacy = (Long) parentObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                        }
                        buzz.privacy = parentPrivacy.intValue();
                    }
                }
                
                
//                Long privacy = new Long("0");
//                if(obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null){
//                    privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
//                }
//                buzz.privacy = privacy.intValue();
                
                Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
                if(shareNumber != null){
                    buzz.shareNumber = shareNumber;
                }else{
                    buzz.shareNumber = 0;
                }

                result.put(fileId, buzz);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static boolean isExists(String buzzId) throws EazyException {
        boolean result = false;
        try {
            //
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);

            if (obj != null) { // found email
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<Buzz> getListBuzzMulti(String parentId){
        List<Buzz> listBuzz = new ArrayList<>();
        BasicDBObject dbObject = new BasicDBObject(BUZZ_DETAIL.PARENT_ID, parentId);
        DBCursor curs = coll.find(dbObject);
        while(curs.hasNext()){
            DBObject obj = curs.next();
             ObjectId buzzId = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
            Buzz buzz = new Buzz(); 
            buzz.buzzId = buzzId.toString();

            String fileId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
            buzz.isLike = Constant.FLAG.OFF;

            String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
            buzz.userId = userId;

            buzz.fileId = fileId;

            Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
            if (likeNum != null) {
                buzz.likeNum = new Long(likeNum);
            } else {
                buzz.likeNum = new Long(0);
            }

            Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
            if (cmtNum != null) {
                buzz.cmtNum = new Long(cmtNum);
            } else {
                buzz.cmtNum = new Long(0);
            }

            Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            buzz.buzzType = new Long(buzzType);

            Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
            buzz.isApp = isApp;

            String coverId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.COVER_ID);
            buzz.coverId = coverId;

            Long privacy = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
            if (privacy != null) {
                buzz.privacy = privacy.intValue();
            }

            Integer shareNumber = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SHARE_NUMBER);
            if (shareNumber != null) {
                buzz.shareNumber = shareNumber;
            } else {
                buzz.shareNumber = 0;
            }
            listBuzz.add(buzz);
        }
        return listBuzz;
    }

    public static List<Buzz> getBuzzByListId(List<String> lTaggedBuzzId, String userId, List<String> friendList) {
        List<Buzz> lTaggedBuzz = new ArrayList<>();
        List<ObjectId> lstId = new ArrayList<>();
        for (String buzzId : lTaggedBuzzId) {
            lstId.add(new ObjectId(buzzId));
        }
        DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, new BasicDBObject("$in", lstId));
        DBCursor cursor = coll.find(findObj);
//        Util.addDebugLog("------------> cursor size: "+cursor.size());
        while (cursor.hasNext()) {
            DBObject buzzObj = cursor.next();
//            Util.addDebugLog("------------> buzzObj: "+buzzObj);
            Integer flag = (Integer) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.FLAG);
            Integer isApp = (Integer) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
            String ownerId = (String) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
            boolean ownedBuzz = false, isFriend = false;
            if (ownerId.endsWith(userId)){
                ownedBuzz = true;
            }
            if (friendList.contains(ownerId)){
                isFriend = true;
            }
//            Util.addDebugLog("-> isown = "+ ownedBuzz + " , isfriend = "+isFriend);
            if (isApp != null) {
                if (flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG) {
                    if ((ownedBuzz && isApp != Constant.REVIEW_STATUS_FLAG.DENIED) || isApp == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                        Long privacy = 0L;
                        if (buzzObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY) != null) {
                            privacy = (Long) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.PRIVACY);
                        }
                        Boolean isShow = false;
                        if (ownedBuzz) {
                            isShow = true;
                        } else if (privacy == Constant.POST_MODE.FRIEND && isFriend) {
                            isShow = true;
                        } else if (privacy == Constant.POST_MODE.EVERYONE) {
                            isShow = true;
                        }
//                        Util.addDebugLog("-> isShow = "+ isShow);
                        if (isShow) {
                            String buzzId = buzzObj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                            Long buzzTime = (Long) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
                            Buzz buzz = new Buzz();
                            buzz.buzzId = buzzId;
                            buzz.lastAct = buzzTime;
                            lTaggedBuzz.add(buzz);
                        }

                    }
                }
            }
        }
        return lTaggedBuzz;
    }
    
    public static List<Buzz> getStreamBuzz(String status){
        List<Buzz> result = new ArrayList<>();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS, status);
            DBCursor cursor = coll.find(findObj);
            while (cursor.hasNext()) {
                DBObject buzzObj = cursor.next();
                Buzz buzz = new Buzz();
                buzz.userId = (String) buzzObj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.buzzId = buzzObj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                result.add(buzz);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Boolean updateStreamBuzz(List<Buzz> listBuzz, String status){
        Boolean result = false;
        try{
            List<ObjectId> listId = new ArrayList<>();
            for(Buzz item: listBuzz){
                ObjectId id = new ObjectId(item.buzzId);
                listId.add(id);
            }
            
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, new BasicDBObject("$in", listId));
                
            BasicDBObject obj = new BasicDBObject();
            obj.append(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS ,status);

            BasicDBObject updateCommand = new BasicDBObject("$set", obj );
            coll.updateMulti(updateQuery, updateCommand);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Buzz getBuzzByFileId(String fileId) throws EazyException {
        Buzz result = new Buzz();
        try {
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                Buzz buzz = new Buzz();
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.userId = userId;
                
                String buzzId = obj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                buzz.buzzId = buzzId;
                
                Integer isApp = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                buzz.isApp = isApp;
                result = buzz;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
