/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.userinformanager;

/**
 *
 * @author RuAc0n
 */
public class UserInfor {

    public String userId;
    public int point;
    public Integer gender;
    public boolean havePurchased;
    public String applicationId;

    public UserInfor(String userId, int point, Integer gender, boolean havePurchased) {
        this.userId = userId;
        this.point = point;
        this.gender = gender;
        this.havePurchased = havePurchased;
    }
    public UserInfor(String userId, int point, int gender, boolean havePurchased,String applicationId) {
        this.userId = userId;
        this.point = point;
        this.gender = gender;
        this.havePurchased = havePurchased;
        this.applicationId = applicationId;
    }

    UserInfor(String userId, int point) {
        this.userId = userId;
        this.point = point;
        this.havePurchased = false;
    }

    public int getPoint() {
        return point;
    }

    public int getGender() {
        return gender;
    }
}
