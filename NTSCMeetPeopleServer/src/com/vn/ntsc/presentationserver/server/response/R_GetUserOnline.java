/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import java.util.ArrayList;
import java.util.Iterator;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class R_GetUserOnline {
    public int total;
    public int female;
    public int male;
    public int videoCall;
    public int voiceCall;
    public ArrayList<String> llUser;

    public R_GetUserOnline() {
        this.llUser = new ArrayList<>();
    }

    public R_GetUserOnline(int total, ArrayList<String> llUser) {
        this.total = total;
        this.llUser = llUser;
    }
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.TOTAL, total);
        jo.put("female", female);
        jo.put("male", male);
        jo.put("video_call", videoCall);
        jo.put("voice_call", voiceCall);
        JSONArray jarr = new JSONArray();
        Iterator<String> it = llUser.iterator();
        while(it.hasNext()){
            jarr.add(it.next());
        }
        jo.put(ParamKey.FRIEND_LIST, jarr);
        return jo.toJSONString();
    }
}
