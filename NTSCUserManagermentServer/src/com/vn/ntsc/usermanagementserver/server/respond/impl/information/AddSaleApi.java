/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static eazycommon.constant.ParamKey.USER_AGENT;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.TransactionStatisticDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogSalePointDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogTransactionDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.InterCommunicator;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
//import vn.com.ntqsolution.usermanagementserver.server.InterCommunicator;

/**
 *
 * @author RuAc0n
 */
public class AddSaleApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Util.addDebugLog("===========AddSaleApi======");
        Respond result = new Respond();
        StringBuffer androidString = new StringBuffer();
        StringBuffer iosString = new StringBuffer();
        Long device_Type = null;
        String sendAdjust = "";
        String currency = "JPY";
        String app_id = "1";
        String app_token = "vp22m9jgqg3k";
        String event_tokenAdr = "ftu3ot";
        String event_tokenIos = "73aw4v";

        try {
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            Util.addDebugLog("===========AddSaleApi userId======" + userId);
            //String userId = Util.getStringParam(obj, "user_id");
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            Double money = Util.getDoubleParam(obj, "money");
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long addbyadmin = Util.getLongParam(obj, ParamKey.ADD_BY_ADMIN);//thanhdd add
            if (type == null || point == null || money == null) {
                return result;
            }

            long productionType = 0L;
            if (type == 5) {
                productionType = 2;
            } else if (type == 3) {
                productionType = 1;
            } else if (type == 2) {
                productionType = 0;
            } else if (type == 4) {
                productionType = 4;
            } else if (type == 6) {
                productionType = 3;
            }
            // thanhdd add: send adjust
            User user = UserDAO.getUserInfor(userId);
            device_Type = user.deviceType;
            Util.addDebugLog("===========AddSaleApi device_Type======" + device_Type);
            String device_id = user.device_id;
            String adid = user.ad_id;
            Util.addDebugLog("===========AddSaleApi adid before======" + adid);
//            if (user.adid == null){
//                adid = user.ad_id;
//                Util.addDebugLog("===========AddSaleApi adid null======" + adid);
//            }
            Util.addDebugLog("===========AddSaleApi adid======" + adid);

            if (user.applicationId != null) {
                app_id = user.applicationId;
            }
            if (app_id.equals("2")) {
                app_token = "5shkkb3crchs";
                event_tokenAdr = "3n0g56";
                event_tokenIos = "532b2a";
            }
            Util.addDebugLog("===========AddSaleApi app_id======" + app_id);
            Util.addDebugLog("===========AddSaleApi app_token======" + app_token);
            JSONObject androidRq = new JSONObject();
            androidRq.put("s2s", "1");
            androidRq.put("event_token", event_tokenAdr);
            androidRq.put("app_token", app_token);
            androidRq.put("device_id", device_id);
            androidRq.put("adid", adid);
            androidRq.put("revenue", money);
            androidRq.put("currency", currency);
            androidRq.put("environment", "production");

            // sandbox
            // production
            androidString.append("s2s=1");
            androidString.append("&event_token=" + event_tokenAdr);
            androidString.append("&app_token=" + app_token);
            androidString.append("&device_id=" + device_id);
            androidString.append("&adid=" + adid);
            androidString.append("&revenue=" + money);
            androidString.append("&currency=" + currency);
            androidString.append("&environment=production");

            JSONObject iosRq = new JSONObject();
            iosRq.put("s2s", "1");
            iosRq.put("event_token", event_tokenIos);
            iosRq.put("app_token", app_token);
            iosRq.put("device_id", device_id);
            iosRq.put("adid", adid);
            iosRq.put("revenue", money);
            iosRq.put("currency", currency);
            iosRq.put("environment", "production");

            iosString.append("s2s=1&");
            iosString.append("event_token=" + event_tokenIos);
            iosString.append("&app_token=" + app_token);
            iosString.append("&device_id=" + device_id);
            iosString.append("&adid=" + adid);
            iosString.append("&revenue=" + money);
            iosString.append("&currency=" + currency);
            iosString.append("&environment=production");

            //String androidRq ="s2s=1&event_token=ftu3ot&app_token=vp22m9jgqg3k&adid="+adid+"&revenue="+money+"&currency="+currency+"";
            //String iosRq ="s2s=1&event_token=73aw4v&app_token=vp22m9jgqg3k&adid="+adid+"&revenue="+money+"&currency="+currency+"";
//            if (device_Type == 0) {
//                sendAdjust = sendPost(iosString.toString(), "https://s2s.adjust.com/event");
//            } else if (device_Type == 1) {
//                sendAdjust = sendPost(androidString.toString(), "https://s2s.adjust.com/event");
//            }
            //Util.addDebugLog("AddSaleApi sendAdjust response" + sendAdjust);
            //end
            ActionManager.doAddPointFromFreePage(userId, point.intValue(), type, null, ip, time);
            String dateTime = DateFormat.format(Util.getGMTTime());
            String day = dateTime.substring(0, 8);
            String hour = dateTime.substring(0, 10);
            TransactionStatisticDAO.update(day, hour, money, (int) productionType);
            Util.addInfoLog("=========Add Transaction==========");
            double totalPrice = LogTransactionDAO.getTotalPrice(userId) + money;
            long dateInLong = time.getTime();

            Util.addInfoLog("=========productionType==========" + productionType);
            LogTransactionDAO.addTransaction(userId, dateTime, point.intValue(), money, totalPrice, type.intValue(), dateInLong, ip);
            LogSalePointDAO.addLog(userId, point.intValue(), money, type.intValue(), time);

            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        try {
            if (device_Type == 1) {
                sendAdjust = sendPost(androidString.toString(), "https://s2s.adjust.com/event");
            } else if (device_Type == 0) {
                sendAdjust = sendPost(iosString.toString(), "https://s2s.adjust.com/event");
            }
            Util.addDebugLog("AddSaleApi sendAdjust response" + sendAdjust);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result.code = ErrorCode.SUCCESS;
        }

        return result;
    }

    private String sendPost(String requestParam, String url) throws Exception {

        //String urlReq = url;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = requestParam;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Util.addDebugLog("\nSending 'POST' request to URL : " + url);
        Util.addDebugLog("Post parameters : " + urlParameters);
        Util.addDebugLog("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        Util.addDebugLog(response.toString());
        return response.toString();
    }

}
