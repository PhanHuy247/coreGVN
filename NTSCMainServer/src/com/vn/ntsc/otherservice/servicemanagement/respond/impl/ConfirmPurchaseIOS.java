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
import com.vn.ntsc.dao.impl.ActionPointPacketDAO;
import com.vn.ntsc.dao.impl.ApplicationDAO;
import com.vn.ntsc.dao.impl.PointPacketDAO;
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
public class ConfirmPurchaseIOS implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try{
            Util.addInfoLog("Request confirm I purchase: " + request.toJson());
            String receipt = Util.getStringParam(request.reqObj, "receipt");
            String packetId = Util.getStringParam(request.reqObj, ParamKey.PACKET_ID);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
            if(receipt == null || receipt.isEmpty()){
                return result;
            }
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }

            String uniqueName = ApplicationDAO.getUniqueNameById(applicationId);
            String url = Constant.IOS_PURCHASE_PRODUCTION_URL;
            String respondReceipt = Helper.getReceipt(receipt, url);
            JSONObject receiptJson = (JSONObject) new JSONParser().parse(respondReceipt);
            Long status = (Long) receiptJson.get("status");
            boolean isProductionTransaction = status == ErrorCode.SUCCESS;
            if(!isProductionTransaction){
                respondReceipt = Helper.getReceipt(receipt, Constant.IOS_PURCHASE_SANDBOX_URL);
                receiptJson = (JSONObject) new JSONParser().parse(respondReceipt);
                status = (Long) receiptJson.get("status");
            }
            Util.addInfoLog("Apple store confirm: " + respondReceipt);
            // status = 21007 is sandbox code
            if(status == ErrorCode.SUCCESS || status == 21007){
//                if(status != ErrorCode.SUCCESS){
//                   respondReceipt = Helper.getReceipt(receipt, Constant.SANDBOX_URL_IOS);
//                   receiptJson = (JSONObject) new JSONParser().parse(respondReceipt);
//                }
                JSONObject receiptObj = (JSONObject) receiptJson.get("receipt");
                String identifier = (String) receiptObj.get("unique_identifier");
                String tranId = (String) receiptObj.get("transaction_id");
                String purchaseDate = (String)receiptObj.get("purchase_date");
                String productId = (String)receiptObj.get("product_id");
//                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_PURCHASE_FORMAT);
                long purchaseTime = DateFormat.parse_yyyy_MM_dd_HH_mm_ss(purchaseDate).getTime();
                if(identifier == null)
                    return result;
//                PointPacket pp = PointPacketDAO.getPointPacket(packetId);
//                pp = pp == null ? ActionPointPacketDAO.getPointPacket(packetId) : pp;
                boolean isPurchase = TransactionLogDAO.isPurchaseByUserId(userId);
                PointPacket pp = ActionPointPacketDAO.getPointPacketVer2(packetId, isPurchase);
                if(pp == null || !pp.productId.equals(productId)){
                    return result;
                }
                if(!TransactionLogDAO.isTransactionExist(identifier, tranId, Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION)){
                    Double money = Util.getDoubleParam(request.reqObj, ParamKey.PRICE);
                    if (money == null) {
                        money = pp.price;
                    }
                    double totalPrice = TransactionLogDAO.getTotalPrice(userId);
                    if(isProductionTransaction)
                        totalPrice += money;
                    int point = pp.point;
                    String dateTime = DateFormat.format(Util.getGMTTime());
//                    boolean isRealTransaction = status == ErrorCode.SUCCESS;
                    String internalTransactionId = Util.getStringParam(request.reqObj, "transaction_id");
                    if(internalTransactionId == null){ // old version
                        TransactionLogDAO.addTransaction(userId, dateTime, point, money, totalPrice, identifier, tranId, Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION, purchaseTime, ip, isProductionTransaction,applicationId);
                    }else{
                        TransactionLogDAO.updateTransaction(userId, internalTransactionId, dateTime, point, money, totalPrice, identifier, tranId, Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION, purchaseTime, ip, isProductionTransaction,applicationId);
                    }
                    // add statistic record for real request
                    // HUNGDT add Multiapp #6374
                    if(isProductionTransaction)
                        Helper.addTransactionStatistic(uniqueName, dateTime, request, Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION, productId, money);
                    
                    result = new IntRespond(ErrorCode.SUCCESS, point);
                }else{
                    result.code = ErrorCode.TRANSACTION_EXIST;
                    return result;
                }
            }else{
                Util.addInfoLog("Confirm IOS purchase: fail with apple store" + request.toJson());
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            
        }
        return result;
    }
    
}
