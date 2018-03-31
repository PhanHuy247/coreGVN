/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.pointpackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.pointpackage.ActionPointPacket;

/**
 *
 * @author RuAc0n
 */
public class ActionPointPacketDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCashDB().getCollection(CashdbKey.ACTION_POINT_PACKET_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<ActionPointPacket> get(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                ActionPointPacket pp = new ActionPointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                pp.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                pp.price = price;
                Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                pp.point = point;
                Integer firstPurchasePoint = (Integer) obj.get(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT);
                pp.firstPurchasePoint = firstPurchasePoint;
                String firstPurchaseDescription = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC);
                pp.firstPurchaseDescription = firstPurchaseDescription;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                pp.productId = proId;
                String chatText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT);
                pp.chatText = chatText;
                Boolean useChat = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_CHAT);
                pp.useChat = useChat;
                Boolean useComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT);
                pp.useComment = useComment;
                Boolean useSubComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT);
                pp.useSubComment = useSubComment;
                Boolean useGift = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_GIFT);
                pp.useGift = useGift;
                String voiceCallText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT);
                pp.voiceCallText = voiceCallText;
                Boolean useVoice = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL);
                pp.useVoiceCall = useVoice;
                String videoCallText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT);
                pp.videoCallText = videoCallText;
                String subCommentText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT);
                pp.subCommentText = subCommentText;
                String commentText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT);
                pp.commentText = commentText;
                String giftText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT);
                pp.giftText = giftText;
                Boolean useVideo = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL);
                pp.useVideoCall = useVideo;
                Boolean useUnlockBackstage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE);
                pp.useUnlockBackstage = useUnlockBackstage;
                String unlockBackstageText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT);
                pp.unlockBackstageText = unlockBackstageText;
                Boolean useSaveImage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE);
                pp.useSaveImage = useSaveImage;
                String saveImageText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT);
                pp.saveImageText = saveImageText;
                Boolean useOther = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_OTHER);
                pp.useOther = useOther;
                String otherText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT);
                pp.otherText = otherText;

                result.add(pp);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getMultiapp(int type, String applicationId) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.APPLICATION_ID, applicationId);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                ActionPointPacket pp = new ActionPointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                pp.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                pp.price = price;
                Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                pp.point = point;
                Integer firstPurchasePoint = (Integer) obj.get(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT);
                pp.firstPurchasePoint = firstPurchasePoint;
                String firstPurchaseDescription = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC);
                pp.firstPurchaseDescription = firstPurchaseDescription;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                pp.productId = proId;
                String chatText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT);
                pp.chatText = chatText;
                Boolean useChat = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_CHAT);
                pp.useChat = useChat;
                Boolean useComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT);
                pp.useComment = useComment;
                Boolean useSubComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT);
                pp.useSubComment = useSubComment;
                Boolean useGift = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_GIFT);
                pp.useGift = useGift;
                String voiceCallText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT);
                pp.voiceCallText = voiceCallText;
                Boolean useVoice = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL);
                pp.useVoiceCall = useVoice;
                String videoCallText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT);
                pp.videoCallText = videoCallText;
                String subCommentText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT);
                pp.subCommentText = subCommentText;
                String commentText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT);
                pp.commentText = commentText;
                String giftText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT);
                pp.giftText = giftText;
                Boolean useVideo = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL);
                pp.useVideoCall = useVideo;
                Boolean useUnlockBackstage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE);
                pp.useUnlockBackstage = useUnlockBackstage;
                String unlockBackstageText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT);
                pp.unlockBackstageText = unlockBackstageText;
                Boolean useSaveImage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE);
                pp.useSaveImage = useSaveImage;
                String saveImageText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT);
                pp.saveImageText = saveImageText;
                Boolean useOther = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_OTHER);
                pp.useOther = useOther;
                String otherText = (String) obj.get(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT);
                pp.otherText = otherText;

                result.add(pp);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String insert(ActionPointPacket actionPointPackage, int type) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.PRICE, actionPointPackage.price);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.POINT, actionPointPackage.point);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT, actionPointPackage.firstPurchasePoint);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC, actionPointPackage.firstPurchaseDescription);
