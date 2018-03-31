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
public class ListCMCodeData implements IEntity{

    private static final String cmCodeListKey = "cm_code_lst";
    public List<IEntity> cmCodeList;

    private static final String totalKey = "total_record";
    public Integer total;

    public ListCMCodeData(List<IEntity> cmCodeList, Integer total) {
        this.cmCodeList = cmCodeList;
        this.total = total;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.total != null) {
            jo.put(totalKey, this.total);
        }
        if (this.cmCodeList != null) {
            JSONArray arr = new JSONArray();
            for (IEntity entity : this.cmCodeList) {
                arr.add(entity.toJsonObject());
            }
            jo.put(cmCodeListKey, arr);
        }

        return jo;
    }

}
