/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AutoNotifyDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNotify;

/**
 *
 * @author RuAc0n
 */
public class GetAutoNotifyDetailApi implements  IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            AutoNotify data = AutoNotifyDAO.get(id);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }     
}
