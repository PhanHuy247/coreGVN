/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class UpdateStaticPageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        Respond result = new Respond();
        try {
            Long pageType = Util.getLongParam(request.reqObj, "page_type");
            String pageContent = Util.getStringParam(request.reqObj, "page_cnt");
            String fileName = null;
            if (pageType == null) {
                return result;
            }
            if (pageType == Constant.STATIC_FILE_TYPE.PRIVACY_POLICY) {
                fileName = FilesAndFolders.FILES.PRIVACY_POLICY_FILE;
            } else if (pageType == Constant.STATIC_FILE_TYPE.SAFETY_TIPS) {
                fileName = FilesAndFolders.FILES.SAFETY_TIPS_FILE;
            } else if (pageType == Constant.STATIC_FILE_TYPE.TERM_OF_USE) {
                fileName = FilesAndFolders.FILES.TERM_OF_USE_FILE;
            }
            Util.writeFile(fileName, pageContent);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
