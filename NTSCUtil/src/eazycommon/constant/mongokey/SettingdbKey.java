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
public class SettingdbKey {

    public static String DB_NAME = "settingdb";

    public static final String DISTANCE_SETTING_COLLECTION = "distance";

    public class DISTANCE_SETTING {

        public static final String ID = "_id";
        public static final String DISTANCE = "distance";
        public static final String LOCAL_BUZZ = "local_buzz";
        public static final String LAST_UPDATE = "lst_upd";
    }
    public static final String BANNED_WORD_COLLECTION = "banned_word";    

    public class BANNED_WORD {

        public static final String ID = "_id";
        public static final String WORD = "word";
        public static final String VERSION = "version";
        public static final String FLAG = "flag";
        public static final String TIME = "dateTime";
    }
    
    public static final String REPLACE_WORD_COLLECTION = "replace_word";

    public class REPLACE_WORD {

        public static final String ID = "_id";
        public static final String WORD = "word";
        public static final String VERSION = "version";
        public static final String FLAG = "flag";
        public static final String TIME = "dateTime";
    }

    public static final String EXTRA_PAGE_COLLECTION = "extra_page";

    public class EXTRA_PAGE {

        public static final String ID = "_id";
        public static final String PAGE_TITLE = "page_title";
        public static final String URL = "url";
        public static final String ORDER = "order";
    }

    public static final String SERVER_LAST_TIME_COLLECTION = "server_last_time";

    public class SERVER_LAST_TIME {

        public static final String ID = "_id";
        public static final String LAST_TIME = "lst_time";
    }

    public static final String OTHER_SETTING_COLLECTION = "other_setting";

    public class OTHER_SETTING {

        public static final String ID = "_id";
        public static final String WINK_BOMB_NUMBER = "wink_bomb_number";
        public static final String LOOK_TIME = "look_time";
        public static final String UNLOCK_FAVORITED_TIME = "unlock_fvt_time";
        public static final String UNLOCK_CHECK_TIME = "unlock_chk_out_time";
        public static final String UNLOCK_BACKSTAGE_TIME = "unlock_bckstg_time";
        public static final String UNLOCK_VIEW_IMAGE_TIME = "unlock_view_image_time";
        public static final String UNLOCK_WATCH_VIDEO_TIME = "unlock_watch_video_time";
        public static final String UNLOCK_LISTEN_AUDIO_TIME = "unlock_listen_audio_time";
        public static final String AUTO_APPROVED_IMAGE = "auto_approved_img";
        public static final String AUTO_APPROVED_VIDEO = "auto_approved_video";
        public static final String AUTO_APPROVED_BUZZ = "auto_approved_buzz";
        public static final String AUTO_APPROVED_COMMENT = "auto_approved_comment";
        public static final String AUTO_APPROVED_USER_INFO = "auto_approved_user_info";
        public static final String TURN_OFF_SAFARY = "turn_off_safary";
        public static final String LOGIN_BY_MOCOM = "turn_off_login_by_mocom";
        public static final String EXTENDED_USER_INFO = "turn_off_extended_user_info";
        public static final String SHOW_NEWS = "turn_off_show_news";
        public static final String TURN_OFF_SAFARY_VERSION = "turn_off_safary_version";
        public static final String GET_FREE_POINT = "turn_off_get_free_point";
        
        public static final String TURN_OFF_BROWSER_ANDROID = "turn_off_browser_android";
        public static final String LOGIN_BY_MOCOM_ANDROID = "turn_off_login_by_mocom_android";
        public static final String EXTENDED_USER_INFO_ANDROID = "turn_off_extended_user_info_android";
        public static final String SHOW_NEWS_ANDROID = "turn_off_show_news_android";
        public static final String TURN_OFF_BROWSER_ANDROID_VERSION = "turn_off_browser_android_version";
        public static final String GET_FREE_POINT_ANDROID = "turn_off_get_free_point_android";
//        public static final String ENTERPRISE_TURN_OFF_SAFARY = "enterprise_turn_off_safary";
//        public static final String ENTERPRISE_LOGIN_BY_MOCOM = "enterprise_turn_off_login_by_mocom";
//        public static final String ENTERPRISE_TURN_OFF_SAFARY_VERSION = "enterprise_turn_off_safary_version";

        public static final String AUTO_HIDE_REPORTED_IMAGE = "auto_hide_reported_image";
        public static final String AUTO_HIDE_REPORTED_VIDEO = "auto_hide_reported_video";
        public static final String AUTO_HIDE_REPORTED_AUDIO = "auto_hide_reported_audio";
        public static final String IOS_ENTERPRISE_USABLE_VERSION = "ios_enterprise_usable_version";
        public static final String IOS_NON_ENTERPRISE_USABLE_VERSION = "ios_non_enterprise_usable_version";
        public static final String ANDROID_USABLE_VERSION = "android_usable_version";
        public static final String MAX_LENGTH_BUZZ = "max_length_buzz";
    }

