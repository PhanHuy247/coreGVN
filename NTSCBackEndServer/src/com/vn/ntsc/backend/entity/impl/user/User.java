/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user;

import eazycommon.constant.mongokey.UserdbKey;
import com.vn.ntsc.backend.entity.IEntity;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author DuongLTD
 */
public class User implements IEntity {

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String applicationIdKey = "application_id";
    public String applicationId;

    private static final String applicationNameKey = "application_name";
    public String applicationName;

    private static final String usernameKey = "user_name";
    public String username;

    private static final String usernameReplsKey = "user_name_repls";
    public String username_repls;

    private static final String sortNameKey = "sort_name";
    public String sortName;

    private static final String emailKey = "email";
    public String email;

    private static final String passwordKey = "pwd";
    public String password;

    private static final String originalPasswordKey = "original_pwd";
    public String originalPassword;

    private static final String avatarIdKey = "ava_id";
    public String avatarId;

    private static final String aboutKey = "abt";
    public String about;

    private static final String aboutReplsKey = "abt_repls";
    public String about_repls;

    private static final String birthdayKey = "bir";
    public String birthday;

    private static final String fbIdKey = "fb_id";
    public String fbId;

    private static final String genderKey = "gender";
    public Long gender;

    private static final String body_typeKey = "body_type";
    public Long body_type;

    private static final String locationKey = "region";
    public Long location;

    private static final String pointKey = "point";
    public Long point;

    private static final String favouristNumberKey = "fav_num";
    public Long favouristNumber;

    private static final String favouritedNumberKey = "fvt_num";
    public Long favouritedNumber;

    private static final String backstageNubmerKey = "bckstg_num";
    public Long backstageNumber;

    private static final String pbImageNumberKey = "pbimg_num";
    public Long pbImageNumber;

    private static final String giftNumberKey = "gift_num";
    public Long giftNumber;

    private static final String lastUpdateKey = "lst_upd";
    public String lastUpdate;

    private static final String requestIdKey = "rqt_id";
    public String requestId;

    private static final String ageKey = "age";
    public Integer age;

    private static final String registerDateKey = "reg_date";
    public String registerDate;

    private static final String reportNumKey = "rpt_num";
    public Integer reportNum;

    private static final String cmCodeKey = "cm_code";
    public String cmCode;

    private static final String lastLoginTimeKey = "last_login_time";
    public String lastLoginTime;

    private static final String lastPurchaseTimeKey = "last_pur_time";
    public String lastPurchaseTime;

    private static final String flagKey = "flag";
    public Long flag;

    private static final String userTypeKey = "user_type";
    public Long userType;

    private static final String jobKey = "job";
    public Long job;

    private static final String videoCallWaitingKey = "video_call_waiting";
    public Boolean videoCall;

    private static final String voiceCallWaitingKey = "voice_call_waiting";
    public Boolean voiceCall;

    public static final String verificationFlagKey = "verification_flag";
    public Long verificationFlag;

//    public static final String updateEmailFlagKey = "update_email_flag";
//    public Long updateEmailFlag;
    public static final String finishRegisterFlagKey = "finish_register_flag";
    public Long finishRegisterFlag;

    private static final String memoKey = "memo";
    public String memo = "";

    //HUNGDT add
    public static final String os_versionKey = "os_version";
    public String os_version;
    public static final String application_versionKey = "application_version";
    public String application_version;
    public static final String device_nameKey = "device_name";
    public String device_name;
    public static final String aidKey = "ad_id";
    public String ad_id;

    //Add LongLT 24Aug2016
    public Integer notificationBuzz = 1;
    public Integer notificationChat = 0;
    public Integer eazyAlert = 1;

    // LongLT 20Sep2016
    public static final String isPurchaseKey = "is_purchase";
    public Integer isPurchase = 0;

    //Linh 10/10/2016
    public static final String deviceIdKey = "device_id";
    public String deviceId;
    public static final String deviceTypeKey = "device_type";
    public Integer deviceType;

