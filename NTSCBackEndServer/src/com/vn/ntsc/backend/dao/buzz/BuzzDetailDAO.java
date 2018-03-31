/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;

/**
 *
 * @author DuongLTD
 */
public class BuzzDetailDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
            coll.createIndex(new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.USER_ID, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static void getListBuzz(List<Buzz> lBuzz) throws EazyException {
        try {
            for (int i = 0; i < lBuzz.size(); i++) {
                Buzz buzz = lBuzz.get(i);
                ObjectId id = new ObjectId(buzz.buzzId);
                DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                DBObject obj = coll.findOne(query);
                if (obj != null) {
                    String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                    buzz.userId = userId;

                    Integer seenNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER);
                    if (seenNum != null) {
                        buzz.seenNum = seenNum;
                    } else {
                        buzz.seenNum = 0;
                    }

                    Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                    if (likeNum != null) {
                        buzz.likeNum = likeNum;
                    } else {
                        buzz.likeNum = 0;
                    }

                    Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                    if (cmtNum != null) {
                        buzz.cmtNum = cmtNum;
                    } else {
                        buzz.cmtNum = 0;
                    }

                    ObjectId buzzId = (ObjectId) obj.get(BuzzdbKey.BUZZ_DETAIL.ID);
                    buzz.buzzId = buzzId.toString();

                    String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                    //HUNGDT 3678
                    buzz.buzzVal = Util.replaceBannedWordBackend(buzzVal);
                    Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                    buzz.buzzType = buzzType;
                    //Thanhdd add #5214
                    Integer buzzStatus = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG);
                    buzz.buzzStatus = buzzStatus;
                    
                    String userDeny = (String) obj.get(BuzzdbKey.USER_BUZZ.USER_DENY);
                    buzz.isDenyUser = userDeny;
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static String getBuzzIp(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            result = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.IP);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static String getUserId(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
            result = userId;
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public boolean checkBuzzExist(String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Integer buzzFlag = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.FLAG);
                if (buzzFlag == Constant.FLAG.ON) {
                    result = true;
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public int getBuzzType(String buzzId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
            result = buzzType.intValue();
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public String getBuzzVal(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
            String buzzType = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
            result = buzzType;
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateFlag(String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.FLAG, flag);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateApprovedFlag(String buzzId, int flag, String userDeny) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, flag);
            updateObject.put(BuzzdbKey.BUZZ_DETAIL.USER_DENY, userDeny);
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
    
    public static boolean updateApprovedFlag2(String buzzId, int flag,String userDeny) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject updateObject = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.APPROVED_FLAG, flag);
            if (userDeny != null) {
                updateObject.put(BuzzdbKey.BUZZ_DETAIL.USER_DENY, userDeny);
            }
            DBObject setObject = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObject);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
    
    private static boolean decreaseFieldList(String buzzId, String fieldname, int num) throws EazyException {
        boolean result = false;
        try {
            //search by id
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject updateQuery = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            //update command
            DBObject obD = coll.findOne(updateQuery);
            Integer point = new Integer(0);
            if (obD != null) {
                point = (Integer) obD.get(fieldname);
                if (point == null) {
                    point = new Integer(0);
                }
            }
            if (num > point) {
                num = point;
            }
            BasicDBObject obj = new BasicDBObject(fieldname, (0 - num));
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeComment(String buzzId) throws EazyException {
        boolean result = false;
        try {
            result = decreaseFieldList(buzzId, BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER, 1);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        }
        return result;
    }

    public static Buzz getBuzzDetail(String buzzId) throws EazyException {
        Buzz result = new Buzz();
        try {
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                Buzz buzz = new Buzz();
                String userId = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.USER_ID);
                buzz.userId = userId;

                Integer seenNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.SEEN_NUMBER);
                if (seenNum != null) {
                    buzz.seenNum  = seenNum;
                } else {
                    buzz.seenNum = 0;
                }

                Integer likeNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.LIKE_NUMBER);
                if (likeNum != null) {
                    buzz.likeNum = likeNum;
                } else {
                    buzz.likeNum = 0;
                }

                Integer cmtNum = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.COMMNET_NUMBER);
                if (cmtNum != null) {
                    buzz.cmtNum = cmtNum;
                } else {
                    buzz.cmtNum = 0;
                }

                buzz.buzzId = buzzId;

                String buzzVal = (String) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_VALUE);
                buzz.buzzVal = buzzVal;
                Integer buzzType = (Integer) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                buzz.buzzType = buzzType;
                Long buzzTime = (Long) obj.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TIME);
 
                Date d = new Date(buzzTime);
                buzz.buzzTime = DateFormat.format(d);

                result = buzz;
            } else {
                throw new EazyException(ErrorCode.BUZZ_NOT_FOUND);
            }
        }catch (EazyException ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateFlagBuzz(String fileId, Integer flag){
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(BuzzdbKey.BUZZ_DETAIL.FLAG, flag);
            coll.update(findObj, new BasicDBObject("$set", updateObj));
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static String getReportedBuzz(String fileId){
        String result = "";
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, fileId);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String buzzId = obj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                result = buzzId;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static String getBuzzId(String userId, String imgId){
        String result = "";
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(BuzzdbKey.BUZZ_DETAIL.USER_ID, userId);
            findObj.append(BuzzdbKey.BUZZ_DETAIL.FILE_ID, imgId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String buzzId = obj.get(BuzzdbKey.BUZZ_DETAIL.ID).toString();
                result = buzzId;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
