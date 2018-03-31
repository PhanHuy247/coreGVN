/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Objects;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.user.User;
import eazycommon.constant.mongokey.StaticFiledbKey;
import org.bson.types.ObjectId;

/**
 *
 * @author DuongLTD
 */
public class ImageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.IMAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean removeImage(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
            seachObj.append(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.FLAG, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Image getImageInfor(String imageId) throws EazyException {
        Image result = null;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            DBObject obj = coll.findOne(seachObj);
            if (obj != null) {
                String userId = (String) obj.get(UserdbKey.IMAGE.USER_ID);
                Integer imageType = (Integer) obj.get(UserdbKey.IMAGE.IMAGE_TYPE);
                Integer imageStatus = (Integer) obj.get(UserdbKey.IMAGE.STATUS);
                Integer avatarFlag = (Integer) obj.get(UserdbKey.IMAGE.AVATAR_FLAG);
                //Integer appFlag = (Integer) obj.get(UserdbKey.IMAGE.APPROVED_FLAG);
                Integer flag = (Integer) obj.get(UserdbKey.IMAGE.FLAG);
                Long uploadTime = (Long) obj.get(UserdbKey.IMAGE.UPLOAD_TIME);
                Long reportTime = (Long) obj.get(UserdbKey.IMAGE.REPORT_TIME);
                Integer reportFlag = (Integer) obj.get(UserdbKey.IMAGE.REPORT_FLAG);
                Integer appearFlag = (Integer) obj.get(UserdbKey.IMAGE.APPEAR_FLAG);
                Integer deniedFlag = (Integer) obj.get(UserdbKey.IMAGE.DENIED_FLAG);
                result = new Image(userId, imageId, imageType, imageStatus, avatarFlag,
                        flag, uploadTime, appearFlag, reportFlag, reportTime);
                result.deniedFlag = deniedFlag;
            } else {
                Util.addInfoLog("Image not found it : " + imageId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static SizedListData searchImage(Long type, Long imageType, String userId, String imageId, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (type == null) {
                return null;
            }
            findObject.put(UserdbKey.IMAGE.STATUS, type.intValue());
            if (imageType != null) {
                findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, imageType.intValue());
            } else {
                DBObject neObj = new BasicDBObject("$ne", 0);
                findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, neObj);
            }
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
                findObject.put("$or", listFindObject);
            }
            if (imageId != null) {
                findObject.put(UserdbKey.IMAGE.IMAGE_ID, imageId);
            }
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
//            DBObject neObj = new BasicDBObject("$ne", 0);
//            findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, neObj);
            findObject.put(UserdbKey.IMAGE.FLAG, Constant.FLAG.ON);
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.IMAGE.UPLOAD_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.IMAGE.REVIEW_TIME, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Image image = new Image();
                String imgId = (String) dbObject.get(UserdbKey.IMAGE.IMAGE_ID);
                image.imageId = imgId;
                String uId = (String) dbObject.get(UserdbKey.IMAGE.USER_ID);
                image.userId = uId;
                Integer imgType = (Integer) dbObject.get(UserdbKey.IMAGE.IMAGE_TYPE);
                image.imageType = imgType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.IMAGE.UPLOAD_TIME);
                if (uploadTime != null) {
                    image.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Long reviewTime = (Long) dbObject.get(UserdbKey.IMAGE.REVIEW_TIME);
                if (reviewTime != null) {
                    image.reviewTimeStr = DateFormat.format(reviewTime);
                }
                list.add(image);

            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    // Linh 2016/12/15 #5677
    public static SizedListData searchImageVer2(Long type, Long imageType, String userId, String imageId, Long sort, Long order, int skip, int take, Long gender) throws EazyException {
        SizedListData result = new SizedListData();

        try {
            DBObject findObject = new BasicDBObject();
            //thanhdd fix #6729
            if (type == 2) {
                DBObject neObj = new BasicDBObject("$ne", type.intValue());
                findObject.put(UserdbKey.IMAGE.STATUS, neObj);
                Util.addDebugLog("=====searchImageVer2 type == 2 findObject:"+findObject.toString());
            } else {
                findObject.put(UserdbKey.IMAGE.STATUS, type.intValue());
                Util.addDebugLog("=====searchImageVer2 findObject:"+findObject.toString());
            }
            //end
            if (imageType != null) {
                findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, imageType.intValue());
            } else {
                DBObject neObj = new BasicDBObject("$ne", 0);
                findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, neObj);
            }
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
                findObject.put("$or", listFindObject);
            }
            if (imageId != null) {
                findObject.put(UserdbKey.IMAGE.IMAGE_ID, imageId);
            }
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
            if (gender != null && !gender.equals(new Long(2))){
                findObject.put(UserdbKey.IMAGE.GENDER, gender);
            }
//            DBObject neObj = new BasicDBObject("$ne", 0);
//            findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, neObj);
            findObject.put(UserdbKey.IMAGE.FLAG, Constant.FLAG.ON);
            Util.addDebugLog("========searchImageVer2 findObject 1111"+findObject.toString());
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.IMAGE.UPLOAD_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.IMAGE.REVIEW_TIME, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            Integer number = cursor.size();
            //set to list
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Image image = new Image();
                String imgId = (String) dbObject.get(UserdbKey.IMAGE.IMAGE_ID);
                image.imageId = imgId;
                String uId = (String) dbObject.get(UserdbKey.IMAGE.USER_ID);
                image.userId = uId;
                Integer imgType = (Integer) dbObject.get(UserdbKey.IMAGE.IMAGE_TYPE);
                image.imageType = imgType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.IMAGE.UPLOAD_TIME);
                if (uploadTime != null) {
                    image.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Long reviewTime = (Long) dbObject.get(UserdbKey.IMAGE.REVIEW_TIME);
                if (reviewTime != null) {
                    image.reviewTimeStr = DateFormat.format(reviewTime);
                }
                Integer imgGender = (Integer) dbObject.get(UserdbKey.IMAGE.GENDER);
                if (imgGender != null) {
                    image.gender = imgGender.longValue();
                }
                String userDeny = (String) dbObject.get(UserdbKey.IMAGE.USER_DENY);
                Util.addDebugLog("=======userDeny==="+userDeny);
                image.userDenyName = userDeny;
                
                Integer status = (Integer) dbObject.get(UserdbKey.IMAGE.STATUS);
                image.type = status.longValue();
                
                
//                Long app_flag = null;  
//                Integer app_flag1 =null;
//                if(dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG) != null && dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG) instanceof Long){ 
//                    Util.addDebugLog("=====app_flag Long 11111:"+app_flag);
//                    app_flag = (Long) dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG);
//                    Util.addDebugLog("=====app_flag Long:"+app_flag);
//                    image.appFlag = app_flag.intValue();
//                } else if (dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG) != null && dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG) instanceof Integer){
//                    Util.addDebugLog("=====app_flag Long 2222:"+app_flag);
//                    app_flag1 = (Integer) dbObject.get(UserdbKey.IMAGE.APPROVED_FLAG);
//                    Util.addDebugLog("=====app_flag Inter:"+app_flag1);
//                    image.appFlag = app_flag1;
//                }
                                
                list.add(image);
//                if (gender == null || gender.equals(new Long(2))) {
//                    image.gender = UserDAO.getUserGender(uId);
//                    list.add(image);
//                } else if (gender.equals(UserDAO.getUserGender(uId))) {
//                    image.gender =gender;
//                    list.add(image);
//                }
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    /////

    public static SizedListData searchReportedImage(Long type, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (type == null) {
                return null;
            }
            findObject.put(UserdbKey.IMAGE.REPORT_FLAG, type.intValue());
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
            DBObject neObj = new BasicDBObject("$ne", 0);
            findObject.put(UserdbKey.IMAGE.IMAGE_TYPE, neObj);
            findObject.put(UserdbKey.IMAGE.FLAG, Constant.FLAG.ON);
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.IMAGE.REPORT_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.IMAGE.REPORT_NUMBER, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Image image = new Image();
                String imageId = (String) dbObject.get(UserdbKey.IMAGE.IMAGE_ID);
                image.imageId = imageId;
                String userId = (String) dbObject.get(UserdbKey.IMAGE.USER_ID);
                image.userId = userId;
                Integer imageType = (Integer) dbObject.get(UserdbKey.IMAGE.IMAGE_TYPE);
                image.imageType = imageType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.IMAGE.UPLOAD_TIME);
                if (uploadTime != null) {
                    image.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Integer reportNumber = (Integer) dbObject.get(UserdbKey.IMAGE.REPORT_NUMBER);
                image.reportNumber = reportNumber;
                Long reportTime = (Long) dbObject.get(UserdbKey.IMAGE.REPORT_TIME);
                if (reportTime != null) {
                    image.reportTimeStr = DateFormat.format(reportTime);
                }
                list.add(image);

            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateReview(String imageId, int status, long timeReview, String userDeny) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.STATUS, status);
            if (status == Constant.REVIEW_STATUS_FLAG.APPROVED) {
                updateObj.append(UserdbKey.IMAGE.APPROVED_FLAG, Constant.FLAG.ON);
            } else {
                updateObj.append(UserdbKey.IMAGE.AVATAR_FLAG, Constant.FLAG.OFF);
                if (status == Constant.REVIEW_STATUS_FLAG.DENIED) {
                    updateObj.append(UserdbKey.IMAGE.DENIED_FLAG, Constant.FLAG.ON);
                }
            }
            updateObj.append(UserdbKey.IMAGE.REVIEW_TIME, timeReview);
            updateObj.append(UserdbKey.IMAGE.USER_DENY, userDeny);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            Util.addDebugLog("jsonnnnnnnnnnnnnn = = = = = " + coll.toString());
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateUserDeny(String imageId, String userDeny, Long timeReview, Long appFlag) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.USER_DENY, userDeny);
            updateObj.append(UserdbKey.IMAGE.APPROVED_FLAG, appFlag);
            updateObj.append(UserdbKey.IMAGE.REVIEW_TIME, timeReview);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateReport(String imageId, int reportFlag) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.REPORT_FLAG, reportFlag);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateAppearFlag(String imageId, int appearFlag) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.APPEAR_FLAG, appearFlag);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static String getImageUrl(String imageId) throws EazyException {
        String url = null;
        try {

            //search by id
            ObjectId id = null;
            try {
                id = new ObjectId(imageId);
            } catch (Exception ex) {
                id = null;
            }
            if (id == null) {
                return null;
            }
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.ID, id);
            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject == null) {
                return null;
            }
            url = (String) dboject.get(StaticFiledbKey.IMAGE.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }

}
