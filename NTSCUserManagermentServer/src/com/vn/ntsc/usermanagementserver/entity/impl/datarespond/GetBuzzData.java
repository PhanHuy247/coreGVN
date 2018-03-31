/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.Collection;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetBuzzData implements IEntity {

    private static final String llUsersKey = "llUsers";
    public List<String> llUsers;

    private static final String blockListKey = "block_lst";
    public List<String> block_lst;
    
    private static final String friendListKey = "friend_list";
    public List<String> friend_lst;

    private static final String genderKey = "gender";
    public Integer gender;    
    
    public GetBuzzData(List<String> llUsers, List<String> block_lst, Integer gender) {
        this.llUsers = llUsers;
        this.block_lst = block_lst;
        this.gender = gender;
    }
    
    public GetBuzzData(List<String> llUsers, List<String> block_lst) {
        this.llUsers = llUsers;
        this.block_lst = block_lst;
    }
    
    public GetBuzzData(List<String> llUsers, List<String> block_lst, List<String> friend_lst) {
        this.llUsers = llUsers;
        this.block_lst = block_lst;
        this.friend_lst = friend_lst;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.llUsers != null) {
            JSONArray arr = new JSONArray();
            for (String llUser : this.llUsers) {
                arr.add(llUser);
            }
            jo.put(llUsersKey, arr);
        }
        if (this.block_lst != null) {
            JSONArray arr = new JSONArray();
            for (String block_lst1 : this.block_lst) {
                arr.add(block_lst1);
            }
            jo.put(blockListKey, arr);
        }
        if(this.friend_lst != null){
            JSONArray arr = new JSONArray();
            for (String item : this.friend_lst) {
                arr.add(item);
            }
            jo.put(friendListKey, arr);
        }
        if(this.gender != null){
            jo.put(genderKey, this.gender);
        }
        return jo;
    }
}
