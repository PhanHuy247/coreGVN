/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.ActionPointPacketDAO;
import com.vn.ntsc.dao.impl.PurchaseLogDAO;
import com.vn.ntsc.dao.impl.TransactionLogDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.LogPurchase;
import com.vn.ntsc.otherservice.entity.impl.PointPacket;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class LogBeforePurchaseVersion2Api implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            Util.addInfoLog("Request log before purchase version 2: " + request.toJson());
            String packetId = Util.getStringParam(request.reqObj, ParamKey.PACKET_ID);
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            Long productionType = Util.getLongParam(request.reqObj, "production_type");
            String ip = Util.getStringParam(request.reqObj, ParamKey.IP);
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }
            
            if (packetId == null || productionType == null) {
                return null;
            }
//            PointPacket pointPacket = PointPacketDAO.getPointPacket(packetId);
//            pointPacket = pointPacket == null ? ActionPointPacketDAO.getPointPacket(packetId) : pointPacket;
            boolean isPurchase = TransactionLogDAO.isPurchaseByUserId(userId);
            PointPacket pointPacket = ActionPointPacketDAO.getPointPacketVer2(packetId, isPurchase);
//            int point = PointPacketDAO.getPoint(packetId);
            int point = pointPacket.point;
            Double money = Util.getDoubleParam(request.reqObj, ParamKey.PRICE);
            if (money == null) {
//                money = PointPacketDAO.getPrice(packetId);
                money = pointPacket.price;
            }
            long timePurchase = Util.currentTime();
            String time = DateFormat.format(new Date(timePurchase));
            String internalTransactionId = PurchaseLogDAO.add(userId, Util.currentTime(), packetId, point, applicationId);
            TransactionLogDAO.addTransaction(userId, time, point, money, productionType.intValue(), timePurchase, internalTransactionId, ip, applicationId);
            result = new EntityRespond(ErrorCode.SUCCESS, new LogPurchase(internalTransactionId));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
