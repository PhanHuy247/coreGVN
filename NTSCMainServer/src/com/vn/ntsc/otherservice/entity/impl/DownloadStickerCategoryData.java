/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class DownloadStickerCategoryData implements IEntity{

    private static final String listKey = "list";
    public List<String> list;

    private static final String catIdKey = "cat_id";
    public String cat_id;

    private static final String pointKey = "point";
    public Long point;


    public DownloadStickerCategoryData(List<String> list, String id, Long point) {
        this.list = list;
        this.cat_id = id;
        this.point = point;
    }    
    
    public DownloadStickerCategoryData(List<String> list, String id) {
        this.list = list;
        this.cat_id = id;
    }     
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.list != null) {
            JSONArray arr = new JSONArray();
            for (String str : this.list) {
                arr.add(str);
            }
            jo.put(listKey, arr);
        }
        if (this.cat_id != null) {
            jo.put(catIdKey, this.cat_id);
        }
        if (this.point != null) {
            jo.put(pointKey, this.point);
        }
        return jo;
    }

}
