/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogFreePointDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetFreePointHistoryApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond resp = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            String toDateStr = Util.getStringParam(obj, "to_date");
            String fromDateStr = Util.getStringParam(obj, "from_date");
//            Long type_id = Util.getLongParam(obj, ParamKey.USERID);
            //parse date
            Date toDate = DateFormat.parse_yyyyMMdd(toDateStr);
            Date fromDate = DateFormat.parse_yyyyMMdd(fromDateStr);
            resp.data = LogFreePointDAO.getListLog(userId, fromDate, toDate);
            resp.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return resp;
    }
    
}