    public static final String OTHER_SETTING_APP_COLLECTION = "other_setting_app";
    
    public class OTHER_SETTING_APP {
        public static final String APP_ID = "application_id";
        public static final String FORCE_UPDATING = "force_updating";
        public static final String URL_WEB = "url_web";
        public static final String APP_VERSION = "app_version";
    }
    
    public static final String SHAKE_CHAT_SETTING_COLLECTION = "shake_chat_setting";

    public class SHAKE_CHAT_SETTING {

        public static final String ID = "_id";
        public static final String TYPE = "type";
        public static final String INTERES_IN = "inters_in";
        public static final String LOWER_AGE = "lower_age";
        public static final String UPPER_AGE = "upper_age";
        public static final String DISTANCE = "distance";
        public static final String ETHINICITY = "ethn";
    }

    public static final String POINT_SETTING_COLLECTION = "point_setting";

    public class POINT_SETTING {

        public static final String ID = "_id";
        public static final String TYPE = "type";
        public static final String MALE_REQUEST_POINT = "male_req_point";
        public static final String POTENTIAL_CUSTOMER_MALE_REQUEST_POINT = "potential_male_customer_req_point";
        public static final String FEMALE_REQUEST_POINT = "female_req_point";
        public static final String POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT = "potential_female_customer_req_point";

        public static final String MALE_PARTNER_POINT = "male_partner_point";
//        public static final String POTENTIAL_CUSTOMER_MALE_PARTNER_POINT = "potential_male_customer_partner_point";
        public static final String FEMALE_PARTNER_POINT = "female_partner_point";
//        public static final String POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT = "potential_female_customer_partner_point"; 
    }

    public static final String COMMUNICATION_SETTING_COLLECTION = "communication_setting";

    public class COMMUNICATION_SETTING {

        public static final String ID = "_id";
        public static final String TYPE = "type";
        public static final String CALLER = "caller";
        public static final String POTENTIAL_CUSTOMER_CALLER = "potential_customer_caller";
        public static final String RECEIVER = "receiver";
        public static final String POTENTIAL_CUSTOMER_RECEIVER = "potential_customer_receiver";
        public static final String CALLER_RECEIVER = "caller_receiver";
    }
    
    public static final String CONNECTION_POINT_SETTING_COLLECTION = "connection_point_setting";

    public class CONNECTION_POINT_SETTING {

        public static final String ID = "_id";
        public static final String TYPE = "type";
        public static final String SENDER = "sender";
        public static final String POTENTIAL_CUSTOMER_SENDER = "potential_customer_sender";
        public static final String RECEIVER = "receiver";
        public static final String POTENTIAL_CUSTOMER_RECEIVER = "potential_customer_receiver";
        public static final String SENDER_RECEIVER = "sender_receiver";
    }

    public static final String ADMINISTRATOR_COLLECTION = "admin";

    public class ADMINISTRATOR {

        public static final String ID = "_id";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String ROLE_ID = "role_id";
        public static final String PASSWORD = "pwd";
        public static final String ORIGINAL_PASSWORD = "original_pwd";
        public static final String FLAG = "flag";
        public static final String SPECIAL_FLAG = "special_flag";
    }

    public static final String ADMIN_STRING_COLLECTION = "admin_string";

    public class ADMIN_STRING {

        public static final String ID = "_id";
        public static final String STRING = "string";
    }
    
    public static final String ADMIN_SETTING_COLLECTION = "admin_setting";

    public class ADMIN_SETTING {

        public static final String ID = "_id";
        public static final String TIME_ZONE = "time_zone";
    }    

    public static final String ROLE_COLLECTION = "role";

    public class ROLE {

        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String SPECIAL_FLAG = "special_flag";
    }

    public static final String ROLE_GROUP_COLLECTION = "role_group";

    public class ROLE_GROUP {

        public static final String ID = "_id";
        public static final String ROLE = "role";
        public static final String GROUP = "group";
    }

    public class AUTO_MESSAGE {

        public static final String ID = "_id";
        public static final String TIME = "time";
        public static final String MESSAGE = "message";
        public static final String QUERY = "query";
        public static final String FLAG = "flag";
        public static final String RECEIVER_NUMBER = "receiver_number";
        public static final String RECEIVERS = "receivers";
        public static final String CLICKED_USERS = "clicked_users";
        public static final String TYPE = "type";

        public static final String AUTO_NOTIFY_COLLECTION = "auto_notify";
        public static final String AUTO_NEWS_NOTIFY_COLLECTION = "auto_news_notify";
        public static final String AUTO_QA_NOTIFY_COLLECTION = "auto_qa_notify";

        public class AUTO_NOTIFY {

            public static final String URL = "url";
            public static final String IP = "ip";
        }

