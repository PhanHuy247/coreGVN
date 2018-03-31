/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.statistic;

import com.vn.ntsc.backend.entity.IEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class TransactionStatistics implements IEntity {

    private static final String timeKey = "time";
    public String time;

    private static final String iosMoneyKey = "ios_money";
    public Double iosMoney;

    private static final String iosTimesKey = "ios_times";
    public Integer iosTimes;

    private static final String androidMoneyKey = "android_money";
    public Double androidMoney;

    private static final String androidTimesKey = "android_times";
    public Integer androidTimes;
    
    private static final String amazonMoneyKey = "amazon_money";
    public Double amazonMoney;

    private static final String amazonTimesKey = "amazon_times";
    public Integer amazoneTimes;
    
    private static final String biscashMoneyKey = "biscash_money";
    public Double biscashMoney;

    private static final String biscashTimesKey = "biscash_times";
    public Integer biscashTimes;

    private static final String creditMoneyKey = "credit_money";
    public Double creditMoney;

    private static final String creditTimesKey = "credit_times";
    public Integer creditTimes;
    
    private static final String pointBackMoneyKey = "point_back_money";
    public Double pointBackMoney;

    private static final String pointBackTimesKey = "point_back_times";
    public Integer pointBackTimes;
    
    private static final String cCheckMoneyKey = "c_check_money";
    public Double cCheckMoney;

    private static final String cCheckTimesKey = "c_check_times";
    public Integer cCheckTimes;
    
    private static final String conboniMoneyKey = "conboni_money";
    public Double conboniMoney;

    private static final String conboniTimesKey = "conboni_times";
    public Integer conboniTimes;    

    public TransactionStatistics(){
        time = null;
        iosMoney = 0.0;
        iosTimes = 0;
        androidMoney = 0.0;
        androidTimes = 0;
        amazonMoney = 0.0;
        amazoneTimes = 0;
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

    public TransactionStatistics(String time, Double iosMoney, Integer iosTimes, Double androidMoney, Integer androidTimes,
                        Double bitCashMoney, Integer bitcashTimes, Double creditMoney, Integer creditTimes,
                        Double pointBackMoney, Integer pointBackTimes, Double cCheckMoney, Integer cCheckTimes,
                        Double conboniMoney, Integer conboniTimes, Double amazoneMoney, Integer amazoneTimes) {
        this.time = time;
        this.iosMoney = iosMoney;
        this.iosTimes = iosTimes;
        this.androidMoney = androidMoney;
        this.androidTimes = androidTimes;
        this.biscashMoney = bitCashMoney;
        this.creditMoney = creditMoney;
        this.biscashTimes = bitcashTimes;
        this.creditTimes = creditTimes;
        this.pointBackMoney = pointBackMoney;
        this.pointBackTimes = pointBackTimes;
        this.cCheckMoney = cCheckMoney;
        this.cCheckTimes = cCheckTimes;
        this.conboniMoney = conboniMoney;
        this.conboniTimes = conboniTimes;
        this.amazonMoney = amazoneMoney;
        this.amazoneTimes = amazoneTimes;
    }
    
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.time != null) {
            jo.put(timeKey, this.time);
        }
        if (this.iosMoney != null) {
            jo.put(iosMoneyKey, this.iosMoney);
        }
        if (this.androidMoney != null) {
            jo.put(androidMoneyKey, this.androidMoney);
        }
        if (this.amazonMoney != null) {
            jo.put(amazonMoneyKey, this.amazonMoney);
        }
        if (this.iosTimes != null) {
            jo.put(iosTimesKey, this.iosTimes);
        }
        if (this.androidTimes != null) {
            jo.put(androidTimesKey, this.androidTimes);
        }
        if (this.amazoneTimes != null) {
            jo.put(amazonTimesKey, this.amazoneTimes);
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
