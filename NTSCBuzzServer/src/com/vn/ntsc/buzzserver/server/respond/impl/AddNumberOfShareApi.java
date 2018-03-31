/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserInteractionDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.ShareNumberData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AddNumberOfShareApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond respond = new Respond();
        try{
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            if (buzzId == null) {
                respond.code = ErrorCode.WRONG_DATA_FORMAT;
                return respond;
            }else{
                Buzz buzz = BuzzDetailDAO.getBuzzAndAddNumberOfShare(buzzId);
                if(buzz != null){
                    if (userId != null && userId.isEmpty()){
                        String ownId = buzz.userId;
                        addInteraction(userId, ownId);
                    }
                    ShareNumberData data = new ShareNumberData(buzz.shareNumber + 1);
                    respond = new EntityRespond(ErrorCode.SUCCESS, data);
                }else{
                    respond.code = ErrorCode.ACCESS_DENIED;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
    public static void addInteraction(String userId, String friendId) {

        boolean isExistUserInteractionList = UserInteractionDAO.checkExistInteractionList(userId);
        boolean isExistFriendInteractionList = UserInteractionDAO.checkExistInteractionList(friendId);
        boolean isInteracted = UserInteractionDAO.checkExistInteraction(userId, friendId);

        if (isExistUserInteractionList && !isInteracted) {
            UserInteractionDAO.updateInteraction(userId, friendId);
        } else if (!isExistUserInteractionList) {
            UserInteractionDAO.addInteraction(userId, friendId);
        }

        if (isExistFriendInteractionList && !isInteracted) {
            UserInteractionDAO.updateInteraction(friendId, userId);
        } else if (!isExistFriendInteractionList) {
            UserInteractionDAO.addInteraction(friendId, userId);
        }
    }
}
