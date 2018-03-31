/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.ChatImageTransactionDAO;

/**
 *
 * @author RuAc0n
 */
public class SendImageByChatApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String from = Util.getStringParam(obj, "from");
            String to = Util.getStringParam(obj, "to");
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long serverTime = Util.getLongParam(obj, ParamKey.TIME);
            
            if (!ChatImageTransactionDAO.isDocumentExist(from, to, imageId))
                ChatImageTransactionDAO.add(from, to, imageId, serverTime, ip);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
