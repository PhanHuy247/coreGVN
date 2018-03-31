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
import com.vn.ntsc.backend.dao.log.LogShakeChatDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogShakeChat;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SearchLogShakeChatApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            if (userType == null) {
                if (email != null) {
                    return null;
                }
            }
            String uId = Util.getStringParam(obj, ParamKey.ID);
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            List<String> listId = UserDAO.getListUser(userType, uId, email, cmCode);
            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
//            toTime = getToTime(toTime);
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
            SizedListData data = LogShakeChatDAO.listLog(listId, fromTime, toTime,
                    sort, order, skip, take, isCsv);
            if (isCsv) {
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, null, new LogShakeChat());
            }
            LogProcess.getShakeChatInfor(data.ll);
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
