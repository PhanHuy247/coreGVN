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
import com.vn.ntsc.otherservice.servicemanagement.respond.common.PublicKeyManager;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.IntRespond;

/**
 *
 * @author RuAc0n
 */
public class ConfirmPurchaseAndroid implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        IntRespond result = new IntRespond();
        try {
            Util.addInfoLog("Request confirm android purchase: " + request.toJson());
            String signature = Util.getStringParam(request.reqObj, "signature");
            String signedData = Util.getStringParam(request.reqObj, "pur_data");
            String packetId = Util.getStringParam(request.reqObj, ParamKey.PACKET_ID);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }

            if (signedData == null || signedData.isEmpty() || packetId == null || packetId.isEmpty() || signature == null || signature.isEmpty()) {
                return result;
            }
            JSONObject sigJson = (JSONObject) new JSONParser().parse(signedData);
            String orderId = (String) sigJson.get("orderId");
            String developerPayload = (String) sigJson.get("developerPayload");
            Util.addInfoLog("ConfirmPurchaseAndroid orderId ==========" + orderId);
            Util.addInfoLog("ConfirmPurchaseAndroid orderId ==========" + developerPayload);
            boolean isProductionTransaction = true;
            if (orderId == null) {
                //orderId = developerPayload;
                isProductionTransaction = false;
            }
            String productId = (String) sigJson.get("productId");
            if (!TransactionLogDAO.isTransactionExist(orderId, null, Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION)) {
                //HUNGDT add Multiapp #6374
                String uniqueName = ApplicationDAO.getUniqueNameById(applicationId);
                Util.addInfoLog("ConfirmPurchaseAndroid uniqueName============" + uniqueName);
                String publicKey = PublicKeyManager.getPublicKey(uniqueName);
                Util.addInfoLog("ConfirmPurchaseAndroid publicKey============" + publicKey);
                if (publicKey == null) {
                    publicKey = Config.ANDROID_PURCHASES_PUBLIC_KEY;
                }
                if (Helper.verifyGooglePurchase(signedData, signature, publicKey)) {
//                    PointPacket pp = PointPacketDAO.getPointPacket(packetId);
//                    pp = pp == null ? ActionPointPacketDAO.getPointPacket(packetId) : pp;
                    boolean isPurchase = TransactionLogDAO.isPurchaseByUserId(userId);
                    PointPacket pp = ActionPointPacketDAO.getPointPacketVer2(packetId, isPurchase);
                    if (pp == null || !pp.productId.equals(productId)) {
                        return result;
                    }
                    Double money = pp.price;
                    Long purchaseTime = (Long) sigJson.get("purchaseTime");
                    double totalPrice = TransactionLogDAO.getTotalPrice(userId) + money;
//                    int point = PointPacketDAO.getPoint(packetId);
                    int point = pp.point;

                    String dateTime = DateFormat.format(Util.getGMTTime());
//                    TransactionLogDAO.addTransaction(userId, dateTime, point, money, totalPrice, orderId, null, Constant.GOOLE_PRODUCTION, purchaseTime, ip);
                    String internalTransactionId = Util.getStringParam(request.reqObj, "transaction_id");
                    if (internalTransactionId == null) { // old version
                        TransactionLogDAO.addTransaction(userId, dateTime, point, money, totalPrice, orderId, null, Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION, purchaseTime, ip, isProductionTransaction, applicationId);
                    } else {
                        TransactionLogDAO.updateTransaction(userId, internalTransactionId, dateTime, point, money, totalPrice, orderId, null, Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION, purchaseTime, ip, isProductionTransaction, applicationId);
                    }
                    // add statistic record
                    if (isProductionTransaction) {
                        Helper.addTransactionStatistic(uniqueName, dateTime, request, Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION, packetId, money);
                    }
                    result = new IntRespond(ErrorCode.SUCCESS, point);
                } else {
                    Util.addInfoLog("Confirm Android purchase: fail with public key" + request.toJson());
                }
            } else {
                result.code = ErrorCode.TRANSACTION_EXIST;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        System.out.println(result.toJsonObject());
        return result;
    }

}
