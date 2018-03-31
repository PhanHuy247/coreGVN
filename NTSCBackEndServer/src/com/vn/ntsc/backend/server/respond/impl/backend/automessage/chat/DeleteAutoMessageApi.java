/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AutoMessageChatDAO;
import com.vn.ntsc.backend.server.automessage.MessageContainer;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class DeleteAutoMessageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            AutoMessageChatDAO.delete(id);
            MessageContainer.remove(id);
            respond = new Respond(ErrorCode.SUCCESS);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
}
