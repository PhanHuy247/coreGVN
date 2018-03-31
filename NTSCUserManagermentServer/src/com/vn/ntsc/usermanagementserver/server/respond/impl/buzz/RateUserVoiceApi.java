/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserRateDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class RateUserVoiceApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String reqUserId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Integer rateValue = Util.getIntParam(obj, ParamKey.RATE_VALUE);

            Boolean success = UserRateDAO.addRate(userId, reqUserId, rateValue);
            
            if(success){
                List<Integer> rateList = UserRateDAO.getUserRate(reqUserId);
                Util.addDebugLog("rateList ================== "+rateList);
                UserDAO.updateUserRate(reqUserId, rateList);
                result = new EntityRespond(ErrorCode.SUCCESS);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
