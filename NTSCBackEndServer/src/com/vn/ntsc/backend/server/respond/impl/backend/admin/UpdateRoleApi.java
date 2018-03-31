/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateRoleApi implements IApiAdapter{
    private static final int list_error = 5;
    private static final Map<String, Integer> keys = new TreeMap<>();
    static {
        keys.put(ParamKey.NAME, 4);
    }
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            String name = Util.getStringParam(obj, ParamKey.NAME);
            List<String> listNewGroup = Util.getListString(obj, "lst_group");
            if(id == null)
                return null;
            if(listNewGroup == null)
                return new Respond(list_error);
            int error = Validator.validate(obj, keys);
            if(error != ErrorCode.SUCCESS)
                return new Respond(error);
            RoleDAO.update(id, name);
            List<String> listOldGroup = RoleGroupManager.getListGroup(id);
            if(listOldGroup != null){
                for(String group : listOldGroup){
                    if(!listNewGroup.contains(group)){
                        RoleGroupDAO.remove(group, id);
                    }
                }
            
                for(String group : listNewGroup){
                    if(!listOldGroup.contains(group)){
                        RoleGroupDAO.update(group, id);
                    }
                }
            }else{
                for(String group : listNewGroup){
                    RoleGroupDAO.update(group, id);
                }
            }
            RoleGroupManager.add(id, listNewGroup);
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
