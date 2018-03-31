/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.chat;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.TwoObjectLog;
import com.vn.ntsc.backend.server.respond.impl.csv.HeaderCreator;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author RuAc0n
 */
public class Message extends TwoObjectLog implements IEntity {

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();

    private static final String REQUEST_ID = "request_id";
    @TypeSwitch(header = Headers.user_id)
    public String reqId;

    @TypeSwitch(header = Headers.user_name)
    public String reqUserName;

    @TypeSwitch(value = ParamKey.USER_TYPE, header = Headers.user_type)
    public Integer reqUserType;

    @TypeSwitch(header = Headers.email)
    public String reqEmail;

    @TypeSwitch(header = Headers.group)
    public String reqGroup;

    @TypeSwitch(header = Headers.cm_code)
    public String reqCmCode;

    @TypeSwitch(header = Headers.ip)
    public String reqIp;

    private static final String userIdKey = "user_id";
    @TypeSwitch(header = Headers.chat_user_id)
    public String userId;

    private static final String userNameKey = "user_name";
    @TypeSwitch(header = Headers.chat_user_name)
    public String userName;

    private static final String userTypeKey = "user_type";
    @TypeSwitch(value = ParamKey.USER_TYPE, header = Headers.chat_user_type)
    public Integer userType;

    private static final String emailKey = "email";
    @TypeSwitch(header = Headers.chat_user_email)
    public String email;

    @TypeSwitch(header = Headers.chat_user_group)
    public String group;

    private static final String cmCodeKey = "cm_code";
    @TypeSwitch(header = Headers.chat_user_cm_code)
    public String cmCode;

    private static final String ipKey = "ip";
    @TypeSwitch(header = Headers.chat_user_ip)
    public String ip;

    @TypeSwitch(header = Headers.chat_content)
    public String message;

    private static final String timeKey = "time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.chat_time)
    public String time;

    private static final String isOwnKey = "is_own";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public Integer isOwn;

    private static final String contentKey = "content";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String content;

    private static final String urlKey = "url";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String url;

    private static final String messageTypeKey = "mess_type";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public String messageType;
    //thanhdd add
    private static final String isAdminKey = "is_Admin";
    @TypeSwitch(value = ParamKey.ISADMIN, header = ParamKey.ISADMIN)
    public Integer isAdmin;

    private static final String partnerResponsedKey = "partner_respond";
    @TypeSwitch(value = ParamKey.PARTNER_RESPONSED, header = Headers.partner_respond)
    public Integer partnerResponsed;

    private static final String isReadKey = "is_Read";
    @TypeSwitch(value = ParamKey.IS_READ, header = ParamKey.IS_READ)
    public Integer isRead;
    
    private static final String isLockKey = "islock";
    @TypeSwitch(value = ParamKey.IS_LOCK, header = ParamKey.IS_LOCK)
    public Integer isLock;

    private static final String messageReadTimeKey = "rt";
    @TypeSwitch(value = ParamKey.RT, header = ParamKey.RT)
    public String messageReadTime;

    public static final String CALLER_VOICE_CALL_RESPONSE_MESSAGE = "caller_voice_call_response_message";
    public static final String RECEIVER_VOICE_CALL_RESPONSE_MESSAGE = "receiver_voice_call_response_message";
    public static final String CALLER_VOICE_CALL_BUSY_MESSAGE = "caller_voice_call_busy_message";
    public static final String RECEIVER_VOICE_CALL_BUSY_MESSAGE = "receiver_voice_call_busy_message";
    public static final String CALLER_VOICE_CALL_NO_ANSWER_MESSAGE = "caller_voice_call_no_answer_message";
    public static final String RECEIVER_VOICE_CALL_NO_ANSWER_MESSAGE = "receiver_voice_call_no_answer_message";
    public static final String CALLER_VOICE_CALL_CANCEL_MESSAGE = "caller_voice_call_cancel_message";
//    public static final String RECEIVER_VOICE_CALL_CANCEL_MESSAGE = "receiver_voice_call_cancel_message";

    public static final String CALLER_VIDEO_CALL_RESPONSE_MESSAGE = "caller_video_call_response_message";
    public static final String RECEIVER_VIDEO_CALL_RESPONSE_MESSAGE = "receiver_video_call_response_message";
    public static final String CALLER_VIDEO_CALL_BUSY_MESSAGE = "caller_video_call_busy_message";
    public static final String RECEIVER_VIDEO_CALL_BUSY_MESSAGE = "receiver_video_call_busy_message";
    public static final String CALLER_VIDEO_CALL_NO_ANSWER_MESSAGE = "caller_video_call_no_answer_message";
    public static final String RECEIVER_VIDEO_CALL_NO_ANSWER_MESSAGE = "receiver_video_call_no_answer_message";
    public static final String CALLER_VIDEO_CALL_CANCEL_MESSAGE = "caller_video_call_cancel_message";
//    public static final String RECEIVER_VIDEO_CALL_CANCEL_MESSAGE = "receiver_video_call_cancel_message";

