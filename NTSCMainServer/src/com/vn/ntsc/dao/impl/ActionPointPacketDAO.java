/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

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
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.ActionPointPacket;

/**
 *
 * @author RuAc0n
 */
public class ActionPointPacketDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getCashDB().getCollection(CashdbKey.ACTION_POINT_PACKET_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<ActionPointPacket> getPoinPacketByType(String applicationId, int actionType, int type, Boolean isPurchase) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        String use_type;
        String type_text;
        switch (actionType) {
            case 1: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_CHAT;
                type_text = CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT;
                break;
            }
            case 2: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL;
                type_text = CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT;
                break;
            }
            case 3: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL;
                type_text = CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT;
                break;
            }
            case 4: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_GIFT;
                type_text = CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT;
                break;
            }
            case 5: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_COMMENT;
                type_text = CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT;
                break;
            }
            case 6: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT;
                type_text = CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT;
                break;
            }
            case 7: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE;
                type_text = CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT;
                break;
            }
            case 8: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE;
                type_text = CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT;
                break;
            }
            case 9: {
                use_type = CashdbKey.ACTION_POINT_PACKET.USE_OTHER;
                type_text = CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT;
                break;
            }
            default:
                return result;
        }

        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.APPLICATION_ID, applicationId);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useChat = (Boolean) obj.get(use_type);
                if (useChat != null && useChat) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point;
                    String text;
                    if (isPurchase) {
                        point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                        text = obj.getString(type_text);
                    } else {
                        point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT);
                        text = obj.getString(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_DESC);
                    }
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    pp.text = text;
                    pp.description = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getChatPointPacket(int type, String applicationId) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.APPLICATION_ID, applicationId);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useChat = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_CHAT);
                if (useChat != null && useChat) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.CHAT_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getSubCommentPointPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useSubComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SUB_COMMENT);
                if (useSubComment != null && useSubComment) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.SUB_COMMENT_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getCommentPointPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useComment = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_COMMENT);
                if (useComment != null && useComment) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.COMMENT_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getGiftPointPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useGift = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_GIFT);
                if (useGift != null && useGift) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.GIFT_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getVideoCallPointPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useVideoCall = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VIDEO_CALL);
                if (useVideoCall != null && useVideoCall) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.VIDEO_CALL_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getVoiceCallPointPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useVoiceCall = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_VOICE_CALL);
                if (useVoiceCall != null && useVoiceCall) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.VOICE_CALL_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getUnlockBackstagePacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useUnlockBackstage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_UNLOCK_BACKSTAGE);
                if (useUnlockBackstage != null && useUnlockBackstage) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.UNLOCK_BACKSTAGE_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getSaveImagePacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean useSaveImage = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_SAVE_IMAGE);
                if (useSaveImage != null && useSaveImage) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.SAVE_IMAGE_TEXT);
                    pp.text = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getOtherPacket(int type) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean use = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_OTHER);
                if (use != null && use) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT);
                    pp.text = text;
                    pp.description = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<ActionPointPacket> getOtherPacketMultiapp(int type, String applicationId) throws EazyException {
        List<ActionPointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.APPLICATION_ID, applicationId);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean use = (Boolean) obj.get(CashdbKey.ACTION_POINT_PACKET.USE_OTHER);
                if (use != null && use) {
                    ActionPointPacket pp = new ActionPointPacket();
                    ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                    pp.id = id.toString();
                    Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                    pp.price = price;
                    Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                    pp.point = point;
//                    String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                    pp.description = des;
                    String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                    pp.productId = proId;
                    String text = obj.getString(CashdbKey.ACTION_POINT_PACKET.OTHER_TEXT);
                    pp.text = text;
                    pp.description = text;
                    result.add(pp);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static ActionPointPacket getPointPacketByProductId(String productId) throws EazyException {
        ActionPointPacket result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID, productId);
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                result = new ActionPointPacket();
                BasicDBObject obj = (BasicDBObject) cur.next();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                result.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                result.price = price;
                Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                result.point = point;
//                String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                result.description = des;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                result.productId = proId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getPoint(String packetId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(packetId);
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.ID, id);
            DBObject obj = coll.findOne(findObj);

            if (obj != null) {
                Integer point = (Integer) obj.get(CashdbKey.ACTION_POINT_PACKET.POINT);
                if (point != null) {
                    result = point;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static ActionPointPacket getPointPacket(String packageId) throws EazyException {
        ActionPointPacket result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.ID, new ObjectId(packageId));
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            BasicDBObject obj = (BasicDBObject) coll.findOne(findObj);
            if (obj != null) {
                result = new ActionPointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                result.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                result.price = price;
                Integer point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                result.point = point;
//                String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                result.description = des;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                result.productId = proId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static ActionPointPacket getPointPacketVer2(String packageId, Boolean isPurchase) throws EazyException {
        ActionPointPacket result = null;
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.ACTION_POINT_PACKET.ID, new ObjectId(packageId));
            findObj.append(CashdbKey.ACTION_POINT_PACKET.FLAG, Constant.FLAG.ON);
            BasicDBObject obj = (BasicDBObject) coll.findOne(findObj);
            if (obj != null) {
                result = new ActionPointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.ACTION_POINT_PACKET.ID);
                result.id = id.toString();
                Double price = obj.getDouble(CashdbKey.ACTION_POINT_PACKET.PRICE);
                result.price = price;
                Integer point;
                if (isPurchase) {
                    point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.POINT);
                } else {
                    point = obj.getInt(CashdbKey.ACTION_POINT_PACKET.FIRST_PURCHASE_POINT);
                }
                result.point = point;
//                String des = obj.getString(CashdbKey.ACTION_POINT_PACKET.DESCRIPTION);
//                result.description = des;
                String proId = obj.getString(CashdbKey.ACTION_POINT_PACKET.PRODUCTION_ID);
                result.productId = proId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
