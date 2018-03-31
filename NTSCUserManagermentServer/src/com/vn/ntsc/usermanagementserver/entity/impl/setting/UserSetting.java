/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.setting;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Admin
 */
public class UserSetting implements IEntity{

    private static final String saveImagePointKey = "save_img_pnt"; //
    public Integer saveImagePoint;

    private static final String onlineAlertPointKey = "onl_alt_pnt"; //out
    public Integer onlineAlertPoint;

    private static final String daylyBonusKey = "day_bns_pnt"; //out
    public Integer daylyBonus;

    private static final String regPointKey = "reg_pnt"; //out
    public Integer regPoint;

    private static final String winkPointKey = "wink_pnt"; //out
    public Integer winkPoint;

    private static final String chatPointKey = "chat_pnt"; //out
    public Integer chatPoint;
    private static final String videoCallPointKey = "video_call_pnt"; //out
    public Integer videoCallPoint;

    private static final String voiceCallPointKey = "voide_call_pnt"; //out
    public Integer voiceCallPoint;

    private static final String backstageTimeKey = "bckstg_time"; //out
    public Integer backstageTime;
    
    private static final String bckstgPriKey = "bckstg_pri";
    public Integer bckstg_pri;
    
    private static final String backstageBonusKey = "bckstg_bonus";
    public Integer backstageBonus;    
    
    public Double near;  
        
    public Double city;  
    
    public Double country;  
    
    private static final String commentBuzzPointKey = "comment_buzz_pnt"; //
    public Integer commentBuzzPoint;
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        if (isNotNull(this.backstageTime)) {
            map.put(backstageTimeKey, this.backstageTime);
        }

        if (isNotNull(this.chatPoint)) {
            map.put(chatPointKey, this.chatPoint);
        }

        if (isNotNull(this.daylyBonus)) {
            map.put(daylyBonusKey, this.daylyBonus);
        }

        if (isNotNull(this.onlineAlertPoint)) {
            map.put(onlineAlertPointKey, this.onlineAlertPoint);
        }

        if (isNotNull(this.regPoint)) {
            map.put(regPointKey, this.regPoint);
        }

        if (isNotNull(this.saveImagePoint)) {
            map.put(saveImagePointKey, this.saveImagePoint);
        }

        if (isNotNull(this.videoCallPoint)) {
            map.put(videoCallPointKey, this.videoCallPoint);
        }
        
        if (isNotNull(this.voiceCallPoint)) {
            map.put(voiceCallPointKey, this.voiceCallPoint);
        }

        if (isNotNull(this.winkPoint)) {
            map.put(winkPointKey, this.winkPoint);
        }
        
        if (isNotNull(this.bckstg_pri)) {
            map.put(bckstgPriKey, this.bckstg_pri);
        }
        
        if (isNotNull(this.commentBuzzPoint)) {
            map.put(commentBuzzPointKey, this.commentBuzzPoint);
        }
        
        if (isNotNull(this.backstageBonus)) {
            map.put(backstageBonusKey, this.backstageBonus);
        } 
        if(isNotNull(this.near))
            map.put("near", this.near);
        if(isNotNull(this.city))
            map.put("city", this.city);
        if(isNotNull(this.country))
            map.put("country", this.country);

        return map;
    }

    private boolean isNotNull(Object obj) {
        return obj != null;
    }

    @Override
    public JSONObject toJsonObject() {
        
        JSONObject obj = new JSONObject();
        if (isNotNull(this.backstageTime)) {
            obj.put(backstageTimeKey, this.backstageTime);
        }

        if (isNotNull(this.chatPoint)) {
            obj.put(chatPointKey, this.chatPoint);
        }

        if (isNotNull(this.daylyBonus)) {
            obj.put(daylyBonusKey, this.daylyBonus);
        }

        if (isNotNull(this.onlineAlertPoint)) {
            obj.put(onlineAlertPointKey, this.onlineAlertPoint);
        }

        if (isNotNull(this.regPoint)) {
            obj.put(regPointKey, this.regPoint);
        }

        if (isNotNull(this.saveImagePoint)) {
            obj.put(saveImagePointKey, this.saveImagePoint);
        }

        if (isNotNull(this.videoCallPoint)) {
            obj.put(videoCallPointKey, this.videoCallPoint);
        }
        
        if (isNotNull(this.voiceCallPoint)) {
            obj.put(voiceCallPointKey, this.voiceCallPoint);
        }

        if (isNotNull(this.winkPoint)) {
            obj.put(winkPointKey, this.winkPoint);
        }
        
        if (isNotNull(this.bckstg_pri)) {
            obj.put(bckstgPriKey, this.bckstg_pri);
        }
        
        if (isNotNull(this.commentBuzzPoint)) {
            obj.put(commentBuzzPointKey, this.commentBuzzPoint);
        }
        
        if (isNotNull(this.backstageBonus)) {
            obj.put(backstageBonusKey, this.backstageBonus);
        } 
        if(isNotNull(this.near))
            obj.put("near", this.near);
        if(isNotNull(this.city))
            obj.put("city", this.city);
        if(isNotNull(this.country))
            obj.put("country", this.country);

        return obj;
    }

}
