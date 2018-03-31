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
public class GetConnectionPointActionData implements IEntity {

    private static final String saveImagePointKey = "save_image_point";
    public Integer saveImagePoint;
    
    private static final String chatPointKey = "chat_point";
    public Integer chatPoint;
    
    private static final String unlockBackstageKey = "unlock_backstage_point";
    public Integer unlockBackstagePoint;
    
    private static final String unlockBackstageBonusKey = "unlock_backstage_bonus_point";
    public Integer unlockBackstageBonusPoint;
    
    private static final String commentPointKey = "comment_point";
    public Integer commentPoint;

    public GetConnectionPointActionData(Integer saveImagePoint, Integer chatPoint, Integer unlockBackstagePoint,
                                Integer unlockBackstageBonusPoint, Integer commentPoint) {
        this.saveImagePoint = saveImagePoint;
        this.chatPoint = chatPoint;
        this.unlockBackstagePoint = unlockBackstagePoint;
        this.unlockBackstageBonusPoint = unlockBackstageBonusPoint;
        this.commentPoint = commentPoint;
    }

    public GetConnectionPointActionData(){
        this.saveImagePoint = 0;
        this.chatPoint = 0;
        this.unlockBackstagePoint = 0;
        this.unlockBackstageBonusPoint = 0;
        this.commentPoint = 0;
    }
    
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if(saveImagePoint != null){
            jo.put(saveImagePointKey, saveImagePoint);
        }
        if(chatPoint != null){
            jo.put(chatPointKey, chatPoint);
        }
        if(unlockBackstagePoint != null){
            jo.put(unlockBackstageKey, unlockBackstagePoint);
        }
        if(unlockBackstageBonusPoint != null){
            jo.put(unlockBackstageBonusKey, unlockBackstageBonusPoint);
        }
        if(commentPoint != null){
            jo.put(commentPointKey, commentPoint);
        }
        return jo;
    }
}
