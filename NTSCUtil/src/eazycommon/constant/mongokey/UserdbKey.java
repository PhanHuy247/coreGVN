/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant.mongokey;

/**
 *
 * @author duyetpt
 */
public class UserdbKey {

    public static final String DB_NAME = "userdb";

    public static final String USERS_COLLECTION = "user";

    public class USER {

        public static final String ID = "_id";
        public static final String APPLICATION_ID = "application_id";
        public static final String EMAIL = "email";
        public static final String USERNAME = "user_name";
        public static final String SORT_NAME = "sort_name";
        public static final String PASSWORD = "pwd";
        public static final String SIP_PASSWORD = "sip_pwd";
        public static final String ORIGINAL_PASSOWRD = "original_pwd";
        public static final String AVATAR_ID = "ava_id";
        public static final String BIRTHDAY = "bir";
        public static final String GENDER = "gender";
        public static final String REGION = "region";
        public static final String ABOUT = "abt";
        public static final String FB_ID = "fb_id";
        public static final String MOCOM_ID = "mocom_id";
        public static final String FAMU_ID = "famu_id";
        public static final String POINT = "point";
        public static final String FAVOURIST_NUMBER = "fav_num";
        public static final String FAVOURITED_NUMBER = "fvt_num";
        public static final String BACKSTAGE_NUMBER = "bckstg_num";
        public static final String PB_IMAGE_NUMBER = "pbimg_num";
        public static final String PB_VIDEO_NUMBER = "pbvideo_num";
        public static final String PB_AUDIO_NUMBER = "pbaudio_num";
        public static final String BUZZ_NUMBER = "buzz_number";
        public static final String GIFT_NUMBER = "gift_num";
        public static final String REPORT_NUMBER = "rpt_num";
        public static final String VERIFICATION_CODE = "vrf_code";
        public static final String BACKSTAGE_PRICE = "bckstg_pri";
        public static final String LAST_UPDATE = "lst_upd";
        public static final String REGISTER_DATE = "reg_date";
        public static final String HAS_ACCOUNT_EMAIL = "has_account_email";
        public static final String BACKSTAGE_RATE = "bckstg_rate";
        public static final String RATE_NUMBER = "rate_num";
        public static final String NOTIFICATION_READ_TIME = "noti_read_time";
        public static final String NOTIFICATION_LIKE_READ_TIME = "noti_like_read_time";
        public static final String NOTIFICATION_NEWS_READ_TIME = "noti_news_read_time";
        public static final String NOTIFICATION_QA_READ_TIME = "noti_qa_read_time";
        public static final String LAST_PURCHASE_TIME = "last_pur_time";
        public static final String HAVE_PURCHASE = "have_purchase";
        public static final String HAVE_EMAIL = "have_email";
        public static final String FIRST_PURCHASE_TIME = "first_pur_time";
        public static final String MEMO = "memo";
        public static final String LAST_LOGIN_TIME = "last_login_time";
        public static final String SYSTEM_ACCOUNT = "sys_acc";
        public static final String CM_CODE = "cm_code";
        public static final String ACCOUNT_TYPE = "type";
        public static final String FLAG = "flag";
        public static final String BODY_TYPE = "body_type";
        public static final String JOB = "job";
        public static final String JOINT_HOURS = "join_hours";
        public static final String VERIFICATION_FLAG = "verification_flag";
        public static final String FINISH_REGISTER_FLAG = "finish_register_flag";
        public static final String VIDEO_CALL_WAITING = "video_call_waiting";
        public static final String VOICE_CALL_WAITING = "voice_call_waiting";
        public static final String CALL_REQUEST_SETTING = "call_request_setting";
        public static final String UPDATE_EMAIL_FLAG = "update_email_flag";
        public static final String USER_ID = "user_id";
        public static final String DEVICE_TYPE = "device_type";
        // female
        public static final String CREA_ID = "crea_id";
        public static final String CREA_PASS = "crea_pass";

//        public static final String INDECENT = "indecent";
        public static final String TYPEOFMANS = "type_of_man";
        public static final String FETISH = "fetish";
        public static final String CUTETYPE = "cute_type";
        public static final String CUP = "cup";
        public static final String MEASUREMENTS = "measurements";
        public static final String BUST = "bust";
        public static final String WAIST = "waist";
        public static final String HIPS = "hips";
        //male
        public static final String HOBBY = "hobby";

        public static final String IS_PURCHASE = "is_purchase";
        public static final String SITE_ID = "site_id"; //thanhdd add