    private static final String VIDEO_RESPONSE = "6";
    private static final String VIDEO_NO_ANSWER = "7";
    private static final String VIDEO_BUSY = "8";
//    private static final String VIDEO_CANCEL = "10";
    private static final String VOICE_RESPONSE = "2";
    private static final String VOICE_NO_ANSWER = "3";
    private static final String VOICE_BUSY = "4";
//    private static final String VOICE_CANCEL = "9";    

    static {
        initHeader();
        initType();
    }

    private static void initType() {

//        jsonEnglishType = new JSONObject();
//        jsonJapaneseType = new JSONObject();
        JSONObject value = new JSONObject();
        //user type
        value.putAll(TypeValue.en_user_type);
        jsonEnglishType.put(ParamKey.USER_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_user_type);
        jsonJapaneseType.put(ParamKey.USER_TYPE, value);

        //chat content
        value = new JSONObject();
        value.putAll(TypeValue.en_chat_content);
        jsonEnglishType.put("chat_content", value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_chat_content);
        jsonJapaneseType.put("chat_content", value);

    }

    private static void initHeader() {

//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
        List<String> keys = new ArrayList<String>();
        keys.add(Headers.number);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.ip);

        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.ip);
        keys.add(Headers.chat_content);
        keys.add(Headers.chat_time);

//        headers = new ArrayList<>();
        headers.add(Headers.number);
        headers.add(Headers.user_id);
        headers.add(Headers.user_name);
        headers.add(Headers.user_type);
        headers.add(Headers.email);
        headers.add(Headers.group);
        headers.add(Headers.cm_code);
        headers.add(Headers.ip);
        headers.add(Headers.chat_user_id);
        headers.add(Headers.chat_user_name);
        headers.add(Headers.chat_user_type);
        headers.add(Headers.chat_user_email);
        headers.add(Headers.chat_user_group);
        headers.add(Headers.chat_user_cm_code);
        headers.add(Headers.chat_user_ip);
        headers.add(Headers.chat_content);
        headers.add(Headers.chat_time);
        headers.add(Headers.is_admin);
        headers.add(Headers.partner_respond);

        headers.add(Headers.is_read);
        headers.add(Headers.rt);

        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);

    }

    public static void set(List<IEntity> list, Integer type) {
        JSONObject jsonValue = (JSONObject) new Message().getJsonType(type).get("chat_content");
        for (IEntity log : list) {
            setChatContent((Message) log, jsonValue);
            if (((Message) log).isOwn == 1) {
                ((Message) log).reqIp = ((Message) log).ip;
                ((Message) log).ip = null;
            }

        }
    }

    private static void setChatContent(Message log, JSONObject json) {
        StringBuilder value = new StringBuilder();
        String[] arr = null;
        String str = null;
        switch (log.messageType) {
            case "PP":
                value.append(log.content);
                break;
            case "FILE":
                arr = log.content.split("\\|");
                if (arr.length >= 3) {
                    value.append((String) json.get(arr[1]));
                    if (arr[1].endsWith("p")) {
                        value.append(" ");
                        value.append(arr[2]);
                    }
                } else {
                    value.append((String) json.get("e"));
                }
                break;
            case "WINK":
                value.append((String) json.get(log.messageType));
                value.append(" ");
                value.append(log.content);
                break;
            case "GIFT":
                arr = log.content.split("\\|");
                value.append((String) json.get(log.messageType));
                value.append(" ");
                value.append(arr[0]);
                break;
            case "STK":
                arr = log.content.split("_");
                value.append((String) json.get(log.messageType));
                value.append(" ");
                value.append(arr[1]);
                break;
            case "LCT":
                arr = log.content.split("\\|");
                value.append((String) json.get(log.messageType));
                value.append(" ");
                value.append(arr[2]);
                break;
            case "EVIDEO":
//                arr = log.content.split("\\|");
//                value.append((String) json.get(arr[0]));
                value.append(getCallContent(log, json));
                break;
            case "EVOICE":
//                arr = log.content.split("\\|");
//                value.append((String) json.get(arr[0]));
                value.append(getCallContent(log, json));
                break;
            default:
                str = (String) json.get(log.messageType);
                if (str != null) {
                    value.append(str);
                }
        }
        log.message = value.toString();
    }

    public Message() {
//        this.url = "http://202.221.140.192:81/201503/26/c46f40b6-1f84-43b2-905e-8aab5a075b44.mp4";
    }

    private static String getCallContent(Message message, JSONObject json) {
        StringBuilder result = new StringBuilder();
        String[] arr = message.content.split("\\|");
        String type = arr[0];
        String time = null;
        String key = null;
        switch (type) {
            case VIDEO_RESPONSE:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VIDEO_CALL_RESPONSE_MESSAGE;
                } else {
                    key = RECEIVER_VIDEO_CALL_RESPONSE_MESSAGE;
                }
                time = getCallDuration(arr[2]);
                break;
            case VIDEO_NO_ANSWER:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VIDEO_CALL_NO_ANSWER_MESSAGE;
                } else {
                    key = RECEIVER_VIDEO_CALL_NO_ANSWER_MESSAGE;
                }
                break;
            case VIDEO_BUSY:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VIDEO_CALL_BUSY_MESSAGE;
                } else {
                    key = RECEIVER_VIDEO_CALL_BUSY_MESSAGE;
                }
                break;
