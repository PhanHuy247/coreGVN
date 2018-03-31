/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import com.vn.ntsc.jpns.dao.impl.LogNotificationDAO;
import com.vn.ntsc.jpns.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.jpns.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.jpns.server.constant.Params;
import com.vn.ntsc.jpns.util.InterCommunicator;
import com.vn.ntsc.jpns.util.MsgUtil;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class SendBuzzRunner extends Thread{

    @Override
    public void run() {
        while (true) {
            try {
                BuzzNotificationPackage pk = BuzzNotificationContainer.poll();
                if(pk != null){
                    String toUserid = pk.toUserid;
                    String fromUserid = pk.fromUserid;
                    if (fromUserid != null && toUserid != null) {
                        JSONObject msg = null;
                        NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUserid);
                        if (notification.notiBuzz != null && notification.notiBuzz == 0) { //OFF push noti
                            msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(pk.api, pk.fromUserName, fromUserid, toUserid, pk.buzzId, true, pk.urlAvatar, pk.text, pk.urlImage, pk.gender,pk.streamId);
                        } else if (notification.notiBuzz != null && notification.notiBuzz == 1) {//ON push noti
                            msg = MsgUtil.iosPayloadNotiNewBuzzForFavorited(pk.api, pk.fromUserName, fromUserid, toUserid, pk.buzzId, false, pk.urlAvatar, pk.text, pk.urlImage, pk.gender,pk.streamId);
                        }
                        InterCommunicator.send(msg, toUserid);
                        LogNotificationDAO.addLog(toUserid, Params.getNotiType(pk.api), fromUserid, Util.getGMTTime(), pk.ip);
                    }
                }else{
                    sleep();
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
        }
    }
    
    public static void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
