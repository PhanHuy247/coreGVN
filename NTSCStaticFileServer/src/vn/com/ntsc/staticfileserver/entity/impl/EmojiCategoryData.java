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
public class EmojiCategoryData implements IEntity{
    
    private static final String idKey = "cat_id";
    public String id;
    
    private static final String enCatNameKey = "en_name";
    public String enCatName;

    private static final String vnCatNameKey = "vn_name";
    public String vnCatName;
    
    private static final String enCatDesKey = "en_des";
    public String enCatDes;

    private static final String vnCatDesKey = "vn_des";
    public String vnCatDes;
    
    private static final String categoryUrlKey = "cat_url";
    public String categoryUrl;
    
    private static final String fileIdKey = "file_id";
    public String fileId;
    
    private static final String emojiNumKey = "emoji_num";
    public Integer emojiNum;
    
    private static final String listEmojiKey = "lst_emoji";
    public List<EmojiData> listEmoji;
    
    private static final String versionKey = "version";
    public Integer version;
    
    private static final String flagKey = "flag";
    public Integer flag;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.vnCatName != null)
            jo.put(vnCatNameKey, vnCatName);
        if(this.enCatDes != null)
            jo.put(enCatDesKey, this.enCatDes);
        if(this.vnCatDes != null)
            jo.put(vnCatDesKey, vnCatDes); 
        if(this.emojiNum != null)
            jo.put(emojiNumKey, emojiNum);
        if(this.categoryUrl != null)
            jo.put(categoryUrlKey, categoryUrl);
        if(this.fileId != null)
            jo.put(fileIdKey, fileId);
        if (listEmoji != null){
            JSONArray arr = new JSONArray();
            for (EmojiData item : listEmoji){
                arr.add(item.toJsonObject());
            }
            jo.put(listEmojiKey, arr);
        }
        if(this.version != null)
            jo.put(versionKey, version);
        if(this.flag != null)
            jo.put(flagKey, flag);
        return jo;
    }
    
}
