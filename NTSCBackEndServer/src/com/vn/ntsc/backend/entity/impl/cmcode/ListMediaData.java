/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.cmcode;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ListMediaData implements IEntity{

    private static final String affListKey = "aff_lst";
    public List<IEntity> affList;

    private static final String mediaListKey = "media_lst";
    public List<IEntity> mediaList;

    public ListMediaData(List<IEntity> affList, List<IEntity> mediaList) {
        this.affList = affList;
        this.mediaList = mediaList;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.affList != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.affList) {
                arr.add(entity.toJsonObject());
            }
            jo.put(affListKey, arr);
        }

        if (this.mediaList != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.mediaList) {
                arr.add(entity.toJsonObject());
            }
            jo.put(mediaListKey, arr);
        }

        return jo;
    }
}
