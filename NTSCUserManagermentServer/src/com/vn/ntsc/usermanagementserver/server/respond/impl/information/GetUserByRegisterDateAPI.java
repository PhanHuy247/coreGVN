/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUserByRegisterDateData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Rua
 */
public class GetUserByRegisterDateAPI implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String startTime = Util.getStringParam(obj, "start_time");
            String endTime = Util.getStringParam(obj, "end_time");
            List<GetUserByRegisterDateData> list = UserDAO.getUsersByRegisterTime(startTime, endTime);
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
