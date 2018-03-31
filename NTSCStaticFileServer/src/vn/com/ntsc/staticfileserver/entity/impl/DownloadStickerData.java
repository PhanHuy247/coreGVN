/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class DownloadStickerData implements IEntity {

    private static final String zipFileKey = "zip_file";
    String zip_file;

    private static final String catIdKey = "cat_id";
    String cat_id;

    private static final String orderKey = "order";
    List<String> order;

    private static final String tradablePointKey = "tradable_point";
    Long tradablePoint;

    private static final String untradablePointKey = "untradable_point";
    Long untradablePoint;

    public DownloadStickerData(String file, String id, List<String> order, Long tradablePoint,Long untradablePoint ) {
        this.zip_file = file;
        this.cat_id = id;
        this.order = order;
        this.tradablePoint = tradablePoint;
        this.untradablePoint = untradablePoint;
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.zip_file != null) {
            jo.put(zipFileKey, this.zip_file);
        }
        if (this.cat_id != null) {
            jo.put(catIdKey, this.cat_id);
        }
        if (this.order != null) {
            JSONArray arr = new JSONArray();
            for (String str : this.order) {
                arr.add(str);
            }
            jo.put(orderKey, arr);
        }
        if (this.tradablePoint != null) {
            jo.put(tradablePointKey, this.tradablePoint);
        }
        if (this.untradablePoint != null) {
            jo.put(untradablePointKey, this.untradablePoint);
        }

        return jo;
    }
}