        public static final int IS_PURCHASE_NO = 0;
        public static final int IS_PURCHASE_YES = 1;
        public static final String OS_VERSION = "os_version";
        public static final String APP_VERSION = "application_version";
        public static final String DEVICE_NAME = "device_name";
        public static final String OS_NAME = "os_name";
        public static final String AdId = "ad_id";
        public static final String DEVICE_ID = "device_id";
        //thanhdd add
        public static final String TOTAL_PURCHASE_APPLE = "total_purchase_apple";
        public static final String TOTAL_PURCHASE_GOOGLE = "total_purchase_google";
        public static final String TOTAL_PURCHASE_CREDIT_CARD = "total_purchase_credit_card";
        public static final String TOTAL_PURCHASE_BITCACH = "total_purchase_bitcach";
        public static final String TOTAL_PURCHASE_POINTS_BACK = "total_purchase_points_back";
        //added by khanhdd
        public static final String TOTAL_PURCHASE = "total_purchase";
        public static final String TOTAL_POINT = "total_point";
        //add
        public static final String SAFE_USER = "safe_user";
        
        public static final String RATE_VALUE = "rate_value";
    }

    public static final String USER_SESSION_COLLECTION = "user_session";

    public class USER_SESSION {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TOKEN = "token";
        public static final String NORMARL_USER_FLAG = "normal_user_flag";
        public static final String APPLICATION_VERSION = "application_version";
        public static final String APPLICATION_TYPE = "application_type";
    }
    
    public static final String USER_TOKEN_COLLECTION = "user_token";

    public class USER_TOKEN {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TOKEN = "token";
        public static final String DEVICE_ID = "device_id";
        public static final String OLD_TOKEN = "old_token";
        public static final String NORMARL_USER_FLAG = "normal_user_flag";
        public static final String APPLICATION_VERSION = "application_version";
        public static final String APPLICATION_TYPE = "application_type";
    }

    //khanhdd
    public static final String MEMO_COLLECTION = "memo";

    public class MEMO {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String FRIEND_ID = "friend_id";
        public static final String MEMO = "memo";
        public static final String UPDATE_TIME = "update_time";
        public static final String AVATAR = "avatar";
        public static final String DATE_OF_BIRTH = "date_of_birth";
    }

    public static final String PB_IMAGE_COLLECTION = "public_image";

    public class PB_IMAGE {

        public static final String ID = "_id";
        public static final String PB_IMAGE_LIST = "pbimg_lst";
        public static final String IMAGE_ID = "img_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String FLAG = "flag";
        public static final String TIME = "time";
        public static final String PRIVACY = "privacy";
    }
    
    public static final String PB_VIDEO_COLLECTION = "public_video";

    public class PB_VIDEO {

        public static final String ID = "_id";
        public static final String PB_VIDEO_LIST = "pbvideo_lst";
        public static final String VIDEO_ID = "video_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String FLAG = "flag";
        public static final String TIME = "time";
        public static final String PRIVACY = "privacy";
    }
    
    public static final String PB_AUDIO_COLLECTION = "public_audio";

    public class PB_AUDIO {

        public static final String ID = "_id";
        public static final String PB_AUDIO_LIST = "pbaudio_lst";
        public static final String AUDIO_ID = "audio_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String FLAG = "flag";
        public static final String COVER = "cover";
        public static final String TIME = "time";
        public static final String PRIVACY = "privacy";
    }

    public static final String USER_BUZZ_COLLECTION = "user_buzz";

    public class USER_BUZZ {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BONUS_POINT = "bonus_point";
    }

    public static final String BACKSTAGE_COLLECTION = "backstage";

    public class BACKSTAGE {

        public static final String ID = "_id";
        public static final String BACKSTAGE_LIST = "bckstg_lst";
        public static final String IMAGE_ID = "img_id";
        public static final String FLAG = "flag";
    }

    public static final String REVIEWING_USER_COLLECTION = "reviewing_user";

    public class REVIEWING_USER {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String GENDER = "gender";
        public static final String ABOUT = "about";
        public static final String HOBBY = "hobby";
        public static final String TYPE_OF_MAN = "type_of_man";
        public static final String FETISH = "fetish";
        public static final String UPLOAD_TIME = "time";
    }

    public static final String CHAT_IMAGE_COLLECTION = "chat_img";

    public class CHAT_IMAGE {

        public static final String ID = "_id";
        public static final String CHAT_IMAGE_LIST = "chat_img_lst";
    }

    public static final String CHAT_IMAGE_TRANSACTION_COLLECTION = "chat_img_transaction";

