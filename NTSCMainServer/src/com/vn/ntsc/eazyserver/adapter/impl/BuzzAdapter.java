/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import java.util.LinkedList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.inspection.version.InspectionVersionDAO;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.adapter.impl.util.ParseData;
import static com.vn.ntsc.eazyserver.adapter.impl.util.ParseData.addImageInfo;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.entity.impl.FileData;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.ListFileUrl;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.MixService;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RuAc0n
 */
public class BuzzAdapter implements IServiceAdapter {

    public static final GetBuzz getBuzz = new GetBuzz();
    public static final GetBuzzDetail getBuzzDetail = new GetBuzzDetail();
    public static final AddBuzz addBuzz = new AddBuzz();
    public static final DelBuzz delBuzz = new DelBuzz();
    public static final GetLocalPeople locPeople = new GetLocalPeople();
    public static final ListComment listComment = new ListComment();
    public static final ListSubComment listSubComment = new ListSubComment();
    public static final Gift gift = new Gift();
    public static final Like like = new Like();
    public static final AddComment addComment = new AddComment();
    public static final AddSubComment addSubComment = new AddSubComment();
    public static final BuzzLeaveSocket buzzLeaveSocket = new BuzzLeaveSocket();
    public static final AddCommentVersion2 addCommentVersion2 = new AddCommentVersion2();
    public static final DelComment delComment = new DelComment();
    public static final DelSubComment delSubComment = new DelSubComment();
    public static final UploadStreamFile uploadStreamFile = new UploadStreamFile();
    public static final UpdateTag updateTag = new UpdateTag();
    public static final InviteFriend inviteFriend = new InviteFriend();
    public static final GetMediaBuzzData getMediaBuzzData = new GetMediaBuzzData();
    public static final AddTag addTag = new AddTag();
    public static final CheckBuzz checkBuzz = new CheckBuzz();

    @Override
    public String callService(Request request) {
        try{
            return InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
        }catch(Exception ex){
            Util.addErrorLog(ex);
            return ResponseMessage.UnknownError;
        }
    }

    public static class GetLocalPeople implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try{
                return InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }

    public static class GetBuzz implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result;
            try {
                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                Long desireLength = (Long) request.getParamValue(ParamKey.TEXT_LENGTH);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject umsJson = (JSONObject) new JSONParser().parse(umsStr);
                Long code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return umsStr;
                }
                JSONObject dataJson = (JSONObject) umsJson.get(ParamKey.DATA);
                JSONArray userArr = (JSONArray) dataJson.get(ParamKey.LIST_USER);
                JSONArray blockList = (JSONArray) dataJson.get(ParamKey.BLOCK_LIST);
                JSONArray friendList = (JSONArray) dataJson.get(ParamKey.FRIEND_LIST);
                request.reqObj.put(ParamKey.LIST_USER, userArr);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
                request.reqObj.put(ParamKey.FRIEND_LIST, friendList);
                
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);

                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                //DuongLTD
                Long codeBuzz = (Long) jo.get(ParamKey.ERROR_CODE);
                if (codeBuzz != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                //
                JSONArray buzzsArr = (JSONArray) jo.get(ParamKey.DATA);

                LinkedList<String> llEmail = new LinkedList<>();
                LinkedList<String> list = new LinkedList<>();
                List<String> listImgId = new LinkedList<>();
                List<String> listGiftId = new LinkedList<>();
                List<String> listVideoId = new LinkedList<>();
                List<String> listCoverId = new LinkedList<>();
                List<String> listAudioId = new LinkedList<>();
                List<String> listStreamId = new LinkedList<>();
                for (Object buzzsArr1 : buzzsArr) {
                    JSONObject buzz = (JSONObject) buzzsArr1;
                    String userID = (String) buzz.get(ParamKey.USER_ID);
                    llEmail.add(userID);
                    list.add(userID);

                    Long buzzType = (Long) buzz.get(ParamKey.BUZZ_TYPE);
                    if (buzzType != null){
                        String buzzValue = (String) buzz.get(ParamKey.BUZZ_VALUE);
                        String fileId = (String) buzz.get(ParamKey.FILE_ID);
                        String coverId = (String) buzz.get(ParamKey.COVER_ID);
                        if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                            if (fileId != null && !fileId.equals(""))
                                listImgId.add(fileId);
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ){
                            if (buzzValue != null)
                                listGiftId.add(buzzValue);
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                            if (fileId != null && !fileId.equals(""))
                                listVideoId.add(fileId);
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                            if (coverId != null && !coverId.equals("")){
                                listCoverId.add(coverId);
                            }
                            if (fileId != null && !fileId.equals(""))
                                listAudioId.add(fileId);
                        }else if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                            if (fileId != null && !fileId.equals("")){
                                listStreamId.add(fileId);
                            }
                        }
                    }

                    JSONArray cmtArr = (JSONArray) buzz.get(ParamKey.BUZZ_COMMENT);
                    if (cmtArr != null && !cmtArr.isEmpty()) {
                        for (Object cmtArr1 : cmtArr) {
                            JSONObject comment = (JSONObject) cmtArr1;
                            String cmtID = (String) comment.get(ParamKey.USER_ID);
                            llEmail.add(cmtID);
                            list.add(cmtID);
                        }
                    }
                    
                    JSONArray tagArr = (JSONArray) buzz.get(ParamKey.TAG_LIST);
                    if (tagArr != null && !tagArr.isEmpty()) {
                        for (Object item : tagArr) {
                            JSONObject tag = (JSONObject) item;
                            String userId = (String) tag.get(ParamKey.USER_ID);
                            llEmail.add(userId);
                        }
                    }
                    
                    JSONObject shareDetail = (JSONObject) buzz.get(ParamKey.SHARE_DETAIL);
                    if (shareDetail != null && !shareDetail.isEmpty()){
                        String shareFromUser = (String) shareDetail.get(ParamKey.USER_ID);
                        list.add(shareFromUser);
                    }
                }

                if (uId != null){
                    getConnectionInfor(request, buzzsArr, list);
                }

                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                Map<String, User> mUserInfo = ParseData.parseUserInfo(psArr);
                listImgId.addAll(ParseData.addAvataId(psArr));
                if (psArr == null) {
                    return buzzStr;
                }
                
//                getUserInfor(buzzsArr, psArr, ParamKey.BUZZ_COMMENT, listImgId);
                
                Util.addDebugLog("listImgId=================="+listImgId);
                Util.addDebugLog("listVideoId=================="+listVideoId);
                Util.addDebugLog("listCoverId=================="+listCoverId);
                Util.addDebugLog("listAudioId=================="+listAudioId);
                
                ListFileData mapData = InterCommunicator.getFileData(listImgId, listVideoId, listCoverId, listAudioId, listStreamId);
                
