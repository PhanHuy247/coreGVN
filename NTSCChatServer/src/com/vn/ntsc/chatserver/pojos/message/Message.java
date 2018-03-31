/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.message;

import java.io.Serializable;
import java.util.Date;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.chatserver.messageio.MessageParser;
import com.vn.ntsc.chatserver.messageio.StringCoder;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.user.IEntity;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.Constant.BUZZ_TYPE_VALUE;
import eazycommon.constant.ParamKey;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tuannxv00804
 */
public class Message implements Comparable<Message>, Serializable, IEntity {

    public static final String idKey = "id";
    public String id; //structure: "<from>;<to>;timestamp";

    public static final String fromKey = "from";
    public String from;

    public static final String toKey = "to";
    public String to;

    public static final String msgTypeKey = "msg_type";
    public MessageType msgType;

    public static final String valueKey = "value";
    public String value;

    public static final String originTimeKey = "origin_time";
    public Date originTime;

    public static final String serverTimeKey = "server_time";
    public long serverTime;

    public static final String destinationTimeKey = "destination_time";
    public Date destinationTime;

    public static final String readTimeKey = "read_time";
    public Long readTime;

    public static final String ipKey = "ip";
    public String ip;

    public static final String callIdKey = "call_id";
    public String callId;

    public static final String islockKey = "is_lock";
    public Long islock;

    public static final String msgIdKey = "msg_id";
    public String msgId;

    public static final String buzzIdKey = "buzz_id";
    public String buzzId;

    public static final String userNameFromKey = "user_name";
    public String userNameFrom;

    public static final String avaUrlFromKey = "ava";
    public String avaUrlFrom;

    public static final ArrayList<String> listToId = new ArrayList<>();
    
    public static final String messageContentKey = "message_content";
    public String messageContent;
    
    public static final String commentIdKey = "cmt_id";
    public String cmtId;
    
    public static final String subCmtIdKey = "sub_comment_id";
    public String subCommentId;
    
    public static final String subCommentIdKey = "sub_cmt_id";
    public String subCmtId;
    
    
    public static final String numberCommentKey = "number_comment";
    public String numberComment;
    
    public static final String filesKey = "files";
    public JSONArray files;
    
    public static final String catIdKey = "cat_id";
    public String catId;
    
    public static final String stickerKey = "sticker_url";
    public String stickerUrl;
    
    public static final String ownBuzzKey = "own_buzz_id";
    public String ownBuzzId;
    
    public static final String typeKey = "type_mds";
    public String typeMDS;
    
    public static final String apiKey = "api";
    public String api;
    
    public static final String cmtValKey = "cmt_val";
    public String cmtVal;
    
    public static final String codeKey = "code";
    public Integer code;
    
    public static final String tokenKey = "token";
    public String token;
    
    public static final String newTokenKey = "new_token";
    public String newToken;
    
    public static final String listToKey = "list_to";
    public String listTo;
    
    public static final String buzzTypeKey = "buzz_type";
    public String buzzType;
    
    public static final String listBuzzIdKey = "list_buzz_id";
    public List<String> listBuzzId;
    
    public static final String listOwnBuzzIdKey = "list_own_buzz_id";
    public List<String> listOwnBuzzId;
    
    public static final String regionKey = "region";
    public Integer region;
    
    public static final String isOnlineKey = "is_online";
    public Boolean isOnline;
    
    public static final String cmtTimeKey = "cmt_time";
    public String cmtTime;
    
    public static final String isFavKey = "is_fav";
    public Integer isFav;
    
    public static final String subCommentNumberKey = "sub_comment_number";
    public Integer subCommentNumber;
    
    public static final String longKey = "long";
    public Double lon;
    
    public static final String genderKey = "gender";
    public Integer gender;
    
    public static final String isAppKey = "is_app";
    public Integer isApp;
    
    public static final String canDeleteKey = "can_delete";
    public Integer canDelete;
    
    public static final String latKey = "lat";
    public Double lat;
    
    public static final String distKey = "dist";
    public Double dist;
    
    public static final String user_id = "user_id";
    public String userId;
    
    public static final String timeKey = "time";
    public Long time;
    
    public static final String cmtNumberKey = "comment_number";
    public Integer cmtNumber;
    
    public static final String allNumberSubCmtKey = "all_sub_comment_number";
    public Integer allNumberSubCmt;
    
