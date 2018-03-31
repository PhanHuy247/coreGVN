/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor;

import eazycommon.constant.Constant;
import eazycommon.util.Dictionary;

/**
 *
 * @author Rua
 */
public class ReportFlagChangeManager {
    
    private static final Dictionary<Integer, Integer, ReportFlagChangeType> dic = new Dictionary<>();
    
    static{
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, Constant.REPORT_STATUS_FLAG.GOOD, ReportFlagChangeType.GOOD_TO_GOOD);
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, Constant.REPORT_STATUS_FLAG.NOT_GOOD, ReportFlagChangeType.GOOD_TO_NG);
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, Constant.REPORT_STATUS_FLAG.WAITING, ReportFlagChangeType.GOOD_TO_WAITING);
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, Constant.REPORT_STATUS_FLAG.GOOD, ReportFlagChangeType.WAITING_TO_GOOD);
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, Constant.REPORT_STATUS_FLAG.NOT_GOOD, ReportFlagChangeType.WAITING_TO_NG);
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, Constant.REPORT_STATUS_FLAG.WAITING, ReportFlagChangeType.WAITING_TO_WAITING);
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, Constant.REPORT_STATUS_FLAG.GOOD, ReportFlagChangeType.NG_TO_GOOD);
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, Constant.REPORT_STATUS_FLAG.NOT_GOOD, ReportFlagChangeType.NG_TO_NG);
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, Constant.REPORT_STATUS_FLAG.WAITING, ReportFlagChangeType.NG_TO_WAITING);
    }
    
    public static ReportFlagChangeType getFlagChange(Integer key1, Integer key2){
        return dic.get(key1, key2);
    }    
}
