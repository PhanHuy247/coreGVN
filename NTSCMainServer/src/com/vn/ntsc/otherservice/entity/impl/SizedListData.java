/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;


/**
 *
 * @author RuAc0n
 * @param <T>
 */
public class SizedListData<T> implements IEntity {

    private static final String totalKey = "total";
    public Integer total;

    private static final String llKey = "list";
    public List<T> ll;

    public SizedListData() {
        this.total = 0;
        this.ll = new ArrayList<>();
    }
        
    public SizedListData(Integer total, List<T> ll) {
        this.total = total;
        this.ll = ll;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.total != null) {
            jo.put(totalKey, this.total);
        }
        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (T entity : this.ll) {
                arr.add(((IEntity) entity).toJsonObject());
            }
            jo.put(llKey, arr);
        }
        return jo;
    }
}
