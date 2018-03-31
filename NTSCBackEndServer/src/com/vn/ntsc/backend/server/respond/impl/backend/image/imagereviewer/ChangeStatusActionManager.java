/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer;

import eazycommon.constant.Constant;
import eazycommon.util.Dictionary;


/**
 *
 * @author Rua
 */
public class ChangeStatusActionManager {

    private static final Dictionary<Integer, ImageStatusChangeType, String> dic = new Dictionary<>();

    static{
        
//        dic.put(Constant.GOOD, ImageStatusChange.APPROVED_TO_APPROVED, "approvedToApproved_goodReport");
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.APPROVED_TO_DENIED, "approvedToDenied_goodReport");
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.APPROVED_TO_PENDING, "approvedToPending_goodReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.DENIED_TO_APPROVED, "deniedToApproved_goodReport");
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.DENIED_TO_PENDING, "deniedToPending_goodReport");
//        dic.put(Constant.GOOD, ImageStatusChange.DENIED_TO_DENIED, "deniedToDenied_goodReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.PENDING_TO_APPROVED, "pendingToApproved_goodReport");
        dic.put(Constant.REPORT_STATUS_FLAG.GOOD, ImageStatusChangeType.PENDING_TO_DENIED, "pendingToDenied_goodReport");
//        dic.put(Constant.GOOD, ImageStatusChange.PENDING_TO_PENDING, "pendingToPending_goodReport");
        
//        dic.put(Constant.WAITING, ImageStatusChange.APPROVED_TO_APPROVED, "approvedToApproved_waitingReport");
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.APPROVED_TO_DENIED, "approvedToDenied_waitingReport");
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.APPROVED_TO_PENDING, "approvedToPending_waitingReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.DENIED_TO_APPROVED, "deniedToApproved_waitingReport");
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.DENIED_TO_PENDING, "deniedToPending_waitingReport");
//        dic.put(Constant.WAITING, ImageStatusChange.DENIED_TO_DENIED, "deniedToDenied_waitingReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.PENDING_TO_APPROVED, "pendingToApproved_waitingReport");
        dic.put(Constant.REPORT_STATUS_FLAG.WAITING, ImageStatusChangeType.PENDING_TO_DENIED, "pendingToDenied_waitingReport");
//        dic.put(Constant.WAITING, ImageStatusChangeType.PENDING_TO_PENDING, "pendingToPending_waitingReport");
        
//        dic.put(Constant.NOT_GOOD, ImageStatusChangeType.APPROVED_TO_APPROVED, "approvedToApproved_NGReport");
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.APPROVED_TO_DENIED, "approvedToDenied_NGReport");
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.APPROVED_TO_PENDING, "approvedToPending_NGReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.DENIED_TO_APPROVED, "deniedToApproved_NGReport");
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.DENIED_TO_PENDING, "deniedToPending_NGReport");
//        dic.put(Constant.NOT_GOOD, ImageStatusChangeType.DENIED_TO_DENIED, "deniedToDenied_NGReport");
        
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.PENDING_TO_APPROVED, "pendingToApproved_NGReport");
        dic.put(Constant.REPORT_STATUS_FLAG.NOT_GOOD, ImageStatusChangeType.PENDING_TO_DENIED, "pendingToDenied_NGReport");
//        dic.put(Constant.NOT_GOOD, ImageStatusChangeType.PENDING_TO_PENDING, "pendingToPending_NGReport");
    }
    
    public static String getMethod(Integer key1, ImageStatusChangeType status){
        return dic.get(key1, status);
    }    
}
