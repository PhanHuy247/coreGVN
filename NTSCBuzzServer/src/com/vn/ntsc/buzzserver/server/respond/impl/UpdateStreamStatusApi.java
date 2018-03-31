/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class UpdateStreamStatusApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try{
            String key = Util.getStringParam(obj, ParamKey.KEY);
            if(key != null && key.equals(Constant.LIVESTREAM_SERVER_KEY)){
                List<Buzz> listBuzz = BuzzDetailDAO.getStreamBuzz(Constant.STREAM_STATUS.ON);
                Boolean success = BuzzDetailDAO.updateStreamBuzz(listBuzz, Constant.STREAM_STATUS.PENDING);
                if(success){
                    UserBuzzDAO.updateStreamBuzzStatus(listBuzz, Constant.STREAM_STATUS.PENDING);
                }
                result = new EntityRespond(ErrorCode.SUCCESS);
            }else{
                result = new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
        }catch(Exception e){
            Util.addErrorLog(e);
        }
        return result;
    }
    
}
