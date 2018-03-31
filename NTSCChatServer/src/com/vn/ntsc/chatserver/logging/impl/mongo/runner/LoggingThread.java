/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging.impl.mongo.runner;

import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.logging.impl.mongo.MongoLogger;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.message.messagetype.SendFileMessage;
import com.vn.ntsc.dao.impl.FileChatDAO;
import eazycommon.exception.EazyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tuannxv00804
 */
public class LoggingThread extends Thread {

    public static final int IdleThreadLatency = Config.IdleThreadLatency;

    public LoggingThread() {
    }

    public static boolean isConfirmReadMessage(Message msg) {
        if (msg.msgType == MessageType.MDS
                && msg.value != null
                && !msg.value.isEmpty()) {
            String value = MessageTypeValue.MsgStatus_getStatus(msg);
            if (value != null && (value.equals(MessageTypeValue.MsgStatus_Read) || value.equals(MessageTypeValue.MsgStatus_Read_All))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            Message msg = MongoLogger.Container.poll();
            if (msg != null) {
                if (isConfirmReadMessage(msg)) {
                    Core.getDAO().updateReadMessage(msg.to, msg.from, msg.to);
                } else if (msg.msgType != MessageType.MDS && msg.msgType != MessageType.AUTH && msg.msgType != MessageType.PRC) {
                    Core.getDAO().save(msg);
                }
            } else {
                sleep(IdleThreadLatency);
            }
        }
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    private void deletePreviousFile(String api, Message msg) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.USER_ID, msg.from);
        jo.put(ParamKey.API_NAME, api);
        jo.put(ParamKey.IMAGE_ID, SendFileMessage.getFileID(msg.value));

        String request = jo.toJSONString();
        Util.sendRequest(request, Config.UMSServer_IP, Config.UMSServer_Port);

    }     
}
