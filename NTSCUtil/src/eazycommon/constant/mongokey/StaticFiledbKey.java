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
public class StaticFiledbKey {

    public static String DB_NAME = "staticfiledb";

    public static final String IMAGE_COLLECTION = "image";

    public class IMAGE {

        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String IS_FREE = "is_free";
        //add by Huy 201705Oct
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
    }

    public static final String THUMBNAIL_COLLECTION = "thumbnail";

    public class THUMBNAIL {

        public static final String ID = "_id";
        public static final String URL = "url";
        //add by Huy 201709Oct
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
    }

    public static final String FILE_COLLECTION = "file";

    public class FILE {

        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String EMAIL = "email";
        public static final String IS_FREE = "is_free";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String TIME = "upload_time";
        public static final String FLAG = "flag";
        public static final String VIDEO_STATUS = "video_status";
        public static final String FILE_DURATION = "file_duration";
    }
    
    public static final String FILE_CHAT_COLLECTION = "file_chat";

    public class FILE_CHAT {

        public static final String ID = "_id";
        public static final String THUMBNAIL_HEIGHT = "thumbnail_height";
        public static final String THUMBNAIL_URL = "thumbnail_url";
        public static final String THUMBNAIL_WIDTH = "thumbnail_width";
        public static final String ORIGINAL_URL = "original_url";
        public static final String ORIGINAL_HEIGHT = "original_height";
        public static final String ORIGINAL_WIDTH = "original_width";
        public static final String FILE_ID = "file_id";
        public static final String FILE_DURATION = "file_duration";
        public static final String FILE_URL = "file_url";
        public static final String FILE_TYPE = "file_type";
        public static final String TIME_SENDER = "time_sender";
        public static final String FROM = "from";
        public static final String TO = "to";
    }

    public static final String STICKER_COLLECTION = "sticker";

    public class STICKER {

        public static final String ID = "_id";
        public static final String CODE = "code";
        public static final String URL = "url";
    }

    public static final String STICKER_CATEGORY_COLLECTION = "sticker_cat";

    public class STICKER_CATEGORY {

        public static final String ID = "_id";
        public static final String AVATAR_URL = "ava_url";
    }

    public static final String GIFT_COLLECTION = "gift";

    public class GIFT {

        public static final String ID = "_id";
        public static final String URL = "url";
    }

    public static final String NEWS_BANNER_COLLECTION = "news_banner";

    public class NEWS_BANNER {

        public static final String ID = "_id";
        public static final String URL = "url";
    }
    
    public static final String EMOJI_COLLECTION = "emoji";
    
    public class EMOJI {
        
        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String CODE = "code";
    }
}
