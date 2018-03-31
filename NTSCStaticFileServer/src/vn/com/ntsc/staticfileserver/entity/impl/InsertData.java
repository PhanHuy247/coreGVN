/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class InsertData implements IEntity{
    
    private static final String idKey = "id";
    public String id;
    
    private static final String warningFlagKey = "warning_flag";
    public Integer warningFlag;

    public InsertData(String id) {
        this.id = id;
    }
    
    public InsertData(String id, Integer flag) {
        this.id = id;
        this.warningFlag = flag;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.id != null) {
            jo.put(idKey, this.id);
        }
        if (this.warningFlag != null){
            jo.put(warningFlagKey, this.warningFlag);
        }

        return jo;
    }
    
}
