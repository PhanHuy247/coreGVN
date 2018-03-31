/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.sticker;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Sticker implements IEntity{
    
    private static final String idKey = "id";
    public String id;

    private static final String codeKey = "code";
    public Long code;    
    
    private static final String categoryIdKey = "cat_id";
    public String categoryId;


    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);        
        if(this.code != null)
            jo.put(codeKey, this.code);
        if(this.categoryId != null)
            jo.put(categoryIdKey, this.categoryId);
        return jo;
    }

}
