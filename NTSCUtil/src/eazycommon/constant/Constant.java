/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author DuongLTD
 */
public class Constant {

    private Constant() {
    }

    public static double R_EARTH = 6378.16;

    public class APPLICATION_TYPE {

        public static final int IOS_PRODUCTION_APPLICATION = 1;
        public static final int IOS_ENTERPRISE_APPLICATION = 2;
        public static final int ANDROID_APPLICATION = 3;
        public static final int WEB_APPLICATION = 4;
    }

    public class IMAGE_KIND_STRING {

        public static final String CHAT_IMAGE = "0";
        public static final String PUBLIC_IMAGE = "1";
        public static final String BACKSTAGE = "2";
        public static final String AVATAR = "3";
        public static final String THUMBNAIL_VIDEO = "4";
    }
    public class VIDEO_KIND_STRING {

        public static final String CHAT_VIDEO = "0";
        public static final String PUBLIC_VIDEO = "1";
        public static final String BACKSTAGE = "2";
        public static final String AVATAR = "3";
        public static final String THUMBNAIL_VIDEO = "4";
    }
    public class AUDIO_KIND_STRING {

        public static final String CHAT_AUDIO = "0";
        public static final String PUBLIC_AUDIO = "1";
        public static final String BACKSTAGE = "2";
        public static final String AVATAR = "3";
        public static final String THUMBNAIL_AUDIO = "4";
    }
    public class IMAGE_TYPE_VALUE {

        public static final int IMAGE_CHAT = 0;
        public static final int IMAGE_PUBLIC = 1;
        public static final int IMAGE_BACKSTAGE = 2;
    }
    public class VIDEO_TYPE_VALUE {

        public static final int VIDEO_CHAT = 0;
        public static final int VIDEO_PUBLIC = 1;
        public static final int VIDEO_BACKSTAGE = 2;
    }
    public class AUDIO_TYPE_VALUE {

        public static final int AUDIO_CHAT = 0;
        public static final int AUDIO_PUBLIC = 1;
        public static final int AUDIO_BACKSTAGE = 2;
    }

//    public class UNLOCK_TYPE_VALUE{
//        public static final int FAVOURITED_UNLOCK = 1;
//        public static final int CHECK_OUT_UNLOCK = 2;
//        public static final int BACKSTAGE_UNLOCK = 3;
//    }
    public class EMAIL_TYPE_VALUE {

        public static final int FORGOT_PASS_EMAIL = 0;
        public static final int ONLINE_ALERT_EMAIL = 1;
    }
    
    public class VIDEO_STATUS {
        public static final int PENDING = 0;
        public static final int APPROVE = 1;
        public static final int DENY = -1;
        public static final int SELECT_ALL = 2;
    }

    public class FLAG {

        public static final int ON = 1;
        public static final int OFF = 0;
    }

    public class GENDER {

        public static final int MALE = 0;
        public static final int FEMALE = 1;
    }

    public class DEVICE_TYPE {

        public static final int ANDROID = 1;
        public static final int IOS = 0;
        public static final int WEB = 2;
    }

    public class BUZZ_TYPE_VALUE {

        public static final int STATUS_BUZZ = 0;
        public static final int IMAGE_BUZZ = 1;
        public static final int GIFT_BUZZ = 2;
        public static final int VIDEO_BUZZ = 3;
        public static final int MULTI_BUZZ = 4;
        
        public static final int TEXT_STATUS = 10;
        public static final int IMAGE_STATUS = 11;
        public static final int VIDEO_STATUS = 12;
        public static final int MULTI_STATUS = 13;
        public static final int STREAM_STATUS = 14;
        public static final int AUDIO_STATUS = 15;
        public static final int SHARE_STATUS = 16;
    }

    public static final int MAX_USER_LOCAL_BUZZ = 100;

    public class REPORT_TYPE_VALUE {

        public static final int BUZZ_REPORT = 0;
        public static final int IMAGE_REPORT = 1;
        public static final int USER_REPORT = 2;
        public static final int VIDEO_REPORT = 3;
        public static final int AUDIO_REPORT = 4;
        public static final int ALBUM_IMG_REPORT = 5;
    }

    public class CALL_TYPE_VALUE {

        public static final int VOICE_CALL = 15;
        public static final int VIDEO_CALL = 16;
    }

    public class CALL_ANSWER_VALUE {

        public static final String VOICE_CALL_ANSWER = "2";
        public static final String VOICE_CALL_REFUSE = "3";
        public static final String VOICE_CALL_BUSY = "4";

        public static final String VIDEO_CALL_ANSWER = "6";
        public static final String VIDEO_CALL_REFUSE = "7";
        public static final String VIDEO_CALL_BUSY = "8";
    }

    //HUNGDT add
    public class PARTNER_ANSWER_VALUE {

        public static final int PARTNER_ANSWER = 0;
        public static final int PARTNER_BUSY = 1;
        public static final int PARTNER_REFUSE = 2;
        public static final int UNKNOW_RESPOND = 3;
    }

