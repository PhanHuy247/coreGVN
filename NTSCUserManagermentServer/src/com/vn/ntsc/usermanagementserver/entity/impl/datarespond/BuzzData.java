/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class BuzzData implements IEntity {

    private static final String buzzOwnerNameKey = "buzz_owner_name";
    public String buzzOwnerName;

    private static final String isNotiKey = "is_noti";
    public Integer isNoti;
    
    public Long point;
    
    public BuzzData( String buzzOwnerName, Integer isNoti, Long point) {
//        this.blck_lst = blck_lst;
        this.buzzOwnerName = buzzOwnerName;
        this.isNoti = isNoti;
        this.point = point;
    }

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();

//        if (this.blck_lst != null) {
//            JSONArray arr = new JSONArray();
//            for (String blck_lst1 : this.blck_lst) {
//                arr.add(blck_lst1);
//            }
//            jo.put(blckListKey, arr);
//        }
        if (buzzOwnerName != null) {
            jo.put(buzzOwnerNameKey, buzzOwnerName);
        }
        if(isNoti != null )
            jo.put(isNotiKey, isNoti);
        
        jo.put(ParamKey.POINT, point);
        return jo;
    }

}
