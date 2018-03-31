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
public class StampdbKey {

    public static String DB_NAME = "stampdb";
    
    public static final String GIFT_COLLECTION = "gift";

    public class GIFT {

        public static final String ID = "_id";
        public static final String CATEGORY_ID = "cat_id";
        public static final String PRICE = "gift_pri";
        public static final String GIFT_INFOR = "gift_inf";
        public static final String JAPANESE_NAME = "jp_name";
        public static final String ENGLISH_NAME = "en_name";
        public static final String FLAG = "flag";
        public static final String ORDER = "order";
    }

    public static final String BUY_STICKER_LOG_COLLECTION = "buy_sticker_log";

    public class BUY_STICKER_LOG {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String TRANSACTION_ID = "transaction_id";
        public static final String UNIQUE_IDENTIFIER = "unique_identifier";
        public static final String PRODUCTION_TYPE = "production_type";
        public static final String TIME = "time";
        public static final String TIME_PURCHASE = "time_purchase";
    }

    public static final String STICKER_CATEGORY_COLLECTION = "sticker_cat";

    public class STICKER_CATEGORY {

        public static final String ID = "_id";
        public static final String JAPANESE_NAME = "jp_name";
        public static final String ENGLISH_NAME = "en_name";
        public static final String CATEGORY_PRICE = "cat_price";
        public static final String CATEGORY_TYPE = "cat_type";
        public static final String JAPANESE_DESCRIPTION = "jp_des";
        public static final String ENGLISH_DESCRIPTION = "en_des";
        public static final String LIVE_TIME = "live_time";
        public static final String ORDER = "order";
        public static final String VERSION = "version";
        public static final String STICKER_NUMBER = "stk_num";
        public static final String DOWNLOAD_NUMBER = "download_num";
        public static final String NEW_FLAG = "new_flag";
        public static final String PUBLIC_FLAG = "public_flag";
        public static final String APPLE_PRODUCTION_ID = "apple_id";
        public static final String GOOGLE_PRODUCTION_ID = "google_id";
        public static final String FLAG = "flag";
    }

    public static final String STICKER_COLLECTION = "sticker";

    public class STICKER {

        public static final String ID = "_id";
        public static final String CODE = "code";
        public static final String CATEGORY_ID = "cat_id";
        public static final String ORDER = "order";
        public static final String FLAG = "flag";
    }

    public static final String GIFT_CATEGORY_COLLECTION = "gift_cat";

    public class GIFT_CATEGORY {

        public static final String ID = "_id";
        public static final String JAPANESE_NAME = "jp_name";
        public static final String ENGLISH_NAME = "en_name";
        public static final String GIFT_NUMBER = "gift_num";
        public static final String ORDER = "order";
        public static final String FLAG = "flag";
    }
    
    public static final String EMOJI_CATEGORY_COLLECTION = "emoji_cat";
    
    public class EMOJI_CATEGORY {
        
        public static final String ID = "_id";
        public static final String VIET_NAME = "vn_name";
        public static final String VIET_DESCRIPTION = "vn_des";
        public static final String ENGLISH_NAME = "en_name";
        public static final String ENGLISH_DESCRIPTION = "en_des";
        public static final String EMOJI_NUMBER = "emoji_number";
        public static final String ORDER = "order";
        public static final String FILE_ID = "file_id";
        public static final String VERSION = "version";
        public static final String FLAG = "flag";
    }
    
    public static final String EMOJI_COLLECTION = "emoji";
    
    public class EMOJI {
        
        public static final String ID = "_id";
        public static final String CATEGORY_ID = "cat_id";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String ORDER = "order";
        public static final String FILE_ID = "file_id";
        public static final String FILE_TYPE = "file_type";
        public static final String FLAG = "flag";
        public static final String WARNING_FLAG = "warning_flag"; //cờ cảnh báo code của emoji hiện tại bị trùng với một emoji trong category khác
    }

}
