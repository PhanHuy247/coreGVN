/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.common;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.log.TransactionLogDAO;

/**
 *
 * @author Rua
 */
public class Utility {

    public static boolean getPurchaseList(JSONObject obj, List<String> purchaseL) {
        boolean result = false;
        List<String> purchase = new ArrayList<>();
        try {
            Long isPurchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
            if (isPurchase != null) {
                if (isPurchase == Constant.FLAG.ON) {
                    Long toTime = null;
                    Long fromTime = null;
                    String to_pur_day = Util.getStringParam(obj, "to_pur_day");
                    if (to_pur_day != null) {
                        toTime = DateFormat.parse(to_pur_day).getTime();
                    }
                    String from_pur_day = Util.getStringParam(obj, "from_pur_day");
                    if (from_pur_day != null) {
                        fromTime = DateFormat.parse(from_pur_day).getTime();
                    }
                    Object to_money = obj.get("to_money");
                    Double toMoney = null;
                    if(to_money != null){
                        toMoney = Double.parseDouble(to_money.toString());
                    }
                    Object from_money = obj.get("from_money");
                    Double fromMoney = null;
                    if(from_money != null){
                        fromMoney = Double.parseDouble(from_money.toString());
                    }
                    result = TransactionLogDAO.getListTransaction(fromTime, toTime, toMoney, fromMoney, purchase);
                } else {
                    purchase = TransactionLogDAO.getAllTrader();
                    result = true;
                }
            } else {
                purchase = new ArrayList<>();
            }
            purchaseL.addAll(purchase);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
}