    public class NOTIFICATION_TYPE_VALUE {

        public static final int CHECK_OUT_UNLOCK_NOTI = 2;
        public static final int FAVORITED_UNLOCK_NOTI = 4;
        public static final int LIKE_MY_BUZZ_NOTI = 5;
        public static final int COMMENT_MY_BUZZ_NOTI = 7;
        public static final int NEW_BUZZ_FROM_FAVORIST_NOTI = 19;
        public static final int CHAT = 11;
        public static final int REPLY_COMMENT = 20;
        public static final int ONLINE_ALERT_NOT = 12;
        public static final int DAILY_BONUS_NOTI = 13;
        public static final int APPROVED_BUZZ_NOTI = 15;
        public static final int APPROVED_BACKSTAGE_NOTI = 16;
        public static final int FREE_PAGE_NOTI = 18;
        public static final int DENIED_BUZZ_NOTI = 21;
        public static final int DENIED_BACKSTAGE_NOTI = 22;
        public static final int APPROVED_TEXT_BUZZ_NOTI = 24;
        public static final int DENIED_TEXT_BUZZ_NOTI = 25;
        public static final int APPROVED_COMMENT_NOTI = 26;
        public static final int DENIED_COMMENT_NOTI = 27;
        public static final int APPROVED_SUB_COMMENT_NOTI = 28;
        public static final int DENIED_SUB_COMMENT_NOTI = 29;
        public static final int APPROVED_USER_INFOR_NOTI = 30;
        public static final int DENIED_A_PART_OF_USER_INFO_NOTI = 31;
        public static final int DENIED_USER_INFO_NOTI = 32;
        public static final int AUTO_NEWS = 33;
        public static final int QA_NOTI = 34;
        public static final int TAG_FROM_BUZZ_NOTI = 37;
        public static final int SHARE_MUSIC = 38;
        public static final int NOTI_NEW_LIVESTREAM_FROM_FAVORIST = 40;
        public static final int NOTI_TAG_LIVESTREAM_FROM_FAVOURIST = 41;
    }

    public class END_CALL_VALUE {

        public static final int BUSY = 1;
        public static final int CANCEL = 2;
        public static final int END_CALL_BY_MALE = 3;
        public static final int END_CALL_BY_FEMALE = 4;
        public static final int END_CALL_BY_NOT_ENOUGH_POINT = 5;
        public static final int OTHERS = 6;
    }

    public class SYSTEM_ACCOUNT {

        public static final int SYSTEM_NEWS = 4;
        public static final int SYSTEM_SAFETY = 1;
        public static final int SYSTEM_TIPS = 2;
        public static final int SYSTEM_ADVERTISE = 3;
        public static final int SYSTEM = 5;
    }

    public class ACCOUNT_TYPE_VALUE {

        public static final int EMAIL_TYPE = 0;
        public static final int FB_TYPE = 1;
        public static final int MOCOM_TYPE = 2;
        public static final int FAMU_TYPE = 3;
    }

    public class USER_STATUS_FLAG {

        public static final int ACTIVE = 1;
        public static final int DEACITIVE = 0;
        public static final int DISABLE = -1;
        public static final int NONE = -2;
    }

    public class REVIEW_STATUS_FLAG {

        public static final int PENDING = 0;
        public static final int APPROVED = 1;
        public static final int DENIED = -1;
        public static final int NONE = -2;
    }

    public class REPORT_STATUS_FLAG {

        public static final int WAITING = 0;
        public static final int GOOD = 1;
        public static final int NOT_GOOD = -1;
    }

    public class REQUEST_CALL_TYPE {

        public static final int MY_REQUEST_CALL = 1;
        public static final int PARTNER_REQUEST_CALL = 2;
    }

    public static final List<String> SPECIAL_CHARACTER = Arrays.asList("\\", "(", ")", "?", "$", ".", "*", "+", "^", "[", "]", "|");
    public static final int THUMBNAIL_WIDTH_SIZE = 300;

    public class AVAILABLE_FLAG_VALUE {

        public static final int AVAILABE_FLAG = 1;
        public static final int NOT_AVAILABLE_FLAG = 0;
        public static final int DISABLE_FLAG = -1;
    }

    public static final int A_MINUTE = 60 * 1000;
    public static final int FIVE_MINUTES = 5 * A_MINUTE;
    public static final int AN_HOUR = 60 * A_MINUTE;
    public static final long A_DAY = 24 * AN_HOUR;
    public static final long JST_TIME_RAW_OFF_SET = 9 * AN_HOUR;

    public class ENCODING_VALUE {

        public static final String ISO = "ISO-8859-1";
        public static final String UTF = "UTF-8";
    }

    public class PURCHASE_PRODUCTION_TYPE {

