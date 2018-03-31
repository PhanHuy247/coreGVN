/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.user;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.log.TransactionLogDAO;
import com.vn.ntsc.backend.dao.statistic.TransactionStatisticDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.user.Point;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class AddPurchaseByAdmin implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Util.addInfoLog("execute AddPurchaseByAdmin API");
        Util.addInfoLog(obj.toJSONString());
        Respond result = new Respond();
        try{
            Util.addInfoLog("Get Param value");
            String userId = Util.getStringParam(obj, ParamKey.ID);

            String ip = Util.getStringParam(obj, ParamKey.IP);
            Double money = Util.getDoubleParam(obj, ParamKey.PRICE);
            Long point = Util.getLongParam(obj, "point");
            //Long productionType = Util.getLongParam(obj, "production_type");
            Long productionType = Util.getLongParam(obj, ParamKey.PRODUCTION_TYPE);
            Util.addInfoLog("Validate input");
            Util.addInfoLog("Input Value :" + userId + " " + money + " " + point + " " + productionType);
            
//            if(!Util.validateString(userId) || !Util.validate(money, point, productionType)){
//                result.code = ErrorCode.WRONG_DATA_FORMAT;
//                return result;
//            }
            if((userId ==null) || (money == null )
                    || (point == null) || (productionType == null)){
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            
            Util.addInfoLog("Get Total Price");
            double totalPrice = TransactionLogDAO.getTotalPrice(userId) + money;
            long time = Util.currentTime();
            String dateTime = DateFormat.format(Util.getGMTTime());
            
            Point pnt = UserDAO.addPoint(userId, point.intValue());
            Util.addInfoLog("Update Purchase time");
            UserDAO.updatePurchaseTime(userId, Util.getGMTTime());
            Util.addInfoLog("Add Point Log");
            
            // LongLT 19Sep2016 /////////////////////////// START #4295
            String adminName = Util.getStringParam(obj, ParamKey.ADMIN_NAME);// LongLT 19Sep2016 /////////////////////////// START #4295
//            LogPointDAO.addLog(userId, 14, null, Util.getGMTTime(), null, pnt);
            LogPointDAO.addLog(userId, 14, adminName, Util.getGMTTime(), null, pnt);
            // LongLT 19Sep2016 /////////////////////////// END #4295
            
            Util.addInfoLog("Add Transaction");
            TransactionLogDAO.addTransaction(userId, dateTime, point.intValue(), money, totalPrice, productionType.intValue(), time, ip);
            
            String day = dateTime.substring(0, 8);
            String hour = dateTime.substring(0, 10);
            Util.addInfoLog("UPdate trans time");
            TransactionStatisticDAO.updateNew(day, hour, money, productionType.intValue());
            
            result.code = ErrorCode.SUCCESS;
            Util.addInfoLog("Execute successfully");
 
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
