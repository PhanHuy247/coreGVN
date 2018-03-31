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
public class Gift implements IEntity{
    
    private static final String giftIdKey = "id";
    public String giftId;

    private static final String categoryIdKey = "cat_id";
    public String categoryId;

    private static final String giftPriceKey = "gift_pri";
    public Integer giftPrice;

    private static final String giftInforKey = "gift_inf";
    public String giftInfor;

    private static final String enGiftNameKey = "en_name";
    public String enGiftName;


    private static final String jpGiftNameKey = "jp_name";
    public String jpGiftName;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.giftId != null)
            jo.put(giftIdKey, this.giftId);
        if(this.categoryId != null)
            jo.put(categoryIdKey, this.categoryId);
        if(this.giftPrice != null)
            jo.put(giftPriceKey, this.giftPrice);
        if(this.giftInfor != null)
            jo.put(giftInforKey, this.giftInfor);
        if(this.enGiftName != null)
            jo.put(enGiftNameKey, this.enGiftName);
        if(this.jpGiftName != null)
            jo.put(jpGiftNameKey, this.jpGiftName);

        return jo;
    }
}
