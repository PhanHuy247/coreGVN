/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import eazycommon.util.Util;
import java.util.List;
import java.util.UUID;
import eazycommon.constant.Constant;
import eazycommon.constant.Format;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Male;
import com.vn.ntsc.usermanagementserver.server.respond.common.UserInforValidator;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;

/**
 *
 * @author DuongLTD
 */
public class User implements Comparable<User>, IEntity {

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String applicationIdKey = "application_id";
    public String applicationId;

    public static final String voip_notify_tokenKey = "voip_notify_token";
    public String voip_notify_token;

    private static final String deviceTypeKey = "device_type";
    public Long deviceType;

    private static final String usernameKey = "user_name";
    public String username;

    private static final String sortNameKey = "sort_name";
    public String sortName;

    private static final String emailKey = "email";
    public String email;

    private static final String passwordKey = "pwd";
    public String password;

    private static final String originalPasswordKey = "original_pwd";
    public String originalPassword;

    public String sipPassword;

    private static final String avatarIdKey = "ava_id";
    public String avatarId;

    private static final String aboutKey = "abt";
    public String about;

    private static final String birthdayKey = "bir";
    public String birthday;

    private static final String fbIdKey = "fb_id";
    public String fbId;

    private static final String genderKey = "gender";
    public Long gender;

    private static final String body_typeKey = "body_type";
    public Long body_type;

    private static final String regionKey = "region";
    public Long region;

    private static final String autoRegionKey = "auto_region";
    public Long autoRegion;

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

    private static final String codeKey = "vrf_code";
    public String code;

    private static final String lastUpdateKey = "lst_upd";
    public String lastUpdate;

    private static final String flagKey = "flag";
    public Long flag;

    public static final String finishRegisterFlagKey = "finish_register_flag";
    public Long finishRegisterFlag;

    public static final String updateEmailFlagKey = "update_email_flag";
    public Long updateEmailFlag;

    private static final String cmCodeKey = "cm_code";
    public String cmCode;

    private static final String checkTimeKey = "chk_time";
    public String checkTime;

    private static final String registerDateKey = "reg_date";
    public String registerDate;

    private static final String notiReadTimeKey = "noit_read_time";
    public Long notiReadTime;

    private static final String systemAccountKey = "sys_acc";
    public Long systemAcc;

    private static final String userTypeKey = "user_type";
    public Long userType;

    private static final String reportNumKey = "rpt_num";
    public Integer reportNum;

    private static final String lastLoginTimeKey = "last_login_time";
    public String lastLoginTime;

    private static final String lastPurchaseTimeKey = "last_pur_time";
    public String lastPurchaseTime;

    private static final String rateNumberKey = "rate_num";
    public Integer rateNumber;

    private static final String ageKey = "age";   // out
    public Integer age;
    
    private static final String hasAccountEmailKey = "has_account_email";
    public Integer hasAccountEmail;

    private static final String isActiveUserKey = "is_active_user"; //out
    public Integer isActiveUser;

    private static final String backStageRateKey = "bckstg_rate";//
    public Double backStageRate;

    private static final String statusKey = "status";
    public String status;

    private static final String buzzNumberKey = "buzz_number";
    public Long buzzNumber;

    private static final String jobKey = "job";
    public Long job;

    public static final String videoCallWaitingKey = "video_call_waiting";
    public Boolean videoCall;

    public static final String voiceCallWaitingKey = "voice_call_waiting";
    public Boolean voiceCall;
    
    //Linh add #5227
    private static final String callRequestSettingKey = "call_request_setting";
    public Integer callRequestSetting;

    public static final String memoKey = "memo";
    public String memo;

    public static final String hobbyKey = "hobby";
    public String hobby;

    // Add LongLT 8/2016
    public static final String osVersionKey = "os_version";
    public String osVersion;

    public static final String appVersionKey = "application_version";
    public String appVersion;

    public static final String deviceNameKey = "device_name";
    public String deviceName;

