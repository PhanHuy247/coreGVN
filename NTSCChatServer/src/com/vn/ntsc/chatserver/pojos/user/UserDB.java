/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import org.bson.types.ObjectId;


/**
 *
 * @author DuongLTD
 */
public class UserDB {
//    private static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

    private static final String userIdKey = "_id";
    public ObjectId id;

    private static final String usernameKey = "user_name";
    public String username;

    private static final String deviceTypeKey = "device_type";
    public Integer deviceType;

    private static final String sortNameKey = "sort_name";
    public String sortName;

    private static final String emailKey = "email";
    public String email;

    private static final String originalPasswordKey = "original_pwd";
    public String originalPassword;

    private static final String passwordKey = "pwd";
    public String password;

    private static final String sipPasswordKey = "sip_pwd";
    public String sipPassword;

    private static final String avatarIdKey = "ava_id";
    public String avatarId;

    private static final String aboutKey = "abt";
    public String about;

    private static final String birthdayKey = "bir";
    public String birthday;

    private static final String fbIdKey = "fb_id";
    public String fbId;

    private static final String mocomIdKey = "mocom_id";
    public String mocomId;

    private static final String famuIdKey = "famu_id";
    public String famuId;

    private static final String genderKey = "gender";
    public Integer gender;

    private static final String regionKey = "region";
    public Integer region;

    private static final String autoRegionKey = "auto_region";
    public Integer autoRegion;

    private static final String hobbyKey = "hobby";
    public String hobby;

    private static final String pointKey = "point";
    public Integer point;

    private static final String favouristNumberKey = "fav_num";
    public Integer favouristNumber;

    private static final String favouritedNumberKey = "fvt_num";
    public Integer favouritedNumber;

    private static final String backstageNubmerKey = "bckstg_num";
    public Integer backstageNumber;

    private static final String pbImageNumberKey = "pbimg_num";
    public Integer pbImageNumber;

    private static final String giftNumberKey = "gift_num";
    public Integer giftNumber;

    private static final String lastUpdateKey = "lst_upd";
    public String lastUpdate;

    private static final String notiReadTimeKey = "noit_read_time";
    public Long notiReadTime;

    private static final String registerDateKey = "reg_date";
    public String registerDate;

    private static final String systemAccountKey = "sys_acc";
    public Integer systemAcc;

    private static final String reportNumKey = "rpt_num";
    public Integer reportNum;

    private static final String cmCodeKey = "cm_code";
    public String cmCode;

    private static final String accountTypeKey = "type";
    public Integer accountType;

    private static final String lastPurchaseTimeKey = "last_pur_time";
    public String lastPurchaseTime;

    private static final String lastLoginTimeKey = "last_login_time";
    public String lastLoginTime;

    private static final String flagKey = "flag";
    public Integer flag;

    public static final String verificationFlagKey = "verification_flag";
    public Integer verificationFlag;

    public static final String finishRegisterFlagKey = "finish_register_flag";
    public Integer finishRegisterFlag;

    private static final String buzzNumberKey = "buzz_number"; // = 0
    public Integer buzzNumber;

    public static final String measurementsKey = "measurements"; //[-1]
    public BasicDBList measurements;

    public static final String bustKey = "bust";
    public Integer bust;

    public static final String waistKey = "waist";
    public Integer waist;

    public static final String hipsKey = "hips";
    public Integer hips;
    // muc do sex - min
//    public static final String indecentKey = "indecent"; //-1
//    public Integer indecent;

    private static final String jobKey = "job";  //-1
    public Integer job;

    private static final String typeOfManKey = "type_of_man"; //[-1]
    public String typeOfMan;

    private static final String fetishKey = "fetish";//[-1]
    public String fetishs;

    private static final String videoCallWaitingKey = "video_call_waiting";
    public Boolean videoCall;

    private static final String voiceCallWaitingKey = "voice_call_waiting";
    public Boolean voiceCall;

    private static final String cupKey = "cup"; //-1
    public Integer cup;

    private static final String cuteTypeKey = "cute_type";///[-1]
    public Integer cuteType;

    private static final String joinHoursKey = "join_hours";
    public Integer joinHours;

    private static final String memoKey = "memo";
    public String memo;

    private static final String updateEmailFlagKey = "update_email_flag";
    public Integer updateEmailFlag;

