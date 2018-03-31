/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ResetConfigApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        try {
           Config.initConfig();
           respond = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
