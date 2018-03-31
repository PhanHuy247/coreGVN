/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.dbentity.ImageDB;
import com.vn.ntsc.usermanagementserver.dbentity.VideoDB;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;

/**
 *
 * @author Phan Huy
 */
public class VideoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USER_VIDEO_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }
    
    public static boolean insertVideo(Video video) throws EazyException {
        boolean result = false;
        try {
            VideoDB videoDB = VideoDB.fromVideoEntity(video);
            DBObject obj = videoDB.toDBObject();
            coll.insert(obj, new WriteConcern(true));
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String videoId, int OFF) throws EazyException{
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_VIDEO.VIDEO_ID, videoId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_VIDEO.FLAG, OFF);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
        
    }
    
    public static Video getVideoInfo(String videoId){
        Video result = new Video();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.USER_VIDEO.VIDEO_ID, videoId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String userId = (String) obj.get(UserdbKey.USER_VIDEO.USER_ID);
                Integer flag = (Integer) obj.get(UserdbKey.USER_VIDEO.FLAG);
                Integer reportFlag = 0;
                if(obj.get(UserdbKey.USER_VIDEO.REPORT_FLAG) != null){
                    reportFlag = (Integer) obj.get(UserdbKey.USER_VIDEO.REPORT_FLAG);
                }else{
                    reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
                }
                result = new Video(userId, videoId, flag, reportFlag);
                result.videoType = (Integer)obj.get(UserdbKey.USER_VIDEO.VIDEO_TYPE);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static boolean addReport(String videoId, Date time, boolean isIncreaseReportNumber, Integer reportStatus) throws EazyException {
        boolean result = false;
        try {
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER_VIDEO.VIDEO_ID, videoId);
            //update command
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER_VIDEO.REPORT_NUMBER, 1);
            BasicDBObject updateCommand = new BasicDBObject();
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(UserdbKey.USER_VIDEO.REPORT_TIME, time.getTime());
            updateObj.append(UserdbKey.USER_VIDEO.REPORT_FLAG, reportStatus);
            updateCommand.append("$set", updateObj);
            if (isIncreaseReportNumber) {
                updateCommand.append("$inc", obj);
            }
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
