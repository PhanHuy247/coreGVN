/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;
import java.util.List;

/**
 *
 * @author RuAc0n
 */
public class AddBuzzData implements IEntity {

    private static final String buzzIdKey = "buzz_id";
    public String buzz_id;
    
    private static final String commentIdKey = "cmt_id";
    public String commentId;

    private static final String timeKey = "time";
    public Long time;

    private static final String isAppKey = "is_app";
    public Integer isApp;
    
    private static final String commentAppKey = "comment_app";
    public Integer commentApp;
    
    private static final String imgListKey = "img_list";
    public List<String> imgList;
    
    private static final String vidListKey = "vid_list";
    public List<String> vidList;


    public AddBuzzData(String buzzId, String commentId,  Long time, Integer isApp, Integer commentApp) {
        this.commentId = commentId;
        this.buzz_id = buzzId;
        this.isApp = isApp;
        this.time = time;
        this.commentApp = commentApp;
    }
    
public AddBuzzData(String buzzId, String commentId,  Long time, Integer isApp, Integer commentApp, List<String> listImg, List<String> listVid) {
        this.commentId = commentId;
        this.buzz_id = buzzId;
        this.isApp = isApp;
        this.time = time;
        this.commentApp = commentApp;
        this.imgList = listImg;
        this.vidList = listVid;
    }


    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (buzz_id != null) {
            jo.put(buzzIdKey, buzz_id);
        }
        if (commentId != null) {
            jo.put(commentIdKey, commentId);
        }
        if (isApp != null) {
            jo.put(isAppKey, isApp);
        }
        if (commentApp != null) {
            jo.put(commentAppKey, commentApp);
        }
        if (time != null) {
            jo.put(timeKey, time);
        }
        if (imgList != null){
            jo.put(imgListKey, imgList);
        }
        if (vidList != null){
            jo.put(vidListKey, vidList);
        }
        return jo;
    }
}
