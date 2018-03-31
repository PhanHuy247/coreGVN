/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.Role;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListRoleApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try{
            List<IEntity> list = RoleDAO.getAll(Constant.FLAG.ON);
            for(IEntity entity : list){
                String id = ((Role)entity).id;
                List<String> listGroup = RoleGroupManager.getListGroup(id);
                if(listGroup != null)
                ((Role)entity).listGroup = listGroup;
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new ListEntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
}
