/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Collections;
import java.util.List;
import com.vn.ntsc.dao.impl.ActionPointPacketDAO;
import com.vn.ntsc.dao.impl.TransactionLogDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.ActionPointPacket;
import com.vn.ntsc.otherservice.entity.impl.ListActionPointPackageVersion2Data;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListActionPointPacketVersion2Api implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = (String) request.getParamValue(ParamKey.USER_ID);
            Long type = Util.getLongParam(request.reqObj, "pro_type");
            if (type == null) {
                return result;
            }
            Long actionType = Util.getLongParam(request.reqObj, "action_type");
            if (actionType == null) {
                return result;
            }
            boolean isPurchase = TransactionLogDAO.isPurchaseByUserId(userId);
            String applicationId = "1";
            if (request.reqObj.get("application") != null && Util.getLongParam(request.reqObj, ParamKey.APPLICATION) != null) {
                applicationId = Util.getLongParam(request.reqObj, ParamKey.APPLICATION).toString();
            }
            List<ActionPointPacket> listRespond;
            listRespond = ActionPointPacketDAO.getPoinPacketByType(applicationId, actionType.intValue(), type.intValue(), isPurchase);
//            if(actionType == 1){
//                listRespond = ActionPointPacketDAO.getChatPointPacket(type.intValue());
//            }else if(actionType == 2){
//                listRespond = ActionPointPacketDAO.getVoiceCallPointPacket(type.intValue());
//            }else if(actionType == 3){
//                listRespond = ActionPointPacketDAO.getVideoCallPointPacket(type.intValue());
//            }else if(actionType == 4){
//                listRespond = ActionPointPacketDAO.getGiftPointPacket(type.intValue());
//            }else if(actionType == 5){
//                listRespond = ActionPointPacketDAO.getCommentPointPacket(type.intValue());
//            }else if(actionType == 6){
//                listRespond = ActionPointPacketDAO.getSubCommentPointPacket(type.intValue());
//            }else if(actionType==7){
//                listRespond = ActionPointPacketDAO.getUnlockBackstagePacket(type.intValue());
//            }else if(actionType == 8) {
//                listRespond = ActionPointPacketDAO.getSaveImagePacket(type.intValue());
//            }else{
//                listRespond = ActionPointPacketDAO.getOtherPacket(type.intValue());
//            }
            Collections.sort(listRespond);
            //fix
//            listRespond.clear();
//            listRespond.add(new ActionPointPacket(type.intValue()));
            result = new EntityRespond(ErrorCode.SUCCESS, new ListActionPointPackageVersion2Data(isPurchase, listRespond));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
