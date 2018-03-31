/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.InstallationStatisticDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class InstallApplicationApi implements IApiAdapter{
    
    private static final long DUPLICATE_TIME = 60L *1000;

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            Long type = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
            String uniqueNumber = (String) request.getParamValue(ParamKey.UNIQUE_NUMBER);

            if(type != Constant.DEVICE_TYPE.IOS && type != Constant.DEVICE_TYPE.ANDROID){
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
//            InstallationLogDAO.insert(DateFormat.format_yyyyMMddHHmmssSSS(Util.currentTime()), type.intValue(), uniqueNumber);
            int uniqueNumberType; 
            if(uniqueNumber != null && !uniqueNumber.isEmpty()){
                uniqueNumberType = Constant.UNIQUE_NUMBER_TYPE.NEW;
                if(InstallationStatisticDAO.isUniqueNumberExists(type.intValue(), uniqueNumber)){
                    uniqueNumberType = Constant.UNIQUE_NUMBER_TYPE.EXISTS;
                    long uniqueInstallTime = InstallationStatisticDAO.getTimeUniqueInstallApplication(type.intValue(), uniqueNumber);
                    if( Util.currentTime() - uniqueInstallTime <= DUPLICATE_TIME){
                        uniqueNumberType = Constant.UNIQUE_NUMBER_TYPE.DUPLICATE;
                    }
                }
            }else {
                uniqueNumberType = Constant.UNIQUE_NUMBER_TYPE.EMPTY;
            }
            InstallationStatisticDAO.insert(Util.currentTime(), type.intValue(), uniqueNumber, uniqueNumberType);
            result = new Respond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }

}
