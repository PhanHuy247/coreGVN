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
public class GiftCategory implements IEntity{
    private static final String idKey = "cat_id";
    public String id;

    private static final String categoryNameKey = "cat_name";
    public String categoryName;

    private static final String enCatNameKey = "en_name";
    public String enCatName;


    private static final String jpCatNameKey = "jp_name";
    public String jpCatName;

    private static final String categoryNumKey = "cat_num";
    public Integer categoryNumber;


    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.categoryName != null)
            jo.put(categoryNameKey, this.categoryName);
        if(this.categoryNumber != null)
            jo.put(categoryNumKey, this.categoryNumber);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.jpCatName != null)
            jo.put(jpCatNameKey, this.jpCatName);
        
        return jo;
    }
}
