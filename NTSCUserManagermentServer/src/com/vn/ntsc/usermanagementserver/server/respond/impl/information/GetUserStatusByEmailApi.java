/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUserStatusByEmail;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Admin
 */
public class GetUserStatusByEmailApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();

        try {
            String email = Util.getStringParam(obj, "email");
            String facebookId = Util.getStringParam(obj, "fb_id");
            String mocomId = Util.getStringParam(obj, "mocom_id");
            String famuId = Util.getStringParam(obj, "famu_id");
            String value;
            int type;
            if(email != null && !email.isEmpty()){
                value = email;
                type = Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE;
            }else if(facebookId != null && !facebookId.isEmpty()){
                value = facebookId;
                type = Constant.ACCOUNT_TYPE_VALUE.FB_TYPE;
            }else if(mocomId != null && !mocomId.isEmpty()){
                value = mocomId;
                type = Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE;
            }else{
                value = famuId;
                type = Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE;
            }
            Integer flag = UserDAO.getFlagByEmail(value, type);
            if(flag != null)
                respond = new EntityRespond(ErrorCode.SUCCESS, new GetUserStatusByEmail(email, flag));
            else
                respond.code = ErrorCode.EMAIL_NOT_FOUND;
//            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
