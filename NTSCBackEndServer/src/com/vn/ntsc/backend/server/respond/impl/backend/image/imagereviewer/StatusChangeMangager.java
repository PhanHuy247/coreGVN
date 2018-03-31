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
public class StatusChangeMangager {
    
    private static final Dictionary<Integer, Integer, ImageStatusChangeType> dic = new Dictionary<>();
    
    static{
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, Constant.REVIEW_STATUS_FLAG.APPROVED, ImageStatusChangeType.APPROVED_TO_APPROVED);
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, Constant.REVIEW_STATUS_FLAG.DENIED, ImageStatusChangeType.APPROVED_TO_DENIED);
        dic.put(Constant.REVIEW_STATUS_FLAG.APPROVED, Constant.REVIEW_STATUS_FLAG.PENDING, ImageStatusChangeType.APPROVED_TO_PENDING);
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, Constant.REVIEW_STATUS_FLAG.APPROVED, ImageStatusChangeType.PENDING_TO_APPROVED);
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, Constant.REVIEW_STATUS_FLAG.DENIED, ImageStatusChangeType.PENDING_TO_DENIED);
        dic.put(Constant.REVIEW_STATUS_FLAG.PENDING, Constant.REVIEW_STATUS_FLAG.PENDING, ImageStatusChangeType.PENDING_TO_PENDING);
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, Constant.REVIEW_STATUS_FLAG.APPROVED, ImageStatusChangeType.DENIED_TO_APPROVED);
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, Constant.REVIEW_STATUS_FLAG.DENIED, ImageStatusChangeType.DENIED_TO_DENIED);
        dic.put(Constant.REVIEW_STATUS_FLAG.DENIED, Constant.REVIEW_STATUS_FLAG.PENDING, ImageStatusChangeType.DENIED_TO_PENDING);
    }
    
    public static ImageStatusChangeType getStatusChange(Integer key1, Integer key2){
        return dic.get(key1, key2);
    }
}
