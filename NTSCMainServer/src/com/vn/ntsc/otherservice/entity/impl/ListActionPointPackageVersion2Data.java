/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;


/**
 *
 * @author RuAc0n
 */
public class ListActionPointPackageVersion2Data implements IEntity {
    
    private static final String isPurchaseKey = "is_purchase";
    public boolean isPurchase = true;

    private static final String packagesKey = "packages";
    public List<ActionPointPacket> packages;

    public ListActionPointPackageVersion2Data(boolean isPurchase, List<ActionPointPacket> packages) {
        this.packages = packages;
        this.isPurchase = isPurchase;
    }
    
    @Override
    public JSONObject toJsonObject() {
        
        JSONObject jo = new JSONObject();

        if (this.packages != null) {
            JSONArray arr = new JSONArray();
            for (ActionPointPacket actionPointPacket : this.packages) {
                arr.add(actionPointPacket.toJsonObject());
            }
            jo.put(packagesKey, arr);
        }
        jo.put(isPurchaseKey, this.isPurchase);
        return jo;
    }

}
