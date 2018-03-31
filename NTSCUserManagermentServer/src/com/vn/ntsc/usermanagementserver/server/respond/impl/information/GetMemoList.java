/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.MemoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Memo;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Administrator
 */
public class GetMemoList implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Util.addDebugLog("Test for request from client "+obj.toJSONString());
        Respond respond = null;
        String userId = (String)obj.get("user_id");
        Long take = (Long)obj.get("take");
        Long skip = (Long)obj.get("skip");
        if (skip == null || take == null) {
            return null;
        }
        List<Memo> data = MemoDAO.getMemoListByUserId(userId, skip, take);
        Util.addDebugLog("Test for data "+data);
        respond = new ListEntityRespond(ErrorCode.SUCCESS, data);
        return respond;
    }
    
}
