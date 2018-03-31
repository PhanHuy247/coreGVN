/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class EmojiData implements IEntity{
    
    private static final String idKey = "emoji_id";
    public String id;
    
    private static final String codeKey = "code";
    public String code;
    
    private static final String nameKey = "name";
    public String name;
    
    private static final String urlKey = "emoji_url";
    public String url;
    
    private static final String fileIdKey = "file_id";
    public String fileId;
    
    private static final String fileTypeKey = "file_type";
    public String fileType;
    
    private static final String codeArrKey = "code_lst";
    public List<String> codeLst;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.code != null)
            jo.put(codeKey, this.code);
        if(this.name != null)
            jo.put(nameKey, this.name);
        if(this.url != null)
            jo.put(urlKey, url);
        if(this.fileId != null)
            jo.put(fileIdKey, fileId);
        if(this.fileType != null)
            jo.put(fileTypeKey, fileType);
        if(this.codeLst != null)
            jo.put(codeArrKey, codeLst);
        return jo;
    }
    
}
