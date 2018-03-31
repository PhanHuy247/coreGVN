/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

import java.util.Date;

/**
 *
 * @author RuAc0n
 */
public class LogPoint {

    public String userId;
    public int type;
    public String partnerId;
    public int point;
    public Date time;
    public String ip;
    public int afterPoint;
    public int beforePoin;
    public Integer saleType;
    public Integer freePointType;

    public LogPoint(String userId, int type, String partnerId, Date time, String ip, PointAction point) {
        this.userId = userId;
        this.type = type;
        this.partnerId = partnerId;
        this.point = point.point;
        this.time = time;
        this.ip = ip;
        this.beforePoin = point.beforePoint;
        this.afterPoint = point.afterPoint;
    }
    
    public LogPoint(String userId, int type, Long saleTye, Long freePointTye , Date time, String ip, PointAction point) {
        this.userId = userId;
        this.type = type;
        if(saleTye != null)
            saleType = saleTye.intValue();
        if(freePointTye != null)
            freePointType = freePointTye.intValue();
        this.point = point.point;
        this.time = time;
        this.ip = ip;
        this.beforePoin = point.beforePoint;
        this.afterPoint = point.afterPoint;
    }    
    
}
