/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.csv;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import eazycommon.constant.FilesAndFolders;
import eazycommon.util.Util;
import com.vn.ntsc.backend.entity.impl.chat.Message;

/**
 *
 * @author RuAc0n
 */
public class TypeValue {
    
    
    public static final Map<Integer, String> en_user_type = new TreeMap<>();
    public static final Map<Integer, String> jp_user_type = new TreeMap<>();
    private static void initUserType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_EMAIL).trim();
        CSVUtils.addMap(0, value, en_user_type);
        value = prop.getProperty(TypeKeys.EN_FACEBOOK).trim();
        CSVUtils.addMap(1, value, en_user_type);
        value = prop.getProperty(TypeKeys.EN_MOCOM).trim();
        CSVUtils.addMap(2, value, en_user_type);
        value = prop.getProperty(TypeKeys.EN_FAMU).trim();
        CSVUtils.addMap(3, value, en_user_type);
        
        value = prop.getProperty(TypeKeys.JP_EMAIL).trim();
        CSVUtils.addMap(0, value, jp_user_type);
        value = prop.getProperty(TypeKeys.JP_FACEBOOK).trim();
        CSVUtils.addMap(1, value, jp_user_type);
        value = prop.getProperty(TypeKeys.JP_MOCOM).trim();
        CSVUtils.addMap(2, value, jp_user_type);
        value = prop.getProperty(TypeKeys.JP_FAMU).trim();
        CSVUtils.addMap(3, value, jp_user_type);
    }

    public static final Map<Integer, String> en_success_type = new TreeMap<>();
    public static final Map<Integer, String> jp_success_type = new TreeMap<>();
    private static void initSuccessType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_SUCCESS).trim();
        CSVUtils.addMap(1, value, en_success_type);
        value = prop.getProperty(TypeKeys.EN_NOT_SUCCESS).trim();
        CSVUtils.addMap(0, value, en_success_type);

        
        value = prop.getProperty(TypeKeys.JP_SUCCESS).trim();
        CSVUtils.addMap(1, value, jp_success_type);
        value = prop.getProperty(TypeKeys.JP_NOT_SUCCESS).trim();
        CSVUtils.addMap(0, value, jp_success_type);
    }    
    
   public static final Map<Integer, String> en_purchase_type = new TreeMap<>();
   public static final Map<Integer, String> jp_purchase_type = new TreeMap<>();
    private static void initPurchaseType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_APPLE).trim();
        CSVUtils.addMap(0, value, en_purchase_type);
        value = prop.getProperty(TypeKeys.EN_GOOGLE).trim();
        CSVUtils.addMap(1, value, en_purchase_type);

        
        value = prop.getProperty(TypeKeys.JP_APPLE).trim();
        CSVUtils.addMap(0, value, jp_purchase_type);
        value = prop.getProperty(TypeKeys.JP_APPLE).trim();
        CSVUtils.addMap(1, value, jp_purchase_type);
    }    
    
    public static final Map<Integer, String> en_gender = new TreeMap<>();
    public static final Map<Integer, String> jp_gender = new TreeMap<>();
    private static void initGender(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_GENDER_MALE).trim();
        CSVUtils.addMap(0, value, en_gender);
        value = prop.getProperty(TypeKeys.EN_GENDER_FEMALE).trim();
        CSVUtils.addMap(1, value, en_gender);
        value = prop.getProperty(TypeKeys.EN_GENDER_OTHER).trim();
        CSVUtils.addMap(2, value, en_gender);        
        
        value = prop.getProperty(TypeKeys.JP_GENDER_MALE).trim();
        CSVUtils.addMap(0, value, jp_gender);
        value = prop.getProperty(TypeKeys.JP_GENDER_FEMALE).trim();
        CSVUtils.addMap(1, value, jp_gender);
        value = prop.getProperty(TypeKeys.JP_GENDER_OTHER).trim();
        CSVUtils.addMap(2, value, jp_gender);        
    }     
  
    public static final Map<Integer, String> en_interes_in = new TreeMap<>();
    public static final Map<Integer, String> jp_interes_in = new TreeMap<>();
    private static void initInterestedIn(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_INTERS_IN_MALE).trim();
        CSVUtils.addMap(0, value, en_interes_in);
        value = prop.getProperty(TypeKeys.EN_INTERS_IN_FEMALE).trim();
        CSVUtils.addMap(1, value, en_interes_in);
        value = prop.getProperty(TypeKeys.EN_INTERS_IN_OTHER).trim();
        CSVUtils.addMap(2, value, en_interes_in);        
        
        value = prop.getProperty(TypeKeys.JP_INTERS_IN_MALE).trim();
        CSVUtils.addMap(0, value, jp_interes_in);
        value = prop.getProperty(TypeKeys.JP_INTERS_IN_FEMALE).trim();
        CSVUtils.addMap(1, value, jp_interes_in);
        value = prop.getProperty(TypeKeys.JP_INTERS_IN_OTHER).trim();
        CSVUtils.addMap(2, value, jp_interes_in);
    }
    
    public static final Map<Integer, String> en_relationship = new TreeMap<>();
    public static final Map<Integer, String> jp_relationship = new TreeMap<>();
    private static void initRelationship(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_ASK_ME).trim();
        CSVUtils.addMap(-1, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_SINGLE).trim();
        CSVUtils.addMap(0, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_IN_A_RELATIONSHIP).trim();
        CSVUtils.addMap(1, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_MARRIED).trim();
        CSVUtils.addMap(2, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_IT_COMPLICATE).trim();
        CSVUtils.addMap(3, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_IN_AN_OPEN_RELATIONSHIP).trim();
        CSVUtils.addMap(4, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_SEPARATED).trim();
        CSVUtils.addMap(5, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_DEVORCED).trim();
        CSVUtils.addMap(6, value, en_relationship);
        value = prop.getProperty(TypeKeys.EN_RELSH_STT_IN_A_CIVIL_UNION).trim();
        CSVUtils.addMap(7, value, en_relationship);        
        
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_ASK_ME).trim();
        CSVUtils.addMap(-1, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_SINGLE).trim();
        CSVUtils.addMap(0, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_IN_A_RELATIONSHIP).trim();
        CSVUtils.addMap(1, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_MARRIED).trim();
        CSVUtils.addMap(2, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_IT_COMPLICATE).trim();
        CSVUtils.addMap(3, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_IN_AN_OPEN_RELATIONSHIP).trim();
        CSVUtils.addMap(4, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_SEPARATED).trim();
        CSVUtils.addMap(5, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_DEVORCED).trim();
        CSVUtils.addMap(6, value, jp_relationship);
        value = prop.getProperty(TypeKeys.JP_RELSH_STT_IN_A_CIVIL_UNION).trim();
        CSVUtils.addMap(7, value, jp_relationship);
    }

    public static final Map<Integer, String> en_ethnicity = new TreeMap<>();
    public static final Map<Integer, String> jp_ethnicity = new TreeMap<>();
    private static void initEthnicity(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_ETH_ASK_ME).trim();
        CSVUtils.addMap(-1, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_LATINA_LATINO).trim();
        CSVUtils.addMap(0, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_BLACK_AFICAN).trim();
        CSVUtils.addMap(1, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_NATIVE_ABORIGINAL).trim();
        CSVUtils.addMap(2, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_ASIAN).trim();
        CSVUtils.addMap(3, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_EAST_INDIAN).trim();
        CSVUtils.addMap(4, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_PACIFIC).trim();
        CSVUtils.addMap(5, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_WHITE_CAUCASIAN).trim();
        CSVUtils.addMap(6, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_MIDDLE_EASTER).trim();
        CSVUtils.addMap(7, value, en_ethnicity);
        value = prop.getProperty(TypeKeys.EN_ETH_MIXED_MULTI).trim();
        CSVUtils.addMap(8, value, en_ethnicity);        
        
        value = prop.getProperty(TypeKeys.JP_ETH_ASK_ME).trim();
        CSVUtils.addMap(-1, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_LATINA_LATINO).trim();
        CSVUtils.addMap(0, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_BLACK_AFICAN).trim();
        CSVUtils.addMap(1, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_NATIVE_ABORIGINAL).trim();
        CSVUtils.addMap(2, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_ASIAN).trim();
        CSVUtils.addMap(3, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_EAST_INDIAN).trim();
        CSVUtils.addMap(4, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_PACIFIC).trim();
        CSVUtils.addMap(5, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_WHITE_CAUCASIAN).trim();
        CSVUtils.addMap(6, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_MIDDLE_EASTER).trim();
        CSVUtils.addMap(7, value, jp_ethnicity);
        value = prop.getProperty(TypeKeys.JP_ETH_MIXED_MULTI).trim();
        CSVUtils.addMap(8, value, jp_ethnicity);        
    }    

    public static final Map<Integer, String> en_flag = new TreeMap<>();
    public static final Map<Integer, String> jp_flag = new TreeMap<>();
    private static void initFlag(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_FLAG_DISABLE).trim();
        CSVUtils.addMap(-1, value, en_flag);
        value = prop.getProperty(TypeKeys.EN_FLAG_DEACTIVE).trim();
        CSVUtils.addMap(0, value, en_flag);
        value = prop.getProperty(TypeKeys.EN_FLAG_ACTIVE).trim();
        CSVUtils.addMap(1, value, en_flag);        
        
        value = prop.getProperty(TypeKeys.JP_FLAG_DISABLE).trim();
        CSVUtils.addMap(-1, value, jp_flag);
        value = prop.getProperty(TypeKeys.JP_FLAG_DEACTIVE).trim();
        CSVUtils.addMap(0, value, jp_flag);
        value = prop.getProperty(TypeKeys.JP_FLAG_ACTIVE).trim();
        CSVUtils.addMap(1, value, jp_flag);
    }    

    public static final Map<Integer, String> en_verify_flag = new TreeMap<>();
    public static final Map<Integer, String> jp_verify_flag = new TreeMap<>();
    private static void initVerifyFlag(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_VERIFICATION_FLAG_NONE).trim();
        CSVUtils.addMap(-2, value, en_verify_flag);
        value = prop.getProperty(TypeKeys.EN_VERIFICATION_FLAG_DENIED).trim();
        CSVUtils.addMap(-1, value, en_verify_flag);
        value = prop.getProperty(TypeKeys.EN_VERIFICATION_FLAG_PENDING).trim();
        CSVUtils.addMap(0, value, en_verify_flag);
        value = prop.getProperty(TypeKeys.EN_VERIFICATION_FLAG_VERIFY).trim();
        CSVUtils.addMap(1, value, en_verify_flag);        
        
        value = prop.getProperty(TypeKeys.JP_VERIFICATION_FLAG_NONE).trim();
        CSVUtils.addMap(-2, value, jp_verify_flag);
        value = prop.getProperty(TypeKeys.JP_VERIFICATION_FLAG_DENIED).trim();
        CSVUtils.addMap(-1, value, jp_verify_flag);
        value = prop.getProperty(TypeKeys.JP_VERIFICATION_FLAG_PENDING).trim();
        CSVUtils.addMap(0, value, jp_verify_flag);
        value = prop.getProperty(TypeKeys.JP_VERIFICATION_FLAG_VERIFY).trim();
        CSVUtils.addMap(1, value, jp_verify_flag);
    }     
    
    public static final Map<Integer, String> en_region = new TreeMap<>();
    public static final Map<Integer, String> jp_region = new TreeMap<>();
    private static void initRegion(Properties prop) throws UnsupportedEncodingException{
        String value;

        value = prop.getProperty(TypeKeys.EN_REGION_HOKKAIDO).trim();
        CSVUtils.addMap(1, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_AOMORI).trim();
        CSVUtils.addMap(2, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_IWATE).trim();
        CSVUtils.addMap(3, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_AKITA).trim();
        CSVUtils.addMap(4, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_MIYAGI).trim();
        CSVUtils.addMap(5, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_YAMAGATA).trim();
        CSVUtils.addMap(6, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_FUKUSHIMA).trim();
        CSVUtils.addMap(7, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_IBARAKI).trim();
        CSVUtils.addMap(8, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_TOCHIGI).trim();
        CSVUtils.addMap(9, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_GUNMA).trim();
        CSVUtils.addMap(10, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_SAITAMA).trim();
        CSVUtils.addMap(11, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_CHIBA).trim();
        CSVUtils.addMap(12, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_TOKYO).trim();
        CSVUtils.addMap(13, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_KANAGAWA).trim();
        CSVUtils.addMap(14, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_YAMANASHI).trim();
        CSVUtils.addMap(15, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_NAGANO).trim();
        CSVUtils.addMap(16, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_NIIGATA).trim();
        CSVUtils.addMap(17, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_TOYAMA).trim();
        CSVUtils.addMap(18, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_ISHIKAWA).trim();
        CSVUtils.addMap(19, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_FUKUI).trim();
        CSVUtils.addMap(20, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_SHIZUOKA).trim();
        CSVUtils.addMap(21, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_AICHI).trim();
        CSVUtils.addMap(22, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_GIFU).trim();
        CSVUtils.addMap(23, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_MIE).trim();
        CSVUtils.addMap(24, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_SHIGA).trim();
        CSVUtils.addMap(25, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_KYOTO).trim();
        CSVUtils.addMap(26, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_OSAKA).trim();
        CSVUtils.addMap(27, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_HYOGO).trim();
        CSVUtils.addMap(28, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_NARA).trim();
        CSVUtils.addMap(29, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_WAKAYAMA).trim();
        CSVUtils.addMap(30, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_SHIMANE).trim();
        CSVUtils.addMap(31, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_TOTORI).trim();
        CSVUtils.addMap(32, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_OKAYAMA).trim();
        CSVUtils.addMap(33, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_HIROSHIMA).trim();
        CSVUtils.addMap(34, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_YAMAGUCHI).trim();
        CSVUtils.addMap(35, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_KAGAWA).trim();
        CSVUtils.addMap(36, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_TOKUSHIMA).trim();
        CSVUtils.addMap(37, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_KOCHI).trim();
        CSVUtils.addMap(38, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_EHIME).trim();
        CSVUtils.addMap(39, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_FUKUOKA).trim();
        CSVUtils.addMap(40, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_SAGA).trim();
        CSVUtils.addMap(41, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_NAGASAKI).trim();
        CSVUtils.addMap(42, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_OITA).trim();
        CSVUtils.addMap(43, value, en_region);        
        value = prop.getProperty(TypeKeys.EN_REGION_KUMAMOTO).trim();
        CSVUtils.addMap(44, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_MIYAZAKI).trim();
        CSVUtils.addMap(45, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_KAGOSHIMA).trim();
        CSVUtils.addMap(46, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_OKINAWA).trim();
        CSVUtils.addMap(47, value, en_region);
        value = prop.getProperty(TypeKeys.EN_REGION_OVERSEA).trim();
        CSVUtils.addMap(48, value, en_region);                
        
        value = prop.getProperty(TypeKeys.JP_REGION_HOKKAIDO).trim();
        CSVUtils.addMap(1, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_AOMORI).trim();
        CSVUtils.addMap(2, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_IWATE).trim();
        CSVUtils.addMap(3, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_AKITA).trim();
        CSVUtils.addMap(4, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_MIYAGI).trim();
        CSVUtils.addMap(5, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_YAMAGATA).trim();
        CSVUtils.addMap(6, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_FUKUSHIMA).trim();
        CSVUtils.addMap(7, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_IBARAKI).trim();
        CSVUtils.addMap(8, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_TOCHIGI).trim();
        CSVUtils.addMap(9, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_GUNMA).trim();
        CSVUtils.addMap(10, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_SAITAMA).trim();
        CSVUtils.addMap(11, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_CHIBA).trim();
        CSVUtils.addMap(12, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_TOKYO).trim();
        CSVUtils.addMap(13, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_KANAGAWA).trim();
        CSVUtils.addMap(14, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_YAMANASHI).trim();
        CSVUtils.addMap(15, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_NAGANO).trim();
        CSVUtils.addMap(16, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_NIIGATA).trim();
        CSVUtils.addMap(17, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_TOYAMA).trim();
        CSVUtils.addMap(18, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_ISHIKAWA).trim();
        CSVUtils.addMap(19, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_FUKUI).trim();
        CSVUtils.addMap(20, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_SHIZUOKA).trim();
        CSVUtils.addMap(21, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_AICHI).trim();
        CSVUtils.addMap(22, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_GIFU).trim();
        CSVUtils.addMap(23, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_MIE).trim();
        CSVUtils.addMap(24, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_SHIGA).trim();
        CSVUtils.addMap(25, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_KYOTO).trim();
        CSVUtils.addMap(26, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_OSAKA).trim();
        CSVUtils.addMap(27, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_HYOGO).trim();
        CSVUtils.addMap(28, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_NARA).trim();
        CSVUtils.addMap(29, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_WAKAYAMA).trim();
        CSVUtils.addMap(30, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_SHIMANE).trim();
        CSVUtils.addMap(31, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_TOTORI).trim();
        CSVUtils.addMap(32, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_OKAYAMA).trim();
        CSVUtils.addMap(33, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_HIROSHIMA).trim();
        CSVUtils.addMap(34, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_YAMAGUCHI).trim();
        CSVUtils.addMap(35, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_KAGAWA).trim();
        CSVUtils.addMap(36, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_TOKUSHIMA).trim();
        CSVUtils.addMap(37, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_KOCHI).trim();
        CSVUtils.addMap(38, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_EHIME).trim();
        CSVUtils.addMap(39, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_FUKUOKA).trim();
        CSVUtils.addMap(40, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_SAGA).trim();
        CSVUtils.addMap(41, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_NAGASAKI).trim();
        CSVUtils.addMap(42, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_OITA).trim();
        CSVUtils.addMap(43, value, jp_region);        
        value = prop.getProperty(TypeKeys.JP_REGION_KUMAMOTO).trim();
        CSVUtils.addMap(44, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_MIYAZAKI).trim();
        CSVUtils.addMap(45, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_KAGOSHIMA).trim();
        CSVUtils.addMap(46, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_OKINAWA).trim();
        CSVUtils.addMap(47, value, jp_region);
        value = prop.getProperty(TypeKeys.JP_REGION_OVERSEA).trim();
        CSVUtils.addMap(48, value, jp_region);         
    }     
    
    public static final Map<Integer, String> en_log_point = new TreeMap<>();
    public static final Map<Integer, String> jp_log_point = new TreeMap<>();
    private static void initLogPoint(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_REGISTER).trim();
        CSVUtils.addMap(1, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_DAILY_BONUS).trim();
        CSVUtils.addMap(2, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_BECAME_FRIEND).trim();
        CSVUtils.addMap(3, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_CASH).trim();
        CSVUtils.addMap(4, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_UNLOCK_BACKSTAGE).trim();
        CSVUtils.addMap(5, value, en_log_point);  
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_UNLOCK_FAVOURITED).trim();
        CSVUtils.addMap(6, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_UNLOCK_CHECK_OUT).trim();
        CSVUtils.addMap(7, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_UNLOCK_BACKSTAGE_XXX).trim();
        CSVUtils.addMap(8, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_SAVE_IMAGE).trim();
        CSVUtils.addMap(9, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_WINK_BOMB).trim();
        CSVUtils.addMap(10, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_SEND_GIFT).trim();
        CSVUtils.addMap(11, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ONLINE_ALERT).trim();
        CSVUtils.addMap(12, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_BID).trim();
        CSVUtils.addMap(13, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADMINISTRATOR).trim();
        CSVUtils.addMap(14, value, en_log_point);  
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_VOICE_CALL).trim();        
        CSVUtils.addMap(15, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_VIDEO_CALL).trim();
        CSVUtils.addMap(16, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_CHAT).trim();
        CSVUtils.addMap(17, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_BUY_STICKER).trim();
        CSVUtils.addMap(18, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_WINK).trim(); 
        CSVUtils.addMap(19, value, en_log_point);
