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
public class LogPurchase extends OneObjectLog implements IEntity {

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();

    private static final String timeKey = "time";
    @TypeSwitch(value = ParamKey.TIME, header = Headers.purchase_time)
    public String time;

    private static final String pointKey = "point";
    @TypeSwitch(header = Headers.points)
    public Integer point;

    private static final String priceKey = "price";
    @TypeSwitch(header = Headers.price)
    public Double price;

    private static final String totalPriceKey = "total_price";
    @TypeSwitch(header = Headers.total_price)
    public Double totalPrice;

    private static final String successKey = "success";
    @TypeSwitch(value = ParamKey.SUCCESS, header = Headers.success)
    public Integer success;

    private static final String purchaseTypeKey = "purchase_type";
    @TypeSwitch(value = ParamKey.PURCHASE_TYPE, header = Headers.purchase_type)
    public Integer purchaseType;
    //thanhdd add
    private static final String addByAdminKey = "add_by_admin";
    @TypeSwitch(value = ParamKey.ADD_BY_ADMIN, header = Headers.add_by_admin)
    public Integer addbyadmin;
//     private static final String productionTypeKey = "production_type";
//    @TypeSwitch(value = ParamKey.PRODUCTION_TYPE, header = Headers.add_by_admin)
    private static final String productionTypeKey = "production_type";
    @TypeSwitch(value = ParamKey.PRODUCTION_TYPE, header = Headers.production_type)
    public Integer production_type;

    private static final String paymentMethodKey = "payment_method";
    @TypeSwitch(value = ParamKey.PAYMENT_METHOD, header = Headers.payment_method)
    public Integer payment_method;

    //end
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
        //success type
        value = new JSONObject();
        value.putAll(TypeValue.en_success_type);
        jsonEnglishType.put(ParamKey.SUCCESS, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_success_type);
        jsonJapaneseType.put(ParamKey.SUCCESS, value);
        //purchase type
        value = new JSONObject();
        value.putAll(TypeValue.en_purchase_type);
        jsonEnglishType.put(ParamKey.PURCHASE_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_purchase_type);
        jsonJapaneseType.put(ParamKey.PURCHASE_TYPE, value);
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
        keys.add(Headers.purchase_time);
        keys.add(Headers.purchase_type);
        keys.add(Headers.points);
        keys.add(Headers.price);
        keys.add(Headers.success);
        keys.add(Headers.total_price);
        keys.add(Headers.add_by_admin);
        keys.add(Headers.production_type);
        keys.add(Headers.payment_method);
        headers.addAll(keys);
//        headers = keys;

        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);

    }

    public LogPurchase() {
    }

    @Override
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

        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.email != null) {
            jo.put(emailKey, email);
        }
        if (this.cmCode != null) {
            jo.put(cmCodeKey, cmCode);
        }
        if (this.ip != null) {
            jo.put(ipKey, ip);
        }
        if (this.userType != null) {
            jo.put(userTypeKey, userType);
        }
        if (this.point != null) {
            jo.put(pointKey, point);
        }
        if (this.price != null) {
            jo.put(priceKey, price);
        }
        if (this.totalPrice != null) {
            jo.put(totalPriceKey, totalPrice);
        }
        if (this.success != null) {
            jo.put(successKey, success);
        }
        //thanhdd add
        if (this.purchaseType != null) {
            jo.put(purchaseTypeKey, purchaseType);
        }
        if (this.addbyadmin != null) {
            jo.put(addByAdminKey, addbyadmin);
        }
        if (this.production_type != null) {
            jo.put(productionTypeKey, production_type);
        }
        if (this.payment_method != null) {
            jo.put(paymentMethodKey, payment_method);
        }
        //end
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
