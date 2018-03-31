/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author duyetpt
 */
public class LogFreePointData implements IEntity {

    public static final String USER_ID_KEY = "userId";
    public String userId;
    public static final String POINT_KEY = "point";
    public Integer point;
    public static final String TYPE_ID_KEY = "type_id";
    public Integer typeId;
    public static final String DATE_KEY = "date";
    public String time;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (userId != null) {
            jo.put(USER_ID_KEY, userId);
        }
        if (point != null) {
            jo.put(POINT_KEY, point);
        }
        if (typeId != null) {
            jo.put(TYPE_ID_KEY, typeId);
        }
        if (time != null) {
            jo.put(DATE_KEY, time);
        }
        return jo;
    }

}
