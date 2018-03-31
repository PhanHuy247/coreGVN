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
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.Gift;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetListGiftApi implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            List<String> list = Util.getListString(request.reqObj, "lst_gift");
            if (list == null || list.isEmpty()) {
                return result;
            }
            List<Gift> giftList = GiftDAO.getListGift(list);
            
            String uId = (String) request.getParamValue(ParamKey.USER_ID);
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

            result = new ListEntityRespond(ErrorCode.SUCCESS, giftList);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