//                getUserAvatarUrl(buzzsArr, map.mapImg, ParamKey.BUZZ_COMMENT);
//                getUserAvatarUrl2(buzzsArr, mapData.mapImg, ParamKey.BUZZ_COMMENT);
                
                for (Object buzzObj : buzzsArr) {
                    JSONObject buzz = (JSONObject) buzzObj;
                    Long buzzType = (Long) buzz.get(ParamKey.BUZZ_TYPE);
                    String buzzValue = (String) buzz.get(ParamKey.BUZZ_VALUE);
                    String fileId = (String) buzz.get(ParamKey.FILE_ID);
                    String coverId = (String) buzz.get(ParamKey.COVER_ID);
                    String userId = (String) buzz.get(ParamKey.USER_ID);
                    
                    
                    //add user info
                    User userInfo = mUserInfo.get(userId);
                    if(userInfo != null){
                        buzz.put(ParamKey.USER_NAME, userInfo.username);
                        buzz.put(ParamKey.IS_ONLINE, userInfo.isOnline);
                        buzz.put(ParamKey.REGION, userInfo.region);
                        if (userInfo.avatarId != null) {
                            buzz.put(ParamKey.AVATAR_ID, userInfo.avatarId);
                            FileData imgData = mapData.mapImg.get(userInfo.avatarId);
                            if (imgData != null && imgData.thumbnailUrl != null){
                                buzz.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                            }else{
                                buzz.put(ParamKey.AVATAR, "");
                            }
                        }else{
                            buzz.put(ParamKey.AVATAR, "");
                        }
                        buzz.put(ParamKey.GENDER, userInfo.gender);
                        buzz.put(ParamKey.AGE, userInfo.age);
                        
                        JSONObject pos = new JSONObject();
                        pos.put(ParamKey.LONGITUDE, userInfo.longitude);
                        pos.put(ParamKey.LATITUDE, userInfo.latitude);
                        buzz.put(ParamKey.POSITION, pos);
                    }
                    
                    //add comment user info
                    JSONArray cmtArr = (JSONArray) buzz.get(ParamKey.BUZZ_COMMENT);
                    if (cmtArr != null && !cmtArr.isEmpty()) {
                        for (Object cmtArr1 : cmtArr) {
                            JSONObject comment = (JSONObject) cmtArr1;
                            String cmtID = (String) comment.get(ParamKey.USER_ID);
                            User commentUserInfo = mUserInfo.get(cmtID);
                            if(commentUserInfo != null){
                                comment.put(ParamKey.USER_NAME, commentUserInfo.username);
                                comment.put(ParamKey.GENDER, commentUserInfo.gender);
                                comment.put(ParamKey.IS_ONLINE, commentUserInfo.isOnline);
                                if (commentUserInfo.avatarId != null) {
                                    FileData imgData = mapData.mapImg.get(commentUserInfo.avatarId);
                                    if (imgData != null && imgData.thumbnailUrl != null){
                                        comment.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                                    }else{
                                        comment.put(ParamKey.AVATAR, "");
                                    }
                                }else{
                                    comment.put(ParamKey.AVATAR, "");
                                }
                            }
                        }
                    }
                    
                    //add tag user info
                    JSONArray tagArr = (JSONArray) buzz.get(ParamKey.TAG_LIST);
                    if (tagArr != null && !tagArr.isEmpty()) {
                        for (Object item : tagArr) {
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
                                    FileData imgData = mapData.mapImg.get(tagUserInfo.avatarId);
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
                    
                    //add list_child and share_detail
                    if (buzzType != null){
                        if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                            if(fileId != null){
                                FileData imgData = mapData.mapImg.get(fileId);
                                
                                buzz.put(ParamKey.LIST_CHILD, ParseData.parseImageBuzz(fileId, buzz, imgData));
                                buzz.put(ParamKey.CHILD_NUM, 1);
                            }
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                            if(fileId != null){
                                FileData videoData = mapData.mapVideo.get(fileId);
                                buzz.put(ParamKey.LIST_CHILD, ParseData.parseVideoBuzz(fileId, buzz, videoData));
                                buzz.put(ParamKey.CHILD_NUM, 1);
                            }
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                            if(coverId != null && fileId != null){
                                FileData coverData = mapData.mapCover.get(coverId);
                                FileData audioData = mapData.mapAudio.get(fileId);
                                buzz.put(ParamKey.LIST_CHILD, ParseData.parseAudioBuzz(fileId, buzz, audioData, coverData));
                                buzz.put(ParamKey.CHILD_NUM, 1);
                            }
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                            FileData streamData = mapData.mapStream.get(fileId);
                            buzz.put(ParamKey.LIST_CHILD, ParseData.parseStreamBuzz(fileId, buzz, streamData));
                            buzz.put(ParamKey.CHILD_NUM, 1);
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                            JSONArray childList = (JSONArray) buzz.get(ParamKey.LIST_CHILD);
                            buzz.put(ParamKey.LIST_CHILD, ParseData.parseMultiBuzz(childList));
                        }
                        else if (buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS){
                            JSONObject shareObject = (JSONObject) buzz.get(ParamKey.SHARE_DETAIL);
                            buzz.put(ParamKey.SHARE_DETAIL, ParseData.parseShareBuzz(shareObject));
                        }
                    }
                    if(buzz.get(ParamKey.USER_ID).equals(uId)){
                        buzz.put(ParamKey.IS_MINE, 1);
                    }else{
                        buzz.put(ParamKey.IS_MINE, 0);
                    }
                    
                    Boolean isSubstring = false;
                    if(desireLength != null && desireLength.intValue() != 0 && buzzValue.length() >= desireLength.intValue() ){
                        buzzValue = buzzValue.substring(0, desireLength.intValue());
                        isSubstring = true;
                    }
                    buzz.put(ParamKey.IS_SUBSTRING, isSubstring);
                    buzz.put(ParamKey.BUZZ_VALUE, buzzValue);
                    buzz.put(ParamKey.SHARE_NUMBER, buzz.get(ParamKey.SHARE_NUMBER));
                    
                    buzz.put(ParamKey.BUZZ_REGION, buzz.get(ParamKey.BUZZ_REGION)==null?0:buzz.get(ParamKey.BUZZ_REGION));
                    
                    JSONObject like = new JSONObject();
                    like.put(ParamKey.IS_LIKE, buzz.get(ParamKey.IS_LIKE)==null?0:buzz.get(ParamKey.IS_LIKE));
                    like.put(ParamKey.LIKE_NUM, buzz.get(ParamKey.LIKE_NUM)==null?0:buzz.get(ParamKey.LIKE_NUM));
                    buzz.put(ParamKey.LIKE_INFO, like);
                    
                    buzz.remove(ParamKey.BUZZ_TYPE);
                    buzz.remove(ParamKey.LAST_ACT);
                    buzz.remove(ParamKey.FILE_ID);
                    
                    buzz.remove(ParamKey.IS_LIKE);
                    buzz.remove(ParamKey.LIKE_NUM);
                    
                    buzz.remove(ParamKey.STREAM_ID);
                    
//                    buzz.remove(ParamKey.VIEW_NUMBER);
                    buzz.remove(ParamKey.CURRENT_VIEW);
                    buzz.remove(ParamKey.STREAM_END_TIME);
                    buzz.remove(ParamKey.STREAM_START_TIME);
                    buzz.remove(ParamKey.STREAM_STATUS);
                }
                result = jo.toJSONString();
                String token = (String) request.getParamValue(ParamKey.TOKEN_STRING);
                
                //case get buzz null token ( not login) 
                if(token == null || token == ""){
                    return jo.toJSONString();
                }
                Session applicationUser = SessionManager.getSession(token);
                Integer applicationType = applicationUser.applicationType;
                //Just Web Join Socket in all buzz
                // Mobile join buzz socket in get_buzz_detail
                if(applicationType == Constant.APPLICATION_TYPE.WEB_APPLICATION){
                    sendGetBuzzJoinWebSocket(request,jo);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            return result;
        }

        private void getConnectionInfor( Request request, JSONArray buzzsArr, List<String> list) {
            JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, list);
            for(Object obj : buzzsArr){
                JSONObject jsonObj = (JSONObject) obj;
                String ownerId = (String) jsonObj.get(ParamKey.USER_ID);
                Util.addDebugLog("------------------------------ "+listConnectionInfor);
                for(Object connectionObj : listConnectionInfor){
                    JSONObject connectionJson = (JSONObject) connectionObj;
                    String id = Util.getStringParam(connectionJson, "rqt_id");
                    if(ownerId.equals(id)){
                        int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                        jsonObj.put(ParamKey.IS_FAV, isFav);
                        if(Util.getLongParam(connectionJson, "comment_buzz_point") != null){
                            int commentPrice = Util.getLongParam(connectionJson, "comment_buzz_point").intValue();
                            jsonObj.put("comment_buzz_point", commentPrice);
                        }                        
                        break;
                    }
                }
                JSONArray cmtArray = (JSONArray) jsonObj.get(ParamKey.BUZZ_COMMENT);
                if (cmtArray != null && !cmtArray.isEmpty()) {
                    for (Object cmtArr1 : cmtArray) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        for(Object connectionObj : listConnectionInfor){
                            JSONObject connectionJson = (JSONObject) connectionObj;
                            String id = Util.getStringParam(connectionJson, "rqt_id");
                            if(cmtID.equals(id)){
                                int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                                comment.put(ParamKey.IS_FAV, isFav);
                                Long commentPrice = Util.getLongParam(connectionJson, "sub_comment_point");
                                if(commentPrice != null)
                                    comment.put("sub_comment_point", commentPrice);
                                break;
                            }
                        }
                    }
                }
                
                JSONObject shareDetail = (JSONObject) jsonObj.get(ParamKey.SHARE_DETAIL);
                if (shareDetail != null && !shareDetail.isEmpty()){
                    String shareFromUser = (String) shareDetail.get(ParamKey.USER_ID);
                    list.add(shareFromUser);
                }
                if (shareDetail != null && !shareDetail.isEmpty()){
                    String shareFromUser = (String) shareDetail.get(ParamKey.USER_ID);
                    for(Object connectionObj : listConnectionInfor){
                        JSONObject connectionJson = (JSONObject) connectionObj;
                        String id = Util.getStringParam(connectionJson, "rqt_id");
                        if(shareFromUser.equals(id)){
                            int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                            shareDetail.put(ParamKey.IS_FAV, isFav);
                            break;
                        }
                    }
                }
            }
        }

    }    
    
    public static void getUserInfor(JSONArray buzzsArr, JSONArray psArr, String subKey, List<String> listImgId) {
        int count = 0;
        for (int i = 0; i < buzzsArr.size(); i++) {
            JSONObject buzz = (JSONObject) buzzsArr.get(i);
            JSONObject ps = (JSONObject) psArr.get(count);
            if (ps != null) {
                Double lon = (Double) ps.get(ParamKey.LONGITUDE);
                Double lat = (Double) ps.get(ParamKey.LATITUDE);
                String userName = (String) ps.get(ParamKey.USER_NAME);
                String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                Long gender = (Long) ps.get(ParamKey.GENDER);
                Long age = (Long) ps.get(ParamKey.AGE);
                Double dist = (Double) ps.get(ParamKey.DIST);
                Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                Long region = (Long) ps.get(ParamKey.REGION);
                buzz.put(ParamKey.LONGITUDE, lon);
                buzz.put(ParamKey.LATITUDE, lat);
                buzz.put(ParamKey.USER_NAME, userName);
                buzz.put(ParamKey.DIST, dist);
                buzz.put(ParamKey.IS_ONLINE, isOnline);
                buzz.put(ParamKey.REGION, region);
                if (avaId != null) {
                    buzz.put(ParamKey.AVATAR_ID, avaId);
                    listImgId.add(avaId);
                }
                buzz.put(ParamKey.AVATAR, "");
                buzz.put(ParamKey.GENDER, gender);
                buzz.put(ParamKey.AGE, age);
            }
            JSONArray cmtArr = (JSONArray) buzz.get(subKey);
            if (cmtArr != null && !cmtArr.isEmpty()) {
                for (int index = 0; index < cmtArr.size(); index++) {
                    JSONObject comment = (JSONObject) cmtArr.get(index);
                    count++;
                    ps = (JSONObject) psArr.get(count);
                    if (ps != null) {
                        String userName = (String) ps.get(ParamKey.USER_NAME);
                        String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                        Long gender = (Long) ps.get(ParamKey.GENDER);
                        Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
                        comment.put(ParamKey.USER_NAME, userName);
                        if (avaId != null) {
                            comment.put(ParamKey.AVATAR_ID, avaId);
                            listImgId.add(avaId);
                        }
                        comment.put(ParamKey.AVATAR, "");
                        comment.put(ParamKey.GENDER, gender);
                        comment.put(ParamKey.IS_ONLINE, isOnline);
                    }
                }
            }
            count++;
        }
    }    
    
    public static void getUserAvatarUrl(JSONArray buzzsArr, HashMap<String, FileUrl> mapImg, String subKey) {
        for (int i = 0; i < buzzsArr.size(); i++) {
            JSONObject buzz = (JSONObject) buzzsArr.get(i);
            String avaId = (String) buzz.get(ParamKey.AVATAR_ID);
            if (avaId != null){
                FileUrl url = mapImg.get(avaId);
                if (url != null){
                    buzz.put(ParamKey.AVATAR, url.getThumbnail());
                }
            }
            JSONArray cmtArr = (JSONArray) buzz.get(subKey);
            if (cmtArr != null && !cmtArr.isEmpty()) {
                for (int index = 0; index < cmtArr.size(); index++) {
                    JSONObject comment = (JSONObject) cmtArr.get(index);
                    String cmtAvaId = (String) comment.get(ParamKey.AVATAR_ID);
                    if (cmtAvaId != null){
                        FileUrl url = mapImg.get(cmtAvaId);
                        if (url != null){
                            comment.put(ParamKey.AVATAR, url.getThumbnail());
                        }
                    }
                }
            }
        }
    }

    public static void getUserAvatarUrl2(JSONArray buzzsArr, HashMap<String, FileData> mapImg, String subKey) {
        for (int i = 0; i < buzzsArr.size(); i++) {
            JSONObject buzz = (JSONObject) buzzsArr.get(i);
            String avaId = (String) buzz.get(ParamKey.AVATAR_ID);
            if (avaId != null){
                FileData imgData = mapImg.get(avaId);
                if (imgData != null){
                    buzz.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                }
            }
            JSONArray cmtArr = (JSONArray) buzz.get(subKey);
            if (cmtArr != null && !cmtArr.isEmpty()) {
                for (int index = 0; index < cmtArr.size(); index++) {
                    JSONObject comment = (JSONObject) cmtArr.get(index);
                    String cmtAvaId = (String) comment.get(ParamKey.AVATAR_ID);
                    if (cmtAvaId != null){
                        FileData imgData = mapImg.get(cmtAvaId);
                        if (imgData != null){
                            comment.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                        }
                    }
                }
            }
        }
    }
    
    public static class ListComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result = "";
            try {
                request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONArray blockList = new JSONArray();
                if(umsStr != null){
                    JSONObject umsO = (JSONObject) new JSONParser().parse(umsStr);
                    if (umsO!=null){
                        if (umsO.get(ParamKey.DATA)!=null){
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST)!=null){
                                blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                            }
                        }
                    }
                }
