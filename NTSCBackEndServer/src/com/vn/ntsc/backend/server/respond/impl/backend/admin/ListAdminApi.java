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
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListAdminApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try{
            List<IEntity> list = AdminDAO.listAdmin(Constant.FLAG.ON);
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
