/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class CheckUnlockBackstageData implements IEntity {

    private static final String isUnlckKey = "is_unlck";
    public Long is_unlck;

    private static final String pointKey = "point";
    public Long point;

    private static final String bckstgRateKey = "bckstg_rate";
    public Double bckstg_rate;

    private static final String bckstgNumKey = "bckstg_num";
    public Long bckstg_num;

    private static final String userRateNumKey = "user_rate_num";
    public Integer user_rate_num;

    private static final String ratePointKey = "rate_point";
    public Integer ratePoint;

    private static final String bckstgPriKey = "bckstg_pri";
    public Integer bckstg_pri;
    
    private static final String backstageBonusKey = "bckstg_bonus";
    public Integer backstageBonus;     
    

    public CheckUnlockBackstageData(Long is_unlck, Long point, Double bckstg_rate, Long bckstg_num, Integer user_rate_num, Integer ratePoint, Integer backstagePrice, Integer backstageBonus) {
        this.is_unlck = is_unlck;
        if (point != null) {
            this.point = point;
        } else {
            this.point = new Long(0);
        }
        this.bckstg_rate = bckstg_rate;
        this.bckstg_num = bckstg_num;
        this.user_rate_num = user_rate_num;
        this.ratePoint = ratePoint;
        this.bckstg_pri = backstagePrice;
        this.backstageBonus = backstageBonus;
    }
    
    public CheckUnlockBackstageData(Long is_unlck, Long point, Double bckstg_rate, Long bckstg_num, Integer user_rate_num, Integer ratePoint) {
        this.is_unlck = is_unlck;
        if (point != null) {
            this.point = point;
        } else {
            this.point = new Long(0);
        }
        this.bckstg_rate = bckstg_rate;
        this.bckstg_num = bckstg_num;
        this.user_rate_num = user_rate_num;
        this.ratePoint = ratePoint;
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.is_unlck != null) {
            jo.put(isUnlckKey, this.is_unlck);
        }
        if (this.bckstg_rate != null) {
            jo.put(bckstgRateKey, bckstg_rate);
        }
        if (this.bckstg_num != null) {
            jo.put(bckstgNumKey, bckstg_num);
        }
        if (this.user_rate_num != null) {
            jo.put(userRateNumKey, user_rate_num);
        }
        if (this.ratePoint != null) {
            jo.put(ratePointKey, ratePoint);
        }
        if (this.point != null) {
            jo.put(pointKey, point);
        }
        if (this.bckstg_pri != null) {
            jo.put(bckstgPriKey, this.bckstg_pri);
        }
        
        if (this.backstageBonus != null) {
            jo.put(backstageBonusKey, this.backstageBonus);
        } 
        return jo;
    }
}