//                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
//                JSONArray blackList = (JSONArray) ((JSONObject)new JSONParser().parse(umsStr)).get(ParamKey.DATA);
                request.put(ParamKey.API_NAME, API.LIST_COMMENT);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                Util.addDebugLog("buzzStr======================================="+buzzStr);
                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                //
                JSONArray cmtArr = (JSONArray) jo.get(ParamKey.DATA);
                LinkedList<String> llEmail = new LinkedList<>();
                if (cmtArr != null && !cmtArr.isEmpty()) {
                    for (Object cmtArr1 : cmtArr) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        llEmail.add(cmtID);
                        
                        JSONArray subCommentArray = (JSONArray) comment.get("sub_comment");
                        if (subCommentArray != null && !subCommentArray.isEmpty()) {
                            for (Object subComment1 : subCommentArray) {
                                JSONObject subComment = (JSONObject) subComment1;
                                String subCommnetUserId = (String) subComment.get(ParamKey.USER_ID);
                                llEmail.add(subCommnetUserId);
                            }
                        }
                    }
                }

                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr == null) {
                    return buzzStr;
                }
                List<String> listImgId = new LinkedList<>();
                getUserInfor(cmtArr, psArr, "sub_comment", listImgId);
                
                HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
                getUserAvatarUrl(cmtArr, imgMap, "sub_comment");
                
                JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, llEmail);
                if (cmtArr != null && !cmtArr.isEmpty()) {
                    for (Object cmtArr1 : cmtArr) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        if (listConnectionInfor!=null)
                        for(Object connectionObj : listConnectionInfor){
                            JSONObject connectionJson = (JSONObject) connectionObj;
                            String id = Util.getStringParam(connectionJson, "rqt_id");
                            if(cmtID.equals(id)){
                                int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                                comment.put(ParamKey.IS_FAV, isFav);
                                Long commentPrice = Util.getLongParam(connectionJson, "sub_comment_point");
                                if(commentPrice != null)
                                    comment.put("sub_comment_point", commentPrice);
                                break;
                            }
                        }
                       
                    }
                }
//                for (int i = 0; i < psArr.size(); i++) {
//                    JSONObject ps = (JSONObject) psArr.get(i);
//                    if (ps != null) {
//                        String userName = (String) ps.get(ParamKey.USER_NAME);
//                        String avaId = (String) ps.get(ParamKey.AVATAR_ID);
//                        Long gender = (Long) ps.get(ParamKey.GENDER);
//                        Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);
//
//                        JSONObject comment = (JSONObject) cmtArr.get(i);
//                        comment.put(ParamKey.USER_NAME, userName);
//                        if (avaId != null) {
//                            comment.put(ParamKey.AVATAR_ID, avaId);
//                        }
//                        comment.put(ParamKey.GENDER, gender);
//                        comment.put(ParamKey.IS_ONLINE, isOnline);
//                    }
//                }

                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class ListSubComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result = "";
            try {
                request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONArray blockList = new JSONArray();
                if (umsStr != null) {
                    JSONObject umsO = (JSONObject) new JSONParser().parse(umsStr);
                    if (umsO != null) {
                        if (umsO.get(ParamKey.DATA) != null) {
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST) != null) {
                                blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                            }
                        }
                    }
                }
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
                request.put(ParamKey.API_NAME, API.LIST_SUB_COMMENT);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                JSONArray cmtArr = (JSONArray) jo.get(ParamKey.DATA);
                LinkedList<String> llEmail = new LinkedList<>();
                LinkedList<String> listAvaId = new LinkedList<>();
                if (cmtArr != null && !cmtArr.isEmpty()) {
                    for (Object cmtArr1 : cmtArr) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        llEmail.add(cmtID);
                    }
                }

                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                if (psArr == null) {
                    return buzzStr;
                }

                for (int i = 0; i < psArr.size(); i++) {
                    JSONObject ps = (JSONObject) psArr.get(i);
                    if (ps != null) {
                        String userName = (String) ps.get(ParamKey.USER_NAME);
                        String avaId = (String) ps.get(ParamKey.AVATAR_ID);
                        Long gender = (Long) ps.get(ParamKey.GENDER);
                        Boolean isOnline = (Boolean) ps.get(ParamKey.IS_ONLINE);

                        JSONObject comment = (JSONObject) cmtArr.get(i);
                        comment.put(ParamKey.USER_NAME, userName);
                        if (avaId != null) {
                            comment.put(ParamKey.AVATAR_ID, avaId);
                            listAvaId.add(avaId);
                        }
                        comment.put(ParamKey.AVATAR, "");
                        comment.put(ParamKey.GENDER, gender);
                        comment.put(ParamKey.IS_ONLINE, isOnline);
                    }
                }
                
                HashMap<String, FileUrl> mapAva = InterCommunicator.getImage(listAvaId);
                for (int i = 0; i < psArr.size(); i++) {
                    JSONObject comment = (JSONObject) cmtArr.get(i);
                    String avaId = (String) comment.get(ParamKey.AVATAR_ID);
                    if (avaId != null && mapAva.get(avaId) != null){
                        comment.put(ParamKey.AVATAR, mapAva.get(avaId).getThumbnail());
                    }
                    
                }

                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static class GetBuzzDetail implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result = "";

            try {
//                request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);
                request.put(ParamKey.API_NAME, API.GET_BUZZ);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONArray blockList = new JSONArray();
                JSONArray friendList = new JSONArray();
                if(umsStr != null){
                    JSONObject umsO = (JSONObject) new JSONParser().parse(umsStr);
//                    if (umsO!=null){
//                        blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
//                    }
                    if (umsO!=null){
                        if (umsO.get(ParamKey.DATA)!=null){
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST)!=null){
                                blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                            }
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.FRIEND_LIST)!=null){
                                friendList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.FRIEND_LIST);
                            }
                        }
                    }
                }
//                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
                request.reqObj.put(ParamKey.FRIEND_LIST, friendList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.GET_BUZZ_DETAIL);