    // ThanhDD add 03/11/2016
    public static final String totalPointKey = "total_point";
    public Integer totalPoint = 0;

    public static final String siteIdKey = "site_id";
    public Integer site_id = 0;

    public static final String isAdminKey = "is_Admin";
    public Integer isAdmin = 0;

    public static final String totalPurchaseKey = "total_purchase";//thanhdd add 09/11/2016 #5184
    public Integer totalPurchase = 0;

    public static final String totalPurchaseUserDetailKey = "total_purchase_user_detail";
    public Integer totalPurchaseUserDetail = 0;
    //thanhdd add 09/11/2016 #5184
    public static final String totalPurchaseAappleKey = "total_purchase_apple";
    public Integer totalPurchaseAapple = 0;
    public static final String totalPurchaseGoogleKey = "total_purchase_google";
    public Integer totalPurchaseGoogle = 0;
    public static final String totalPurchaseCreditCardKey = "total_purchase_credit_card";
    public Integer totalPurchaseCreditCard = 0;
    public static final String totalPurchaseBitcachKey = "total_purchase_bitcach";
    public Integer totalPurchaseBitcach = 0;
    public static final String totalPurchaseConvenienceKey = "total_purchase_convenience";
    public Integer totalPurchaseConvenience = 0;
    public static final String totalPurchasePointsBackKey = "total_purchase_points_back";
    public Integer totalPurchasePointsBack = 0;

    public static final String totalPurchaseCCheckKey = "total_purchase_points_ccheck";
    public Integer totalPurchaseCCheck = 0;

    //end
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.applicationName != null) {
            jo.put(applicationNameKey, this.applicationName);
        }
        if (this.applicationId != null) {
            jo.put(applicationIdKey, this.applicationId);
        }
        if (this.username != null) {
            jo.put(usernameKey, this.username);
        }
        if (this.username_repls != null) {
            jo.put(usernameReplsKey, this.username_repls);
        }

        // LongLT 20Sep2016 /////////////////////////// #4483
