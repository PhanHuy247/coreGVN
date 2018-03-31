/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.util;

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
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.dao.pojos.Device;
import com.vn.ntsc.jpns.server.Request;
import com.vn.ntsc.jpns.server.workers.MsgContainer;
import com.vn.ntsc.jpns.server.workers.ReadyPackage;

/**
 *
 * @author tuannxv00804
 */
public class InterCommunicator {

    public static void send(JSONObject msg, String toUserid) {
        LinkedList<Device> ll = Core.dao.getDevices(toUserid);
        for (int i = 0; i < ll.size(); i++) {
            Device d = ll.get(i);
            ReadyPackage p = new ReadyPackage(d, msg);
            MsgContainer.add(p);
        }
    }
    
    public static void sendByDeviceId(JSONObject msg, String deviceId) {
        Device d = Core.dao.getDevice(deviceId);
        ReadyPackage p = new ReadyPackage(d, msg);
        MsgContainer.add(p);
    }
    
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
}
