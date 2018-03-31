/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.cmcode;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.statistic.CMCodeStatisticDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class CMCodeStatisticApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            String from = Util.getStringParam(obj, ParamKey.FROM_TIME);
            String to = Util.getStringParam(obj, ParamKey.TO_TIME);
//            String fromDay = null;
//            if (from != null) {
//                fromDay = from.substring(0, 8);
//            }
//            String toDay = null;
//            if (to != null) {
//                toDay = to.substring(0, 8);
//            }
            List<IEntity> list = CMCodeStatisticDAO.statistic(from, to);
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

//    private static String convertTime(int numOfDay, String dateString) throws Exception{
//        Date date = sdf.parse(dateString);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, numOfDay);
//        return sdf.format(cal.getTime());
//    }
}
