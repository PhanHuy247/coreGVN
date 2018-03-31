/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.Collections;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.ActionPointPacketDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.ActionPointPacket;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListPointPacketApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            Long type = Util.getLongParam(request.reqObj, "pro_type");
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }
            if (type == null) {
                return result;
            }
//            List<PointPacket> listRespond = PointPacketDAO.getAllPointPacket(type.intValue());
            List<ActionPointPacket> listRespond = ActionPointPacketDAO.getOtherPacketMultiapp(type.intValue(),applicationId);
            Collections.sort(listRespond);
            for (int i = 0; i< listRespond.size();i++) {
                Util.addDebugLog("ListPointPacketApi listRespond =========" + listRespond.get(i));
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS, listRespond);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
