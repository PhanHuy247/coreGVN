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
import com.vn.ntsc.usermanagementserver.entity.impl.setting.UserSetting;

/**
 *
 * @author Admin
 */
public class LoginData implements IEntity {

    public User user;
    private static final String notiNumKey = "noti_num"; //out
    public Integer notiNum;
    private static final String checkoutNumKey = "checkout_num";  //out
    public Integer checkoutNum;
    private static final String myFootprintNumberKey = "my_footprint_num";
    public Integer myFootprintNumber;
    private static final String backListKey = "backlst";//
    public List<String> blackList;
    private static final String homePageUrlKey = "home_page_url"; //out
    public String homePageUrl;
    private static final String isNotiKey = "is_noti"; //out
    public Long isNoti;
    private static final String isVerifyKey = "is_verify"; //out
    public boolean isVerify;
    private static final String addPointKey = "add_point";
    public Integer addPoint;
    public UserSetting setting;
    private static final String notiLikeNumberKey = "noti_like_num";
    public Long notiLikeNumber;

    private static final String notiNewsNumberKey = "noti_news_num";
    public Long notiNewsNumber;

    private static final String notiQANumberKey = "noti_qa_num";
    public Long notiQANumber;
    
    public JSONObject toJsonObject() {
        JSONObject jo = user.toJsonObject();
        if (setting != null) {
            jo.putAll(setting.toMap());
        }
        if (blackList != null) {
            jo.put(backListKey, blackList);
        }
        if (checkoutNum != null) {
            jo.put(checkoutNumKey, checkoutNum);
        }
        if (myFootprintNumber != null) {
            jo.put(myFootprintNumberKey, myFootprintNumber);
        }
        if (notiNumKey != null) {
            jo.put(notiNumKey, notiNum);
        }
        if (homePageUrl != null) {
            jo.put(homePageUrlKey, homePageUrl);
        }
        if (this.isNoti != null) {
            jo.put(isNotiKey, isNoti);
        }
        if (this.addPoint != null) {
            jo.put(addPointKey, addPoint);
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
        jo.put(isVerifyKey, isVerify);
        return jo;
    }

}