//        if (this.isPurchase != null) {
        if (this.lastPurchaseTime != null) {
            // LongLT 21Sep2016 /////////////////////////// hot fix
//            jo.put(isPurchaseKey, this.isPurchase);
            jo.put(isPurchaseKey, 1);
        } else {
            jo.put(isPurchaseKey, 0);
        }

        // Add LongLT 24Aug2016
        if (this.notificationBuzz != null) {
            jo.put(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ, this.notificationBuzz);
        }
        if (this.notificationChat != null) {
            jo.put(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT, this.notificationChat);
        }
        if (this.eazyAlert != null) {
            jo.put(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT, this.eazyAlert);
        }

        //HUNGDT add
        if (this.os_version != null) {
            jo.put(os_versionKey, this.os_version);
        }
        if (this.application_version != null) {
            jo.put(application_versionKey, this.application_version);
        }
        if (this.device_name != null) {
            jo.put(device_nameKey, this.device_name);
        }
        if (this.ad_id != null) {
            jo.put(aidKey, this.ad_id);
        }
        //END

        if (this.sortName != null) {
            jo.put(sortNameKey, this.sortName);
        }
        if (this.email != null) {
            jo.put(emailKey, email);
        }
        if (this.password != null) {
            jo.put(passwordKey, password);
        }
        if (this.originalPassword != null) {
            jo.put(originalPasswordKey, originalPassword);
        }
        if (this.avatarId != null) {
            jo.put(avatarIdKey, avatarId);
        }
        if (this.about != null) {
            jo.put(aboutKey, about);
        }
        if (this.about_repls != null) {
            jo.put(aboutReplsKey, about_repls);
        }
        if (this.birthday != null) {
            jo.put(birthdayKey, birthday);
        }
        if (this.fbId != null) {
            jo.put(fbIdKey, fbId);
        }
        if (this.gender != null) {
            jo.put(genderKey, gender);
        }
        if (this.location != null) {
            jo.put(locationKey, location);
        }
        if (this.point != null) {
            jo.put(pointKey, point);
        }
        if (this.favouristNumber != null) {
            jo.put(favouristNumberKey, favouristNumber);
        }
        if (this.favouritedNumber != null) {
            jo.put(favouritedNumberKey, favouritedNumber);
        }
        if (this.backstageNumber != null) {
            jo.put(backstageNubmerKey, backstageNumber);
        }
        if (this.pbImageNumber != null) {
            jo.put(pbImageNumberKey, pbImageNumber);
        }
        if (this.giftNumber != null) {
            jo.put(giftNumberKey, giftNumber);
        }
        if (this.lastUpdate != null) {
            jo.put(lastUpdateKey, lastUpdate);
        }
        if (this.requestId != null) {
            jo.put(requestIdKey, requestId);
        }
        if (this.age != null) {
            jo.put(ageKey, age);
        }
        if (this.registerDate != null) {
            jo.put(registerDateKey, registerDate);
        }
        if (this.reportNum != null) {
            jo.put(reportNumKey, this.reportNum);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (this.flag != null) {
            jo.put(flagKey, this.flag);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, this.userType);
        }
        if (this.lastLoginTime != null) {
            jo.put(lastLoginTimeKey, this.lastLoginTime);
        }
        if (this.lastPurchaseTime != null) {
            jo.put(lastPurchaseTimeKey, this.lastPurchaseTime);
        }
        if (this.job != null) {
            jo.put(jobKey, job);
        }
        if (this.videoCall != null) {
            jo.put(videoCallWaitingKey, videoCall);
        }
        if (this.voiceCall != null) {
            jo.put(voiceCallWaitingKey, voiceCall);
        }
        if (this.verificationFlag != null) {
            jo.put(verificationFlagKey, this.verificationFlag);
        }
        if (this.finishRegisterFlag != null) {
            jo.put(finishRegisterFlagKey, this.finishRegisterFlag);
        }
        if (this.memo != null) {
            jo.put(memoKey, this.memo);
        }

        if (this.body_type != null) {
            jo.put(body_typeKey, body_type);
        }

        if (this.deviceId != null) {
            jo.put(deviceIdKey, deviceId);
        }
        
        if (this.deviceType != null) {
            jo.put(deviceTypeKey, deviceType);
        }

        if (this.totalPoint != null) {
            jo.put(totalPointKey, totalPoint);
        }
        if (this.totalPurchase != null) {
            jo.put(totalPurchaseKey, totalPurchase);
        }

        if (this.site_id != null) {
            jo.put(siteIdKey, site_id);
        }
        //thanhdd add 11/11/2016
        if (this.totalPurchaseUserDetail != null) {
            jo.put(totalPurchaseUserDetailKey, totalPurchaseUserDetail);
        }
        if (this.totalPurchaseAapple != null) {
            jo.put(totalPurchaseAappleKey, totalPurchaseAapple);
        }
        if (this.totalPurchaseGoogle != null) {
            jo.put(totalPurchaseGoogleKey, totalPurchaseGoogle);
        }
        if (this.totalPurchaseBitcach != null) {
            jo.put(totalPurchaseBitcachKey, totalPurchaseBitcach);
        }
        if (this.totalPurchaseConvenience != null) {
            jo.put(totalPurchaseConvenienceKey, totalPurchaseConvenience);
        }
        if (this.totalPurchaseCreditCard != null) {
            jo.put(totalPurchaseCreditCardKey, totalPurchaseCreditCard);
        }
        if (this.totalPurchasePointsBack != null) {
            jo.put(totalPurchasePointsBackKey, totalPurchasePointsBack);
        }

        if (this.isAdmin != null) {
            jo.put(isAdminKey, isAdmin);
        }

        if (this.totalPurchaseCCheck != null) {
            jo.put(totalPurchaseCCheckKey, totalPurchaseCCheck);
        }

        return jo;
    }

}
