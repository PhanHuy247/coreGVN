/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.workerfactory.impl;

import com.vn.ntsc.Core;
import eazycommon.util.Util;

/**
 *
 * @author Phan Huy
 */
public class WorkerExprieToken extends Thread{
    public static int IdleThreadLatency = 1000; //ms
    @Override
    public void run() {
        while(true){
            Core.getStoreEngine().expireToken();
            sleep();
        }
    }
    
    private void sleep() {
        try {
            Thread.sleep(IdleThreadLatency * 60 * 10);
        } catch (InterruptedException ex) {
            Util.addErrorLog(ex);
        }
    }
}
