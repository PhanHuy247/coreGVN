/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.screen;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.screen.ScreenAPIDAO;
import com.vn.ntsc.backend.dao.screen.ScreenDAO;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.screen.ListScreenData;
import com.vn.ntsc.backend.entity.impl.screen.Screen;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListScreenApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            List<IEntity> listGroup = ScreenGroupDAO.getAll(null);
            List<IEntity> listScreen = ScreenDAO.getAll(flag);
            for (IEntity entity : listScreen) {
                List<String> listApi = ScreenAPIDAO.listApi(((Screen) entity).id);
                ((Screen) entity).listApi = listApi;
            }
            ListScreenData data = new ListScreenData(listGroup, listScreen);
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
