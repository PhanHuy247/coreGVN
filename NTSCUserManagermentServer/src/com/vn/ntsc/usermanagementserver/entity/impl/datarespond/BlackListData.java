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
public class BlackListData implements IEntity {

    private static final String blockListKey = "block_lst";
    public List<String> block_lst;

    private static final String deactiveListKey = "deact_lst";
    public Collection<String> deactiveList;

    private static final String genderKey = "gender";
    public Integer gender;

    public BlackListData(List<String> block_lst, Collection<String> deactiveList, Integer gender) {
        this.block_lst = block_lst;
        this.deactiveList = deactiveList;
        this.gender = gender;
    }

    public BlackListData(List<String> block_lst) {
        this.block_lst = block_lst;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.block_lst != null) {
            JSONArray arr = new JSONArray();
            for (String block_lst1 : this.block_lst) {
                arr.add(block_lst1);
            }
            jo.put(blockListKey, arr);
        }
        if (this.deactiveList != null) {
            JSONArray arr = new JSONArray();
            for (String deactiveList1 : this.deactiveList) {
                arr.add(deactiveList1);
            }
            jo.put(deactiveListKey, arr);
        }

        if (this.gender != null) {
            jo.put(genderKey, this.gender);
        }
        return jo;
    }
}
