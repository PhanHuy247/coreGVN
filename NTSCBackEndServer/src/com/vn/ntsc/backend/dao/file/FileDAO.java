/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.file;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.vn.ntsc.backend.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.thumbnail.ThumbNailDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.ImageStfDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.video.Video;
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.util.DateFormat;
import java.nio.charset.Charset;
import java.util.Date;

/**
 *
 * @author RuAc0n
 */
public class FileDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getStaticFileDB().getCollection(UserdbKey.FILE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    //HUNGDT add
    public static int getIsFree(String fileId) {
        int result = 1;
        ObjectId id = new ObjectId(fileId);
        BasicDBObject findObject = new BasicDBObject(UserdbKey.FILE.ID, id);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer is_free = (Integer) respondObj.get(UserdbKey.FILE.IS_FREE);
            if (is_free != null) {
                return is_free;
            }
        }
        return result;
    }

    public static String getFileUrl(String fileId) throws EazyException {
        String url = null;
        try {
            //search by id
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject != null) {
                url = (String) dboject.get(StaticFiledbKey.FILE.URL);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }
    
     public static SizedListData searchVideo(Long type, String userId, String videoId, Long sort, Long order, int skip, int take,String username, String emailUser) throws EazyException {
        SizedListData result = new SizedListData();

        try {
            DBObject findObject = new BasicDBObject();
            
            if (type != Constant.VIDEO_STATUS.SELECT_ALL) {
                findObject.put(UserdbKey.FILE.VIDEO_STATUS, type.intValue());
            } else {
                DBObject neObj = new BasicDBObject("$ne", type.intValue());
                findObject.put(UserdbKey.FILE.VIDEO_STATUS, neObj);
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
            if (videoId != null) {
                ObjectId id = new ObjectId(videoId);
                findObject.put(UserdbKey.FILE.ID, id);
            }
            if (username != null) {
                findObject.put(UserdbKey.FILE.USER_NAME, username.toString());
            }
              if (emailUser != null) {
                findObject.put(UserdbKey.FILE.EMAIL, emailUser);
            }
            findObject.put(UserdbKey.FILE.FLAG, 1);
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
   
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.IMAGE.UPLOAD_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.IMAGE.REVIEW_TIME, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            Util.addDebugLog("findObject--------------------------"+findObject);
            Util.addDebugLog("sortObj--------------------------"+sortObj);
            Integer number = cursor.size();
            //set to list
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                
                Integer flag = (Integer)dbObject.get(UserdbKey.FILE.FLAG);
                Util.addDebugLog("flag----------------------"+flag);
                if(flag == null || flag != 1) continue;
                Video video = new Video();
                String vidId = ((ObjectId)dbObject.get(UserdbKey.FILE.ID)).toHexString();
                
                video.videoId = vidId;
                String uId = (String) dbObject.get(UserdbKey.FILE.USER_ID);
                if(uId != null)
                    video.userId = uId;
                String userName = (String)dbObject.get(UserdbKey.FILE.USER_NAME);
                if(userName != null){
                    video.username = userName;
                }
                String email = (String)dbObject.get(UserdbKey.FILE.EMAIL);
                if(email != null){
                    video.email = email;
                }
                
                String url = (String) dbObject.get(UserdbKey.FILE.URL);
                if(url.contains("http"))
                    video.url = url;
                else video.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE +url;
                
                if(url.contains("mp3")){
                    String thumnail = ImageDAO.getImageUrl(vidId);
                    if(thumnail != null){
                        video.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                    }
                }
                if(url.contains("mp4")){
                    String thumnail = ThumbNailDAO.getThumbnailUrl(vidId);
                    if(thumnail != null){
                        video.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                    }
                }
                Long videoStatus = (Long) dbObject.get(UserdbKey.FILE.VIDEO_STATUS);
                video.videoStatus = videoStatus;
                Date uploadTime = (Date) dbObject.get(UserdbKey.FILE.UPLOAD_TIME);
                if (uploadTime != null) {
                    video.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Date reviewTime = (Date) dbObject.get(UserdbKey.FILE.REVIEW_TIME);
                if (reviewTime != null) {
                    video.reviewTimeStr = DateFormat.format(reviewTime);
                }
                
                list.add(video);

            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
     public static void InsertReviewVideo(String videoId,Long videoStatus){
         ObjectId id = new ObjectId(videoId);
         BasicDBObject seachObj = new BasicDBObject(UserdbKey.FILE.ID, id);
         
         BasicDBObject newDocument = new BasicDBObject();
         newDocument.append("$set", new BasicDBObject().append(UserdbKey.FILE.VIDEO_STATUS, videoStatus));
         coll.update(seachObj, newDocument);
     }
     public static void UpdateTimeReviewVideo(String videoId,Date timeReview){
         ObjectId id = new ObjectId(videoId);
         BasicDBObject seachObj = new BasicDBObject(UserdbKey.FILE.ID, id);
         
         BasicDBObject newDocument = new BasicDBObject();
         newDocument.append("$set", new BasicDBObject().append(UserdbKey.FILE.REVIEW_TIME, timeReview));
         coll.update(seachObj, newDocument);
     }
      public static Video getVideoInfo(String videoId) throws EazyException {
        Video result = null;
        try {
            ObjectId id = new ObjectId(videoId);
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.FILE.ID, id);
            DBObject obj = coll.findOne(seachObj);
            if (obj != null) {
                String userId = (String) obj.get(UserdbKey.FILE.USER_ID);
                Long fileStatus = (Long) obj.get(UserdbKey.FILE.VIDEO_STATUS);
                Date uploadTime = (Date) obj.get(UserdbKey.FILE.UPLOAD_TIME);
                Date reviewTime = (Date) obj.get(UserdbKey.FILE.REVIEW_TIME);
                String userName = (String) obj.get(UserdbKey.FILE.USER_NAME);
                String email = (String) obj.get(UserdbKey.FILE.EMAIL);
                String url = (String) obj.get(UserdbKey.FILE.URL);
                String urlVideo = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE+ url;
                result = new Video(userId,userName,email,videoId, fileStatus,
                         String.valueOf(uploadTime.getTime()), String.valueOf(reviewTime.getTime()),url);
            } else {
                Util.addInfoLog("Image not found it : " + videoId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
