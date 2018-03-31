/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Point implements IEntity {

    public Integer point;

    public Point(Integer point) {
        this.point = point;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.point != null) {
            jo.put(ParamKey.POINT, point);
        }
        return jo;
    }

}
