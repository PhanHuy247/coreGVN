/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.gift;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GiftCategory implements IEntity{
    private static final String idKey = "cat_id";
    public String id;

    private static final String enCatNameKey = "en_name";
    public String enCatName;

    private static final String jpCatNameKey = "jp_name";
    public String jpCatName;

    private static final String giftNumKey = "gift_num";
    public Integer giftNumber;


    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.giftNumber != null)
            jo.put(giftNumKey, this.giftNumber);
        if(this.enCatName != null)
            jo.put(enCatNameKey, this.enCatName);
        if(this.jpCatName != null)
            jo.put(jpCatNameKey, this.jpCatName);
        
        return jo;
    }

}
