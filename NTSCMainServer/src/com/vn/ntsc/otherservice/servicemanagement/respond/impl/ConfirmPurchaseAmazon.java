/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.ActionPointPacketDAO;
import com.vn.ntsc.dao.impl.ApplicationDAO;
import com.vn.ntsc.dao.impl.TransactionLogDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.PointPacket;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.common.Helper;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.IntRespond;

/**
 *
 * @author RuAc0n
 */
public class ConfirmPurchaseAmazon implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        IntRespond result = new IntRespond();
        try {
            Util.addInfoLog("Request confirm amazone purchase: " + request.toJson());
            String receipt = Util.getStringParam(request.reqObj, "receipt");
            String packetId = Util.getStringParam(request.reqObj, ParamKey.PACKET_ID);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String buyerId = Util.getStringParam(request.reqObj, "buyer_id");
            String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }
            if (packetId == null || packetId.isEmpty() || receipt == null || receipt.isEmpty() || buyerId == null || buyerId.isEmpty()) {
                return result;
            }
            String uniqueName = ApplicationDAO.getUniqueNameById(applicationId);
            JSONObject amazonResponseJson = null;
            String url = String.format(Constant.AMAZON_PURCHASE_PRODUCTION_URL, Config.AMAZONE_PURCHASES_KEY, buyerId, receipt);
            Util.addInfoLog("Request confirm amazone purchase URL " + url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
 
            switch (responseCode){
                case 400:
                    Util.addInfoLog("Request confirm amazone purchase: Invalid ReceiptId " + receipt);
                    return result;
                case 496:
                    Util.addInfoLog("Request confirm amazone purchase: Invalid Secret Key " + request.toJson());
                    return result;
 
                case 497:
                    Util.addInfoLog("Request confirm amazone purchase: Invalid BuyerId " + buyerId);
                    return result;
 
                case 500:
                    Util.addInfoLog("Request confirm amazone purchase: Internal Server Error");
                    return result;
 
                case 200:{
                    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
 
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Util.addInfoLog("Request confirm amazone purchase  respnose String : " + response.toString());
                    amazonResponseJson = (JSONObject) new JSONParser().parse(response.toString());
                    break;
                }
                default:{
                    Util.addInfoLog("Request confirm amazone purchase  code : " + responseCode);
                    return result;
                }
            }
            if(amazonResponseJson != null){
                String receiptId = (String) amazonResponseJson.get("receiptId");
                String productId = (String) amazonResponseJson.get("productId");
                Boolean testTransaction = (Boolean) amazonResponseJson.get("testTransaction");
                if (!TransactionLogDAO.isTransactionExist(receiptId, null, Constant.PURCHASE_PRODUCTION_TYPE.AMAZON_PRODUCTION)) {
                    boolean isPurchase = TransactionLogDAO.isPurchaseByUserId(userId);
                    PointPacket pp = ActionPointPacketDAO.getPointPacketVer2(packetId, isPurchase);
                    if(pp == null || !pp.productId.equals(productId) ){
                        return result;
                    }
                    Double money = pp.price;
                    double totalPrice = TransactionLogDAO.getTotalPrice(userId) + money;
                    int point = pp.point;

                    String dateTime = DateFormat.format(Util.getGMTTime());
                    String internalTransactionId = Util.getStringParam(request.reqObj, "transaction_id");
                    if(internalTransactionId == null){ // old version
                        TransactionLogDAO.addTransaction(userId, dateTime, point, money, totalPrice, receiptId, null, Constant.PURCHASE_PRODUCTION_TYPE.AMAZON_PRODUCTION, Util.currentTime(), ip, !testTransaction,applicationId);
                    }else{
                        TransactionLogDAO.updateTransaction(userId, internalTransactionId, dateTime, point, money, totalPrice, receiptId, null, Constant.PURCHASE_PRODUCTION_TYPE.AMAZON_PRODUCTION, Util.currentTime(), ip, !testTransaction,applicationId);
                    }
                    // add statistic record
                    if(!testTransaction)
                        Helper.addTransactionStatistic(uniqueName, dateTime, request, Constant.PURCHASE_PRODUCTION_TYPE.AMAZON_PRODUCTION, packetId, money);
                    result = new IntRespond(ErrorCode.SUCCESS, point);
                } else {
                    result.code = ErrorCode.TRANSACTION_EXIST;
                }
            }else{
                Util.addInfoLog("Confirm Amazone purchase Fail" + request.toJson());
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        System.out.println(result.toJsonObject());
        return result;
    }

}
