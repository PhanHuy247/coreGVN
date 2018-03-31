/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.api;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListStringRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListAPIApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListStringRespond respond = new ListStringRespond();
        try {
            List<String> data = APIManager.getAllApiName();
            respond = new ListStringRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
