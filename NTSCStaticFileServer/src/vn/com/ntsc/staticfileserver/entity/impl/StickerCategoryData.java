/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class StickerCategoryData implements IEntity{
    
    private static final String idKey = "cat_id";
    public String id;
    
    private static final String categoryNumberKey = "stk_num";
    public Integer categoryNumber;
    
    private static final String enCatNameKey = "en_name";
    public String enCatName;

    private static final String jpCatNameKey = "jp_name";
    public String jpCatName;
    
    private static final String enCatDesKey = "en_des";
    public String enCatDes;

    private static final String jpCatDesKey = "jp_des";
    public String jpCatDes;
    
    private static final String categoryUrlKey = "cat_url";
    public String categoryUrl;
    
    private static final String listStickerKey = "lst_stk";
    public List<StickerData> listSticker;
    
    public static final String versionKey = "version";
    public Integer version;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.jpCatName != null)
            jo.put(jpCatNameKey, jpCatName);
        if(this.enCatDes != null)
            jo.put(enCatDesKey, this.enCatDes);
        if(this.jpCatDes != null)
            jo.put(jpCatDesKey, jpCatDes); 
        if(this.categoryNumber != null)
            jo.put(categoryNumberKey, categoryNumber);
        if(this.categoryUrl != null)
            jo.put(categoryUrlKey, categoryUrl);
        if (listSticker != null){
            JSONArray arr = new JSONArray();
            for (StickerData item : listSticker){
                arr.add(item.toJsonObject());
            }
            jo.put(listStickerKey, arr);
        }
        if(this.version != null)
            jo.put(versionKey, version);
        return jo;
    }
    
}
