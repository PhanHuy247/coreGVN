/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUpdateInfoFlagsData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetUpdateInfoFlagsApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
        String userId = Util.getStringParam(obj, ParamKey.USER_ID);
        try {
            List<Integer> list = UserDAO.getUpdateInfoFlags(userId);
            respond.code = ErrorCode.SUCCESS;
            GetUpdateInfoFlagsData data = new GetUpdateInfoFlagsData();
            if (list.size() < 3) {
                data.updateEmailFlag = list.get(0);
                data.finishRegisterFlag = list.get(1);
            } else {
                data.updateEmailFlag = list.get(0);
                data.finishRegisterFlag = list.get(1);
                data.verificationFlag = list.get(2);
            }
            respond.data = data;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond.code = ex.getErrorCode();
        }

        return respond;
    }

}
