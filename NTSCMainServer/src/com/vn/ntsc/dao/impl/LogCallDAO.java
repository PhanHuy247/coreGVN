/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class LogCallDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getLogDB().getCollection(LogdbKey.LOG_CALL_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean addLog(String call_sip, String requestId, String partnerId, Date startTime, Date endTime,  String ip, int callType, int duration, int partnerRespond) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_CALL.REQUEST_ID, requestId);
            addObj.append(LogdbKey.LOG_CALL.CALL_ID, call_sip);
            addObj.append(LogdbKey.LOG_CALL.PARTNER_ID, partnerId);
            addObj.append(LogdbKey.LOG_CALL.DURATION, duration);
            addObj.append(LogdbKey.LOG_CALL.CALL_TYPE, callType);
            addObj.append(LogdbKey.LOG_CALL.START_TIME, DateFormat.format(startTime));
            addObj.append(LogdbKey.LOG_CALL.END_TIME, DateFormat.format(endTime));
            addObj.append(LogdbKey.LOG_CALL.DURATION, duration);
            addObj.append(LogdbKey.LOG_CALL.IP, ip);
            addObj.append(LogdbKey.LOG_CALL.PARTNER_RESPOND, partnerRespond);
            addObj.append(LogdbKey.LOG_CALL.FINISH_FLAG, Constant.FLAG.OFF);
            coll.insert(addObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }   
    
    public static boolean updateStartCall (String call_id, int partnerRespond) throws EazyException{
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(LogdbKey.LOG_CALL.CALL_ID, call_id);
            DBObject updateObj = new BasicDBObject("$set", new BasicDBObject(LogdbKey.LOG_CALL.PARTNER_RESPOND, partnerRespond));
            coll.update(findObj, updateObj);
            result = true;
        } catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean addStartCallLog(String requestId, String partnerId, String callId, Date startTime, String ip, int callType) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject addObj = new BasicDBObject(LogdbKey.LOG_CALL.REQUEST_ID, requestId);
            addObj.append(LogdbKey.LOG_CALL.PARTNER_ID, partnerId);
            addObj.append(LogdbKey.LOG_CALL.CALL_ID, callId);
            addObj.append(LogdbKey.LOG_CALL.CALL_TYPE, callType);
            addObj.append(LogdbKey.LOG_CALL.START_TIME, DateFormat.format(startTime));
            addObj.append(LogdbKey.LOG_CALL.END_TIME, DateFormat.format(startTime));
            addObj.append(LogdbKey.LOG_CALL.DURATION, 0);
            addObj.append(LogdbKey.LOG_CALL.IP, ip);
            addObj.append(LogdbKey.LOG_CALL.PARTNER_RESPOND, PARTNER_BUSY);
            addObj.append(LogdbKey.LOG_CALL.FINISH_FLAG, Constant.FLAG.OFF);
            coll.insert(addObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateEndCall(String call_id, int partnerRespond, int duration, int finish_type ) throws EazyException{
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(LogdbKey.LOG_CALL.CALL_ID, call_id);
            DBObject setObj = new BasicDBObject(LogdbKey.LOG_CALL.PARTNER_RESPOND, partnerRespond)
                    .append(LogdbKey.LOG_CALL.DURATION, duration)
                    .append(LogdbKey.LOG_CALL.FINISH_FLAG, Constant.FLAG.ON)
                    .append(LogdbKey.LOG_CALL.FINISH_TYPE, finish_type);
            DBObject updateObj = new BasicDBObject("$set", setObj);
            coll.update(findObj, updateObj);
            result = true;
        } catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }     

    private static final int PARTNER_ANSWER = 0;
    private static final int PARTNER_BUSY = 1;
    private static final int PARTNER_REFUSE = 2;
    private static final int UNKNOW_RESPOND = 3;
    
    public static boolean addEndCallLog(String callId, Date endTime, int duration, String partner_respond) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.LOG_CALL.CALL_ID, callId);

            BasicDBObject updObj = new BasicDBObject(LogdbKey.LOG_CALL.END_TIME, DateFormat.format(endTime));
            updObj.append(LogdbKey.LOG_CALL.DURATION, duration);
            
            int partnerR;
            switch (partner_respond) {
                case Constant.CALL_ANSWER_VALUE.VIDEO_CALL_ANSWER:
                case Constant.CALL_ANSWER_VALUE.VOICE_CALL_ANSWER:
                    partnerR = PARTNER_ANSWER;
                    break;
                case Constant.CALL_ANSWER_VALUE.VIDEO_CALL_BUSY:
                case Constant.CALL_ANSWER_VALUE.VOICE_CALL_BUSY:
                    partnerR = PARTNER_BUSY;
                    break;
                case Constant.CALL_ANSWER_VALUE.VIDEO_CALL_REFUSE:
                case Constant.CALL_ANSWER_VALUE.VOICE_CALL_REFUSE:
                    partnerR = PARTNER_REFUSE;
                    break;
                default:
                    partnerR = UNKNOW_RESPOND;
                    break;
            }
            
            updObj.append(LogdbKey.LOG_CALL.PARTNER_RESPOND, partnerR);
            updObj.append(LogdbKey.LOG_CALL.FINISH_FLAG, Constant.FLAG.ON);
            BasicDBObject setObj = new BasicDBObject("$set", updObj);

            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean addDurationLog(String callId, Date endTime, int duration) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject findObj = new BasicDBObject(LogdbKey.LOG_CALL.CALL_ID, callId);
            findObj.append(LogdbKey.LOG_CALL.FINISH_FLAG, Constant.FLAG.OFF);

            BasicDBObject updObj = new BasicDBObject(LogdbKey.LOG_CALL.END_TIME, DateFormat.format(endTime));
            updObj.append(LogdbKey.LOG_CALL.DURATION, duration);
            updObj.append(LogdbKey.LOG_CALL.PARTNER_RESPOND, PARTNER_ANSWER);
            BasicDBObject setObj = new BasicDBObject("$set", updObj);

            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