    public class CHAT_IMAGE_TRANSACTION {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TO = "to";
        public static final String FROM = "from";
        public static final String SEND_TIME = "send_time";
        public static final String IP = "ip";
        public static final String IMAGE_ID = "image_id";
    }

    public static final String USER_ACTIVITY_COLLECTION = "user_act";

    public class USER_ACTIVITY {

        public static final String ID = "_id";
        public static final String LOCATION = "loc";
        public static final String LAST_ACTIVITY = "lst_act";
        public static final String STATUS = "stt";
        public static final String LAST_ONLINE = "lst_onl";
        public static final String LAST_LOGIN = "lst_login";
    }

    public static final String FAVORIST_COLLECTION = "favorist";

    public class FAVORIST {

        public static final String ID = "_id";
        public static final String FAVOURIS_LIST = "fav_lst";
    }

    public static final String FAVORITED_COLLECTION = "favorited";

    public class FAVORITED {

        public static final String ID = "_id";
        public static final String FAVOURITED_LIST = "fvt_lst";
        public static final String FAVOURISTED_TIME = "fvt_time";
        public static final String FAVOURISTED_ID = "fvt_id";
    }

    public static final String FRIEND_COLLECTION = "friend";

    public static final String BLOCK_COLLECTION = "block";

    public class BLOCK {

        public static final String ID = "_id";
        public static final String BLOCK_LIST = "block_lst";
        public static final String BE_BLOCKED_LIST = "blocked_lst";
    }

    public static final String CHECKOUT_COLLECTION = "check_out";

    public class CHECKOUT {

        public static final String ID = "_id";
        public static final String CHECKER_LIST = "chk_lst";
        public static final String CHECKER_ID = "chk_id";
        public static final String CHECKER_TIME = "chk_time";
    }

    public static final String MY_FOOTPRINT_COLLECTION = "my_footprint";

    public class MY_FOOTPRINTS {

        public static final String ID = "_id";
        public static final String MY_FOOTPRINTS_LIST = "my_footprints_list";
        public static final String VISITOR_ID = "visitor_id";
        public static final String VISITED_TIME = "visited_time";
    }

    public static final String DEACTIVATE_COLLECTION = "deactivate";

    public class DEACTIVATE {

        public static final String ID = "_id";
        public static final String DEACTIVATE_TIME = "de_act_time";
        public static final String DEACTIVATE_COMMENT = "de_act_cmt";
        public static final String FLAG = "flag";
    }

    public static final String GIFT_USER_COLLECTION = "gift";

    public class GIFT_USER {

        public static final String ID = "_id";
        public static final String GIFT_LIST = "gift_lst";
        public static final String SENDER_ID = "send_id";
        public static final String GIFT_ID = "gift_id";
    }

    public static final String IMAGE_COLLECTION = "user_image";

    public static final String IMAGE_STF_COLLECTION = "image";

    public class IMAGE {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String IMAGE_ID = "img_id";
        public static final String UPLOAD_TIME = "upload_time";
        public static final String REVIEW_TIME = "review_time";
        public static final String REPORT_NUMBER = "rpt_num";
        public static final String IMAGE_TYPE = "img_type";
        public static final String FLAG = "flag";
        public static final String STATUS = "status";
        public static final String AVATAR_FLAG = "ava_flag";
        public static final String APPROVED_FLAG = "app_flag";
        public static final String DENIED_FLAG = "denied_flag";
        public static final String APPEAR_FLAG = "appear_flag";
        public static final String REPORT_FLAG = "report_flag";
        public static final String REPORT_TIME = "report_time";
        //HUNGDT add
        public static final String IS_FREE = "is_free";
        public static final String GENDER = "gender";
        // Namhv
        public static final String USER_DENY = "user_deny";
    }

    public static final String INVITATION_CODE_COLLECTION = "invitation_code";

    public class INVITATION_CODE {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
    }

    public static final String LAST_TIME_COLLECTION = "last_time";

    public class LAST_TIME {

        public static final String ID = "_id";
        public static final String LAST_TIME_GET_FAVORITED_LIST = "last_get_fvt";
        public static final String LAST_TIME_GET_CHECKOUT_LIST = "last_get_checkout";
        public static final String OLD_USER_CHECKOUT_LIST = "old_list_checkout";
        public static final String OLD_USER_FAVORITED_LIST = "old_list_fvt";
    }

    public static final String NOTIFICATION_SETTING_COLLECTION = "noti_setting";

    public class NOTIFICATION_SETTING {

