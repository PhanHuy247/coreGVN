/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.RateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MessageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageStfDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FileDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckUnlockBackstageData;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckUnlockChatData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class CheckUnlockVersion3Api implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String msgId = Util.getStringParam(obj, ParamKey.MESSAGEID);
            String req_user_id = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Util.addDebugLog("=======CheckUnlockVersion3Api req_user_id:" + req_user_id);
            Util.addDebugLog("==========UMS CheckUnlockVersion3Api msgId==" + msgId);
            Long type = Util.getLongParam(obj, "type");
            if (type == 1) {
                result = checkUnlockBackstage(obj, userId);
                Util.addDebugLog("==========UMS CheckUnlockVersion3Api type =2 ");
                MessageDAO.updateReadMessage(msgId, req_user_id);
            } else if (type == 2) {
                result = checkUnlockImage(obj, userId);
                Util.addDebugLog("==========UMS CheckUnlockVersion3Api type =2 ");
                MessageDAO.updateReadMessage(msgId, req_user_id);
            } else if (type == 3) {
                result = checkUnlockVideo(obj, userId);
                Util.addDebugLog("==========UMS CheckUnlockVersion3Api type =3 ");
                MessageDAO.updateReadMessage(msgId, req_user_id);
            } else {
                result = checkUnlockAudio(obj, userId);
                Util.addDebugLog("==========UMS CheckUnlockVersion3Api type =4 ");
                MessageDAO.updateReadMessage(msgId, userId);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private Respond checkUnlockBackstage(JSONObject obj, String userId) throws EazyException {
        Respond result;
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        EntityRespond backRes = new EntityRespond();
        if (friendId != null && !friendId.isEmpty()) {
            if (userId.equals(friendId)) {
                User user = UserDAO.getBackStageInfor(userId);
                ConnectionPrice cp = ConnectionPrice.getBacstageBonusPrice(userId);
                CheckUnlockBackstageData data = new CheckUnlockBackstageData((long) Constant.FLAG.ON, user.point, user.backStageRate, user.backstageNumber, user.rateNumber, null,cp.senderPrice,cp.receiverPrice);
                backRes = new EntityRespond(ErrorCode.SUCCESS, data);
            } else {
                boolean checker = UserDAO.checkUser(friendId);
                if (checker) {
                    if (!BlockUserManager.isBlock(userId, friendId)) {
                        CheckUnlockBackstageData data;
                        ConnectionPrice connectionPrice = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId);
                        int price = 0 - connectionPrice.senderPrice;
                        int bonus = 0 - connectionPrice.receiverPrice;
                        boolean isUnlock = price == 0 || UnlockDAO.isBackStageUnlock(userId, friendId);
                        int ratePoint = RateDAO.getRatePoint(friendId, userId);
                        User user = UserDAO.getBackStageInfor(friendId);
                        int point = UserInforManager.getPoint(userId);
                        if (!isUnlock) {
                            data = new CheckUnlockBackstageData((long) Constant.FLAG.OFF, (long) point, user.backStageRate, user.backstageNumber, user.rateNumber, ratePoint, price, bonus);
                        } else {
                            data = new CheckUnlockBackstageData((long) Constant.FLAG.ON, (long) point, user.backStageRate, user.backstageNumber, user.rateNumber, ratePoint, price, bonus);
                        }
                        backRes = new EntityRespond(ErrorCode.SUCCESS, data);
                    } else {
                        backRes.code = ErrorCode.BLOCK_USER;
                    }
                } else {
                    backRes.code = ErrorCode.USER_NOT_EXIST;
                }
            }

        } else {
            User user = UserDAO.getBackStageInfor(userId);
            CheckUnlockBackstageData data = new CheckUnlockBackstageData((long) Constant.FLAG.ON, user.point, user.backStageRate, user.backstageNumber, user.rateNumber, null);
            backRes = new EntityRespond(ErrorCode.SUCCESS, data);
        }
        result = backRes;
        return result;
    }

    private Respond checkUnlockImage(JSONObject obj, String userId) throws EazyException {
        Respond result;
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String imageId = Util.getStringParam(obj, "image_id");
        EntityRespond backRes = new EntityRespond();
        if (imageId != null && !imageId.isEmpty()) {
            boolean checker = UserDAO.checkUser(friendId);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    CheckUnlockChatData data;
                    ConnectionPrice connectionPrice = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.view_image), userId, friendId);
                    int price = 0 - connectionPrice.senderPrice;
                    int bonus = 0 - connectionPrice.receiverPrice;
                    boolean isUnlock = price == 0 || UnlockDAO.isImageUnlock(userId, imageId);
                    if (!isUnlock) {
                        Image image = ImageDAO.getImageInfor(imageId);
                        if (image != null && image.userId != null) {
                            isUnlock = userId.equals(image.userId);
                        }
                    }
                    int point = UserInforManager.getPoint(userId);
                    int is_free = ImageStfDAO.getIsFree(imageId);
                    Util.addDebugLog("checkUnlockImage " + is_free);
                    if (is_free == 1) {
                        Util.addDebugLog("Free point " + imageId);
                        price = 0;
                        bonus = 0;
                        isUnlock = true;
                    }
                    if (is_free == -1) {
                        Util.addDebugLog("Free point " + imageId);
                        price = 0;
                        bonus = 0;
                        isUnlock = true;
                    }
                    if (!isUnlock) {
                        data = new CheckUnlockChatData((long) Constant.FLAG.OFF, (long) point, price, bonus);
                    } else {
                        data = new CheckUnlockChatData((long) Constant.FLAG.ON, (long) point, price, bonus);
                    }
                    backRes = new EntityRespond(ErrorCode.SUCCESS, data);
                } else {
                    backRes.code = ErrorCode.BLOCK_USER;
                }
            } else {
                backRes.code = ErrorCode.USER_NOT_EXIST;
            }
        }
        result = backRes;
        return result;
    }

    private Respond checkUnlockAudio(JSONObject obj, String userId) throws EazyException {
        Respond result;
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String fileId = Util.getStringParam(obj, "file_id");
        EntityRespond backRes = new EntityRespond();
        if (fileId != null && !fileId.isEmpty()) {
            boolean checker = UserDAO.checkUser(friendId);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    CheckUnlockChatData data;
                    ConnectionPrice connectionPrice = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.listen_audio), userId, friendId);
                    int price = 0 - connectionPrice.senderPrice;
                    int bonus = 0 - connectionPrice.receiverPrice;
                    boolean isUnlock = price == 0 || UnlockDAO.isAudioUnlock(userId, fileId);
                    int point = UserInforManager.getPoint(userId);
                    int is_free = FileDAO.getIsFree(fileId);
                    Util.addDebugLog("checkUnlockAudio " + is_free);
                    if (is_free == 1) {
                        Util.addDebugLog("Free point " + fileId);
                        price = 0;
                        bonus = 0;
                        isUnlock = true;
                    }
                    if (!isUnlock) {
                        data = new CheckUnlockChatData((long) Constant.FLAG.OFF, (long) point, price, bonus);
                    } else {
                        data = new CheckUnlockChatData((long) Constant.FLAG.ON, (long) point, price, bonus);
                    }
                    backRes = new EntityRespond(ErrorCode.SUCCESS, data);
                } else {
                    backRes.code = ErrorCode.BLOCK_USER;
                }
            } else {
                backRes.code = ErrorCode.USER_NOT_EXIST;
            }
        }
        result = backRes;
        return result;
    }

    private Respond checkUnlockVideo(JSONObject obj, String userId) throws EazyException {
        Respond result;
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String fileId = Util.getStringParam(obj, "file_id");
        EntityRespond backRes = new EntityRespond();
        if (fileId != null && !fileId.isEmpty()) {
            boolean checker = UserDAO.checkUser(friendId);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    CheckUnlockChatData data;
                    ConnectionPrice connectionPrice = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.watch_video), userId, friendId);
                    int price = 0 - connectionPrice.senderPrice;
                    int bonus = 0 - connectionPrice.receiverPrice;
                    boolean isUnlock = price == 0 || UnlockDAO.isVideoUnlock(userId, fileId);
                    int point = UserInforManager.getPoint(userId);
                    int is_free = FileDAO.getIsFree(fileId);
                    Util.addDebugLog("checkUnlockVideo " + is_free);
                    if (is_free == 1) {
                        Util.addDebugLog("Free point " + fileId);
                        price = 0;
                        bonus = 0;
                        isUnlock = true;
                    }
                    if (!isUnlock) {
                        data = new CheckUnlockChatData((long) Constant.FLAG.OFF, (long) point, price, bonus);
                    } else {
                        data = new CheckUnlockChatData((long) Constant.FLAG.ON, (long) point, price, bonus);
                    }
                    backRes = new EntityRespond(ErrorCode.SUCCESS, data);
                } else {
                    backRes.code = ErrorCode.BLOCK_USER;
                }
            } else {
                backRes.code = ErrorCode.USER_NOT_EXIST;
            }
        }
        result = backRes;
        return result;
    }

}
