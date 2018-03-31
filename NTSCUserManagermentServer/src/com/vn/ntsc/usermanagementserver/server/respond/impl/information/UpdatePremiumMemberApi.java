/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInfor;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class UpdatePremiumMemberApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            Long type = Util.getLongParam(obj, "account_type");
            boolean hasPurchase = false;
            if(type != null && type == Constant.FLAG.ON)
                hasPurchase = true;
            
            // change have purchase for other purchase
            UserInfor ui = UserInforManager.get(userId);
            ui.havePurchased = hasPurchase;
            UserInforManager.put(ui);
            UserDAO.updatePurchaseTime(userId, Util.getGMTTime(), hasPurchase);

            respond.code = ErrorCode.SUCCESS;
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
