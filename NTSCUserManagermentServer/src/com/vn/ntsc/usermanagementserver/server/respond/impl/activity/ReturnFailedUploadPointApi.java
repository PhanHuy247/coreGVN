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
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.PointExchangedByChatData;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class ReturnFailedUploadPointApi implements IApiAdapter{
    
    /**
     * 
     * @param obj {API, message_id, ip}
     * @param time
     * @return 
     */
    @Override
    public Respond execute(JSONObject obj, Date time) {
        
        Respond result = new Respond();
        try {
            String messageId = Util.getStringParam(obj, ParamKey.MESSAGE_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            PointExchangedByChatData data = PointExchangedByChatDAO.getDocumentByMessageId(messageId);
            if (data!=null){
                /**
                 * get pointDiffer from PointExchangedByChatDAO and return to user.
                 */
                String userId = data.getUserId();
                int pointDiffer = data.getPointDiffer();
                ActionManager.returnPoint(userId, pointDiffer, ip);

                /**
                 * Delete the respective document after return point
                 */
                boolean resultRemove = PointExchangedByChatDAO.removeDocumentByMessageId(messageId);   

            }
            result = new EntityRespond (ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = new Respond(ErrorCode.UNKNOWN_ERROR);
        }
        return result;

    }
    
}   
