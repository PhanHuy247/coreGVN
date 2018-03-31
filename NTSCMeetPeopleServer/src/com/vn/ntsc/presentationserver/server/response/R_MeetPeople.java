/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.response;

import java.util.Iterator;
import java.util.LinkedList;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.User;

/**
 *
 * @author tuannxv00804
 */
public class R_MeetPeople {
    public int code;
    public LinkedList<User> llUser;

    public R_MeetPeople() {
        code = 0;
        llUser = new LinkedList<>();
    }
    
    public String toJson(){
        JSONObject jo = new JSONObject();
        jo.put(ParamKey.ERROR_CODE, code);
        JSONArray jarr = new JSONArray();
        Iterator<User> it = llUser.iterator();
        while(it.hasNext()){
            jarr.add(it.next().toJsonObject());
        }
        jo.put(ParamKey.DATA, jarr);
        return jo.toJSONString();
    }
    
}
