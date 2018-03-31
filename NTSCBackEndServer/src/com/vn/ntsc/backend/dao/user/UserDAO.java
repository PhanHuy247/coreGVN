/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.admin.ApplicationDAO;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.log.TransactionLogDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.SystemAccount;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.BaseUser;
import com.vn.ntsc.backend.entity.impl.user.Point;
import com.vn.ntsc.backend.entity.impl.user.ReviewingUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.entity.impl.user.UserDB;
import com.vn.ntsc.backend.entity.impl.user.extend.Female;
import com.vn.ntsc.backend.entity.impl.user.extend.Male;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static DBCollection coll;
    private static MessageDigest md;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.USERS_COLLECTION);
            coll.createIndex(new BasicDBObject(UserdbKey.USER.USER_ID, 1));
            md = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    // LongLT 8/8/2016
    public static boolean updatePurchaseTime(String userId, Date purchaseTime) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_HOUR_FORMAT);
            DBObject obj = coll.findOne(query);
            String firstPurchaseTime = (String) obj.get(UserdbKey.USER.FIRST_PURCHASE_TIME);
            String time = DateFormat.format(purchaseTime);
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.LAST_PURCHASE_TIME, time);
            if (firstPurchaseTime == null) {
                addition.append(UserdbKey.USER.FIRST_PURCHASE_TIME, time);
            }
            addition.append(UserdbKey.USER.HAVE_PURCHASE, true);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<IEntity> getAllSystemAcc() throws EazyException {
        List<IEntity> result = new ArrayList<>();
        try {
            DBObject gte = new BasicDBObject("$gt", 0);
            DBObject obj = new BasicDBObject(UserdbKey.USER.SYSTEM_ACCOUNT, gte);
            DBCursor cursor = coll.find(obj);
            if (cursor != null && cursor.size() > 0) {
                while (cursor.hasNext()) {
                    DBObject dbO = cursor.next();
                    String id = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                    String username = (String) dbO.get(UserdbKey.USER.USERNAME);
                    result.add(new SystemAccount(id, username));
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    //return List<User> with User = {id, point, name};
    public static List<User> getListUserAutoMessage(JSONObject obj, List<String> purchaseList) throws EazyException {
        List<User> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            findObject = createQueryForSearch(obj, findObject, purchaseList);
            if (findObject == null) {
                return null;
            }
            findObject.append(UserdbKey.USER.SYSTEM_ACCOUNT, new BasicDBObject("$lt", 1));
            DBCursor cursor = coll.find(findObject);
//            if (findObject.isEmpty()) {
//                cursor = coll.find();
//            } else {
//                cursor = coll.find(findObject);
//            }
//            System.out.println(" size cursor : " + cursor.size());
            //set to list
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
//                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
//                if (flag != null && flag == Constant.ACTIVE) {
                User user = new User();
                user.userId = dbObject.get(UserdbKey.USER.ID).toString();
                Integer point = (Integer) dbObject.get(UserdbKey.USER.POINT);
                user.point = point == null ? 0 : (long) point;
                user.username = (String) dbObject.get(UserdbKey.USER.USERNAME);
                result.add(user);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getListAutoMessage(JSONObject obj, List<String> purchaseList) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            findObject = createQueryForSearch(obj, findObject, purchaseList);
            if (findObject == null) {
                return null;
            }
            findObject.append(UserdbKey.USER.SYSTEM_ACCOUNT, new BasicDBObject("$lt", 1));
            DBCursor cursor = coll.find(findObject);
//            if (findObject.isEmpty()) {
//                cursor = coll.find();
//            } else {
//                cursor = coll.find(findObject);
//            }
//            System.out.println(" size cursor : " + cursor.size());
            //set to list
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
//                Integer flag = (Integer) dbObject.get(UserdbKey.USER.FLAG);
//                if (flag != null && flag == Constant.ACTIVE) {
                result.add(dbObject.get(UserdbKey.USER.ID).toString());
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData searchUser(JSONObject obj, boolean isCsv, List<String> purchaseL) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            findObject = createQueryForSearch(obj, findObject, purchaseL);
            if (findObject == null) {
                return null;
            }
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            if (order == null) {
                return null;
            }

            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }

            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            if (sort == null) {
                return null;
            }

            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.USER.USERNAME, or);
            } else if (sort == 2) {
                or = 0 - or;
                sortObj.append(UserdbKey.USER.BIRTHDAY, or);
            } else if (sort == 3) {
                sortObj.append(UserdbKey.USER.POINT, or);
            } else if (sort == 5) {
                sortObj.append(UserdbKey.USER.REGISTER_DATE, or);
            } else if (sort == 6) {
                sortObj.append(UserdbKey.USER.LAST_PURCHASE_TIME, or);
            } else if (sort == 7) {
                sortObj.append(UserdbKey.USER.LAST_LOGIN_TIME, or);
            }

            findObject.append(UserdbKey.USER.SYSTEM_ACCOUNT, new BasicDBObject("$lt", 1));
//            findObject.append(UserdbKey.USER.VERIFICATION_FLAG, Constant.YES);
//            findObject.append(UserdbKey.USER.FINISH_REGISTER_FLAG, Constant.YES);
//            System.out.println("find obj" + findObject.toString());
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            //set to list
            Integer number = cursor.size();
            if (skip != null && take != null) {
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            }
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                IEntity user = getInfor(dbObject);
                list.add(user);
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<Integer, List<IEntity>> searchConnectionUser(JSONObject obj) throws EazyException {
        Map<Integer, List<IEntity>> result = new TreeMap<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            findObject = createConnectionQuery(obj, findObject);
            if (findObject == null) {
                return null;
            }
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            if (sort == null) {
                return null;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.USER.USERNAME, or);
            } else if (sort == 3) {
                sortObj.append(UserdbKey.USER.FAVOURIST_NUMBER, or);
            } else if (sort == 4) {
                sortObj.append(UserdbKey.USER.FAVOURITED_NUMBER, or);
            }

            findObject.append(UserdbKey.USER.SYSTEM_ACCOUNT, new BasicDBObject("$lt", 1));
            DBCursor cursor = coll.find(findObject).sort(sortObj);

            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (skip == null || take == null) {
                return null;
            }

            Integer number = cursor.size();
            cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                IEntity user = getConnectionInfor(dbObject);
                list.add(user);
            }
            result.put(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static BasicDBObject createConnectionQuery(JSONObject obj, BasicDBObject findObject) {
//           DBObject findObject = QueryBuilder.start(UserdbKey.USER.RELATIONSHIP_STATUS).in(relationship).get();
        String userId = Util.getStringParam(obj, ParamKey.ID);
        if (userId != null && !userId.isEmpty()) {
            ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
            String listUserId[] = userId.split("[,、，､]");
            for (String i : listUserId) {
                if (i != null && !i.trim().isEmpty()) {
                    i = i.trim();
                    for (String str : Constant.SPECIAL_CHARACTER) {
                        if (i.contains(str)) {
                            String string = "\\" + str;
                            i = i.replace(str, string);
                        }
                    }
                    BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                    BasicDBObject findObjectUserId = new BasicDBObject(UserdbKey.USER.USER_ID, regex);
                    listFindObject.add(findObjectUserId);
                }
            }
            findObject.append("$or", listFindObject);
        }
        Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
        String email = Util.getStringParam(obj, ParamKey.EMAIL);
        if (email != null && userType == null) {
            return null;
        }
        if (userType != null) {
            boolean isExists = false;
            if (userType.intValue() == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                isExists = true;
            }
            BasicDBObject exist = new BasicDBObject("$exists", isExists);
            findObject.append(UserdbKey.USER.FB_ID, exist);
            if (email != null) {
                if (isExists) {
                    findObject.append(UserdbKey.USER.FB_ID, email.toLowerCase());
                } else {
                    findObject.append(UserdbKey.USER.EMAIL, email.toLowerCase());
                }
            }
        }
        String userName = Util.getStringParam(obj, ParamKey.USER_NAME);
        if (userName != null) {
            String[] list = userName.trim().split("\\s+");
            if (list.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.length; i++) {
                    sb.append(list[i].toLowerCase());
                    if (i < (list.length - 1)) {
                        sb.append("|");
                    }
                }
                BasicDBObject regex = new BasicDBObject("$regex", sb.toString());
                findObject.append(UserdbKey.USER.SORT_NAME, regex);
            }
        }
        BasicDBList ands = new BasicDBList();
        Long fromFav = Util.getLongParam(obj, "from_fav");
        Long toFav = Util.getLongParam(obj, "to_fav");
        if (fromFav != null && toFav != null) {
            BasicDBObject gte = new BasicDBObject("$gte", fromFav);
            BasicDBObject lte = new BasicDBObject("$lte", toFav);
            ands.add(new BasicDBObject(UserdbKey.USER.FAVOURIST_NUMBER, lte));
            ands.add(new BasicDBObject(UserdbKey.USER.FAVOURIST_NUMBER, gte));
            findObject.append("$and", ands);
        } else {
            if (fromFav != null) {
                BasicDBObject gte = new BasicDBObject("$gte", fromFav);
                findObject.append(UserdbKey.USER.FAVOURIST_NUMBER, gte);
            }
            if (toFav != null) {
                BasicDBObject lte = new BasicDBObject("$lte", toFav);
                findObject.append(UserdbKey.USER.FAVOURIST_NUMBER, lte);
            }
        }
        Long fromFvt = Util.getLongParam(obj, "from_fvt");
        Long toFvt = Util.getLongParam(obj, "to_fvt");
        if (fromFvt != null && toFvt != null) {
            BasicDBObject gte = new BasicDBObject("$gte", fromFvt);
            BasicDBObject lte = new BasicDBObject("$lte", toFvt);
            ands.add(new BasicDBObject(UserdbKey.USER.FAVOURITED_NUMBER, lte));
            ands.add(new BasicDBObject(UserdbKey.USER.FAVOURITED_NUMBER, gte));
            findObject.append("$and", ands);
        } else {
            if (fromFvt != null) {
                BasicDBObject gte = new BasicDBObject("$gte", fromFvt);
                findObject.append(UserdbKey.USER.FAVOURITED_NUMBER, gte);
            }
            if (toFvt != null) {
                BasicDBObject lte = new BasicDBObject("$lte", toFvt);
                findObject.append(UserdbKey.USER.FAVOURITED_NUMBER, lte);
            }
        }
        String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
        if (cmCode != null) {
            findObject.append(UserdbKey.USER.CM_CODE, cmCode);
        }
        return findObject;
    }

    public static Map<String, IEntity> getMapLogInfor(List<String> friendIds) throws EazyException {
        Map<String, IEntity> result = new TreeMap<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            for (String friendId : friendIds) {
                listId.add(new ObjectId(friendId));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBCursor friendCursor = coll.find(query);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                User user = new User();
                String userId = dbObject.get(UserdbKey.USER.ID).toString();
                user.userId = userId;
                String userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                String email = (String) dbObject.get(UserdbKey.USER.EMAIL);
                String fbId = (String) dbObject.get(UserdbKey.USER.FB_ID);
                String mocomId = (String) dbObject.get(UserdbKey.USER.MOCOM_ID);
                String famuId = (String) dbObject.get(UserdbKey.USER.FAMU_ID);
                String cmCode = (String) dbObject.get(UserdbKey.USER.CM_CODE);
                Boolean videoCall = (Boolean) dbObject.get(UserdbKey.USER.VIDEO_CALL_WAITING);
                Boolean voiceCall = (Boolean) dbObject.get(UserdbKey.USER.VOICE_CALL_WAITING);
                user.cmCode = cmCode;
                if (fbId != null) {
                    user.email = fbId;
                    user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FB_TYPE);
                } else if (mocomId != null) {
                    user.email = mocomId;
                    user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE);
                } else if (famuId != null) {
                    user.email = famuId;
                    user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE);
                } else {
                    user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE);
                    user.email = email;
                }
                user.username = userName;
                user.videoCall = videoCall;
                user.voiceCall = voiceCall;
                result.put(userId, user);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static IEntity getDetailUser(String userId) throws EazyException {
        IEntity result = new User();
        try {
            //command to take profile of friend
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject dbO = coll.findOne(query);
            result = getDetailInfor(dbO);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getListUser(Long userType, String userId, String email, String cmCode) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            if (userType == null && userId == null && email == null && cmCode == null) {
                return null;
            }
            BasicDBObject findObject = new BasicDBObject();
            ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
            if (userId != null && !userId.isEmpty()) {
                String listUserId[] = userId.split("[,、，､]");

                for (String i : listUserId) {
                    if (i != null && !i.trim().isEmpty()) {
                        i = i.trim();
                        for (String str : Constant.SPECIAL_CHARACTER) {
                            if (i.contains(str)) {
                                String string = "\\" + str;
                                i = i.replace(str, string);
                            }
                        }
                        BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                        BasicDBObject findObjectUserId = new BasicDBObject(UserdbKey.USER.USER_ID, regex);
                        listFindObject.add(findObjectUserId);
                    }
                }
                findObject.append("$or", listFindObject);
            }

            if (email != null && userType == null) {
                return null;
            }
            if (userType != null) {
                findObject.append(UserdbKey.USER.ACCOUNT_TYPE, userType);
                if (email != null) {
                    if (email.trim().isEmpty()) {
                        if (userType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                            findObject.append(UserdbKey.USER.FB_ID, "{}");
                        } else if (userType == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                            findObject.append(UserdbKey.USER.MOCOM_ID, "{}");
                        } else if (userType == Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE) {
                            findObject.append(UserdbKey.USER.FAMU_ID, "{}");
                        } else {
                            findObject.append(UserdbKey.USER.EMAIL, "{}");
                        }
                    } else {
                        for (String str : Constant.SPECIAL_CHARACTER) {
                            if (email.contains(str)) {
                                String string = "\\" + str;
                                email = email.replace(str, string);
                            }
                        }
                        BasicDBObject regex = new BasicDBObject("$regex", email.toLowerCase());
                        if (userType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                            findObject.append(UserdbKey.USER.FB_ID, regex);
                        } else if (userType == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                            findObject.append(UserdbKey.USER.MOCOM_ID, regex);
                        } else if (userType == Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE) {
                            findObject.append(UserdbKey.USER.FAMU_ID, regex);
                        } else {
                            findObject.append(UserdbKey.USER.EMAIL, regex);
                        }
                    }
                }
            }
            if (cmCode != null) {
                findObject.append(UserdbKey.USER.CM_CODE, cmCode);
            }
            DBCursor cursor = coll.find(findObject);
            //set to list
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String uId = ((ObjectId) dbObject.get(UserdbKey.USER.ID)).toString();
                result.add(uId);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static BasicDBObject createQuery(JSONObject obj, BasicDBObject findObject) {
//           DBObject findObject = QueryBuilder.start(UserdbKey.USER.RELATIONSHIP_STATUS).in(relationship).get();
        String userId = Util.getStringParam(obj, ParamKey.ID);
        if (userId != null && !userId.isEmpty()) {
            for (String str : Constant.SPECIAL_CHARACTER) {
                if (userId.contains(str)) {
                    String string = "\\" + str;
                    userId = userId.replace(str, string);
                }
            }
            BasicDBObject regex = new BasicDBObject("$regex", userId.toLowerCase());
            findObject = new BasicDBObject(UserdbKey.USER.USER_ID, regex);
        }
        Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
        String email = Util.getStringParam(obj, ParamKey.EMAIL);
        if (email != null && userType == null) {
            return null;
        }
        if (userType != null) {
            findObject.append(UserdbKey.USER.ACCOUNT_TYPE, userType);
            if (email != null) {
                if (userType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                    findObject.append(UserdbKey.USER.FB_ID, email.toLowerCase());
                } else if (userType == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                    findObject.append(UserdbKey.USER.MOCOM_ID, email.toLowerCase());
                } else if (userType == Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE) {
                    findObject.append(UserdbKey.USER.FAMU_ID, email.toLowerCase());
                } else {
                    findObject.append(UserdbKey.USER.EMAIL, email.toLowerCase());
                }
            }
        }
        Long gender = Util.getLongParam(obj, ParamKey.GENDER);
        if (gender != null) {
            findObject.append(UserdbKey.USER.GENDER, gender.intValue());
        }
        List<Long> region = Util.getListLong(obj, ParamKey.REGION);
        if (region != null) {
            if (!region.isEmpty()) {
                List<Integer> list = new ArrayList<>();
                for (Long re : region) {
                    list.add(re.intValue());
                }
                BasicDBObject inObj = new BasicDBObject("$in", list);
                findObject.append(UserdbKey.USER.REGION, inObj);
            }
        }
        String userName = Util.getStringParam(obj, ParamKey.USER_NAME);
        //abc, cde
        if (userName != null) {
            for (String str : Constant.SPECIAL_CHARACTER) {
                if (userName.contains(str)) {
                    String string = "\\" + str;
                    userName = userName.replace(str, string);
                }
            }
            String[] list = userName.trim().split("\\s+");
            //abc| cde
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                sb.append(list[i].toLowerCase());
                if (i < (list.length - 1)) {
                    sb.append("|");
                }
            }
            //sb= a|b|c
            BasicDBObject regex = new BasicDBObject("$regex", sb.toString());
            findObject.append(UserdbKey.USER.SORT_NAME, regex);
        }
        BasicDBList ands = new BasicDBList();
        Long lowerAge = Util.getLongParam(obj, "lower_age");
        String upperBirthday = Util.toBirthday(lowerAge);
        Long upperAge = Util.getLongParam(obj, "upper_age");
        String lowerBirthday = null;
        if (upperAge != null) {
            lowerBirthday = Util.toBirthday(upperAge + 1);
        }
        if (lowerBirthday != null && upperBirthday != null) {
            BasicDBObject gt = new BasicDBObject("$gt", lowerBirthday);
            BasicDBObject lte = new BasicDBObject("$lte", upperBirthday);
            ands.add(new BasicDBObject(UserdbKey.USER.BIRTHDAY, lte));
            ands.add(new BasicDBObject(UserdbKey.USER.BIRTHDAY, gt));
            findObject.append("$and", ands);
        } else {
            if (lowerBirthday != null) {
                BasicDBObject gt = new BasicDBObject("$gt", lowerBirthday);
                findObject.append(UserdbKey.USER.BIRTHDAY, gt);
            }

            if (upperBirthday != null) {
                BasicDBObject lte = new BasicDBObject("$lte", upperBirthday);
                findObject.append(UserdbKey.USER.BIRTHDAY, lte);
            }
        }
        Long lowerPoint = Util.getLongParam(obj, "lower_pnt");
        Long upperPoint = Util.getLongParam(obj, "upper_pnt");
        if (lowerPoint != null && upperPoint != null) {
            BasicDBObject gte = new BasicDBObject("$gte", lowerPoint);
            BasicDBObject lte = new BasicDBObject("$lte", upperPoint);
            ands.add(new BasicDBObject(UserdbKey.USER.POINT, lte));
            ands.add(new BasicDBObject(UserdbKey.USER.POINT, gte));
            findObject.append("$and", ands);
        } else {
            if (lowerPoint != null) {
                BasicDBObject gte = new BasicDBObject("$gte", lowerPoint);
                findObject.append(UserdbKey.USER.POINT, gte);
            }

            if (upperPoint != null) {
                BasicDBObject lte = new BasicDBObject("$lte", upperPoint);
                findObject.append(UserdbKey.USER.POINT, lte);
            }
        }
        String lowerDay = Util.getStringParam(obj, "from_reg_day");
        String upperDay = Util.getStringParam(obj, "to_reg_day");
        if (lowerDay != null && upperDay != null) {
//                lowerDay += "000000";
//                upperDay += "999999";
            BasicDBObject gte = new BasicDBObject("$gte", lowerDay);
            BasicDBObject lt = new BasicDBObject("$lt", upperDay);
            ands.add(new BasicDBObject(UserdbKey.USER.REGISTER_DATE, lt));
            ands.add(new BasicDBObject(UserdbKey.USER.REGISTER_DATE, gte));
            findObject.append("$and", ands);
        } else {
            if (lowerDay != null) {
//                    lowerDay += "000000";
                BasicDBObject gte = new BasicDBObject("$gte", lowerDay);
                findObject.append(UserdbKey.USER.REGISTER_DATE, gte);
            }

            if (upperDay != null) {
//                    upperDay += "999999";
                BasicDBObject lt = new BasicDBObject("$lt", upperDay);
                findObject.append(UserdbKey.USER.REGISTER_DATE, lt);
            }
        }

        String fromPurDay = Util.getStringParam(obj, "last_from_pur_day");
        String toPurDay = Util.getStringParam(obj, "last_to_pur_day");
        if (fromPurDay != null && toPurDay != null) {
//                fromPurDay += "000000";
//                toPurDay += "999999";
            BasicDBObject gte = new BasicDBObject("$gte", fromPurDay);
            BasicDBObject lt = new BasicDBObject("$lte", toPurDay);
            ands.add(new BasicDBObject(UserdbKey.USER.LAST_PURCHASE_TIME, lt));
            ands.add(new BasicDBObject(UserdbKey.USER.LAST_PURCHASE_TIME, gte));
            findObject.append("$and", ands);
        } else {
            if (fromPurDay != null) {
//                    fromPurDay += "000000";
                BasicDBObject gte = new BasicDBObject("$gte", fromPurDay);
                findObject.append(UserdbKey.USER.LAST_PURCHASE_TIME, gte);
            }

            if (toPurDay != null) {
//                    toPurDay += "999999";
                BasicDBObject lt = new BasicDBObject("$lte", toPurDay);
                findObject.append(UserdbKey.USER.LAST_PURCHASE_TIME, lt);
            }
        }

        String fromLoginDay = Util.getStringParam(obj, "from_login_day");
        String toLoginDay = Util.getStringParam(obj, "to_login_day");
        if (fromLoginDay != null && toLoginDay != null) {
//                fromPurDay += "000000";
//                toPurDay += "999999";
            BasicDBObject gte = new BasicDBObject("$gte", fromLoginDay);
            BasicDBObject lt = new BasicDBObject("$lte", toLoginDay);
            ands.add(new BasicDBObject(UserdbKey.USER.LAST_LOGIN_TIME, lt));
            ands.add(new BasicDBObject(UserdbKey.USER.LAST_LOGIN_TIME, gte));
            findObject.append("$and", ands);
        } else {
            if (fromLoginDay != null) {
//                    fromPurDay += "000000";
                BasicDBObject gte = new BasicDBObject("$gte", fromLoginDay);
                findObject.append(UserdbKey.USER.LAST_LOGIN_TIME, gte);
            }

            if (toLoginDay != null) {
//                    toPurDay += "999999";
                BasicDBObject lt = new BasicDBObject("$lte", toLoginDay);
                findObject.append(UserdbKey.USER.LAST_LOGIN_TIME, lt);
            }
        }

        Long flag = Util.getLongParam(obj, ParamKey.FLAG);
        if (flag != null) {
            findObject.append(UserdbKey.USER.FLAG, flag);
        }
        String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
        if (cmCode != null) {
            findObject.append(UserdbKey.USER.CM_CODE, cmCode);
        }

        return findObject;
    }

    private static void queryAnd(BasicDBList ands, BasicDBObject obj, String key, Object from, Object to) {
        BasicDBObject objGt = new BasicDBObject();
        BasicDBObject objLt = new BasicDBObject();
        if (from != null && to != null) {
            objGt.append("$gte", from);
            objLt.append("$lte", to);
            ands.add(new BasicDBObject(key, objGt));
            ands.add(new BasicDBObject(key, objLt));
        } else {
            if (from != null) {
                objGt.append("$gte", from);
                obj.append(key, objGt);
            }
            if (to != null) {
                objLt.append("$lte", to);
                obj.append(key, objLt);
            }
        }
    }

    private static BasicDBObject createQueryForSearch(JSONObject obj, BasicDBObject findObject, List<String> purchaseL) {
//           DBObject findObject = QueryBuilder.start(UserdbKey.USER.RELATIONSHIP_STATUS).in(relationship).get();
        String userId = Util.getStringParam(obj, ParamKey.ID);
        if (userId != null && !userId.isEmpty()) {
            ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
            String listUserId[] = userId.split("[,、，､]");
            for (String i : listUserId) {
                if (i != null && !i.trim().isEmpty()) {
                    i = i.trim();
                    for (String str : Constant.SPECIAL_CHARACTER) {
                        if (i.contains(str)) {
                            String string = "\\" + str;
                            i = i.replace(str, string);
                        }
                    }
                    BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                    BasicDBObject findObjectUserId = new BasicDBObject(UserdbKey.USER.USER_ID, regex);
                    listFindObject.add(findObjectUserId);
                }
            }
            findObject.append("$or", listFindObject);
        }
        Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
        String email = Util.getStringParam(obj, ParamKey.EMAIL);
        if (email != null && userType == null) {
            return null;
        }
        if (userType != null) {
            findObject.append(UserdbKey.USER.ACCOUNT_TYPE, userType);
            if (email != null) {
                if (email.trim().isEmpty()) {
                    if (userType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                        findObject.append(UserdbKey.USER.FB_ID, "{}");
                    } else if (userType == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                        findObject.append(UserdbKey.USER.MOCOM_ID, "{}");
                    } else if (userType == Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE) {
                        findObject.append(UserdbKey.USER.FAMU_ID, "{}");
                    } else {
                        findObject.append(UserdbKey.USER.EMAIL, "{}");
                    }
                } else {
                    for (String str : Constant.SPECIAL_CHARACTER) {
                        if (email.contains(str)) {
                            String string = "\\" + str;
                            email = email.replace(str, string);
                        }
                    }
                    BasicDBObject regex = new BasicDBObject("$regex", email.toLowerCase());
                    if (userType == Constant.ACCOUNT_TYPE_VALUE.FB_TYPE) {
                        findObject.append(UserdbKey.USER.FB_ID, regex);
                    } else if (userType == Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE) {
                        findObject.append(UserdbKey.USER.MOCOM_ID, regex);
                    } else if (userType == Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE) {
                        findObject.append(UserdbKey.USER.FAMU_ID, regex);
                    } else {
                        findObject.append(UserdbKey.USER.EMAIL, regex);
                    }
                }
            }
        }
        JSONArray bodyType = (JSONArray) obj.get(UserdbKey.USER.BODY_TYPE);
        if (bodyType != null && !bodyType.isEmpty()){
            BasicDBList list = new BasicDBList();
            list.addAll(bodyType);
            BasicDBObject query = new BasicDBObject("$in", list);
            findObject.append(UserdbKey.USER.BODY_TYPE, query);
        }
        Long gender = Util.getLongParam(obj, ParamKey.GENDER);
        if (gender != null) {
            findObject.append(UserdbKey.USER.GENDER, gender.intValue());
        } else {
            gender = new Long(Constant.GENDER.FEMALE);
        }
        List<Long> region = Util.getListLong(obj, ParamKey.REGION);
        if (region != null) {
            if (!region.isEmpty()) {
                List<Integer> list = new ArrayList<>();
                for (Long re : region) {
                    list.add(re.intValue());
                }
                BasicDBObject inObj = new BasicDBObject("$in", list);
                findObject.append(UserdbKey.USER.REGION, inObj);
            }
        }
        String userName = Util.getStringParam(obj, ParamKey.USER_NAME);
        //abc, cde
        if (userName != null) {
            for (String str : Constant.SPECIAL_CHARACTER) {
                if (userName.contains(str)) {
                    String string = "\\" + str;
                    userName = userName.replace(str, string);
                }
            }
            String[] list = userName.trim().split("\\s+");
            //abc| cde
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                sb.append(list[i].toLowerCase());
                if (i < (list.length - 1)) {
                    sb.append("|");
                }
            }
            //sb= a|b|c
            BasicDBObject regex = new BasicDBObject("$regex", sb.toString());
            findObject.append(UserdbKey.USER.SORT_NAME, regex);
        }

        // LongLT 20Sep2016 ///////////////////////////  #4483
        Object isPurchaseTemp = obj.get(ParamKey.IS_PURCHASE);
        if ((isPurchaseTemp != null) && ("".equals(isPurchaseTemp.toString()))) {
            Integer isPurchase = new Integer(isPurchaseTemp.toString());
            findObject.append(UserdbKey.USER.IS_PURCHASE, isPurchase);
        }

        //Linh #4483
//            Object haveEmailTemp = obj.get("have_email");
//            if ((haveEmailTemp != null) && ("".equals(haveEmailTemp.toString()))) {
//                Integer haveEmail = new Integer(haveEmailTemp.toString());
//                findObject.append("have_email", haveEmail);
//            }
//            else{
//                Integer haveEmail = 0;
//                findObject.append("have_email", haveEmail);
//            }
        BasicDBList ands = new BasicDBList();
        String upperBirthday = Util.getStringParam(obj, "upper_bir");
        String lowerBirthday = Util.getStringParam(obj, "lower_bir");
        queryAnd(ands, findObject, UserdbKey.USER.BIRTHDAY, lowerBirthday, upperBirthday);

        Long lowerPoint = Util.getLongParam(obj, "lower_pnt");
        Long upperPoint = Util.getLongParam(obj, "upper_pnt");
        queryAnd(ands, findObject, UserdbKey.USER.POINT, lowerPoint, upperPoint);
        // thanhdd add #5258
        Long lowerTotalPoint = Util.getLongParam(obj, "user_total_point_from");
        Long upperTotalPoint = Util.getLongParam(obj, "user_total_point_to");
        //Edited by Khanhdd
        queryAnd(ands, findObject, UserdbKey.USER.TOTAL_POINT, lowerTotalPoint, upperTotalPoint);// chua có key cho total point  

        Long lowerTotalPurchase = Util.getLongParam(obj, "user_total_purchase_from");
        Long upperTotalPurchase = Util.getLongParam(obj, "user_total_purchase_to");
        queryAnd(ands, findObject, UserdbKey.USER.TOTAL_PURCHASE, lowerTotalPurchase, upperTotalPurchase);// chua có key cho total purchase  
        //end

        String lowerDay = Util.getStringParam(obj, "from_reg_day");
        String upperDay = Util.getStringParam(obj, "to_reg_day");
        queryAnd(ands, findObject, UserdbKey.USER.REGISTER_DATE, lowerDay, upperDay);

        String fromPurDay = Util.getStringParam(obj, "last_from_pur_day");
        String toPurDay = Util.getStringParam(obj, "last_to_pur_day");
        queryAnd(ands, findObject, UserdbKey.USER.LAST_PURCHASE_TIME, fromPurDay, toPurDay);

        String fromLoginDay = Util.getStringParam(obj, "from_login_day");
        String toLoginDay = Util.getStringParam(obj, "to_login_day");
        queryAnd(ands, findObject, UserdbKey.USER.LAST_LOGIN_TIME, fromLoginDay, toLoginDay);

        if (!ands.isEmpty()) {
            findObject.append("$and", ands);
        }
        String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
//        StringBuilder sb = new StringBuilder();
//        sb.append("/");
//        sb.append(cmCode);
//        sb.append("/");
//        Util.addDebugLog("cmCode " + cmCode);
//        BasicDBObject regex1 = new BasicDBObject("$regex", sb.toString());
//        Util.addDebugLog("cmCode regex " + sb.toString());
//        Util.addDebugLog("cmCode regex1 " + regex1);
        if (cmCode != null && !cmCode.isEmpty()) {
            findObject.append(UserdbKey.USER.CM_CODE, cmCode);
        }
        Util.addDebugLog("findObject regex " + findObject.toString());
        Long flag = Util.getLongParam(obj, ParamKey.FLAG);
        if (flag != null) {
            findObject.append(UserdbKey.USER.FLAG, flag);
        }

        Long deviceType = Util.getLongParam(obj, UserdbKey.USER.DEVICE_TYPE);
        if (deviceType != null) {
            findObject.append(UserdbKey.USER.DEVICE_TYPE, deviceType);
        }

        Long profileImage = Util.getLongParam(obj, "profile_image");
        if (profileImage != null) {
            boolean exists = true;
            if (profileImage == 0) {
                exists = false;
            }
            DBObject checkAvatar = new BasicDBObject("$exists", exists);
            findObject.append(UserdbKey.USER.AVATAR_ID, checkAvatar);
        }

//        String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
//        if (cmCode != null && !cmCode.isEmpty()) {
//            BasicDBObject inObj = new BasicDBObject("$in", cmCode);
//            findObject.append(UserdbKey.USER.CM_CODE, inObj);
//        }
        List<Long> jobs = Util.getListLong(obj, UserdbKey.USER.JOB);
        if (jobs != null && !jobs.isEmpty()) {
            findObject.append(UserdbKey.USER.JOB, new BasicDBObject("$in", jobs));
        }
        List<Long> joins = Util.getListLong(obj, "join_hours");
        if (joins != null && !joins.isEmpty()) {
            findObject.append(UserdbKey.USER.JOINT_HOURS, new BasicDBObject("$in", joins));
        }
        if (gender != Constant.GENDER.MALE) {

//                Long indecent = Util.getLongParam(obj, ParamKey.INDECENT);
//                if (indecent != null) {
//                    findObject.append(UserdbKey.USER.INDECENT, indecent.intValue());
//                }
            List<Long> cups = Util.getListLong(obj, "cup");
            if (cups != null && !cups.isEmpty()) {
                findObject.append(UserdbKey.USER.CUP, new BasicDBObject("$in", cups));
            }
            List<Long> cute_types = Util.getListLong(obj, "cute_type");
            if (cute_types != null && !cute_types.isEmpty()) {
                findObject.append(UserdbKey.USER.CUTETYPE, new BasicDBObject("$in", cute_types));
            }
        }

        Long is_purchase = Util.getLongParam(obj, ParamKey.IS_PURCHASE);
        if (is_purchase == null) {

        } else {
            List<ObjectId> list = new ArrayList<>();
            for (String str : purchaseL) {
                list.add(new ObjectId(str));
            }
            if (is_purchase == Constant.FLAG.ON) {
                findObject.append(UserdbKey.USER.ID, new BasicDBObject("$in", list));
            } else {
                findObject.append(UserdbKey.USER.ID, new BasicDBObject("$nin", list));
            }
        }

        //Linh 10/10/2016 #4431
        String deviceId = Util.getStringParam(obj, UserdbKey.USER.DEVICE_ID);
        if (deviceId != null) {
            findObject.append(UserdbKey.USER.DEVICE_ID, deviceId);
        }
        //
        Long application = Util.getLongParam(obj, "application");
        Util.addDebugLog("application " + application);
        if (application != null) {
            findObject.append(UserdbKey.USER.APPLICATION_ID, application.toString());
        }

        //ThanhDD 06/06/2017 #8588
        String about_me = Util.getStringParam(obj, "about_me");
//        Util.addDebugLog("==========about_me===" + about_me);
        if (about_me != null) {
            for (String strAbt : Constant.SPECIAL_CHARACTER) {
                if (about_me.contains(strAbt)) {
                    String stringAbt = "\\" + strAbt;
                    about_me = about_me.replace(strAbt, stringAbt);
                }
            }
            String[] listAbt = about_me.trim().split("\\s+");
            //abc| cde
            StringBuilder sbAbt = new StringBuilder();
            for (int i = 0; i < listAbt.length; i++) {
                sbAbt.append(listAbt[i].toLowerCase());
                if (i < (listAbt.length - 1)) {
                    sbAbt.append("|");
                }
            }
            //sb= a|b|c
            BasicDBObject regexAbt = new BasicDBObject("$regex", sbAbt.toString());
            findObject.append(UserdbKey.USER.ABOUT, regexAbt);
        }

//        if (about_me != null) {
//            findObject.append(UserdbKey.USER.ABOUT, about_me);
//        }
        String memo = Util.getStringParam(obj, "memo");
//        Util.addDebugLog("==========memo===" + memo);
        if (memo != null) {
            for (String strMe : Constant.SPECIAL_CHARACTER) {
                if (memo.contains(strMe)) {
                    String stringMe = "\\" + strMe;
                    memo = memo.replace(strMe, stringMe);
                }
            }
            String[] listMe = memo.trim().split("\\s+");
            //abc| cde
            StringBuilder sbMe = new StringBuilder();
            for (int i = 0; i < listMe.length; i++) {
                sbMe.append(listMe[i].toLowerCase());
                if (i < (listMe.length - 1)) {
                    sbMe.append("|");
                }
            }
            //sb= a|b|c
            BasicDBObject regexMe = new BasicDBObject("$regex", sbMe.toString());
            findObject.append(UserdbKey.USER.MEMO, regexMe);
        }
        Util.addDebugLog("==========findObject===" + findObject.toString());
//        if (memo != null) {
//            findObject.append(UserdbKey.USER.MEMO, memo);
//        }

        return findObject;
    }

    private static IEntity getInfor(DBObject obj) {

        BaseUser user = new BaseUser();

        user.userId = obj.get(UserdbKey.USER.ID).toString();
        user.cmCode = (String) obj.get(UserdbKey.USER.CM_CODE);
        String email = (String) obj.get(UserdbKey.USER.EMAIL);
        String fbId = (String) obj.get(UserdbKey.USER.FB_ID);
        String mocomId = (String) obj.get(UserdbKey.USER.MOCOM_ID);
        String famuId = (String) obj.get(UserdbKey.USER.FAMU_ID);
        if (fbId != null) {
            user.email = fbId;
            user.userType = Constant.ACCOUNT_TYPE_VALUE.FB_TYPE;
        } else if (mocomId != null) {
            user.email = mocomId;
            user.userType = Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE;
        } else if (famuId != null) {
            user.email = famuId;
            user.userType = Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE;
        } else {
            user.email = email;
            user.userType = Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE;
        }

        user.userName = (String) obj.get(UserdbKey.USER.USERNAME);
        //HUNGDT add 3678
        user.userName = Util.replaceBannedWordBackend(user.userName);
        Integer gender = (Integer) obj.get(UserdbKey.USER.GENDER);
        user.gender = gender;

        Integer location = (Integer) obj.get(UserdbKey.USER.REGION);
        user.region = location;

        String time = (String) obj.get(UserdbKey.USER.BIRTHDAY);
        user.age = Util.convertBirthdayToAge(time);
        user.bir = DateFormat.format(DateFormat.parse_yyyyMMdd(time));

        Integer flg = (Integer) obj.get(UserdbKey.USER.FLAG);
        user.flag = flg;

//        user.height = (Double) obj.get(UserdbKey.USER.HEIGHT);
//        Integer rela = (Integer) obj.get(UserdbKey.USER.RELATIONSHIP_STATUS);
//        user.relationship = rela;
        Integer point = (Integer) obj.get(UserdbKey.USER.POINT);
        if (point != null) {
            user.point = point;
        } else {
            user.point = 0;
        }
        Object ttpoint = obj.get(UserdbKey.USER.TOTAL_POINT);
        if (ttpoint != null) {
            user.total_point = new Double(ttpoint.toString());
        } else {
            user.total_point = 0.0;
        }
        Object ttPurchase = obj.get(UserdbKey.USER.TOTAL_PURCHASE) ;
        if (ttPurchase != null) {
            user.total_purchase = new Double(ttPurchase.toString());
        } else {
            user.total_purchase = 0.0;
        }

        user.registerDate = (String) obj.get(UserdbKey.USER.REGISTER_DATE);

        user.lastPurchaseTime = (String) obj.get(UserdbKey.USER.LAST_PURCHASE_TIME);

        String lastLoginTime = (String) obj.get(UserdbKey.USER.LAST_LOGIN_TIME);
        user.lastLoginTime = lastLoginTime;
        if (lastLoginTime == null) {
            user.lastLoginTime = user.registerDate;
        }
        String firstLoginTime = (String) obj.get(UserdbKey.USER.FIRST_PURCHASE_TIME);
        user.firstPurchaseTime = firstLoginTime;
//        String bir = (String) obj.get(UserdbKey.USER.BIRTHDAY);

        String memo = (String) obj.get(UserdbKey.USER.MEMO);
        user.memo = memo;

        String about_me = (String) obj.get(UserdbKey.USER.ABOUT);
        user.about = about_me;
        user.verification_flag = (Integer) obj.get(UserdbKey.USER.VERIFICATION_FLAG);
        //HUNGDT edit
        user.application_id = "1";
        if (obj.get(UserdbKey.USER.APPLICATION_ID) != null) {
            user.application_id = obj.get(UserdbKey.USER.APPLICATION_ID).toString();
        }
        String app_id = "1";
        if (user.application_id != null) {
            app_id = user.application_id.toString();
        }
        user.applicationName = ApplicationDAO.getUniqueNameById(app_id);

        return user;
    }

    // Add LongLT 24Aug2016
    public static DBObject getUserNotificationSetting(String userId) {
        DBObject result;
        try {
            //command to take profile of friend
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            result = DBManager.getUserDB().getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION).findOne(query);

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = null;
        }
        return result;
    }

    private static IEntity getDetailInfor(DBObject obj) {
        User user;
        Integer gender = (Integer) obj.get(UserdbKey.USER.GENDER);
        if (gender == Constant.GENDER.MALE) {
            user = new Male();
        } else {
            user = new Female();
        }

        user.applicationId = "1";
        if (obj.get(UserdbKey.USER.APPLICATION_ID) != null) {
            user.applicationId = obj.get(UserdbKey.USER.APPLICATION_ID).toString();
        }
        String app_id = "1";
        if (user.applicationId != null) {
            app_id = user.applicationId.toString();
        }
        user.applicationName = ApplicationDAO.getUniqueNameById(app_id);

        // LongLT 20Sep2016 ///////////////////////////  #4483
        if (obj.get(UserdbKey.USER.IS_PURCHASE) != null) {
            user.isPurchase = (Integer) obj.get(UserdbKey.USER.IS_PURCHASE);
        }
        Integer siteId = (Integer) obj.get(UserdbKey.USER.SITE_ID);
        if (siteId != null) {
            user.site_id = siteId;
        }

        user.application_version = (String) obj.get(UserdbKey.USER.APP_VERSION);
        user.os_version = (String) obj.get(UserdbKey.USER.OS_VERSION);
        user.device_name = (String) obj.get(UserdbKey.USER.DEVICE_NAME);
        user.ad_id = (String) obj.get(UserdbKey.USER.AdId);
        user.gender = new Long(gender);

        Integer bdy_type = (Integer) obj.get(UserdbKey.USER.BODY_TYPE);
        if (bdy_type != null) {
            user.body_type = new Long(bdy_type);
        } else {
            user.body_type = -1L;
        }
        user.userId = obj.get(UserdbKey.USER.ID).toString();
        user.cmCode = (String) obj.get(UserdbKey.USER.CM_CODE);

        // Add LongLT 24Aug2016
        DBObject notiObject = getUserNotificationSetting(user.userId);
        if (notiObject != null) {
            user.notificationBuzz = (Integer) notiObject.get(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ);
            user.notificationChat = (Integer) notiObject.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT);
            user.eazyAlert = (Integer) notiObject.get(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT);
        }

        String email = (String) obj.get(UserdbKey.USER.EMAIL);
        String fbId = (String) obj.get(UserdbKey.USER.FB_ID);
        String mocomId = (String) obj.get(UserdbKey.USER.MOCOM_ID);
        String famuId = (String) obj.get(UserdbKey.USER.FAMU_ID);
        if (fbId != null) {
            user.email = fbId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FB_TYPE);
        } else if (mocomId != null) {
            user.email = mocomId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE);
        } else if (famuId != null) {
            user.email = famuId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE);
        } else {
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE);
            user.email = email;
        }

        user.username = (String) obj.get(UserdbKey.USER.USERNAME);
        if (user.username != null) {
            user.username_repls = Util.replaceBannedWordBackend(user.username);
        }

        user.avatarId = (String) obj.get(UserdbKey.USER.AVATAR_ID);

        user.originalPassword = (String) obj.get(UserdbKey.USER.ORIGINAL_PASSOWRD);

        Integer location = (Integer) obj.get(UserdbKey.USER.REGION);
        if (location != null) {
            user.location = new Long(location);
        }

        Integer flg = (Integer) obj.get(UserdbKey.USER.FLAG);
        user.flag = new Long(flg);

        user.registerDate = (String) obj.get(UserdbKey.USER.REGISTER_DATE);

        user.lastPurchaseTime = (String) obj.get(UserdbKey.USER.LAST_PURCHASE_TIME);

        //HUNGDT add
        user.about = (String) obj.get(UserdbKey.USER.ABOUT);
        if (user.about != null) {
//            if (!Util.replaceBannedWordBackend(user.about).equals(user.about)) {
            user.about_repls = Util.replaceBannedWordBackend(user.about);
            //"「(" + 
            //           } else {
            //               user.about_repls = user.about;
            //           }
        }
        user.birthday = DateFormat.format(DateFormat.parse_yyyyMMdd((String) obj.get(UserdbKey.USER.BIRTHDAY)));

        Integer job = (Integer) obj.get(UserdbKey.USER.JOB);
        if (job != null) {
            user.job = new Long(job);
        }
        //Thanhdd fix #5190 03/11/2016
        SizedListData data = null;
        //Thanhdd fix #5184 09/11/2016
        SizedListData dataPurchase = null;
        int totalPC_Apple;
        int totalPC_Google;
        int dataPC_CreditCard;
        int dataPC_Bitcach;
        int dataPC_Convenience;
        int dataPC_PointsBack;
        int dataPC_CCheck;

        try {
            data = LogPointDAO.listLogTotalPoint(user.userId);
            dataPurchase = TransactionLogDAO.listLogTotalPrice(user.userId);
            user.totalPoint = data.totalPoint;
            user.totalPurchaseUserDetail = Integer.valueOf(dataPurchase.totalPrice.intValue());//thanhdd add
            totalPC_Apple = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 0).totalPrice.intValue());
            user.totalPurchaseAapple = totalPC_Apple;
            totalPC_Google = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 1).totalPrice.intValue());
            user.totalPurchaseGoogle = totalPC_Google;
            dataPC_CreditCard = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 2).totalPrice.intValue());
            user.totalPurchaseCreditCard = dataPC_CreditCard;
            dataPC_Bitcach = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 3).totalPrice.intValue());
            user.totalPurchaseBitcach = dataPC_Bitcach;
            dataPC_Convenience = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 4).totalPrice.intValue());
            user.totalPurchaseConvenience = dataPC_Convenience;
            dataPC_PointsBack = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 5).totalPrice.intValue());
            user.totalPurchasePointsBack = dataPC_PointsBack;
            dataPC_CCheck = Integer.valueOf(TransactionLogDAO.listLogTotalPrice(user.userId, 6).totalPrice.intValue());
            user.totalPurchaseCCheck = dataPC_CCheck;

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }

        Integer point = (Integer) obj.get(UserdbKey.USER.POINT);
        user.point = new Long(point);
        user.videoCall = (Boolean) obj.get(UserdbKey.USER.VIDEO_CALL_WAITING);
        user.voiceCall = (Boolean) obj.get(UserdbKey.USER.VOICE_CALL_WAITING);
        String memo = (String) obj.get(UserdbKey.USER.MEMO);
        if (memo != null) {
            user.memo = memo;
        }
        Integer verification = (Integer) obj.get(UserdbKey.USER.VERIFICATION_FLAG);

        //Linh 10/10/2016 #4431
        String deviceID = (String) obj.get(UserdbKey.USER.DEVICE_ID);
