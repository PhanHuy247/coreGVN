/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.pointpackage;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class PointPacket implements Comparable<PointPacket>, IEntity {
    protected static final String idKey = "id";
    public String id;

    protected static final String priceKey = "price";
    public Double price;

    protected static final String pointKey = "point";
    public Integer point;
    
    protected static final String desKey = "des";
    public String description;

    protected static final String firstPurchasePointKey = "first_purchase_point";
    public Integer firstPurchasePoint;
    
    protected static final String firstPurchaseDescriptionKey = "first_purchase_description";
    public String firstPurchaseDescription;
    

    protected static final String productIdKey = "production_id";
    public String productId;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.price != null)
            jo.put(priceKey, this.price);
        if(this.point != null)
            jo.put(pointKey, this.point);
        if(this.description != null)
            jo.put(desKey, this.description);
        if(this.productId != null)
            jo.put(productIdKey, this.productId);


        return jo;
    }

    @Override
    public int compareTo(PointPacket o) {
        return this.price.compareTo(o.price);
    }
}
