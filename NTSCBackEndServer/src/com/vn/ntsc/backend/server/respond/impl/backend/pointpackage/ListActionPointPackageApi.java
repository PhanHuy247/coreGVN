/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.pointpackage;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.pointpackage.ActionPointPacketDAO;
import com.vn.ntsc.backend.entity.impl.pointpackage.ActionPointPacket;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListActionPointPackageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String applicationId = "1";
            if (obj.get("application") != null && Util.getLongParam(obj, "application").toString() != null) {
                applicationId = Util.getLongParam(obj, "application").toString();
            }
            //String applicationId = ApplicationDAO.getIdByUniqueName(applicationName);

            Util.addDebugLog("applicationId " + applicationId);
            List<ActionPointPacket> list = ActionPointPacketDAO.getMultiapp(type.intValue(),applicationId);
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
