/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;

/**
 *
 * @author Admin
 */
public class GetUserInfoData implements IEntity {

    public User user;
    private static final String isFavouristKey = "is_fav";   // out
    public Long isFavourist;
    private static final String listGiftKey = "lst_gift";//
    public List<String> listGift;
    private static final String unlockBackstageKey = "unlck_bckstg"; //out
    public Integer unlockBackstage;
    private static final String isAlertKey = "is_alt";//
    public Long isAlert;
    private static final String isNotiKey = "is_noti"; //out
    public Long isNoti;
    private static final String notiNumberKey = "noti_num"; //out
    public Integer notiNum;
    private static final String checkoutNumKey = "checkout_num";  //out
    public Integer checkoutNum;
    private static final String myFootprintNumberKey = "my_footprint_num";
    public Integer myFootprintNumber;
    private static final String reviewingAvatarKey = "reviewing_avatar";  //out
    public String reviewingAvatar;
    private static final String memoKey = "memo";  //out
    public String memo;

    public JSONObject toJsonObject() {
        JSONObject jo = user.toJsonObject();
        if (isFavourist != null) {
            jo.put(isFavouristKey, isFavourist);
        }
        if (listGift != null) {
            jo.put(listGiftKey, listGift);
        }
        if (unlockBackstage != null) {
            jo.put(unlockBackstageKey, unlockBackstage);
        }
        if (isAlert != null) {
            jo.put(isAlertKey, isAlert);
        }
        if (isNoti != null) {
            jo.put(isNotiKey, isNoti);
        }
        if (notiNum != null) {
            jo.put(notiNumberKey, notiNum);
        }
        if (checkoutNum != null) {
            jo.put(checkoutNumKey, checkoutNum);
        }
        if (reviewingAvatar != null) {
            jo.put(reviewingAvatarKey, reviewingAvatar);
        }
        if (this.myFootprintNumber != null) {
            jo.put(myFootprintNumberKey, this.myFootprintNumber);
        }
        if (this.memo != null) {
            jo.put(memoKey, this.memo);
        }

        return jo;
    }

}
