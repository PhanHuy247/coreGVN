/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
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
public class UpdateStreamVideoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Integer viewNumber = Util.getIntParam(obj, ParamKey.VIEW_NUMBER);
            Integer currentView = Util.getIntParam(obj, ParamKey.CURRENT_VIEW);
            Integer duration = Util.getIntParam(obj, ParamKey.DURATION);
            String streamStatus = Util.getStringParam(obj, ParamKey.STREAM_STATUS);
//            Long privacy = Util.getLongParam(obj, ParamKey.PRIVACY);
            JSONArray video_list = (JSONArray) obj.get(ParamKey.VID_LIST);
            
            if(streamStatus.equals(Constant.STREAM_STATUS.UPDATE)){
                BuzzDetailDAO.updateView(userId, buzzId, viewNumber, currentView, duration);
            }else{
                if(video_list.size() != 0){
                    JSONObject temp = (JSONObject) video_list.get(0);
                    String file = (String) temp.get("data"); 
                    Integer affFlag = ((Long) temp.get("is_app")).intValue();
                    BuzzDetailDAO.updateBuzz(userId, buzzId, time, affFlag, file, viewNumber, currentView, streamStatus);

                    UserBuzzDAO.updateBuzzActivity(buzzId, userId, time, affFlag, Constant.BUZZ_TYPE_VALUE.STREAM_STATUS, null, streamStatus);
                }else{
                    BuzzDetailDAO.updateBuzz(userId, buzzId, time, null, null, viewNumber, currentView, streamStatus);

                    UserBuzzDAO.updateBuzzActivity(buzzId, userId, time, null, Constant.BUZZ_TYPE_VALUE.STREAM_STATUS, null, streamStatus);
                }
            }
            
            result = new EntityRespond(ErrorCode.SUCCESS);
        }catch(Exception e){
            Util.addErrorLog(e);
        }
        return result;
    }
    
}
