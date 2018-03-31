/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AutoNewsNotifyDAO;
import com.vn.ntsc.backend.dao.admin.AutoQANotifyDAO;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNewsNotify;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoQANotify;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class GetQANotifyDetailApi implements IApiAdapter{
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            AutoQANotify data = AutoQANotifyDAO.get(id);
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
