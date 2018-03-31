/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import com.vn.ntsc.otherservice.entity.IEntity;
import eazycommon.constant.ParamKey;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    
    public HashMap<String, FileData> mapImg = new HashMap<>();
    public HashMap<String, FileData> mapVideo = new HashMap<>();
    public HashMap<String, FileData> mapCover = new HashMap<>();
    public HashMap<String, FileData> mapAudio = new HashMap<>();
    public HashMap<String, FileData> mapGift = new HashMap<>();
    public HashMap<String, FileData> mapStream = new HashMap<>();

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
    
    public static ListFileData fromJSONOject(JSONObject jo){
        ListFileData list = new ListFileData();
        JSONArray imgArr = (JSONArray) jo.get(listImgKey);
        if (imgArr != null){
            for (Object obj : imgArr){
                JSONObject img = (JSONObject) obj;
                String id = (String) img.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(img);
                list.mapImg.put(id, url);
            }
        }
        JSONArray videoArr = (JSONArray) jo.get(listVideoKey);
        if (videoArr != null){
            for (Object obj : videoArr){
                JSONObject video = (JSONObject) obj;
                String id = (String) video.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(video);
                list.mapVideo.put(id, url);
            }
        }
        JSONArray coverArr = (JSONArray) jo.get(listCoverKey);
        if (coverArr != null){
            for (Object obj : coverArr){
                JSONObject cover = (JSONObject) obj;
                String id = (String) cover.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(cover);
                list.mapCover.put(id, url);
            }
        }
        JSONArray audioArr = (JSONArray) jo.get(listAudioKey);
        if (audioArr != null){
            for (Object obj : audioArr){
                JSONObject audio = (JSONObject) obj;
                String id = (String) audio.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(audio);
                list.mapAudio.put(id, url);
            }
        }
        JSONArray giftArr = (JSONArray) jo.get(listGiftKey);
        if (giftArr != null){
            for (Object obj : giftArr){
                JSONObject gift = (JSONObject) obj;
                String id = (String) gift.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(gift);
                list.mapGift.put(id, url);
            }
        }
        JSONArray streamArr = (JSONArray) jo.get(listStreamKey);
        if (streamArr != null){
            for (Object obj : streamArr){
                JSONObject stream = (JSONObject) obj;
                String id = (String) stream.get(ParamKey.FILE_ID);
                FileData url = FileData.fromJSONOject(stream);
                list.mapStream.put(id, url);
            }
        }
        return list;
    }
    
}