//             insertObj.put(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION, actionPointPackage.description);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID, actionPointPackage.productId);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_CHAT, actionPointPackage.useChat);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL, actionPointPackage.useVideoCall);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL, actionPointPackage.useVoiceCall);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT, actionPointPackage.useComment);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT, actionPointPackage.useSubComment);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_GIFT, actionPointPackage.useGift);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE, actionPointPackage.useUnlockBackstage);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE, actionPointPackage.useSaveImage);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_OTHER, actionPointPackage.useOther);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT, actionPointPackage.chatText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT, actionPointPackage.videoCallText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT, actionPointPackage.voiceCallText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT, actionPointPackage.subCommentText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT, actionPointPackage.commentText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT, actionPointPackage.giftText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT, actionPointPackage.unlockBackstageText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT, actionPointPackage.saveImageText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT, actionPointPackage.otherText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            coll.insert(insertObj);
            result = insertObj.get(CashdbKey.ACTION_POINT_PACKET.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String insertMultiapp(String applicationId, ActionPointPacket actionPointPackage, int type) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.PRICE, actionPointPackage.price);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.POINT, actionPointPackage.point);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT, actionPointPackage.firstPurchasePoint);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC, actionPointPackage.firstPurchaseDescription);
//             insertObj.put(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION, actionPointPackage.description);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID, actionPointPackage.productId);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_CHAT, actionPointPackage.useChat);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL, actionPointPackage.useVideoCall);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL, actionPointPackage.useVoiceCall);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT, actionPointPackage.useComment);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT, actionPointPackage.useSubComment);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_GIFT, actionPointPackage.useGift);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE, actionPointPackage.useUnlockBackstage);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE, actionPointPackage.useSaveImage);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.USE_OTHER, actionPointPackage.useOther);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT, actionPointPackage.chatText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT, actionPointPackage.videoCallText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT, actionPointPackage.voiceCallText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT, actionPointPackage.subCommentText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT, actionPointPackage.commentText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT, actionPointPackage.giftText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT, actionPointPackage.unlockBackstageText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT, actionPointPackage.saveImageText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT, actionPointPackage.otherText);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            insertObj.put(CashdbKey.ACTION_POINT_PACKET.APPLICATION_ID, applicationId);
            coll.insert(insertObj);
            result = insertObj.get(CashdbKey.ACTION_POINT_PACKET.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getPointPacketByProductId(String productId) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID, productId);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                ActionPointPacket res = new ActionPointPacket();
                BasicDBObject obj = (BasicDBObject) cur.next();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                res.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                res.price = price;
                Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                res.point = point;
//                String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                res.description = des;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                res.productId = proId;
                result.add(res);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, ActionPointPacket actionPointPackage) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.ID, new ObjectId(id));
            DBObject updateObject = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.PRICE, actionPointPackage.price);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.POINT, actionPointPackage.point);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT, actionPointPackage.firstPurchasePoint);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC, actionPointPackage.firstPurchaseDescription);
//             updateObject.put(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION, actionPointPackage.description);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID, actionPointPackage.productId);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_CHAT, actionPointPackage.useChat);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL, actionPointPackage.useVideoCall);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL, actionPointPackage.useVoiceCall);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT, actionPointPackage.useSubComment);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT, actionPointPackage.useComment);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_GIFT, actionPointPackage.useGift);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE, actionPointPackage.useUnlockBackstage);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE, actionPointPackage.useSaveImage);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.USE_OTHER, actionPointPackage.useOther);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT, actionPointPackage.chatText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT, actionPointPackage.videoCallText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT, actionPointPackage.voiceCallText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT, actionPointPackage.subCommentText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT, actionPointPackage.commentText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT, actionPointPackage.giftText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT, actionPointPackage.unlockBackstageText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT, actionPointPackage.saveImageText);
            updateObject.put(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT, actionPointPackage.otherText);
            DBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delete(String id) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.ID, new ObjectId(id));
            DBObject updateObject = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
            DBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
