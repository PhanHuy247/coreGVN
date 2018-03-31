/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.server.systemaccount.SystemAccountManager;

/**
 *
 * @author RuAc0n
 */
public class GetSystemAccountApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try{
            List<IEntity> list = SystemAccountManager.toList();
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
}
