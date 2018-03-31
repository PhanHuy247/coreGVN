/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class Application implements IEntity {
    
    private static final String idKey = "id";
    public String id;

    private static final String uniqueNameKey = "unique_name";
    public String uniqueName;

    private static final String displayNameKey = "display_name";
    public String displayName;

    private static final String numberOfUserKey = "number_of_user";
    public Integer numberOfUser;

    private static final String canDeleteKey = "can_delete";
    public Boolean canDelete;

    public Application() {
        numberOfUser = 0;
        canDelete = false;
    }
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.uniqueName != null)
            jo.put(uniqueNameKey, this.uniqueName);
        if(this.displayName != null)
            jo.put(displayNameKey, this.displayName);
        if(this.numberOfUser != null)
            jo.put(numberOfUserKey , this.numberOfUser);
        if(this.canDelete != null)
            jo.put(canDeleteKey , this.canDelete);

        return jo;
    }

}
