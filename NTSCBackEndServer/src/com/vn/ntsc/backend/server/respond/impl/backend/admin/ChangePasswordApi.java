/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ChangePasswordApi implements IApiAdapter{

    private static Map<String, Integer> keys = new TreeMap<String, Integer>();
    static {
        keys.put(ParamKey.OLD_PASSWORD, 4);
        keys.put(ParamKey.NEW_PASSWORD, 5);
    }     
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            if(userId == null || userId.isEmpty())
                return null;
            String oldPass = Util.getStringParam(obj, ParamKey.OLD_PASSWORD);
            String newPass = Util.getStringParam(obj, ParamKey.NEW_PASSWORD);
            String originalPaswword = Util.getStringParam(obj, ParamKey.ORIGINAL_PASSWORD);
            int error = Validator.validate(obj, keys);
            if(error != ErrorCode.SUCCESS)
                return new Respond(error);
            AdminDAO.changePassword(userId, oldPass, newPass, originalPaswword);
            respond = new Respond(ErrorCode.SUCCESS);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new Respond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }    
}
