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
 * @author RuAc0n
 */
public class GetConnectionNumberData implements IEntity {

    private static final String favNumKey = "fav_num";
    public Long favNum;

    private static final String fvtNumKey = "fvt_num";
    public Long fvtNum;

    private static final String myFootprintNumberKey = "my_footprint_num";
    public Integer myFootprintNumber;    
    
    public GetConnectionNumberData(Long favNum, Long fvtNum, Integer myFootprintNumber) {
        this.favNum = favNum;
        this.fvtNum = fvtNum;
        this.myFootprintNumber = myFootprintNumber;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.favNum != null) {
            jo.put(favNumKey, this.favNum);
        }
        if (this.fvtNum != null) {
            jo.put(fvtNumKey, this.fvtNum);
        }
        if (this.myFootprintNumber != null) {
            jo.put(myFootprintNumberKey, this.myFootprintNumber);
        }
        return jo;
    }
}
