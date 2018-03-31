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
public class CashdbKey {

    public static final String DB_NAME = "cashdb";

    public static final String POINT_PACKET_COLLECTION = "point_packet";

    public class POINT_PACKET {

        public static final String ID = "_id";
        public static final String PRICE = "pri";
        public static final String POINT = "point";
        public static final String DESCRIPTION = "des";
        public static final String TYPE = "type";
        public static final String PRODUCTION_ID = "production_id";
        public static final String FLAG = "flag";
        public static final String APPLICATION_ID = "application_id";
    }

    public static final String ACTION_POINT_PACKET_COLLECTION = "action_point_packet";

    public class ACTION_POINT_PACKET {

        public static final String ID = "_id";
        public static final String PRICE = "pri";
        public static final String POINT = "point";
        public static final String FIRST_PURCHASE_POINT = "first_purchase_point";
        public static final String FIRST_PURCHASE_DESC = "first_purchase_description";
//        public static final String DESCRIPTION = "des";
        public static final String TYPE = "type";
        public static final String PRODUCTION_ID = "production_id";
        public static final String USE_CHAT = "use_chat";
        public static final String CHAT_TEXT = "chat_text";
        public static final String USE_GIFT = "use_gift";
        public static final String GIFT_TEXT = "gift_text";
        public static final String USE_VOICE_CALL = "use_voice_call";
        public static final String VOICE_CALL_TEXT = "voice_call_text";
        public static final String USE_SUB_COMMENT = "use_sub_comment";
        public static final String SUB_COMMENT_TEXT = "sub_comment_text";
        public static final String USE_COMMENT = "use_comment";
        public static final String COMMENT_TEXT = "comment_text";
        public static final String USE_VIDEO_CALL = "use_video_call";
        public static final String VIDEO_CALL_TEXT = "video_call_text";
        public static final String USE_UNLOCK_BACKSTAGE = "use_unlock_backstage";
        public static final String UNLOCK_BACKSTAGE_TEXT = "unlock_backstage_text";
        public static final String USE_SAVE_IMAGE = "use_save_image";
        public static final String SAVE_IMAGE_TEXT = "save_image_text";
        public static final String USE_OTHER = "use_other";
        public static final String OTHER_TEXT = "other_text";
        public static final String FLAG = "flag";
        public static final String APPLICATION_ID = "application_id";
    }

    public static final String PRODUCTION_ID_COLLECTION = "production_id";

    public class PRODUCTION_ID {

        public static final String ID = "_id";
        public static final String APPLE_PRODUCTION_ID = "apple_id";
        public static final String GOOGLE_PRODUCTION_ID = "google_id";
    }

    public static final String TRANSACTION_LOG_COLLECTION = "transaction_log";

    public class TRANSACTION_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TRANSACTION_ID = "transaction_id";
        public static final String UNIQUE_IDENTIFIER = "unique_identifier";
        public static final String PRODUCTION_TYPE = "production_type";
        public static final String IS_REAL_TRANSACTION = "is_real_transaction";
        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String TIME_PURCHASE = "time_purchase";
        public static final String TOTAL_PRICE = "total_point";
        public static final String PRICE = "price";
        public static final String IP = "ip";
        public static final String INTERNAL_TRANSACTION_ID = "internal_transaction_id";
        public static final String SUCCESS_FLAG = "success_flag";

        public static final String ADD_BY_ADMIN_FLAG = "add_by_admin_flag";// LongLT 8/8/2016
        public static final String APPLICATION_ID = "application_id";
        public static final String ADDED_TO_TOTAL = "added_to_total";

    }

    public static final String PURCHASE_LOG_COLLECTION = "purchase_log";

    public class PURCHASE_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String PACKET_ID = "packet_id";
        public static final String APPLICATION_ID = "application_id";
    }
}