    private static final String havePurchaseKey = "have_purchase";
    public Boolean havePurchase;

    private static final String body_typeKey = "body_type";
    public Integer body_type;

    private static final String isPurchaseKey = "is_purchase";
    public Integer isPurchased;

    //HUNGDT add app_version
    public static final String appVersionKey = "application_version";
    public String appVersion;
    public static final String osVersionKey = "os_version";
    public String osVersion;
    public static final String deviceNameKey = "device_name";
    public String deviceName;

    public static final String applicationIdKey = "application_id";
    public String applicationId;
    
    public static UserDB fromDBObject(DBObject dbObj) {
        UserDB user = new UserDB();

        ObjectId i = (ObjectId) dbObj.get(userIdKey);
        user.id = i;

        Integer deviceType = (Integer) dbObj.get(deviceTypeKey);
        if (deviceType != null) {
            user.deviceType = deviceType;
        }

        String os = (String) dbObj.get(osVersionKey);
        if (os != null) {
            user.osVersion = os;
        }

        String devName = (String) dbObj.get(deviceNameKey);
        if (devName != null) {
            user.deviceName = devName;
        }

        String uName = (String) dbObj.get(usernameKey);
        user.username = uName;

        //HUNGDT add app_version
        String appVersion = (String) dbObj.get(appVersionKey);
        user.appVersion = appVersion;
        
        String appId = (String) dbObj.get(applicationIdKey);
        user.applicationId = appId;

        String sippwd = (String) dbObj.get(sipPasswordKey);
        user.sipPassword = sippwd;

        String el = (String) dbObj.get(emailKey);
        user.email = el;

        String avaId = (String) dbObj.get(avatarIdKey);
        user.avatarId = avaId;

        String abt = (String) dbObj.get(aboutKey);
        user.about = abt;

        String fId = (String) dbObj.get(fbIdKey);
        user.fbId = fId;

        String mId = (String) dbObj.get(mocomIdKey);
        user.mocomId = mId;

        String famuId = (String) dbObj.get(famuIdKey);
        user.famuId = famuId;

        String bir = (String) dbObj.get(birthdayKey);
        user.birthday = bir;

        Integer gen = (Integer) dbObj.get(genderKey);
        user.gender = gen;

        Integer loc = (Integer) dbObj.get(regionKey);
        user.region = loc;

        Integer autoLoc = (Integer) dbObj.get(autoRegionKey);
        user.autoRegion = autoLoc;

        Integer cup = (Integer) dbObj.get(cupKey);
        user.cup = cup;

        Integer actionT = (Integer) dbObj.get(joinHoursKey);
        if (actionT != null) {
            user.joinHours = actionT;
        }

        Integer bodyType = (Integer) dbObj.get(cuteTypeKey);
        if (bodyType != null) {
            user.cuteType = bodyType;
        }
        Object point = (Object) dbObj.get(pointKey);
        if (point != null) {
            user.point = Integer.parseInt(point.toString());
        }

        Integer favNum = (Integer) dbObj.get(favouristNumberKey);
        if (favNum != null) {
            user.favouristNumber = favNum;
        }

        Integer fvtNum = (Integer) dbObj.get(favouritedNumberKey);
        if (fvtNum != null) {
            user.favouritedNumber = fvtNum;
        }

        Integer bckNum = (Integer) dbObj.get(backstageNubmerKey);
        if (bckNum != null) {
            user.backstageNumber = bckNum;
        }

        Integer pbNum = (Integer) dbObj.get(pbImageNumberKey);
        if (pbNum != null) {
            user.pbImageNumber = pbNum;
        }

        Integer gfNum = (Integer) dbObj.get(giftNumberKey);
        if (gfNum != null) {
            user.giftNumber = gfNum;
        }

        Long notiTime = (Long) dbObj.get(notiReadTimeKey);
        if (notiTime != null) {
            user.notiReadTime = notiTime;
        }

        Integer repNum = (Integer) dbObj.get(reportNumKey);
        if (repNum != null) {
            user.reportNum = repNum;
        }

        String cmCode = (String) dbObj.get(cmCodeKey);
        if (cmCode != null) {
            user.cmCode = cmCode;
        }

        Integer flg = (Integer) dbObj.get(flagKey);
        if (flg != null) {
            user.flag = flg;
        }
        Integer veriFlag = (Integer) dbObj.get(verificationFlagKey);
        if (veriFlag != null) {
            user.verificationFlag = veriFlag;
        }
        Integer registerFlag = (Integer) dbObj.get(finishRegisterFlagKey);
        if (registerFlag != null) {
            user.finishRegisterFlag = registerFlag;
        }

        String regTime = (String) dbObj.get(registerDateKey);
        user.registerDate = regTime;

        String purTime = (String) dbObj.get(lastPurchaseTimeKey);
        user.lastPurchaseTime = purTime;

        String lasLoginTime = (String) dbObj.get(lastLoginTimeKey);
        user.lastLoginTime = lasLoginTime;

        Integer buzzNum = (Integer) dbObj.get(buzzNumberKey);
        if (buzzNum != null) {
            user.buzzNumber = buzzNum;
        }
        Integer job = (Integer) dbObj.get(jobKey);
        if (job != null) {
            user.job = job;
        }
        Integer accType = (Integer) dbObj.get(accountTypeKey);
        if (accType != null) {
            user.accountType = accType;
        }
        Boolean voice_call = (Boolean) dbObj.get(voiceCallWaitingKey);
        if (voice_call != null) {
            user.voiceCall = voice_call;
        }
        Boolean video_call = (Boolean) dbObj.get(videoCallWaitingKey);
        if (video_call != null) {
            user.videoCall = video_call;
        }
        Integer updateEmailFlag = (Integer) dbObj.get(updateEmailFlagKey);
        if (updateEmailFlag != null) {
            user.updateEmailFlag = updateEmailFlag;
        }
        int gender = (Integer) dbObj.get(genderKey);
        if (gender == Constant.GENDER.FEMALE) {
//            user.indecent = (Integer) dbObj.get(indecentKey);
            user.cuteType = (Integer) dbObj.get(cuteTypeKey);
            user.fetishs = (String) dbObj.get(fetishKey);
            user.typeOfMan = (String) dbObj.get(typeOfManKey);
            user.cup = (Integer) dbObj.get(cupKey);
            user.measurements = (BasicDBList) dbObj.get(measurementsKey);
            user.bust = (Integer) dbObj.get(bustKey);
            user.waist = (Integer) dbObj.get(waistKey);
            user.hips = (Integer) dbObj.get(hipsKey);
            user.hobby = (String) dbObj.get(hobbyKey);
        } else {
            user.hobby = (String) dbObj.get(hobbyKey);
        }
        Integer body_type = (Integer) dbObj.get(body_typeKey);
        if (flg != null) {
            user.body_type = body_type;
        }

        Integer isPurchase = (Integer) dbObj.get(isPurchaseKey);
        if (isPurchase != null) {
            user.isPurchased = isPurchase;
        }

        return user;
    }

