/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.log;

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
 * @author RuAc0n
 */
public class LogPoint extends OneObjectLog implements IEntity {

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();

    private static final String replace_key = "XXX";

    @TypeSwitch(header = Headers.purpose)
    public String purpose;

    private static final String beforePointKey = "before_point";
    @TypeSwitch(header = Headers.before_point)
    public Integer beforePoint;

    private static final String afterPointKey = "after_point";
    @TypeSwitch(header = Headers.after_point)
    public Integer afterPoint;

    private static final String pointKey = "point";
    @TypeSwitch(header = Headers.used_point)
    public Integer point;

    private static final String timeKey = "time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.action_time)
    public String time;

    private static final String typeKey = "type";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public Integer type;

    private static final String saleTypeKey = "sale_type";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public Integer saleType;

    private static final String freePointTypeKey = "free_point_type";
    @TypeSwitch(value = ParamKey.NEXT, header = ParamKey.NEXT)
    public Integer freePointType;

    private static final String isAdminKey = "is_Admin";
    @TypeSwitch(value = ParamKey.ISADMIN, header = ParamKey.ISADMIN)
    public Integer isAdmin;

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

        // type
        value = new JSONObject();
        value.putAll(TypeValue.en_log_point);
        jsonEnglishType.put(ParamKey.TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_log_point);
        jsonJapaneseType.put(ParamKey.TYPE, value);

    }

    private static void initHeader() {

//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
        List<String> keys = new ArrayList<String>();
        keys.add(Headers.number);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.ip);
        keys.add(Headers.purpose);
        keys.add(Headers.before_point);
        keys.add(Headers.used_point);
        keys.add(Headers.after_point);
        keys.add(Headers.action_time);
        keys.add(Headers.is_admin);
        headers.addAll(keys);
//        headers = keys;

        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);

    }

    public static void setPurpose(List<IEntity> list, Integer type) {
        JSONObject jsonValue = (JSONObject) new LogPoint().getJsonType(type).get(ParamKey.TYPE);
        for (IEntity log : list) {
            setPurpose((LogPoint) log, jsonValue);
        }
    }

    private static void setPurpose(LogPoint log, JSONObject json) {
        String value = (String) json.get(log.type);
        if (log.partnerId != null) {
            value = value.replace(replace_key, log.partnerId);
        }
        log.purpose = value;
    }

    public LogPoint() {
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        //HUNGDT edit
        if (this.application_id != null) {
            jo.put(application_idKey, this.application_id);
        }
        if (this.applicationName != null) {
            jo.put(applicationNameKey, this.applicationName);
        }
        if (this.type != null) {
            jo.put(typeKey, this.type);
        }
        if (this.saleType != null) {
            jo.put(saleTypeKey, this.saleType);
        }
        if (this.freePointType != null) {
            jo.put(freePointTypeKey, this.freePointType);
        }
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.partnerId != null) {
            jo.put(partnerIdKey, this.partnerId);
        }
        if (this.partnerName != null) {
            jo.put(partnerNameKey, this.partnerName);
        }
        if (this.point != null) {
            jo.put(pointKey, this.point);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.email != null) {
            jo.put(emailKey, this.email);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, this.cmCode);
        }
        if (this.ip != null) {
            jo.put(ipKey, ip);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, userType);
        }
        if (this.beforePoint != null) {
            jo.put(beforePointKey, this.beforePoint);
        }
        if (this.afterPoint != null) {
            jo.put(afterPointKey, this.afterPoint);
        }
        if (this.isAdmin != null) {
            jo.put(isAdminKey, this.isAdmin);
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
