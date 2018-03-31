/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.messageio;

import eazycommon.constant.Constant;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.user.UserInfo;
import com.vn.ntsc.dao.impl.FileDAO;
import com.vn.ntsc.dao.impl.ImageStfDAO;
import com.vn.ntsc.dao.impl.UserDAO;

/**
 *
 * @author tuannxv00804
 */
public class MessageParser {

    static final String msgPattern = "\\{[^}]+\\}";
    static final Pattern pattern = Pattern.compile(msgPattern);

    public static String serialize(Message msg) {
//        StringBuilder builder = new StringBuilder();
//
//        Util.addDebugLog("MessageParser serialize " + msg.value);
//        String value = StringCoder.encode(msg.value);
//        builder.append(StringCoder.OpenBracket)
//                .append(msg.id).append(StringCoder.Semicolon)
//                .append(msg.from).append(StringCoder.Semicolon)
//                .append(msg.to).append(StringCoder.Semicolon)
//                .append(msg.msgType.toString()).append(StringCoder.Semicolon)
//                .append(value).append(StringCoder.Semicolon);
//
//        String originTimeStr = (msg.originTime == null) ? StringCoder.BlankChar : DateFormat.format_yyyyMMddHHmmssSSS(msg.originTime);
//        String serverTimeStr = (msg.serverTime == 0) ? StringCoder.BlankChar : DateFormat.format_yyyyMMddHHmmssSSS(msg.serverTime);
//        String destTimeStr = (msg.destinationTime == null) ? StringCoder.BlankChar : DateFormat.format_yyyyMMddHHmmssSSS(msg.destinationTime);
//
//        builder.append(originTimeStr).append(StringCoder.Semicolon)
//                .append(serverTimeStr).append(StringCoder.Semicolon)
//                .append(destTimeStr).append(StringCoder.CloseBracket);
//                .append( destTimeStr ).append( StringCoder.Semicolon )
//                .append( msg.);

//        return builder.toString();
        String jsonString = msg.toJsonObject().toString();
        Util.addDebugLog("JSON SENT CLIENT==================================================="+jsonString);
        return jsonString;
    }

    static int Index_ID = 0;
    static int Index_From = 1;
    static int Index_To = 2;
    static int Index_MsgType = 3;
    static int Index_Value = 4;
    static int Index_OriginTime = 5;
    static int Index_ServerTime = 6;
    static int Index_DestinationTime = 7;

