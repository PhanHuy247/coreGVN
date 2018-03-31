/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Rua
 */
public class UserTemplate implements IEntity{

    private static final String idKey = "template_id";
    public String id;
    
    private static final String userIdKey = "user_id";
    public String userId;
    
    private static final String templateContentKey = "template_content";
    public String templateContent;
    
    private static final String templateTitleKey = "template_title";
    public String templateTitle;

    public UserTemplate() {
    }

    public UserTemplate(String id) {
        this.id = id;
    }
    
    public UserTemplate(String id, String userId, String templateContent, String templateTitle) {
        this.id = id;
        this.userId = userId;
        this.templateContent = templateContent;
        this.templateTitle = templateTitle;
    }
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        
        if(this.id != null)
            obj.put(idKey, this.id);
        if(this.userId != null)
            obj.put(userIdKey, this.userId);
        if(this.templateContent != null)
            obj.put(templateContentKey, this.templateContent);
        if(this.templateTitle != null)
            obj.put(templateTitleKey, this.templateTitle);
        
        return obj;
    }
    
    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }    
    
}
