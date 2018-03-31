/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.ListFileUrl;
import eazycommon.constant.Constant;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tuannxv00804
 */
public class InterCommunicator {

    public static String sendRequest(String requestString, String serverIP, int serverPort) {
        String result = null;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);                 
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return result;

    }

    public static byte[] sendCSVRequest(String requestString, String serverIP, int serverPort) {
        byte[] result = null;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);                 
            }
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (in == null) {
                return null;
            }
            byte[] buffer = new byte[1];
            while ((in.read(buffer, 0, 1)) != -1) {
                bos.write(buffer);
            }
            bos.flush();
            bos.close();
            result = bos.toByteArray();

        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return result;

    }

    public static JSONArray getUserPresentList(String userId, Collection<String> llEmail) {
        try {
            JSONObject psJO = new JSONObject();
            psJO.put(ParamKey.USER_ID, userId);
            psJO.put(ParamKey.LIST_EMAIL, llEmail);
            psJO.put(ParamKey.API_NAME, API.GET_USER_PRESENTATION);

            String presenServerStr = InterCommunicator.sendRequest(psJO.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
            if(presenServerStr != null){
                psJO = (JSONObject) new JSONParser().parse(presenServerStr);
                JSONArray psArr = (JSONArray) ((JSONObject) psJO.get("usersPresentation")).get(ParamKey.LIST_USER);
                return psArr;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return null;
    }
    
    public static void resetOrDeletPresentation(List<String> list) {
        try {
            JSONObject requestPre = new JSONObject();
            requestPre.put(ParamKey.API_NAME, API.DELETE_PRESENTATION);
            requestPre.put(ParamKey.LIST_USER, list);
            InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
    }
    public static void updatePresentation(List<String> list) {
        try {
            JSONObject requestPre = new JSONObject();
            requestPre.put(ParamKey.API_NAME, API.UPDATE_PRESENTATION);
            requestPre.put(ParamKey.LIST_USER, list);
            InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
    }
    
    public static JSONArray getConnectionInfor(Request request, List<String> list) {
        try {
            request.put(ParamKey.API_NAME, API.GET_CONNECTION_INFOR);
            request.reqObj.put(ParamKey.LIST_ID, list);

            String result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject obj = (JSONObject) new JSONParser().parse(result);
            request.reqObj.remove(ParamKey.LIST_ID);
            Long code = (Long) obj.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONArray data = (JSONArray) obj.get(ParamKey.DATA);
                return data;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return null;
    }     

    private String loadImage(String imgId) {
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(Config.StaticFileServerIp);
        url.append(":");
        url.append(Config.StaticFileServerPort);
        url.append("/");
        url.append("api=");
        url.append(API.LOAD_IMAGE);
        url.append("&img_id=");
        url.append(imgId);
        url.append("&img_kind=");
        url.append(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
        String result = sendGetRequest(url.toString());
        return result;

    }

    private String sendGetRequest(String url) {
        String result;
        JSONObject jsonOject = new JSONObject();
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            jsonOject.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
            result = jsonOject.toJSONString();
        }
        return result;
    }
    
    public static HashMap<String, FileUrl> getImage(List<String> listImgId){
        HashMap<String, FileUrl> mapImg = new HashMap<>();
        JSONObject request = new JSONObject();
        JSONArray listImg = new JSONArray();
        listImg.addAll(listImgId);
        request.put("list_img_id", listImg);
        request.put(ParamKey.API_NAME, API.LOAD_LIST_IMAGE);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONArray data = (JSONArray) result.get(ParamKey.DATA);
        if (data != null){
            for (Object imgObj : data){
                FileUrl url = FileUrl.fromJSONOject((JSONObject) imgObj);
                mapImg.put(url.getImageId(), url);
            }
        }
        return mapImg;
    }
    
    public static HashMap<String, FileUrl> getVideo(List<String> listVideoId){
        HashMap<String, FileUrl> mapVideo = new HashMap<>();
        JSONObject request = new JSONObject();
        JSONArray listVideo = new JSONArray();
        listVideo.addAll(listVideoId);
        request.put("list_video_id", listVideo);
        request.put(ParamKey.API_NAME, API.LOAD_LIST_VIDEO);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONArray data = (JSONArray) result.get(ParamKey.DATA);
        if (data != null){
            for (Object imgObj : data){
                FileUrl url = FileUrl.fromJSONOject((JSONObject) imgObj);
                mapVideo.put(url.getImageId(), url);
            }
        }
        return mapVideo;
    }
    
    public static HashMap<String, FileUrl> getAudio(List<String> listAudioId,List<String> listCoverId){
        HashMap<String, FileUrl> mapAudio = new HashMap<>();
        JSONObject request = new JSONObject();
        JSONArray listAudio = new JSONArray();
        JSONArray listCover = new JSONArray();
        listAudio.addAll(listAudioId);
        listCover.addAll(listCoverId);
        request.put("list_audio_id", listAudio);
        request.put("list_cover_id", listCover);
        request.put(ParamKey.API_NAME, API.LOAD_LIST_AUDIO);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        Util.addDebugLog("audio-----------------------------------"+resultStf);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONArray data = (JSONArray) result.get(ParamKey.DATA);
        if (data != null){
            for (Object imgObj : data){
                FileUrl url = FileUrl.fromJSONOject((JSONObject) imgObj);
                mapAudio.put(url.getImageId(), url);
            }
        }
        return mapAudio;
    }
    
    public static ListFileUrl getFile(List<String> listImgId, List<String> listGiftId, List<String> listVideoId, List<String> listCoverId, List<String> listAudioId, List<String> listStreamId) {
        ListFileUrl map = new ListFileUrl();
        
        JSONObject request = new JSONObject();
        JSONArray listImg = new JSONArray();
        listImg.addAll(listImgId);
        request.put("list_img_id", listImg);
        JSONArray listGift = new JSONArray();
        listGift.addAll(listGiftId);
        request.put("list_gift_id", listGift);
        JSONArray listVideo = new JSONArray();
        listVideo.addAll(listVideoId);
        request.put("list_video_id", listVideo);
        JSONArray listCover = new JSONArray();
        listCover.addAll(listCoverId);
        request.put("list_cover_id", listCover);
        JSONArray listAudio = new JSONArray();
        listAudio.addAll(listAudioId);
        request.put("list_audio_id", listAudio);
        JSONArray listStream = new JSONArray();
        listStream.addAll(listStreamId);
        request.put("list_stream_id", listStream);
        
        request.put(ParamKey.API_NAME, API.LOAD_LIST_FILE);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONObject data = (JSONObject) result.get(ParamKey.DATA);
        map = ListFileUrl.fromJSONOject(data);
        Util.addDebugLog("resultStf=================="+resultStf);
        return map;
    }
    
    public static ListFileData getFileData(List<String> listImgId){
        ListFileData map = new ListFileData();
        JSONObject request = new JSONObject();
        JSONArray listImg = new JSONArray();
        listImg.addAll(listImgId);
        request.put("list_img_id", listImg);
        request.put(ParamKey.API_NAME, API.LOAD_FILE_DATA);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        Util.addDebugLog("resultStf=================="+resultStf);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONObject data = (JSONObject) result.get(ParamKey.DATA);
        map = ListFileData.fromJSONOject(data);
        return map;
    }
    
    public static ListFileData getFileData(List<String> listImgId, List<String> listVideoId, List<String> listCoverId, List<String> listAudioId){
        ListFileData map = new ListFileData();
        JSONObject request = new JSONObject();
        JSONArray listImg = new JSONArray();
        listImg.addAll(listImgId);
        request.put("list_img_id", listImg);
        JSONArray listVideo = new JSONArray();
        listVideo.addAll(listVideoId);
        request.put("list_video_id", listVideo);
        JSONArray listCover = new JSONArray();
        listCover.addAll(listCoverId);
        request.put("list_cover_id", listCover);
        JSONArray listAudio = new JSONArray();
        listAudio.addAll(listAudioId);
        request.put("list_audio_id", listAudio);
        request.put(ParamKey.API_NAME, API.LOAD_FILE_DATA);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        Util.addDebugLog("resultStf=================="+resultStf);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONObject data = (JSONObject) result.get(ParamKey.DATA);
        map = ListFileData.fromJSONOject(data);
        return map;
    }
    
    public static ListFileData getFileData(List<String> listImgId, List<String> listVideoId, List<String> listCoverId, List<String> listAudioId, List<String> listStreamId){
        ListFileData map = new ListFileData();
        JSONObject request = new JSONObject();
        JSONArray listImg = new JSONArray();
        listImg.addAll(listImgId);
        request.put("list_img_id", listImg);
        JSONArray listVideo = new JSONArray();
        listVideo.addAll(listVideoId);
        request.put("list_video_id", listVideo);
        JSONArray listCover = new JSONArray();
        listCover.addAll(listCoverId);
        request.put("list_cover_id", listCover);
        JSONArray listAudio = new JSONArray();
        listAudio.addAll(listAudioId);
        request.put("list_audio_id", listAudio);
        JSONArray listStream = new JSONArray();
        listStream.addAll(listStreamId);
        request.put("list_stream_id", listStream);
        request.put(ParamKey.API_NAME, API.LOAD_FILE_DATA);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        Util.addDebugLog("resultStf=================="+resultStf);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONObject data = (JSONObject) result.get(ParamKey.DATA);
        map = ListFileData.fromJSONOject(data);
        return map;
    }
    
    public static ListFileData getGiftData(List<String> listGiftId){
        ListFileData map = new ListFileData();
        JSONObject request = new JSONObject();
        JSONArray listGift = new JSONArray();
        listGift.addAll(listGiftId);
        request.put("list_gift_id", listGift);
        request.put(ParamKey.API_NAME, API.LOAD_FILE_DATA);
        String resultStf = InterCommunicator.sendRequest(request.toJSONString(), Config.StaticFileServerIp, Config.StaticFileServerPort);
        Util.addDebugLog("resultStf=================="+resultStf);
        JSONObject result = Util.toJSONObject(resultStf);
        JSONObject data = (JSONObject) result.get(ParamKey.DATA);
        map = ListFileData.fromJSONOject(data);
        return map;
    }
}
