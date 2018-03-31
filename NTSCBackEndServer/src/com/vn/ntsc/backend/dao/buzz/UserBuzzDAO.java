/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Iterator;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;

/**
 *
 * @author DuongLTD
 */
public class UserBuzzDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.USER_BUZZ_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<Buzz> getListBuzz(List<String> listUser, Long fromTime, Long toTime, Long buzzType, Long buzzStatus) throws EazyException {
        List<Buzz> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listUser != null) {
                List<ObjectId> listId = new ArrayList<>();
                for (String listUser1 : listUser) {
                    listId.add(new ObjectId(listUser1));
                }
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(BuzzdbKey.USER_BUZZ.ID, inObj);
            }
            DBCursor cursor;
            if (findObject.isEmpty()) {
                cursor = coll.find();
            } else {
                cursor = coll.find(findObject);
            }
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if (listBuzz != null) {
                    for (int i = listBuzz.size() - 1; i > -1; i--) {
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        //Util.addDebugLog("====getListBuzz flag:" + flag);
                        if (flag == null) {
                            System.out.println("userId : " + userId);
                        }
                        
                        Integer buType = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                        Integer approved = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        //Util.addDebugLog("====getListBuzz approved:" + approved);
                        //int approved =buzzObj.getInt(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);

                        if ((buType != null && approved != null) && (buzzType == null || buType == buzzType.intValue())) {
                            //if ((buzzType == null || buType == buzzType.intValue())) {
                            Long postTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.POST_TIME);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
//                            String postTimeStr = DateFormat.format(postTime);
                            String buzzTimeStr = DateFormat.format(buzzTime);
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Buzz buzz = new Buzz();
                            buzz.buzzId = buzzId;
                            buzz.buzzTime = buzzTimeStr;
                            buzz.userId = userId;
                            if (flag != Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG && approved == 1) {
                                buzz.isDeleted = 1;
                            }

                            if (buzzStatus == 2) {
                                //if (approved == 1) {
                                if (flag == 0 && approved == 1) {
                                    buzz.isDeleted = 1;
                                    Util.addDebugLog("============isDeleted=============");
                                } else if (flag == 1 && approved == 0) {
                                    buzz.buzzStatus = 0;
                                } else if (approved == -1) {
                                    buzz.buzzStatus = -1;
                                } else if (approved == 1 && flag == 1) {
                                    buzz.buzzStatus = 1;
                                }
                                Util.addDebugLog("=========================");
                                if (fromTime != null && toTime != null) {
                                    if (postTime >= fromTime && postTime <= toTime) {
                                        result.add(buzz);
                                    }
                                } else if (fromTime != null) {
                                    if (postTime >= fromTime) {
                                        result.add(buzz);
                                    }
                                } else if (toTime != null) {
                                    if (postTime <= toTime) {
                                        result.add(buzz);
                                    }
                                } else {
                                    result.add(buzz);
                                }
//                                }
                            } else if (buzzStatus == 1) {
                                Util.addDebugLog("============Approved=============");
                                if (approved == 1) {
                                    buzz.buzzStatus = 1;
                                    if (fromTime != null && toTime != null) {
                                        if (postTime >= fromTime && postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else if (fromTime != null) {
                                        if (postTime >= fromTime) {
                                            result.add(buzz);
                                        }
                                    } else if (toTime != null) {
                                        if (postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else {
                                        result.add(buzz);
                                    }
                                }
                            } else if (buzzStatus == 0) {
                                if (approved == 0 && flag == 1) {
                                    buzz.buzzStatus = 0;
                                    Util.addDebugLog("============Pending=============");
                                    if (fromTime != null && toTime != null) {
                                        if (postTime >= fromTime && postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else if (fromTime != null) {
                                        if (postTime >= fromTime) {
                                            result.add(buzz);
                                        }
                                    } else if (toTime != null) {
                                        if (postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else {
                                        result.add(buzz);
                                    }
                                }
                            } else if (buzzStatus == -1) {
                                if (approved == -1 && flag > -1) {
                                    buzz.buzzStatus = -1;
                                    Util.addDebugLog("============Denied=============");
                                    if (fromTime != null && toTime != null) {
                                        if (postTime >= fromTime && postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else if (fromTime != null) {
                                        if (postTime >= fromTime) {
                                            result.add(buzz);
                                        }
                                    } else if (toTime != null) {
                                        if (postTime <= toTime) {
                                            result.add(buzz);
                                        }
                                    } else {
                                        result.add(buzz);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

//Linh added to increase performance
    public static List<Buzz> getListBuzzVer2(List<String> listUser, Long fromTime, Long toTime, Long buzzType, Long buzzStatus, Long isDeleted) throws EazyException {
        List<Buzz> result = new ArrayList<>();
        try {
            BasicDBObject unwind = new BasicDBObject("$unwind", "$" + BuzzdbKey.USER_BUZZ.BUZZ_LIST);
            BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.POST_TIME, -1));

            BasicDBObject findObject = new BasicDBObject();
            addQueryToSearchByUserId(findObject, listUser);
            addQueryToSearchByApproveFlag(findObject, buzzStatus);
//            addQueryToSearchByFlag(findObject, isDeleted);
            addQueryToSearchByTime(findObject, fromTime, toTime);
            addQueryToSearchByType(findObject, buzzType);

            BasicDBObject match = new BasicDBObject("$match", findObject);
            Util.addDebugLog("Search buzz query: " + match);
            AggregationOutput output = coll.aggregate(unwind, match, sort);
            Iterator<DBObject> outputResult = output.results().iterator();

            while (outputResult.hasNext()) {
                DBObject obj = outputResult.next();
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                DBObject buzzObj = (DBObject) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                Long postTime = (Long) buzzObj.get(BuzzdbKey.USER_BUZZ.POST_TIME);
                Long buzzTime = (Long) buzzObj.get(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
                Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                String buzzTimeStr = DateFormat.format(buzzTime);
                String buzzId = (String) buzzObj.get(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                Buzz buzz = new Buzz();
                buzz.buzzId = buzzId;
                buzz.buzzTime = buzzTimeStr;
                buzz.userId = userId;
                if (flag == 1) {
                    buzz.isDeleted = 0;
                } else {
                    buzz.isDeleted = 1;
                }

                result.add(buzz);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static BasicDBObject addQueryToSearchByUserId(BasicDBObject findObject, List<String> listUser) {
        if (listUser != null) {
            List<ObjectId> listId = new ArrayList<>();
            for (String listUser1 : listUser) {
                listId.add(new ObjectId(listUser1));
            }
            BasicDBObject inObj = new BasicDBObject("$in", listId);
            findObject.append(BuzzdbKey.USER_BUZZ.ID, inObj);
        }
        return findObject;
    }

    private static BasicDBObject addQueryToSearchByApproveFlag(BasicDBObject findObject, Long buzzStatus) {
        if (buzzStatus <= 1 && buzzStatus >= -1) {
            findObject.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG, buzzStatus);
        }
        return findObject;
    }

    private static BasicDBObject addQueryToSearchByFlag(BasicDBObject findObject, Long isDeleted) {
        if (isDeleted == 1) {
            findObject.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG, 0);
        } else {
            findObject.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG, 1);
        }
        return findObject;
    }

    private static BasicDBObject addQueryToSearchByTime(BasicDBObject findObject, Long fromTime, Long toTime) {
        if (fromTime != null || toTime != null) {
            BasicDBObject timequery = new BasicDBObject();
            if (fromTime != null) {
                timequery = new BasicDBObject("$gt", fromTime);
            }
            if (toTime != null) {
                timequery = new BasicDBObject("$lt", toTime);
            }
            findObject.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.POST_TIME, timequery);
        }
        return findObject;
    }

    private static BasicDBObject addQueryToSearchByType(BasicDBObject findObject, Long buzzType) {
        if (buzzType != null) { // search buzz_type
            findObject.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST + "." + BuzzdbKey.USER_BUZZ.BUZZ_TYPE, buzzType);
        }
        return findObject;
    }

    // thanhdd add #5214
    public static List<Buzz> getListBuzzByOrder(List<String> listUser, Long fromTime, Long toTime, Long buzzType, Long order) throws EazyException {
        List<Buzz> result = new ArrayList<>();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (listUser != null) {
                List<ObjectId> listId = new ArrayList<>();
                for (String listUser1 : listUser) {
                    listId.add(new ObjectId(listUser1));
                }
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(BuzzdbKey.USER_BUZZ.ID, inObj);
            }
            DBCursor cursor;
            if (findObject.isEmpty()) {
                cursor = coll.find();
            } else {
                cursor = coll.find(findObject);
            }
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(BuzzdbKey.USER_BUZZ.ID).toString();
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if (listBuzz != null) {
                    for (int i = listBuzz.size() - 1; i > -1; i--) {
                        BasicDBObject buzzObj = (BasicDBObject) listBuzz.get(i);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if (flag == null) {
                            System.out.println("userId : " + userId);
                        }

                        Integer buType = buzzObj.getInt(BuzzdbKey.USER_BUZZ.BUZZ_TYPE);
                        if (flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG && (buzzType == null || buType == buzzType.intValue())) {
                            Long postTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.POST_TIME);
                            Long buzzTime = buzzObj.getLong(BuzzdbKey.USER_BUZZ.BUZZ_TIME);
//                            String postTimeStr = DateFormat.format(postTime);
                            String buzzTimeStr = DateFormat.format(buzzTime);
                            String buzzId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                            Buzz buzz = new Buzz();
                            buzz.buzzId = buzzId;
                            buzz.buzzTime = buzzTimeStr;
                            buzz.userId = userId;
                            if (fromTime != null && toTime != null) {
                                if (postTime >= fromTime && postTime <= toTime) {
                                    result.add(buzz);
                                }
                            } else if (fromTime != null) {
                                if (postTime >= fromTime) {
                                    result.add(buzz);
                                }
                            } else if (toTime != null) {
                                if (postTime <= toTime) {
                                    result.add(buzz);
                                }
                            } else {
                                result.add(buzz);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delBuzz(String userId, String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
//            DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
//            BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
//            BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", buzzer );
//            coll.update(findObj, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApproveFlag(String userId, String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
//            DBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
//            BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
//            BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
//            BasicDBObject updateCommand = new BasicDBObject("$pull", buzzer );
//            coll.update(findObj, updateCommand);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            BasicDBObject buzzObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzObj);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.APPROVED_FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, flag);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateBuzzActivity(String buzzId, String userId, long time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                boolean check = false;
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                if (listBuzz != null) {
                    for (Object listBuzz1 : listBuzz) {
                        BasicDBObject buzzer = (BasicDBObject) listBuzz1;
                        String buId = (String) buzzer.get(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                        if (buId.equals(buzzId)) {
                            check = true;
                            break;
                        }
                    }
                }
                if (check) {
                    BasicDBObject buzzerObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                    BasicDBObject elemMatch = new BasicDBObject("$elemMatch", buzzerObj);
                    findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST, elemMatch);
                    String field = BuzzdbKey.USER_BUZZ.BUZZ_LIST + ".$." + BuzzdbKey.USER_BUZZ.BUZZ_TIME;
                    BasicDBObject updateObj = new BasicDBObject(field, time);
                    BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                    coll.update(findObj, setObj);
                } else {
                    BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TIME, time);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.POST_TIME, time);
                    buzzElement.append(BuzzdbKey.USER_BUZZ.FLAG, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
                    BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
                    BasicDBObject updateCommand = new BasicDBObject("$push", buzzer);
                    coll.update(findObj, updateCommand);
                }
            } else {
                BasicDBObject buzzElement = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
                buzzElement.append(BuzzdbKey.USER_BUZZ.BUZZ_TIME, time);
                buzzElement.append(BuzzdbKey.USER_BUZZ.POST_TIME, time);
                BasicDBObject buzzer = new BasicDBObject(BuzzdbKey.USER_BUZZ.BUZZ_LIST, buzzElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", buzzer);
                coll.update(findObj, updateCommand, true, false);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isAppearBuzz(String userId, String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.USER_BUZZ.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                BasicDBList listBuzz = (BasicDBList) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_LIST);
                for (Object buzz : listBuzz) {
                    BasicDBObject buzzObj = (BasicDBObject) buzz;
                    String buId = buzzObj.getString(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                    if (buId.equals(buzzId)) {
                        Integer approvedFlag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.APPROVED_FLAG);
                        Integer flag = (Integer) buzzObj.get(BuzzdbKey.USER_BUZZ.FLAG);
                        if ((approvedFlag == null || approvedFlag == Constant.REVIEW_STATUS_FLAG.APPROVED) && (flag == null || flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateFlagUserBuzz(String userId, String buzzId, Integer flag){
        try{
            ObjectId id = new ObjectId(userId);
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.USER_BUZZ.ID, id);
            findObj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+"."+BuzzdbKey.USER_BUZZ.BUZZ_ID, buzzId);
            
            BasicDBObject obj = new BasicDBObject();
            obj.append(BuzzdbKey.USER_BUZZ.BUZZ_LIST+".$."+BuzzdbKey.USER_BUZZ.FLAG, flag);
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append("$set", obj);
            
            coll.update(findObj, updateObj, true, false);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
