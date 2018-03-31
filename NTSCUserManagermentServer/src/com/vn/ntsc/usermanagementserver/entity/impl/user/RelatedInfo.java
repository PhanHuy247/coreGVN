/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;

/**
 *
 * @author Admin
 */
public class RelatedInfo {

    private static final String isFavouristKey = "is_fav";   // out
    public Long isFavourist;

    private static final String requestIdKey = "rqt_id"; //out
    public String requestId;

    private static final String longitudeKey = "long";//
    public Double longitude;

    private static final String latitudeKey = "lat";//
    public Double latitude;

    private static final String backListKey = "backlst";//
    public List<String> backList;

    private static final String statusKey = "status";
    public String status;

    private static final String listGiftKey = "lst_gift";//
    public List<String> listGift;

    private static final String isAlertKey = "is_alt";//
    public Long isAlert;

    private static final String backStageRateKey = "bckstg_rate";//
    public Double backStageRate;


    private static final String saveImagePointKey = "save_img_pnt"; //
    public Integer saveImagePoint;

    private static final String onlineAlertPointKey = "onl_alt_pnt"; //out
    public Integer onlineAlertPoint;

    private static final String daylyBonusKey = "day_bns_pnt"; //out
    public Integer daylyBonus;

    private static final String unlockBackstageKey = "unlck_bckstg"; //out
    public Integer unlockBackstage;

    private static final String isActiveUserKey = "is_active_user"; //out
    public Integer isActiveUser;

    private static final String addPointKey = "add_point"; //out
    public Integer addPoint;

    private static final String notiNumKey = "noti_num"; //out
    public Integer notiNum;

    private static final String isNotiKey = "is_noti"; //out
    public Long isNoti;

    private static final String regPointKey = "reg_pnt"; //out
    public Integer regPoint;

    private static final String winkPointKey = "wink_pnt"; //out
    public Integer winkPoint;

    private static final String viewImagePointKey = "view_img_pnt"; //out
    public Integer viewPoint;

    private static final String chatPointKey = "chat_pnt"; //out
    public Integer chatPoint;

    private static final String videoCallPointKey = "video_call_pnt"; //out
    public Integer videoCallPoint;

    private static final String voiceCallPointKey = "voide_call_pnt"; //out
    public Integer voiceCallPoint;

    private static final String backstageTimeKey = "bckstg_time"; //out
    public Integer backstageTime;

    private static final String invitationUrlCodeKey = "ivt_url"; //out
    public String invitationUrlCode;

    private static final String homePageUrlKey = "home_page_url"; //out
    public String homePageUrl;

    private static final String checkoutNumKey = "checkout_num";  //out
    public Integer checkoutNum;
    
    private static final String notificationKey = "noti"; // out
    public Long notification;    
    
    public Map toMap() {
        HashMap<String, Object> jo = new HashMap<String, Object>();

        if (this.isFavourist != null) {
            jo.put(isFavouristKey, isFavourist);
        }

        if (this.requestId != null) {
            jo.put(requestIdKey, requestId);
        }
        
        if (this.longitude != null) {
            jo.put(longitudeKey, longitude);
        }
        if (this.latitude != null) {
            jo.put(latitudeKey, latitude);
        }
        if (this.backList != null) {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < backList.size(); i++) {
                arr.add(backList.get(i));
            }
            jo.put(backListKey, arr);
        }
        if (this.status != null) {
            jo.put(statusKey, status);
        }
        if (this.listGift != null) {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < listGift.size(); i++) {
                arr.add(listGift.get(i));
            }
            jo.put(listGiftKey, arr);
        }
        if (this.isAlert != null) {
            jo.put(isAlertKey, isAlert);
        }
        if (this.backStageRate != null) {
            jo.put(backStageRateKey, backStageRate);
        }
        if (this.saveImagePoint != null) {
            jo.put(saveImagePointKey, saveImagePoint);
        }
        if (this.onlineAlertPoint != null) {
            jo.put(onlineAlertPointKey, onlineAlertPoint);
        }
        if (this.notification != null) {
            jo.put(notificationKey, notification);
        }
        if (this.daylyBonus != null) {
            jo.put(daylyBonusKey, daylyBonus);
        }
        if (this.unlockBackstage != null) {
            jo.put(unlockBackstageKey, unlockBackstage);
        }
        if (this.isActiveUser != null) {
            jo.put(isActiveUserKey, isActiveUser);
        }
        if (this.addPoint != null) {
            jo.put(addPointKey, addPoint);
        }
        if (this.notiNum != null) {
            jo.put(notiNumKey, notiNum);
        }
        if (this.isNoti != null) {
            jo.put(isNotiKey, isNoti);
        }
        if (this.regPoint != null) {
            jo.put(regPointKey, regPoint);
        }
        if (this.backstageTime != null) {
            jo.put(backstageTimeKey, backstageTime);
        }
        if (this.invitationUrlCode != null) {
            jo.put(invitationUrlCodeKey, invitationUrlCode);
        }
        if (this.homePageUrl != null) {
            jo.put(homePageUrlKey, homePageUrl);
        }
        if (this.chatPoint != null) {
            jo.put(chatPointKey, chatPoint);
        }
        if (this.videoCallPoint != null) {
            jo.put(videoCallPointKey, videoCallPoint);
        }
        if (this.voiceCallPoint != null) {
            jo.put(voiceCallPointKey, voiceCallPoint);
        }
        if (this.winkPoint != null) {
            jo.put(winkPointKey, winkPoint);
        }

        if (this.viewPoint != null) {
            jo.put(viewImagePointKey, viewPoint);
        }
        if (this.checkoutNum != null) {
            jo.put(checkoutNumKey, checkoutNum);
        }

        return jo;
    }
}
