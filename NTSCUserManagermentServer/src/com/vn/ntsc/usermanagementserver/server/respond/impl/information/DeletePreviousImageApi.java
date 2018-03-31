/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.ChatImageDAO;

/**
 *
 * @author RuAc0n
 */
public class DeletePreviousImageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            ChatImageDAO.removeChatImage(userId, imageId);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        } 
        return new Respond(ErrorCode.SUCCESS);
    }
    
}