    public DBObject toDBObject() {
        BasicDBObject dbObject = new BasicDBObject();

        if (this.username != null) {
            dbObject.append(usernameKey, this.username);
        }

        if (this.cmCode != null) {
            dbObject.append(cmCodeKey, this.cmCode);
        }

        if (this.deviceType != null) {
            dbObject.append(deviceTypeKey, this.deviceType);
        }

        if (this.sortName != null) {
            dbObject.append(sortNameKey, this.sortName);
        }
        if (this.email != null) {
//            System.out.println("ToDBObject :" + this.email);
            dbObject.append(emailKey, this.email);
        }
//        System.out.println("ToDBObject :" + dbObject.toString());
        if (this.password != null) {
            dbObject.append(passwordKey, this.password);
        }
        if (this.originalPassword != null) {
            dbObject.append(originalPasswordKey, this.originalPassword);
        }
        if (this.sipPassword != null) {
            dbObject.append(sipPasswordKey, this.sipPassword);
        }
        if (this.avatarId != null) {
            dbObject.append(avatarIdKey, this.avatarId);
        }
        if (this.about != null) {
            dbObject.append(aboutKey, this.about);
        }
        if (this.birthday != null) {
            dbObject.append(birthdayKey, birthday);
        }
        if (this.fbId != null) {
            dbObject.append(fbIdKey, fbId);
        }
        if (this.mocomId != null) {
            dbObject.append(mocomIdKey, mocomId);
        }
        if (this.famuId != null) {
            dbObject.append(famuIdKey, famuId);
        }
        if (this.gender != null) {
            dbObject.append(genderKey, gender);
        }
        if (this.autoRegion != null) {
            dbObject.append(autoRegionKey, autoRegion);
        }
        if (this.region != null) {
            dbObject.append(regionKey, region);
        }
        if (hobby != null) {
            dbObject.append(hobbyKey, hobby);
        }
        if (point != null) {
            dbObject.append(pointKey, point);
        }
        if (registerDate != null) {
            dbObject.append(registerDateKey, registerDate);
        }
        if (this.lastUpdate != null) {
            dbObject.append(lastUpdateKey, lastUpdate);
        }
        if (this.systemAcc != null) {
            dbObject.append(systemAccountKey, systemAcc);
        }
        if (this.flag != null) {
            dbObject.append(flagKey, flag);
        }
        if (this.accountType != null) {
            dbObject.append(accountTypeKey, accountType);
        }
        if (this.favouristNumber != null) {
            dbObject.append(favouristNumberKey, favouristNumber);
        }
        if (this.favouritedNumber != null) {
            dbObject.append(favouritedNumberKey, favouritedNumber);
        }
        if (this.backstageNumber != null) {
            dbObject.append(backstageNubmerKey, backstageNumber);
        }
        if (this.pbImageNumber != null) {
            dbObject.append(pbImageNumberKey, pbImageNumber);
        }
        if (this.giftNumber != null) {
            dbObject.append(giftNumberKey, giftNumber);
        }

//        if (this.indecent != null) {
//            dbObject.append(indecentKey, indecent);
//        }
        if (this.measurements != null) {
            dbObject.append(measurementsKey, measurements);
        }
        if (this.bust != null) {
            dbObject.append(bustKey, bust);
        }
        if (this.waist != null) {
            dbObject.append(waistKey, waist);
        }
        if (this.hips != null) {
            dbObject.append(hipsKey, hips);
        }
        if (this.typeOfMan != null) {
            dbObject.append(typeOfManKey, typeOfMan);
        }
        if (this.fetishs != null) {
            dbObject.append(fetishKey, fetishs);
        }
        if (this.job != null) {
            dbObject.append(jobKey, job);
        }
        if (videoCall != null) {
            dbObject.append(videoCallWaitingKey, videoCall);
        }
        if (voiceCall != null) {
            dbObject.append(voiceCallWaitingKey, voiceCall);
        }
        if (havePurchase != null) {
            dbObject.append(havePurchaseKey, havePurchase);
        }
        if (verificationFlag != null) {
            dbObject.append(verificationFlagKey, verificationFlag);
        }
        if (finishRegisterFlag != null) {
            dbObject.append(finishRegisterFlagKey, finishRegisterFlag);
        }
        if (cuteType != null) {
            dbObject.append(cuteTypeKey, cuteType);
        }
        if (cup != null) {
            dbObject.append(cupKey, cup);
        }
        if (joinHours != null) {
            dbObject.append(joinHoursKey, joinHours);
        }
        if (memo != null) {
            dbObject.append(memoKey, memo);
        }
        if (updateEmailFlag != null) {
            dbObject.append(updateEmailFlagKey, updateEmailFlag);
        }
        if (this.body_type != null) {
            dbObject.append(body_typeKey, body_type);
        }
        if (this.isPurchased != null) {
            dbObject.append(isPurchaseKey, isPurchased);
        }
        if (osVersion != null) {
            dbObject.append(osVersionKey, osVersion);
        }

        if (deviceName != null) {
            dbObject.append(deviceNameKey, deviceName);
        }

        if (appVersion != null) {
            dbObject.append(appVersionKey, appVersion);
        }

        return dbObject;
    }

