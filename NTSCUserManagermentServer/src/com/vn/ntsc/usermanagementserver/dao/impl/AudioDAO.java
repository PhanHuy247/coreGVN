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
import com.vn.ntsc.usermanagementserver.dbentity.AudioDB;
import com.vn.ntsc.usermanagementserver.dbentity.VideoDB;
import com.vn.ntsc.usermanagementserver.entity.impl.audio.Audio;
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
public class AudioDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USER_AUDIO_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }
    
    public static boolean insertAudio(Audio audio) throws EazyException {
        boolean result = false;
        try {
            AudioDB audioDB = AudioDB.fromAudioEntity(audio);
            DBObject obj = audioDB.toDBObject();
            coll.insert(obj, new WriteConcern(true));
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static boolean updateFlag(String audioId, int OFF) throws EazyException{
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_AUDIO.AUDIO_ID, audioId);
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
    
    public static Audio getAudioInfo(String audioId){
        Audio result = new Audio();
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.USER_AUDIO.AUDIO_ID, audioId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String userId = (String) obj.get(UserdbKey.USER_AUDIO.USER_ID);
                Integer flag = (Integer) obj.get(UserdbKey.USER_AUDIO.FLAG);
                Integer reportFlag = 0;
                if(obj.get(UserdbKey.USER_AUDIO.REPORT_FLAG) != null){
                    reportFlag = (Integer) obj.get(UserdbKey.USER_AUDIO.REPORT_FLAG);
                }else{
                    reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
                }
                result = new Audio(userId, audioId, flag, reportFlag);
                result.audioType = (Integer)obj.get(UserdbKey.USER_AUDIO.AUDIO_TYPE);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static boolean addReport(String audioId, Date time, boolean isIncreaseReportNumber, Integer reportStatus) throws EazyException {
        boolean result = false;
        try {
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER_AUDIO.AUDIO_ID, audioId);
            //update command
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER_AUDIO.REPORT_NUMBER, 1);
            BasicDBObject updateCommand = new BasicDBObject();
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(UserdbKey.USER_AUDIO.REPORT_TIME, time.getTime());
            updateObj.append(UserdbKey.USER_AUDIO.REPORT_FLAG, reportStatus);
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
