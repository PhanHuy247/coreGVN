/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;

/**
 *
 * @author Administrator
 */
public class ListFileUrl implements IEntity {
    public static final String listImgKey = "list_img";
    public List<FileUrl> listImg;
    
    public static final String listGiftKey = "list_gift";
    public List<FileUrl> listGift;
    
    public static final String listVideoKey = "list_video";
    public List<FileUrl> listVideo;
    
    public static final String listCoverKey = "list_cover";
    public List<FileUrl> listCover;
    
    public static final String listAudioKey = "list_audio";
    public List<FileUrl> listAudio;
    
    public static final String listStreamKey = "list_stream";
    public List<FileUrl> listStream;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (listImg != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listImg){
                arr.add(url.toJsonObject());
            }
            jo.put(listImgKey, arr);
        }
        if (listGift != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listGift){
                arr.add(url.toJsonObject());
            }
            jo.put(listGiftKey, arr);
        }
        if (listVideo != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listVideo){
                arr.add(url.toJsonObject());
            }
            jo.put(listVideoKey, arr);
        }
        if (listCover != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listCover){
                arr.add(url.toJsonObject());
            }
            jo.put(listCoverKey, arr);
        }
        if (listAudio != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listAudio){
                arr.add(url.toJsonObject());
            }
            jo.put(listAudioKey, arr);
        }
        if (listStream != null){
            JSONArray arr = new JSONArray();
            for (FileUrl url : listStream){
                arr.add(url.toJsonObject());
            }
            jo.put(listStreamKey, arr);
        }
        return jo;
    }
    
}