        public static final String AUTO_MESSAGE_CHAT_COLLECTION = "auto_message";

        public class AUTO_MESSAGE_CHAT {

            public static final String SENDER = "sender";
        }

    }
    
    public static final String LOGIN_BONUS_MESSAGE_COLLECTION = "login_bonus_message";
    
    public class LOGIN_BONUS_MESSAGE{
        public static final String ID = "_id";
        public static final String LOGIN_BONUS_TIMES = "login_bonus_times";
        public static final String SENDER = "sender";
        public static final String CONTENT = "content";
        public static final String GENDER = "gender";
        public static final String RECEIVER_COUNT = "receiver_count";
        public static final String RECEIVERS = "receivers";
    }
    
    public static final String LOGIN_TRACKING_COLLECTION = "login_tracking";
    
    public class LOGIN_TRACKING{
        public static final String ID = "_id";
        public static final String LOGIN_BONUS_TIMES = "login_bonus_times";
        public static final String MESSAGES_SENT = "messages_sent";
        public static final String GENDER = "gender";
    }

    public static final String SCREEN_GROUP_COLLECTION = "screen_group";

    public class SCREEN_GROUP {

        public static final String ID = "_id";
        public static final String FLAG = "flag";
        public static final String TITLE = "title";
        public static final String NAME = "name";
        public static final String ORDER = "order";
    }

    public static final String SCREEN_API_COLLECTION = "screen_api";

    public class SCREEN_API {

        public static final String ID = "_id";
        public static final String API = "api";
        public static final String SCREEN = "screen";
    }
    public static final String SCREEN_COLLECTION = "screen";

    public class SCREEN {

        public static final String ID = "_id";
        public static final String GROUP_ID = "group_id";
        public static final String FLAG = "flag";
        public static final String TITLE = "title";
        public static final String PATH = "path";
        public static final String NAME = "name";
        public static final String CONTROLLER = "controller";
        public static final String ORDER = "order";
    }
    
    public static final String FREE_POINT_COLLECTION = "free_point";
    public class FREE_POINT{
        public static final String ID = "_id";
        public static final String FREE_POINT_NAME = "free_point_name";
        public static final String FREE_POINT_NUMBER = "free_point_number";
    }     
    
    public static final String NEWS_COLLECTION = "news";
    public class NEWS{
        public static final String ID = "_id";
        public static final String title = "title";
        public static final String update_date = "update_date";
        public static final String banner_id = "banner_id";
        public static final String content = "content";
        public static final String is_shown = "is_shown";
        public static final String from = "from";
        public static final String to = "to";
        public static final String device_type = "device_type";
        public static final String target_gender = "target_gender";
        public static final String IS_PURCHASED = "is_purchase";// LongLT 19Sep2016 /////////////////////////// START #4597
        public static final String haveEmail = "have_email";//linh 10/11/2016
        public static final String registerFromStr = "register_from";
        public static final String registerToStr = "register_to";
        public static final String registerType = "register_type";
        public static final String userType = "user_type";
    }
    
    //HUNGDT add Multi #6374
    public static final String APPLICATION_COLLECTION = "application";

    public class APPLICATION {

        public static final String ID = "_id";
        public static final String DISPLAY_NAME = "display_name";
        public static final String UNIQUE_NAME = "unique_name";
        public static final String ORDER = "order";
        public static final String USER_NUMBER = "user_number";
        public static final String APPLICATION_ID = "application_id";
    }
    
    public static final String UPLOAD_SETTING_COLLECTION = "upload_setting";

    public class UPLOAD_SETTING {

        public static final String ID = "_id";
        public static final String MAX_FILE_SIZE = "max_file_size";
        public static final String MAX_TOTAL_FILE_SIZE = "total_file_size";
        public static final String MAX_IMAGE_FILE = "max_image_number";
        public static final String MAX_VIDEO_FILE = "max_video_number";
        public static final String MAX_AUDIO_FILE = "max_audio_number";
        public static final String MAX_TOTAL_FILE_UPLOAD = "total_file_upload";
        public static final String DEFAULT_AUDIO_IMG = "default_audio_img";
        public static final String MAX_FILE_PER_ALBUM = "max_file_per_album";
        public static final String SHARE_HAS_DELETED_IMG = "share_has_deleted_img";
        public static final String MAX_LENGTH_BUZZ = "max_length_buzz";
    }
    
    public static final String PRIORITIZE_USER_BUZZ_SETTING_COLLECTION = "prioritize_user_buzz_setting";

    public class PRIORITIZE_USER_BUZZ_SETTING {

        public static final String ID = "_id";
        public static final String LIST_USER_ID = "list_user";
        public static final String LIST_BUZZ_ID = "list_buzz";
        public static final String TAKE_BUZZ = "take_buzz";
        public static final String SKIP_BUZZ = "skip_buzz";
    }
}