//        value = prop.getProperty(TypeKeys.EN_LOG_POINT_VIEW_IMAGE_XXX).trim(); 
//        Util.addMap(20, value, en_log_point);
//        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_VIEW_YOUR_IMAGE).trim();
//        Util.addMap(21, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_RECEIVE_GIFT_FROM_XXX).trim();
        CSVUtils.addMap(22, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_SEND_STICKER_TO_XXX).trim();
        CSVUtils.addMap(23, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_RECEIVE_STICKER_FROM_XXX).trim(); 
        CSVUtils.addMap(24, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_SAVE_YOUR_IMAGE).trim(); 
        CSVUtils.addMap(25, value, en_log_point);         
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADVERTISEMENT).trim();
        CSVUtils.addMap(26, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_BUZZ).trim();
        CSVUtils.addMap(27, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_LOOKING_FOR).trim();
        CSVUtils.addMap(28, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_ABOUT_ME).trim(); 
        CSVUtils.addMap(29, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_AVATAR).trim(); 
        CSVUtils.addMap(30, value, en_log_point);         
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_RELATIONSHIP).trim();
        CSVUtils.addMap(31, value, en_log_point);        
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_BODY_TYPE).trim();
        CSVUtils.addMap(32, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_HEIGHT).trim();
        CSVUtils.addMap(33, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_ETHNICITY).trim(); 
        CSVUtils.addMap(34, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_INTERES).trim(); 
        CSVUtils.addMap(35, value, en_log_point); 
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_RECEIVE_WINK).trim(); 
        CSVUtils.addMap(36, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_RECEIVE_CHAT).trim(); 
        CSVUtils.addMap(37, value, en_log_point); 
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_TRADE_POINT).trim(); 
        CSVUtils.addMap(38, value, en_log_point); 
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_COMMENT_BUZZ_XXX).trim(); 
        CSVUtils.addMap(39, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_COMMENT_YOUR_BUZZ).trim(); 
        CSVUtils.addMap(40, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_FREE_POINT).trim(); 
        CSVUtils.addMap(41, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_POINT_BY_SALE).trim(); 
        CSVUtils.addMap(42, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_VIEW_IMAGE).trim(); 
        CSVUtils.addMap(43, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_VIEW_YOUR_IMAGE).trim();
        CSVUtils.addMap(44, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_WATCH_VIDEO).trim(); 
        CSVUtils.addMap(45, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_WATCH_YOUR_VIDEO).trim();
        CSVUtils.addMap(46, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_LISTEN_AUDIO).trim(); 
        CSVUtils.addMap(47, value, en_log_point);
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_LISTEN_YOUR_AUDIO).trim();
        CSVUtils.addMap(48, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_ADD_BY_ADMIN_IN_LIST).trim();
        CSVUtils.addMap(49, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_REPLY_XXX_COMMENT).trim();
        CSVUtils.addMap(50, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_XXX_REPLY_YOUR_COMMENT).trim();
        CSVUtils.addMap(51, value, en_log_point);     
        value = prop.getProperty(TypeKeys.EN_LOG_POINT_REPAY).trim();
        CSVUtils.addMap(52, value, en_log_point);     
        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_REGISTER).trim();
        CSVUtils.addMap(1, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_DAILY_BONUS).trim();
        CSVUtils.addMap(2, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_BECAME_FRIEND).trim();
        CSVUtils.addMap(3, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_CASH).trim();
        CSVUtils.addMap(4, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_UNLOCK_BACKSTAGE).trim();
        CSVUtils.addMap(5, value, jp_log_point);  
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_UNLOCK_FAVOURITED).trim();
        CSVUtils.addMap(6, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_UNLOCK_CHECK_OUT).trim();
        CSVUtils.addMap(7, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_UNLOCK_BACKSTAGE_XXX).trim();
        CSVUtils.addMap(8, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_SAVE_IMAGE).trim();
        CSVUtils.addMap(9, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_WINK_BOMB).trim();
        CSVUtils.addMap(10, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_SEND_GIFT).trim();
        CSVUtils.addMap(11, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ONLINE_ALERT).trim();
        CSVUtils.addMap(12, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_BID).trim();
        CSVUtils.addMap(13, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADMINISTRATOR).trim();
        CSVUtils.addMap(14, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_VOICE_CALL).trim();        
        CSVUtils.addMap(15, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_VIDEO_CALL).trim();
        CSVUtils.addMap(16, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_CHAT).trim();
        CSVUtils.addMap(17, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_BUY_STICKER).trim();
        CSVUtils.addMap(18, value, jp_log_point);    
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_WINK).trim(); 
        CSVUtils.addMap(19, value, jp_log_point);
