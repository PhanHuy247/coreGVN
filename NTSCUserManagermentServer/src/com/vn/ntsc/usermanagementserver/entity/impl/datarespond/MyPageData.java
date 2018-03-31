/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Admin
 */
public class MyPageData implements IEntity {

    private static final String backstageNubmerKey = "bckstg_num";
    public Long backstageNumber;

//    private static final String giftListKey = "gift_list";
//    public List<GiftData> giftData;

    private static final String buzzNumberKey = "buzz_number";
    public Long buzzNumber;

    private static final String checkoutNumKey = "checkout_num";  //out
    public Integer checkoutNum;
    
    private static final String myFootprintNumberKey = "my_footprint_num";
    public Integer myFootprintNumber;    

    private static final String favouritedNumberKey = "fvt_num";
    public Long favouritedNumber;

    private static final String notiLikeNumberKey = "noti_like_num";
    public Long notiLikeNumber;
    
    private static final String notiNewsNumberKey = "noti_news_num";
    public Long notiNewsNumber;
    
    private static final String notiQANumberKey = "noti_qa_num";
    public Long notiQANumber;
    
    private static final String notiNumberKey = "noti_num";
    public Integer notiNumber;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (backstageNumber != null) {
            jo.put(backstageNubmerKey, backstageNumber);
        }
        
        if (notiNumber != null) {
            jo.put(notiNumberKey, notiNumber);
        }
//        if (giftData != null) {
//            JSONArray arr = new JSONArray();
//            for(GiftData data: giftData){
//                arr.add(data.toJsonObject());
//            }
//            jo.put(giftListKey, arr);
//        }
        if (buzzNumber != null) {
            jo.put(buzzNumberKey, buzzNumber);
        }
        if (favouritedNumber != null) {
            jo.put(favouritedNumberKey, favouritedNumber);
        }
        if (checkoutNum != null) {
            jo.put(checkoutNumKey, checkoutNum);
        }
        if (this.myFootprintNumber != null) {
            jo.put(myFootprintNumberKey, this.myFootprintNumber);
        }
        if (notiLikeNumber != null) {
            jo.put(notiLikeNumberKey, notiLikeNumber);
        }
        if (notiNewsNumber != null) {
            jo.put(notiNewsNumberKey, notiNewsNumber);
        }
        if (notiQANumber != null) {
            jo.put(notiQANumberKey, notiQANumber);
        }
        return jo;
    }

}
