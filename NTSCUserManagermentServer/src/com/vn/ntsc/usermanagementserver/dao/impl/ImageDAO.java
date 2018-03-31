/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBList;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.dbentity.ImageDB;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;

/**
 *
 * @author DuongLTD
 */
public class ImageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.IMAGE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean insertImage(Image image) throws EazyException {
        boolean result = false;
        try {
            ImageDB imageDB = ImageDB.fromImageEntity(image);
            DBObject obj = imageDB.toDBObject();
            coll.insert(obj, new WriteConcern(true));
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
                String appFlagObj = obj.get(UserdbKey.IMAGE.APPROVED_FLAG).toString();
                int appFlag = 0;
                if(appFlagObj != null) {
                    appFlag = Integer.parseInt(appFlagObj);
                }
                Integer flag = (Integer) obj.get(UserdbKey.IMAGE.FLAG);
                Long uploadTime = (Long) obj.get(UserdbKey.IMAGE.UPLOAD_TIME);
                Long reportTime = (Long) obj.get(UserdbKey.IMAGE.REPORT_TIME);
                Integer reportFlag = (Integer) obj.get(UserdbKey.IMAGE.REPORT_FLAG);
                Integer appearFlag = (Integer) obj.get(UserdbKey.IMAGE.APPEAR_FLAG);
                result = new Image(userId, imageId, imageType, imageStatus, avatarFlag,
                        appFlag, flag, uploadTime, appearFlag, reportFlag, reportTime);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getStattus(String imageId) throws EazyException {
        int result = -1;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            DBObject respondObj = coll.findOne(findObject);
            if (respondObj != null) {
                Integer status = (Integer) respondObj.get(UserdbKey.IMAGE.STATUS);
                result = status;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateReviewTime(String imageId, Long reviewTime) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.REVIEW_TIME, reviewTime);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateFlag(String imageId, int flag) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.FLAG, flag);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkImageExist(String userId, String imageId) {
        boolean result = false;
        BasicDBObject updateQuery = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
        DBCursor cur = coll.find(updateQuery);
        while (cur.hasNext()) {
            DBObject respondObj = cur.next();
            if (respondObj != null) {
                String imgId = (String) respondObj.get(UserdbKey.IMAGE.IMAGE_ID);
                if (imgId.equals(imageId)) {
                    result = true;
                }
            }
        }
        return result;
    }

    public static boolean imageExist(String imageId, String userId) {
        boolean result = false;
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer flag = (Integer) respondObj.get(UserdbKey.IMAGE.FLAG);
            if (flag != null && flag == Constant.FLAG.ON) {
                String usId = (String) respondObj.get(UserdbKey.IMAGE.USER_ID);
                Integer status = (Integer) respondObj.get(UserdbKey.IMAGE.STATUS);
                if (status == Constant.REVIEW_STATUS_FLAG.APPROVED || (userId.equals(usId) && status != Constant.REVIEW_STATUS_FLAG.DENIED)) {
                    return true;
                }
            }
        }
        return result;
    }

