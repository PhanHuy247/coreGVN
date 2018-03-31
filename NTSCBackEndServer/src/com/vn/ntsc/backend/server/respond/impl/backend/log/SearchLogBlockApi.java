/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.log.LogBlockDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogBlock;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SearchLogBlockApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long requestUserType = Util.getLongParam(obj, ParamKey.REQUEST_USER_TYPE);
            String requestEmail = Util.getStringParam(obj, ParamKey.REQUEST_EMAIL);
            if (requestUserType == null) {
                if (requestEmail != null) {
                    return null;
                }
            }
            String requestUId = Util.getStringParam(obj, ParamKey.REQUEST_ID);
            String requestCmCode = Util.getStringParam(obj, ParamKey.REQUEST_CM_CODE);
            List<String> listRequestId = UserDAO.getListUser(requestUserType, requestUId, requestEmail, requestCmCode);

            Long partnerUserType = Util.getLongParam(obj, ParamKey.PARTNER_USER_TYPE);
            String partnerEmail = Util.getStringParam(obj, ParamKey.PARTNER_EMAIL);
            if (partnerUserType == null) {
                if (partnerEmail != null) {
                    return null;
                }
            }
            String partnerUId = Util.getStringParam(obj, ParamKey.PARTNER_ID);
            String patCmCode = Util.getStringParam(obj, ParamKey.PARTNER_CM_CODE);
            List<String> listPartnerId = UserDAO.getListUser(partnerUserType, partnerUId, partnerEmail, patCmCode);

            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
            String fromTime = Util.getStringParam(obj, ParamKey.FROM_TIME);

            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (sort == null || order == null) {
                return null;
            }

            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            SizedListData data = LogBlockDAO.listLog(listRequestId, listPartnerId, fromTime, toTime,
                    sort, order, skip, take, isCsv);
            if (isCsv) {
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, null, new LogBlock());
            }
            LogProcess.getTwoObjectInfor(data.ll);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
