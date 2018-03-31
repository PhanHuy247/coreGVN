/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class BuzzDetailApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            List<String> list = new ArrayList<>();
            list.add(buzz.userId);
            Map<String, String> listName = UserDAO.getUserName(list);
            buzz.userName = listName.get(buzz.userId);
            respond = new EntityRespond(ErrorCode.SUCCESS, buzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return respond;
    }

}
