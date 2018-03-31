/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.inspection.version.InspectionVersionDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.presentationserver.meetpeople.impl.AgePlus;
import com.vn.ntsc.presentationserver.meetpeople.impl.MeetPeopleProcessor;
import static com.vn.ntsc.presentationserver.meetpeople.impl.MeetPeopleProcessor.allSafeUser;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Setting;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Tool;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.User;

/**
 *
 * @author RuAc0n
 */
public class DatabaseLoader {

    public static DB db, dbSetting;

    static {
        try {
            db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
            dbSetting = CommonDAO.mongo.getDB(SettingdbKey.DB_NAME);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void init() {
        Util.addInfoLog("Start service initAllUsers");
        initAllUsers();
        initSetting();
//        initAllSafeUsers();
        Util.addInfoLog("Start service initAllSafeUsers");
    }

    public static boolean initAllSafeUsers() {
        Util.addInfoLog("Start service initAllSafeUsers START");
        try {
            String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
            DBCollection coll = db.getCollection(UserdbKey.USERS_COLLECTION);
            DBCollection collLocation = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.SAFE_USER, 1);
            obj.append(UserdbKey.USER.GENDER, 1);

            BasicDBObject obj2 = new BasicDBObject(UserdbKey.USER.APP_VERSION, safaryVersion);
            BasicDBObject[] or = new BasicDBObject[2];
            or[0] = obj;
            or[1] = obj2;
            BasicDBObject query = new BasicDBObject("$or", or);
            DBCursor dbCur = coll.find(query);
            

            Iterator<DBObject> iDb = dbCur.iterator();
            DBObject oDb;
            Date now = new Date();
            while (iDb.hasNext()) {
                try {
                    oDb = iDb.next();

                    Integer flag = (Integer) oDb.get(UserdbKey.USER.FLAG);
                    flag = flag == null ? Constant.FLAG.ON : flag;
//                    Integer verificationFlag = (Integer) oDb.get(UserdbKey.USER.VERIFICATION_FLAG);
                    Integer finishRegisFlag = (Integer) oDb.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                    finishRegisFlag = finishRegisFlag == null ? Constant.FLAG.ON : finishRegisFlag;
                    Integer systemAccount = (Integer) oDb.get("sys_acc");
                    boolean isSystemAccount = systemAccount != null && systemAccount > 0;
                    if (isSystemAccount || ((flag == Constant.FLAG.ON) /**
                             * && (verificationFlag == null || verificationFlag
                             * == Constant.YES)
                             */
                            && (finishRegisFlag == Constant.FLAG.ON))) {
                        ObjectId obId = (ObjectId) oDb.get(UserdbKey.USER.ID);

                        String user_id = obId.toString();
                        String user_name = (String) oDb.get(UserdbKey.USER.USERNAME);
                        String avatar_id = (String) oDb.get(UserdbKey.USER.AVATAR_ID);
                        Integer gender = (Integer) oDb.get(UserdbKey.USER.GENDER);
                        Integer body_type = (Integer) oDb.get(UserdbKey.USER.BODY_TYPE);
                        if (body_type == null) {
                            body_type = 999;
                        }
                        if (gender != null) {
                            Integer loc = (Integer) oDb.get(UserdbKey.USER.REGION);
                            int location = 0;
                            if (loc != null && loc != -1) {
                                location = loc;
                            }
                            String birthday = (String) oDb.get(UserdbKey.USER.BIRTHDAY);
                            Boolean videoCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                            Boolean voiceCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VOICE_CALL_WAITING);
                            boolean isVideo = false;
                            if (videoCallWaiting == null) {
                                if (gender == 1) {
                                    isVideo = true;
                                }
                            } else {
                                isVideo = videoCallWaiting;
                            }

                            boolean isVoice = false;
                            if (voiceCallWaiting == null) {
                                if (gender == 1) {
                                    isVoice = true;
                                }
                            } else {
                                isVoice = voiceCallWaiting;
                            }
                            int callWaiting = 1;
                            if (!isVideo && !isVoice) {
                                callWaiting = 0;
                            }
                            Date bir = new Date();
                            try {
                                bir = DateFormat.parse_yyyyMMdd(birthday);
                            } catch (Exception ex) {
                                Util.addErrorLog(ex);
                            }
                            AgePlus.mDate.put(user_id, bir);
                            long age = Tool.calAge(now, bir);
                            String registerTimeStr = (String) oDb.get(UserdbKey.USER.REGISTER_DATE);
                            long registerTime = System.currentTimeMillis();
                            if (registerTimeStr != null) {
                                registerTime = DateFormat.parse(registerTimeStr).getTime();
                            }
                            String lastLogin = (String) oDb.get(UserdbKey.USER.LAST_LOGIN_TIME);
                            long lastOnline = registerTime;
                            if (lastLogin != null) {
                                lastOnline = DateFormat.parse(lastLogin).getTime();
                            }
                            DBObject oDb_loc = collLocation.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, obId));
                            double lon = 0, lat = 0;
                            if (oDb_loc != null) {
                                ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
                                if (locs != null) {
                                    lon = locs.get(0);
                                    lat = locs.get(1);
                                }
                            }
                            boolean isOnline = false;

                            MeetPeopleProcessor.insertSafeUser((int) gender, callWaiting, (int) age, registerTime, lon, lat, isOnline, user_id, user_name, avatar_id, location, isVideo, isVoice, lastOnline, (int) body_type);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            Util.addInfoLog("Start service initAllSafeUsers END");
            Util.addInfoLog("Start service AllSafeUsers DATA SIZE " + allSafeUser.size());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return true;
    }

    public static boolean initAllUsers() {
        try {
            DBCollection coll = db.getCollection(UserdbKey.USERS_COLLECTION);
            DBCollection collLocation = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            DBCursor dbCur = coll.find();
            Iterator<DBObject> iDb = dbCur.iterator();
            DBObject oDb;
            Date now = new Date();
            while (iDb.hasNext()) {
                try {
                    oDb = iDb.next();

                    Integer flag = (Integer) oDb.get(UserdbKey.USER.FLAG);
                    flag = flag == null ? Constant.FLAG.ON : flag;
//                    Integer verificationFlag = (Integer) oDb.get(UserdbKey.USER.VERIFICATION_FLAG);
                    Integer finishRegisFlag = (Integer) oDb.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                    finishRegisFlag = finishRegisFlag == null ? Constant.FLAG.ON : finishRegisFlag;
                    Integer systemAccount = (Integer) oDb.get("sys_acc");
                    boolean isSystemAccount = systemAccount != null && systemAccount > 0;
                    if (isSystemAccount || ((flag != Constant.USER_STATUS_FLAG.DISABLE) /**
                             * && (verificationFlag == null || verificationFlag
                             * == Constant.YES)
                             */
                            && (finishRegisFlag == Constant.FLAG.ON))) {
                        ObjectId obId = (ObjectId) oDb.get(UserdbKey.USER.ID);

                        String user_id = obId.toString();
                        String user_name = (String) oDb.get(UserdbKey.USER.USERNAME);
                        String avatar_id = (String) oDb.get(UserdbKey.USER.AVATAR_ID);
                        Integer gender = (Integer) oDb.get(UserdbKey.USER.GENDER);
                        Integer body_type = (Integer) oDb.get(UserdbKey.USER.BODY_TYPE);
                        if (body_type == null) {
                            body_type = 999;
                        }
                        if (gender != null) {
                            Integer loc = (Integer) oDb.get(UserdbKey.USER.REGION);
                            int location = 0;
                            if (loc != null && loc != -1) {
                                location = loc;
                            }
                            String birthday = (String) oDb.get(UserdbKey.USER.BIRTHDAY);
                            Boolean videoCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                            Boolean voiceCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VOICE_CALL_WAITING);
                            boolean isVideo = false;
                            if (videoCallWaiting == null) {
                                if (gender == 1) {
                                    isVideo = true;
                                }
                            } else {
                                isVideo = videoCallWaiting;
                            }

                            boolean isVoice = false;
                            if (voiceCallWaiting == null) {
                                if (gender == 1) {
                                    isVoice = true;
                                }
                            } else {
                                isVoice = voiceCallWaiting;
                            }
                            int callWaiting = 1;
                            if (!isVideo && !isVoice) {
                                callWaiting = 0;
                            }
                            Date bir = new Date();
                            try {
                                bir = DateFormat.parse_yyyyMMdd(birthday);
                            } catch (Exception ex) {
                                Util.addErrorLog(ex);
                            }
                            AgePlus.mDate.put(user_id, bir);
                            long age = Tool.calAge(now, bir);
                            String registerTimeStr = (String) oDb.get(UserdbKey.USER.REGISTER_DATE);
                            long registerTime = System.currentTimeMillis();
                            if (registerTimeStr != null) {
                                registerTime = DateFormat.parse(registerTimeStr).getTime();
                            }
                            String lastLogin = (String) oDb.get(UserdbKey.USER.LAST_LOGIN_TIME);
                            long lastOnline = registerTime;
                            if (lastLogin != null) {
                                lastOnline = DateFormat.parse(lastLogin).getTime();
                            }
                            DBObject oDb_loc = collLocation.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, obId));
                            double lon = 0, lat = 0;
                            if (oDb_loc != null) {
                                ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
                                if (locs != null) {
                                    lon = locs.get(0);
                                    lat = locs.get(1);
                                }
                            }
                            boolean isOnline = false;
                            MeetPeopleProcessor.insertUser((int) gender, callWaiting, (int) age, registerTime, lon, lat, isOnline, user_id, user_name, avatar_id, location, isVideo, isVoice, lastOnline, (int) body_type);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return true;
    }

    private static void initSetting() {
        try {
            Setting.Distance.init();
            DBCollection coll = dbSetting.getCollection(SettingdbKey.DISTANCE_SETTING_COLLECTION);
            DBObject oDb = coll.findOne();
            ArrayList<Double> dis = (ArrayList<Double>) (oDb.get(SettingdbKey.DISTANCE_SETTING.DISTANCE));
            for (int i = 0; i < dis.size() && i < 4; i++) {
                Setting.Distance.Value.set(i, dis.get(i));
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public static User getOneUser(String userId) {
        try {
            DBCollection coll = db.getCollection(UserdbKey.USERS_COLLECTION);
            DBCollection collLocation = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            DBObject oDb = coll.findOne(new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId)));
            Date now = new Date();
            try {
                Integer flag = (Integer) oDb.get(UserdbKey.USER.FLAG);
//                    Integer verificationFlag = (Integer) oDb.get(UserdbKey.USER.VERIFICATION_FLAG);
                Integer finishRegisFlag = (Integer) oDb.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                Integer systemAccount = (Integer) oDb.get("sys_acc");
                boolean isSystemAccount = systemAccount != null && systemAccount > 0;
                if (isSystemAccount || ((flag == Constant.FLAG.ON) /**
                         * && (verificationFlag == null || verificationFlag ==
                         * Constant.YES)
                         */
                        && (finishRegisFlag == Constant.FLAG.ON))) {
                    ObjectId obId = (ObjectId) oDb.get(UserdbKey.USER.ID);
                    String user_id = obId.toString();
                    String user_name = (String) oDb.get(UserdbKey.USER.USERNAME);
                    String avatar_id = (String) oDb.get(UserdbKey.USER.AVATAR_ID);
                    int gender = (Integer) oDb.get(UserdbKey.USER.GENDER);
                    int body_type = (Integer) oDb.get(UserdbKey.USER.BODY_TYPE);
                    Integer loc = (Integer) oDb.get(UserdbKey.USER.REGION);
                    int location = 0;
                    if (loc != null && loc != -1) {
                        location = loc;
                    }
                    String birthday = (String) oDb.get(UserdbKey.USER.BIRTHDAY);
                    Boolean videoCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                    Boolean voiceCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VOICE_CALL_WAITING);
                    boolean isVideo = false;
                    if (videoCallWaiting == null) {
                        if (gender == 1) {
                            isVideo = true;
                        }
                    } else {
                        isVideo = videoCallWaiting;
                    }

                    boolean isVoice = false;
                    if (voiceCallWaiting == null) {
                        if (gender == 1) {
                            isVoice = true;
                        }
                    } else {
                        isVoice = voiceCallWaiting;
                    }
                    int callWaiting = 1;
                    if (!isVideo && !isVoice) {
                        callWaiting = 0;
                    }
                    Date bir = new Date();
                    try {
                        bir = DateFormat.parse_yyyyMMdd(birthday);
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
//                        AgePlus.mDate.put(user_id, bir);
                    long age = Tool.calAge(now, bir);
                    String registerTimeStr = (String) oDb.get(UserdbKey.USER.REGISTER_DATE);
                    long registerTime = System.currentTimeMillis();
                    if (registerTimeStr != null) {
                        registerTime = DateFormat.parse(registerTimeStr).getTime();
                    }
                    String lastLogin = (String) oDb.get(UserdbKey.USER.LAST_LOGIN_TIME);
                    long lastOnline = registerTime;
                    if (lastLogin != null) {
                        lastOnline = DateFormat.parse(lastLogin).getTime();
                    }
                    DBObject oDb_loc = collLocation.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, obId));
                    double lon = 0, lat = 0;
                    if (oDb_loc != null) {
                        ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
                        if (locs != null) {
                            lon = locs.get(0);
                            lat = locs.get(1);
                        }
                    }
                    boolean isOnline = false;

                    return new User((int) gender, callWaiting, (int) age, registerTime, lon, lat, isOnline, user_id, user_name, avatar_id, location, isVideo, isVoice, lastOnline, (int) body_type);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return null;
    }

    public static User getOneUserFromEmail(String userId) {
        try {
            DBCollection coll = db.getCollection(UserdbKey.USERS_COLLECTION);
            DBCollection collLocation = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            DBObject oDb = coll.findOne(new BasicDBObject(UserdbKey.USER.EMAIL, new ObjectId(userId)));
            Date now = new Date();
            try {
                Integer flag = (Integer) oDb.get(UserdbKey.USER.FLAG);
//                    Integer verificationFlag = (Integer) oDb.get(UserdbKey.USER.VERIFICATION_FLAG);
                Integer finishRegisFlag = (Integer) oDb.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
                Integer systemAccount = (Integer) oDb.get("sys_acc");
                boolean isSystemAccount = systemAccount != null && systemAccount > 0;
                if (isSystemAccount || ((flag == Constant.FLAG.ON) /**
                         * && (verificationFlag == null || verificationFlag ==
                         * Constant.YES)
                         */
                        && (finishRegisFlag == Constant.FLAG.ON))) {
                    ObjectId obId = (ObjectId) oDb.get(UserdbKey.USER.ID);
                    String user_id = obId.toString();
                    String user_name = (String) oDb.get(UserdbKey.USER.USERNAME);
                    String avatar_id = (String) oDb.get(UserdbKey.USER.AVATAR_ID);
                    int gender = (Integer) oDb.get(UserdbKey.USER.GENDER);
                    int body_type = (Integer) oDb.get(UserdbKey.USER.BODY_TYPE);
                    Integer loc = (Integer) oDb.get(UserdbKey.USER.REGION);
                    int location = 0;
                    if (loc != null && loc != -1) {
                        location = loc;
                    }
                    String birthday = (String) oDb.get(UserdbKey.USER.BIRTHDAY);
                    Boolean videoCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                    Boolean voiceCallWaiting = (Boolean) oDb.get(UserdbKey.USER.VOICE_CALL_WAITING);
                    boolean isVideo = false;
                    if (videoCallWaiting == null) {
                        if (gender == 1) {
                            isVideo = true;
                        }
                    } else {
                        isVideo = videoCallWaiting;
                    }

                    boolean isVoice = false;
                    if (voiceCallWaiting == null) {
                        if (gender == 1) {
                            isVoice = true;
                        }
                    } else {
                        isVoice = voiceCallWaiting;
                    }
                    int callWaiting = 1;
                    if (!isVideo && !isVoice) {
                        callWaiting = 0;
                    }
                    Date bir = new Date();
                    try {
                        bir = DateFormat.parse_yyyyMMdd(birthday);
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
//                        AgePlus.mDate.put(user_id, bir);
                    long age = Tool.calAge(now, bir);
                    String registerTimeStr = (String) oDb.get(UserdbKey.USER.REGISTER_DATE);
                    long registerTime = System.currentTimeMillis();
                    if (registerTimeStr != null) {
                        registerTime = DateFormat.parse(registerTimeStr).getTime();
                    }
                    String lastLogin = (String) oDb.get(UserdbKey.USER.LAST_LOGIN_TIME);
                    long lastOnline = registerTime;
                    if (lastLogin != null) {
                        lastOnline = DateFormat.parse(lastLogin).getTime();
                    }
                    DBObject oDb_loc = collLocation.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, obId));
                    double lon = 0, lat = 0;
                    if (oDb_loc != null) {
                        ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
                        if (locs != null) {
                            lon = locs.get(0);
                            lat = locs.get(1);
                        }
                    }
                    boolean isOnline = false;

                    return new User((int) gender, callWaiting, (int) age, registerTime, lon, lat, isOnline, user_id, user_name, avatar_id, location, isVideo, isVoice, lastOnline, (int) body_type);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return null;
    }
}
