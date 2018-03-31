/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import com.vn.ntsc.buzzserver.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ViewNumberData implements IEntity{

    private static final String viewNumberKey = "view_number";
    public Integer viewNumber;

    public ViewNumberData(Integer viewNumber) {
        this.viewNumber = viewNumber;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (viewNumber != null) {
            jo.put(viewNumberKey, viewNumber);
        }

        return jo;
    }
    
}