        public static final String ID = "_id";
        public static final String NOTI_BUZZ = "noti_buzz";
        public static final String EAZY_ALERT = "eazy_alt";
        public static final String NOTI_CHAT = "noti_chat";
        public static final String NOTI_CHECK_OUT = "noti_chk_out";
        public static final String NOTI_LIKE = "noti_like";
    }

    public static final String NOTIFICATION_COLLECTION = "notification";

    public class NOTIFICATION {

        public static final String ID = "_id";
        public static final String TO_USER_ID = "to_user_id";
        public static final String TYPE = "noti_type";
        public static final String NOTI_USER_NAME = "noti_user_name";
        public static final String FROM_NOTI_USER_ID = "from_noti_user_id";
        public static final String NOTI_BUZZ_ID = "noti_buzz_id";
        public static final String NOTI_IMAGE_ID = "noti_image_id";
        public static final String NOTI_BUZZ_OWNER_ID = "noti_buzz_owner_id";
        public static final String NOTI_BUZZ_OWNER_NAME = "noti_buzz_owner_name";
        public static final String RANK_LOOK = "rank_look";
        public static final String CONTENT = "content";
        public static final String URL = "url";
        public static final String PUSH_ID = "push_id";
        public static final String POINT = "point";
        public static final String NOTI_COMMENT_ID = "noti_comment_id";
        public static final String NOTI_COMMENT_OWNER_ID = "noti_comment_owner_id";
        public static final String NOTI_COMMENT_OWNER_NAME = "noti_comment_owner_name";
        public static final String TIME = "time";
        public static final String IS_READ = "is_read";
        public static final String NUMBER_CLICK = "number_click";
        public static final String IS_SEEN = "is_seen";
    }

    public static final String RATE_COLLECTION = "rate";

    public class RATE {

        public static final String ID = "_id";
        public static final String RATE_LIST = "rate_lst";
        public static final String RATE_ID = "rate_id";
        public static final String RATE_POINT = "rate_point";
    }

    public static final String ONLINE_ALERT_COLLECTION = "online_alert";

    public class ONLINE_ALERT {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String ALERT_USER_ID = "alt_id";
        public static final String ALERT_TYPE = "alt_type";
        public static final String MAX_ALERT_NUMBER = "max_alt_time";
        public static final String LASTEST_ALERT_NUMBER = "lst_alt_num";
        public static final String LASTEST_ALERT_DATE = "lst_alt_date";
    }

    public static final String REQUEST_COLLECTION = "request_friend";

    public class REQUEST {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "rqt_id";
        public static final String TIME_REQUEST = "tm_rqt";
        public static final String DEVICE_ID = "device_id";
        public static final String RECIVER_ID = "rcv_id";
    }

    public static final String UNLOCK_COLLECTION = "unlock";

    public class UNLOCK {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        //public static final String UNLOCK_TYPE = "unlck_type";
        public static final String END_TIME = "end_time";
        public static final String UNLOCK_ID = "unlck_id";
        public static final String IMAGE_ID = "image_id";
        public static final String VIDEO_ID = "video_id";
        public static final String AUDIO_ID = "audio_id";
    }

    public static final String USER_GIFT_COLLECTION = "user_gift";

    public class USER_GIFT {

        public static final String ID = "_id";
        public static final String GIFT_LIST = "gift_lst";
        public static final String GIF_NUMBER = "gift_num";
        public static final String GIFT_ID = "gift_id";
    }

    public static final String POINT_EXCHANGED_BY_CHAT_COLLECTION = "point_exchanged_by_chat";

    public class POINT_EXCHANGED_BY_CHAT {

        public static final String ID = "_id";
        public static final String MESSAGE_ID = "msg_id";
        public static final String USER_ID = "user_id";
        public static final String POINT_DIFFER = "point_differ";
    }

    public static final String SETTING_COLLECTION = "setting";

    public class SETTING {

        public static final String USER_ID = "user_id";
        public static final String SHOW_ME = "show_me";
        public static final String LOWER_AGE = "lower_age";
        public static final String UPPER_AGE = "upper_age";
        public static final String DISTANCE = "distance";
        public static final String INTERESTED_IN = "inters_in";
        public static final String ETHNICITY = "ethn";
        public static final String LOCATION = "region";
    }

    public static final String USER_STICKER_COLLECTION = "user_stk";

    public class USER_STICKER {

        public static final String ID = "_id";
        public static final String NEW_LIST_SEEN = "new_lst_seen";
        public static final String DOWNLOAD_LIST = "lst_download";
        public static final String STICKER_CATEGORY_ID = "stk_cat_id";
        public static final String STICKER_CATEGORY_TYPE = "stk_cat_type";
        public static final String DOWNLOAD_TIME = "download_time";
    }

