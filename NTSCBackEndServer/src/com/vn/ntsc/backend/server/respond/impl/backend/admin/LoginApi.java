/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class LoginApi implements IApiAdapter{
    private static Map<String, Integer> keys = new TreeMap<String, Integer>();
    static {
        keys.put(ParamKey.EMAIL, 4);
        keys.put(ParamKey.PASSWORD, 5);
    }       
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            String password = Util.getStringParam(obj, ParamKey.PASSWORD);
            IEntity entity = AdminDAO.login(email, password);
            respond = new EntityRespond(ErrorCode.SUCCESS, entity);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }    
}
