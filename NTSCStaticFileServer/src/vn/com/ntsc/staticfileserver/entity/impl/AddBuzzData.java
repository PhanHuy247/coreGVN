/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author hoangnh
 */
public class AddBuzzData implements IEntity{
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


    public AddBuzzData(String buzzId, String commentId,  Long time, Integer isApp, Integer commentApp) {
        this.commentId = commentId;
        this.buzz_id = buzzId;
        this.isApp = isApp;
        this.time = time;
        this.commentApp = commentApp;
    }
    
    public AddBuzzData(String buzzId, String commentId,  Long time, Integer isApp, Integer commentApp, List<String> listImg) {
        this.commentId = commentId;
        this.buzz_id = buzzId;
        this.isApp = isApp;
        this.time = time;
        this.commentApp = commentApp;
        this.imgList = listImg;
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
        return jo;
    }
}