    public static final String isPurchasedKey = "is_purchase";
    public Integer isPurchased;

    //HUNGDT add
    public static final String device_idKey = "device_id";
    public String device_id;

    public static final String aidKey = "adid";
    public String adid;

    public static final String adidKey = "ad_id";
    public String ad_id;

    public static final String countryCodeKey = "countryCode";
    public String countryCode;

    public static final String IPKey = "ip";
    public String ip;

    //Thanhdd add Affiliation
    public static final String siteIdKey = "site_id";
    public Integer site_id;

//    //HUNGDT add Multi #6374
//    private static final String applicationNameKey = "application_name";
//    public String applicationName;
    
    public static final String idNumberKey = "id_number";
    public String idNumber;
    
    public static final String phoneNumberKey = "phone_number";
    public String phoneNumber;
    
    //add by Huy 20170310
    public static final String isFavKey = "is_fav";
    public Long isFav;
    public static final String avatarUrlKey = "ava";
    public String avaUrl;
    
    public static final String rateValueKey = "rate_value";
    public List<Integer> rateValue;
    
    public static final String hasRatedKey = "has_rated";
    public Integer hasRated;
    
    public static final String isAlertKey = "is_alt";
    public Integer isAlert;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        //HUNGDT add Multi #6374
        if (this.applicationId != null) {
            jo.put(applicationIdKey, this.applicationId);
        }
        if (this.username != null) {
            jo.put(usernameKey, this.username);
        }
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
        if (this.birthday != null) {
            jo.put(birthdayKey, birthday);
        }
        if (this.fbId != null) {
            jo.put(fbIdKey, fbId);
        }
        if (status != null) {
            jo.put(statusKey, status);
        }
        if (this.gender != null) {
            jo.put(genderKey, gender);
        }
        //HUNGDT
        if (this.body_type != null) {
            jo.put(body_typeKey, body_type);
        }

