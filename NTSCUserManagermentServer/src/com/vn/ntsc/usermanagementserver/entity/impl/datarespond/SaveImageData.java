/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;

/**
 *
 * @author RuAc0n
 */
public class SaveImageData implements IEntity {

    public Point point;

    private static final String saveImagePointKey = "save_image_point";
    public Integer saveImagePoint;

    public SaveImageData(Integer point, Integer saveImagePoint) {
        this.point = new Point(point);
        this.saveImagePoint = saveImagePoint;
    }

    public SaveImageData(){
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(point != null){
            jo = point.toJsonObject();
        }
        if(saveImagePoint != null){
            jo.put(saveImagePointKey, saveImagePoint);
        }
        return jo;
    }
}