    public static boolean removeAvatar(String userId) {
        boolean result = false;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.AVATAR_FLAG, Constant.FLAG.OFF);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObject, setObj, false, true);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return result;
    }

    public static String getReviewingAvatar(String userId) {
        String result = null;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
            findObject.append(UserdbKey.IMAGE.STATUS, Constant.REVIEW_STATUS_FLAG.PENDING);
            DBCursor cursor = coll.find(findObject);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                Integer avatarFlag = (Integer) obj.get(UserdbKey.IMAGE.AVATAR_FLAG);
                if (avatarFlag != null && avatarFlag == Constant.FLAG.ON) {
                    result = (String) obj.get(UserdbKey.IMAGE.IMAGE_ID);
                    break;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return result;
    }

//    public static boolean removeAvatar(String userId) {     
//        boolean result = false;
//        try{
//            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
//            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.AVATAR_FLAG, Constant.NO);
//            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
//            coll.update(findObject, setObj, false, true);
//            result = true;
//        }catch(Exception ex){
//            Util.addErrorLog(ex);            
//           
//        }
//        return result;
//    }     
    public static boolean updateAvatarFlag(String userId, String imageId, int flag) {
        boolean result = false;
        try {
            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
            findObject.append(UserdbKey.IMAGE.IMAGE_ID, imageId);
            DBObject setObj = new BasicDBObject("$set", new BasicDBObject(UserdbKey.IMAGE.AVATAR_FLAG, flag));
            coll.update(findObject, setObj);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return result;
    }

    public static boolean isExists(String imageId) throws EazyException {
        boolean result = false;
        try {
            //
            BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            DBObject respondObj = coll.findOne(findObject);
            if (respondObj != null) {
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getUserId(String imageId) {
        String result = null;
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            result = (String) respondObj.get(UserdbKey.IMAGE.USER_ID);
        }
        return result;
    }

    public static int getImageType(String imageId) {
        int result = 1;
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer flag = (Integer) respondObj.get(UserdbKey.IMAGE.IMAGE_TYPE);
            if (flag != null) {
                return flag;
            }
        }
        return result;
    }

    public static boolean addReport(String imageId, int appearFlag, Date time, boolean isIncreaseReportNumber) throws EazyException {
        boolean result = false;
        try {
            //search by id
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            //update command
            BasicDBObject obj = new BasicDBObject(UserdbKey.IMAGE.REPORT_NUMBER, 1);
            BasicDBObject updateCommand = new BasicDBObject();
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.IMAGE.APPEAR_FLAG, appearFlag);
            updateObj.append(UserdbKey.IMAGE.REPORT_TIME, time.getTime());
            updateObj.append(UserdbKey.IMAGE.REPORT_FLAG, Constant.REPORT_STATUS_FLAG.WAITING);
            updateCommand.append("$set", updateObj);
            if (isIncreaseReportNumber) {
                updateCommand.append("$inc", obj);
            }
            coll.update(updateQuery, updateCommand);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean addAlbumImgReport(String imageId, String userId, Date time, boolean isIncreaseReportNumber){
        Boolean result = false;
        try{
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
            
            BasicDBObject obj = new BasicDBObject(UserdbKey.IMAGE.REPORT_NUMBER, 1);
            
            BasicDBObject updateCommand = new BasicDBObject();
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(UserdbKey.IMAGE.IMAGE_ID, imageId);
            updateObj.append(UserdbKey.IMAGE.USER_ID, userId);
            updateObj.append(UserdbKey.IMAGE.IMAGE_TYPE, 3);
            updateObj.append(UserdbKey.IMAGE.REPORT_TIME, time.getTime());
            updateObj.append(UserdbKey.IMAGE.FLAG, Constant.FLAG.ON);
            updateObj.append(UserdbKey.IMAGE.REPORT_FLAG, Constant.REPORT_STATUS_FLAG.WAITING);
            updateCommand.append("$set", updateObj);
            if (isIncreaseReportNumber) {
                updateCommand.append("$inc", obj);
            }
            coll.update(updateQuery, updateCommand, true, false);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

    //HUNGDT add
    public static int getIsFree(String imageId) {
        int result = 0;
        ObjectId id = new ObjectId(imageId);
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.ID, id);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer is_free = (Integer) respondObj.get(UserdbKey.IMAGE.IS_FREE);
            if (is_free != null && is_free == 1) {
                Util.addDebugLog("getIsFree " + is_free);
                result = 1;
                return result;
            } else {
                Util.addDebugLog("getIsFree1 " + is_free);
            }
        }
        return result;
    }
    
    public static String getAvatarIdByUserId(String userId){
        String result = null;
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.USER_ID, userId);
        findObject.append(UserdbKey.IMAGE.AVATAR_FLAG, 1);
        DBObject objectResult = coll.findOne(findObject);
        if(objectResult != null){
            result = (String) objectResult.get(UserdbKey.IMAGE.IMAGE_ID);
            return result;
        }
        else{
            return null;
        }
    }

    public static Image getImageWithoutGender() {
        BasicDBObject findObject = new BasicDBObject("gender", null);
        DBObject obj = coll.findOne(findObject);
        if (obj != null){
            String id = obj.get("_id").toString();
            String userId = obj.get("user_id").toString();
            return new Image(userId, id);
        }
        else {
            return null;
        }
    }

    public static void updateImageGender(String imageId, Integer gender) {
        BasicDBObject findObject = new BasicDBObject("_id", new ObjectId(imageId));
        BasicDBObject query = new BasicDBObject("gender", gender);
        BasicDBObject update = new BasicDBObject("$set", query);
        coll.update(findObject, update);
    }
}
