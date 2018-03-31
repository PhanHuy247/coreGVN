/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzTagDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AddTagApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Long privacy = Util.getLongParam(obj, ParamKey.PRIVACY);
            String reqUserId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);

            String buzzUser = BuzzDetailDAO.getUserId(buzzId);
            if(userId.equals(buzzUser)){
                BuzzTagDAO.addTag(buzzId, reqUserId);
                UserBuzzDAO.updateBuzzActivity(buzzId, userId, time, null, Constant.BUZZ_TYPE_VALUE.STREAM_STATUS, privacy, null);
                result = new EntityRespond(ErrorCode.SUCCESS);
            }else{
                result = new EntityRespond(ErrorCode.ACCESS_DENIED);
            }
            
        }catch(Exception e){
            Util.addErrorLog(e);
        }
        return result;
    }
    
}
