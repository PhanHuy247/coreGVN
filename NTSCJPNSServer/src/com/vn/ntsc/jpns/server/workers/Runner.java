/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import javapns.notification.Payload;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.Core;

/**
 *
 * @author tuannxv00804
 */
public class Runner extends Thread {

    public static void sendMsgFCM(JSONObject msg, ReadyPackage p) {
        try {
            FCM fcm = FCM.sendNotification(msg.toJSONString(), p.device.deviceToken, Config.NTS_FCM);
            if (!fcm.verifyFmcRespond()) {
                Core.dao.remove(p.device);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    //HUNGDT add FCM
    public static void sendAndroidMsgFCM(JSONObject msg, ReadyPackage p) {
        try {
            FCM fcm = FCM.sendNotification(msg.toJSONString(), p.device.deviceToken, Config.NTS_FCM);
            if (!fcm.verifyFmcRespond()) {
                Core.dao.remove(p.device);
            }
//            if(fcm.isCanonicalIDs()){
//                if(Core.dao.isExistDevice(p.device.userid, fcm.registerId))
//                    Core.dao.remove(p.device);
//                else
//                    Core.dao.updateDeviceToken(p.device, fcm.registerId);
//            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static void sendIOSMsgFCM( JSONObject msg, ReadyPackage readyPackage ){
        try{
//            Payload p = new Payload( msg.toJSONString() ) {};
            FCM fcm = FCM.sendNotificationIOS(readyPackage.device.deviceToken, Config.NTS_FCM, msg.toJSONString());
            if(!fcm.verifyFmcRespond())
                Core.dao.remove(readyPackage.device);
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    } 

    @Override
    public void run() {
        while (true) {
            try {
                ReadyPackage p = MsgContainer.poll();
                if (p != null) {
                        if (p.device.deviceToken != null && !p.device.deviceToken.isEmpty()) {
                            if (p.device.deviceType == 0) {
                                sendIOSMsgFCM(p.msg, p);
                            } else if (p.device.deviceType == 1) {
                                sendAndroidMsgFCM(p.msg, p);
                            }
                            else {
                                sendMsgFCM(p.msg, p);
                            }
                    }
                } else {
                    sleep();
                }

            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
        }
    }
}
