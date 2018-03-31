/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screengroup;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListScreenGroupApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            List<IEntity> list = ScreenGroupDAO.getAll(flag);
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode(), null);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
