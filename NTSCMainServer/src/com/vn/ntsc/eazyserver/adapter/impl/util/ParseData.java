/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.util;

import com.vn.ntsc.otherservice.entity.impl.FileData;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.User;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ParseData {
    
    public static JSONObject parseShareBuzz(JSONObject shareObject){
        JSONObject result = new JSONObject();
        if(shareObject != null && !shareObject.isEmpty()){
            JSONArray childList = (JSONArray) shareObject.get(ParamKey.LIST_CHILD);
            String userId = (String) shareObject.get(ParamKey.USER_ID);
            String avaId = "";

            List<String> llEmail = new ArrayList<>();
            llEmail.add(userId);
            
            JSONArray tagArr = (JSONArray) shareObject.get(ParamKey.TAG_LIST);
            if (tagArr != null && !tagArr.isEmpty()) {
                for (Object item : tagArr) {
                    JSONObject tag = (JSONObject) item;
                    String tagUserId = (String) tag.get(ParamKey.USER_ID);
                    llEmail.add(tagUserId);
                }
            }

            JSONArray psArr = InterCommunicator.getUserPresentList(null, llEmail);
            Map<String, User> mUserInfo = ParseData.parseUserInfo(psArr);
            List<String> tagImgList = ParseData.addAvataId(psArr);
            
            User userInfo = mUserInfo.get(userId);
            if(userInfo != null){
                shareObject.put(ParamKey.USER_NAME, userInfo.username);
                shareObject.put(ParamKey.REGION, userInfo.region);
                shareObject.put(ParamKey.GENDER, userInfo.gender);
                avaId = userInfo.avatarId;
            }
            
            shareObject.put(ParamKey.BUZZ_VALUE, shareObject.get(ParamKey.BUZZ_VALUE) == null ? "": shareObject.get(ParamKey.BUZZ_VALUE));
            shareObject.put(ParamKey.BUZZ_REGION, shareObject.get(ParamKey.BUZZ_REGION)==null?0:shareObject.get(ParamKey.BUZZ_REGION));
            
            if(childList.isEmpty()){
                JSONObject item = new JSONObject();
                item.put(ParamKey.BUZZ_ID, (String) shareObject.get(ParamKey.BUZZ_ID));
                item.put(ParamKey.BUZZ_TYPE, (Long) shareObject.get(ParamKey.BUZZ_TYPE));
                item.put(ParamKey.FILE_ID, (String) shareObject.get(ParamKey.FILE_ID));
                item.put(ParamKey.COVER_ID, (String) shareObject.get(ParamKey.COVER_ID));
                item.put(ParamKey.COMMENT_NUM, (Long) shareObject.get(ParamKey.COMMENT_NUM));
                item.put(ParamKey.SUB_COMMENT_NUM, (Long) shareObject.get(ParamKey.SUB_COMMENT_NUM));
                Integer viewNumber = 0;
                if(shareObject.get(ParamKey.VIEW_NUMBER) != null){
                    viewNumber = ((Long) shareObject.get(ParamKey.VIEW_NUMBER)).intValue();
                }
                item.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
                childList.add(item);
            }

            ListFileData map = getFileData(childList, avaId, tagImgList);

            for (int i = 0; i < childList.size(); i++) {
                JSONObject item = (JSONObject) childList.get(i);
                String fileId = (String) item.get(ParamKey.FILE_ID);
                String coverId = (String) item.get(ParamKey.COVER_ID);
                Long buzzType = (Long) item.get(ParamKey.BUZZ_TYPE);

                switch(buzzType.intValue()){
                    case Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS:
                        item.put(ParamKey.BUZZ_TYPE, "image");
                        FileData imgData = map.mapImg.get(fileId);
                        if (imgData != null){
                            addImageInfo(item, imgData);
                        }
                        break;
                    case Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS:
                        item.put(ParamKey.BUZZ_TYPE, "video");
                        FileData videoData = map.mapVideo.get(fileId);
                        if (videoData != null){
                            addVideoInfo(item, videoData);
                        }
                        break;
                    case Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS:
                        item.put(ParamKey.BUZZ_TYPE, "audio");
                        FileData coverData = map.mapCover.get(coverId);
                        FileData audioData = map.mapAudio.get(fileId);
                        if (audioData != null){
                            addAudioInfo(item, audioData, coverData);
                        }
                        break;
                    case Constant.BUZZ_TYPE_VALUE.STREAM_STATUS:
                        item.put(ParamKey.BUZZ_TYPE, "stream");
                        FileData streamData = map.mapStream.get(fileId);
                        if (streamData != null){
                            addVideoInfo(item, streamData);
                        }
                        
                        if(Constant.STREAM_STATUS.ON.equals(shareObject.get(ParamKey.STREAM_STATUS)) || shareObject.get(ParamKey.STREAM_STATUS) == null){
                            item.put(ParamKey.STREAM_ID, shareObject.get(ParamKey.STREAM_ID));  
                            item.put(ParamKey.STREAM_END_TIME, -1);
                            Integer currentView = 0;
                            if(shareObject.get(ParamKey.CURRENT_VIEW) != null){
                                currentView = ((Long) shareObject.get(ParamKey.CURRENT_VIEW)).intValue();
                            }
                            item.put(ParamKey.CURRENT_VIEW,  currentView == -1 ? 0 : currentView);
                            Integer duration = 0;
                            if(shareObject.get(ParamKey.DURATION) != null){
                                duration = ((Long) shareObject.get(ParamKey.DURATION)).intValue();
                            }
                            item.put(ParamKey.DURATION, duration == -1 ? 0 : duration);
                            
                            
                            item.put(ParamKey.STREAM_DURATION, caculateStreamTime(shareObject.get(ParamKey.STREAM_START_TIME).toString()));
                            //item.put(ParamKey.CURRENT_VIEW, shareObject.get(ParamKey.CURRENT_VIEW) == null ? 0 : shareObject.get(ParamKey.CURRENT_VIEW));
                            //item.put(ParamKey.DURATION, shareObject.get(ParamKey.DURATION) == null ? 0 : shareObject.get(ParamKey.DURATION));
                        }else{
                            item.put(ParamKey.STREAM_END_TIME, shareObject.get(ParamKey.STREAM_END_TIME));
                            item.put(ParamKey.CURRENT_VIEW, 0);
                            item.put(ParamKey.DURATION, 0);
                            
                            if(shareObject.get(ParamKey.STREAM_END_TIME) != null){
                                long streamDuration = caculateStreamTime(shareObject.get(ParamKey.STREAM_START_TIME).toString(), shareObject.get(ParamKey.STREAM_END_TIME).toString());
                                item.put(ParamKey.STREAM_DURATION, streamDuration);
                            }else{
                                item.put(ParamKey.STREAM_DURATION, 0);
                            }
                        }
                        Integer viewNumber = 0;
                        if(shareObject.get(ParamKey.VIEW_NUMBER) != null){
                            viewNumber = ((Long) shareObject.get(ParamKey.VIEW_NUMBER)).intValue();
                        }
                        item.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
                        if(shareObject.get(ParamKey.STREAM_STATUS) == null){
                            item.put(ParamKey.STREAM_STATUS, Constant.STREAM_STATUS.ON);
                        }else{
                            item.put(ParamKey.STREAM_STATUS, shareObject.get(ParamKey.STREAM_STATUS));
                        }
                        item.put(ParamKey.STREAM_START_TIME, shareObject.get(ParamKey.STREAM_START_TIME));
                        item.put(ParamKey.BUZZ_ID, shareObject.get(ParamKey.BUZZ_ID));
                        item.put(ParamKey.BUZZ_TYPE, "stream");
                        item.put(ParamKey.COMMENT_NUM, shareObject.get(ParamKey.COMMENT_NUM));
                        item.put(ParamKey.BUZZ_TIME, shareObject.get(ParamKey.BUZZ_TIME));
                        item.put(ParamKey.USER_ID, shareObject.get(ParamKey.USER_ID));
                        item.put(ParamKey.IS_APPROVED_IMAGE, shareObject.get(ParamKey.IS_APPROVED_IMAGE));
                        item.put(ParamKey.FILE_ID, fileId);
                        break;
                }
                JSONObject like = new JSONObject();
                like.put(ParamKey.IS_LIKE, shareObject.get(ParamKey.IS_LIKE)==null?0:shareObject.get(ParamKey.IS_LIKE));
                like.put(ParamKey.LIKE_NUM, shareObject.get(ParamKey.LIKE_NUM) == null ? 0 : shareObject.get(ParamKey.LIKE_NUM));
                item.put(ParamKey.LIKE_INFO, like);
                removeListChildInfo(item);
            }
            
            //add tag user info
            JSONArray tagArray = (JSONArray) shareObject.get(ParamKey.TAG_LIST);
            if (tagArray != null && !tagArray.isEmpty()) {
                for (Object item : tagArray) {
                    JSONObject tag = (JSONObject) item;
                    String tagUserId = (String) tag.get(ParamKey.USER_ID);
                    User tagUserInfo = mUserInfo.get(tagUserId);
                    if(tagUserInfo != null){
                        tag.put(ParamKey.USER_NAME, tagUserInfo.username);
                        tag.put(ParamKey.GENDER, tagUserInfo.gender);
                        tag.put(ParamKey.IS_ONLINE, tagUserInfo.isOnline);
                        tag.put(ParamKey.REGION, tagUserInfo.region);
                        tag.put(ParamKey.AGE, tagUserInfo.age);
                        if (tagUserInfo.avatarId != null) {
                            FileData imgData = map.mapImg.get(tagUserInfo.avatarId);
                            if (imgData != null && imgData.thumbnailUrl != null){
                                tag.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                            }else{
                                tag.put(ParamKey.AVATAR, "");
                            }
                        }else{
                            tag.put(ParamKey.AVATAR, "");
                        }
                    }
                }
            }

            removeBuzzInfo(shareObject);

            result = shareObject;
        }else{
            //waitting...
        }
        return result;
    }
    
    public static JSONArray parseMultiBuzz(JSONArray listChild){
        JSONArray result = new JSONArray();
        
        ListFileData map = getFileData(listChild, null, null);
        for (int i = 0; i < listChild.size(); i++) {
            JSONObject item = (JSONObject) listChild.get(i);
            String fileId = (String) item.get(ParamKey.FILE_ID);
            String coverId = (String) item.get(ParamKey.COVER_ID);
            Long buzzType = (Long) item.get(ParamKey.BUZZ_TYPE);
            
            switch(buzzType.intValue()){
                case Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS:
                    item.put(ParamKey.BUZZ_TYPE, "image");
                    FileData imgData = map.mapImg.get(fileId);
                    if (imgData != null){
                        addImageInfo(item, imgData);
                    }
                    break;
                case Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS:
                    item.put(ParamKey.BUZZ_TYPE, "video");
                    FileData videoData = map.mapVideo.get(fileId);
                    if (videoData != null){
                        addVideoInfo(item, videoData);
                    }
                    break;
                case Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS:
                    item.put(ParamKey.BUZZ_TYPE, "audio");
                    FileData coverData = map.mapCover.get(coverId);
                    FileData audioData = map.mapAudio.get(fileId);
                    if (audioData != null){
                        addAudioInfo(item, audioData, coverData);
                    }
                    break;
            }

            JSONObject like = new JSONObject();
            like.put(ParamKey.IS_LIKE, item.get(ParamKey.IS_LIKE)==null?0:item.get(ParamKey.IS_LIKE));
            like.put(ParamKey.LIKE_NUM, item.get(ParamKey.LIKE_NUM)==null?0:item.get(ParamKey.LIKE_NUM));
            item.put(ParamKey.LIKE_INFO, like);
            
            Integer viewNumber = 0;
            if(item.get(ParamKey.VIEW_NUMBER) != null){
                viewNumber = ((Long) item.get(ParamKey.VIEW_NUMBER)).intValue();
            }
            item.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
            
            item.remove(ParamKey.LIST_CHILD);
            item.remove(ParamKey.BUZZ_COMMENT);
            item.remove(ParamKey.IS_LIKE);
            item.remove(ParamKey.LIKE_NUM);
            item.remove(ParamKey.TAG_LIST);
        }
        result = listChild;
        return result;
    }
    
    public static JSONArray parseImageBuzz(String fileId, JSONObject data, FileData fileData){
        JSONArray result = new JSONArray();
        
        JSONObject val = new JSONObject();
        if (fileData != null){
            ParseData.addImageInfo(val, fileData);
        }
        JSONObject like = new JSONObject();
        like.put(ParamKey.IS_LIKE, data.get(ParamKey.IS_LIKE)==null?0:data.get(ParamKey.IS_LIKE));
        like.put(ParamKey.LIKE_NUM, data.get(ParamKey.LIKE_NUM)==null?0:data.get(ParamKey.LIKE_NUM));
        
        Integer viewNumber = 0;
        if(data.get(ParamKey.VIEW_NUMBER) != null){
            viewNumber = ((Long) data.get(ParamKey.VIEW_NUMBER)).intValue();
        }
        val.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
        val.put(ParamKey.SUB_COMMENT_NUM, data.get(ParamKey.SUB_COMMENT_NUM));
        val.put(ParamKey.BUZZ_ID, data.get(ParamKey.BUZZ_ID));
        val.put(ParamKey.BUZZ_TYPE, "image");
        val.put(ParamKey.COMMENT_NUM, data.get(ParamKey.COMMENT_NUM));
        val.put(ParamKey.BUZZ_TIME, data.get(ParamKey.BUZZ_TIME));
        val.put(ParamKey.USER_ID, data.get(ParamKey.USER_ID));
        val.put(ParamKey.IS_APPROVED_IMAGE, data.get(ParamKey.IS_APPROVED_IMAGE));
        val.put(ParamKey.LIKE_INFO, like);
        val.put(ParamKey.FILE_ID, fileId);
        
        result.add(val);      
        return result;
    }
    
    public static JSONArray parseVideoBuzz(String fileId, JSONObject data, FileData fileData){
        JSONArray result = new JSONArray();
        
        JSONObject val = new JSONObject();
        if (fileData != null){
            ParseData.addVideoInfo(val, fileData);
        }
        JSONObject like = new JSONObject();
        like.put(ParamKey.IS_LIKE, data.get(ParamKey.IS_LIKE)==null?0:data.get(ParamKey.IS_LIKE));
        like.put(ParamKey.LIKE_NUM, data.get(ParamKey.LIKE_NUM)==null?0:data.get(ParamKey.LIKE_NUM));
        
        Integer viewNumber = 0;
        if(data.get(ParamKey.VIEW_NUMBER) != null){
            viewNumber = ((Long) data.get(ParamKey.VIEW_NUMBER)).intValue();
        }
        val.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
        val.put(ParamKey.SUB_COMMENT_NUM, data.get(ParamKey.SUB_COMMENT_NUM));
        val.put(ParamKey.BUZZ_ID, data.get(ParamKey.BUZZ_ID));
        val.put(ParamKey.BUZZ_TYPE, "video");
        val.put(ParamKey.COMMENT_NUM, data.get(ParamKey.COMMENT_NUM));
        val.put(ParamKey.BUZZ_TIME, data.get(ParamKey.BUZZ_TIME));
        val.put(ParamKey.USER_ID, data.get(ParamKey.USER_ID));
        val.put(ParamKey.IS_APPROVED_IMAGE, data.get(ParamKey.IS_APPROVED_IMAGE));
        val.put(ParamKey.LIKE_INFO, like);
        val.put(ParamKey.FILE_ID, fileId);
        
        result.add(val);
        return result;
    }
    
    public static JSONArray parseAudioBuzz(String fileId, JSONObject data, FileData audioData, FileData coverData){
        JSONArray result = new JSONArray();
        
        JSONObject val = new JSONObject();
        if (audioData != null){
            ParseData.addAudioInfo(val, audioData, coverData);
        }
        JSONObject like = new JSONObject();
        like.put(ParamKey.IS_LIKE, data.get(ParamKey.IS_LIKE)==null?0:data.get(ParamKey.IS_LIKE));
        like.put(ParamKey.LIKE_NUM, data.get(ParamKey.LIKE_NUM)==null?0:data.get(ParamKey.LIKE_NUM));
        
        Integer viewNumber = 0;
        if(data.get(ParamKey.VIEW_NUMBER) != null){
            viewNumber = ((Long) data.get(ParamKey.VIEW_NUMBER)).intValue();
        }
        val.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
        val.put(ParamKey.SUB_COMMENT_NUM, data.get(ParamKey.SUB_COMMENT_NUM));
        val.put(ParamKey.BUZZ_ID, data.get(ParamKey.BUZZ_ID));
        val.put(ParamKey.BUZZ_TYPE, "audio");
        val.put(ParamKey.COMMENT_NUM, data.get(ParamKey.COMMENT_NUM));
        val.put(ParamKey.BUZZ_TIME, data.get(ParamKey.BUZZ_TIME));
        val.put(ParamKey.USER_ID, data.get(ParamKey.USER_ID));
        val.put(ParamKey.IS_APPROVED_IMAGE, data.get(ParamKey.IS_APPROVED_IMAGE));
        val.put(ParamKey.LIKE_INFO, like);
        val.put(ParamKey.FILE_ID, fileId);
        
        result.add(val);
        return result;
    }
    
    public static JSONArray parseStreamBuzz(String fileId, JSONObject data, FileData fileData){
        JSONArray result = new JSONArray();
        
        JSONObject val = new JSONObject();
        if (fileData != null){
            ParseData.addVideoInfo(val, fileData);
        }
        
        if(Constant.STREAM_STATUS.ON.equals(data.get(ParamKey.STREAM_STATUS)) || data.get(ParamKey.STREAM_STATUS) == null){
            val.put(ParamKey.STREAM_ID, data.get(ParamKey.STREAM_ID));  
            val.put(ParamKey.STREAM_END_TIME, -1);
            Integer currentView = 0;
            if(data.get(ParamKey.CURRENT_VIEW) != null){
                currentView = ((Long) data.get(ParamKey.CURRENT_VIEW)).intValue();
            }
            val.put(ParamKey.CURRENT_VIEW,  currentView == -1 ? 0 : currentView);
            Integer duration = 0;
            if(data.get(ParamKey.DURATION) != null){
                duration = ((Long) data.get(ParamKey.DURATION)).intValue();
            }
            val.put(ParamKey.DURATION, duration == -1 ? 0 : duration);
            
            val.put(ParamKey.STREAM_DURATION, caculateStreamTime(data.get(ParamKey.STREAM_START_TIME).toString()));
            
        }else{
            val.put(ParamKey.STREAM_END_TIME, data.get(ParamKey.STREAM_END_TIME));
            val.put(ParamKey.CURRENT_VIEW, 0);
            val.put(ParamKey.DURATION, 0);
            if(data.get(ParamKey.STREAM_END_TIME) != null && data.get(ParamKey.STREAM_START_TIME) != null){
                long streamDuration = caculateStreamTime(data.get(ParamKey.STREAM_START_TIME).toString(), data.get(ParamKey.STREAM_END_TIME).toString());
                val.put(ParamKey.STREAM_DURATION, streamDuration);
            }else{
                val.put(ParamKey.STREAM_DURATION, 0);
            }
        }
        
        if(data.get(ParamKey.STREAM_STATUS) == null){
            val.put(ParamKey.STREAM_STATUS, Constant.STREAM_STATUS.ON);
        }else{
            val.put(ParamKey.STREAM_STATUS, data.get(ParamKey.STREAM_STATUS));
        }
        
        JSONObject like = new JSONObject();
        like.put(ParamKey.IS_LIKE, data.get(ParamKey.IS_LIKE)==null?0:data.get(ParamKey.IS_LIKE));
        like.put(ParamKey.LIKE_NUM, data.get(ParamKey.LIKE_NUM)==null?0:data.get(ParamKey.LIKE_NUM));
        
        val.put(ParamKey.STREAM_START_TIME, data.get(ParamKey.STREAM_START_TIME));
        
        Integer viewNumber = 0;
        if(data.get(ParamKey.VIEW_NUMBER) != null){
            viewNumber = ((Long) data.get(ParamKey.VIEW_NUMBER)).intValue();
        }
        val.put(ParamKey.VIEW_NUMBER, viewNumber == -1 ? 0 : viewNumber);
        val.put(ParamKey.BUZZ_ID, data.get(ParamKey.BUZZ_ID));
        val.put(ParamKey.BUZZ_TYPE, "stream");
        val.put(ParamKey.COMMENT_NUM, data.get(ParamKey.COMMENT_NUM));
        val.put(ParamKey.BUZZ_TIME, data.get(ParamKey.BUZZ_TIME));
        val.put(ParamKey.USER_ID, data.get(ParamKey.USER_ID));
        val.put(ParamKey.IS_APPROVED_IMAGE, data.get(ParamKey.IS_APPROVED_IMAGE));
        val.put(ParamKey.LIKE_INFO, like);
        val.put(ParamKey.FILE_ID, fileId);
        
        result.add(val);
        return result;
    }
    
    public static ListFileData getFileData(JSONArray childList, String avaId, List<String> imgList){
        ListFileData result = new ListFileData();
        
        List<String> listImgId = new LinkedList<>();
        List<String> listVideoId = new LinkedList<>();
        List<String> listCoverId = new LinkedList<>();
        List<String> listAudioId = new LinkedList<>();
        List<String> listStreamId = new LinkedList<>();
        
        for (int i = 0; i < childList.size(); i++) {
            JSONObject item = (JSONObject) childList.get(i);
            Long buzzType = (Long) item.get(ParamKey.BUZZ_TYPE);
            if (buzzType != null){
                String fileId = (String) item.get(ParamKey.FILE_ID);
                String coverId = (String) item.get(ParamKey.COVER_ID);
                if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                    if (fileId != null && !fileId.equals(""))
                        listImgId.add(fileId);
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                    if (fileId != null && !fileId.equals(""))
                        listVideoId.add(fileId);
                }else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                    if (coverId != null && !coverId.equals(""))
                        listCoverId.add(coverId);
                    if (fileId != null && !fileId.equals(""))
                        listAudioId.add(fileId);
                }else if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                    if (fileId != null && !fileId.equals(""))
                        listStreamId.add(fileId);
                }
            }
        }
        
        if(avaId != null && !avaId.equals("")){
            listImgId.add(avaId);
        }
        
        if(imgList != null){
            for(String item: imgList){
                listImgId.add(item);
            }
        }
        
        result = InterCommunicator.getFileData(listImgId, listVideoId, listCoverId, listAudioId, listStreamId);
        
        return result;
    }
    
    public static void addImageInfo(JSONObject item, FileData data){
        item.put(ParamKey.BUZZ_VALUE, data.originalUrl);
        item.put(ParamKey.ORIGINAL_WIDTH, data.originalWidth);
        item.put(ParamKey.ORIGINAL_HEIGHT, data.originalHeight);
        item.put(ParamKey.THUMBNAIL_URL, data.thumbnailUrl);
        item.put(ParamKey.THUMBNAIL_WIDTH, data.thumbnailWidth);
        item.put(ParamKey.THUMBNAIL_HEIGHT, data.thumbnailHeight);
    }
    
    public static void addVideoInfo(JSONObject item, FileData data){
        item.put(ParamKey.BUZZ_VALUE, data.originalUrl);
        item.put(ParamKey.ORIGINAL_WIDTH, data.originalWidth);
        item.put(ParamKey.ORIGINAL_HEIGHT, data.originalHeight);
        item.put(ParamKey.THUMBNAIL_URL, data.thumbnailUrl);
        item.put(ParamKey.THUMBNAIL_WIDTH, data.thumbnailWidth);
        item.put(ParamKey.THUMBNAIL_HEIGHT, data.thumbnailHeight);
        item.put(ParamKey.URL, data.fileUrl);
        if(data.fileDuration != null){
            item.put(ParamKey.FILE_DURATION, data.fileDuration);
        }else{
            item.put(ParamKey.FILE_DURATION, 0);
        }
    }
    
    public static void addAudioInfo(JSONObject item, FileData audioData, FileData coverData){
        if(coverData != null){
            item.put(ParamKey.BUZZ_VALUE, coverData.originalUrl);
            item.put(ParamKey.THUMBNAIL_URL, coverData.originalUrl);
            item.put(ParamKey.ORIGINAL_WIDTH, coverData.originalWidth);
            item.put(ParamKey.ORIGINAL_HEIGHT, coverData.originalHeight);
            item.put(ParamKey.THUMBNAIL_WIDTH, coverData.originalWidth);
            item.put(ParamKey.THUMBNAIL_HEIGHT, coverData.originalHeight);
        }
        if(audioData.fileDuration != null){
            item.put(ParamKey.FILE_DURATION, audioData.fileDuration);
        }else{
            item.put(ParamKey.FILE_DURATION, 0);
        }
        item.put(ParamKey.URL, audioData.fileUrl);
    }
    
    public static void removeListChildInfo(JSONObject item){
        item.remove(ParamKey.LIST_CHILD);
        //item.remove(ParamKey.COMMENT_NUM);
        item.remove(ParamKey.IS_LIKE);
        item.remove(ParamKey.SHARE_NUMBER);
        item.remove(ParamKey.TAG_LIST);
//        item.remove(ParamKey.IS_APPROVED_IMAGE);
        item.remove(ParamKey.LIKE_NUM);
        item.remove(ParamKey.BUZZ_COMMENT);
    }
    
    public static void removeBuzzInfo(JSONObject item){
        item.remove(ParamKey.COMMENT_NUM);
        item.remove(ParamKey.IS_LIKE);
        item.remove(ParamKey.PRIVACY);
        item.remove(ParamKey.STREAM_ID);
        item.remove(ParamKey.SHARE_NUMBER);
        //item.remove(ParamKey.TAG_LIST);
        item.remove(ParamKey.FILE_ID);
        item.remove(ParamKey.BUZZ_TYPE);
        //item.remove(ParamKey.IS_APPROVED_IMAGE);
        item.remove(ParamKey.LIKE_NUM);
        item.remove(ParamKey.BUZZ_COMMENT);
        item.remove(ParamKey.COVER_ID);
        item.remove(ParamKey.DURATION);
        //item.remove(ParamKey.VIEW_NUMBER);
        item.remove(ParamKey.CURRENT_VIEW);
    }
    
    public static Map<String, User> parseUserInfo(JSONArray arr){
        Map<String, User> result = new HashMap<>();
        for(Object item: arr){
            JSONObject user = (JSONObject) item;
            if(user == null) continue;
            String userId = (String) user.get(ParamKey.USER_ID);
            User temp = new User();
            temp.userId = userId;
            temp.username = (String) user.get(ParamKey.USER_NAME);
            temp.region = (Long) user.get(ParamKey.REGION);
            temp.gender = (Long) user.get(ParamKey.GENDER);
            temp.age = ((Long) user.get(ParamKey.AGE)).intValue();
            temp.avatarId = (String) user.get(ParamKey.AVATAR_ID);
            temp.longitude = (Double) user.get(ParamKey.LONGITUDE);
            temp.latitude = (Double) user.get(ParamKey.LATITUDE);
            temp.dist = (Double) user.get(ParamKey.DIST);
            temp.isOnline = (Boolean) user.get(ParamKey.IS_ONLINE);
            
            result.put(userId, temp);
        }
        return result;
    }
    
    public static List<String> addAvataId(JSONArray arr){
        List<String> result = new ArrayList<>();
        for(Object item: arr){
            JSONObject user = (JSONObject) item;
            if(user == null) continue;
            if(user.get(ParamKey.AVATAR_ID) != null && !user.get(ParamKey.AVATAR_ID).equals("")){
                String avaId = (String) user.get(ParamKey.AVATAR_ID);
                result.add(avaId);
            }
        }
        return result;
    }
    
    public static Long caculateStreamTime(String startTime){
        Long result;
        Date currentTime = Util.getGMTTime();
        
        long a = new Long(startTime);
        Timestamp timestamp = new Timestamp(a);
        Date date = new Date(timestamp.getTime());
        
        long diff = currentTime.getTime() - date.getTime();
        long diffSeconds = diff / 1000; 
        Util.addDebugLog("timestamp.getTime();==="+diffSeconds);
        result = diffSeconds;
        
        return result;
    }
    
    public static Long caculateStreamTime(String startTime, String endTime){
        Long result;
        
        long start = new Long(startTime);
        Timestamp startTimestamp = new Timestamp(start);
        Date startDate = new Date(startTimestamp.getTime());
        
        long end = new Long(endTime);
        Timestamp endTimestamp = new Timestamp(end);
        Date endDate = new Date(endTimestamp.getTime());
        
        long diff = endDate.getTime() - startDate.getTime();
        long diffSeconds = diff / 1000;
        if(diff%1000 >0){
            result = diffSeconds + 1;
        }else{
            result = diffSeconds;
        }
        Util.addDebugLog("caculateStreamTime==="+diff);
        
        
        return result;
    }
}