//                request.reqObj.put(ParamKey.BLACK_LIST, blackList);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                //DuongLTD
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                //
                JSONObject buzz = (JSONObject) jo.get(ParamKey.DATA);

                LinkedList<String> llEmail = new LinkedList<>();
                String userID = (String) buzz.get(ParamKey.USER_ID);
                
                LinkedList<String> listImgId = new LinkedList<>();
                LinkedList<String> listGiftId = new LinkedList<>();
                LinkedList<String> listVideoId = new LinkedList<>();
                LinkedList<String> listCoverId = new LinkedList<>();
                LinkedList<String> listAudioId = new LinkedList<>();
                LinkedList<String> listStreamId = new LinkedList<>();

                llEmail.add(userID);
                
                JSONArray cmtArr = (JSONArray) buzz.get(ParamKey.BUZZ_COMMENT);
                if (cmtArr != null && !cmtArr.isEmpty()) {
                    for (Object cmtArr1 : cmtArr) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        llEmail.add(cmtID);
                        JSONArray subComments = (JSONArray) comment.get("sub_comment");
                        if(subComments != null && !subComments.isEmpty()){
                            for (Object subComment1 : subComments) {
                                JSONObject subComment = (JSONObject) subComment1;
                                String subCommentId = (String) subComment.get(ParamKey.USER_ID);
                                llEmail.add(subCommentId);
                            }
                        }
                    }
                }
                
                JSONArray tagArr = (JSONArray) buzz.get(ParamKey.TAG_LIST);
                if (tagArr != null && !tagArr.isEmpty()) {
                    for (Object item : tagArr) {
                        JSONObject tag = (JSONObject) item;
                        String tagId = (String) tag.get(ParamKey.USER_ID);
                        llEmail.add(tagId);
                    }
                }
                
                JSONObject shareDetail = (JSONObject) buzz.get(ParamKey.SHARE_DETAIL);
                if (shareDetail != null && !shareDetail.isEmpty()){
                    String shareFromUser = (String) shareDetail.get(ParamKey.USER_ID);
                    llEmail.add(shareFromUser);
                }
                
                // get favourist infor
                JSONArray listConnectionInfor = InterCommunicator.getConnectionInfor(request, llEmail);

                if (listConnectionInfor!=null){
                    if(!listConnectionInfor.isEmpty()){
                        String ownerId = (String) buzz.get(ParamKey.USER_ID);
                        for(Object connectionObj : listConnectionInfor){
                            JSONObject connectionJson = (JSONObject) connectionObj;
                            String id = Util.getStringParam(connectionJson, "rqt_id");
                            if(ownerId.equals(id)){
                                int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                                buzz.put(ParamKey.IS_FAV, isFav);
                                if(Util.getLongParam(connectionJson, "comment_buzz_point") != null){
                                    int commentPrice = Util.getLongParam(connectionJson, "comment_buzz_point").intValue();
                                buzz.put("comment_buzz_point", commentPrice);
                                }

                                break;
                            }
                        }
                        JSONArray cmtArray = (JSONArray) buzz.get(ParamKey.BUZZ_COMMENT);
                        if (cmtArray != null && !cmtArray.isEmpty()) {
                            for (Object cmtArr1 : cmtArray) {
                                JSONObject comment = (JSONObject) cmtArr1;
                                String cmtID = (String) comment.get(ParamKey.USER_ID);
                                for(Object connectionObj : listConnectionInfor){
                                    JSONObject connectionJson = (JSONObject) connectionObj;
                                    String id = Util.getStringParam(connectionJson, "rqt_id");
                                    if(cmtID.equals(id)){
                                        int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                                        comment.put(ParamKey.IS_FAV, isFav);
                                        Long commentPrice = Util.getLongParam(connectionJson, "sub_comment_point");
                                        if(commentPrice != null)
                                            comment.put("sub_comment_point", commentPrice);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if (shareDetail != null && !shareDetail.isEmpty()){
                            String shareFromUser = (String) shareDetail.get(ParamKey.USER_ID);
                            for(Object connectionObj : listConnectionInfor){
                                JSONObject connectionJson = (JSONObject) connectionObj;
                                String id = Util.getStringParam(connectionJson, "rqt_id");
                                if(shareFromUser.equals(id)){
                                    int isFav = Util.getLongParam(connectionJson, "is_fav").intValue();
                                    shareDetail.put(ParamKey.IS_FAV, isFav);
                                    break;
                                }
                            }
                        }
                        
                    }
                }
                
                String uId = (String) request.getParamValue(ParamKey.USER_ID);
                JSONArray psArr = InterCommunicator.getUserPresentList(uId, llEmail);
                Map<String, User> mUserInfo = ParseData.parseUserInfo(psArr);
                if (psArr == null) {
                    return buzzStr;
                }
                
                Long buzzType = (Long) buzz.get(ParamKey.BUZZ_TYPE);
                String buzzVal = (String) buzz.get(ParamKey.BUZZ_VALUE);
                String fileId = (String) buzz.get(ParamKey.FILE_ID);
                String coverId = (String) buzz.get(ParamKey.COVER_ID);
                if (buzzType != null && buzzVal != null){
                    if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                        if(fileId != null && !fileId.equals("")){
                            listImgId.add(fileId);
                        }
                    }
                    else if (buzzType == Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ){
                        listGiftId.add(buzzVal);
                    }
                    else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                        if(fileId != null && !fileId.equals("")){
                            listVideoId.add(fileId);
                        }
                    }
                    else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                        if(coverId != null && !coverId.equals("")){
                            listCoverId.add(coverId);
                        }
                        if(fileId != null && !fileId.equals("")){
                            listAudioId.add(fileId);
                        }
                    }else if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                        if(fileId != null && !fileId.equals("")){
                            listStreamId.add(fileId);
                        }
                    }
                }
                
                listImgId.addAll(ParseData.addAvataId(psArr));
                
                ListFileData mapData = InterCommunicator.getFileData(listImgId, listVideoId, listCoverId, listAudioId, listStreamId);
                
                //add user info
                User userInfo = mUserInfo.get(userID);
                if(userInfo != null){
                    buzz.put(ParamKey.USER_NAME, userInfo.username);
                    buzz.put(ParamKey.IS_ONLINE, userInfo.isOnline);
                    buzz.put(ParamKey.REGION, userInfo.region);
                    if (userInfo.avatarId != null) {
                        buzz.put(ParamKey.AVATAR_ID, userInfo.avatarId);
                        FileData imgData = mapData.mapImg.get(userInfo.avatarId);
                        if (imgData != null && imgData.thumbnailUrl != null){
                            buzz.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                        }else{
                            buzz.put(ParamKey.AVATAR, "");
                        }
                    }else{
                        buzz.put(ParamKey.AVATAR, "");
                    }
                    buzz.put(ParamKey.GENDER, userInfo.gender);
                    buzz.put(ParamKey.AGE, userInfo.age);

                    JSONObject pos = new JSONObject();
                    pos.put(ParamKey.LONGITUDE, userInfo.longitude);
                    pos.put(ParamKey.LATITUDE, userInfo.latitude);
                    buzz.put(ParamKey.POSITION, pos);
                }
                
                //add comment user info
                JSONArray cmtArray = (JSONArray) buzz.get(ParamKey.BUZZ_COMMENT);
                if (cmtArray != null && !cmtArray.isEmpty()) {
                    for (Object cmtArr1 : cmtArray) {
                        JSONObject comment = (JSONObject) cmtArr1;
                        String cmtID = (String) comment.get(ParamKey.USER_ID);
                        User commentUserInfo = mUserInfo.get(cmtID);
                        if(commentUserInfo != null){
                            comment.put(ParamKey.USER_NAME, commentUserInfo.username);
                            comment.put(ParamKey.GENDER, commentUserInfo.gender);
                            comment.put(ParamKey.IS_ONLINE, commentUserInfo.isOnline);
                            if (commentUserInfo.avatarId != null) {
                                FileData imgData = mapData.mapImg.get(commentUserInfo.avatarId);
                                if (imgData != null && imgData.thumbnailUrl != null){
                                    comment.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                                }else{
                                    comment.put(ParamKey.AVATAR, "");
                                }
                            }else{
                                comment.put(ParamKey.AVATAR, "");
                            }
                        }
                        
                        //add sub comment user info
                        JSONArray subComments = (JSONArray) comment.get("sub_comment");
                        if(subComments != null && !subComments.isEmpty()){
                            for (Object subComment1 : subComments) {
                                JSONObject subComment = (JSONObject) subComment1;
                                String subCmtID = (String) comment.get(ParamKey.USER_ID);
                                User subCommentUserInfo = mUserInfo.get(subCmtID);
                                if(subCommentUserInfo != null){
                                    subComment.put(ParamKey.USER_NAME, subCommentUserInfo.username);
                                    subComment.put(ParamKey.GENDER, subCommentUserInfo.gender);
                                    subComment.put(ParamKey.IS_ONLINE, subCommentUserInfo.isOnline);
                                    if (subCommentUserInfo.avatarId != null) {
                                        FileData imgData = mapData.mapImg.get(subCommentUserInfo.avatarId);
                                        if (imgData != null && imgData.thumbnailUrl != null){
                                            subComment.put(ParamKey.AVATAR, imgData.thumbnailUrl);
                                        }else{
                                            subComment.put(ParamKey.AVATAR, "");
                                        }
                                    }else{
                                        subComment.put(ParamKey.AVATAR, "");
                                    }
                                }
                            }
                        }
                    }
                }
                
                //add tag user info
                JSONArray tagArray = (JSONArray) buzz.get(ParamKey.TAG_LIST);
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
                                FileData imgData = mapData.mapImg.get(tagUserInfo.avatarId);
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
                
                //add list_child and share_detail
                if(buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                    JSONArray childList = (JSONArray) buzz.get(ParamKey.LIST_CHILD);
                    buzz.put(ParamKey.LIST_CHILD, ParseData.parseMultiBuzz(childList));
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){     
                    if (fileId != null){
                        FileData imgData = mapData.mapImg.get(fileId);
                        buzz.put(ParamKey.LIST_CHILD, ParseData.parseImageBuzz(fileId, buzz, imgData));
                        buzz.put(ParamKey.CHILD_NUM, 1);
                    } 
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                    if (fileId != null){
                        FileData videoData = mapData.mapVideo.get(fileId);
                        buzz.put(ParamKey.LIST_CHILD, ParseData.parseVideoBuzz(fileId, buzz, videoData));
                        buzz.put(ParamKey.CHILD_NUM, 1);
                    } 
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                    if(fileId != null){
                        FileData coverData = mapData.mapCover.get(coverId);
                        FileData audioData = mapData.mapAudio.get(fileId);
                        buzz.put(ParamKey.LIST_CHILD, ParseData.parseAudioBuzz(fileId, buzz, audioData, coverData));
                        buzz.put(ParamKey.CHILD_NUM, 1);
                    }
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                    FileData streamData = mapData.mapStream.get(fileId);
                    buzz.put(ParamKey.LIST_CHILD, ParseData.parseStreamBuzz(fileId, buzz, streamData));
                    buzz.put(ParamKey.CHILD_NUM, 1);
                }
                else if (buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS){
                    JSONObject shareObject = (JSONObject) buzz.get(ParamKey.SHARE_DETAIL);
                    buzz.put(ParamKey.SHARE_DETAIL, ParseData.parseShareBuzz(shareObject));
                }
                
                buzz.put(ParamKey.BUZZ_VALUE, buzzVal);
                
                buzz.put(ParamKey.SHARE_NUMBER, buzz.get(ParamKey.SHARE_NUMBER));
                
                buzz.put(ParamKey.BUZZ_REGION, buzz.get(ParamKey.BUZZ_REGION)==null?0:buzz.get(ParamKey.BUZZ_REGION));
                
                sendBuzzJoinWebSocket(request, jo);
                
                JSONObject like = new JSONObject();
                like.put(ParamKey.IS_LIKE, buzz.get(ParamKey.IS_LIKE)==null?0:buzz.get(ParamKey.IS_LIKE));
                like.put(ParamKey.LIKE_NUM, buzz.get(ParamKey.LIKE_NUM)==null?0:buzz.get(ParamKey.LIKE_NUM));
                buzz.put(ParamKey.LIKE_INFO, like);
                
                buzz.remove(ParamKey.BUZZ_TYPE);
                buzz.remove(ParamKey.FILE_ID);

                buzz.remove(ParamKey.IS_LIKE);
                buzz.remove(ParamKey.LIKE_NUM);
                
                buzz.remove(ParamKey.STREAM_ID);
                
//                buzz.remove(ParamKey.VIEW_NUMBER);
                buzz.remove(ParamKey.STREAM_END_TIME);
                buzz.remove(ParamKey.STREAM_START_TIME);
                buzz.remove(ParamKey.STREAM_STATUS);
                
                result = jo.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }
    
    public static class GetMediaBuzzData implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result = "";
            try{
                request.put(ParamKey.API_NAME, API.GET_MEDIA_BUZZ_DATA);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) jo.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                JSONArray buzz = (JSONArray) jo.get(ParamKey.DATA);
                
                
                
                
                result = buzz.toJSONString().toString();
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
            return result;
        }
        
    }

    public static class DelComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String msgType = (String)request.getParamValue("msg_type");
            request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
//                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.DELETE_COMMENT);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject cmtWebsocket = (JSONObject) new JSONParser().parse(buzzStr);
                if(msgType != null){
                    sendBuzzDelCmtWebsocket(request,cmtWebsocket);
                }
                return buzzStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ums;
            }
        }
    }
    
    public static class DelSubComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String msgType = (String)request.getParamValue("msg_type");
            request.put(ParamKey.API_NAME, API.GET_BLOCK_LIST);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
//                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.DELETE_SUB_COMMENT);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                if(msgType != null){
                    JSONObject cmtWebsocket = (JSONObject) new JSONParser().parse(buzzStr);
                    sendBuzzDelSubCmtWebsocket(request,cmtWebsocket);
                }
                return buzzStr;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ums;
            }
        }
    }

    public static class AddComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            request.put(ParamKey.API_NAME, API.ADD_COMMENT_GET_INFOR);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.ADD_COMMENT);
                // sent request add_comment
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
                Long time = (Long) dataObj.get(ParamKey.TIME);
                Long buzzType = (Long) dataObj.get(ParamKey.BUZZ_TYPE);
                int isApp = ((Long) dataObj.get("is_app")).intValue();
                request.reqObj.put(ParamKey.TIME, time);
                request.reqObj.put("is_app", isApp);
                String buzzOwner = (String) dataObj.get(ParamKey.BUZZ_OWNER_ID);
                String streamStatus = (String) dataObj.get(BuzzdbKey.BUZZ_DETAIL.STREAM_STATUS);
                request.put(ParamKey.BUZZ_OWNER_ID, buzzOwner);
                request.reqObj.put(ParamKey.BUZZ_TYPE, buzzType);

//                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                
                if (!blockList.contains(buzzOwner)) {
                    request.put("api", "lst_cmt");
                    String cmtId = (String) dataObj.get("cmt_id");
                    request.put("cmt_id", cmtId);
                    String result = listComment.callService(request);
                    Util.addDebugLog("result===========================================" + result);
                    JSONObject cmtWebsocket = (JSONObject) new JSONParser().parse(result);

                    String resultConfirmSendNoti = sendBuzzCmtWebSocket(request, cmtWebsocket);
                    JSONObject jsonConfirmSendNoti = (JSONObject) new JSONParser().parse(resultConfirmSendNoti);
                    Util.addDebugLog("jsonConfirmSendNoti========================"+jsonConfirmSendNoti.toJSONString());
                    Long isSendNoti = (Long) jsonConfirmSendNoti.get("code");
                    if (isSendNoti == ErrorCode.SUCCESS) {
                        JSONArray listUserCmt = (JSONArray)jsonConfirmSendNoti.get("list_user_cmt");
                        JSONArray listUserNotiSocket = (JSONArray)jsonConfirmSendNoti.get("list_user_noti_socket");
                        JSONArray listDeviceIdNotiSocket = (JSONArray)jsonConfirmSendNoti.get("list_device_id_noti_socket");
                        Util.addDebugLog("isApp========================"+isApp);
                        if (isApp == Constant.FLAG.ON) {
                            String from = (String) request.getParamValue(ParamKey.USER_ID);
                            String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            Util.addDebugLog("streamStatus======================================="+streamStatus);
                            if (streamStatus == null || Constant.STREAM_STATUS.OFF.equals(streamStatus)) {
                                JSONObject notiRequest = new JSONObject();
                                //add noti comment your buzz
                                    notiRequest.clear();
                                    notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_YOUR_BUZZ);
                                    notiRequest.put(ParamKey.FROM_USER_ID, from);
                                    notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                                    notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                                    notiRequest.put(ParamKey.IP, ip);
                                    notiRequest.put("list_user_comment", listUserCmt);// list user send Noti FCM
                                    notiRequest.put("list_user_noti_socket", listUserNotiSocket); // list User send Noti Websocket
                                    notiRequest.put("list_device_id_noti_socket", listDeviceIdNotiSocket); // list User send Noti Websocket
                                    InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                            }
                        }
                    }
                }

                dataObj.remove(ParamKey.TIME);
                dataObj.remove(ParamKey.LIST);
                dataObj.remove(ParamKey.BUZZ_ID);
                dataObj.remove(ParamKey.BUZZ_OWNER_ID);
                return buzzObj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ums;
            }
            
        }   
    }
     public static class BuzzLeaveSocket implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            Respond response = new Respond();
            try {
                sendBuzzLeaveWebSocket(request);
                response.code = 0;
            } catch (EazyException ex) {
                Util.addDebugLog(ex.toString());
            }
            return response.toString();
        }
         
     }
    
    public static class AddCommentVersion2 implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            request.put(ParamKey.API_NAME, API.ADD_COMMENT_GET_INFOR_VERSION_2);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                Long commentBuzzPoint = (Long) ((JSONObject) umsO.get(ParamKey.DATA)).get("comment_buzz_point");
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.ADD_COMMENT);
                // sent request add_comment
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
                Long time = (Long) dataObj.get(ParamKey.TIME);
                int isApp = ((Long) dataObj.get("is_app")).intValue();
                request.reqObj.put(ParamKey.TIME, time);
                request.reqObj.put("is_app", isApp);
                String buzzOwner = (String) dataObj.get(ParamKey.BUZZ_OWNER_ID);
                request.put(ParamKey.BUZZ_OWNER_ID, buzzOwner);

                request.put(ParamKey.API_NAME, API.ADD_COMMENT_VERSION_2);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject umsObj = new JSONObject();
                try {
                    umsObj = (JSONObject) new JSONParser().parse(umsStr);
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                }

