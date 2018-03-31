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
 * @author hoangnh
 */
public class ListFileData implements IEntity{
    public static final String listImgKey = "list_img";
    public List<FileData> listImg;
    
    public static final String listVideoKey = "list_video";
    public List<FileData> listVideo;
    
    public static final String listCoverKey = "list_cover";
    public List<FileData> listCover;
    
    public static final String listAudioKey = "list_audio";
    public List<FileData> listAudio;
    
    public static final String listGiftKey = "list_gift";
    public List<FileData> listGift;
    
    public static final String listStreamKey = "list_stream";
    public List<FileData> listStream;
    
    public ListFileData(){
        
    }
    
    public ListFileData(List<FileData> listImg, List<FileData> listVideo, List<FileData> listAudio){
        this.listImg = listImg;
        this.listVideo = listVideo;
        this.listAudio = listAudio;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        if (listImg != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listImg){
                arr.add(url.toJsonObject());
            }
            jo.put(listImgKey, arr);
        }
        if (listVideo != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listVideo){
                arr.add(url.toJsonObject());
            }
            jo.put(listVideoKey, arr);
        }
        if (listCover != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listCover){
                arr.add(url.toJsonObject());
            }
            jo.put(listCoverKey, arr);
        }
        if (listAudio != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listAudio){
                arr.add(url.toJsonObject());
            }
            jo.put(listAudioKey, arr);
        }
        if (listGift != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listGift){
                arr.add(url.toJsonObject());
            }
            jo.put(listGiftKey, arr);
        }
        if (listStream != null){
            JSONArray arr = new JSONArray();
            for (FileData url : listStream){
                arr.add(url.toJsonObject());
            }
            jo.put(listStreamKey, arr);
        }
        return jo;
    }
    
}