//            case VIDEO_CANCEL:
//                if(message.isOwn == Constant.YES){
//                    key = CALLER_VIDEO_CALL_CANCEL_MESSAGE;
//                }else{
//                    key = RECEIVER_VIDEO_CALL_CANCEL_MESSAGE;
//                }
//                break;
            case VOICE_RESPONSE:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VOICE_CALL_RESPONSE_MESSAGE;
                } else {
                    key = RECEIVER_VOICE_CALL_RESPONSE_MESSAGE;
                }
                time = getCallDuration(arr[2]);
                break;
            case VOICE_NO_ANSWER:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VOICE_CALL_NO_ANSWER_MESSAGE;
                } else {
                    key = RECEIVER_VOICE_CALL_NO_ANSWER_MESSAGE;
                }
                break;
            case VOICE_BUSY:
                if (message.isOwn == Constant.FLAG.ON) {
                    key = CALLER_VOICE_CALL_BUSY_MESSAGE;
                } else {
                    key = RECEIVER_VOICE_CALL_BUSY_MESSAGE;
                }
                break;
//            case VOICE_CANCEL:
//                if(message.isOwn == Constant.YES){
//                    key = CALLER_VOICE_CALL_CANCEL_MESSAGE;
//                }else{
//                    key = RECEIVER_VOICE_CALL_CANCEL_MESSAGE;
//                }
//                break;
        }
        result.append((String) json.get(key));
        if (time != null) {
            result.append(" ");
            result.append(time);
        }
        return result.toString();
    }

    private static String getCallDuration(String rawData) {
        try {
            StringBuilder result = new StringBuilder();
            int duration = Integer.parseInt(rawData);
            String minute = format2DNumber(duration / 60);
            String second = format2DNumber(duration % 60);
            result.append(minute);
            result.append(":");
            result.append(second);
            return result.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return "00:00";
        }
    }

    private static final String Zero = "0";

    private static String format2DNumber(int n) {
        return n > 9 ? String.valueOf(n) : (Zero + n);
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.reqId != null) {
            jo.put(REQUEST_ID, this.reqId);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.email != null) {
            jo.put(emailKey, this.email);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (this.ip != null) {
            jo.put(ipKey, ip);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, this.userType);
        }
        if (this.isOwn != null) {
            jo.put(isOwnKey, this.isOwn);
        }
        if (this.content != null) {
            jo.put(contentKey, this.content);
        }
        if (this.messageType != null) {
            jo.put(messageTypeKey, this.messageType);
        }
        if (this.url != null) {
            jo.put(urlKey, this.url);
        }
        if (this.isAdmin != null) {
            jo.put(isAdminKey, this.isAdmin);
        }
        if (this.partnerResponsed != null) {
            jo.put(partnerResponsedKey, this.partnerResponsed);
        }

        if (this.isRead != null) {
            jo.put(isReadKey, this.isRead);
        }
        if (this.messageReadTime != null) {
            jo.put(messageReadTimeKey, this.messageReadTime);
        }
        //Thanhdd add 02/03/2017
        if (this.isLock != null) {
            jo.put(isLockKey, this.isLock);
        }
        
        return jo;
    }

    @Override
    public String getReqId() {
        return this.reqId;
    }

    @Override
    public void setReqeusetInfor(String reqUserName, Integer reqUserType, String reqEmail, String reqCmCode) {
        this.reqUserName = reqUserName;
        this.reqUserType = reqUserType;
        this.reqEmail = reqEmail;
        this.reqCmCode = reqCmCode;
    }

    @Override
    public String getPartnerId() {
        return this.userId;
    }

    @Override
    public void setPartnerInfor(String partnerUserName, Integer partnerUserType, String partnerEmail, String partnerCmCode) {
        this.userName = partnerUserName;
        this.userType = partnerUserType;
        this.email = partnerEmail;
        this.cmCode = partnerCmCode;
    }

    @Override
    public List<String> getHeaders(Integer type) {
        if (type != null && type == 1) {
            return englishHeader;
        } else {
            return japaneseHeader;
        }
    }

    // GET LIST KEY OF SUBCLASS
    @Override
    public List<String> getKeys() {
        return headers;
    }

    // get user type follow english or japanese
    @Override
    public JSONObject getJsonType(Integer type) {
        if (type != null && type == 1) {
            return jsonEnglishType;
        } else {
            return jsonJapaneseType;
        }
    }

}
