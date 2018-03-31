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
public class InstallationStatistic implements IEntity{
    
    private static final String timeKey = "time";
    public String time;
    
    private static final String androidInstallationTimesKey = "android_install_time";
    public Integer androidInstallationTimes;
    
    private static final String iosInstallationTimesKey = "ios_install_time";
    public Integer iosInstallationTimes;
    
    private static final String androidUniqueNumberKey = "android_unique_number";
    public Integer androidUniqueNumber;
    
    private static final String iosUniqueNumberKey = "ios_unique_number";
    public Integer iosUniqueNumber;
    
    private static final String blankAndroidUniqueNumberKey = "blank_android_unique_number";
    public Integer blankAndroidUniqueNumber;
    
    private static final String blankIosUniqueNumberKey = "blank_ios_unique_number";
    public Integer blankIosUniqueNumber;
    
    private static final String duplicateAndroidUniqueNumberKey = "duplicate_android_unique_number";
    public Integer duplicateAndroidUniqueNumber;
    
    private static final String duplicateIosUniqueNumberKey = "duplicate_ios_unique_number";
    public Integer duplicateIosUniqueNumber;
    
    
    
    
    private static final String totalTimeKey = "total";
    public Integer totalTime; 
    
    private static final String totalUniqueNumberKey = "total_unique_number";
    public Integer totalUniqueNumber; 

    public InstallationStatistic() {
        this.androidInstallationTimes = 0;
        this.iosInstallationTimes = 0;
        this.iosUniqueNumber = 0;
        this.androidUniqueNumber = 0;
        this.blankAndroidUniqueNumber = 0;
        this.blankIosUniqueNumber = 0;
        this.duplicateAndroidUniqueNumber = 0;
        this.duplicateIosUniqueNumber = 0;
    }    

//    public InstallationStatistic(String time, Integer androidInstallationTimes, Integer iosInstallationTimes) {
//        this.time = time;
//        this.androidInstallationTimes = androidInstallationTimes;
//        this.iosInstallationTimes = iosInstallationTimes;
//        this.totalTime = this.androidInstallationTimes + this.iosInstallationTimes;
//        
//    }
    public InstallationStatistic(String time, Integer androidInstallationTimes, Integer iosInstallationTimes,
            Integer androidUniqueNumber, Integer iosUniqueNumber) {
        this.time = time;
        this.androidInstallationTimes = androidInstallationTimes;
        this.iosInstallationTimes = iosInstallationTimes;
        this.iosUniqueNumber = iosUniqueNumber;
        this.androidUniqueNumber = androidUniqueNumber;
        this.totalUniqueNumber = this.androidUniqueNumber + this.iosUniqueNumber;
        this.totalTime = this.androidInstallationTimes + this.iosInstallationTimes;
        
    }
    
    
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.totalTime != null) {
            jo.put(totalTimeKey, this.totalTime);
        }
        if(this.iosInstallationTimes != null)
            jo.put(iosInstallationTimesKey, this.iosInstallationTimes);
        if(this.androidInstallationTimes != null)
            jo.put(androidInstallationTimesKey, this.androidInstallationTimes);
        
        if (this.totalUniqueNumber != null) {
            jo.put(totalUniqueNumberKey, this.totalUniqueNumber);
        }
        if(this.iosUniqueNumber != null)
            jo.put(iosUniqueNumberKey, this.iosUniqueNumber);
        if(this.androidUniqueNumber != null)
            jo.put(androidUniqueNumberKey, this.androidUniqueNumber);
        if(this.blankAndroidUniqueNumber != null)
            jo.put(blankAndroidUniqueNumberKey, this.blankAndroidUniqueNumber);
        if(this.blankIosUniqueNumber != null)
            jo.put(blankIosUniqueNumberKey, this.blankIosUniqueNumber);
        if(this.duplicateAndroidUniqueNumber != null)
            jo.put(duplicateAndroidUniqueNumberKey, this.duplicateAndroidUniqueNumber);
        if(this.duplicateIosUniqueNumber != null)
            jo.put(duplicateIosUniqueNumberKey, this.duplicateIosUniqueNumber);
        return jo;
    }

}
