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
public class R_GetUsersPresentation {
    public LinkedList<User> llUsers;
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        JSONArray jarr = new JSONArray();
        Iterator<User> it = llUsers.iterator();
        while(it.hasNext()){
            User user = it.next();
            if(user == null){
                jarr.add(null);
            }else{
                jarr.add(user.toJsonObject());
            }
        }
        
        jo.put(ParamKey.LIST_USER, jarr);
        
        return jo;
        
    }
}
