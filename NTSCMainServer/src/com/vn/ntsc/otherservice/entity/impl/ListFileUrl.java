/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import com.vn.ntsc.otherservice.entity.IEntity;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    
    public HashMap<String, FileUrl> mapImg = new HashMap<>();
    public HashMap<String, FileUrl> mapGift = new HashMap<>();
    public HashMap<String, FileUrl> mapVideo = new HashMap<>();
    public HashMap<String, FileUrl> mapCover = new HashMap<>();
    public HashMap<String, FileUrl> mapAudio = new HashMap<>();
    public HashMap<String, FileUrl> mapStream = new HashMap<>();

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
    
    public static ListFileUrl fromJSONOject(JSONObject jo){
        ListFileUrl list = new ListFileUrl();
        JSONArray imgArr = (JSONArray) jo.get(listImgKey);
        if (imgArr != null){
            for (Object obj : imgArr){
                JSONObject img = (JSONObject) obj;
                String id = (String) img.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(img);
                list.mapImg.put(id, url);
            }
        }
        JSONArray giftArr = (JSONArray) jo.get(listGiftKey);
        if (giftArr != null){
            for (Object obj : giftArr){
                JSONObject gift = (JSONObject) obj;
                String id = (String) gift.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(gift);
                list.mapGift.put(id, url);
            }
        }
        JSONArray videoArr = (JSONArray) jo.get(listVideoKey);
        if (videoArr != null){
            for (Object obj : videoArr){
                JSONObject video = (JSONObject) obj;
                String id = (String) video.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(video);
                list.mapVideo.put(id, url);
            }
        }
        JSONArray coverArr = (JSONArray) jo.get(listCoverKey);
        if (coverArr != null){
            for (Object obj : coverArr){
                JSONObject cover = (JSONObject) obj;
                String id = (String) cover.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(cover);
                list.mapCover.put(id, url);
            }
        }
        JSONArray audioArr = (JSONArray) jo.get(listAudioKey);
        if (audioArr != null){
            for (Object obj : audioArr){
                JSONObject audio = (JSONObject) obj;
                String id = (String) audio.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(audio);
                list.mapAudio.put(id, url);
            }
        }
        JSONArray streamArr = (JSONArray) jo.get(listStreamKey);
        if (streamArr != null){
            for (Object obj : streamArr){
                JSONObject stream = (JSONObject) obj;
                String id = (String) stream.get(FileUrl.fileIdKey);
                FileUrl url = FileUrl.fromJSONOject(stream);
                list.mapStream.put(id, url);
            }
        }
        return list;
    }
}
