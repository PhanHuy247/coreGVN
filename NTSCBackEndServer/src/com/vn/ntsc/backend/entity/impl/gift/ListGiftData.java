/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.gift;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class ListGiftData implements IEntity {

    private static final String giftCategoryKey = "gift_cat";
    public IEntity giftCategory;

    private static final String llKey = "list";
    public List<IEntity> ll;

    public ListGiftData(IEntity giftCategory, List<IEntity> ll) {
        this.giftCategory = giftCategory;
        this.ll = ll;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.giftCategory != null) {
            jo.put(giftCategoryKey, this.giftCategory.toJsonObject());

        }
        if (this.ll != null) {
            JSONArray arr = new JSONArray();
            for (IEntity ll1 : ll) {
                arr.add(ll1.toJsonObject());
            }
            jo.put(llKey, arr);
        }
        return jo;
    }

}
