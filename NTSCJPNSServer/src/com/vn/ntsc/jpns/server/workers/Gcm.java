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

/**
 *
 * @author RuAc0n
 */
public class Gcm {

    public Long success;
    public Long canonicalIDs;
    public String registerId;

    public static Gcm sendNotification(String msg, String deviceToken, String key) {
        String result = "";
        Gcm gcm;
        try {

            List<String> list = new ArrayList<>();
            list.add(deviceToken);
            JSONObject sendData = new JSONObject();
            sendData.put("registration_ids", list);
            JSONObject dataJson = (JSONObject) new JSONParser().parse(msg);
            sendData.put(ParamKey.DATA, dataJson);
            sendData.put("time_to_live", Constant.GCM_TIME_TO_LIVE);
            final String jsonData = sendData.toJSONString();
            StringBuilder postData = new StringBuilder();
            postData.append(jsonData);

            URL u = new URL(Constant.GCM_URL);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            String encodedData = postData.toString();
            byte[] bytes = encodedData.getBytes();
            //post method
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            //data to send
//            String key = "";
//            if(Config.production)
//                key = Constant.GCM_PRODUCTION_KEY;
//            else
//                key = Constant.GCM_DEVELOPMENT_KEY;
            Util.addDebugLog("GCM_PRODUCTION_KEYNEW " + key);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "key=" + key);
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
            gcm = parse(result);
            Util.addDebugLog("GMC result : " + result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
        return gcm;
    }

    private static Gcm parse(String respond) {
        Gcm gcm = new Gcm();
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(respond);
            Long canonicalNum = (Long) obj.get("canonical_ids");
            gcm.canonicalIDs = canonicalNum;
            JSONArray arr = (JSONArray) obj.get("results");
            if (arr != null && !arr.isEmpty()) {
                JSONObject result = (JSONObject) arr.get(0);
                if (result != null) {
                    String registerId = (String) result.get("registration_id");
                    gcm.registerId = registerId;
                }

            }
            Long successNum = (Long) obj.get(ParamKey.SUCCESS);
            gcm.success = successNum;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return gcm;
    }

    public boolean verifyGmcRespond() {
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
}