        if (this.region != null) {
            jo.put(regionKey, region);
        }
        if (this.autoRegion != null) {
            jo.put(autoRegionKey, autoRegion);
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
        if (this.code != null) {
            jo.put(codeKey, code);
        }
        if (this.lastUpdate != null) {
            jo.put(lastUpdateKey, lastUpdate);
        }
        if (this.flag != null) {
            jo.put(flagKey, this.flag);
        }

        if (this.finishRegisterFlag != null) {
            jo.put(finishRegisterFlagKey, this.finishRegisterFlag);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (this.checkTime != null) {
            jo.put(checkTimeKey, checkTime);
        }
        if (this.registerDate != null) {
            jo.put(registerDateKey, registerDate);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, userType);
        }
        if (this.notiReadTime != null) {
            jo.put(notiReadTimeKey, notiReadTime);
        }
        if (this.reportNum != null) {
            jo.put(reportNumKey, reportNum);
        }
        if (this.lastLoginTime != null) {
            jo.put(lastLoginTimeKey, lastLoginTime);
        }
        if (this.lastPurchaseTime != null) {
            jo.put(lastPurchaseTimeKey, lastPurchaseTime);
        }
        if (this.rateNumber != null) {
            jo.put(rateNumberKey, rateNumber);
        }
        if (this.age != null) {
            jo.put(ageKey, age);
        }
        if (isActiveUser != null) {
            jo.put(isActiveUserKey, isActiveUser);
        }
        if (this.backStageRate != null) {
            jo.put(backStageRateKey, backStageRate);
        }
        if (this.buzzNumber != null) {
            jo.put(buzzNumberKey, buzzNumber);
        }
        if (this.job != null) {
            jo.put(jobKey, job);
        }
        if (videoCall != null) {
            jo.put(videoCallWaitingKey, videoCall);
        }
        if (voiceCall != null) {
            jo.put(voiceCallWaitingKey, voiceCall);
        }
        if (updateEmailFlag != null) {
            jo.put(updateEmailFlagKey, updateEmailFlag);
        }
        if (osVersion != null) {
            jo.put(osVersionKey, osVersion);
        }
        if (appVersion != null) {
            jo.put(appVersionKey, appVersion);
        }
        if (deviceName != null) {
            jo.put(deviceNameKey, deviceName);
        }

        if (device_id != null) {
            jo.put(device_idKey, device_id);
        }

        if (isPurchased != null) {
            jo.put(isPurchasedKey, isPurchased);
        }

        if (site_id != null) {
            jo.put(siteIdKey, site_id);
        }

        if (this.adid != null) {
            jo.put(aidKey, this.adid);
        }

        if (this.ad_id != null) {
            jo.put(adidKey, this.ad_id);
        }

        //HUNGDT add countryCode
        if (this.countryCode != null) {
            jo.put(countryCodeKey, this.countryCode);
        }

        if (this.ip != null) {
            jo.put(IPKey, this.ip);
        }

        //HUNGDT #6360 call kit
        if (voip_notify_token != null) {
            jo.put(voip_notify_tokenKey, voip_notify_token);
        }
        
        if (idNumber != null) {
            jo.put(idNumberKey, idNumber);
        }
        if (phoneNumber != null) {
            jo.put(phoneNumberKey, phoneNumber);
        }
        
        //Huy 20170310 #10018
        if(isFav != null){
            jo.put(isFavKey, isFav);
        }
        if(avaUrl != null){
            jo.put(avatarUrlKey,avaUrl);
        }
        if(rateValue != null){
            jo.put(rateValueKey, rateValue);
        }
        if(hasRated != null){
            jo.put(hasRatedKey, hasRated);
        }
        if(hobby != null){
            jo.put("hobbies",hobby);
        }
        if(hasAccountEmail != null){
            jo.put(hasAccountEmailKey,hasAccountEmail);
        }
        if(isAlert != null){
            jo.put(isAlertKey, isAlert);
        }
        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

    public static User getLoginUser(JSONObject obj) {
        User user = new User();
        String mail = (String) obj.get(emailKey);
        if (mail != null) {
            user.email = mail;
        }
        String pwd = (String) obj.get(passwordKey);
        if (pwd != null) {
            user.password = pwd;
        }
        String fId = (String) obj.get(fbIdKey);
        if (fId != null) {
            user.fbId = fId;
        }
        return user;
    }
    
    public static User getRegisterUser(JSONObject obj) throws EazyException {
        User user = new User();
        Long gender = (Long) obj.get(genderKey);
        if (gender != null && gender == Constant.GENDER.FEMALE) {
            user = new Female();
        } 
        else if (gender != null && gender == Constant.GENDER.MALE) {
            user = new Male();
        }
        user.gender = gender;
        String fId = (String) obj.get(fbIdKey);
        if (fId != null) {
            user.fbId = fId;
        } else {
            String email = (String) obj.get(ParamKey.EMAIL);
            if (email != null){
                user.email = email;
            }
            else {
                user.email = UUID.randomUUID().toString();
            }
            user.password = (String) obj.get(ParamKey.PASSWORD);
            user.originalPassword = (String) obj.get(ParamKey.ORIGINAL_PASSWORD);
        }

        user.finishRegisterFlag = (long) Constant.FLAG.OFF;
        user.flag = (long) Constant.FLAG.ON;
        String birthday = (String) obj.get(birthdayKey);
        if (birthday != null) {
            user.birthday = birthday;
        }
        
        String username = (String) obj.get(usernameKey);
        if (username != null){
            user.username = username;
        }
        String idNum = (String) obj.get(idNumberKey);
        if (idNum != null){
            user.idNumber = idNum;
        }
        String phoneNum = (String) obj.get(phoneNumberKey);
        if (phoneNum != null){
            user.phoneNumber = phoneNum;
        }
        
        String abt = (String) obj.get(aboutKey);
        if (abt != null) {
            user.about = abt;
        }

        Long loc = (Long) obj.get(regionKey);
        if (loc != null) {
            user.region = loc;
        }
        Long autoLoc = (Long) obj.get(autoRegionKey);
        if (autoLoc != null) {
            user.autoRegion = autoLoc;
        }
        
        Long jo = (Long) obj.get(jobKey);
        if (jo != null) {
            user.job = jo;
        }
        
        String hobby = (String) obj.get(hobbyKey);
        if (hobby != null) {
            user.hobby = hobby;
        }
        Long bdy_type = (Long) obj.get(body_typeKey);
        if (bdy_type != null) {
            user.body_type = bdy_type;
        }
        //temp hot fix job
        Long device_type = (Long) obj.get(deviceTypeKey);
        if (device_type != null) {
            user.deviceType = device_type;
        }

        user.osVersion = obj.get(osVersionKey) != null ? obj.get(osVersionKey).toString() : "";
        user.device_id = obj.get(device_idKey) != null ? obj.get(device_idKey).toString() : "";

        user.appVersion = obj.get(appVersionKey) != null ? obj.get(appVersionKey).toString() : "";

        user.deviceName = obj.get(deviceNameKey) != null ? obj.get(deviceNameKey).toString() : "";

        user.isPurchased = obj.get(isPurchasedKey) != null ? new Integer(obj.get(isPurchasedKey).toString()) : null;
        user.site_id = obj.get(siteIdKey) != null ? new Integer(obj.get(siteIdKey).toString()) : null;
        //HUNGDT add countryCode
        user.countryCode = obj.get(countryCodeKey) != null ? obj.get(countryCodeKey).toString() : "";
        user.ip = obj.get(IPKey) != null ? obj.get(IPKey).toString() : "";
        
        //HUNGDT callkit
        String voip_notify_token = (String) obj.get(voip_notify_tokenKey);
        if (voip_notify_token != null) {
            user.voip_notify_token = voip_notify_token;
        }
        
        return user;
    }

    public static User getRegisterUserByAdmin(JSONObject obj) {
        User user;

        Long gen = (Long) obj.get(genderKey);
        if (gen != null && gen == Constant.GENDER.FEMALE) {
            user = new Female();
        } else {
            user = new Male();
        }
        user.gender = gen;
        Long body_type = (Long) obj.get(body_typeKey);
        user.body_type = body_type;
        String userName = (String) obj.get(usernameKey);
        user.username = userName;
        user.region = (Long) obj.get(regionKey);
        user.autoRegion = (long) Constant.FLAG.OFF;

        user.job = (Long) obj.get(jobKey);
        user.about = (String) obj.get(aboutKey);

        String fId = (String) obj.get(fbIdKey);
        if (fId != null) {
            user.fbId = fId;
        }
        else {
            user.email = (String) obj.get(emailKey);
            user.originalPassword = (String) obj.get(originalPasswordKey);
            user.password = (String) obj.get(passwordKey);
            user.updateEmailFlag = (long) Constant.FLAG.ON;

        }
        user.finishRegisterFlag = (long) Constant.FLAG.ON;
        if (gen != null && gen == Constant.GENDER.FEMALE) {
            Female female = (Female) user;
            female.verificationFlag = (long) Constant.REVIEW_STATUS_FLAG.APPROVED;
            female.measurements = (List<Long>) obj.get(Female.measurementsKey);
            female.typeOfMan = (String) obj.get(Female.typeOfMansKey);
            female.cup = (Long) obj.get(Female.cupKey);
            female.cuteType = (Long) obj.get(Female.cuteTypeKey);
            female.joinHours = (Long) obj.get(Female.joinHoursKey);
            female.fetishs = (String) obj.get(Female.fetishKey);
            female.hobby = (String) obj.get(Female.hobbyKey);
//            if(macomId != null || famuId != null){
//                female.verificationFlag = (long)Constant.APPROVED;
//            }else{
//                female.verificationFlag = (long)Constant.NONE;
//            }
        } else {
            Male male = (Male) user;
            male.hobby = (String) obj.get(Male.hobbyKey);
        }

        user.flag = (long) Constant.FLAG.ON;
        String bir = (String) obj.get(birthdayKey);
        if (bir != null) {
            user.birthday = bir;
        }
        return user;
    }

    public void getUpdateUser(JSONObject obj) {
        String uName = (String) obj.get(usernameKey);
        if (uName != null) {
            this.username = uName;
            this.sortName = uName.toLowerCase();
            this.flag = (long) Constant.USER_STATUS_FLAG.ACTIVE;
        }

//        this.point = null;
//        this.fbId = null;

        String email = (String) obj.get(emailKey);
        if (email != null) {
            this.email = email;
            this.updateEmailFlag = 1L;
        }
        String fId = (String) obj.get(fbIdKey);
        if (fId != null) {
            this.fbId = fId.toLowerCase();
            this.email = String.format(Format.FACEBOOK_EMAIL_FORMAT, fbId);
        }
        
        String idNum = (String) obj.get(idNumberKey);
        if (idNum != null){
            this.idNumber = idNum;
        }
        String phoneNum = (String) obj.get(phoneNumberKey);
        if (phoneNum != null){
            this.phoneNumber = phoneNum;
        }
        
        String pwd = (String) obj.get(passwordKey);
        if (pwd != null) {
            this.password = pwd;
        }
        String oripwd = (String) obj.get(originalPasswordKey);
        if (oripwd != null) {
            this.originalPassword = oripwd;
        }
        String abt = (String) obj.get(aboutKey);
        if (abt != null) {
            this.about = abt;
        }

        Long loc = (Long) obj.get(regionKey);
        if (loc != null) {
            this.region = loc;
        }
        Long autoLoc = (Long) obj.get(autoRegionKey);
        if (autoLoc != null) {
            this.autoRegion = autoLoc;
        }
        Long flg = (Long) obj.get(flagKey);
        if (flg != null) {
            this.flag = flg;
        }
        String bir = (String) obj.get(birthdayKey);
        if (bir != null) {
            this.birthday = bir;
        }
        Long jo = (Long) obj.get(jobKey);
        if (jo != null) {
            this.job = jo;
        }
        String mem = (String) obj.get(memoKey);
        if (mem != null) {
            this.memo = mem;
        }
        String cm = (String) obj.get(cmCodeKey);
        if (cm != null) {
            this.cmCode = cm;
        }
        Boolean videoCall = (Boolean) obj.get(videoCallWaitingKey);
        if (videoCall != null) {
            this.videoCall = videoCall;
        }
        Boolean voiceCall = (Boolean) obj.get(voiceCallWaitingKey);
        if (voiceCall != null) {
            this.voiceCall = voiceCall;
        }

        String str = (String) obj.get(hobbyKey);
        if (str != null) {
            hobby = str;
        }
        Long bdy_type = (Long) obj.get(body_typeKey);
        if (bdy_type != null) {
            this.body_type = bdy_type;
        }
        //temp hot fix job
        Long device_type = (Long) obj.get(deviceTypeKey);
        if (device_type != null) {
            this.deviceType = device_type;
        }

        // add LongLT 8/2016        
        this.osVersion = obj.get(osVersionKey) != null ? obj.get(osVersionKey).toString() : "";

        this.appVersion = obj.get(appVersionKey) != null ? obj.get(appVersionKey).toString() : "";

        this.appVersion = obj.get(deviceNameKey) != null ? obj.get(deviceNameKey).toString() : "";

        this.isPurchased = obj.get(isPurchasedKey) != null ? new Integer(obj.get(isPurchasedKey).toString()) : null;
        this.site_id = obj.get(siteIdKey) != null ? new Integer(obj.get(siteIdKey).toString()) : null;
    }

    @Override
    public int compareTo(User o) {
        return o.checkTime.compareTo(this.checkTime);
    }

    public boolean isCompleteUser() {
        return this.username != null && this.region != null;
    }
}
