/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author DuongLTD
 */
public class UserStickerDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getUserDB().getCollection(UserdbKey.USER_STICKER_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static boolean addNewListSeen(List<String> listCat, String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            BasicDBList list = new BasicDBList();
            list.addAll(listCat);
            BasicDBObject set = new BasicDBObject("$set", new BasicDBObject(UserdbKey.USER_STICKER.NEW_LIST_SEEN, list));
            coll.update(findObject, set, true, false);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void addDownLoadList(String userId, String categoryId, int categoryType) throws EazyException {
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            BasicDBObject checkerObj = new BasicDBObject(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID, categoryId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObject.append(UserdbKey.USER_STICKER.DOWNLOAD_LIST, elemMatch);
            DBObject obj = coll.findOne(findObject);
            if (obj == null) {
                findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
                BasicDBObject checkerElement = new BasicDBObject(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID, categoryId);
                checkerElement.append(UserdbKey.USER_STICKER.STICKER_CATEGORY_TYPE, categoryType);
                checkerElement.append(UserdbKey.USER_STICKER.DOWNLOAD_TIME, Util.getGMTTime());

                BasicDBObject checker = new BasicDBObject(UserdbKey.USER_STICKER.DOWNLOAD_LIST, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", checker);
                coll.update(findObject, updateCommand, true, false);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static List<String> getNewList(String userId) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            DBObject obj = coll.findOne(findObject);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.USER_STICKER.NEW_LIST_SEEN);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        String catId = (String) list.get(i);
                        result.add(catId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getDownLoadList(String userId) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            DBObject obj = coll.findOne(findObject);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.USER_STICKER.DOWNLOAD_LIST);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        DBObject dbO = (DBObject) list.get(i);
                        String catId = (String) dbO.get(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID);
                        result.add(catId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean isDownload(String userId, String catId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            DBObject obj = coll.findOne(findObject);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.USER_STICKER.DOWNLOAD_LIST);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        DBObject dbO = (DBObject) list.get(i);
                        String cId = (String) dbO.get(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID);
                        if (cId.equals(catId)) {
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

    public static String getDownloadTime(String userId, String catId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObject = new BasicDBObject(UserdbKey.USER_STICKER.ID, id);
            DBObject obj = coll.findOne(findObject);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.USER_STICKER.DOWNLOAD_LIST);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        DBObject dbO = (DBObject) list.get(i);
                        String cId = (String) dbO.get(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID);
                        if (cId.equals(catId)) {
                            Long time = (Long) dbO.get(UserdbKey.USER_STICKER.DOWNLOAD_TIME);
                            result = DateFormat.format(new Date(time));
                            break;
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

    public static void removeSticker() {
        long time;
        time = Util.getGMTTime().getTime();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.USER_STICKER.DOWNLOAD_LIST);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        DBObject dbO = (DBObject) list.get(i);
                        Integer type = (Integer) dbO.get(UserdbKey.USER_STICKER.STICKER_CATEGORY_TYPE);
                        if (type == 2 || type == 4) {
                            String catId = (String) dbO.get(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID);
                            Long downloadTime = (Long) dbO.get(UserdbKey.USER_STICKER.DOWNLOAD_TIME);
                            Integer timeLive = (Integer) StickerCategoryDAO.getTimeLive(catId);
                            if ((time - downloadTime) / Constant.A_DAY > timeLive) {
                                BasicDBObject catObj = new BasicDBObject(UserdbKey.USER_STICKER.STICKER_CATEGORY_ID, catId);
                                BasicDBObject downloadObj = new BasicDBObject(UserdbKey.USER_STICKER.DOWNLOAD_LIST, catObj);
                                BasicDBObject updateCommand = new BasicDBObject("$pull", downloadObj);
                                coll.update(obj, updateCommand);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

    }

}
