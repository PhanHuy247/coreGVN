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
public class StatisticdbKey {

    public static String DB_NAME = "statisticdb";

    public static final String BACKUP_STATISTIC_COLLECTION = "backup_statistic";

    public class BACKUP_STATISTIC {

        public static final String ID = "_id";
        public static final String STATISTIC_TYPE = "statistic_type";
        public static final String LASTEST_DAY = "lst_day";
        public static final String ACTIVE_IOS_LIST = "act_ios_lst";
        public static final String ACTIVE_ANDROID_LIST = "act_android_lst";
    }

    public static final String TRANSACTION_STATISTIC_COLLECTION = "transaction_statistic";

    public class TRANSACTION_STATISTIC {

        public static final String ID = "_id";
        public static final String DAY = "day";
        public static final String LIST_HOUR = "lst_hour";
        public static final String HOUR = "hour";
        public static final String IOS_MONEY = "ios_money";
        public static final String IOS_TIMES = "ios_time";
        public static final String ANDROID_MONEY = "android_money";
        public static final String ANDROID_TIMES = "android_time";
        public static final String AMAZON_MONEY = "amazon_money";
        public static final String AMAZON_TIMES = "amazon_time";
        public static final String CREDIT_MONEY = "credit_money";
        public static final String CREDIT_TIMES = "credit_time";
        public static final String BITCASH_MONEY = "bitcash_money";
        public static final String BITCASH_TIMES = "bitcash_time";
        public static final String C_CHECK_MONEY = "c_check_money";
        public static final String C_CHECK_TIMES = "c_check_time";
        public static final String CONBONI_MONEY = "conboni_money";
        public static final String CONBONI_TIMES = "conboni_time";
        public static final String POINT_BACK_MONEY = "point_back_money";
        public static final String POINT_BACK_TIMES = "point_back_time";
    }
    
     public static final String USER_DAILY_STATISTIC_COLLECTION = "user_daily_statistic";

    public class USER_DAILY_STATISTIC {

        public static final String ID = "_id";
        public static final String DAY = "day";
        public static final String LIST_HOUR = "lst_hour";
        public static final String HOUR = "hour";
        public static final String ONLINE_USER_IOS = "onl_user_ios";
        public static final String ONLINE_USER_FEMALE_IOS = "onl_user_female_ios";
        public static final String ONLINE_USER_MALE_IOS = "onl_user_male_ios";
        public static final String ONLINE_USER_ANDROID = "onl_user_android";
        public static final String ONLINE_USER_FEMALE_ANDROID = "onl_user_female_android";
        public static final String ONLINE_USER_MALE_ANDROID = "onl_user_male_android";
        public static final String NEW_USER_IOS = "new_user_ios";
        public static final String NEW_USER_FEMALE_IOS = "new_user_female_ios";
        public static final String NEW_USER_MALE_IOS = "new_user_male_ios";
        public static final String NEW_USER_ANDROID = "new_user_android";
        public static final String NEW_USER_FEMALE_ANDROID = "new_user_female_android";
        public static final String NEW_USER_MALE_ANDROID = "new_user_male_android";
        public static final String NEW_USER_WEB = "new_user_web";
        public static final String NEW_USER_FEMALE_WEB = "new_user_female_web";
        public static final String NEW_USER_MALE_WEB = "new_user_male_web";
        public static final String ACTIVE_USER_IOS = "active_user_ios";
        public static final String ACTIVE_USER_ANDROID = "active_user_android";
        public static final String VIDEO_CALL = "video_call";
        public static final String VOICE_CALL = "voice_call";
        public static final String TOTAL_USER = "total_user";
    }
    
    public static final String USER_MONTH_STATISTIC_COLLECTION = "user_month_statistic";

    public class USER_MONTH_STATISTIC {

        public static final String ID = "_id";
        public static final String MONTH = "month";
        public static final String LIST_DAY = "lst_day";
        public static final String DAY = "day";
        public static final String ONLINE_USER_IOS = "onl_user_ios";
        public static final String ONLINE_USER_FEMALE_IOS = "onl_user_female_ios";
        public static final String ONLINE_USER_MALE_IOS = "onl_user_male_ios";
        public static final String ONLINE_USER_ANDROID = "onl_user_android";
        public static final String ONLINE_USER_FEMALE_ANDROID = "onl_user_female_android";
        public static final String ONLINE_USER_MALE_ANDROID = "onl_user_male_android";
        public static final String NEW_USER_IOS = "new_user_ios";
        public static final String NEW_USER_FEMALE_IOS = "new_user_female_ios";
        public static final String NEW_USER_MALE_IOS = "new_user_male_ios";
        public static final String NEW_USER_ANDROID = "new_user_android";
        public static final String NEW_USER_FEMALE_ANDROID = "new_user_female_android";
        public static final String NEW_USER_MALE_ANDROID = "new_user_male_android";
        public static final String NEW_USER_WEB = "new_user_web";
        public static final String NEW_USER_FEMALE_WEB = "new_user_female_web";
        public static final String NEW_USER_MALE_WEB = "new_user_male_web";
        public static final String ACTIVE_USER_IOS = "active_user_ios";
        public static final String ACTIVE_USER_ANDROID = "active_user_android";
        public static final String VIDEO_CALL = "video_call";
        public static final String VOICE_CALL = "voice_call";
        
        public static final String TOTAL_USER = "total_user";
    }
    
    public static final String INSTALLATION_STATISTIC_COLLECTION = "installation_statistic";

    public class INSTALLATION_STATISTIC {

        public static final String ID = "_id";
        public static final String HOUR = "day";
        public static final String DEVICE_TYPE = "device_type";
        public static final String UNIQUE_NUMBER = "unique_number";
        public static final String UNIQUE_NUMBER_TYPE = "unique_number_type";
    }    
}
