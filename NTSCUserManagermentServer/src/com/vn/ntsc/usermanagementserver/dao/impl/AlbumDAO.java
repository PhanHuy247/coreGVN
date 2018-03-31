/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class AlbumDAO {

    private static DBCollection coll;
    private static DB db;

    static {
        coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.ALBUM_COLLECTION);
    }

    public static String addAlbum(String userId, String albumName, String albumDes, Integer privacy, Date time) {
        String result = null;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            insertObj.append(UserdbKey.ALBUM.USER_ID, userId);
            insertObj.append(UserdbKey.ALBUM.ALBUM_NAME, albumName);
            insertObj.append(UserdbKey.ALBUM.ALBUM_DES, albumDes);
            insertObj.append(UserdbKey.ALBUM.PRIVACY, privacy);
            insertObj.append(UserdbKey.ALBUM.PRIVACY, privacy);
            insertObj.append(UserdbKey.ALBUM.TIME, time.getTime());
            coll.insert(insertObj);
            result = insertObj.get(UserdbKey.ALBUM.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static Boolean delAlbum(String userId, String albumId) {
        Boolean result = false;
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.USER_ID, userId);
                findObj.append(UserdbKey.ALBUM.ID, id);

                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    coll.remove(findObj);
                    result = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    public static void updateNumberImage(String albumId,Integer numberImage) {
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.ID, id);

//                DBObject obj = coll.findOne(findObj);
//                if (obj != null) {
                    BasicDBObject newDocument =
                        new BasicDBObject().append("$inc",
                            new BasicDBObject().append("number_image", numberImage));
                    coll.update(findObj, newDocument);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
   

    public static Boolean updateAlbum(String userId, String albumId, String albumName, String albumDes, Integer privacy) {
        Boolean result = false;
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.USER_ID, userId);
                findObj.append(UserdbKey.ALBUM.ID, id);

                BasicDBObject updateObj = new BasicDBObject();
                updateObj.append(UserdbKey.ALBUM.ALBUM_NAME, albumName);
                updateObj.append(UserdbKey.ALBUM.ALBUM_DES, albumDes);
                updateObj.append(UserdbKey.ALBUM.PRIVACY, privacy);

                coll.update(findObj, new BasicDBObject("$set", updateObj));
                result = true;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static List<AlbumData> loadAllAlbum(String userId, Integer privacy, Integer skip, Integer take) {
        List<AlbumData> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM.USER_ID, userId);

            BasicDBList list = new BasicDBList();
            if (privacy == Constant.POST_MODE.ONLY_ME) {
                list.add(Constant.POST_MODE.EVERYONE);
                list.add(Constant.POST_MODE.ONLY_ME);
            } else {
                list.add(Constant.POST_MODE.EVERYONE);
            }
            findObj.append(UserdbKey.ALBUM.PRIVACY, new BasicDBObject("$in", list));

            DBCursor cursor = coll.find(findObj).skip(skip).limit(take);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                AlbumData album = new AlbumData();
                album.albumId = obj.get(UserdbKey.ALBUM.ID).toString();
                album.userId = (String) obj.get(UserdbKey.ALBUM.USER_ID);
                album.albumName = (String) obj.get(UserdbKey.ALBUM.ALBUM_NAME);
                album.albumDes = (String) obj.get(UserdbKey.ALBUM.ALBUM_DES);
                album.privacy = (Integer) obj.get(UserdbKey.ALBUM.PRIVACY);
                album.time = (Long) obj.get(UserdbKey.ALBUM.TIME);
                album.numberImage = (Integer) obj.get("number_image");
                result.add(album);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static Boolean checkAlbumOwned(String userId, String albumId) {
        Boolean resule = false;
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.USER_ID, userId);
                findObj.append(UserdbKey.ALBUM.ID, id);

                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    resule = true;
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return resule;
    }

    public static AlbumData getAlbum(String albumId) {
        AlbumData resule = new AlbumData();
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.ID, id);

                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    resule.albumId = obj.get(UserdbKey.ALBUM.ID).toString();
                    resule.userId = (String) obj.get(UserdbKey.ALBUM.USER_ID);
                    resule.albumName = (String) obj.get(UserdbKey.ALBUM.ALBUM_NAME);
                    resule.albumDes = (String) obj.get(UserdbKey.ALBUM.ALBUM_DES);
                    resule.privacy = (Integer) obj.get(UserdbKey.ALBUM.PRIVACY);
                    resule.time = (Long) obj.get(UserdbKey.ALBUM.TIME);
                    resule.numberImage = (Integer) obj.get("number_image");
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return resule;
    }
    
    public static String getUserId(String albumId){
        String result = "";
        try{
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.ID, id);

                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    result = (String) obj.get(UserdbKey.ALBUM.USER_ID);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