//                JSONArray blackListArr = (JSONArray) ((JSONObject) umsObj.get(ParamKey.DATA)).get(ParamKey.BLACK_LIST);
                Long point = (Long) ((JSONObject) umsObj.get(ParamKey.DATA)).get(ParamKey.POINT);
                Long isNoti = (Long) ((JSONObject) umsObj.get(ParamKey.DATA)).get(ParamKey.IS_NOTI);
                if(isApp == Constant.FLAG.ON){
                    String from = (String) request.getParamValue(ParamKey.USER_ID);
                    String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                    String ip = (String) request.getParamValue(ParamKey.IP);
                    if (!blockList.contains(buzzOwner) && !deactiveList.contains(buzzOwner)) {
                        //add noti comment your buzz
                        if(!from.equals(buzzOwner)){
                            JSONObject notiRequest = new JSONObject();
                            notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_YOUR_BUZZ);
                            notiRequest.put(ParamKey.FROM_USER_ID, from);
                            notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                            notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                            notiRequest.put(ParamKey.IP, ip);
                            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                        }
                    }
                    if(isNoti != null && isNoti == Constant.FLAG.ON){
                        JSONObject notiRequest = new JSONObject();
                        notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_APPROVED);
                        notiRequest.put(ParamKey.TOUSERID, from);
                        notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                        notiRequest.put(ParamKey.IP, ip);
                        InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    }
                }
                dataObj.put(ParamKey.POINT, point);
                dataObj.put("comment_buzz_point", commentBuzzPoint);