        public static final int APPLE_PRODUCTION = 0;
        public static final int GOOLE_PRODUCTION = 1;
        public static final int AMAZON_PRODUCTION = 2;
    }

//    public class REPORT_REASON_VALUE{
//        public static final int SEXUAL_CONTENT = 0;
//        public static final int VIOLENT_CONTENT = 1;
//        public static final int HATEFUL_CONTENT = 2;
//        public static final int DANGEROUS_CONTENT = 3;
//        public static final int COPYRIGHTED_CONTENT = 4;
//        public static final int SPAM_CONTENT = 5;
//        public static final int UNDERAGER_CONTENT = 6;
//    }
    public static final String GCM_DEVELOPMENT_KEY = "AIzaSyC9qz9W7qDJ4ijPYUKgKw1OFOiINDDtFmY";
    public static final String GCM_PRODUCTION_KEY = "AIzaSyC9qz9W7qDJ4ijPYUKgKw1OFOiINDDtFmY";
    public static final String GCM_URL = "https://android.googleapis.com/gcm/send";
    public static final int GCM_TIME_TO_LIVE = 60;
    
    public static final String FCM_PRODUCTION_KEY = "AIzaSyBuBAgr3nYkQBC7JwdpAEtU_BT1vt3YhEw";
    public static final String FCM_DEVELOPMENT_KEY = "AIzaSyBuBAgr3nYkQBC7JwdpAEtU_BT1vt3YhEw";

    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final int FCM_TIME_TO_LIVE = 60;
    

    public static final String NO_AVATAR_STRING_VALUE = "-1";

    public static final String COOKIES_NAME = "eazy_cookie";

    public class DEVICE_NAME {

        public static final String MAC_DEVICE = "Mac";
        public static final String ANDROID_DEVICE = "Android";
    }

    public class USER_AGENT {

        public static final String USER_AGENT_IOS = "ios";
        public static final String USER_AGENT_ANDROID = "android";
    }

    public class STATIC_FILE_TYPE {

        public static final int TERM_OF_USE = 0;
        public static final int PRIVACY_POLICY = 1;
        public static final int SAFETY_TIPS = 2;
    }

    public class STATISTIC_USER_TYPE {

        public static final int DAY_USER_STATISTIC = 0;
        public static final int MONTH_USER_STATISTIC = 1;
        public static final int YEAR_USER_STATISTIC = 2;
    }

    public class PAYMENT_TYPE {

        public static final int CREDIT = 0;
        public static final int BISCASH = 1;
        public static final int POINT_BACK = 2;
        public static final int C_CHECK = 3;
        public static final int CONBONI = 4;
        
//        public static final int CREDIT = 2;
//        public static final int BISCASH = 3;
//        public static final int POINT_BACK = 5;
//        public static final int C_CHECK = 3;
//        public static final int CONBONI = 4;
    }

    public class UNIQUE_NUMBER_TYPE {

        public static final int NEW = 0;
        public static final int EXISTS = 1;
        public static final int EMPTY = 2;
        public static final int DUPLICATE = 3;
    }

    public static final String ANDROID_PURCHASES_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhxcDnsuqEkwRb9ZEI/fgrVVOcmdIHptHFomocB804N19FakPWM9l8Qi9Io4G5TNNNzi1Z3VelaoT4q9fljbsrp4tJ8TUZcqpjyNXAMO7ct7laMI5gnMPGu+v13Ai39/NGJ+xcKJ5nRi6AnMQnIdGlOznwI7U16UmKUV8xJVrExRSi/EV8fqUbkkEi16aXso9jSj2+etCTrlPRn5AhGsUbB12SZtaZTfIedG7hhji4Qy8JmkocF04K39RouUXPzhtfZJ2RM/I7bT7goPPHOC9/EQu5BArj4HqkoaR4SK+XIOxb+kN9kc+P8w8hFNgME4oP37Vu3Yg236kDPOYKwmeIQIDAQAB";
    public static final String IOS_PURCHASE_SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    public static final String IOS_PURCHASE_PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    public static final String AMAZON_PURCHASE_PRODUCTION_URL = "https://appstore-sdk.amazon.com/version/1.0/verifyReceiptId/developer/%s/user/%s/receiptId/%s";

    public static final String LIVESTREAM_SERVER_KEY = "rf4rdd9lbk4g2u8ysckr";
    
    public class LANGUAGE {

        public static final String JAPANESE = "ja";
        public static final String ENGLISH = "en";
    }

    public class IMAGE_KIND_VALUE {

        public static final String ORIGINAL_IMAGE = "2";
        public static final String THUMBNAIL = "1";
        public static final String STICKER = "3";
        public static final String GIFT = "4";
        public static final String STICKER_CATEGORY = "5";
        public static final String NEWS_BANNER = "6";
    }
    
    public class POST_MODE {
        
        public static final int EVERYONE = 0;
        public static final int FRIEND = 1;
        public static final int ONLY_ME = 2;
        public static final int TAG_ONLY = 3;
    }
    
    public class STREAM_STATUS {
        public static final String ON = "on";
        public static final String OFF = "off";
        public static final String PENDING = "pending";
        public static final String UPDATE = "update";
        public static final String RECORDING = "recording";
    }
    
}
