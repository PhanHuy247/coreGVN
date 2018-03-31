/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.admin;

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
public class DeleteAdminApi implements IApiAdapter{
   
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            if(id == null || id.isEmpty())
                return null;
            if(AdminDAO.isSupperAdmin(id))
                return new Respond(ErrorCode.UNKNOWN_ERROR);
            AdminDAO.delete(id);
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
