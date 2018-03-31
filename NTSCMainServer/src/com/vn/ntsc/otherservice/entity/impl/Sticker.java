/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Sticker implements IEntity{
    private static final String codeKey = "code";
    public Long code;

    private static final String categoryIdKey = "cat_id";
    public String categoryId;

    private static final String stickerPriceKey = "stk_pri";
    public Double stickerPrice;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.code != null)
            jo.put(codeKey, this.code);
        if(this.categoryId != null)
            jo.put(categoryIdKey, this.categoryId);
        if(this.stickerPrice != null)
            jo.put(stickerPriceKey, this.stickerPrice);
        
        return jo;
    }

}
