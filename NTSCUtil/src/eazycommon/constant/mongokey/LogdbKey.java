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
public class LogdbKey {

    public static String DB_NAME = "logdb";

    public static final String LOG_CALL_COLLECTION = "log_call";

    public class LOG_CALL {

        public static final String ID = "_id";
        public static final String CALL_ID = "call_id";
        public static final String CALL_TYPE = "call_type";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String DURATION = "duration";
        public static final String IP = "ip";
        public static final String PARTNER_RESPOND = "partner_respond";
        public static final String FINISH_FLAG = "finish_flag";
        public static final String FINISH_TYPE = "finish_type";
    }
    public static final String LOG_NOTIFICATION_COLLECTION = "log_notification";

    public class LOG_NOTIFICATION {

        public static final String ID = "_id";
        public static final String TO_USER_ID = "user_id";
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String FROM_USER_ID = "partner_id";
        public static final String IP = "ip";
    }

    public static final String LOG_POINT_COLLECTION = "log_point";
    public class LOG_POINT {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TYPE = "type";
        public static final String POINT = "point";
        public static final String BEFORE_POINT = "before_point";
        public static final String AFTER_POINT = "after_point";
        public static final String TIME = "time";
        public static final String ORIGINAL_TIME = "original_time";
        public static final String PARTNER_ID = "partner_id";
        public static final String SALE_TYPE = "sale_type";
        public static final String FREE_POINT_TYPE = "free_point_type";
        public static final String IP = "ip";
        public static final String ADDED_TO_TOTAL = "added_to_total";
    }

    public class PURPOSE_TYPE
    {
          public static final int TRADE_PONT_TO_MONEY = 38;
    }
    
    public static final String LOG_LOGIN_COLLECTION = "log_login";

    public class LOG_LOGIN {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_LOOK_COLLECTION = "log_look";

    public class LOG_LOOK {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_ONLINE_ALERT_COLLECTION = "log_online_alert";

    public class LOG_ONLINE_ALERT {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String ALERT_TYPE = "alt_type";
        public static final String ALERT_FREQUENCY = "alt_fre";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_BLOCK_COLLECTION = "log_block";

    public class LOG_BLOCK {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }
    
    public static final String LOG_DEACTIVATE_COLLECTION = "log_deactivate";

    public class LOG_DEACTIVATE {

        public static final String ID = "_id";
        public static final String USER_ID = "req_id";
        public static final String TIME = "time";
        public static final String COMMENT = "comment";
        public static final String IP = "ip";
    }

    public static final String LOG_CHECK_OUT_COLLECTION = "log_check_out";

    public class LOG_CHECK_OUT {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_FAVOURIST_COLLECTION = "log_favourist";

    public class LOG_FAVOURIST {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_WINK_COLLECTION = "log_wink";

    public class LOG_WINK {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_SHAKE_CHAT_COLLECTION = "log_shake_chat";

    public class LOG_SHAKE_CHAT {

        public static final String ID = "_id";
        public static final String REQUEST_ID = "req_id";
        public static final String PARTNER_ID = "partner_id";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String LOG_WINK_BOMB_COLLECTION = "log_wink_bomb";

    public class LOG_WINK_BOM {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BOMB_NUMBER = "bomb_num";
        public static final String POINT = "point";
        public static final String MESSAGE = "mess_type";
        public static final String LIST_RECIVER = "lst_rcv";
        public static final String TIME = "time";
        public static final String IP = "ip";
    }

    public static final String USER_REPORT_COLLECTION = "user_report";

    public class USER_REPORT {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String REPORT_ID = "rpt_id";
        public static final String REPORT_TYPE = "rpt_type";
        public static final String REPORT_TIME = "rpt_time";
        public static final String REPORT_IP = "ip";
    }

    public static final String IMAGE_REPORT_COLLECTION = "image_report";

    public class IMAGE_REPORT {

        public static final String ID = "_id";
        public static final String IMAGE_ID = "img_id";
        public static final String USER_ID = "user_id";
        public static final String IMAGE_TYPE = "img_type";
        public static final String REPORT_ID = "rpt_id";
        public static final String REPORT_TYPE = "rpt_type";
        public static final String REPORT_TIME = "rpt_time";
        public static final String REPORT_IP = "ip";
    }
    
    public static final String VIDEO_REPORT_COLLECTION = "video_report";

    public class VIDEO_REPORT {

        public static final String ID = "_id";
        public static final String VIDEO_ID = "file_id";
        public static final String VIDEO_TYPE = "video_type";
        public static final String USER_ID = "user_id";
        public static final String REPORT_ID = "rpt_id";
        public static final String REPORT_TYPE = "rpt_type";
        public static final String REPORT_TIME = "rpt_time";
        public static final String REPORT_IP = "ip";
    }
    
    public static final String AUDIO_REPORT_COLLECTION = "audio_report";

    public class AUDIO_REPORT {

        public static final String ID = "_id";
        public static final String AUDIO_ID = "file_id";
        public static final String AUDIO_TYPE = "audio_type";
        public static final String USER_ID = "user_id";
        public static final String REPORT_ID = "rpt_id";
        public static final String REPORT_TYPE = "rpt_type";
        public static final String REPORT_TIME = "rpt_time";
        public static final String REPORT_IP = "ip";
    }

    public static final String TRANSACTION_LOG_COLLECTION = "transaction_log";

    public class TRANSACTION_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TRANSACTION_ID = "transaction_id";
        public static final String UNIQUE_IDENTIFIER = "unique_identifier";
        public static final String PRODUCTION_TYPE = "production_type";
        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String TIME_PURCHASE = "time_purchase";
        public static final String TOTAL_PRICE = "total_point";
        public static final String PRICE = "price";
        public static final String IP = "ip";
    }
    
    public static final String FREE_PAGE_TRANSACTION_LOG_COLLECTION = "free_page_transaction_log";    
    
    public class FREE_PAGE_TRANSACTION_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
//        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String TIME_PURCHASE = "time_purchase";
//        public static final String TOTAL_PRICE = "total_point";
//        public static final String PRICE = "price";
    }
    
    public static final String FREE_POINT_LOG_COLLECTION = "free_point_log";    
    public class FREE_POINT_LOG {
        public static final String USER_ID = "user_id";
        public static final String POINT = "point";
        public static final String TYPE_ID = "type_id";
        public static final String TIME = "time";
    } 
    
    public static final String SALE_POINT_LOG_COLLECTION = "sale_point_log";    
    public class SALE_POINT_LOG {
        public static final String USER_ID = "user_id";
        public static final String POINT = "point";
        public static final String MONEY = "money";
        public static final String TYPE = "type";
        public static final String TIME = "time";
    } 
    
    public static final String INSTALLATION_LOG_COLLECTION = "installation_log";

    public class INSTALLATION_LOG {

        public static final String ID = "_id";
        public static final String HOUR = "hour";
        public static final String DEVICE_TYPE = "device_type";
        public static final String UNIQUE_NUMBER = "unique_number";
    }      
    
    public static final String USER_INFO_LOG_COLLECTION = "user_info_log";

    public class USER_INFO_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String GENDER = "gender";
        public static final String USER_TYPE = "user_type";
        public static final String APPLICATION_ID = "application_id";
        public static final String ABOUT = "about";
        public static final String UPDATE_TIME = "update_time";
        public static final String APPROVED_FLAG = "app_flag";
        public static final String USER_DENY = "user_deny";
    }      
}
