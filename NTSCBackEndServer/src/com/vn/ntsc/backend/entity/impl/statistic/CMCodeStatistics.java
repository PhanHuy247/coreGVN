/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.statistic;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class CMCodeStatistics implements IEntity{
//    private static final String timeKey = "time";
//    public String time;

    private static final String cmCodeKey = "cm_code";
    public String cmCode;    
    
    private static final String androidRegTimesKey = "android_reg_time";
    public Integer androidRegTimes;

    private static final String androidLoginTimesKey = "android_login_time";
    public Integer androidLoginTimes;    
    
    private static final String androidPurchaseTimesKey = "android_pur_times";
    public Integer androidPurchaseTimes;
    
    private static final String androidPurchaseMoneyKey = "android_pur_money";
    public Double androidPurchaseMoney;
    
    private static final String iosRegTimesKey = "ios_reg_time";
    public Integer iosRegTimes;

    private static final String iosLoginTimesKey = "ios_login_time";
    public Integer iosLoginTimes;    
    
    private static final String iosPurchaseTimesKey = "ios_pur_times";
    public Integer iosPurchaseTimes;
    
    private static final String iosPurchaseMoneyKey = "ios_pur_money";
    public Double iosPurchaseMoney;
    
    public InstallationStatistic installationStatistic;    

    public CMCodeStatistics() {
        this.cmCode = null;
        this.androidLoginTimes = 0;
        this.androidPurchaseMoney = 0.0;
        this.androidPurchaseTimes = 0;
        this.androidRegTimes = 0;
        this.iosLoginTimes = 0;
        this.iosPurchaseMoney = 0.0;
        this.iosPurchaseTimes = 0;
        this.iosRegTimes = 0;
        installationStatistic = new InstallationStatistic();
    }    
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

//        if(this.time != null)
//            jo.put(timeKey, this.time);
        if(this.androidRegTimes != null)
            jo.put(androidRegTimesKey, this.androidRegTimes);
        if(this.androidLoginTimes != null)
            jo.put(androidLoginTimesKey, this.androidLoginTimes);
        if(this.androidPurchaseTimes != null)
            jo.put(androidPurchaseTimesKey, this.androidPurchaseTimes);
        if(this.androidPurchaseMoney != null)
            jo.put(androidPurchaseMoneyKey, this.androidPurchaseMoney);
        if(this.iosRegTimes != null)
            jo.put(iosRegTimesKey, this.iosRegTimes);
        if(this.iosLoginTimes != null)
            jo.put(iosLoginTimesKey, this.iosLoginTimes);
        if(this.iosPurchaseTimes != null)
            jo.put(iosPurchaseTimesKey, this.iosPurchaseTimes);
        if(this.iosPurchaseMoney != null)
            jo.put(iosPurchaseMoneyKey, this.iosPurchaseMoney);
        if(this.cmCode != null)
            jo.put(cmCodeKey, this.cmCode);
        if(this.installationStatistic != null){
            jo.putAll(this.installationStatistic.toJsonObject());
        }        
        return jo;
    }

}
