/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.entity.impl;

import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class PointStatistics implements IEntity{
    private static final String timeKey = "time";
    public String time;

    private static final String malePointKey = "male_point";
    public Integer malePoint;

    private static final String femalePointKey = "female_point";
    public Integer femalePoint;    
    
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.time != null)
            jo.put(timeKey, this.time);
        if(this.malePoint != null)
            jo.put(malePointKey, this.malePoint);
        if(this.femalePoint != null)
            jo.put(femalePointKey, this.femalePoint);
        
        return jo;
    }

}
