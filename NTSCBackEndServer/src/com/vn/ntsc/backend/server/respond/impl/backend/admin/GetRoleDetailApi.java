/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.Role;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class GetRoleDetailApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            if(id == null || id.isEmpty())
                return null;
            IEntity entity = RoleDAO.getDetail(id);
            List<String> listGroup = RoleGroupManager.getListGroup(id);
            ((Role)entity).listGroup = listGroup;
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