    public static final String BUZZ_LIKE_COLLECTION = "buzz_like";

    public class BUZZ_LIKE {

        public static final String ID = "_id";
        public static final String LIKE_LIST = "like_lst";
        public static final String USER_ID = "user_id";
        public static final String LIKE_TIME = "like_time";
        public static final String LIKE_FLAG = "like_flag";
    }

    public static final String BUZZ_SEEN_COLLECTION = "buzz_seen";

    public class BUZZ_SEEN {

        public static final String ID = "_id";
        public static final String SEEN_LIST = "seen_lst";
    }

    public static final String USER_TEMPLATE_COLLECTION = "user_template";

    public class USER_TEMPLATE {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TEMPLATE_CONTENT = "template_content";
        public static final String TEMPLATE_TITLE = "template_title";
        public static final String SORT_TIME = "sort_time";
    }

    public static final String CONTACT_TRACKING_COLLECTION = "contact_tracking";

    public class CONTACT_TRACKING {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String CONTACTED_USERS = "contacted_users";
    }

    //HUNGDT add
    public static final String FILE_COLLECTION = "file";

    public class FILE {

        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String USER_ID = "user_id";
        public static final String IS_FREE = "is_free";
        public static final String VIDEO_STATUS = "video_status";
        public static final String UPLOAD_TIME = "upload_time";
        public static final String REVIEW_TIME = "review_time";
        public static final String USER_NAME = "user_name";
        public static final String EMAIL = "email";
        public static final String FLAG = "flag";
        public static final String THUMBNAIL_URL = "thumbnail_url";
    }

    //Thanhdd add
    public static final String CHAT_LOG_EXTENSION  = "chatlogextension";

    public class MESSAGE {

        public static final String Field_MsgID = "msgid";
        public static final String Field_From = "from";
        public static final String Field_To = "to";
        public static final String Field_MsgType = "type";
        public static final String Field_Value = "value";
        public static final String Field_OriginTime = "ot";
        public static final String Field_ServerTime = "st";
        public static final String Field_DestTime = "dt";
        public static final String Field_IsDel = "del";

        public static final String Field_BlackList = "bl";
        public static final String ObjectID = "_id";
        public static final String Field_Ip = "ip";
        public static final String Field_IsLock = "islock";
    }
    
    //Linh add #5747
    public static final String USER_INTERACTION_COLLECTION  = "user_interaction";

    public class USER_INTERACTION {

        public static final String ID = "_id";
        public static final String INTERACTION_LIST = "interaction_list";
        public static final String USER_ID = "user_id";
    }
    
    public static final String RATE_VOICE_COLLECTION = "rate_voice";

    public class RATE_VOICE {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String REQUEST_ID = "rqt_id";
        public static final String RATE_VALUE = "rate_value";
    }
    
    public static final String USER_VIDEO_COLLECTION = "user_video";
    
    public class USER_VIDEO {
        
        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String VIDEO_ID = "video_id";
        public static final String FLAG = "flag";
        public static final String VIDEO_TYPE = "video_type";
        public static final String STATUS = "status";
        public static final String UPLOAD_TIME = "upload_time";
        public static final String REVIEW_TIME = "review_time";
        public static final String REPORT_FLAG = "report_flag";
        public static final String REPORT_NUMBER = "rpt_num";
        public static final String REPORT_TIME = "report_time";
    }
    
    public static final String USER_AUDIO_COLLECTION = "user_audio";
    
    public class USER_AUDIO {
        
        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String AUDIO_ID = "audio_id";
        public static final String FLAG = "flag";
        public static final String AUDIO_TYPE = "audio_type";
        public static final String STATUS = "status";
        public static final String UPLOAD_TIME = "upload_time";
        public static final String REVIEW_TIME = "review_time";
        public static final String REPORT_FLAG = "report_flag";
        public static final String REPORT_NUMBER = "rpt_num";
        public static final String REPORT_TIME = "report_time";
    }
    
    public static final String ALBUM_COLLECTION = "album";
    
    public class ALBUM {
        
        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String ALBUM_NAME = "album_name";
        public static final String ALBUM_DES = "album_des";
        public static final String PRIVACY = "privacy";
        public static final String TIME = "time";
    }
    
    public static final String ALBUM_IMAGE_COLLECTION = "album_image";
    
    public class ALBUM_IMAGE {
        
        public static final String ID = "_id";
        public static final String ALBUM_ID = "album_id";
        public static final String IMAGE_ID = "image_id";
        public static final String TIME = "time";
        public static final String FLAG = "flag";
    }
}
