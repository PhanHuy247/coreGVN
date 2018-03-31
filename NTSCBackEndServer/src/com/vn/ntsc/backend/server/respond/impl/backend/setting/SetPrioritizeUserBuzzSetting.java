/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import com.vn.ntsc.backend.dao.setting.PrioritizeUserBuzzDAO;
import com.vn.ntsc.backend.entity.impl.setting.PrioritizeUserBuzzSetting;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class SetPrioritizeUserBuzzSetting implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            List<String> buzzId = Util.getListString(obj, "list_buzz_id");
            List<String> listUser = Util.getListString(obj, "list_user_id");
            Integer takeBuzz = Util.getIntParam(obj, "take_buzz");
            Integer skipBuzz = Util.getIntParam(obj, "skip_buzz");
            
            if(buzzId == null && listUser == null){
                respond = new EntityRespond(ErrorCode.WRONG_DATA_FORMAT);
            }
            PrioritizeUserBuzzSetting pUserBuzz = new PrioritizeUserBuzzSetting(buzzId, listUser, takeBuzz, skipBuzz);
            PrioritizeUserBuzzDAO.updateUploadSetting(pUserBuzz);
            respond = new EntityRespond(ErrorCode.SUCCESS,pUserBuzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        }
        return respond;
    }
    
}
