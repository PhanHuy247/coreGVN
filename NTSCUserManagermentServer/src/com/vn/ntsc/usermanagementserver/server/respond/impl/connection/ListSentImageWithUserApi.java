/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import java.util.ArrayList;
import java.util.List;
import com.vn.ntsc.usermanagementserver.dao.impl.ChatImageTransactionDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageStfDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.SentImage;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListSentImageWithUserApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String requestId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            SizedListData data = ChatImageTransactionDAO.list(userId, requestId, skip.intValue(), take.intValue());
            addUnlockInfoToResult(data, userId, requestId);

            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private void addUnlockInfoToResult(SizedListData data, String userId, String requestId) throws EazyException {

        List<String> imageIds = new ArrayList<>();
        for (Object object : data.listEntity) {
            SentImage sentImage = (SentImage) object;
            imageIds.add(sentImage.imageId);
        }
        List<String> imageIdsUnlocked = UnlockDAO.getImageIdsUnlocked(userId, imageIds);

        for (Object object : data.listEntity) {
            SentImage sentImage = (SentImage) object;
            sentImage.setIsUnlock(sentImage.isOwn ? true : imageIdsUnlocked.contains(sentImage.imageId));
            int is_free = ImageStfDAO.getIsFree(sentImage.imageId);
            if (is_free == 1) {
                sentImage.setIsUnlock(true);
            }
            User u = UserDAO.getUserInfor(userId);
            Util.addDebugLog("addUnlockInfoToResult is_unlock " + u.gender);
            if (u.gender == Constant.GENDER.FEMALE) {
                Util.addDebugLog("addUnlockInfoToResult is_unlock ");
                sentImage.setIsUnlock(true);
            }
        }

    }

}
