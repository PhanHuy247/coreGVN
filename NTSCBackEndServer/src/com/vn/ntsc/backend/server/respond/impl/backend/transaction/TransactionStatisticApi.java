/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.statistic.TransactionStatisticDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.statistic.TransactionStatistics;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class TransactionStatisticApi implements IApiAdapter {

    private static final String first_day_of_month = "/01";
//    private static final String first_month = "01";

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            String val = Util.getStringParam(obj, "statistic_time");
            Long timezone = Util.getLongParam(obj, "time_zone");
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            if (timezone == null || timezone < -12 || timezone > 12) {
                return null;
            }
            if (val == null || val.isEmpty()) {
                return null;
            }
            if (type == null || type < 1) {
                return null;
            }
            List<IEntity> list = new ArrayList<>();
            if (type == 1) {
                list = getListDayStatistic(val, timezone);
            } else if (type == 2) {
                for (int i = 1; i <= 12; i++) {
                    String month = String.valueOf(i);
                    if (i < 10) {
                        month = "0" + String.valueOf(i);
                    }
                    String time = val + "/" + month;
                    List<IEntity> listDay = getListDayStatistic(time, timezone);
                    list.add(getMonthStatistic(listDay, time));
                }
            } else {
                list = getListHourStatistic(val, timezone);
            }
//            List<BaseEntity> list = TransactionStatisticDAO.getStatistic(val);            
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private List<IEntity> getListHourStatistic(String val, Long timezone) throws EazyException, Exception {
        List<IEntity> list;
        String fromDay = getBeforeDay(val);
        String toDay = getAfterDay(val);
        long addTime = timezone * 3600 * 1000;
        long fromTime = getFromTime(val);
        long toTime = getToTimeHours(val);
        Util.addDebugLog("addTime " + addTime + " fromTime " + fromTime + " toTime " + toTime);
        list = TransactionStatisticDAO.getHourStatistic(fromDay, toDay, addTime, fromTime, toTime);
        return list;
    }

    private List<IEntity> getListDayStatistic(String val, Long timezone) throws EazyException, Exception {
        List<IEntity> list;
        val += first_day_of_month;
        String fromDay = getBeforeDay(val);
        String toDay = getLastDayOfMonth(val);
        long addTime = timezone * 3600 * 1000;
        long fromTime = getFromTime(val);
        long toTime = getToTimeInDays(val);
        Util.addDebugLog("addTime " + addTime + " fromTime " + fromTime + " toTime " + toTime);
        list = TransactionStatisticDAO.getStatistic(fromDay, toDay, addTime, fromTime, toTime);
        return list;
    }

    private static String getAfterDay(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyy_MM_dd(dateTime));
        cal.add(Calendar.DATE, 1);
        return DateFormat.format_yyyyMMdd(cal.getTime());
    }

    private static String getLastDayOfMonth(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyy_MM_dd(dateTime));
        cal.add(Calendar.MONTH, 1);
        return DateFormat.format_yyyyMMdd(cal.getTime());
    }

    private static String getBeforeDay(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyy_MM_dd(dateTime));
        cal.add(Calendar.DATE, -1);
        return DateFormat.format_yyyyMMdd(cal.getTime());
    }

    private static long getFromTime(String dateTime) throws Exception {
        long time = DateFormat.parse_yyyy_MM_dd(dateTime).getTime();
        return time;
    }

    private static long getToTimeInDays(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyy_MM_dd(dateTime));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        long time = cal.getTime().getTime();
        return time;
    }

    private static long getToTimeHours(String dateTime) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormat.parse_yyyy_MM_dd(dateTime));
        cal.add(Calendar.DATE, 1);
        //HUNGDT edit
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.SECOND, -1);
        long time = cal.getTime().getTime();
        return time;
    }

    private static IEntity getMonthStatistic(List<IEntity> list, String time) {
        int iosTime = 0;
        double iosMoney = 0;
        int androidTime = 0;
        double androidMoney = 0;
        int amazonTime = 0;
        double amazonMoney = 0;
        int biscashTime = 0;
        double biscahsMoney = 0;
        int creditTime = 0;
        double creditMoney = 0;
        int pointBackTime = 0;
        double pointBackMoney = 0;
        int cCheckTime = 0;
        double cCheckMoney = 0;
        int conboniTime = 0;
        double conboniMoney = 0;
        for (IEntity entity : list) {
            TransactionStatistics tran = (TransactionStatistics) entity;
            iosTime += tran.iosTimes;
            iosMoney += tran.iosMoney;
            androidMoney += tran.androidMoney;
            androidTime += tran.androidTimes;
            amazonMoney += tran.amazonMoney;
            amazonTime += tran.amazoneTimes;
            biscashTime += tran.biscashTimes;
            biscahsMoney += tran.biscashMoney;
            creditTime += tran.creditTimes;
            creditMoney += tran.creditMoney;

            pointBackTime += tran.pointBackTimes;
            pointBackMoney += tran.pointBackMoney;
            cCheckMoney += tran.cCheckMoney;
            cCheckTime += tran.cCheckTimes;
            conboniMoney += tran.conboniMoney;
            conboniTime += tran.conboniTimes;
        }
        return new TransactionStatistics(time, iosMoney, iosTime, androidMoney, androidTime, biscahsMoney,
                biscashTime, creditMoney, creditTime,
                pointBackMoney, pointBackTime,
                cCheckMoney, cCheckTime, conboniMoney, conboniTime,
                amazonMoney, amazonTime);
    }

}
