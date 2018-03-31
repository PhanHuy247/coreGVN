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
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MessageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageStfDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FileDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Calendar;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckUnlockChatData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class UnlockVersion3Api implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond res = null;
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String msgId = Util.getStringParam(obj, ParamKey.MESSAGEID);
            // Long unlockType = Util.getLongParam(obj, ParamKey.UNLOCK_TYPE);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            //int curPoint = UserPointManager.getPoint(userId);
            int point = UserInforManager.getPoint(userId);
//            int point = userDAO.getPoint(userId);
            Long type = Util.getLongParam(obj, "type");
            if (type == 1) {
                res = unlockBackstage(obj, userId, point, ip, time);
            } else if (type == 2) {
                res = unlockImage(obj, userId, point, ip, time);
                MessageDAO.updateReadMessage(msgId, userId);
            } else if (type == 3) {
                res = unlockVideo(obj, userId, point, ip, time);
                MessageDAO.updateReadMessage(msgId, userId);
            } else {
                res = unlockAudio(obj, userId, point, ip, time);
                MessageDAO.updateReadMessage(msgId, userId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return res;
    }

    private Respond unlockBackstage(JSONObject obj, String userId, int point, String ip, Date time) throws EazyException {
        EntityRespond result = new EntityRespond();
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        int price = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId).senderPrice;
        if (price == 0) {
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
                            result.data = new CheckUnlockChatData((long) point, price);
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
        return result;
    }

    private Respond unlockImage(JSONObject obj, String userId, int point, String ip, Date time) throws EazyException {
        EntityRespond result = new EntityRespond();
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String imageId = Util.getStringParam(obj, "image_id");
        int price = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.view_image), userId, friendId).senderPrice;

        int is_free = ImageStfDAO.getIsFree(imageId);
        Util.addDebugLog("unlockImage " + is_free);
        if (is_free == 1) {
            Util.addDebugLog("Free point " + imageId);
            price = 0;
        }

        if (price == 0) {
            result.data = new Point(point);
            result.code = ErrorCode.SUCCESS;
        }
        boolean isUnlock = UnlockDAO.isImageUnlock(userId, imageId);
        if (!isUnlock) {
            Image image = ImageDAO.getImageInfor(imageId);
            if (image != null && image.userId != null) {
                isUnlock = userId.equals(image.userId);
            }
        }
        if (!isUnlock) {
            if (UserDAO.checkUser(friendId)) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    //int number = 3;
                    if (is_free == 0) {
                        ActionResult actionResult = ActionManager.doAction(ActionType.view_image, userId, friendId, time, null, null, ip);
                        if (actionResult.code == ErrorCode.SUCCESS) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(time);
                            cal.add(Calendar.HOUR, Setting.VIEW_IMAGE_TIME);
                            Date endTime = cal.getTime();
                            String id = UnlockDAO.addUnlockImage(userId, endTime.getTime(), friendId, imageId);

                            UnlockInfor infor = new UnlockInfor(id, userId, endTime.getTime());
                            UnlockManager.put(infor);

                            result.data = new Point(actionResult.point);
                            result.code = ErrorCode.SUCCESS;
                            return result;
                        } else {
                            result.code = actionResult.code;
                            result.data = new CheckUnlockChatData((long) point, price);
                        }
                    } else {
                        Util.addDebugLog("Free point " + imageId);
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
        return result;
    }

    private Respond unlockVideo(JSONObject obj, String userId, int point, String ip, Date time) throws EazyException {
        EntityRespond result = new EntityRespond();
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String fileId = Util.getStringParam(obj, "file_id");
        int price = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.watch_video), userId, friendId).senderPrice;
        int is_free = FileDAO.getIsFree(fileId);
        Util.addDebugLog("unlockVideo " + is_free);
        if (is_free == 1) {
            Util.addDebugLog("Free point " + fileId);
            price = 0;
        }
        if (price == 0) {
            result.data = new Point(point);
            result.code = ErrorCode.SUCCESS;
        }
        if (!UnlockDAO.isVideoUnlock(userId, fileId)) {
            if (UserDAO.checkUser(friendId)) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    //int number = 3;
                    if (is_free == 0) {
                        ActionResult actionResult = ActionManager.doAction(ActionType.watch_video, userId, friendId, time, null, null, ip);
                        if (actionResult.code == ErrorCode.SUCCESS) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(time);
                            cal.add(Calendar.HOUR, Setting.WATCH_VIDEO_TIME);
                            Date endTime = cal.getTime();
                            String id = UnlockDAO.addUnlockVideo(userId, endTime.getTime(), friendId, fileId);

                            UnlockInfor infor = new UnlockInfor(id, userId, endTime.getTime());
                            UnlockManager.put(infor);

                            result.data = new Point(actionResult.point);
                            result.code = ErrorCode.SUCCESS;
                            return result;
                        } else {
                            result.code = actionResult.code;
                            result.data = new CheckUnlockChatData((long) point, price);
                        }
                    } else {
                        Util.addDebugLog("Free point " + fileId);
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
        return result;
    }

    private Respond unlockAudio(JSONObject obj, String userId, int point, String ip, Date time) throws EazyException {
        EntityRespond result = new EntityRespond();
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        String fileId = Util.getStringParam(obj, "file_id");
        int price = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.listen_audio), userId, friendId).senderPrice;
        int is_free = FileDAO.getIsFree(fileId);
        Util.addDebugLog("unlockAudio " + is_free);
        if (is_free == 1) {
            Util.addDebugLog("Free point " + fileId);
            price = 0;
        }
        if (price == 0) {
            result.data = new Point(point);
            result.code = ErrorCode.SUCCESS;
        }
        if (!UnlockDAO.isAudioUnlock(userId, fileId)) {
            if (UserDAO.checkUser(friendId)) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    //int number = 3;
                    if (is_free == 0) {
                        ActionResult actionResult = ActionManager.doAction(ActionType.listen_audio, userId, friendId, time, null, null, ip);
                        if (actionResult.code == ErrorCode.SUCCESS) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(time);
                            cal.add(Calendar.HOUR, Setting.LISTEN_AUDIO_TIME);
                            Date endTime = cal.getTime();
                            String id = UnlockDAO.addUnlockAudio(userId, endTime.getTime(), friendId, fileId);

                            UnlockInfor infor = new UnlockInfor(id, userId, endTime.getTime());
                            UnlockManager.put(infor);

                            result.data = new Point(actionResult.point);
                            result.code = ErrorCode.SUCCESS;
                            return result;
                        } else {
                            result.code = actionResult.code;
                            result.data = new CheckUnlockChatData((long) point, price);
                        }
                    } else {
                        Util.addDebugLog("Free point " + fileId);
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
        return result;
    }

}
