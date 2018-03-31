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
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.StickerCategory;

/**
 *
 * @author RuAc0n
 */
public class StickerCategoryDAO {

    private static final int DEFAULT_CATEGORY = 0;
    private static final int FREE_CATEGORY = 1;
    private static final int CASH_CATEGORY = 2;
    
    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getStampDB().getCollection(StampdbKey.STICKER_CATEGORY_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String insertDefaultCategory(String enCatNae, String jpCatName, String enDes, String jpDes) throws EazyException {
        String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, enCatNae);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, jpCatName);
            obj.append(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION, enCatNae);
            obj.append(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION, jpDes);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE, 0);
            obj.append(StampdbKey.STICKER_CATEGORY.ORDER, 1);
            obj.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
            obj.append(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, DEFAULT_CATEGORY);
            coll.insert(obj);
            result = obj.getObjectId(StampdbKey.STICKER_CATEGORY.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<StickerCategory> list(Long type, String language, int skip, int take) throws EazyException {
        List<StickerCategory> result = new ArrayList<>();
//        System.out.println("skip : " + skip + " take : " + take);
        boolean isEng = true;
        if (language != null && language.equals(Constant.LANGUAGE.JAPANESE)) {
            isEng = false;
        }
        try {
            DBCursor cur = null;
            BasicDBObject gt = new BasicDBObject("$gt", 0);
            BasicDBObject findObject = new BasicDBObject(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, gt);
            findObject.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
            if (type == 1) {
                BasicDBObject sort = new BasicDBObject(StampdbKey.STICKER_CATEGORY.DOWNLOAD_NUMBER, -1);
                cur = coll.find(findObject).sort(sort).limit(5);
            } else if (type == 2) {
                findObject.append(StampdbKey.STICKER_CATEGORY.NEW_FLAG, 1);
//                BasicDBObject sort = new BasicDBObject(DBParamKey.STICKER_CATEGORY.ORDER, 1);
                cur = coll.find(findObject).skip(skip).limit(take);
            } else if (type == 3) {
                BasicDBList ors = new BasicDBList();
                
                ors.add(new BasicDBObject(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, 1));
                ors.add(new BasicDBObject(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, 2));
//                BasicDBObject sort = new BasicDBObject(DBParamKey.STICKER_CATEGORY.ORDER, 1);
                findObject.append("$or", ors);
                cur = coll.find(findObject).skip(skip).limit(take);
            } else {
//                System.out.println("vào");
//                BasicDBObject sort = new BasicDBObject(DBParamKey.STICKER_CATEGORY.ORDER, 1);
                cur = coll.find(findObject).skip(skip).limit(take);
            }
//            System.out.println("cursor : " + cur.size());
            while (cur.hasNext()) {
                StickerCategory cat = new StickerCategory();
                DBObject obj = cur.next();
                String enName = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                String jpName = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                String name = isEng ? enName : jpName;
                String id = obj.get(StampdbKey.STICKER_CATEGORY.ID).toString();
                Integer price = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
                Integer flag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.FLAG);
                Integer catType = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
                Integer newFlag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.NEW_FLAG);
                if (flag == Constant.FLAG.ON) {
                    cat.categoryName = name;
                    cat.id = id;
                    cat.categoryPrice = price;
                    cat.catType = catType;
                    cat.newFlag = newFlag;
                    result.add(cat);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<StickerCategory> listDefaultCategory(String language, int skip, int take) throws EazyException {
        List<StickerCategory> result = new ArrayList<>();
//        System.out.println("skip : " + skip + " take : " + take);
        boolean isEng = true;
        if (language != null && language.equals(Constant.LANGUAGE.JAPANESE)) {
            isEng = false;
        }
        try {

            BasicDBObject findObject = new BasicDBObject(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, 0);
            findObject.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
            findObject.append(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER, new BasicDBObject("$gt", 0));
            DBCursor cur = coll.find(findObject);
            while (cur.hasNext()) {
                StickerCategory cat = new StickerCategory();
                DBObject obj = cur.next();
                String enName = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                String jpName = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                String name = isEng ? enName : jpName;
                String id = obj.get(StampdbKey.STICKER_CATEGORY.ID).toString();
                Integer flag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.FLAG);
                Integer version = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.VERSION);
                Integer stickerNumber = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.STICKER_NUMBER);
                if (flag == Constant.FLAG.ON) {
                    cat.categoryName = name;
                    cat.id = id;
                    cat.stickerNumber = stickerNumber;
                    cat.version = 1;
                    if(version != null)
                        cat.version = version;
                    result.add(cat);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static List<String> getListNew() throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            BasicDBObject findObject = new BasicDBObject(StampdbKey.STICKER_CATEGORY.NEW_FLAG, 1);
            findObject.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
            DBCursor cur = coll.find(findObject);
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                String id = obj.get(StampdbKey.STICKER_CATEGORY.ID).toString();
                result.add(id);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<StickerCategory> search(String key, String language, int skip, int take) throws EazyException {
        List<StickerCategory> result = new ArrayList<>();
        boolean isEng = true;
        if (language != null && language.equals(Constant.LANGUAGE.JAPANESE)) {
            isEng = false;
        }
        try {
            if (key != null) {
                String[] list = key.trim().split("\\s+");
                if (list.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < list.length; i++) {
                        sb.append(list[i].toLowerCase());
                        if (i < (list.length - 1)) {
                            sb.append("|");
                        }
                    }
                    BasicDBObject regex = new BasicDBObject("$regex", sb.toString());
                    BasicDBObject findObject = new BasicDBObject();
                    if (!isEng) {
                        findObject.append(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME, regex);
                    } else {
                        findObject.append(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME, regex);
                    }
                    findObject.append(StampdbKey.STICKER_CATEGORY.FLAG, 1);
                    DBCursor cur = coll.find(findObject).skip(skip).limit(take);
                    while (cur.hasNext()) {
                        StickerCategory cat = new StickerCategory();
                        DBObject obj = cur.next();
                        String enName = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
                        String jpName = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
                        String name = isEng ? enName : jpName;
                        String id = obj.get(StampdbKey.STICKER_CATEGORY.ID).toString();
                        Integer price = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
                        Integer flag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.FLAG);
                        Integer catType = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
                        Integer newFlag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.NEW_FLAG);
                        if (flag == Constant.FLAG.ON && catType != DEFAULT_CATEGORY) {
                            cat.categoryName = name;
                            cat.id = id;
                            cat.categoryPrice = price;
                            cat.catType = catType;
                            cat.newFlag = newFlag;
                            result.add(cat);
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

    public static String getDefaultCategoryId() throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE, DEFAULT_CATEGORY);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            ObjectId id = (ObjectId) dbOject.get(StampdbKey.STICKER_CATEGORY.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getPoint(String catId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            Integer price = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
            result = price;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void increaseDownloadNumber(String catId) throws EazyException {
        try {
            //search by id
            ObjectId id = new ObjectId(catId);
            BasicDBObject updateQuery = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            //update command
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.DOWNLOAD_NUMBER, 1);
            BasicDBObject updateCommand = new BasicDBObject("$inc", obj);
            coll.update(updateQuery, updateCommand);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }    
    
    public static int getType(String catId) {
        int result = 0;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            Integer type = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
            result = type;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }    
    
    public static int getTimeLive(String catId) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            Integer time = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.LIVE_TIME);
            result = time;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static StickerCategory get(String catId, String language) throws EazyException {
        StickerCategory result = new StickerCategory();
        boolean isEng = true;
        if (language != null && language.equals(Constant.LANGUAGE.JAPANESE)) {
            isEng = false;
        }
        try {
            ObjectId id = new ObjectId(catId);
            BasicDBObject obj = new BasicDBObject(StampdbKey.STICKER_CATEGORY.ID, id);
            DBObject dbOject = (DBObject) coll.findOne(obj);
            if (dbOject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            String enName = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_NAME);
            String jpName = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_NAME);
            String name = isEng ? enName : jpName;
            String endes = (String) obj.get(StampdbKey.STICKER_CATEGORY.ENGLISH_DESCRIPTION);
            String jpdes = (String) obj.get(StampdbKey.STICKER_CATEGORY.JAPANESE_DESCRIPTION);
            String des = isEng ? endes : jpdes;
            Integer price = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_PRICE);
            Integer catType = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.CATEGORY_TYPE);
            Integer liveTime = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.LIVE_TIME);
            String appleId = (String) obj.get(StampdbKey.STICKER_CATEGORY.APPLE_PRODUCTION_ID);
            String googleId = (String) obj.get(StampdbKey.STICKER_CATEGORY.GOOGLE_PRODUCTION_ID);
            Integer newFlag = (Integer) obj.get(StampdbKey.STICKER_CATEGORY.NEW_FLAG);
            result.categoryName = name;
            result.id = catId;
            result.categoryPrice = price;
            result.categoryDes = des;
            result.catType = catType;
            result.liveTime = liveTime;
            result.appleId = appleId;
            result.googleId = googleId;
            result.newFlag = newFlag;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
