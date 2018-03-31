/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class ActionManager {

    private static final Map<ActionType, Price> m = new TreeMap<>();
    public static final JSONObject communicationPointSetting = new JSONObject();
    public static final JSONObject connectionPointSetting = new JSONObject();

    public static void put(ActionType type, Price price) {
        m.put(type, price);
    }

    public static Price get(ActionType type) {
        return m.get(type);
    }

    public static void update(JSONObject json) {
        Iterator iter = json.keySet().iterator();
        while (iter.hasNext()) {
            String type = (String) iter.next();
            Object data = json.get(type);
            if (data instanceof JSONObject) {
                JSONObject value = (JSONObject) data;
                Long malePoint = (Long) value.get(SettingdbKey.POINT_SETTING.MALE_REQUEST_POINT);
                Long potentialCustomerMalePrice = (Long) value.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT);
                Long femalePoint = (Long) value.get(SettingdbKey.POINT_SETTING.FEMALE_REQUEST_POINT);
                Long potentialCustomerFemalePrice = (Long) value.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT);
                Long malePartnerTradablePoint = (Long) value.get(SettingdbKey.POINT_SETTING.MALE_PARTNER_POINT);
//                Long potentialCustomerMalePartnerPrice = (Long) value.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_MALE_PARTNER_POINT);
                Long femalePartnerTradablePoint = (Long) value.get(SettingdbKey.POINT_SETTING.FEMALE_PARTNER_POINT);
//                Long potentialCustomerFemalePartnerPrice = (Long) value.get(SettingdbKey.POINT_SETTING.POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT);

                Price price = new Price(malePoint, femalePoint, malePartnerTradablePoint, femalePartnerTradablePoint,
                        potentialCustomerMalePrice, potentialCustomerFemalePrice);
                put(ActionType.valueOf(type), price);
            }
        }
    }

    public static ActionResult doAction(ActionType type, String userId, String partnerId, Date date, Integer point, Integer number, String ip) {
        int curPoint = UserInforManager.getPoint(userId);
        ActionResult result = new ActionResult(ErrorCode.NOT_ENOUGHT_POINT, curPoint);
        ActionResult respond = null;
        switch (type) {
            case register:
                respond = doBonusPoint(userId, type, date, ip);
                return respond == null ? result : respond;
            case daily_bonus:
                respond = doBonusPoint(userId, type, date, ip);
                return respond == null ? result : respond;
            case advertsement:
                respond = doBonusPoint(userId, type, date, ip);
                return respond == null ? result : respond;
            case cash:
                respond = doAddPoint(userId, type, date, ip, point);
                return respond == null ? result : respond;
            case admistrator:
                respond = doAddPoint(userId, type, date, ip, point);
                return respond == null ? result : respond;
            case online_alert:
                respond = doDecreasePoint(userId, partnerId, type, date, ip);
                return respond == null ? result : respond;
            case buy_sticker:
                respond = doDecreasePoint(userId, type, date, ip, point);
                return respond == null ? result : respond;
            case unlock_backstage:
                respond = doExchangePoint(userId, ActionType.unlock_backstage, partnerId, ActionType.unlock_backstage_bonus, date, ip);
                return respond == null ? result : respond;
            case view_image:
                respond = doExchangePoint(userId, ActionType.view_image, partnerId, ActionType.view_image_bonus, date, ip);
                return respond == null ? result : respond;
            case watch_video:
                respond = doExchangePoint(userId, ActionType.watch_video, partnerId, ActionType.watch_video_bonus, date, ip);
                return respond == null ? result : respond;
            case listen_audio:
                respond = doExchangePoint(userId, ActionType.listen_audio, partnerId, ActionType.listen_audio_bonus, date, ip);
                return respond == null ? result : respond;
            case save_image:
                respond = doExchangePoint(userId, ActionType.save_image, partnerId, ActionType.save_image_bonus, date, ip);
                return respond == null ? result : respond;
            case wink:
                respond = doExchangePoint(userId, ActionType.wink, partnerId, ActionType.receive_wink, date, ip);
                return respond == null ? result : respond;
            case chat:
                respond = doExchangePoint(userId, ActionType.chat, partnerId, ActionType.receive_chat, date, ip);
                return respond == null ? result : respond;
            case send_gift:
                respond = sendGift(userId, partnerId, date, ip, point);
                return respond == null ? result : respond;
            case video_call:
                respond = doCall(userId, type, partnerId, date, ip, point);
                return respond == null ? result : respond;
            case voice_call:
                respond = doCall(userId, type, partnerId, date, ip, point);
                return respond == null ? result : respond;
            case trade_point_to_money:
                respond = doTradeToMoney(userId, ip, date, point);
                return respond == null ? result : respond;
            case comment_buzz:
                respond = doExchangePoint(userId, ActionType.comment_buzz, partnerId, ActionType.comment_bonus, date, ip);
                return respond == null ? result : respond;
            case reply_comment:
                respond = doExchangePoint(userId, ActionType.reply_comment, partnerId, ActionType.reply_comment_bonus, date, ip);
                return respond == null ? result : respond;
            case free_point:
                respond = doAddPoint(userId, type, date, ip, point);
                return respond == null ? result : respond;
            default:
                return null;
        }
    }

    public static ActionResult doBonusPoint(String userId, ActionType type, Date date, String ip) {
        int gender = UserInforManager.getGender(userId);
        int point = 0;
        ActionResult result = null;
        Price price = m.get(type);
        if (price != null) {
            if (gender == Constant.GENDER.MALE) {
                point = price.malePrice;
            } else if (gender == Constant.GENDER.FEMALE) {
                point = price.femalePrice;
            }
            if (point > 0) {
                PointAction pointAction = UserInforManager.increasePoint(userId, point);
                int logType = LogValue.getValue(type);
                addLogPoint(userId, logType, null, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            } else if (point == 0) {
                int curPoint = UserInforManager.getPoint(userId);
                result = new ActionResult(ErrorCode.SUCCESS, curPoint);
            }
        } else {
            int curPoint = UserInforManager.getPoint(userId);
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static ActionResult doAddPoint(String userId, ActionType type, Date date, String ip, int point) {
        ActionResult result = null;
        if (point != 0) {
            PointAction pointAction = UserInforManager.increasePoint(userId, point);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, null, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        }
        return result;
    }
    
    public static ActionResult doRefund(String userId, String partnerId, ActionType type, Date date, String ip) {
        ActionResult result = null;
        int curPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = null;
        if (type == ActionType.refund_comment){
            price = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.comment_buzz), userId, partnerId);
        }
        else if (type == ActionType.refund_reply_comment){
            price = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.reply_comment), userId, partnerId);
        }
        int senderPrice = price.senderPrice;
        
        if (senderPrice > 0) {
            PointAction pointAction = UserInforManager.increasePoint(userId, senderPrice);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, partnerId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        } else if (senderPrice < 0 ) {
            PointAction pointAction = UserInforManager.decreasePoint(userId, senderPrice);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, partnerId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        }
        return result;
    }
    
    public static ActionResult doDecreasePointVer2(String userId, String partnerId, ActionType type, Date date, String ip) {
        ActionResult result = null;
        int curPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(type), userId, partnerId);
        int senderPrice = price.senderPrice;
        
        if (senderPrice > 0 && senderPrice <= curPoint) {
            PointAction pointAction = UserInforManager.decreasePoint(userId, senderPrice);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, partnerId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        } else if (senderPrice < 0 && (curPoint+senderPrice) > 0) {
            PointAction pointAction = UserInforManager.increasePoint(userId, senderPrice);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, partnerId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        }
        return result;
    }
    
    public static ActionResult doIncreasePointVer2(String userId, String partnerId, ActionType ownertype, ActionType partnertype, Date date, String ip) {
        ActionResult result = null;
        int curPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ownertype), userId, partnerId);
        
        int receiverPrice = price.receiverPrice;
        
        if (receiverPrice > 0) {
            PointAction pointAction = UserInforManager.increasePoint(partnerId, receiverPrice);
            int logType = LogValue.getValue(partnertype);
            addLogPoint(partnerId, logType, userId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        } else if (receiverPrice < 0) {
            PointAction pointAction = UserInforManager.decreasePoint(partnerId, receiverPrice);
            int logType = LogValue.getValue(partnertype);
            addLogPoint(partnerId, logType, userId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        }
        return result;
    }

    public static ActionResult doDecreasePoint(String userId, String partnerId, ActionType type, Date date, String ip) {
        int gender = UserInforManager.getGender(userId);
        int curPoint = UserInforManager.getPoint(userId);
        int point = 0;
        ActionResult result = null;
        Price price = m.get(type);
        if (price != null) {
            if (gender == Constant.GENDER.MALE) {
                point = price.malePrice;
            } else if (gender == Constant.GENDER.FEMALE) {
                point = price.femalePrice;
            }
            if (point >= 0 && point <= curPoint) {
                PointAction pointAction = UserInforManager.decreasePoint(userId, point);
                int logType = LogValue.getValue(type);
                addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            }
//            else if (point == 0) {
//                result = new ActionResult(ErrorCode.SUCCESS, curPoint);
//            }
        } else {
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static ActionResult doDecreasePointReplyComment(String userId, ActionType ownerType, String partnerId, ActionType partnerType, Date date, String ip) {
        ActionResult result = null;
        int curSenderPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ownerType), userId, partnerId);
        if (price != null) {
            int senderPrice = price.senderPrice;
            if (senderPrice >= 0 || (senderPrice < 0 && 0 - senderPrice <= curSenderPoint)) {
                if (senderPrice > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(userId, senderPrice);
                    int logType = LogValue.getValue(ownerType);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                } else if (senderPrice <= 0) {
                    PointAction pointAction = UserInforManager.decreasePoint(userId, 0 - senderPrice);
                    int logType = LogValue.getValue(ownerType);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                }
            }
        } else {
            result = new ActionResult(ErrorCode.SUCCESS, curSenderPoint);
        }
        return result;
    }

    public static ActionResult doExchangePoint_old(String userId, ActionType ownerType, String partnerId, ActionType partnerType, Date date, String ip) {
        int gender = UserInforManager.getGender(userId);
        boolean havePurchase = UserInforManager.havePurchased(userId);
        int curPoint = UserInforManager.getPoint(userId);
        int point = 0;
        ActionResult result = null;
        Price price = m.get(ownerType);
        if (price != null) {
            if (gender == Constant.GENDER.MALE) {
                point = price.malePrice;
            } else if (gender == Constant.GENDER.FEMALE) {
                point = price.femalePrice;
            }
//            if(havePurchase){
//                if (gender == Constant.MALE) {
//                    point = price.malePrice;
//                } else if (gender == Constant.FEMALE) {
//                    point = price.femalePrice;
//                }
//            }else{
//                if (gender == Constant.MALE) {
//                    point = price.potentialCustomerMalePrice;
//                } else if (gender == Constant.FEMALE) {
//                    point = price.potentialCustomerFemalePrice;
//                }
//            }
            if (point >= 0 && point <= curPoint) {
                if (point >= 0) {
                    PointAction pointAction = UserInforManager.decreasePoint(userId, point);
                    int logType = LogValue.getValue(ownerType);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                }
//                else if (point == 0) {
//                    result = new ActionResult(ErrorCode.SUCCESS, curPoint);
//                }
                gender = UserInforManager.getGender(partnerId);
                havePurchase = UserInforManager.havePurchased(partnerId);
                point = 0;
                if (gender == Constant.GENDER.MALE) {
                    point = price.malePartnerPrice;
                } else if (gender == Constant.GENDER.FEMALE) {
                    point = price.femalePartnerPrice;
                }
//                if(havePurchase){
//                    if (gender == Constant.MALE) {
//                        point = price.malePartnerPrice;
//                    } else if (gender == Constant.FEMALE) {
//                        point = price.femalePartnerPrice;
//                    }
//                }else{
//                    if (gender == Constant.MALE) {
//                        point = price.potentialCustomerMalePartnerPrice;
//                    } else if (gender == Constant.FEMALE) {
//                        point = price.potentialCustomerFemalePartnerPrice;
//                    }
//                }
                if (point > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(partnerId, point);
                    int logType = LogValue.getValue(partnerType);
                    addLogPoint(partnerId, logType, userId, date, ip, pointAction);
                }
            }
        } else {
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static ActionResult doExchangePoint(String userId, ActionType ownerType, String partnerId, ActionType partnerType, Date date, String ip) {
        ActionResult result = null;
        int curSenderPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ownerType), userId, partnerId);
        Util.addDebugLog("================================== price: " + price.toString());
        if (price != null) {
            int senderPrice = price.senderPrice;
            if (senderPrice >= 0 || (senderPrice < 0 && 0 - senderPrice <= curSenderPoint)) {
                if (senderPrice > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(userId, senderPrice);
                    int logType = LogValue.getValue(ownerType);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                } else if (senderPrice <= 0) {
                    PointAction pointAction = UserInforManager.decreasePoint(userId, 0 - senderPrice);
                    int logType = LogValue.getValue(ownerType);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                }
//                int curReceiverPoint = UserInforManager.getPoint(partnerId);
                int receiverPrice = price.receiverPrice;
                if (receiverPrice > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(partnerId, receiverPrice);
                    int logType = LogValue.getValue(partnerType);
                    addLogPoint(partnerId, logType, userId, date, ip, pointAction);
                } else if (receiverPrice < 0) {
                    PointAction pointAction = UserInforManager.decreasePoint(partnerId, 0 - receiverPrice);
                    int logType = LogValue.getValue(partnerType);
                    addLogPoint(partnerId, logType, userId, date, ip, pointAction);
                }
            }
        } else {
            result = new ActionResult(ErrorCode.SUCCESS, curSenderPoint);
        }
        return result;
    }

    public static ActionResult doPayBack(String userId, ActionType ownerType, String partnerId, ActionType partnerType, Date date, String ip) {
        ActionResult result = null;
        int curSenderPoint = UserInforManager.getPoint(userId);
        if (ownerType == ActionType.payback){
            ownerType = ActionType.chat;
        }
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ownerType), userId, partnerId);
        if (price != null) {
            
            int senderPrice = price.senderPrice;
            PointAction pointAction1 = UserInforManager.increasePoint(userId, 0 - senderPrice);
            int logType1 = LogValue.getValue(ownerType);
            addLogPoint(userId, logType1, partnerId, date, ip, pointAction1);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction1.afterPoint);

            int receiverPrice = price.receiverPrice;
            PointAction pointAction2 = UserInforManager.decreasePoint(partnerId, receiverPrice);
            int logType2 = LogValue.getValue(partnerType);
            addLogPoint(partnerId, logType2, userId, date, ip, pointAction2);

        } else {
            result = new ActionResult(ErrorCode.SUCCESS, curSenderPoint);
        }
        return result;
    }

    public static ActionResult doExchangeOldVersion(String userId, ActionType ownerType, String partnerId, ActionType partnerType, Date date, String ip) {
        ActionResult result = null;
        int curSenderPoint = UserInforManager.getPoint(userId);
        ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ownerType), userId, partnerId);
        int senderPrice = ConnectionPrice.getBadConnectionPrice(String.valueOf(ownerType), userId).senderPrice;
        if (senderPrice >= 0 || (senderPrice < 0 && 0 - senderPrice <= curSenderPoint)) {
            if (senderPrice > 0) {
                PointAction pointAction = UserInforManager.increasePoint(userId, senderPrice);
                int logType = LogValue.getValue(ownerType);
                addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            } else if (senderPrice <= 0) {
                PointAction pointAction = UserInforManager.decreasePoint(userId, 0 - senderPrice);
                int logType = LogValue.getValue(ownerType);
                addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            }
//                int curReceiverPoint = UserInforManager.getPoint(partnerId);
            int receiverPrice = price.receiverPrice;
            if (receiverPrice > 0) {
                PointAction pointAction = UserInforManager.increasePoint(partnerId, receiverPrice);
                int logType = LogValue.getValue(partnerType);
                addLogPoint(partnerId, logType, userId, date, ip, pointAction);
            } else if (receiverPrice < 0) {
                PointAction pointAction = UserInforManager.decreasePoint(partnerId, 0 - receiverPrice);
                int logType = LogValue.getValue(partnerType);
                addLogPoint(partnerId, logType, userId, date, ip, pointAction);
            }
        }
        return result;
    }

    public static ActionResult doDecreasePoint(String userId, ActionType type, Date date, String ip, int point) {
        ActionResult result = null;
        int curPoint = UserInforManager.getPoint(userId);
        if (point > 0 && point <= curPoint) {
            PointAction pointAction = UserInforManager.decreasePoint(userId, point);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, null, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        } else if (point == 0) {
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static ActionResult doCall(String userId, ActionType type, String partnerId, Date date, String ip, int point) {
        ActionResult result = null;
        if (point > 0) {
            PointAction pointAction = UserInforManager.increasePoint(userId, point);
            int logType = LogValue.getValue(type);
            addLogPoint(userId, logType, partnerId, date, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
        } else if (point <= 0) {
            int curPoint = UserInforManager.getPoint(userId);
            if (curPoint >= (0 - point)) {
                PointAction pointAction = UserInforManager.decreasePoint(userId, 0 - point);
                int logType = LogValue.getValue(type);
                addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            }
        } else {
            int curPoint = UserInforManager.getPoint(userId);
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static ActionResult unlockBackstage_old_version(String userId, String partnerId, Date date, String ip, int point) {
        ActionResult result = null;
        int curPoint = UserInforManager.getPoint(userId);
        if (point >= 0 && point <= curPoint) {
            if (point > 0) {
                PointAction pointAction = UserInforManager.decreasePoint(userId, point);
                int logType = LogValue.getValue(ActionType.unlock_backstage);
                addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
            } else if (point == 0) {
                result = new ActionResult(ErrorCode.SUCCESS, curPoint);
            }

            Price price = m.get(ActionType.unlock_backstage_bonus);
            if (price != null) {
                int gender = UserInforManager.getGender(partnerId);
                int pointUp = 0;
                if (gender == Constant.GENDER.MALE) {
                    pointUp = (int) (((double) price.malePrice / 100) * point);
                } else if (gender == Constant.GENDER.FEMALE) {
                    pointUp = (int) (((double) price.femalePrice / 100) * point);
                }
                if (pointUp > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(partnerId, pointUp);
                    int logType = LogValue.getValue(ActionType.unlock_backstage_bonus);
                    addLogPoint(partnerId, logType, userId, date, ip, pointAction);
                }
            }
        }
        return result;
    }

    public static ActionResult sendGift(String userId, String partnerId, Date date, String ip, int point) {
        ActionResult result = null;
        try {
            int curPoint = UserInforManager.getPoint(userId);
            if (point >= 0 && point <= curPoint) {
                if (point >= 0) {
                    PointAction pointAction = UserInforManager.decreasePoint(userId, point);
                    int logType = LogValue.getValue(ActionType.send_gift);
                    addLogPoint(userId, logType, partnerId, date, ip, pointAction);
                    result = new ActionResult(ErrorCode.SUCCESS, pointAction.afterPoint);
                }
//                else if (point == 0) {
//                    result = new ActionResult(ErrorCode.SUCCESS, curPoint);
//                }
                boolean havePurchase = UserInforManager.havePurchased(partnerId);
                Price price = m.get(ActionType.receive_gift);
                int malePrice = 30;
                int femalePrice = 30;
                if (price != null) {
                    malePrice = price.malePrice;
                    femalePrice = price.femalePrice;
                    if (havePurchase) {
                        malePrice = price.malePrice;
                        femalePrice = price.femalePrice;
                    } else {
                        malePrice = price.potentialCustomerMalePrice;
                        femalePrice = price.potentialCustomerFemalePrice;
                    }
                }

                int gender = UserInforManager.getGender(partnerId);
                int upPoint = 0;
                if (gender == Constant.GENDER.MALE) {
                    upPoint = (int) (((double) malePrice / 100) * point);
                } else if (gender == Constant.GENDER.FEMALE) {
                    upPoint = (int) (((double) femalePrice / 100) * point);
                }
                if (upPoint > 0) {
                    PointAction pointAction = UserInforManager.increasePoint(partnerId, upPoint);
                    int logType = LogValue.getValue(ActionType.receive_gift);
                    addLogPoint(partnerId, logType, userId, date, ip, pointAction);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void doAddPointFromFreePage(String id, int point, Long saleType, Long freePointType, String ip, Date time) {
        if (point > 0) {
            ActionType actionType = ActionType.add_sale;
            if (freePointType != null) {
                actionType = ActionType.free_point;
            }
            PointAction pointAction = UserInforManager.increasePoint(id, point);
            int logType = LogValue.getValue(actionType);
            addLogPoint_freePage(id, logType, saleType, freePointType, time, ip, pointAction);
        }
    }

    public static void returnPoint(String userId, int pointDiffer, String ip) {
        PointAction pointAction = UserInforManager.increasePoint(userId, pointDiffer);
        int logType = LogValue.getValue(ActionType.return_failed_upload_point);
        addLogPoint(userId, logType, null, Util.getGMTTime(), ip, pointAction);
    }

    public static void addLogPoint(String userId, int logType, String partnerId, Date time, String ip, PointAction point) {
        LogPoint log = new LogPoint(userId, logType, partnerId, time, ip, point);
        LogPointContainer.add(log);
    }

    public static void addLogPoint_freePage(String userId, int logType, Long saleType, Long freePointType, Date time, String ip, PointAction point) {
        LogPoint log = new LogPoint(userId, logType, saleType, freePointType, time, ip, point);
        LogPointContainer.add(log);
    }

    public static ActionResult doTradeToMoney(String id, String ip, Date time, int point) {
        int curPoint = UserInforManager.getPoint(id);
        ActionResult result = new ActionResult(ErrorCode.NOT_ENOUGHT_POINT, curPoint);
        if (point <= curPoint) {
            PointAction pointAction = UserInforManager.decreasePoint(id, point);
            int logType = LogValue.getValue(ActionType.trade_point_to_money);
            addLogPoint(id, logType, null, time, ip, pointAction);
            result = new ActionResult(ErrorCode.SUCCESS, curPoint);
        }
        return result;
    }

    public static boolean checkInformationList(int bonusFlag, List<Long> list, int flag) {
        return (bonusFlag & flag) != flag && list != null && !list.isEmpty() && list.get(0) != -1;
    }

    public static boolean checkInformation(int bonusFlag, Object obj, int flag) {
        if (obj instanceof Double) {
            Double d = (Double) obj;
            return (bonusFlag & flag) != flag && d != null && d != -1;
        }
        if (obj instanceof Integer) {
            Integer d = (Integer) obj;
            return (bonusFlag & flag) != flag && d != null && d != -1;
        }
        if (obj instanceof Long) {
            Long d = (Long) obj;
            return (bonusFlag & flag) != flag && d != null && d != -1;
        }
        if (obj instanceof String) {
            String str = (String) obj;
//            if(flag == Constant.ABOUT_FLAG)
            return (bonusFlag & flag) != flag && str != null && str.length() >= 100;
//            else
//                return (bonusFlag & flag) != flag && str != null;
        }
        return false;
    }

    public static int doBonus(int bonusFlag, int flag, ActionType type, String userId, Date time, String ip) {
        doAction(type, userId, null, time, null, null, ip);
        return bonusFlag | flag;
    }

}
