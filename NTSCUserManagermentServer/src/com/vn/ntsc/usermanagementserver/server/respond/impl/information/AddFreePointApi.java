/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogFreePointDAO;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class AddFreePointApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            Long freePointType = Util.getLongParam(obj, "type_id");
            String ip = Util.getStringParam(obj, ParamKey.IP);
            if (point > 0) {
                ActionManager.doAddPointFromFreePage(userId, point.intValue(), null, freePointType, ip, time);
                LogFreePointDAO.addLog(userId, point.intValue(), freePointType.intValue(), time);
                
                // change have purchase for other purchase
//                UserInfor ui = UserInforManager.get(userId);
//                ui.havePurchased = true;
//                UserInforManager.put(ui);
//                UserDAO.updatePurchaseTime(userId, Util.convertToGMT());
                
                respond.code = ErrorCode.SUCCESS;
            } 
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
