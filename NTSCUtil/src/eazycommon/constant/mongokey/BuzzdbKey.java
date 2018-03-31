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
public class BuzzdbKey {

    public static String DB_NAME = "buzzdb";

    public static final String BUZZ_DETAIL_COLLECTION = "buzz_detail";

    public class BUZZ_DETAIL {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BUZZ_VALUE = "buzz_val";
        public static final String BUZZ_TYPE = "buzz_type";
        public static final String LIKE_NUMBER = "like_num";
        public static final String SEEN_NUMBER = "seen_num";
        public static final String COMMNET_NUMBER = "cmt_num";
        public static final String REPORT_NUMBER = "rpt_num";
        public static final String BUZZ_TIME = "buzz_time";
        public static final String POST_TIME = "post_time";
        public static final String LAST_ACTIVITY = "lst_act";
        public static final String FLAG = "flag";
        public static final String IP = "ip";
        public static final String APPROVED_FLAG = "app_flag";
        public static final String USER_DENY = "user_deny";
        public static final String PARENT_ID = "parent_id";
        public static final String FILE_ID = "file_id";
        public static final String PRIVACY = "privacy";
        public static final String COVER_ID = "cover_id";
        public static final String STREAM_ID = "stream_id";
        public static final String VIEW_NUMBER = "view_number";
        public static final String CURRENT_VIEW = "current_view";
        public static final String DURATION = "duration";
        public static final String STREAM_END_TIME = "stream_end_time";
        public static final String STREAM_START_TIME = "stream_start_time";
        public static final String STREAM_STATUS = "stream_status";
        public static final String SHARE_NUMBER = "share_number";
        public static final String SHARE_ID = "share_id";
        public static final String BUZZ_REGION = "buzz_region";
    }
    
    public static final String REVIEWING_BUZZ_COLLECTION = "reviewing_buzz";

    public class REVIEWING_BUZZ {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BUZZ_VALUE = "buzz_val";
        public static final String BUZZ_TYPE = "buzz_type";
        public static final String BUZZ_TIME = "buzz_time";
        public static final String IP = "ip";
        public static final String USER_DENY = "user_deny";
    }
    
    public static final String REVIEWING_COMMENT_COLLECTION = "reviewing_comment";
    
    public class REVIEWING_COMMENT {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String COMMENT_ID = "comment_id";
        public static final String VALUE = "value";
        public static final String REVIEWING_SUB_COMMENT_NUMBER = "reviewing_sub_comment_number";
        public static final String TIME = "time";
        public static final String IP = "ip";
        public static final String REVIEW_FLAG = "review_flag";
        public static final String APPEAR_FLAG = "appear_flag";
        public static final String ACTION_TIME = "action_time";
    }
    
    public static final String REVIEWING_SUB_COMMENT_COLLECTION = "reviewing_sub_comment";
    
    public class REVIEWING_SUB_COMMENT {

        public static final String ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String COMMENT_ID = "comment_id";
        public static final String SUB_COMMENT_ID = "sub_comment_id";
        public static final String VALUE = "value";
        public static final String TIME = "time";
        public static final String IP = "ip";
        public static final String APPEAR_FLAG = "appear_flag";
    }

    public static final String BUZZ_COMMENT_COLLECTION = "buzz_comment";

    public class BUZZ_COMMENT {

        public static final String ID = "_id";
        public static final String COMMENT_LIST = "comment_lst";
        public static final String COMMENT_ID = "cmt_id";
        public static final String USER_ID = "user_id";
        public static final String FLAG = "flag";
        public static final String APPROVE_FLAG = "approve_flag";
    }

    public static final String USER_BUZZ_COLLECTION = "user_buzz";

    public class USER_BUZZ {

        public static final String ID = "_id";
        public static final String BUZZ_LIST = "buzz_lst";
        public static final String BUZZ_ID = "buzz_id";
        public static final String BUZZ_TYPE = "buzz_type";
        public static final String POST_TIME = "post_time";
        public static final String BUZZ_TIME = "buzz_time";
        public static final String FLAG = "flag";
        public static final String APPROVED_FLAG = "app_flag";
        public static final String USER_DENY = "user_deny";
        public static final String PRIVACY = "privacy";
        public static final String STREAM_STATUS = "stream_status";
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

    public static final String USER_STATUS_COLLECTION = "user_status";

    public class USER_STATUS {

        public static final String ID = "_id";
        public static final String BUZZ_ID = "buzz_id";
    }

    public static final String COMMENT_DETAIL_COLLECTION = "comment_detail";
    
    public class COMMENT_DETAIL {

        public static final String ID = "_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String USER_ID = "user_id";
        public static final String IS_DEL = "is_del";
        public static final String SUB_COMMENT_NUMBER = "sub_comment_number";
        public static final String COMMENT_VALUE = "cmt_val";
        public static final String COMMENT_TIME = "cmt_time";
        public static final String COMMENT_FLAG = "cmt_flag";
        public static final String APPROVE_FLAG = "approve_flag";
        public static final String USER_DENY = "user_deny";

    }
    
    public static final String SUB_COMMENT_DETAIL_COLLECTION = "sub_comment_detail";
    
    public class SUB_COMMENT_DETAIL {

        public static final String ID = "_id";
        public static final String BUZZ_ID = "buzz_id";
        public static final String COMMENT_ID = "comment_id";
        public static final String USER_ID = "user_id";
        public static final String VALUE = "value";
        public static final String TIME = "time";
        public static final String FLAG = "flag";
        public static final String APPROVE_FLAG = "approve_flag";

    }    
    
    public static final String SUB_COMMENT_LIST_COLLECTION = "sub_comment_list";

    public class SUB_COMMENT_LIST {

        public static final String ID = "_id";
        public static final String SUB_COMMENT_LIST = "sub_comment_lst";
        public static final String SUB_COMMENT_ID = "sub_comment_id";
        public static final String USER_ID = "user_id";
        public static final String FLAG = "flag";
        public static final String APPROVE_FLAG = "approve_flag";
    }    
    
    public static final String BUZZ_TAG_COLLECTION = "buzz_tag";

    public class BUZZ_TAG {

        public static final String ID = "_id";
        public static final String TAG_LIST = "tag_lst";
        public static final String USER_ID = "user_id";
        public static final String TAG_FLAG = "tag_flag";
    }
    
    public static final String BUZZ_VIEW_COLLECTION = "buzz_view";
    
    public class BUZZ_VIEW {
        
        public static final String ID = "_id";
        public static final String VIEW_LIST = "view_list";
        public static final String VIEW_FROM = "view_from";
        public static final String TYPE = "type";  //ip-id
    }
    
}
