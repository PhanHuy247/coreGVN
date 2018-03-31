/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.screen;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ListScreenData implements IEntity {

    private static final String listScreenGroupKey = "list_scr_group";
    public List<IEntity> listScreenGroup;

    private static final String listScreenKey = "list_scr";
    public List<IEntity> listScreen;

    public ListScreenData(List<IEntity> listScreenGroup, List<IEntity> listScreen) {
        this.listScreenGroup = listScreenGroup;
        this.listScreen = listScreen;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.listScreenGroup != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.listScreenGroup) {
                arr.add(entity.toJsonObject());
            }
            jo.put(listScreenGroupKey, arr);
        }

        if (this.listScreen != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.listScreen) {
                arr.add(entity.toJsonObject());
            }
            jo.put(listScreenKey, arr);
        }

        return jo;
    }
}