    public UserInfo getFriendInfor() {
        UserInfo user = new UserInfo();
        String uId = this.id.toString();
        user.userId = uId;
        String avatId = this.avatarId;
        user.avatarId = avatId;
        String usernme = this.username;
        user.username = Util.replaceBannedWord(usernme);
        Integer uGender = this.gender;
        user.gender = (long) uGender;
        Integer jb = this.job;
        if (job != null) {
            user.job = (long) jb;
        }
        String time = this.birthday;
        int age = Util.convertBirthdayToAge(time);
        user.age = age;
        user.about = Util.replaceBannedWord(this.about);
        // 48: other region
        user.region = this.region != null ? (long) this.region : 48;
        return user;
    }

    public UserInfo getUserInfor() {
        UserInfo user = new UserInfo();

        user.email = this.email;
        //user.appVersion = this.appVersion;
        if (this.appVersion != null) {
            user.appVersion = this.appVersion;
        } else {
            if (user.deviceType != null && user.deviceType == 1) {
                user.appVersion = "1.1";
            }
        }
        
        if (this.applicationId != null){
            user.applicationId = this.applicationId;
        }

        String uId = this.id.toString();
        user.userId = uId;
        String avaId = this.avatarId;
        user.avatarId = avaId;
        user.username = Util.replaceBannedWord(this.username);
        Integer favouristNum = this.favouristNumber;
        if (favouristNum != null) {
            user.favouristNumber = new Long(favouristNum);
        } else {
            user.favouristNumber = 0L;
        }
        Integer favouritedNum = this.favouritedNumber;
        if (favouritedNum != null) {
            user.favouritedNumber = new Long(favouritedNum);
        } else {
            user.favouritedNumber = 0L;
        }
        Integer pbImageNum = this.pbImageNumber;
        if (pbImageNum != null) {
            user.pbImageNumber = new Long(pbImageNum);
        } else {
            user.pbImageNumber = 0L;
        }
        Integer backStageNum = this.backstageNumber;
        if (backStageNum != null) {
            user.backstageNumber = new Long(backStageNum);
        } else {
            user.backstageNumber = 0L;
        }
        Integer giftNum = this.giftNumber;
        if (giftNum != null) {
            user.giftNumber = new Long(giftNum);
        } else {
            user.giftNumber = 0L;
        }
        Integer pnt = this.point;
        if (pnt != null) {
            user.point = new Long(pnt);
        } else {
            user.point = 0L;
        }

        if (this.about != null) {
            String abt = this.about;
            user.about = Util.replaceBannedWord(abt);
        }
        Integer gder = this.gender;
        user.gender = new Long(gder);
        Integer bdy_type = this.body_type;
        if (bdy_type != null) {
            user.body_type = new Long(bdy_type);
        } else {
            user.body_type = -1L;
        }
        user.userType = new Long(this.accountType);
        String time = this.birthday;
        int age = Util.convertBirthdayToAge(time);
        user.age = age;

        Integer loc = this.region;
        if (loc != null) {
            user.region = new Long(loc);
        }
        Integer autoLoc = this.autoRegion;
        if (autoLoc != null) {
            user.autoRegion = new Long(autoLoc);
        }
        Integer buzzNum = this.buzzNumber;
        if (buzzNum != null) {
            user.buzzNumber = new Long(buzzNum);
        } else {
            user.buzzNumber = new Long(0);
        }
        Integer job = this.job;
        if (job != null) {
            user.job = new Long(this.job);
        }
        Integer actionTime = this.joinHours;

        user.videoCall = this.videoCall;
        user.voiceCall = this.voiceCall;
        user.birthday = this.birthday.substring(0, 8);
        user.finishRegisterFlag = new Long(this.finishRegisterFlag);
        if (this.updateEmailFlag != null) {
            user.updateEmailFlag = new Long(this.updateEmailFlag);
        }
        
        Integer deviceType = this.deviceType;
        if (deviceType != null) {
            user.deviceType = new Long(this.deviceType);
        }

        Integer isPurchase = this.isPurchased;
        if (isPurchase != null) {
            user.isPurchased = isPurchase;
        }
        // Namhv #7939 start day 2017 - 04 - 17
        Integer flag = this.flag;
        if (flag != null) {
            user.flag = flag.longValue();
        }
        // Namhv #7939 end day 2017 - 04 - 17

        return user;
    }

    public static List<Long> convertDBListToList(BasicDBList dbList) {
        List<Long> list = null;
        if (dbList != null) {
            list = new ArrayList<>();
            for (Object dbList1 : dbList) {
                Integer d = (Integer) dbList1;
                list.add(new Long(d));
            }
        }
        return list;
    }

}