    public static final String subCommentKey = "sub_comment";
    public List<String> subComment;
    
    public static final String buzzParIdKey = "buzz_parent_id";
    public String buzzParId;
    
    public static final String msgFileIdKey = "msg_file_id";
    public String msgFileId;
    
    public static final String isApproveKey = "is_approve";
    public Integer isApprove;
    
    public static final String applicationTypeKey = "application_type";
    public Integer applicationType;
    
    public static final String notificationListKey = "notification_list";
    public List<String> notificationList;
    
    public static final String listUserCmtKey = "list_user_cmt";
    public List<String> listUserComment;
    
    public static final String listBuzzDataKey = "list_buzz_data";
    public List<String> listBuzzData;
    
    public Message() {
    }

    public Message(String from, String to, MessageType msgType, String value) {
        this.originTime = new Date(Util.currentTime());
        this.serverTime = Util.currentTime();
        this.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(this.serverTime);
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        
//        this.msgId = this.id;
    }

    public Message(String id, String from, String to, MessageType msgType, String value) {
//        this.originTime = new Date(Util.currentTime());
        this.id = id;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        this.serverTime = Util.currentTime();
//        this.msgId = this.id;
    }

    public Message(Date clientTime, String from, String to, MessageType type, String value) {
        this.originTime = clientTime;
        this.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(this.originTime);
        this.from = from;
        this.to = to;
        this.msgType = type;
        this.value = value;
//        this.msgId = this.id;
    }

    public Message(String from, String to, MessageType msgType, String value, long serverTime,String token) {
        this.serverTime = serverTime;
        this.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(this.serverTime);
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        this.token = token;
//        this.msgId = this.id;
    }
    
     public Message(String from, String to, MessageType msgType,long serverTime) {
        this.serverTime = serverTime;
        this.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(this.serverTime);
        this.from = from;
        this.to = to;
        this.msgType = msgType;
    }
    
    public Message(String from, String to, MessageType msgType, String value, long serverTime,String messageContent,String msgId) {
        this.serverTime = serverTime;
        this.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(this.serverTime);
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        this.messageContent = messageContent;
        this.msgId = msgId;
//        this.msgId = this.id;
    }
    
    public Message(String from, String to, MessageType msgType, String value, long serverTime,String messageContent,String msgId,Date originTime) {
        this.serverTime = serverTime;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        this.messageContent = messageContent;
        this.msgId = msgId;
        this.originTime = originTime;
//        this.msgId = this.id;
    }

    public Message(String id, String from, String to, MessageType msgType, String value, Date originTime, long serverTime, Date destinationTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.value = value;
        this.originTime = originTime;
        this.serverTime = serverTime;
        this.destinationTime = destinationTime;
//        this.msgId = this.id;
    }
    
    

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (id != null) {
            jo.put(idKey, id);
        }
        if (from != null) {
            jo.put(fromKey, from);
        }
        if (to != null) {
            jo.put(toKey, to);
        }
        if (msgType != null) {
            jo.put(msgTypeKey, msgType.toString());
        }
        if (isApprove != null) {
            jo.put(isApproveKey, isApprove);
        }
        if (value != null) {
    //            String value = StringCoder.encode(this.value);
            jo.put(valueKey, value);
        }
        if (originTime != null) {
            jo.put(originTimeKey, DateFormat.format_yyyyMMddHHmmssSSS(originTime));
        }
        if (destinationTime != null) {
            jo.put(destinationTimeKey, destinationTime);
        }
        if (serverTime != 0L) {
            jo.put(serverTimeKey, DateFormat.format_yyyyMMddHHmmssSSS(serverTime));
        }
        if (msgId != null) {
            jo.put(msgIdKey, msgId);
        }
        if(id != null){
           jo.put(idKey, id);
        }
        if (buzzId != null) {
            jo.put(buzzIdKey, buzzId);
        }

        if (userNameFrom != null) {
            jo.put(userNameFromKey, userNameFrom);
        }
        if (avaUrlFrom != null) {
            jo.put(avaUrlFromKey, avaUrlFrom);
        }
        
        if(messageContent != null){
            jo.put(messageContentKey,messageContent);
        }
        
        if(cmtId != null){
            jo.put(commentIdKey,cmtId);
        }
         
