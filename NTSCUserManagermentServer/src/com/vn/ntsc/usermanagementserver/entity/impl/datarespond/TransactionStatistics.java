/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class TransactionStatistics implements IEntity {

    private static final String timeKey = "time";
    public String time;

    private static final String iosMoneyKey = "money_appstore";
    public Double iosMoney;

    private static final String iosTimesKey = "count_appstore";
    public Integer iosTimes;

    private static final String androidMoneyKey = "money_googleplay";
    public Double androidMoney;

    private static final String androidTimesKey = "count_googleplay";
    public Integer androidTimes;
    
    private static final String biscashMoneyKey = "money_bitcash";
    public Double biscashMoney;

    private static final String biscashTimesKey = "count_bitcash";
    public Integer biscashTimes;

    private static final String creditMoneyKey = "money_credit";
    public Double creditMoney;

    private static final String creditTimesKey = "count_credit";
    public Integer creditTimes;
    
    private static final String pointBackMoneyKey = "money_point_back";
    public Double pointBackMoney;

    private static final String pointBackTimesKey = "count_point_back";
    public Integer pointBackTimes;
    
    private static final String cCheckMoneyKey = "money_c_check";
    public Double cCheckMoney;

    private static final String cCheckTimesKey = "count_c_check";
    public Integer cCheckTimes;
    
    private static final String conboniMoneyKey = "money_conboni";
    public Double conboniMoney;

    private static final String conboniTimesKey = "count_conboni";
    public Integer conboniTimes;

    public TransactionStatistics(){
        time = null;
        iosMoney = 0.0;
        iosTimes = 0;
        androidMoney = 0.0;
        androidTimes = 0;
        biscashMoney = 0.0;
        biscashTimes = 0;
        creditMoney = 0.0;
        creditTimes = 0;
        pointBackMoney = 0.0;
        pointBackTimes = 0;
        cCheckMoney = 0.0;
        cCheckTimes = 0;
        conboniMoney = 0.0;
        conboniTimes = 0;
    }

    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

//        if (this.time != null) {
//            jo.put(timeKey, this.time);
//        }
        if (this.iosMoney != null) {
            jo.put(iosMoneyKey, this.iosMoney);
        }
        if (this.androidMoney != null) {
            jo.put(androidMoneyKey, this.androidMoney);
        }
        if (this.iosTimes != null) {
            jo.put(iosTimesKey, this.iosTimes);
        }
        if (this.androidTimes != null) {
            jo.put(androidTimesKey, this.androidTimes);
        }
        if (this.biscashMoney != null) {
            jo.put(biscashMoneyKey, this.biscashMoney);
        }
        if (this.biscashTimes != null) {
            jo.put(biscashTimesKey, this.biscashTimes);
        }
        if (this.creditMoney != null) {
            jo.put(creditMoneyKey, this.creditMoney);
        }
        if (this.creditTimes != null) {
            jo.put(creditTimesKey, this.creditTimes);
        }
        if (this.pointBackMoney != null) {
            jo.put(pointBackMoneyKey, this.pointBackMoney);
        }
        if (this.pointBackTimes != null) {
            jo.put(pointBackTimesKey, this.pointBackTimes);
        }
        if (this.cCheckMoney != null) {
            jo.put(cCheckMoneyKey, this.cCheckMoney);
        }
        if (this.cCheckTimes != null) {
            jo.put(cCheckTimesKey, this.cCheckTimes);
        }
        if (this.conboniMoney != null) {
            jo.put(conboniMoneyKey, this.conboniMoney);
        }
        if (this.conboniTimes != null) {
            jo.put(conboniTimesKey, this.conboniTimes);
        }

        return jo;
    }

}
