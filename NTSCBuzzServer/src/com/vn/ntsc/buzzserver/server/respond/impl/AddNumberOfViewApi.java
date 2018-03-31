/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzViewDAO;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.ViewNumberData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AddNumberOfViewApi  implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Util.addDebugLog("==========AddNumberOfViewApi==========");
        Respond respond = new Respond();
        try{
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            if (buzzId == null) {
                respond.code = ErrorCode.WRONG_DATA_FORMAT;
                return respond;
            }else{
                String view_from = "";
                if(userId != null){
                    view_from = userId;
                }else{
                    view_from = ip;
                }
                
                Boolean isViewed = BuzzViewDAO.isViewed(buzzId, view_from);
                if(!isViewed){
                    BuzzViewDAO.addBuzzViewInfo(buzzId, userId, ip);
                }
                
                Integer viewCount = BuzzViewDAO.count(buzzId);
                ViewNumberData data = new ViewNumberData(viewCount);
                respond = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
