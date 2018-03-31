/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.user.Point;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.server.common.Utility;
import com.vn.ntsc.backend.server.respond.result.ListStringRespond;

/**
 *
 * @author RuAc0n
 */
public class AddPointByListApi implements IApiAdapter {

    private static final int point_error = 4;

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            Long isPurchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
            // 2 is get all user, dont care purchase or not
            if(isPurchase != null && isPurchase == 2)
                obj.remove(ParamKey.IS_PURCHASE);
            Long point = Util.getLongParam(obj, "added_point");
            if (point == null) {
                return new Respond(point_error);
            }
            
            String adminName = Util.getStringParam(obj, ParamKey.ADMIN_NAME);// LongLT 19Sep2016 /////////////////////////// START #4295
            
            List<String> listFriend = new ArrayList<>();
            if(point != 0 ){ 
                List<String> purchaseL = new ArrayList<>();
                boolean getUserPurchaseByOtherSystem = Utility.getPurchaseList(obj, purchaseL);
                if(getUserPurchaseByOtherSystem){
                    purchaseL = UserDAO.getPurchaseUsers();
                }
                listFriend = UserDAO.getListAutoMessage(obj, purchaseL);
                for(String id : listFriend){
                    Point pnt = UserDAO.addPoint(id, point.intValue());
//                    LogPointDAO.addLog(id, 49, null, Util.getGMTTime(), null, pnt);
                    LogPointDAO.addLog(id, 49, adminName, Util.getGMTTime(), null, pnt);// LongLT 19Sep2016 /////////////////////////// START #4295
                }
            }
            respond = new ListStringRespond(ErrorCode.SUCCESS, listFriend);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
