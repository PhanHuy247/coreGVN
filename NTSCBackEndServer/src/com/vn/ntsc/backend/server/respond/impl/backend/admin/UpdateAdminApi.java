/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateAdminApi implements IApiAdapter{

    private static String REGEX = "^([0-9a-zA-Z]{2,30})$";
    private static final Pattern pattern = Pattern.compile(REGEX);    
    private static final int flag_error = 8;
    private static Map<String, Integer> keys = new TreeMap<String, Integer>();
    static {
        keys.put(ParamKey.EMAIL, 4);
        keys.put(ParamKey.PASSWORD, 5);
        keys.put(ParamKey.NAME, 6);
        keys.put(ParamKey.ROLE_ID, 7);
    }    
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            if(id == null || id.isEmpty())
                return null;
            if(AdminDAO.isSupperAdmin(id))
                return new Respond(ErrorCode.UNKNOWN_ERROR);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            String password = Util.getStringParam(obj, ParamKey.PASSWORD);
            String originalPassword = Util.getStringParam(obj, ParamKey.ORIGINAL_PASSWORD);
            String name = Util.getStringParam(obj, ParamKey.NAME);
            String roleId = Util.getStringParam(obj, ParamKey.ROLE_ID);
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            if(flag == null || flag < 0 || flag > 1)
                return new Respond(flag_error);
            int error = validate(obj, keys);
            if(error != ErrorCode.SUCCESS)
                return new Respond(error);
            AdminDAO.update(id, email, password, originalPassword, roleId, name, flag.intValue());
            respond = new Respond(ErrorCode.SUCCESS);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new Respond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
    private static int validate(JSONObject obj, Map<String, Integer> keys){
        for(Map.Entry<String, Integer> pair : keys.entrySet()){
            try{
                String str = Util.getStringParam(obj, pair.getKey());
                if(str == null || str.trim().isEmpty())
                    return pair.getValue();
                if(pair.getKey().equals(ParamKey.EMAIL)){
                    if(!validate(str))
                        return pair.getValue();
                }
                    
            }catch(Exception ex){
                Util.addErrorLog(ex);                
                return pair.getValue();
            }
        }
        return 0;
    }
  
    private static boolean validate(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }    
}
