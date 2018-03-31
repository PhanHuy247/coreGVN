/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl.backend.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.impl.backend.IServiceBackendAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class BackEndLogAdapter implements IServiceBackendAdapter{

    @Override
    public String callService(Request request) {
        return InterCommunicator.sendRequest(request.toJson(), Config.BackEndServerIp, Config.BackEndServerPort);
    }
    
    public static byte[] sendCSVRequest (Request request){
        return InterCommunicator.sendCSVRequest(request.toJson(),Config.BackEndServerIp, Config.BackEndServerPort);
    }
}
