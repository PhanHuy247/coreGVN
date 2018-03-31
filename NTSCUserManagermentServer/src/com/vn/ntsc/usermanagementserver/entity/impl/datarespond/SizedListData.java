/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class SizedListData implements IEntity {

    private static final String totalKey = "total";
    public Integer total;

    private static final String llKey = "list";
    public List<String> ll;
    
    public List<IEntity> listEntity;

    public SizedListData() {
        this.total = 0;
        this.ll = new ArrayList<String>();
    }
        
    public SizedListData(Integer total, List<String> ll) {
        this.total = total;
        this.ll = ll;
    }
    public SizedListData(List<IEntity> listEntity, Integer total) {
        this.total = total;
        this.listEntity = listEntity;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.total != null) {
            jo.put(totalKey, this.total);
        }
        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (String string : this.ll) {
                arr.add(string);
            }
            jo.put(llKey, arr);
        }
        if (this.listEntity != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.listEntity) {
                arr.add(entity.toJsonObject());
            }
            jo.put(llKey, arr);
        }
        return jo;
    }
}
