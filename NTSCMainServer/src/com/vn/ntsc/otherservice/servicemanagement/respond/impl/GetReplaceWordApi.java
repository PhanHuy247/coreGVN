/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import eazycommon.backlist.SizedListData;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.ReplaceWordDAO;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.BannedWord;
import com.vn.ntsc.otherservice.entity.impl.GetBannedWordData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetReplaceWordApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
        try {
            Long clientVersion = Util.getLongParam(request.reqObj, "version");
            if (clientVersion == null) {
                return null;
            }
            Integer serverVersion = ReplaceWordDAO.getLastestVersion();
            if (clientVersion.intValue() == serverVersion) {
                result.code = ErrorCode.NO_CHANGE;
            } else {
//                SizedListData list = ReplaceWordDAO.getList(Constant.FLAG.ON, null, null);
//                List<String> words = new ArrayList<>();
//                for (BannedWord word : list) {
//                    words.add(word.word);
//                }
//                GetBannedWordData data = new GetBannedWordData(serverVersion, words);
//                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
