/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.ChatLogDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckCallData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class CheckCallApi implements IApiAdapter {

    private static final String video_call = "video_call";
    private static final String voice_call = "voice_call";
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String receiver = Util.getStringParam(obj, ParamKey.RECEIVER_ID);
            
            String callType = voice_call;
            if(type == 1){
                callType = video_call;
            }
            
            CheckCallData data = new CheckCallData();
            ConnectionPrice price = ConnectionPrice.getCommunicationPrice(callType, userId, receiver);
            data.callerPoint = 0 - price.senderPrice;
            data.receiverPoint = 0 - price.receiverPrice;
            User user = UserDAO.getUserInfor(userId);
            User friend = UserDAO.getUserInfor(receiver);
            if (user.lastPurchaseTime != null || user.gender == 1){
                data.isPurchase = 1;
            }
            else {
                data.isPurchase = 0;
            }
            
            int curCallerPoint = UserInforManager.getPoint(userId);
            int curReceiverPoint = UserInforManager.getPoint(receiver);
            
            int code = ErrorCode.SUCCESS;
            if(curCallerPoint < data.callerPoint){
                code = ErrorCode.NOT_ENOUGHT_POINT;
            }else if(curReceiverPoint < data.receiverPoint){
                code = ErrorCode.PATNER_NOT_ENOUGHT_POINT;
            }
            
            if (checkCallRequestSetting(friend.callRequestSetting, userId, receiver)){
                data.canSendRequest = 1;
            }
            Util.addDebugLog("HOT FIX " + user.applicationId + " " + user.appVersion + " " + friend.voiceCall + " " + friend.videoCall);
            if (user.deviceType == 1) {
                if ("3".equals(user.applicationId)
                        && ("3.9".equals(user.appVersion) || "4.0".equals(user.appVersion))
                        && (friend.videoCall == true || friend.voiceCall == true)) {
                    Util.addDebugLog("APPLICATION ID 3");
                    data.canSendRequest = 1;
                }

                if ("1".equals(user.applicationId)
                        && "3.8".equals(user.appVersion)
                        && (friend.videoCall == true || friend.voiceCall == true)) {
                    Util.addDebugLog("APPLICATION ID 1");
                    data.canSendRequest = 1;
                }
            }
            Util.addDebugLog("DATA CAN SEND REQUEST " + data.canSendRequest);
            result = new EntityRespond(code, data);
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    private static boolean checkCallRequestSetting(Integer call_request_setting, String userId, String friendId) throws EazyException{
        boolean result = false;
        if (call_request_setting != null){
            switch(call_request_setting){
                case 0:
                    result = true;
                    break;
                case 1:
                    List<String> friendFavoriteList = FavoristDAO.getFavouristList(friendId);
                    result = friendFavoriteList.contains(userId);
                    break;
                case 2:
                    result = ChatLogDAO.isContacted(friendId, userId);
                    break;
                case 3:
                    break;
            }
        }
        else {
            result = true;
        }
        return result;
    }

}
