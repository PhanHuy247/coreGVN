/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.OneObjectLog;
import com.vn.ntsc.backend.server.respond.impl.csv.HeaderCreator;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author DuongLTD
 */
public class BaseUser extends OneObjectLog implements IEntity {

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();

    private static final String genderKey = "gender";
    @TypeSwitch(value = ParamKey.GENDER, header = Headers.gender)
    public Integer gender;

    private static final String birthdayKey = "bir";
    @TypeSwitch(header = Headers.birthday)
    public String bir;

    private static final String ageKey = "age";
    @TypeSwitch(header = Headers.age)
    public Integer age;

    private static final String regionKey = "region";
    @TypeSwitch(value = ParamKey.REGION, header = Headers.region)
    public Integer region;

    private static final String lastLoginTimeKey = "last_login_time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.last_time_login)
    public String lastLoginTime;

    private static final String registerDateKey = "reg_date";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.register_time)
    public String registerDate;

    private static final String lastPurchaseTimeKey = "last_pur_time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.last_time_purchase)
    public String lastPurchaseTime;

    private static final String firstPurchaseTimeKey = "first_pur_time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.first_purchase_time)
    public String firstPurchaseTime;

    private static final String pointKey = "point";
    @TypeSwitch(header = Headers.current_point)
    public Integer point;
    
    private static final String totalPointKey = "total_point";
    @TypeSwitch(header = Headers.total_point)
    public Double total_point;
    
    private static final String totalPurchaseKey = "total_purchase";
    @TypeSwitch(header = Headers.total_purchase)
    public Double total_purchase;

    private static final String flagKey = "flag";
    @TypeSwitch(value = ParamKey.FLAG, header = Headers.status)
    public Integer flag;

    private static final String memoKey = "memo";
    @TypeSwitch(header = Headers.memo)
    public String memo;
    
     private static final String aboutKey = "about";
    @TypeSwitch(header = Headers.about)
    public String about;

    private static final String verificationKey = "verification_flag";
    @TypeSwitch(value = "age_check", header = Headers.age_check)
    public Integer verification_flag;
    
    private static final String application_idKey = "application_id";
    @TypeSwitch(value = ParamKey.APPLICATION_ID, header = Headers.status)
    public String application_id;

    protected static final String applicationNameKey = "application_name";
    @TypeSwitch(header = ParamKey.APPLICATION_NAME)
    public String applicationName;

    static {
        initHeader();
        initType();
    }

    private static void initType() {
//        jsonEnglishType = new JSONObject();
//        jsonJapaneseType = new JSONObject();

        JSONObject value = new JSONObject();

        //user type
        value.putAll(TypeValue.en_user_type);
        jsonEnglishType.put(ParamKey.USER_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_user_type);
        jsonJapaneseType.put(ParamKey.USER_TYPE, value);

        //gender
        value = new JSONObject();
        value.putAll(TypeValue.en_gender);
        jsonEnglishType.put(ParamKey.GENDER, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_gender);
        jsonJapaneseType.put(ParamKey.GENDER, value);

        //flag
        value = new JSONObject();
        value.putAll(TypeValue.en_flag);
        jsonEnglishType.put(ParamKey.FLAG, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_flag);
        jsonJapaneseType.put(ParamKey.FLAG, value);

        //verification flag
        value = new JSONObject();
        value.putAll(TypeValue.en_verify_flag);
        jsonEnglishType.put("age_check", value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_verify_flag);
        jsonJapaneseType.put("age_check", value);
    }

    private static void initHeader() {
//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();

        List<String> keys = new ArrayList<>();
        keys.add(Headers.number);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.gender);
        keys.add(Headers.birthday);
        keys.add(Headers.age);
        keys.add(Headers.region);
        keys.add(Headers.last_time_login);
        keys.add(Headers.register_time);
        keys.add(Headers.last_time_purchase);
        keys.add(Headers.first_purchase_time);
        keys.add(Headers.current_point);
        keys.add(Headers.status);
        keys.add(Headers.memo);
        keys.add(Headers.about);
        keys.add(Headers.age_check);
        keys.add(Headers.total_point);
        keys.add(Headers.total_purchase);
        headers.addAll(keys);
//        headers = keys;

        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);
    }

    public BaseUser() {

    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.email != null) {
            jo.put(emailKey, email);
        }
        if (this.gender != null) {
            jo.put(genderKey, gender);
        }
        if (this.region != null) {
            jo.put(regionKey, region);
        }
        if (this.point != null) {
            jo.put(pointKey, point);
        }
        if (this.registerDate != null) {
            jo.put(registerDateKey, registerDate);
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
        if (this.firstPurchaseTime != null) {
            jo.put(firstPurchaseTimeKey, this.firstPurchaseTime);
        }
        if (this.lastPurchaseTime != null) {
            jo.put(lastPurchaseTimeKey, this.lastPurchaseTime);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (this.age != null) {
            jo.put(ageKey, this.age);
        }
        if (this.bir != null) {
            jo.put(birthdayKey, this.bir);
        }
        if (this.memo != null) {
            jo.put(memoKey, this.memo);
        }
        if (this.verification_flag != null) {
            jo.put(verificationKey, verification_flag);
        }
        if (this.application_id != null) {
            jo.put(application_idKey, this.application_id);
        }
        if (this.applicationName != null) {
            jo.put(applicationNameKey, this.applicationName);
        }
        if (this.about != null) {
            jo.put(aboutKey, this.about);
        }
        if (this.total_point != null) {
            jo.put(totalPointKey, this.total_point);
        }
        if (this.total_purchase != null) {
            jo.put(totalPurchaseKey, this.total_purchase);
        }
        return jo;

    }

    @Override
    public List<String> getHeaders(Integer type) {
        if (type != null && type == 1) {
            return englishHeader;
        } else {
            return japaneseHeader;
        }
    }

    // GET LIST KEY OF SUBCLASS
    @Override
    public List<String> getKeys() {
        return headers;
    }

    // get user type follow english or japanese

    @Override
    public JSONObject getJsonType(Integer type) {
        if (type != null && type == 1) {
            return jsonEnglishType;
        } else {
            return jsonJapaneseType;
        }
    }
}
