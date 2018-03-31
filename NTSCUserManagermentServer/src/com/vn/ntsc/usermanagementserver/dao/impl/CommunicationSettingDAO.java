/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckCallData;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;


/**
 *
 * @author RuAc0n
 */
public class CommunicationSettingDAO {

    private static DBCollection coll;
    
    private static final String male = "male";
    private static final String female = "female";

    static {
        try {
            coll = DatabaseLoader.getSettingDB().getCollection(SettingdbKey.COMMUNICATION_SETTING_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }



    public static int checkCall(String type, String callerId, String receiverId, CheckCallData data ) throws EazyException {
        int result = 0;
        int callGender = UserInforManager.getGender(callerId);
        int receiverGender = UserInforManager.getGender(receiverId);
        int callPoint = UserInforManager.getPoint(callerId);
        int receiverPoint = UserInforManager.getPoint(receiverId);
        try {
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.COMMUNICATION_SETTING.TYPE, type);
            
            String callGenderStr = female;
            if(callGender == Constant.GENDER.MALE ){
                callGenderStr = male;
            }
            
            String receiverGenderStr = female;
            if(receiverGender == Constant.GENDER.MALE ){
                receiverGenderStr = male;
            }
            
            String pair = callGenderStr + "_" + receiverGenderStr;
            
            findObj.append(SettingdbKey.COMMUNICATION_SETTING.CALLER_RECEIVER, pair);
            
            DBObject obj = coll.findOne(findObj);
            
            Integer callCharge = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.CALLER);
            Integer receiverCharge = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.RECEIVER);
            Integer potentialReceiverCharge = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER);
            Integer potentialCallerCharge = (Integer) obj.get(SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER);
            
            boolean callerHavePurchase = UserInforManager.havePurchased(callerId);
            boolean receiverHavePurchase = UserInforManager.havePurchased(receiverId);
            
            callCharge = !callerHavePurchase && potentialCallerCharge != null ? potentialCallerCharge : callCharge;
            receiverCharge = ! receiverHavePurchase && potentialReceiverCharge != null ? potentialReceiverCharge : receiverCharge;
            data.callerPoint = 0 - callCharge;
            data.receiverPoint = 0 - receiverCharge;
            if(callPoint < 0 - callCharge )
                return ErrorCode.NOT_ENOUGHT_POINT;
            if(receiverPoint < 0 - receiverCharge)
                return ErrorCode.PATNER_NOT_ENOUGHT_POINT;
  
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
