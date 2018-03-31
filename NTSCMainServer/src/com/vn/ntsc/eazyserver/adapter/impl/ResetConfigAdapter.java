/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import com.vn.ntsc.Config;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class ResetConfigAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        try {
            Config.initConfig();
            String backend = InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
//            Util.addInfoLog(backend);
            String buzz = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
//            Util.addInfoLog(buzz);
            String chat = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
//            Util.addInfoLog(chat);
            String jpns = InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
//            Util.addInfoLog(jpns);
            String meet = InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
//            Util.addInfoLog(meet);
            String stf = InterCommunicator.sendRequest(request.toJson(), Config.StaticFileServerIp, Config.StaticFileServerPort);
//            Util.addInfoLog(stf);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//            Util.addInfoLog(ums);
            return ResponseMessage.SuccessMessage;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return ResponseMessage.UnknownError;
        }
    }

}
