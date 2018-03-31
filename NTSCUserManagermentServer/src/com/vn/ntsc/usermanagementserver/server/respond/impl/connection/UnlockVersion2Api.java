/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionResult;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.unlockpool.UnlockInfor;
import com.vn.ntsc.usermanagementserver.server.unlockpool.UnlockManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Calendar;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class UnlockVersion2Api implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond res = null;
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            // Long unlockType = Util.getLongParam(obj, ParamKey.UNLOCK_TYPE);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            //int curPoint = UserPointManager.getPoint(userId);
            int point = UserInforManager.getPoint(userId);
//            int point = userDAO.getPoint(userId);
            EntityRespond result = new EntityRespond();
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            int price = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId).senderPrice;
            if(price == 0){
                result.data = new Point(point);
                result.code = ErrorCode.SUCCESS;
            }
            if (!UnlockDAO.isBackStageUnlock(userId, friendId)) {
                if (UserDAO.checkUser(friendId)) {
                    if (!BlockUserManager.isBlock(userId, friendId)) {
                        int number = UserDAO.getBackStageNumber(friendId);
                        //int number = 3;
                        if (number > 0) {
                            ActionResult actionResult = ActionManager.doAction(ActionType.unlock_backstage, userId, friendId, time, null, null, ip);
                            if (actionResult.code == ErrorCode.SUCCESS) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(time);
                                cal.add(Calendar.HOUR, Setting.BACKSTAGE_TIME);
                                Date endTime = cal.getTime();
                                String id = UnlockDAO.addUnlockBackstage(userId, endTime.getTime(), friendId);

                                UnlockInfor infor = new UnlockInfor(id, userId, endTime.getTime());
                                UnlockManager.put(infor);

                                result.data = new Point(actionResult.point);
                                result.code = ErrorCode.SUCCESS;
                                return result;
                            } else {
                                result.code = actionResult.code;
                                result.data = new Point(point);
                            }
                        } else {
                            result.code = ErrorCode.EMPTY_DATA;
                        }
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                }
            } else {
                result.data = new Point(point);
                result.code = ErrorCode.SUCCESS;
            }
            res = result;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return res;
    }

}
