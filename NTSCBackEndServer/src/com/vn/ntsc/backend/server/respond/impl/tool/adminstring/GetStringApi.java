/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.tool.adminstring;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import org.json.simple.JSONObject;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminStringDAO;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.StringRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class GetStringApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        StringRespond respond = new StringRespond();
        try {
            String str = AdminStringDAO.get();
            respond = new StringRespond(ErrorCode.SUCCESS, str);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new StringRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
