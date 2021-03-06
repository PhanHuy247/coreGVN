/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.bannedword;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.BannedWordDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListBannedWordApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
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
            SizedListData data = BannedWordDAO.getList(null, skip, take, search_word);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
