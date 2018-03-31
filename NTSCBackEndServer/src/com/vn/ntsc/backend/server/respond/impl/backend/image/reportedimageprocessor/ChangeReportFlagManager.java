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
public class ChangeReportFlagManager {
    
    private static final Dictionary<Integer, ReportFlagChangeType, String> dic = new Dictionary<>();

    static{
        
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.GOOD_TO_GOOD, "goodToGood_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.GOOD_TO_NG, "goodToNG_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.GOOD_TO_WAITING, "goodToWaiting_approvedStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.NG_TO_GOOD, "NGToGood_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.NG_TO_WAITING, "NGToWaiting_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.NG_TO_NG, "NGToNG_approvedStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.WAITING_TO_GOOD, "WaitingToGood_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.WAITING_TO_NG, "WaitingToNG_approvedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, ReportFlagChangeType.WAITING_TO_WAITING, "WaitingToWaiting_approvedStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.GOOD_TO_GOOD, "GoodToGood_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.GOOD_TO_NG, "GoodToNG_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.GOOD_TO_WAITING, "GoodToWaiting_pendingStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.NG_TO_GOOD, "NGToGood_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.NG_TO_WAITING, "NGToWaiting_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.NG_TO_NG, "NGToNG_pendingStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.WAITING_TO_GOOD, "WaitingToGood_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.WAITING_TO_NG, "WaitingToNG_pendingStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, ReportFlagChangeType.WAITING_TO_WAITING, "WaitingToWaiting_pendingStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.GOOD_TO_GOOD, "GoodToGood_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.GOOD_TO_NG, "GoodToNG_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.GOOD_TO_WAITING, "GoodToWaiting_deniedStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.NG_TO_GOOD, "NGToGood_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.NG_TO_WAITING, "NGToWaiting_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.NG_TO_NG, "NGToNG_deniedStatus");
        
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.WAITING_TO_GOOD, "WaitingToGood_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.WAITING_TO_NG, "WaitingToNG_deniedStatus");
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, ReportFlagChangeType.WAITING_TO_WAITING, "WaitingToWaiting_deniedStatus");
    }
    
    public static String getMethod(Integer key1, ReportFlagChangeType flag){
        return dic.get(key1, flag);
    }    
}
