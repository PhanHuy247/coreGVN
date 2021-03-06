/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogSalePointDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogTransactionDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.TransactionStatistics;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetSaleStatisticApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
        try {
            String fromDateStr = Util.getStringParam(obj, "from_date");
            String toDateStr = Util.getStringParam(obj, "to_date");
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            Date toDate = DateFormat.parse_yyyyMMdd(toDateStr);
            Date fromDate = DateFormat.parse_yyyyMMdd(fromDateStr);
            TransactionStatistics statistic = new TransactionStatistics();
            LogSalePointDAO.getStatistic(userId, fromDate, toDate, statistic);
            LogTransactionDAO.getStatistic(userId, fromDate, toDate, statistic);
            respond = new EntityRespond(ErrorCode.SUCCESS, statistic);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
