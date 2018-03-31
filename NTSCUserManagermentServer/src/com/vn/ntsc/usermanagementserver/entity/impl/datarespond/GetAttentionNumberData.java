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
 * @author duyetpt
 */
public class GetAttentionNumberData implements IEntity {

    private static final String checkoutNumKey = "checkout_num";  //out
    public Integer checkoutNum;

    private static final String favouritedNumberKey = "fvt_num";
    public Integer favouritedNumber;

    private static final String newCheckoutNumKey = "new_checkout_num";
    public Integer newCheckoutNum;

    private static final String newFavoritedNumberKey = "new_fvt_num";
    public Integer newFavoritedNumber;
    
    private static final String notiLikeNumberKey = "noti_like_num";
    public Integer notiLikeNumber;
    
    private static final String notiNumberKey = "noti_num";
    public Integer notiNumber;

    private static final String myFootprintNumberKey = "my_footprint_num";
    public Integer myFootprintNumber;    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (checkoutNum != null) {
            jo.put(checkoutNumKey, checkoutNum);
        }
        if (favouritedNumber != null) {
            jo.put(favouritedNumberKey, favouritedNumber);
        }
        if (newCheckoutNum != null) {
            jo.put(newCheckoutNumKey, newCheckoutNum);
        }
        if (newFavoritedNumber != null) {
            jo.put(newFavoritedNumberKey, newFavoritedNumber);
        }
        if (notiNumber != null) {
            jo.put(notiNumberKey, notiNumber);
        }
        if (notiLikeNumber != null) {
            jo.put(notiLikeNumberKey, notiLikeNumber);
        }
        if (myFootprintNumber != null) {
            jo.put(myFootprintNumberKey, myFootprintNumber);
        }

        return jo;
    }

}
