/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListStatusApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {

        ListEntityRespond result = new ListEntityRespond();
        try {
            List<String> listUser = Util.getListString(obj, ParamKey.LIST_USER);
            long getStatusListStart = System.currentTimeMillis();
            List<IEntity> listStatus = UserDAO.getListAboutbyListUserId(listUser);
            long getStatusListEnd = System.currentTimeMillis();
            getStatusListEnd -= getStatusListStart;
            if(getStatusListEnd > 1500){
                Util.addInfoLog("List Status API : get status list slow");
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS, listStatus);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
