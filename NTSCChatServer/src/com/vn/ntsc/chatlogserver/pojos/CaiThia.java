/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatlogserver.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author tuannxv00804
 */
public class CaiThia implements Comparable<CaiThia>, Serializable {

    public String msgID;
    public String friendID;
    public int unreadNum;
    public String lastMsg;
    public Date sentTime;
    public boolean isOwn;
    public String msgType;
    
    public String toJson(boolean isFullProperties) {
        return toJsonObject(isFullProperties).toJSONString();
    }
    
    public JSONObject toJsonObject(boolean isFullProperties) {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.FRDID, this.friendID);
        obj.put(ParamKey.UNREAD_NUMBER, this.unreadNum);
        if(isFullProperties){
            obj.put("is_own", this.isOwn);
            obj.put("last_msg", this.lastMsg);
            obj.put("sent_time", DateFormat.format_yyyyMMddHHmmssSSS(this.sentTime));
            obj.put(ParamKey.MSG_TYPE, this.msgType);
            obj.put(ParamKey.MESSAGE_ID, this.msgID);
        }
        return obj;
    }

    public static String toJson(List<CaiThia> list, boolean isFullProperties) {
        JSONArray arr = new JSONArray();
        if(list != null && !list.isEmpty()){
            Iterator<CaiThia> iter = list.iterator();
            while (iter.hasNext()) {
                CaiThia thia = iter.next();
                if (thia != null) {
                    arr.add(thia.toJsonObject(isFullProperties));
                }
            }
        }
        return arr.toJSONString();
    }

    /**
     * Cách implements này chỉ phục vụ cho Arrays.sort(), với mục đích sort(asc)
     * mà vẫn đưa về kết quả (desc) có nghĩa là CaiThia mới hơn sẽ nằm trước cái
     * thìa cũ.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(CaiThia o) {
        return o.sentTime.compareTo(this.sentTime);
    }

}
