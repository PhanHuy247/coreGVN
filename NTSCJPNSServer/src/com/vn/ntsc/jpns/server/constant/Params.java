/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.constant;

import java.util.TreeMap;
import eazycommon.constant.API;

/**
 *
 * @author tuannxv00804
 */
public class Params {
   
    private static final TreeMap<String, Integer> NotiTypes = new TreeMap<>();
    static{
        NotiTypes.put( API.NOTI_CHECK_PROFILE, 2 );        
        NotiTypes.put( API.NOTI_FAVORITED, 4 );
        NotiTypes.put( API.NOTI_LIKE_YOUR_BUZZ, 5 );
//        NotiTypes.put( API.noti_also_like_buzz, 6);
        NotiTypes.put( API.NOTI_COMMENT_YOUR_BUZZ, 7 );
//        NotiTypes.put( API.noti_also_responsed_buzz, 8 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_TEXT, 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_WINK , 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_PHOTO, 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_VIDEO, 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_AUDIO, 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_STICKER, 11 );
        NotiTypes.put( API.NOTI_NEW_CHAT_MSG_LOCATION, 11 );
        NotiTypes.put( API.NOTI_GAVE_GIFT, 11 );
        NotiTypes.put( API.NOTI_ONLINE_ALERT, 12 );
        NotiTypes.put( API.NOTI_DAILY_BONUS, 13 ); 
        NotiTypes.put(API.NOTI_BUZZ_APPROVED, 15);
        NotiTypes.put(API.NOTI_BACKSTAGE_APPROVE, 16);
        NotiTypes.put(API.NOTI_PING, 17);
        NotiTypes.put(API.AUTO_PUSH, 18);
        NotiTypes.put(API.PUSH_NOTIFICATION_FROM_FREE_PAGE, 18);
        NotiTypes.put(API.NOTI_NEW_BUZZ_FROM_FAVORIST, 19);
        NotiTypes.put(API.NOTI_REPLY_YOUR_COMMENT, 20);
        NotiTypes.put(API.NOTI_BUZZ_DENIED, 21);
        NotiTypes.put(API.NOTI_BACKSTAGE_DENIED, 22);
        NotiTypes.put(API.NOTI_CALL_REQUEST, 23);
        NotiTypes.put(API.NOTI_TEXT_BUZZ_APPROVED, 24);
        NotiTypes.put(API.NOTI_TEXT_BUZZ_DENIED, 25);
        NotiTypes.put(API.NOTI_COMMENT_APPROVED, 26);
        NotiTypes.put(API.NOTI_COMMENT_DENIED, 27);
        NotiTypes.put(API.NOTI_SUB_COMMENT_APPROVED, 28);
        NotiTypes.put(API.NOTI_SUB_COMMENT_DENIED, 29);
        NotiTypes.put("approve_user_infor_noti", 30);
        NotiTypes.put("denied_apart_of_user_info", 31);
        NotiTypes.put("denied_user_infor_noti", 32);
        NotiTypes.put(API.PUSH_NOTIFICATION, 33);
        NotiTypes.put(API.NOTI_MISS_CALL, 35);
        NotiTypes.put(API.NOTI_INVITE_FRIEND, 36);
        NotiTypes.put(API.NOTI_HAS_TAG_IN_BUZZ,37);
        NotiTypes.put(API.NOTI_SHARE_MUSIC,38);
        NotiTypes.put(API.NOTI_RECORDING_FILE,39);
        NotiTypes.put(API.NOTI_LIVESTREAM_FROM_FAVOURIST,40);
        NotiTypes.put(API.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST,41);
    }
    
    public static Integer getNotiType( String locKey ){
        Integer nt = NotiTypes.get( locKey );
        if( nt != null){
            return nt;
        }
        return 0;
    }
}
