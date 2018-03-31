/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.inspection.version.InspectionVersionDAO;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.GiftDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileData;
import com.vn.ntsc.otherservice.entity.impl.Gift;
import com.vn.ntsc.otherservice.entity.impl.ListFileData;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;
import java.util.ArrayList;

/**
 *
 * @author RuAc0n
 */
public class GetAllGiftApi implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String language = Util.getStringParam(request.reqObj, ParamKey.LANGUAGE);
            List<Gift> giftList = GiftDAO.getAllGift(language);
            List<String> listGiftId = new ArrayList<>();
            for(Gift item: giftList){
                listGiftId.add(item.giftId);
            }
            ListFileData map = InterCommunicator.getGiftData(listGiftId);
            
            for(Gift item: giftList){
                FileData gift = map.mapGift.get(item.giftId);
                item.giftUrl = gift.fileUrl;
            }
            
            String uId = (String) request.getParamValue(ParamKey.USER_ID);
            if(uId != null){
                User u = UserDAO.getUserInfor(uId);
                if (u.deviceType == 0) {
                    String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();

                    if (u.appVersion.equals(safaryVersion)) {
                        for(int i = 0; i < giftList.size(); ++i){
                            Gift g = giftList.get(i);
                            g.giftPrice = 0.0;
                        }
                    }
                }
            }
            

            result = new ListEntityRespond(ErrorCode.SUCCESS, giftList);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
