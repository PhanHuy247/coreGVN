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
public class CMcodedbKey {

    public static final String DB_NAME = "cmcodedb";
    public static final String CM_CODE_COLLECTION = "cm_code";

    public class CM_CODE {

        public static final String ID = "_id";
        public static final String AFFICIATE_ID = "afficiate_id";
        public static final String MEDIA_ID = "media_id";
        public static final String CODE = "code";
        public static final String MONEY = "money";
        public static final String REDIRECT_URL = "redirect_url";
        public static final String REGISTER_URL = "reg_url";
        public static final String TYPE = "type";
        public static final String SALE_URL = "sale_url";
        public static final String DESCRIPTION = "des";
        public static final String FLAG = "flag";
    }

    public static final String CM_CODE_STATISTIC_COLLECTION = "cm_code_statistic";

    public class CM_CODE_STATISTIC {

        public static final String ID = "_id";
        public static final String CM_CODE = "cm_code";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String ANDROID_REGISTER_TIMES = "android_reg_times";
        public static final String ANDROID_LOGIN_TIMES = "android_log_times";
        public static final String ANDROID_PURCHASE_TIMES = "android_pur_times";
        public static final String ANDROID_PURCHASE_MONEY = "android_pur_money";
        public static final String IOS_REGISTER_TIMES = "ios_reg_times";
        public static final String IOS_LOGIN_TIMES = "ios_log_times";
        public static final String IOS_PURCHASE_TIMES = "ios_pur_times";
        public static final String IOS_PURCHASE_MONEY = "ios_pur_money";
        public static final String IOS_INSTALL_TIMES = "ios_install_times";
        public static final String ANDROID_INSTALL_TIMES = "adroid_install_times";
        public static final String IOS_INSTALL_LIST = "ios_install_list";
        public static final String ANDROID_INSTALL_LIST = "android_install_list";
        public static final String UNIQUE_NUMBER = "unique_number";
        public static final String TIME_INSTALL = "time_install";
    }

    public static final String AFFICIATE_COLLECTION = "afficiate";

    public class AFFICIATE {

        public static final String ID = "_id";
        public static final String AFFICIATE_NAME = "afficiate_name";
        public static final String AFFICIATE_LOGIN_ID = "aff_login_id";
        public static final String AFFICIATE_PASSWORD = "afficiate_pwd";
        public static final String AFFICIATE_EMAIL = "afficiate_email";
        public static final String FLAG = "flag";
    }

    public static final String MEDIA_COLLECTION = "media";

    public class MEDIA {

        public static final String ID = "_id";
        public static final String AFFICIATE_ID = "afficiate_id";
        public static final String MEDIA_NAME = "media_name";
        public static final String MEDIA_URL = "media_url";
        public static final String FLAG = "flag";
    }

    //HUNGDT add 16-04-2016
    public static final String ADJUST_CMCODE_COLLECTION = "adjust_cmcode";

    public class ADJUST_CMCODE {

        public static final String ID = "_id";
        public static final String DEVICETYPE = "deviceType";
        public static final String DEVICEID = "deviceId";
        public static final String TIME = "time";
        public static final String TRACKERID = "tracker_id";
        public static final String Label = "label";
        public static final String FLAG = "flag";
    }
    //END
}
