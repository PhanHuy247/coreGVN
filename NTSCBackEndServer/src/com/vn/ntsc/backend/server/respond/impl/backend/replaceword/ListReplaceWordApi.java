/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.replaceword;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import org.eclipse.jetty.server.Response;
import com.vn.ntsc.backend.dao.admin.ReplaceWordDAO;

/**
 *
 * @author RuAc0n
 */
public class ListReplaceWordApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        Util.addDebugLog("Test for obj request from replace word "+obj.toJSONString());
        try {
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            String search_word = null;
            if(Util.getStringParam(obj, "search_word") != null) {
                search_word = Util.getStringParam(obj, "search_word");
            }
            if (skip == null || take == null) {
                return null;
            }
            SizedListData data = ReplaceWordDAO.getList(null, skip, take, search_word);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
            Util.addDebugLog("Test response for replace word "+ data.toString());
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
