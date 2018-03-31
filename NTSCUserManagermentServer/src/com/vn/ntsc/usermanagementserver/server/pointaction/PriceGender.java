/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class PriceGender {
    
//    private static UserDAO userDAO = new UserDAO();
    
    public static int getPrice(String userId , ActionType type){
        int gender = UserInforManager.getGender(userId);
        Price price = ActionManager.get(type);
        if(price == null)
            return 0;
        else{
            int point = 0;
            if(gender == Constant.GENDER.MALE){
                point = price.malePrice;
            }else if(gender == Constant.GENDER.FEMALE){
                point = price.femalePrice ;
            }
            return point;
        }
    }
    
    public static int getUnlockBackStageBonus(String userId){
        int gender = UserInforManager.getGender(userId);
        Price price = ActionManager.get(ActionType.unlock_backstage);
        if(price == null)
            return 0;
        else{
            int rate = 0;
            if(gender == Constant.GENDER.MALE){
                rate = price.malePartnerPrice;
            }else if(gender == Constant.GENDER.FEMALE){
                rate = price.femalePartnerPrice;
            }
            return rate;
        }
    }
     
//    private static int getPriceByGender(int gender, int type) {
//        int price = 0;
//        if (gender == Constant.MALE) {
//            if (type == Constant.REGISTER) {
//                price = Constant.REGISTER_POINT_MALE;
//            } else if (type == Constant.DAILY_BONUS) {
//                price = Constant.DAILY_BONUS_POINT_MALE;
//            } else if (type == Constant.INVITE_FRIEND) {
//                price = Constant.INVITE_FRIEND_PRICE_MALE;
//            } else if (type == Constant.UNLOCK_BACKSTAGE) {
//                price = Constant.BACK_STAGE_RATE_PRICE_MALE;
//            } else if (type == Constant.ONLINE_ALERT) {
//                price = Constant.ONLINE_ALERT_PRICE_MALE;
//            } else if (type == Constant.UNLOCK_FAVORITED) {
//                price = Constant.UNLOCK_FAVORIST_PRICE_MALE;
//            } else if (type == Constant.UNLOCK_CHECK_OUT) {
//                price = Constant.UNLOCK_CHECK_OUT_PRICE_MALE;
//            } else if (type == Constant.WINK_BOMB) {
//                price = Constant.WINK_BOMB_PRICE_MALE;
//            } else if (type == Constant.SAVE_IMAGE) {
//                price = Constant.SAVE_IMAGE_PRICE_MALE;
//            } else if (type == Constant.CHAT) {
//                price = Constant.CHAT_POINT_MALE;
//            } else if (type == Constant.VOICE_CALL) {
//                price = Constant.VOICE_CALL_POINT_MALE;
//            } else if (type == Constant.VIDEO_CALL) {
//                price = Constant.VIDEO_CALL_POINT_MALE;
//            } else if (type == Constant.WINK){
//                price = Constant.WINK_PRICE_MALE;
//            }
//        } else {
//            if (type == Constant.REGISTER) {
//                price = Constant.REGISTER_POINT_FEMALE;
//            } else if (type == Constant.DAILY_BONUS) {
//                price = Constant.DAILY_BONUS_POINT_FEMALE;
//            } else if (type == Constant.INVITE_FRIEND) {
//                price = Constant.INVITE_FRIEND_PRICE_FEMALE;
//            } else if (type == Constant.UNLOCK_BACKSTAGE) {
//                price = Constant.BACK_STAGE_RATE_PRICE_FEMALE;
//            } else if (type == Constant.ONLINE_ALERT) {
//                price = Constant.ONLINE_ALERT_PRICE_FEMALE;
//            } else if (type == Constant.UNLOCK_FAVORITED) {
//                price = Constant.UNLOCK_FAVORIST_PRICE_FEMALE;
//            } else if (type == Constant.UNLOCK_CHECK_OUT) {
//                price = Constant.UNLOCK_CHECK_OUT_PRICE_FEMALE;
//            } else if (type == Constant.WINK_BOMB) {
//                price = Constant.WINK_BOMB_PRICE_FEMALE;
//            } else if (type == Constant.SAVE_IMAGE) {
//                price = Constant.SAVE_IMAGE_PRICE_FEMALE;
//            } else if (type == Constant.CHAT) {
//                price = Constant.CHAT_POINT_FEMALE;
//            } else if (type == Constant.VOICE_CALL) {
//                price = Constant.VOICE_CALL_POINT_FEMALE;
//            } else if (type == Constant.VIDEO_CALL) {
//                price = Constant.VIDEO_CALL_POINT_FEMALE;
//            }  else if (type == Constant.WINK){
//                price = Constant.WINK_PRICE_FEMALE;
//            }
//        }
//        return price;
//    }
//
//    private static int getGender(String id) throws DaoException{
////        return userDAO.getGender(id);
//        return UserPointManager.getGender(id);
//    }
//    
//    public static int getRegisterPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.REGISTER);
//    }
//    
//    public static int getRegisterPoint(int gender) throws DaoException{
//        return getPriceByGender(gender, Constant.REGISTER);
//    }    
//    
//    public static int getDailyBonusPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.DAILY_BONUS);
//    }
//    
//    public static int getInviteFriendPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.INVITE_FRIEND);
//    }
//    
//    public static int getUnlockBackstagePoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.UNLOCK_BACKSTAGE);
//    }
// 
//    public static int getOnlineAlertPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.ONLINE_ALERT);
//    }
//
//    public static int getUnlockFavouritedPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.UNLOCK_FAVORITED);
//    }
//    
//    public static int getUnlockCheckOutPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.UNLOCK_CHECK_OUT);
//    } 
//    
//    public static int getWinkBombPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.WINK_BOMB);
//    } 
//    
//    public static int getSaveImagePoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.SAVE_IMAGE);
//    }
//    
//    public static int getChatPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.CHAT);
//    }
//    
//    public static int getVoiceCallPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.VOICE_CALL);
//    }
//    
//    public static int getVideoCallPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.VIDEO_CALL);
//    }
//    
//    public static int getWinkPoint(String id) throws DaoException{
//        int gender = getGender(id);
//        return getPriceByGender(gender, Constant.WINK);
//    }    
}