//        value = prop.getProperty(TypeKeys.JP_LOG_POINT_VIEW_IMAGE_XXX).trim(); 
//        Util.addMap(20, value, jp_log_point);
//        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_VIEW_YOUR_IMAGE).trim();
//        Util.addMap(21, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_RECEIVE_GIFT_FROM_XXX).trim();
        CSVUtils.addMap(22, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_SEND_STICKER_TO_XXX).trim();
        CSVUtils.addMap(23, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_RECEIVE_STICKER_FROM_XXX).trim(); 
        CSVUtils.addMap(24, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_SAVE_YOUR_IMAGE).trim(); 
        CSVUtils.addMap(25, value, jp_log_point);         
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADVERTISEMENT).trim();
        CSVUtils.addMap(26, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_BUZZ).trim();
        CSVUtils.addMap(27, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_LOOKING_FOR).trim();
        CSVUtils.addMap(28, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_ABOUT_ME).trim(); 
        CSVUtils.addMap(29, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_AVATAR).trim(); 
        CSVUtils.addMap(30, value, jp_log_point);         
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_RELATIONSHIP).trim();
        CSVUtils.addMap(31, value, jp_log_point);        
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_BODY_TYPE).trim();
        CSVUtils.addMap(32, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_HEIGHT).trim();
        CSVUtils.addMap(33, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_ETHNICITY).trim(); 
        CSVUtils.addMap(34, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_INTERES).trim(); 
        CSVUtils.addMap(35, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_RECEIVE_WINK).trim(); 
        CSVUtils.addMap(36, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_RECEIVE_CHAT).trim(); 
        CSVUtils.addMap(37, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_TRADE_POINT).trim(); 
        CSVUtils.addMap(38, value, jp_log_point); 
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_COMMENT_BUZZ_XXX).trim(); 
        CSVUtils.addMap(39, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_COMMENT_YOUR_BUZZ).trim(); 
        CSVUtils.addMap(40, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_FREE_POINT).trim(); 
        CSVUtils.addMap(41, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_ADD_POINT_BY_SALE).trim(); 
        CSVUtils.addMap(42, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_VIEW_IMAGE).trim(); 
        CSVUtils.addMap(43, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_VIEW_YOUR_IMAGE).trim();
        CSVUtils.addMap(44, value, jp_log_point);     
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_WATCH_VIDEO).trim(); 
        CSVUtils.addMap(45, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_WATCH_YOUR_VIDEO).trim();
        CSVUtils.addMap(46, value, jp_log_point);     
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_LISTEN_AUDIO).trim(); 
        CSVUtils.addMap(47, value, jp_log_point);
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_LISTEN_YOUR_AUDIO).trim();
        CSVUtils.addMap(48, value, jp_log_point);  
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_BY_ADMIN_IN_LIST).trim();
        CSVUtils.addMap(49, value, jp_log_point);  
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_REPLY_XXX_COMMENT).trim();
        CSVUtils.addMap(50, value, jp_log_point);  
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_XXX_REPLY_YOUR_COMMENT).trim();
        CSVUtils.addMap(51, value, jp_log_point);  
        value = prop.getProperty(TypeKeys.JP_LOG_POINT_REPAY).trim();
        CSVUtils.addMap(52, value, jp_log_point);   
        
    }    
    
    public static final Map<Integer, String> en_log_notification = new TreeMap<>();
    public static final Map<Integer, String> jp_log_notification = new TreeMap<>();
    private static void initLogNotification(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_SOMEONE_CHECK_OUT).trim();
        CSVUtils.addMap(1, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_CHECK_OUT).trim();
        CSVUtils.addMap(2, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_SOMEONE_FAVOURIST).trim();
        CSVUtils.addMap(3, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_FAVOURIST).trim();
        CSVUtils.addMap(4, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_LIKE).trim();
        CSVUtils.addMap(5, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_ALSO_LIKE).trim();
        CSVUtils.addMap(6, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_RESPONSED).trim();
        CSVUtils.addMap(7, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_ALSO_RESPONSED).trim();
        CSVUtils.addMap(8, value, en_log_notification);     
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_UNLOCKED_BACKSTAGE).trim();
        CSVUtils.addMap(9, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_BECAME_FRIEND).trim();
        CSVUtils.addMap(10, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_CHAT).trim();
        CSVUtils.addMap(11, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_BECAME_FRIEND).trim();
        CSVUtils.addMap(12, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DAILY_BONUS).trim();
        CSVUtils.addMap(13, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_LOOK_AT_ME).trim();
        CSVUtils.addMap(14, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_BUZZ).trim();
        CSVUtils.addMap(15, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_IMAGE).trim();
        CSVUtils.addMap(16, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(18, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_XXX_POST_BUZZ).trim();
        CSVUtils.addMap(19, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_REPLY_COMMENT).trim();
        CSVUtils.addMap(20, value, en_log_notification);
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_BUZZ).trim();
        CSVUtils.addMap(21, value, en_log_notification);        
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_IMAGE).trim();
        CSVUtils.addMap(22, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_TEXT_BUZZ).trim();
        CSVUtils.addMap(24, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_TEXT_BUZZ).trim();
        CSVUtils.addMap(25, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_COMMENT).trim();
        CSVUtils.addMap(26, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_COMMENT).trim();
        CSVUtils.addMap(27, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_SUB_COMMENT).trim();
        CSVUtils.addMap(28, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_SUB_COMMENT).trim();
        CSVUtils.addMap(29, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_APPROVED_USER_INFO).trim();
        CSVUtils.addMap(30, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_APART_USER_INFO).trim();
        CSVUtils.addMap(31, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_DENIED_USER_INFO).trim();
        CSVUtils.addMap(32, value, en_log_notification);  
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(33, value, en_log_notification); 
        value = prop.getProperty(TypeKeys.EN_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(34, value, en_log_notification); 
        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_SOMEONE_CHECK_OUT).trim();
        CSVUtils.addMap(1, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_CHECK_OUT).trim();
        CSVUtils.addMap(2, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_SOMEONE_FAVOURIST).trim();
        CSVUtils.addMap(3, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_FAVOURIST).trim();
        CSVUtils.addMap(4, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_LIKE).trim();
        CSVUtils.addMap(5, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_ALSO_LIKE).trim();
        CSVUtils.addMap(6, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_RESPONSED).trim();
        CSVUtils.addMap(7, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_ALSO_RESPONSED).trim();
        CSVUtils.addMap(8, value, jp_log_notification);     
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_UNLOCKED_BACKSTAGE).trim();
        CSVUtils.addMap(9, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_BECAME_FRIEND).trim();
        CSVUtils.addMap(10, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_CHAT).trim();
        CSVUtils.addMap(11, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_BECAME_FRIEND).trim();
        CSVUtils.addMap(12, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DAILY_BONUS).trim();
        CSVUtils.addMap(13, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_LOOK_AT_ME).trim();
        CSVUtils.addMap(14, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_BUZZ).trim();
        CSVUtils.addMap(15, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_IMAGE).trim();
        CSVUtils.addMap(16, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(18, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_XXX_POST_BUZZ).trim();
        CSVUtils.addMap(19, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_REPLY_COMMENT).trim();
        CSVUtils.addMap(20, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_BUZZ).trim();
        CSVUtils.addMap(21, value, jp_log_notification);        
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_IMAGE).trim();
        CSVUtils.addMap(22, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_TEXT_BUZZ).trim();
        CSVUtils.addMap(24, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_TEXT_BUZZ).trim();
        CSVUtils.addMap(25, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_COMMENT).trim();
        CSVUtils.addMap(26, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_COMMENT).trim();
        CSVUtils.addMap(27, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_SUB_COMMENT).trim();
        CSVUtils.addMap(28, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_SUB_COMMENT).trim();
        CSVUtils.addMap(29, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_APPROVED_USER_INFO).trim();
        CSVUtils.addMap(30, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_APART_USER_INFO).trim();
        CSVUtils.addMap(31, value, jp_log_notification);  
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_DENIED_USER_INFO).trim();
        CSVUtils.addMap(32, value, jp_log_notification);
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(33, value, jp_log_notification); 
        value = prop.getProperty(TypeKeys.JP_LOG_NOTIFICATION_FREE_PAGE).trim();
        CSVUtils.addMap(34, value, jp_log_notification); 
        
    }     
    
    public static final Map<Integer, String> en_block_type = new TreeMap<>();
    public static final Map<Integer, String> jp_block_type = new TreeMap<>();
    private static void initBlockType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_UNBLOCK).trim();
        CSVUtils.addMap(0, value, en_block_type);
        value = prop.getProperty(TypeKeys.EN_BLOCK).trim();
        CSVUtils.addMap(1, value, en_block_type);
        
        value = prop.getProperty(TypeKeys.JP_UNBLOCK).trim();
        CSVUtils.addMap(0, value, jp_block_type);
        value = prop.getProperty(TypeKeys.JP_BLOCK).trim();
        CSVUtils.addMap(1, value, jp_block_type);
    }    
    
    public static final Map<Integer, String> en_alert_type = new TreeMap<>();
    public static final Map<Integer, String> jp_alert_type = new TreeMap<>();
    private static void initAlertType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_ALERT_TYPE_BOTH).trim();
        CSVUtils.addMap(0, value, en_alert_type);
        value = prop.getProperty(TypeKeys.EN_ALERT_TYPE_PUSH).trim();
        CSVUtils.addMap(1, value, en_alert_type);
        value = prop.getProperty(TypeKeys.EN_ALERT_TYPE_EMAIL).trim();
        CSVUtils.addMap(2, value, en_alert_type);        
        
        value = prop.getProperty(TypeKeys.JP_ALERT_TYPE_BOTH).trim();
        CSVUtils.addMap(0, value, jp_alert_type);
        value = prop.getProperty(TypeKeys.JP_ALERT_TYPE_PUSH).trim();
        CSVUtils.addMap(1, value, jp_alert_type);
        value = prop.getProperty(TypeKeys.JP_ALERT_TYPE_EMAIL).trim();
        CSVUtils.addMap(2, value, jp_alert_type);
    } 
    
    public static final Map<Integer, String> en_alert_frequency = new TreeMap<>();
    public static final Map<Integer, String> jp_alert_frequency = new TreeMap<>();
    private static void initAlertFrequency(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_ALERT_FREQUENCY_NEVER).trim();
        CSVUtils.addMap(-1, value, en_alert_frequency);
        value = prop.getProperty(TypeKeys.EN_ALERT_FREQUENCY_EVERYTIME).trim();
        CSVUtils.addMap(0, value, en_alert_frequency);
        value = prop.getProperty(TypeKeys.EN_ALERT_FREQUENCY_ONCE).trim();
        CSVUtils.addMap(1, value, en_alert_frequency); 
        value = prop.getProperty(TypeKeys.EN_ALERT_FREQUENCY_MAX_5).trim();
        CSVUtils.addMap(5, value, en_alert_frequency);
        value = prop.getProperty(TypeKeys.EN_ALERT_FREQUENCY_MAX_10).trim();
        CSVUtils.addMap(10, value, en_alert_frequency);        
        
        value = prop.getProperty(TypeKeys.JP_ALERT_FREQUENCY_NEVER).trim();
        CSVUtils.addMap(-1, value, jp_alert_frequency);
        value = prop.getProperty(TypeKeys.JP_ALERT_FREQUENCY_EVERYTIME).trim();
        CSVUtils.addMap(0, value, jp_alert_frequency);
        value = prop.getProperty(TypeKeys.JP_ALERT_FREQUENCY_ONCE).trim();
        CSVUtils.addMap(1, value, jp_alert_frequency); 
        value = prop.getProperty(TypeKeys.JP_ALERT_FREQUENCY_MAX_5).trim();
        CSVUtils.addMap(5, value, jp_alert_frequency);
        value = prop.getProperty(TypeKeys.JP_ALERT_FREQUENCY_MAX_10).trim();
        CSVUtils.addMap(10, value, jp_alert_frequency);
    }     

    public static final Map<Integer, String> en_favourist_type = new TreeMap<>();
    public static final Map<Integer, String> jp_favourist_type = new TreeMap<>();
    private static void initFavouristType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_UNFAVOURIST).trim();
        CSVUtils.addMap(0, value, en_favourist_type);
        value = prop.getProperty(TypeKeys.EN_FAVOURIST).trim();
        CSVUtils.addMap(1, value, en_favourist_type);
        
        value = prop.getProperty(TypeKeys.JP_UNFAVOURIST).trim();
        CSVUtils.addMap(0, value, jp_favourist_type);
        value = prop.getProperty(TypeKeys.JP_FAVOURIST).trim();
        CSVUtils.addMap(1, value, jp_favourist_type);
    }     

    public static final Map<Integer, String> en_call_type = new TreeMap<>();
    public static final Map<Integer, String> jp_call_type = new TreeMap<>();
    private static void initCallType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_VOICE_CALL).trim();
        CSVUtils.addMap(1, value, en_call_type);
        value = prop.getProperty(TypeKeys.EN_VIDEO_CALL).trim();
        CSVUtils.addMap(2, value, en_call_type);
        
        value = prop.getProperty(TypeKeys.JP_VOICE_CALL).trim();
        CSVUtils.addMap(1, value, jp_call_type);
        value = prop.getProperty(TypeKeys.JP_VIDEO_CALL).trim();
        CSVUtils.addMap(2, value, jp_call_type);
    }    
    
    public static final Map<Integer, String> en_image_type = new TreeMap<>();    
    public static final Map<Integer, String> jp_image_type = new TreeMap<>();
    private static void initImageType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_PUBLIC_IMAGE).trim();
        CSVUtils.addMap(1, value, en_image_type);
        value = prop.getProperty(TypeKeys.EN_BACKSTAGE_IMAGE).trim();
        CSVUtils.addMap(2, value, en_image_type);
        
        value = prop.getProperty(TypeKeys.JP_PUBLIC_IMAGE).trim();
        CSVUtils.addMap(1, value, jp_image_type);
        value = prop.getProperty(TypeKeys.JP_BACKSTAGE_IMAGE).trim();
        CSVUtils.addMap(2, value, jp_image_type);        

    }
    
    public static final Map<Integer, String> en_report_type = new TreeMap<>();    
    public static final Map<Integer, String> jp_report_type = new TreeMap<>();
    private static void initReportType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_REPORT_SEXUAL_CONTENT).trim();
        CSVUtils.addMap(0, value, en_report_type);
        value = prop.getProperty(TypeKeys.EN_REPORT_VIOLENT_CONTENT).trim();
        CSVUtils.addMap(1, value, en_report_type); 
        value = prop.getProperty(TypeKeys.EN_REPORT_HATEFUL_CONTENT).trim();
        CSVUtils.addMap(2, value, en_report_type);
        value = prop.getProperty(TypeKeys.EN_REPORT_DANGEROUS_CONTENT).trim();
        CSVUtils.addMap(3, value, en_report_type);
        value = prop.getProperty(TypeKeys.EN_REPORT_COPYRIGHTED_CONTENT).trim();
        CSVUtils.addMap(4, value, en_report_type);
        value = prop.getProperty(TypeKeys.EN_REPORT_SPAM_OR_SCAM).trim();
        CSVUtils.addMap(5, value, en_report_type); 
        value = prop.getProperty(TypeKeys.EN_REPORT_UNDERAGER_CONTENT).trim();
        CSVUtils.addMap(6, value, en_report_type);    
        
        value = prop.getProperty(TypeKeys.JP_REPORT_SEXUAL_CONTENT).trim();
        CSVUtils.addMap(0, value, jp_report_type);
        value = prop.getProperty(TypeKeys.JP_REPORT_VIOLENT_CONTENT).trim();
        CSVUtils.addMap(1, value, jp_report_type); 
        value = prop.getProperty(TypeKeys.JP_REPORT_HATEFUL_CONTENT).trim();
        CSVUtils.addMap(2, value, jp_report_type);
        value = prop.getProperty(TypeKeys.JP_REPORT_DANGEROUS_CONTENT).trim();
        CSVUtils.addMap(3, value, jp_report_type);
        value = prop.getProperty(TypeKeys.JP_REPORT_COPYRIGHTED_CONTENT).trim();
        CSVUtils.addMap(4, value, jp_report_type);
        value = prop.getProperty(TypeKeys.JP_REPORT_SPAM_OR_SCAM).trim();
        CSVUtils.addMap(5, value, jp_report_type); 
        value = prop.getProperty(TypeKeys.JP_REPORT_UNDERAGER_CONTENT).trim();
        CSVUtils.addMap(6, value, jp_report_type);        

    }     

    public static final Map<String, String> en_chat_content = new TreeMap<>();    
    public static final Map<String, String> jp_chat_content = new TreeMap<>();
    private static void initChatContent(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_CHAT_IMAGE).trim();
        CSVUtils.addMap("p", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_STICKER).trim();
        CSVUtils.addMap("STK", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_VIDEO).trim();
        CSVUtils.addMap("v", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_AUDIO).trim();
        CSVUtils.addMap("a", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_FILE_ERROR).trim();
        CSVUtils.addMap("e", value, en_chat_content);        
        value = prop.getProperty(TypeKeys.EN_CHAT_GIFT).trim();
        CSVUtils.addMap("GIFT", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_LOCATION).trim();
        CSVUtils.addMap("LCT", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_WINK).trim();
        CSVUtils.addMap("WINK", value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_START_VIDEO_CALL).trim();
        
        CSVUtils.addMap("SVIDEO", value, en_chat_content); 
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_RESPONSE_CALLER).trim();
        
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_RESPONSE_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_RESPONSE_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_RESPONSE_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_NO_ANSWER_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_NO_ANSWER_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_NO_ANSWER_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_NO_ANSWER_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_BUSY_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_BUSY_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VIDEO_CALL_BUSY_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_BUSY_MESSAGE, value, en_chat_content);
        
        value = prop.getProperty(TypeKeys.EN_CHAT_START_VOICE_CALL).trim();
        CSVUtils.addMap("SVOICE", value, en_chat_content);
        
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_RESPONSE_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_RESPONSE_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_RESPONSE_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_RESPONSE_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_NO_ANSWER_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_NO_ANSWER_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_NO_ANSWER_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_NO_ANSWER_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_BUSY_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_BUSY_MESSAGE, value, en_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_END_VOICE_CALL_BUSY_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_BUSY_MESSAGE, value, en_chat_content);

        value = prop.getProperty(TypeKeys.JP_CHAT_IMAGE).trim();
        CSVUtils.addMap("p", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_STICKER).trim();
        CSVUtils.addMap("STK", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_VIDEO).trim();
        CSVUtils.addMap("v", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_AUDIO).trim();
        CSVUtils.addMap("a", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.EN_CHAT_FILE_ERROR).trim();
        CSVUtils.addMap("e", value, en_chat_content);         
        value = prop.getProperty(TypeKeys.JP_CHAT_GIFT).trim();
        CSVUtils.addMap("GIFT", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_LOCATION).trim();
        CSVUtils.addMap("LCT", value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_WINK).trim();
        CSVUtils.addMap("WINK", value, jp_chat_content);
        
        value = prop.getProperty(TypeKeys.JP_CHAT_START_VIDEO_CALL).trim();
        CSVUtils.addMap("SVIDEO", value, jp_chat_content); 
        
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_RESPONSE_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_RESPONSE_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_RESPONSE_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_RESPONSE_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_NO_ANSWER_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_NO_ANSWER_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_NO_ANSWER_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_NO_ANSWER_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_BUSY_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VIDEO_CALL_BUSY_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VIDEO_CALL_BUSY_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VIDEO_CALL_BUSY_MESSAGE, value, jp_chat_content);
        
        value = prop.getProperty(TypeKeys.JP_CHAT_START_VOICE_CALL).trim();
        CSVUtils.addMap("SVOICE", value, jp_chat_content);
        
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_RESPONSE_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_RESPONSE_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_RESPONSE_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_RESPONSE_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_NO_ANSWER_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_NO_ANSWER_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_NO_ANSWER_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_NO_ANSWER_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_BUSY_CALLER).trim();
        CSVUtils.addMap(Message.CALLER_VOICE_CALL_BUSY_MESSAGE, value, jp_chat_content);
        value = prop.getProperty(TypeKeys.JP_CHAT_END_VOICE_CALL_BUSY_RECEIVER).trim();
        CSVUtils.addMap(Message.RECEIVER_VOICE_CALL_BUSY_MESSAGE, value, jp_chat_content);             

    }    

    public static final Map<Integer, String> en_buzz_type = new TreeMap<>();    
    public static final Map<Integer, String> jp_buzz_type = new TreeMap<>();
    private static void initBuzzType(Properties prop) throws UnsupportedEncodingException{
        String value;
        
        value = prop.getProperty(TypeKeys.EN_BUZZ_STATUS).trim();
        CSVUtils.addMap(0, value, en_buzz_type);
        value = prop.getProperty(TypeKeys.EN_BUZZ_IMAGE).trim();
        CSVUtils.addMap(1, value, en_buzz_type);
        value = prop.getProperty(TypeKeys.EN_BUZZ_GIFT).trim();
        CSVUtils.addMap(2, value, en_buzz_type);
        
        value = prop.getProperty(TypeKeys.JP_BUZZ_STATUS).trim();
        CSVUtils.addMap(0, value, jp_buzz_type);
        value = prop.getProperty(TypeKeys.JP_BUZZ_IMAGE).trim();
        CSVUtils.addMap(1, value, jp_buzz_type);
        value = prop.getProperty(TypeKeys.JP_BUZZ_GIFT).trim();
        CSVUtils.addMap(2, value, jp_buzz_type);        

    }    
    
    public static void init(){
        try{
            FileInputStream fis = new FileInputStream( FilesAndFolders.FILES.TRANSLATE_FILE );
            Properties prop = new Properties();
            prop.load( fis );
            initUserType(prop);
            initSuccessType(prop);
            initPurchaseType(prop);
            initGender(prop);
            initInterestedIn(prop);
            initRelationship(prop);
            initEthnicity(prop);
            initFlag(prop);
            initRegion(prop);
            initLogPoint(prop);
            initLogNotification(prop);
            initBlockType(prop);
            initAlertType(prop);
            initAlertFrequency(prop);
            initFavouristType(prop);
            initCallType(prop);
            initImageType(prop);
            initReportType(prop);
            initChatContent(prop);
            initBuzzType(prop);
            initVerifyFlag(prop);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
    }
}
