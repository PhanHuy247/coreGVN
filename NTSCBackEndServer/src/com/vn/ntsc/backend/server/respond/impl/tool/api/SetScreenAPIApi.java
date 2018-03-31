/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.api;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.screen.ScreenAPIDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.rolegroupmanagement.ScreenAPIManager;

/**
 *
 * @author RuAc0n
 */
public class SetScreenAPIApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            List<String> listNewApi = Util.getListString(obj, "lst_api");
            String screenId = Util.getStringParam(obj, ParamKey.ID);
            if (listNewApi != null) {
                List<String> listOldApi = ScreenAPIDAO.listApi(screenId);
                for (String api : listOldApi) {
                    if (!listNewApi.contains(api)) {
                        ScreenAPIManager.removeAPI(api, screenId);
                        ScreenAPIDAO.remove(screenId, api);
                    }
                }
                for (String api : listNewApi) {
                    if (!listOldApi.contains(api)) {
                        ScreenAPIManager.addAPI(api, screenId);
                        ScreenAPIDAO.update(screenId, api);
                    }
                }
            }
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
