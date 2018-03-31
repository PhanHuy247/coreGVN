/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AddCommentData implements IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzz_id;

    private static final String timeKey = "time";
    public Long time;

    private static final String buzzOwnerKey = "buzz_owner_id";
    public String buzz_owner_id;

    private static final String cmtIdKey = "cmt_id";
    public String cmt_id;
    
    private static final String isApprovedKey = "is_app";
    public Integer isApp;
    
    private static final String buzzTypeKey = "buzz_type";
    public Long buzzType;
    
    private static final String buzzStreamStatusKey = "stream_status";
    public String streamStatus;

    public AddCommentData(String buzzId, String cmtId, Long time, String buzzOwner, int isApp,Long buzzType) {
        this.buzz_id = buzzId;
        this.cmt_id = cmtId;
        this.time = time;
        this.buzz_owner_id = buzzOwner;
        this.isApp = isApp;
        this.buzzType = buzzType;
    }

    public AddCommentData(String buzz_id, String cmt_id, Long time, String buzzOwner, Integer isApp, Long buzzType, String streamStatus) {
        this.buzz_id = buzz_id;
        this.time = time;
        this.cmt_id = cmt_id;
        this.isApp = isApp;
        this.buzz_owner_id = buzzOwner;
        this.buzzType = buzzType;
        this.streamStatus = streamStatus;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzz_id != null) {
            jo.put(buzzIdKey, buzz_id);
        }
        if (cmt_id != null) {
            jo.put(cmtIdKey, cmt_id);
        }
        if (time != null) {
            jo.put(timeKey, time);
        }
        if (buzz_owner_id != null) {
            jo.put(buzzOwnerKey, buzz_owner_id);
        }
        if (isApp != null) {
            jo.put(isApprovedKey, isApp);
        }
        if (buzzType != null) {
            jo.put(buzzTypeKey, buzzType);
        }
        if (streamStatus != null) {
            jo.put(buzzStreamStatusKey, streamStatus);
        }
        return jo;
    }

}