//        String deviceID = "abczyx";
        if (deviceID != null) {
            user.deviceId = deviceID;
        }
        //

        Integer deviceType = (Integer) obj.get(UserdbKey.USER.DEVICE_TYPE);
        if (deviceType != null) {
            user.deviceType = deviceType;
        }

        if (verification != null) {
            user.verificationFlag = new Long(verification);
        }
        Integer finishFlag = (Integer) obj.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
        if (finishFlag != null) {
            user.finishRegisterFlag = new Long(finishFlag);
        }

        if (user instanceof Male) {
            Male male = (Male) user;
            String hobby = (String) obj.get(UserdbKey.USER.HOBBY);
            if (hobby != null) {
                male.hobby = hobby;
            }
        } else {
            Female female = (Female) user;
            Integer cup = (Integer) obj.get(UserdbKey.USER.CUP);
            if (cup != null) {
                female.cup = new Long(cup);
            }
            Integer joins = (Integer) obj.get(UserdbKey.USER.JOINT_HOURS);
            if (joins != null) {
                female.joinHours = new Long(joins);
            }

            Integer cuteType = (Integer) obj.get(UserdbKey.USER.CUTETYPE);
            if (cuteType != null) {
                female.cuteType = new Long(cuteType);
            }
            female.typeOfMan = (String) obj.get(UserdbKey.USER.TYPEOFMANS);
            female.fetishs = (String) obj.get(UserdbKey.USER.FETISH);
            BasicDBList meas = (BasicDBList) obj.get(UserdbKey.USER.MEASUREMENTS);
            List<Long> measList = new ArrayList<>();
            if (meas != null) {
                for (Object mea : meas) {
                    Integer d = (Integer) mea;
                    measList.add(new Long(d));
                }
            }
            String hobby = (String) obj.get(UserdbKey.USER.HOBBY);
            if (hobby != null) {
                female.hobby = hobby;
            }
//            Integer indecent = (Integer) obj.get(UserdbKey.USER.INDECENT);
//            if (indecent != null) {
//                female.indecent = new Long(indecent);
//            }
            female.measurements = measList;
        }
        return user;
    }

    private static IEntity getConnectionInfor(DBObject obj) {
        User user = new User();

        user.userId = obj.get(UserdbKey.USER.ID).toString();
        user.cmCode = (String) obj.get(UserdbKey.USER.CM_CODE);
        String email = (String) obj.get(UserdbKey.USER.EMAIL);
        String fbId = (String) obj.get(UserdbKey.USER.FB_ID);
        String mocomId = (String) obj.get(UserdbKey.USER.MOCOM_ID);
        String famuId = (String) obj.get(UserdbKey.USER.FAMU_ID);
        if (fbId != null) {
            user.email = fbId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FB_TYPE);
        } else if (mocomId != null) {
            user.email = mocomId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.MOCOM_TYPE);
        } else if (famuId != null) {
            user.email = famuId;
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.FAMU_TYPE);
        } else {
            user.userType = new Long(Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE);
            user.email = email;
        }

        user.username = (String) obj.get(UserdbKey.USER.USERNAME);

        Integer favNum = (Integer) obj.get(UserdbKey.USER.FAVOURIST_NUMBER);
        if (favNum == null) {
            user.favouristNumber = new Long(0);
        } else {
            user.favouristNumber = favNum.longValue();
        }

        Integer fvt = (Integer) obj.get(UserdbKey.USER.FAVOURITED_NUMBER);
        if (fvt == null) {
            user.favouritedNumber = new Long(0);
        } else {
            user.favouritedNumber = fvt.longValue();
        }

        return user;
    }

    public static Map<String, String> getUserName(List<String> listFriendId) throws EazyException {
        Map<String, String> result = new TreeMap<>();
        try {
            //command to take profile of friend
            List<ObjectId> listId = new ArrayList<>();
            //thanhdd fixbug #4884
            // if (listFriendId.size()>0){
            for (String friend : listFriendId) {
                if (ObjectId.isValid(friend)) {
                    listId.add(new ObjectId(friend));
                }
            }
            // }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor friendCursor = coll.find(query).sort(sortObject);
            //set to list
            while (friendCursor.hasNext()) {
                DBObject dbObject = friendCursor.next();
                String userId = dbObject.get(UserdbKey.USER.ID).toString();
                String userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                result.put(userId, userName);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean decreaseFieldList(String userId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            //update command
            DBObject obD = coll.findOne(updateQuery);
            Integer point = 0;
            if (obD != null) {
                Object obj = obD.get(fieldname);
                if (obj == null) {
                    point = 0;
                } else {
                    point = Integer.parseInt(obj.toString());
                }
            }
            if (num > point) {
                num = point;
            }
            BasicDBObject obj = new BasicDBObject(fieldname, (0 - num));
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static boolean increaseFieldInt(String userId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(fieldname, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    private static boolean increaseFieldDouble(String userId, String fieldname, double num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.USER.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(fieldname, num);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean increaseTotalPoint(String userId, double num) throws EazyException {
        boolean result = false;
        try {
            increaseFieldDouble(userId, UserdbKey.USER.TOTAL_POINT, num);           
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static boolean increaseTotalPurchase(String userId, double num) throws EazyException {
        boolean result = false;
        try {
            increaseFieldDouble(userId, UserdbKey.USER.TOTAL_PURCHASE, num);           
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean addPbImage(String userId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldInt(userId, UserdbKey.USER.PB_IMAGE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean addBuzz(String userId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldInt(userId, UserdbKey.USER.BUZZ_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean addBackstage(String userId) throws EazyException {
        boolean result = false;
        try {
            result = increaseFieldInt(userId, UserdbKey.USER.BACKSTAGE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removePbImage(String userId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(userId, UserdbKey.USER.PB_IMAGE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean removeBuzz(String userId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(userId, UserdbKey.USER.BUZZ_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean isAvatar(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            DBObject obj = coll.findOne(query);
            String avatarId = (String) obj.get(UserdbKey.USER.AVATAR_ID);
            if (avatarId != null) {
                if (avatarId.equals(imageId)) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeAvatar(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            query.append(UserdbKey.USER.AVATAR_ID, imageId);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.AVATAR_ID, Constant.FLAG.ON);
            BasicDBObject command = new BasicDBObject("$unset", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeBackstage(String userId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(userId, UserdbKey.USER.BACKSTAGE_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static boolean updateAvatar(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.AVATAR_ID, imageId);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateTotalPurchaseAndPoint(String userId, int purchaseAmount, int point) throws EazyException {
        boolean result = false;
        try {            
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);            
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.TOTAL_PURCHASE, purchaseAmount);
            addition.append(UserdbKey.USER.TOTAL_POINT, point);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateReviewField(String userId, ReviewingUser user) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject updateObj = new BasicDBObject();
            if (user.about != null) {
//               if (!"".equals(user.about)) {
//                    user.about = user.about.replaceAll("\\\n/******** 痴漢後 ********/\n.*", "");
//                }
                updateObj.append(UserdbKey.USER.ABOUT, user.about);
            }
            if (user.hobby != null) {
                updateObj.append(UserdbKey.USER.HOBBY, user.hobby);
            }
            if (user.fetish != null) {
                updateObj.append(UserdbKey.USER.FETISH, user.fetish);
            }
            if (user.typeOfMan != null) {
                updateObj.append(UserdbKey.USER.TYPEOFMANS, user.typeOfMan);
            }
            if (!updateObj.isEmpty()) {
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(query, setObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Point addPoint(String userId, int point) throws EazyException {
        Point result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll.findOne(query);
            Integer pnt = (Integer) obj.get(UserdbKey.USER.POINT);
            int updatePoint = point;
            if (pnt != null) {
                if (pnt + point < 0) {
                    updatePoint = 0;
                } else {
                    updatePoint += pnt;
                }
            }
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.POINT, updatePoint);
            BasicDBObject command = new BasicDBObject("$set", addition);
            if(point > 0){
                command.append("$inc", new BasicDBObject(UserdbKey.USER.TOTAL_POINT, point));
            }
            coll.update(query, command);
            result = new Point(pnt, point, updatePoint);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getBlackList() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.DEACITIVE || flag == Constant.USER_STATUS_FLAG.DISABLE)) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                    result.add(userId);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getPurchaseUsers() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find(new BasicDBObject(UserdbKey.USER.HAVE_PURCHASE, true));
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
                result.add(userId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean resetPassword(String userId, String newPassword, String originalPassword) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject findObj = coll.findOne(obj);
            if (findObj == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            } else {
                int type = (int) findObj.get(UserdbKey.USER.ACCOUNT_TYPE);
                if (type != Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE) {
                    throw new EazyException(ErrorCode.UNKNOWN_ERROR);
                }
            }
            if (newPassword == null || newPassword.length() < 6 || originalPassword == null || originalPassword.length() < 6) {
                throw new EazyException(ErrorCode.INVALID_PASSWORD);
            }
            byte[] bt = md.digest(newPassword.getBytes());
            String encryptedPassword = Util.byteToString(bt);
            BasicDBObject changePass = new BasicDBObject(UserdbKey.USER.PASSWORD, encryptedPassword);
            changePass.append(UserdbKey.USER.SIP_PASSWORD, newPassword);
            changePass.append(UserdbKey.USER.ORIGINAL_PASSOWRD, originalPassword);
//            changePass.append(UserdbKey.USER.UPDATE_EMAIL_FLAG, Constant.YES);
            BasicDBObject updateObj = new BasicDBObject("$set", changePass);
            coll.update(obj, updateObj);
            result = true;

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Long getUserGender(String userId) {
        DBObject query = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId));
        DBObject user = coll.findOne(query);
        return new Long(user.get(UserdbKey.USER.GENDER).toString());
    }

    //HUNGDT add
    public static User getUserInfor(String userId) throws EazyException {
        User result = new User();
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            UserDB user = UserDB.fromDBObject(dbOject);
            result = user.getUserInfor();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, String> getListName(List<String> listUserId) throws EazyException {
        Map<String, String> result = new TreeMap<>();
        try {
            List<ObjectId> listId = new ArrayList<>();
            for (String uId : listUserId) {
                listId.add(new ObjectId(uId));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER.ID).in(listId).get();
            DBObject sortObject = new BasicDBObject(UserdbKey.USER.SORT_NAME, 1);
            DBCursor cursor = coll.find(query).sort(sortObject);
            
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String userId = (String) dbObject.get(UserdbKey.USER.USER_ID);
                String userName = (String) dbObject.get(UserdbKey.USER.USERNAME);
                result.put(userId, userName);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
