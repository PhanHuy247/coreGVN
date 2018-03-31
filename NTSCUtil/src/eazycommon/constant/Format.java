/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon.constant;

/**
 *
 * @author Rua
 */
public class Format {
    
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_HOUR_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_HOUR_PURCHASE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static final String FACEBOOK_EMAIL_FORMAT = "%s@facebook.com";
    public static final String MOCOM_EMAIL_FORMAT = "%s@mocom.com";
    public static final String FAMU_EMAIL_FORMAT = "%s@famu.com";
    
    public static String EMAIL_REGEX = "^((([0-9a-zA-Z]((\\.(?!\\.))|[-!#\\$%&'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)(?<=[0-9a-zA-Z-])@))((\\[(\\d{1,3}\\.){3}\\d{1,3}\\])|(([0-9a-zA-Z-]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,6}))$";
    //public static final String URL_REGEX = "^(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_=]*)?$";
    //thanhdd fix #6356
    public static final String URL_REGEX =  "^(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_=\\:]*)?$";
}
