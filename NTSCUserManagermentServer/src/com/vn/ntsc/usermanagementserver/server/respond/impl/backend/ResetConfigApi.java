/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.backend;

import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ResetConfigApi implements IApiAdapter {

  @Override
  public Respond execute(JSONObject obj, Date time) {
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