        if(subCmtId != null){
            jo.put(subCommentIdKey,subCmtId);
        }
        if(subCommentId != null){
            jo.put(subCmtIdKey,subCommentId);
        }
        if(numberComment != null){
            jo.put(numberCommentKey,numberComment);
        }
        if(allNumberSubCmt != null){
            jo.put(allNumberSubCmtKey,allNumberSubCmt);
        }
        if(files != null){
            jo.put(filesKey,files);
        }
        if(catId != null){
            jo.put(catIdKey,catId);
        }
        if(stickerUrl != null){
            jo.put(stickerKey,stickerUrl);
        }
        if(typeMDS != null){
            jo.put(typeKey,typeMDS);
        }
        if(api != null){
            jo.put(apiKey, api);
        }
        if(cmtVal != null){
            jo.put(cmtValKey, cmtVal);
        }
        if(code != null){
            jo.put(codeKey, code);
        }
        if(token != null){
            jo.put(tokenKey, token);
        }
        if(buzzType != null){
            jo.put(buzzTypeKey, buzzType);
        }
        if(region != null){
            jo.put(regionKey, region);
        }
        if(isOnline != null){
            jo.put(isOnlineKey, isOnline);
        }
        if(cmtTime != null){
            jo.put(cmtTimeKey,cmtTime);
        }
        if(isFav != null){
            jo.put(isFavKey , isFav);
        }
        if(lon != null){
            jo.put(longKey, lon);
        }
        if(subCommentNumber != null){
            jo.put(subCommentNumberKey,subCommentNumber);
        }
        if(gender != null){
            jo.put(genderKey, gender);
        }
        if(isApp != null){
            jo.put(isAppKey, isApp);
        }
        if(canDelete != null){
            jo.put(canDeleteKey, canDelete);
        }
        if(lat != null){
            jo.put(latKey,lat);
        }
        if(dist != null){
            jo.put(distKey,dist);
        }
        if(userId != null){
            jo.put(user_id,userId);
        }
        if(cmtNumber != null){
            jo.put(cmtNumberKey,cmtNumber);
        }
        if(time != null){
            jo.put(timeKey,DateFormat.format_yyyyMMddHHmmssSSS(time));
        }
        if(subComment != null){
            JSONArray array = (JSONArray)subComment;;
            jo.put(subCommentKey, array);
        }
        if(this.buzzParId != null){
            jo.put(buzzParIdKey, this.buzzParId);
        }
        if(this.msgFileId != null){
            jo.put(msgFileIdKey, msgFileId);
        }
        if(this.newToken != null){
            jo.put(newTokenKey, newToken);
        }
        return jo;
    }

    public static Message parseFromJSONString(String jsonString) {
        Message msg = new Message();
        JSONObject jo = Util.toJSONObject(jsonString);
        String msgType = (String) jo.get(msgTypeKey);
        if (msgType != null) {
            try {
                msg.msgType = MessageType.valueOf(msgType);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
        }
        
        String userNameFrom = (String) jo.get("user_name");
        if (userNameFrom != null) {
            msg.userNameFrom = userNameFrom;
        }
        String msgFileId = (String) jo.get(msgFileIdKey);
        if (msgFileId != null) {
            msg.msgFileId = msgFileId;
        }
        String avaUrlFrom = (String) jo.get(avaUrlFromKey);
        if (avaUrlFrom != null)
            msg.avaUrlFrom = avaUrlFrom;
        
        String to = (String) jo.get(toKey);
        if (to != null) {
            msg.to = to;
        }
        
        String from = (String) jo.get(fromKey);
        if (from != null) {
            msg.from = from;
        }
        String value = (String) jo.get(valueKey);
        if (value != null && value != "") {
            msg.value = value;
        }
        
        String catId = (String) jo.get(catIdKey);
        if (catId != null) {
            msg.catId = catId;
        }
        
        String stickerUrl = (String) jo.get(stickerKey);
        if (stickerUrl != null) {
            msg.stickerUrl = stickerUrl;
        }
        String originTime = (String) jo.get(originTimeKey);
        if (originTime != null) {
            msg.originTime = DateFormat.parse_yyyyMMddHHmmssSSS(originTime);
        }
        String msgId = (String) jo.get(msgIdKey);
        if (msgId != null) {
            msg.msgId = msgId;
        }else{
            msg.msgId = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(Util.currentTime());
        }
        String buzzId = (String) jo.get(buzzIdKey);
        if (buzzId != null) {
            msg.buzzId = buzzId;
        }
        String newToken = (String) jo.get(newTokenKey);
        if (newToken != null) {
            msg.newToken = newToken;
        }
        String cmtId = (String) jo.get(commentIdKey);
        if (cmtId != null) {
            msg.cmtId = cmtId;
        }
        String ownBuzzId = (String) jo.get(ownBuzzKey);
        if(ownBuzzId != null){
            msg.ownBuzzId = ownBuzzId;
        }
        String subCmtId = (String) jo.get(subCommentIdKey);
        if (subCmtId != null) {
            msg.subCmtId = subCmtId;
        }
        String subCommentId = (String) jo.get(subCmtIdKey);
        if (subCommentId != null) {
            msg.subCommentId = subCommentId;
        }
        String cmtVal = (String) jo.get(cmtValKey);
        if (cmtVal != null) {
            msg.cmtVal = cmtVal;
        }
        String userId = (String) jo.get(user_id);
        if (userId != null) {
            msg.userId = userId;
        }
        
        msg.serverTime = Util.currentTime();
        JSONArray files = (JSONArray)jo.get(filesKey);
        if(files != null) {
            msg.files = files;
        }
        if(msg.msgType == MessageType.BUZZCMT){
            msg.api = API.ADD_COMMENT;
            if(msg.avaUrlFrom == null)
                msg.avaUrlFrom = "";
        }
        if(msg.msgType == MessageType.BUZZSUBCMT){
            msg.api = API.ADD_SUB_COMMENT;
            if(msg.cmtVal != null)
                msg.value = msg.cmtVal;
            msg.time = msg.serverTime;
        }
        if(msg.msgType == MessageType.BUZZJOIN){
            msg.api = API.CHECK_BUZZ_WEBSOCKET;
        }
         if(msg.msgType == MessageType.PONG){
            msg.api = API.EXPIRE_TOKEN;
        }
        if(msg.msgType == MessageType.BUZZLEAVE){
            msg.api = API.BUZZ_LEAVE_WEBSOCKET;
        }
        if(msg.msgType == MessageType.BUZZTAG){
            msg.api = API.UPDATE_TAG;
        }
        if(msg.msgType == MessageType.BUZZADDTAG){
            msg.api = API.ADD_TAG;
        }
        if(msg.msgType == MessageType.BUZZDELCMT){
            msg.api = API.DELETE_COMMENT;
        }
        if(msg.msgType == MessageType.BUZZDELSUBCMT){
            msg.api = API.DELETE_SUB_COMMENT;
        }
        Long code = (Long)jo.get(codeKey);
        if(code != null){
            msg.code = code.intValue();
        }
        Long cmtNumber = (Long)jo.get(cmtNumberKey);
        if(cmtNumber != null){
            msg.cmtNumber = cmtNumber.intValue();
        }
        Long isApprove = (Long)jo.get(isApproveKey);
        if(isApprove != null){
            msg.isApprove = isApprove.intValue();
        }
        Long subAllNumberCmt = (Long)jo.get(allNumberSubCmtKey);
        if(subAllNumberCmt != null){
            msg.allNumberSubCmt = subAllNumberCmt.intValue();
        }
        String token = (String)jo.get(tokenKey);
        if(token != null){
            msg.token = token;
        }
        String listTo = (String)jo.get(listToKey);
        if(listTo != null){
            msg.listTo = listTo;
        }
        List<String> listBuzzId = (List<String>)jo.get(listBuzzIdKey);
        if(listBuzzId != null){
            msg.listBuzzId = listBuzzId;
        }
        List<String> listOwnBuzzId = (List<String>)jo.get(listOwnBuzzIdKey);
        if(listOwnBuzzId != null){
            msg.listOwnBuzzId = listOwnBuzzId;
        }
        Long buzzType = (Long)jo.get(buzzTypeKey);
        if(buzzType != null){
            switch(buzzType.intValue()){
               case BUZZ_TYPE_VALUE.AUDIO_STATUS:
                   msg.buzzType = "audio";
                   break;
                case BUZZ_TYPE_VALUE.MULTI_STATUS:
                    msg.buzzType = "multi";
                    break;
                case BUZZ_TYPE_VALUE.IMAGE_STATUS:
                    msg.buzzType = "image";
                    break;
                case BUZZ_TYPE_VALUE.STREAM_STATUS:
                    msg.buzzType = "stream";
                    break;
                case BUZZ_TYPE_VALUE.TEXT_STATUS:
                    msg.buzzType = "text";
                    break;
                case BUZZ_TYPE_VALUE.VIDEO_STATUS:
                    msg.buzzType = "video";
                    break; 
                default: msg.buzzType = "buzz_share";
                    break;
                                   
            }
        }
        Long region = (Long)jo.get(regionKey);
        if(region != null){
            msg.region = region.intValue();
        }
        Boolean isOnline = (Boolean)jo.get(isOnlineKey);
        if(isOnline != null){
            msg.isOnline = isOnline;
        }
        String cmtTime = (String)jo.get(cmtTimeKey);
        if(cmtTime != null){
            msg.cmtTime = cmtTime;
        }
        String buzzParId = (String)jo.get(buzzParIdKey);
        if(buzzParId != null){
            msg.buzzParId = buzzParId;
        }
        Long isFav = (Long)jo.get(isFavKey);
        if(isFav != null){
            msg.isFav = isFav.intValue();
        }
        Double lon = (Double)jo.get(longKey);
        if(lon != null){
            msg.lon = lon;
        }
        Long subCommentNumber = (Long)jo.get(subCommentNumberKey);
        if(subCommentNumber != null){
            msg.subCommentNumber = subCommentNumber.intValue();
        }
        Long gender = (Long)jo.get(genderKey);
        if(gender != null){
            msg.gender = gender.intValue();
        }
        Long isApp = (Long)jo.get(isAppKey);
        if(isApp != null){
            msg.isApp = isApp.intValue();
        }
        Long canDelete = (Long)jo.get(canDeleteKey);
        if(canDelete != null){
            msg.canDelete = canDelete.intValue();
        }
        Double lat = (Double)jo.get(latKey);
        if(lat != null){
            msg.lat = lat;
        }
        Double disk = (Double)jo.get(distKey);
        if(disk != null){
            msg.dist = disk;
        }
        JSONArray array = (JSONArray)jo.get(subCommentKey);
        if(array != null){
            msg.subComment = array;
        }
        Long applicationType = (Long)jo.get(applicationTypeKey);
        if(applicationType != null){
            msg.applicationType = applicationType.intValue();
        }
        JSONArray notificationList = (JSONArray)jo.get(notificationListKey);
        if(notificationList != null){
            msg.notificationList = notificationList;
        }
        List<String> listUserComment = (List<String>)jo.get(listUserCmtKey);
        if(listUserComment != null){
            msg.listUserComment = listUserComment;
        }
        String msgContent = (String)jo.get(messageContentKey);
        if(msgContent != null){
            msg.messageContent = msgContent;
        }
        JSONArray arrayBuzzData = (JSONArray)jo.get(listBuzzDataKey);
        if(arrayBuzzData != null){
            msg.listBuzzData = arrayBuzzData;
        }
        msg.id = from + "&" + to + "&" + DateFormat.format_yyyyMMddHHmmssSSS(Util.currentTime());
        return msg;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", from=" + from + ", to=" + to + ", msgType=" + msgType + ", value=" + value + ", originTime=" + originTime + ", serverTime=" + serverTime + ", destinationTime=" + destinationTime + '}';
    }
    
    public String toStringTagBuzz(){
        JSONObject jsonTagBuzz = new JSONObject();
        jsonTagBuzz.put(ParamKey.BUZZ_ID, buzzId);
        jsonTagBuzz.put(ParamKey.USER_NAME, userNameFrom);
        return jsonTagBuzz.toJSONString();
    }
    public void printStruct() {
        String serialize = MessageParser.serialize(this);
        System.out.println(serialize);
    }

    /**
     * Thằng msg nào gửi trước thì được coi là lớn hơn.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Message o) {
        return o.serverTime < this.serverTime ? -1 : (o.serverTime > this.serverTime) ? 1 : 0;
    }
    
    //structure send Noti Websocket
    public String jsonSendNotiWebSocket(){
        JSONObject jsonSendNotiWebSocket = new JSONObject();
        jsonSendNotiWebSocket.put(msgTypeKey, msgType.toString());
        jsonSendNotiWebSocket.put(valueKey, Util.toJSONObject(messageContent));
        return jsonSendNotiWebSocket.toJSONString();
    }
}