    public static Message deserialize(String str) {
        try {
            String[] eles = str.split(StringCoder.Semicolon_Pattern);

            String id = eles[Index_ID].substring(1);
            String from = eles[Index_From];
            String to = eles[Index_To];

            String msgTypeStr = eles[Index_MsgType];
            MessageType msgType = MessageType.PP;
            try {
                msgType = MessageType.valueOf(msgTypeStr);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }

            String value = eles[Index_Value];
            value = StringCoder.decode(value);
//            if(value.equals(""))
//            value = Util.replaceWordChat(value);
            String originTimeStr = eles[Index_OriginTime];
            Date originTime = null;
            if (originTimeStr != null
                    && !originTimeStr.contains(StringCoder.BlankChar)
                    && !originTimeStr.isEmpty()) {
//                originTime = Message.SDF.parse( originTimeStr );
                originTime = DateFormat.parse_yyyyMMddHHmmssSSS(originTimeStr);
            }

            String serverTimeStr = eles[Index_ServerTime];
            long serverTime = Util.currentTime();
            if (serverTimeStr != null
                    && !serverTimeStr.equals(StringCoder.BlankChar)
                    && !serverTimeStr.isEmpty()) {
//                serverTime = Message.SDF.parse( serverTimeStr );
                try {
                    serverTime = Long.parseLong(serverTimeStr);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                }
            }

            String destTimeStr = eles[Index_DestinationTime];
            destTimeStr = destTimeStr.substring(0, destTimeStr.length() - 1);
            Date destTime = null;
            if (destTimeStr != null
                    && !destTimeStr.equals(StringCoder.BlankChar)
                    && !destTimeStr.isEmpty()) {
//                destTime = Message.SDF.parse( destTimeStr );
                destTime = DateFormat.parse_yyyyMMddHHmmssSSS(destTimeStr);
            }

            //if (msgType.equals("FILE")) {
            Util.addDebugLog("value " + value);
            if (value != null) {
                String[] arr = value.split("\\|");
                if (arr.length >= 3) {
                    int is_free = 0;
                    if (msgTypeStr.equals("FILE")) {
                        if (arr[1].endsWith("p")) {
                            is_free = ImageStfDAO.getIsFree(arr[2]);
                        } else if (arr[1].endsWith("v") || arr[1].endsWith("a")) {
                            is_free = FileDAO.getIsFree(arr[2]);
                        }
                    }
                    UserInfo u = UserDAO.getUserInfor(from);
                    Double uFromAppId = 1.0;
                    if (u.applicationId != null){
                        uFromAppId = Double.parseDouble(u.applicationId);
                    }
                    
                    Util.addDebugLog("MessageParser is_unlock " + u.gender);
                    if (u.gender == Constant.GENDER.MALE) {
                        is_free = 1;
                    }
                    Util.addDebugLog("MessageParser deserialize before" + value);
                    Double dfrom = 1.1;
                    if (u.appVersion != null) {
                        if (uFromAppId == 1){
                            dfrom = Double.parseDouble(u.appVersion);
                        }
                        else {
                            dfrom = 2.0;
                        }
                    }
                    UserInfo uTo = UserDAO.getUserInfor(to);
                    Double uToAppId = 1.0;
                    if (uTo.applicationId != null){
                        uToAppId = Double.parseDouble(uTo.applicationId);
                    }
                        
                    Double dTo = 1.1;
                    if (uTo.appVersion != null) {
                        if (uToAppId == 1) {
                            dTo = Double.parseDouble(uTo.appVersion);
                        }
                        else {
                            dTo = 2.0;
                        }
                    }
                    Util.addDebugLog("MessageParser deserialize before" + u.deviceType + " " + dfrom);
                    Util.addDebugLog("MessageParser deserialize before" + uTo.deviceType + " " + dTo);
                    if (u.deviceType == 0 && uTo.deviceType == 0) {
                        if (msgTypeStr.equals("FILE")) {
                            if (dfrom > 1.3 && dTo > 1.3) {
                                value = value + "|" + is_free;
                            } else {
                            }

                        }
                    }
                    if (u.deviceType == 1 && uTo.deviceType == 1) {
                        if (msgTypeStr.equals("FILE")) {
                            if (dfrom > 1.2 && dTo > 1.2) {
                                value = value + "|" + is_free;
                            } else {
                            }

                        }
                    }
                    if (u.deviceType == 1 && uTo.deviceType == 0) {
                        if (msgTypeStr.equals("FILE")) {
                            if (dfrom > 1.2 && dTo > 1.3) {
                                value = value + "|" + is_free;
                            } else {
                            }

                        }
                    }
                    if (u.deviceType == 0 && uTo.deviceType == 1) {
                        if (msgTypeStr.equals("FILE")) {
                            if (dfrom > 1.3 && dTo > 1.2) {
                                value = value + "|" + is_free;
                            } else {
                            }

                        }
                    }
                    Util.addDebugLog("MessageParser deserialize after" + value);
                }
                if (msgTypeStr.equals("FILE")) {
                    
                } else {
                    value = Util.replaceWordChat(value);
                }
            }
            //}

            Util.addDebugLog("MessageParser deserialize " + value);
            Message result = new Message(id, from, to, msgType, value, originTime, serverTime, destTime);
            return result;

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
    }

    public static LinkedList<Message> parse(String msgStr) {
        if (msgStr == null) {
            return null;
        }
        LinkedList<Message> ll = new LinkedList<Message>();
        Matcher m = pattern.matcher(msgStr);
        String element;

        while (m.find()) {
            element = m.group();
            ll.add(deserialize(element));
        }
        return ll;
    }

    public static LinkedList<Message> parse(StringBuilder builder) {
        LinkedList<Message> ll = new LinkedList<Message>();

        String msgStr = builder.toString();
        Matcher m = pattern.matcher(msgStr);
        int lastCloseBracket = 0;
        String element;

        while (m.find()) {
            element = m.group();
            lastCloseBracket = m.end();

            Message msg = deserialize(element);
            if (msg != null) {
                ll.add(msg);
            }
        }

        builder.delete(0, lastCloseBracket);
        return ll;
    }
}
