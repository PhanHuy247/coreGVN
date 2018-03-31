/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

import java.util.ArrayList;
import java.util.Date;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.presentationserver.meetpeople.dao.impl.UserActivityDAO;

/**
 *
 * @author tuannxv00804
 */
public class InsertUser {

    public String api;
    public int showme;
    public String birthday;
    public int age;
    public double lon;
    public double lat;
    public boolean isOnline;
    public String email;
    public String username;
    public String avataID;
    public int location;
    public long registerTime;
    public int callWaiting;
    public boolean isVideo;
    public boolean isVoice;
    public long lastOnline;
    public int body_type;
    public int is_avatar;

    public InsertUser() {
    }

    public InsertUser(String s) throws Exception {
        try {
            JSONObject jo = (JSONObject) (new JSONParser().parse(s));
            api = (String) (jo.get(ParamKey.API_NAME));
            showme = ((Long) (jo.get(ParamKey.GENDER))).intValue();
            callWaiting = 1;
             if (jo.get("body_type") != null) {
                body_type = ((Long) (jo.get("body_type"))).intValue();
            } else {
                body_type = -1;

            }
            if (jo.get("is_avatar") != null) {
                is_avatar = ((Long) (jo.get("is_avatar"))).intValue();
            } else {
                is_avatar = -1;
            }
            Boolean videoCallWaiting = (Boolean) (jo.get(ParamKey.VIDEO_CALL_WAITING));
            Boolean voiceCallWaiting = (Boolean) (jo.get(ParamKey.VOICE_CALL_WAITING));
            isVideo = false;
            if (videoCallWaiting == null) {
                if (showme == Constant.GENDER.FEMALE) {
                    isVideo = true;
                }
            } else {
                isVideo = videoCallWaiting;
            }
            isVoice = false;
            if (voiceCallWaiting == null) {
                if (showme == Constant.GENDER.FEMALE) {
                    isVoice = true;
                }
            } else {
                isVoice = voiceCallWaiting;
            }
            if (!isVideo && !isVoice) {
                callWaiting = 0;
            }
            birthday = (String) (jo.get(ParamKey.BIRTHDAY));
            age = 0;
            email = (String) (jo.get(ParamKey.EMAIL));
            String regDate = (String) (jo.get(ParamKey.REGISTER_DATE));
            registerTime = DateFormat.parse(regDate).getTime();
//        Double LON = ((Double)(jo.get(ParamKey.LONGITUDE)));
//        if(LON != null) lon = LON;
//        else lon = 0;
//        Double LAT = ((Double)(jo.get(ParamKey.LATITUDE)));
//        if(LAT != null) lat = LAT;
//        else lat = 0;
            getLocation();
            Boolean ISONLINE = ((Boolean) (jo.get(ParamKey.IS_ONLINE)));
            if (ISONLINE != null) {
                isOnline = ISONLINE;
            } else {
                isOnline = false;
            }
            username = (String) (jo.get(ParamKey.USER_NAME));
            avataID = (String) (jo.get(ParamKey.AVATAR_ID));
            Long loc = (Long) (jo.get(ParamKey.REGION));
            location = 0;
            if (loc != null && loc != -1) {
                location = loc.intValue();
            }
            String lastLogin = (String) (jo.get(UserdbKey.USER.LAST_LOGIN_TIME));
            if (lastLogin != null) {
                lastOnline = DateFormat.parse(lastLogin).getTime();
            } else {
                lastOnline = registerTime;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public InsertUser(int showme, int callWaiting, int age, long registerTime, double lon, double lat, boolean isOnline, String email, String username, String avataID, int body_type, int is_avatar) {

        this.api = API.REGISTER;
        this.showme = showme;
        this.callWaiting = callWaiting;
        this.age = age;
        this.registerTime = registerTime;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = isOnline;
        this.email = email;
        this.username = username;
        this.avataID = avataID;
        this.body_type = body_type;
        this.is_avatar = is_avatar;
    }

    public InsertUser(int showme, int callWaiting, long registerTime, double lon, double lat, boolean isOnline, String email, String username, String avataID, int body_type, int is_avatar) {
        this.api = API.REGISTER;
        Date d = new Date();
        d.setYear(d.getYear() - 26);
        d.setDate(d.getDate() - 1);
        this.birthday = DateFormat.format(d).substring(0, 8);
        this.showme = showme;
        this.callWaiting = callWaiting;
        this.registerTime = registerTime;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = isOnline;
        this.email = email;
        this.username = username;
        this.avataID = avataID;
        this.body_type = body_type;
        this.is_avatar = is_avatar;
    }

    private void getLocation() {
        ArrayList<Double> locs = UserActivityDAO.getLocation(this.email);
        if (locs != null && locs.size() == 2) {
            this.lon = locs.get(0);
            this.lat = locs.get(1);
        } else {
            this.lon = 0;
            this.lat = 0;
        }
    }
}
