/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class AddBuzzData implements IEntity{

    private static final String isNotiKey = "is_noti";
    public Integer isNoti;
    
    private static final String isNotiCommentKey = "is_noti_comment";
    public Integer isNotiComment;

    private static final String lisFavouritedKey = "fvt_lst";
    public List<String> listFavourited;    
    
    private static final String buzzOwnerNameKey = "noti_buzz_owner_name";
    public String buzzOwnerName;
    
    public AddBuzzData(Integer isNoti, List<String> listFavourited, String buzzOwnerName, Integer isNotiComment) {
        this.isNoti = isNoti;
        this.listFavourited = listFavourited;
        this.buzzOwnerName = buzzOwnerName;
        this.isNotiComment = isNotiComment;
    }

    public AddBuzzData(Integer isNoti) {
        this.isNoti = isNoti;
    }    
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.isNoti != null) {
            jo.put(isNotiKey, this.isNoti);
        }
        if (this.isNotiComment != null) {
            jo.put(isNotiCommentKey, this.isNotiComment);
        }
        if(this.listFavourited != null){
            jo.put(lisFavouritedKey, this.listFavourited);
        }
        if(this.buzzOwnerName != null){
            jo.put(buzzOwnerNameKey, this.buzzOwnerName);
        }

        return jo;
    }
}
