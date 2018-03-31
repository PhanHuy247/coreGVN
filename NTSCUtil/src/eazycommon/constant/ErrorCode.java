/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant;

/**
 *
 * @author DuongLTD
 */
public class ErrorCode {

    // ums
    public static final int SUCCESS = 0;
    public static final int UNKNOWN_ERROR = 1;
    public static final int WRONG_DATA_FORMAT = 2;
    public static final int INVALID_TOKEN = 3;
    public static final int INVALID_ACCOUNT = 9;

    public static final int EMAIL_NOT_FOUND = 10;
    public static final int INVALID_EMAIL = 11;
    public static final int EMAIL_REGISTED = 12;
    public static final int SEND_MAIL_FAIL = 13;
    public static final int INVALID_USER_NAME = 14;
    public static final int INVALID_BIRTHDAY = 15;
    public static final int UNUSABLE_EMAIL = 16;
    public static final int DUPLICATE_USER_NAME = 17;

    public static final int INCORRECT_PASSWORD = 20;
    public static final int INVALID_PASSWORD = 21;
    public static final int PASSWORD_NOT_MATCH = 22;

    public static final int UPLOAD_IMAGE_ERROR = 30;
    public static final int UPLOAD_FILE_ERROR = 35;
    
    public static final int ACCESS_DENIED = 41;
    public static final int INVALID_FILE = 45;
    public static final int FILE_NOT_FOUND = 46;

    public static final int WRONG_VERIFICATION_CODE = 90;
    public static final int USER_NOT_EXIST = 80;
    public static final int LOCKED_USER = 81;

    public static final int LOCK_FEATURE = 50;
    public static final int NOT_ENOUGHT_POINT = 70;
    public static final int PATNER_NOT_ENOUGHT_POINT = 71;
    public static final int CANT_SEND_CALL_REQUEST = 73;
    public static final int BLOCK_USER = 60;
    public static final int WRONG_URL = 13;
    public static final int EMPTY_DATA = 79;
    
    public static final int WATTING_APPROVE = 47;

    //back end
    public static final int NO_CONTENT = 8;
    public static final int OUT_OF_DATE = 9;
    public static final int DISABLE_EMAIL = 15;
    public static final int IMAGE_NOT_FOUND = 30;
    public static final int BUZZ_NOT_FOUND = 40;
    public static final int COMMENT_NOT_FOUND = 43;
    public static final int EXISTS_DATA = 75;
    public static final int CM_CODE_EXISTS = 70;
    public static final int PERMISSION_DENIED = 999;

    //main
    public static final int OUT_OF_DATE_API = 5;
    public static final int UNUSABLE_VERSION_APPLICATION = 6;
    public static final int CHANGE_SETTING_TOKEN = 7;
    public static final int CATEGORY_NAME_EXIST = 70;
    public static final int NO_CHANGE = 4;
    public static final int TRANSACTION_EXIST = 99;
    public static final int NOT_DOWNLOAD = 98;
    
    public static final int WRONG_FILE_TYPE = 97;
    
    public static final int INVALID_APPLICATION_NAME = 7;
    
    //stf
    public static final int MAX_FILE_SIZE = 666;
    public static final int MAX_FILE_NUMBER = 667;
    public static final int MAX_LENGTH_BUZZ = 668;
    
    public static final int TOKEN_NOT_EXIST_DB = 100;
}
