/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.statistic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.statistic.InstallationStatisticDAO;
import com.vn.ntsc.backend.entity.impl.statistic.InstallationStatistic;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class InstallationStatisticApi implements IApiAdapter {

    private static final String first_day_of_month = "01";
    private static final String first_hour_of_day = "000000";
    private static final String end_hour_of_day = "235959";

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            Long type = Util.getLongParam(obj, "statistic_type");
            String val = Util.getStringParam(obj, "statistic_time");
            if (type == null || val == null || val.isEmpty()) {
                return null;
            }
            Long timezone = Util.getLongParam(obj, "time_zone");
            if (timezone == null || timezone < -12 || timezone > 12) {
                return null;
            }
            long addTime = timezone * 3600 * 1000;
            List<InstallationStatistic> list = new ArrayList<>();
            if (type == 0) {
                Date date = DateFormat.parse_yyyyMMdd(val);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);
                long from = cal.getTimeInMillis();
                cal.add(Calendar.DATE, 2);
                long to = cal.getTimeInMillis();
                long fromTime = getTime(val + first_hour_of_day);
                long toTime = getTime(val + end_hour_of_day);
                list = InstallationStatisticDAO.getDayStatistic(from, to, addTime, fromTime, toTime);
//                list.add(new InstallationStatistic("2015031601", 1, 1));
//                list = UserDailyStatisticDAO.getStatistic(val);
            } else if (type == 1) {
                val += first_day_of_month;
                long from = getFrom(val);
                long to = getTo(val);
                long fromTime = getFromTime(val);
                long toTime = getToTime(val);
                list = InstallationStatisticDAO.getMonthStatistic(from, to, addTime, fromTime, toTime);
//                list.add(new InstallationStatistic("20150316", 1, 1));
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private static long getFromTime(String dateTime) throws Exception {
        long time = DateFormat.parse_yyyyMMdd(dateTime).getTime();
        return time;
    }

    private static long getToTime(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyyMMdd(dateTime));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        long time = cal.getTime().getTime();
        return time;
    }

    private static long getTime(String dateTime) throws Exception {
        long time = DateFormat.parse(dateTime).getTime();
        return time;
    }

    private static long getFrom(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyyMMdd(dateTime));
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }    
    
    private static long getTo(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyyMMdd(dateTime));
        cal.add(Calendar.MONTH, 1);
        return cal.getTimeInMillis();
    }    
}
