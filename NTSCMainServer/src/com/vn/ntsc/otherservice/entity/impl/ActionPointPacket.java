/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class ActionPointPacket extends PointPacket{
    
    private static final String textKey = "text";
    public String text;

    public ActionPointPacket(){
    }
    
//    public ActionPointPacket(int type){
//        if(type == 0){
//            text = "ios text";
//            productId = "eazy_test_point_package1";
//            description = "10$ Course";
//            price = (double) 1000;
//            point = 700;
//            id = "540b02b0e4b0bc362d159173";
//        }else{
//            productId = "eazy_point_package2";
//            description = "30$ Course";
//            price = (double) 3000;
//            point = 2150;
//            id = "540b02b0e4b0bc362d159176";
//            text = "android text";
//        }
//    }
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.price != null)
            jo.put(priceKey, this.price);
        if(this.point != null)
            jo.put(pointKey, this.point);
//        if(this.description != null)
//            jo.put(desKey, this.description);
        if(this.productId != null)
            jo.put(productIdKey, this.productId);
        if(this.text != null)
            jo.put(textKey, this.text);
        if(this.description != null)
            jo.put(desKey, this.description);


        return jo;
    }
}
