/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.PointExchangedByChatDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author HuyDX
 */
public class RemoveSuccessUploadPointDocumentApi implements IApiAdapter{
    
    /**
     * 
     * @param obj : {API, message_id}
     * @return 
     */
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        
        Respond result = new Respond (ErrorCode.SUCCESS);
        try {
            String messageId = Util.getStringParam(obj, ParamKey.MESSAGE_ID);
            boolean resultRemove = PointExchangedByChatDAO.removeDocumentByMessageId(messageId);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = new Respond (ErrorCode.UNKNOWN_ERROR);
        }
        return result;

    }
    
}
