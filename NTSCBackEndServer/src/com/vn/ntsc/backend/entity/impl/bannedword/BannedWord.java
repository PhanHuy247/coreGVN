/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.bannedword;

import java.util.Date;
import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class BannedWord implements IEntity{
    private static final String idKey = "id";
    public String id;

    private static final String wordKey = "word";
    public String word;

    private static final String flagKey = "flag";
    public Integer flag; 
    
     private static final String dateTimeKey = "dateTịme";
    public Date dateTime;

    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.word != null)
            jo.put(wordKey, this.word);
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        if(this.dateTime != null)
            jo.put(dateTimeKey, this.dateTime);

        return jo;
    }   

    public BannedWord(String id, String word, Integer flag) {
        this.id = id;
        this.word = word;
        this.flag = flag;
        this.dateTime = dateTime;
    }

}
