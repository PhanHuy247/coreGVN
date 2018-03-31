/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.AutoNewsNotifyDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.AutoNewsNotify;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author hungdt
 */
public class ListAutoNewsNotifyApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        long take  = Util.getLongParam(request.reqObj, "take");
        Respond response = new Respond();
        Util.addInfoLog(" hungdt check : " + request.toString());
        Util.addInfoLog("hungcheck1" + take);
        try {
            Util.addInfoLog("hungcheck");
            List<AutoNewsNotify> newsList = AutoNewsNotifyDAO.getAll();
            response = new ListEntityRespond(ErrorCode.SUCCESS, newsList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            Util.addInfoLog("hungcheck0");
            response = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        
        return response;
    }
    
}
