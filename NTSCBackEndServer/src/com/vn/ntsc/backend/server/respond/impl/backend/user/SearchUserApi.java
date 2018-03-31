/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.ArrayList;
import java.util.List;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.BaseUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.common.Utility;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SearchUserApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Util.addDebugLog("Test obj for SearchUserApi " + obj.toJSONString());
            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            Long isPurchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
            // 2 is get all user, dont care purchase or not
            if (isPurchase != null && isPurchase == 2) {
                obj.remove(ParamKey.IS_PURCHASE);
            }
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            List<String> purchaseL = new ArrayList<>();
            boolean getUserPurchaseByOtherSystem = Utility.getPurchaseList(obj, purchaseL);
            if (getUserPurchaseByOtherSystem) {
                purchaseL = UserDAO.getPurchaseUsers();
            }     
            SizedListData data = UserDAO.searchUser(obj, isCsv, purchaseL);
            if (isCsv) {
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, new BaseUser(), null);
            }
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return respond;
    }
}
