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
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InsertRoleApi implements IApiAdapter{

    private static final int list_error = 5;
    private static final Map<String, Integer> keys = new TreeMap<>();
    static {
        keys.put(ParamKey.NAME, 4);
    }     
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String name = Util.getStringParam(obj, ParamKey.NAME);
            List<String> listGroup = Util.getListString(obj, "lst_group");
            if(listGroup == null)
                return new EntityRespond(list_error);
            int error = Validator.validate(obj, keys);
            if(error != ErrorCode.SUCCESS)
                return new EntityRespond(error);
            String id = RoleDAO.insert(name);

            for(String group : listGroup){
                RoleGroupDAO.update(group, id);
            }
            RoleGroupManager.add(id, listGroup);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }    
}