//                dataObj.put(ParamKey.IS_APPROVED_IMAGE, Constant.NO);
                dataObj.remove(ParamKey.TIME);
                dataObj.remove(ParamKey.LIST);
                dataObj.remove(ParamKey.BUZZ_ID);
                dataObj.remove(ParamKey.BUZZ_OWNER_ID);
                return buzzObj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ums;
            }
        }
    }
    
    public static class AddSubComment implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String msgType = (String)request.getParamValue("msg_type");
            request.put(ParamKey.API_NAME, API.ADD_SUB_COMMENT_GET_INFOR);
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                Long commentBuzzPoint = (Long) ((JSONObject) umsO.get(ParamKey.DATA)).get("sub_comment_point");
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
                request.put(ParamKey.API_NAME, API.ADD_SUB_COMMENT);
                // sent request add_comment
                
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
                Long time = (Long) dataObj.get(ParamKey.TIME);
                int isApp = ((Long) dataObj.get("is_app")).intValue();
                request.reqObj.put(ParamKey.TIME, time);
                request.reqObj.put("is_app", isApp);
                String buzzOwner = (String) dataObj.get(ParamKey.BUZZ_OWNER_ID);
                request.put(ParamKey.BUZZ_OWNER_ID, buzzOwner);
                String commentOwner =(String) dataObj.get("comment_owner_id");
                request.put("comment_owner_id", commentOwner);
                JSONArray notificationList =  (JSONArray) dataObj.get("notification_list");
                request.reqObj.put("notification_list", notificationList);

                request.put(ParamKey.API_NAME, API.ADD_SUB_COMMENT);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject umsObj = new JSONObject();
                try {
                    umsObj = (JSONObject) new JSONParser().parse(umsStr);
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                }
          
                if (!blockList.contains(buzzOwner)) {
                    request.put("api", "list_sub_comment");
                    String subCmtId = (String) dataObj.get("sub_comment_id");
                    request.put("sub_comment_id", subCmtId);
                    String result = listSubComment.callService(request);
                    Util.addDebugLog("result===========================================" + result);
                    JSONObject cmtSubWebsocket = (JSONObject) new JSONParser().parse(result);
                    String resultListUserNoti = sendBuzzSubCmtWebSocket(request, cmtSubWebsocket,umsObj);
                    JSONObject listUserNoti = (JSONObject) new JSONParser().parse(resultListUserNoti);
                    Long isSendNoti = (Long) listUserNoti.get("code");
                    if(isSendNoti == ErrorCode.SUCCESS){
                        notificationList = (JSONArray)listUserNoti.get("notification_list");
                        JSONArray listUserNotiSocketSubCmt = (JSONArray)listUserNoti.get("list_user_noti_socket");
                        JSONArray listDeviceIdNotiSocket = (JSONArray)listUserNoti.get("list_device_id_noti_socket");
                        if(isApp == Constant.FLAG.ON){
                            sendNotiListUserReplyComment(request,dataObj,umsObj,notificationList,buzzOwner,listUserNotiSocketSubCmt,listDeviceIdNotiSocket);
                        }
                    }
                }
                Long point = (Long) ((JSONObject) umsObj.get(ParamKey.DATA)).get(ParamKey.POINT);
                dataObj.put(ParamKey.POINT, point);
                dataObj.put("sub_comment_point", commentBuzzPoint);
                dataObj.remove(ParamKey.TIME);
                dataObj.remove("notification_list");
                dataObj.remove(ParamKey.BUZZ_ID);
                dataObj.remove(ParamKey.BUZZ_OWNER_ID);
                dataObj.remove("comment_owner_id");
                dataObj.remove("cmt_id");
                return buzzObj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }

        private void sendNotiListUserReplyComment(Request request, JSONObject dataObj, JSONObject umsObj, JSONArray notificationList, String buzzOwner,JSONArray listUserNotiSocketSubCmt,JSONArray listDeviceIdNotiSocket) throws EazyException {
            JSONObject notiRequest = new JSONObject();
            String from = (String) request.getParamValue(ParamKey.USER_ID);
            String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
            String cmtId = (String) request.getParamValue(ParamKey.COMMENT_ID);
            String cmtValue = (String) dataObj.get("cmt_value");
            String cmtUserId = (String) dataObj.get("cmt_user_id");
            String cmtTime = (String) dataObj.get("cmt_time");
            String ip = (String) request.getParamValue(ParamKey.IP);
            String buzzOwnerName = (String) ((JSONObject) umsObj.get(ParamKey.DATA)).get("buzz_owner_name");
            notificationList = (JSONArray) ((JSONObject) umsObj.get(ParamKey.DATA)).get("notification_list");

            notiRequest.put(ParamKey.API_NAME, API.NOTI_REPLY_YOUR_COMMENT);
            notiRequest.put(ParamKey.FROM_USER_ID, from);
            notiRequest.put("buzz_owner_name", buzzOwnerName);
            notiRequest.put(ParamKey.TO_LIST_USER_ID, notificationList);
            notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
            notiRequest.put(ParamKey.IP, ip);
            notiRequest.put(ParamKey.COMMENT_ID, cmtId);
            notiRequest.put("cmt_value", cmtValue);
            notiRequest.put("cmt_user_id", cmtUserId);
            notiRequest.put("cmt_time", cmtTime);
            notiRequest.put("list_user_noti_socket", listUserNotiSocketSubCmt);
            notiRequest.put("list_device_id_noti_socket", listDeviceIdNotiSocket);
            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
        }
    }
    
    private static String sendBuzzSubCmtWebSocket(Request request,JSONObject buzzObj,JSONObject umsObj) throws EazyException{
        String parCmtId = (String) request.getParamValue("cmt_id");
        Util.addDebugLog("buzzObj================================"+buzzObj.toJSONString());
        JSONArray arrayObj = (JSONArray) buzzObj.get(ParamKey.DATA);
        
        //arrayObj has 1 element is comment add, because edit api lst_cmt return 1 element with request cmt_id
        JSONObject dataObj = (JSONObject)arrayObj.get(0);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        
        JSONArray notificationList = (JSONArray) ((JSONObject) umsObj.get(ParamKey.DATA)).get("notification_list");
        
        Long code = (Long)buzzObj.get("code");
        dataObj.put("code", code);
        dataObj.put(ParamKey.API_NAME, API.SUB_COMMENT_WEBSOCKET);
        dataObj.put(ParamKey.FROM, userId);
        dataObj.put("msg_type", "BUZZSUBCMT");
        dataObj.put(ParamKey.BUZZ_ID, buzzId);
        dataObj.put(ParamKey.USER_ID, userId);
        dataObj.put(ParamKey.COMMENT_ID, parCmtId);
        dataObj.put("notification_list", notificationList);
        Util.addDebugLog("dataObj========================================"+dataObj.toJSONString());
        return InterCommunicator.sendRequest(dataObj.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    
    private static void sendBuzzDelCmtWebsocket(Request request,JSONObject buzzObj){
        Util.addDebugLog("request========================"+request.toString());
        JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        String cmtId = (String) request.getParamValue(ParamKey.COMMENT_ID);
        
        dataObj.put(ParamKey.API_NAME, API.DELETE_COMMENT);
        dataObj.put(ParamKey.USER_ID, userId);
        dataObj.put(ParamKey.FROM, userId);
        dataObj.put("msg_type", "BUZZDELCMT");
        dataObj.put(ParamKey.BUZZ_ID, buzzId);
        dataObj.put(ParamKey.COMMENT_ID, cmtId);
        Util.addDebugLog("dataObj========================================"+dataObj.toJSONString());
        InterCommunicator.sendRequest(dataObj.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    
    private static void sendBuzzDelSubCmtWebsocket(Request request,JSONObject buzzObj){
        JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        String cmtId = (String) request.getParamValue(ParamKey.COMMENT_ID);
        String subCmtId = (String) request.getParamValue("sub_comment_id");
        
        dataObj.put(ParamKey.API_NAME, API.DELETE_SUB_COMMENT);
        dataObj.put(ParamKey.USER_ID, userId);
        dataObj.put(ParamKey.FROM, userId);
        dataObj.put("msg_type", "BUZZDELSUBCMT");
        dataObj.put(ParamKey.BUZZ_ID, buzzId);
        dataObj.put(ParamKey.COMMENT_ID, cmtId);
        dataObj.put("sub_comment_id", subCmtId);
        Util.addDebugLog("dataObj========================================"+dataObj.toJSONString());
        InterCommunicator.sendRequest(dataObj.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    
    private static String sendBuzzCmtWebSocket(Request request,JSONObject buzzObj) throws EazyException{
        JSONArray arrayObj = (JSONArray) buzzObj.get(ParamKey.DATA);
        //arrayObj has 1 element is comment add, because edit api lst_cmt return 1 element with request cmt_id
        JSONObject dataObj = (JSONObject)arrayObj.get(0);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        Long code = (Long) buzzObj.get("code");

        dataObj.put(ParamKey.API_NAME, API.COMMENT_WEBSOCKET);
        dataObj.put(ParamKey.FROM, userId);
        dataObj.put(ParamKey.BUZZ_ID, buzzId);
        dataObj.put("code", code);
        dataObj.put("msg_type", "BUZZCMT");
        return InterCommunicator.sendRequest(dataObj.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    
    private static void sendBuzzJoinWebSocket(Request request,JSONObject jo) throws EazyException{
        JSONObject buzz = (JSONObject) jo.get(ParamKey.DATA);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        String ownBuzzId = (String) buzz.get(ParamKey.USER_ID);
        Long code = (Long)jo.get("code");
        JSONObject webSocketRequest = new JSONObject();
        
        webSocketRequest.put(ParamKey.API_NAME, API.BUZZ_JOIN_WEBSOCKET);
        webSocketRequest.put(ParamKey.FROM, userId);
        webSocketRequest.put(ParamKey.BUZZ_ID, buzzId);
        webSocketRequest.put("own_buzz_id", ownBuzzId);
        webSocketRequest.put("msg_type", "BUZZJOIN");
        webSocketRequest.put("code", code);
        InterCommunicator.sendRequest(webSocketRequest.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    private static void sendGetBuzzJoinWebSocket(Request request,JSONObject jo) throws EazyException{
        List<String> listBuzzId = new ArrayList<>();
        List<String> listUserId = new ArrayList<>();
        JSONArray buzz = (JSONArray) jo.get(ParamKey.DATA);
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        if(buzz != null && !buzz.isEmpty()){
            for(Object obj : buzz){
                JSONObject json = (JSONObject)obj;
                String buzzId = (String)json.get("buzz_id");
                if(buzzId != null)
                    listBuzzId.add(buzzId);
                String ownBuzzId = (String) json.get(ParamKey.USER_ID);
                if(ownBuzzId != null){
                    listUserId.add(userId);
                }
            }
        }
        
        Long code = (Long)jo.get("code");
        JSONObject webSocketRequest = new JSONObject();
        
        webSocketRequest.put(ParamKey.API_NAME, API.BUZZ_JOIN_WEBSOCKET);
        webSocketRequest.put(ParamKey.FROM, userId);
        webSocketRequest.put("list_buzz_id", listBuzzId);
        webSocketRequest.put("list_own_buzz_id", listUserId);
        webSocketRequest.put("msg_type", "BUZZJOIN");
        webSocketRequest.put("code", code);
        if(userId != null){
            InterCommunicator.sendRequest(webSocketRequest.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
        }
    }
    
    private static void sendBuzzLeaveWebSocket(Request request) throws EazyException{
        String userId = (String) request.getParamValue(ParamKey.USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        JSONObject webSocketRequest = new JSONObject();
        
        webSocketRequest.put(ParamKey.API_NAME, API.BUZZ_LEAVE_WEBSOCKET);
        webSocketRequest.put(ParamKey.FROM, userId);
        webSocketRequest.put(ParamKey.BUZZ_ID, buzzId);
        webSocketRequest.put("msg_type", "BUZZLEAVE");
        InterCommunicator.sendRequest(webSocketRequest.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
    }
    
    private static void sendBuzzTagWebSocket(Request request) throws EazyException, ParseException{
        
        String from = (String) request.getParamValue(ParamKey.USER_ID);
        String to = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
        String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
        Long buzzTypeShare = (Long) request.getParamValue(ParamKey.BUZZ_TYPE_OTHER_SHARE);//buzz share 
        String listTo = (String) request.getParamValue(ParamKey.TAG_LIST);
        
        User user = UserDAO.getUserInfor(from);
        String userName = user.username;
        String avaId = user.avatarId;
        Long gender = user.gender;
        FileUrl url = null;
        JSONObject webSocketRequest = new JSONObject();
        if (avaId != null) {
            List<String> listImgId = new LinkedList<>();
            listImgId.add(avaId);
            HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
            url = imgMap.get(avaId);
            if (url != null){
                webSocketRequest.put("ava", url.getThumbnail());
//                        joData.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
            }
        }
        webSocketRequest.put(ParamKey.API_NAME, API.ADD_TAG_WEBSOCKET);
        webSocketRequest.put(ParamKey.FROM, from);
        if(to != null){
            webSocketRequest.put("to", to);
        }else{
            webSocketRequest.put("list_to", listTo);
        }
        webSocketRequest.put(ParamKey.BUZZ_ID, buzzId);
        webSocketRequest.put(ParamKey.USER_NAME, userName);
        webSocketRequest.put("msg_type", "BUZZTAG");
        webSocketRequest.put(ParamKey.BUZZ_TYPE, buzzTypeShare);
        webSocketRequest.put("code",ErrorCode.SUCCESS);
        Util.addDebugLog("webSocketRequest====================================" + webSocketRequest.toJSONString());
        InterCommunicator.sendRequest(webSocketRequest.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
        
        
        //get buzz_type liveStream 
        Long buzz_type = (Long) request.getParamValue("buzz_type");
        if (buzz_type == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS) {
            //send addtag JPNS
            JSONObject objNoti = new JSONObject();
            String streamId = (String) request.getParamValue("stream_id");
            objNoti.put(ParamKey.API_NAME, API.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST);
            objNoti.put(ParamKey.USER_NAME, userName);
            objNoti.put(ParamKey.FROM_USER_ID, from);
            if (to != null) {
                JSONArray listToUserId = new JSONArray();
                listToUserId.add(to);
                objNoti.put(ParamKey.TO_LIST_USER_ID, listToUserId);
            } else {
                objNoti.put(ParamKey.TO_LIST_USER_ID, listTo);
            }
            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
            if (streamId != null) {
                objNoti.put(ParamKey.STREAM_ID, streamId);
            }

            InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
            return;
        }

        JSONArray listFvt = new JSONArray();
        JSONParser parser = new JSONParser();
        if (to != null) {
            listFvt.add(to);
        } else {
            listFvt = (JSONArray) parser.parse(listTo);
        }
        if (listFvt != null && !listFvt.isEmpty()) {
            JSONObject objNoti = new JSONObject();
            objNoti.put(ParamKey.API_NAME, API.NOTI_SHARE_MUSIC);
            objNoti.put(ParamKey.USER_NAME, userName);
            objNoti.put(ParamKey.FROM_USER_ID, from);
            objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
            InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
        }
        
    }
        
    public static class Like implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            request.put(ParamKey.API_NAME, API.GET_BLACK_LIST);
            Util.addDebugLog("--------------------------- LIKE REQUEST JSON " + request.toJson());
            String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            try {
                JSONObject umsO = (JSONObject) new JSONParser().parse(ums);
                Long codeUms = (Long) umsO.get(ParamKey.ERROR_CODE);
                if (codeUms != ErrorCode.SUCCESS) {
                    return ums;
                }
                JSONArray blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
//                request.reqObj.put(ParamKey.DEACTIVE_LIST, deactiveList);
                request.put(ParamKey.API_NAME, API.LIKE_BUZZ);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);

                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                Long likeType = (Long) request.getParamValue("like_type");
                if (likeType == Constant.FLAG.ON) {
//                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
//                    buzzObj.remove(ParamKey.DATA);
                    JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
                    Long time = (Long) dataObj.get(ParamKey.TIME);
                    request.reqObj.put(ParamKey.TIME, time);
                    JSONArray list = (JSONArray) dataObj.get(ParamKey.LIST);
                    request.reqObj.put(ParamKey.LIST, list);
                    String buzzOwner = (String) dataObj.get(ParamKey.BUZZ_OWNER_ID);
                    request.put(ParamKey.BUZZ_OWNER_ID, buzzOwner);
                    
                    // Linh 2016-9-29
                    JSONObject newRq = new JSONObject();
                    newRq.put(ParamKey.USER_ID, buzzOwner);
                    newRq.put(ParamKey.API_NAME, API.GET_NOTIFICATION_SETTING);
                    String data = InterCommunicator.sendRequest(newRq.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                    
                    JSONObject setting = (JSONObject) new JSONParser().parse(data);
                    JSONObject settingO = (JSONObject) new JSONParser().parse(setting.get(ParamKey.DATA).toString());
                    JSONObject settingData = (JSONObject) new JSONParser().parse(settingO.get("setting").toString());
                    Object notiLike = settingData.get("noti_like");
                    int isNoti;
                    if (notiLike != null) {
                        Integer notiLike1 = new Integer(notiLike.toString());
                        isNoti =  notiLike1;
                    } else {
                        isNoti = Constant.FLAG.ON;
                    }
                    //
                    
//                    Long isNoti = (Long) dataObj.get(ParamKey.IS_NOTI);
                    request.reqObj.put(ParamKey.IS_NOTI, isNoti);

                    JSONObject notiRequest = new JSONObject();
                    String from = (String) request.getParamValue(ParamKey.USER_ID);
                    String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);

                    InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                    //if (isNoti == Constant.FLAG.ON) {
//                        JSONObject umsObj = new JSONObject();
//                        try {
//                            umsObj = (JSONObject) new JSONParser().parse(umsStr);
//                        } catch (Exception ex) {
//                            Util.addErrorLog(ex);
//                        }
                        String ip = (String) request.getParamValue(ParamKey.IP);

//                        JSONArray blackList = (JSONArray) ((JSONObject) umsObj.get(ParamKey.DATA)).get(ParamKey.BLACK_LIST);
                        if (!blockList.contains(buzzOwner) && !deactiveList.contains(buzzOwner)
                               && !from.equals(buzzOwner) && NotificationSettingDAO.checkUserNotification(buzzOwner, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI)) {
                            //add noti comment your buzz
                            notiRequest.clear();
                            notiRequest.put(ParamKey.API_NAME, API.NOTI_LIKE_YOUR_BUZZ);
                            notiRequest.put(ParamKey.FROM_USER_ID, from);
                            notiRequest.put(ParamKey.TOUSERID, buzzOwner);
                            notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                            notiRequest.put(ParamKey.IP, ip);
                            Util.addDebugLog("--------------------------- LIKE REQUEST JSON to JSPN" + notiRequest.toJSONString());
                            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                        }
                    //}
                    buzzObj.remove(ParamKey.DATA);
                }
                return buzzObj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ums;
            }
        }
    }

    public static class AddBuzz implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            Util.addDebugLog("===========AddBuzz========");
            Long buzzType = (Long) request.getParamValue(ParamKey.BUZZ_TYPE);
            Long isApp = (Long) request.getParamValue(ParamKey.IS_APPROVED_IMAGE);
            Long privacy = (Long) request.getParamValue(ParamKey.PRIVACY);
            String shareId = (String) request.getParamValue(ParamKey.SHARE_ID);
            String tagList = (String) request.getParamValue(ParamKey.TAG_LIST);
            
            Util.addDebugLog("===========buzzType========"+buzzType);
            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ && isApp == null) {
                request.put(ParamKey.API_NAME, API.GET_IMAGE_STATUS_INFOR);
                String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                try {
                    JSONObject umsJson = (JSONObject) new JSONParser().parse(ums);
                    Long code = (Long) umsJson.get(ParamKey.ERROR_CODE);
                    if (code != ErrorCode.SUCCESS) {
                        return ums;
                    }
                    JSONObject dataObj = (JSONObject) umsJson.get(ParamKey.DATA);
                    isApp = (Long) dataObj.get(ParamKey.IS_APPROVED_IMAGE);
                    if (isApp == Constant.REVIEW_STATUS_FLAG.DENIED) {
                        return ResponseMessage.UnknownError;
                    }
                    request.reqObj.put(ParamKey.IS_APPROVED_IMAGE, isApp);
                    request.put(ParamKey.API_NAME, API.ADD_BUZZ);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    return ResponseMessage.UnknownError;
                }
            }
            String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
            try {
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                
                JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
                isApp = (Long) dataObj.get(ParamKey.IS_APPROVED_IMAGE);
                Long commentApp = (Long) dataObj.get("comment_app");
                String buzzId = (String) dataObj.get(ParamKey.BUZZ_ID);
                String imageId = (String) request.getParamValue(ParamKey.BUZZ_VALUE);
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                request.reqObj.put(ParamKey.IS_APPROVED_IMAGE, isApp);
                request.reqObj.put("comment_app", commentApp);
                request.put(ParamKey.BUZZ_ID, buzzId);
                request.put(ParamKey.IMAGE_ID, imageId);
                request.put(ParamKey.USER_ID, userId);
                request.put(ParamKey.SHARE_ID, shareId);
                request.put(ParamKey.TAG_LIST, tagList);
                Long time = (Long) dataObj.get(ParamKey.TIME);
                request.reqObj.put(ParamKey.TIME, time);
                request.put(ParamKey.API_NAME, API.ADD_BUZZ);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);

                JSONObject obj = (JSONObject) new JSONParser().parse(umsStr);
                Long error = (Long) obj.get(ParamKey.ERROR_CODE);
                //Notify to all user in list favoristed
                String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
                if (error == ErrorCode.SUCCESS && privacy != 2 && shareId == null) {
                    if(buzzType != Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                        JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
                        Long isNoti = (Long) data.get(ParamKey.IS_NOTI);
                        if (isNoti != null && isNoti == Constant.FLAG.ON && Setting.auto_approved_buzz == 1) {
                            request.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                            if(buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS)
                                request.put(ParamKey.API_NAME, API.NOTI_BUZZ_APPROVED);
                            else if(buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS)
                                request.put(ParamKey.API_NAME, API.NOTI_TEXT_BUZZ_APPROVED);
                            request.put(ParamKey.TOUSERID, userId);
                            request.put(ParamKey.NOTI_IMAGE_ID, imageId);                              
                            InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                        }
                        Long isNotiComment = (Long) data.get("is_noti_comment");
                        Util.addDebugLog("===========AddBuzz isNotiComment========"+isNotiComment);
                        if(isNotiComment != null && isNotiComment == Constant.FLAG.ON){
                            JSONObject notiRequest = new JSONObject();
                            notiRequest.put(ParamKey.API_NAME, API.NOTI_COMMENT_APPROVED);
                            notiRequest.put(ParamKey.TOUSERID, userId);
                            notiRequest.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                            notiRequest.put(ParamKey.IP, ip);
                            InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
                        }
                        JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
                        Util.addDebugLog("===========AddBuzz listFvt========"+listFvt);
                        if(listFvt != null && !listFvt.isEmpty()){
                            String userName = (String) data.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
                            JSONObject objNoti = new JSONObject();
                            objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
                            objNoti.put(ParamKey.USER_NAME, userName);
                            objNoti.put(ParamKey.FROM_USER_ID, userId);
                            objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                            objNoti.put(ParamKey.IP, ip);

                            InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                        }
                    }
                    
                    //get buzz_type liveStream
                    //notify user tag in buzz
                    JSONArray listFvt = new JSONArray();
                    JSONParser parser = new JSONParser();
                    String to = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                    String from = (String) request.getParamValue(ParamKey.USER_ID);
                    User user = UserDAO.getUserInfor(from);
                    String userName = user.username;
                    if (to != null) {
                        listFvt.add(to);
                    } else {
                        listFvt = (JSONArray) parser.parse(tagList);
                    }
                    if (listFvt != null && !listFvt.isEmpty() && buzzType != Constant.BUZZ_TYPE_VALUE.STREAM_STATUS) {
                        JSONObject objNoti = new JSONObject();
                        objNoti.put(ParamKey.API_NAME, API.NOTI_HAS_TAG_IN_BUZZ);
                        objNoti.put(ParamKey.USER_NAME, userName);
                        objNoti.put(ParamKey.FROM_USER_ID, from);
                        objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                        objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                        InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);

                    }
//                    else{
//                        JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
//                        JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
//                        String listTo = (String) request.getParamValue(ParamKey.TAG_LIST);
//                        Util.addDebugLog("===========AddBuzz listFvt========" + listFvt);
//                        if (listFvt != null && !listFvt.isEmpty()) {
//                            String userName = (String) data.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
//                            JSONObject objNoti = new JSONObject();
//                            objNoti.put(ParamKey.API_NAME, API.NOTI_LIVESTREAM_FROM_FAVOURIST);
//                            objNoti.put(ParamKey.USER_NAME, userName);
//                            objNoti.put(ParamKey.FROM_USER_ID, userId);
//                            objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
//                            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
//                            objNoti.put(ParamKey.TAG_LIST, listTo);
//                            objNoti.put(ParamKey.IP, ip);
//
//                            InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
//                        }
//                    }
                }
               
                if(shareId != null){
                    sendBuzzTagWebSocket(request);
                }
                dataObj.remove(ParamKey.TIME);
                dataObj.remove(ParamKey.USER_ID);
                dataObj.remove(ParamKey.BUZZ_TYPE);
                Util.addDebugLog("===========AddBuzz buzzObj========"+buzzObj);
                return buzzObj.toJSONString();
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return buzzStr;
            }
        }
    }
    
    public static class UploadStreamFile implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                return InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
        
    }
    
    public static class UpdateTag implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                sendBuzzTagWebSocket(request);
                return InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
        
    }
    
    public static class AddTag implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                String result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONParser parser = new JSONParser();
                JSONObject jsonBuzz = (JSONObject)parser.parse(result);
                Long buzzType = (Long)jsonBuzz.get("buzz_type");
                String streamId = (String)jsonBuzz.get("stream_id");
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
//                String userName = (String) request.getParamValue(ParamKey.USER_NAME);
                String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                String reqUserId = (String) request.getParamValue(ParamKey.REQUEST_USER_ID);
                
                String userName = (String) UserDAO.getUserInfor(userId, ParamKey.USER_NAME);
                String buzzString = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                JSONObject buzzJson = new JSONObject();
                buzzJson = (JSONObject) new JSONParser().parse(buzzString);
                Long umsCode = (Long) buzzJson.get(ParamKey.ERROR_CODE);
                if (umsCode == ErrorCode.SUCCESS) {
                    
                    JSONArray listFvt = new JSONArray();
                    listFvt.add(reqUserId);
                    if(listFvt != null && !listFvt.isEmpty() && buzzType != Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                        JSONObject objNoti = new JSONObject();
                        String apiName = API.NOTI_HAS_TAG_IN_BUZZ;
                        if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS){
                            apiName = API.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST;
                        }
                        objNoti.put(ParamKey.API_NAME, apiName);
                        objNoti.put(ParamKey.USER_NAME, userName);
                        objNoti.put(ParamKey.FROM_USER_ID, userId);
                        objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
                        objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
                        if(streamId != null){
                            objNoti.put(ParamKey.STREAM_ID, streamId);
                        }

                        InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                    
                    }
                    request.reqObj.put("buzz_type", buzzType);

                    sendBuzzTagWebSocket(request);

                }
                return buzzString;
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
        
    }
    

    public static class Gift implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            try {
                LinkedList<String> giftList = new LinkedList<>();
                String giftId = (String) request.getParamValue(ParamKey.GIFT_ID);
                String reciveId = (String) request.getParamValue(ParamKey.RECEIVER_ID);
                String ip = (String) request.getParamValue(ParamKey.IP);
                giftList.add(giftId);
                JSONArray gifArray = getListGift(request, giftList);
                JSONObject gift = (JSONObject) gifArray.get(0);
                Double giftPrice = (Double) gift.get(ParamKey.GIFT_PRICE);
                String sender = (String) request.getParamValue(ParamKey.USER_ID);
                JSONObject umsRequest = new JSONObject();
                umsRequest.put(ParamKey.API_NAME, API.GIFT);
                umsRequest.put(ParamKey.USER_ID, sender);
                umsRequest.put(ParamKey.RECEIVER_ID, reciveId);

                int price = giftPrice.intValue();
                String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
                User u1 = UserDAO.getUserInfor(reciveId);
                User u2 = UserDAO.getUserInfor(sender);
                if (u1.deviceType == 0) {
                    if (u1.appVersion.equals(safaryVersion)) {
                        price = 0;
                    }
                }
                if (u2.deviceType == 0) {
                    if (u2.appVersion.equals(safaryVersion)) {
                        price = 0;
                    }
                }

                
                umsRequest.put(ParamKey.GIFT_PRICE, price);
                umsRequest.put(ParamKey.IP, ip);
                String umsString = InterCommunicator.sendRequest(umsRequest.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                JSONObject umsJson = new JSONObject();
                try {
                    umsJson = (JSONObject) new JSONParser().parse(umsString);
                } catch (ParseException ex) {
                    Util.addErrorLog(ex);
                }
                Long umsCode = (Long) umsJson.get(ParamKey.ERROR_CODE);
                if (umsCode == ErrorCode.SUCCESS) {
//                    Long point = (Long) ((JSONObject) umsJson.get(ParamKey.DATA)).get(ParamKey.POINT);
                    //request.put(ParamKey.API_NAME, API.SEND_GIFT);
//                    String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
//                    JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
//                    Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
//                    if (code != ErrorCode.SUCCESS) {
//                        return buzzStr;
//                    }
//                    JSONObject dataObj = (JSONObject) buzzObj.get(ParamKey.DATA);
//                    String userId = (String) dataObj.get(ParamKey.USER_ID);
//                    Long time = (Long) dataObj.get(ParamKey.TIME);
//                    String buzzId = (String) dataObj.get(ParamKey.BUZZ_ID);
                    Long time = new Date().getTime();
                    request.put(ParamKey.USER_ID, reciveId);
                    request.reqObj.put(ParamKey.TIME, time);
                    request.put(ParamKey.SENDER, sender);
                    //request.put(ParamKey.BUZZ_ID, buzzId);
                    request.put(ParamKey.API_NAME, API.SEND_GIFT);
                    String ums = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);

                    JSONObject obj = (JSONObject) new JSONParser().parse(ums);
                    Long error = (Long) obj.get(ParamKey.ERROR_CODE);
                    if (error == ErrorCode.SUCCESS) {
                        JSONObject data = (JSONObject) obj.get(ParamKey.DATA);
//                        JSONArray listFvt = (JSONArray) data.get(ParamKey.FAVORITED_LIST);
//                        if(listFvt != null && !listFvt.isEmpty()){
//                            String userName = (String) data.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
//                            Util.addDebugLog("========Username Gift===:" +userName);
//                            JSONObject objNoti = new JSONObject();
//                            objNoti.put(ParamKey.API_NAME, API.NOTI_NEW_BUZZ_FROM_FAVORIST);
//                            objNoti.put(ParamKey.USER_NAME, userName);
//                            objNoti.put(ParamKey.FROM_USER_ID, userId);
//                            objNoti.put(ParamKey.TO_LIST_USER_ID, listFvt);
//                            objNoti.put(ParamKey.NOTI_BUZZ_ID, buzzId);
//
//                            objNoti.put(ParamKey.IP, ip);
//
//                            InterCommunicator.sendRequest(objNoti.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
//                        }
                        
                        JSONObject jsonObject = new JSONObject();
                        String token = (String) request.getParamValue(ParamKey.TOKEN_STRING);
                        jsonObject.put(ParamKey.API_NAME, API.SEND_GIFT);
                        jsonObject.put("value", giftId);
                        jsonObject.put("from", sender);
                        jsonObject.put("to", reciveId);
                        jsonObject.put("token", token);
                        jsonObject.put("msg_type", "GIFT");
//                        jsonObject.put("origin_time", Util.currentTime()+"");
                        jsonObject.put("id", sender + "&" + reciveId + "&" + DateFormat.format_yyyyMMddHHmmssSSS(Util.currentTime()));
                        InterCommunicator.sendRequest(jsonObject.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
                    }
                    return umsString;
                } else {
                    return umsString;
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }
    public static class CheckBuzz implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try {
                request.put(ParamKey.API_NAME, API.GET_BUZZ);
                String umsStr = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONArray blockList = new JSONArray();
                JSONArray friendList = new JSONArray();
                if (umsStr != null) {

                    JSONObject umsO = (JSONObject) new JSONParser().parse(umsStr);
//                    if (umsO!=null){
//                        blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
//                    }
                    if (umsO != null) {
                        if (umsO.get(ParamKey.DATA) != null) {
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST) != null) {
                                blockList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.BLOCK_LIST);
                            }
                            if (((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.FRIEND_LIST) != null) {
                                friendList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.FRIEND_LIST);
                            }
                        }
                    }

                }
//                JSONArray deactiveList = (JSONArray) ((JSONObject) umsO.get(ParamKey.DATA)).get(ParamKey.DEACTIVE_LIST);
                request.reqObj.put(ParamKey.BLOCK_LIST, blockList);
                request.reqObj.put(ParamKey.FRIEND_LIST, friendList);
                request.put(ParamKey.API_NAME, API.CHECK_BUZZ_WEBSOCKET);
                String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
                String userId = (String) request.getParamValue(ParamKey.USER_ID);
                String buzzId = (String) request.getParamValue(ParamKey.BUZZ_ID);
                JSONObject jo = (JSONObject) new JSONParser().parse(buzzStr);
                jo.put("msg_type", "BUZZJOIN");
                jo.put(ParamKey.API_NAME, API.CHECK_BUZZ_WEBSOCKET);
                jo.put(ParamKey.FROM, userId);
                jo.put(ParamKey.BUZZ_ID, buzzId);
                InterCommunicator.sendRequest(jo.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
            } catch (ParseException ex) {
                Logger.getLogger(BuzzAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return request.toString();
        }
        
    }
    
    
    public static class DelBuzz implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String buzzStr = InterCommunicator.sendRequest(request.toJson(), Config.BuzzServerIP, Config.BuzzServerPort);
            try {
                JSONObject buzzObj = (JSONObject) new JSONParser().parse(buzzStr);
                buzzObj.put("api", "del_buzz");
                Long code = (Long) buzzObj.get(ParamKey.ERROR_CODE);
                if (code != ErrorCode.SUCCESS) {
                    return buzzStr;
                }
                
//                JSONArray dataObj = (JSONArray) buzzObj.get(ParamKey.DATA);
//                Long buzzType = (Long) dataObj.get(ParamKey.BUZZ_TYPE);
//                String buzzVal = (String) dataObj.get(ParamKey.BUZZ_VALUE);
//                Long isStatus = (Long) dataObj.get(ParamKey.IS_STATUS);
//                String fileId = (String) dataObj.get(ParamKey.FILE_ID);
//                Long isAva = (long) Constant.FLAG.OFF;
//                request.reqObj.put(ParamKey.BUZZ_TYPE, buzzType);
//                request.reqObj.put(ParamKey.BUZZ_VALUE, buzzVal);
//                request.reqObj.put(ParamKey.IS_STATUS, isStatus);
//                request.reqObj.put(ParamKey.FILE_ID, fileId);

                String umsString = InterCommunicator.sendRequest(buzzObj.toJSONString(), Config.UMSServerIP, Config.UMSPort);
                JSONArray dataJson = (JSONArray) ((JSONObject) new JSONParser().parse(umsString)).get(ParamKey.DATA);
                if (dataJson != null && !dataJson.isEmpty()) {
                    for(Object obj : dataJson){
                        JSONObject jsonObj = (JSONObject)obj;
                        Long isAva = (long) Constant.FLAG.OFF;
                        isAva = (Long) jsonObj.get(ParamKey.IS_AVATAR);
                        if (isAva == Constant.FLAG.ON) {
                            request.put(ParamKey.API_NAME, API.UPDATE_USER_INFOR);
                            request.put(ParamKey.IMAGE_ID, Constant.NO_AVATAR_STRING_VALUE);
                            InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                        }
                        jsonObj.clear();
                        jsonObj.put(ParamKey.IS_AVATAR, isAva);
                    }
                }
                
//                String buzzId = Util.getStringParam(request, ParamKey.BUZZ_ID);
//                request.put(ParamKey.BUZZ_ID, buzzId);
//                String userId = Util.getStringParam(request, ParamKey.USER_ID);
//                request.put(ParamKey.USER_ID, userId);
//                request.put(ParamKey.API_NAME, API.PROCESS_DELETE_BUZZ);
//                String ip = Util.getStringParam(request, ParamKey.IP);
//                request.put(ParamKey.IP, ip);
//                request.reqObj.put(ParamKey.FLAG, -1);
//                String result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort); 
//                JSONObject resultJson = (JSONObject) new JSONParser().parse(result);
//                JSONObject resultData = (JSONObject) resultJson.get(ParamKey.DATA);
//                resultData.put(ParamKey.IS_AVATAR, isAva);
                
                return umsString;
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return buzzStr;
            }
        }
    }

    public static JSONArray getListGift(Request request, LinkedList<String> llGift) {
        try {
            request.reqObj.put("lst_gift", llGift);
            request.put(ParamKey.API_NAME, API.GET_LIST_GIFT);

            MixService ms = new MixService();
            String result = ms.callApi(Request.initRequest(request.toJson()));
            JSONObject obj = (JSONObject) new JSONParser().parse(result);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONArray arrayGift = (JSONArray) obj.get(ParamKey.DATA);
                return arrayGift;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return null;
    }  
    
    private static class BuzzObject implements Comparable<BuzzObject> {

        public String buzzOwnerId;
        public int index;

        public BuzzObject(String buzzOwnerId, int index) {
            super();
            this.buzzOwnerId = buzzOwnerId;
            this.index = index;
        }

        @Override
        public int compareTo(BuzzObject obj) {
            //ascending order
            return this.buzzOwnerId.compareTo(obj.buzzOwnerId);
        }
    }

    private static class UMSObject implements Comparable<UMSObject> {

        public String giftId;
        public String giftInfor;

        public UMSObject(String userId, String giftInfor) {
            super();
            this.giftId = userId;
            this.giftInfor = giftInfor;
        }

        @Override
        public int compareTo(UMSObject obj) {
            //ascending order
            return this.giftId.compareTo(obj.giftId);
        }
    }

    private static class InviteFriend implements IServiceAdapter{
        String result;
        @Override
        public String callService(Request request) {
            try{
                JSONObject jsonObject = request.reqObj;
                String uId = (String)jsonObject.get(ParamKey.USER_ID);
                Util.addDebugLog("uId---------------------------------"+uId);
                String buzzId = (String) jsonObject.get(ParamKey.BUZZ_ID);
                String ip = (String) jsonObject.get(ParamKey.IP);
                JSONArray listFriendId = (JSONArray) jsonObject.get(ParamKey.LIST_FRIEND);
                for(Object o : listFriendId){
                    String friendId = (String)o;

                    jsonObject.put(ParamKey.API_NAME, API.GET_MY_PAGE_INFOR);
                    jsonObject.put(ParamKey.USER_ID, friendId);
                    String umsResult = InterCommunicator.sendRequest(request.toString(), Config.UMSServerIP, Config.UMSPort);
                    Util.addDebugLog("========== umsResult: "+umsResult);
                    JSONObject unreadNotiObj = Util.toJSONObject(umsResult);
                    JSONObject unreadNoti = (JSONObject) unreadNotiObj.get("data");
                    long notiLikeNum = (Long) unreadNoti.get("noti_like_num");
                    long notiNum = (Long) unreadNoti.get("noti_num");
                    long notiQANum = (Long) unreadNoti.get("noti_qa_num");
                    long notiNewsNum = (Long) unreadNoti.get("noti_news_num");
    //                jsonObject.put(ParamKey.API_NAME, API.TOTAL_UNREAD);
    //                String chatResult = InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
    //                Util.addDebugLog("========== chatResult: "+chatResult);
    //                int unreadMessage = new Integer(chatResult);

                    long badge = notiLikeNum + notiNum + notiQANum + notiNewsNum ;
                    Util.addDebugLog("========== total badge: "+badge);

                    sendJPNSWithInviteFriend(uId, friendId, API.NOTI_INVITE_FRIEND,(int) badge, ip);
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
                result = ResponseMessage.UnknownError;
            }
            result = ResponseMessage.SuccessMessage;
            return  result;
        }
    }
    private static void sendJPNSWithInviteFriend(String fromUserid, String toUserid, String api, int badge,String ip) {

        JSONObject jo = new JSONObject();
        jo.put(ParamKey.FROM_USER_ID, fromUserid);
        jo.put(ParamKey.TOUSERID, toUserid);
        jo.put(ParamKey.API_NAME, api);
//        jo.put(ParamKey.BADGE, badge + 1);
        jo.put(ParamKey.BADGE, badge);
        jo.put(ParamKey.IP, ip);

        String msg = jo.toJSONString();
        Util.sendRequest(msg, Config.NotificationServerIP, Config.NotificationPort);
        }
}
