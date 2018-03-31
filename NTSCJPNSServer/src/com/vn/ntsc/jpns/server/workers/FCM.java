/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.jpns.Config;

/**
 *
 * @author RuAc0n
 */
public class FCM {

    public Long success;
    public Long canonicalIDs;
    public String registerId;

    public static FCM sendNotification(String msg, String deviceToken, String key) {
        String result = "";
        FCM fcm;
        try {

            List<String> list = new ArrayList<>();
            list.add(deviceToken);
            JSONObject sendData = new JSONObject();
            sendData.put("to", deviceToken);
            //sendData.put("registration_ids", list);
            JSONObject dataJson = (JSONObject) new JSONParser().parse(msg);
            Util.addDebugLog("sendNotificationAndroid " + msg);
            sendData.put(ParamKey.DATA, dataJson);
            sendData.put("time_to_live", Constant.FCM_TIME_TO_LIVE);
            final String jsonData = sendData.toJSONString();
            StringBuilder postData = new StringBuilder();
            postData.append(jsonData);

            URL u = new URL(Constant.FCM_URL);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            String encodedData = postData.toString();
            byte[] bytes = encodedData.getBytes();
            //post method
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            Util.addDebugLog("check FCM Android" + encodedData);
            //data to send
//            String key = "";
//            if (Config.production) {
//                key = Constant.FCM_PRODUCTION_KEY;
//            } else {
//                key = Constant.FCM_DEVELOPMENT_KEY;
//            }
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "key=" + key);
            Util.addDebugLog("check FCM key " + key);
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());

            try {
                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.flush();
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);
                return null;
            }

            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            String line;
            while ((line = buf.readLine()) != null) {
                result += line;
            }
            fcm = parse(result);
            Util.addDebugLog("FCM Android result : " + result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
        return fcm;
    }

    private static FCM parse(String respond) {
        FCM fcm = new FCM();
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(respond);
            Long canonicalNum = (Long) obj.get("canonical_ids");
            fcm.canonicalIDs = canonicalNum;
            JSONArray arr = (JSONArray) obj.get("results");
            if (arr != null && !arr.isEmpty()) {
                JSONObject result = (JSONObject) arr.get(0);
                if (result != null) {
                    String registerId = (String) result.get("registration_id");
                    fcm.registerId = registerId;
                }

            }
            Long successNum = (Long) obj.get(ParamKey.SUCCESS);
            fcm.success = successNum;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return fcm;
    }

    public boolean verifyFmcRespond() {
        boolean result = true;
        if (this.success != null && this.success == 0) {
            result = false;
        }
        return result;
    }

    public boolean isCanonicalIDs() {
        boolean result = false;
        if (this.canonicalIDs != null && this.canonicalIDs != 0 && this.registerId != null) {
            result = true;
        }
        return result;
    }

    public static FCM sendNotificationIOS(String deviceToken, String key, String msg) {
        String result = "";
        FCM fcm;
        try {

            List<String> list = new ArrayList<>();
            list.add(deviceToken);
            JSONObject sendData = new JSONObject();
            sendData.put("to", deviceToken);
            //sendData.put("registration_ids", list);
            JSONObject dataJsonOr = (JSONObject) new JSONParser().parse(msg);
            Util.addDebugLog("sendNotificationIOS msg " + msg);
            JSONObject dataJson =  (JSONObject) dataJsonOr.get("aps");
            //JSONObject dataJson = (JSONObject) new JSONParser().parse(aps);
            //Util.addDebugLog("sendNotificationIOS aps " + aps);
            JSONObject dataJsonNoti = new JSONObject();
            
            JSONObject alertJson = (JSONObject) dataJson.get("alert");
            if(alertJson == null){
                alertJson = (JSONObject) dataJson.get("alert_off");
            }
            dataJsonNoti.put("body_loc_key", alertJson.get("loc-key"));
            dataJsonNoti.put("body_loc_args", alertJson.get("loc-args"));
            dataJsonNoti.put("sound", dataJson.get("sound"));
            //dataJsonNoti.put("body", dataJson.get("body"));
            dataJsonNoti.put("body", dataJson.get("content"));
            Long badge = 0L;
            if(dataJson.get("badge") != null) {
                badge = (Long) dataJson.get("badge");
            }
            dataJsonNoti.put("badge", badge);

            JSONObject datJson = (JSONObject) dataJson.get("data");
            //Util.addDebugLog("sendNotificationIOS data " + data);
            //JSONObject datJson = (JSONObject) new JSONParser().parse(data);
            JSONObject dataJsonData = new JSONObject();
            dataJsonData.put("userid", datJson.get("userid"));
            dataJsonData.put("noti_type", datJson.get("noti_type"));
            dataJsonData.put("ownerid", datJson.get("ownerid"));
            dataJsonData.put("content", datJson.get("content"));
            dataJsonData.put("url", datJson.get("url"));
            dataJsonData.put("push_id", datJson.get("push_id"));
            dataJsonData.put("buzz_id", datJson.get("buzzid"));
            dataJsonData.put("text_buzz", datJson.get("text_buzz"));
            dataJsonData.put("ava_url", datJson.get("ava_url"));
            JSONArray arrName = ((JSONArray) alertJson.get("loc-args"));
            if (arrName != null && !arrName.isEmpty())
                dataJsonData.put("user_name", arrName.get(0));

            sendData.put(ParamKey.DATA, dataJsonData);

            sendData.put("notification", dataJsonNoti);
            sendData.put("time_to_live", Constant.FCM_TIME_TO_LIVE);
            Util.addDebugLog("sendNotificationIOS sendData " + sendData);
            final String jsonData = sendData.toJSONString();
            StringBuilder postData = new StringBuilder();
            postData.append(jsonData);

            URL u = new URL(Constant.FCM_URL);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            String encodedData = postData.toString();
            byte[] bytes = encodedData.getBytes();
            //post method
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            Util.addDebugLog("check FCM IOS" + encodedData);
            //data to send
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "key=" + key);
            Util.addDebugLog("check FCM key " + key);
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());

            try {
                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.flush();
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);
                return null;
            }

            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            String line;
            while ((line = buf.readLine()) != null) {
                result += line;
            }
            fcm = parse(result);
            Util.addDebugLog("FCM IOS result : " + result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
        return fcm;
    }
}
