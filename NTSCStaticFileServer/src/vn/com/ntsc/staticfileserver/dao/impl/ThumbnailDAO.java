/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;

/**
 *
 * @author RuAc0n
 */
public class ThumbnailDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.THUMBNAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean insertThumbnail(String imageId, String url) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(imageId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);
            obj.append(StaticFiledbKey.THUMBNAIL.URL, url);
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    //add by Huy 201709Oct
    public static boolean insertThumbnailWithWidthHeight(String imageId, String url,Double width,Double height) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(imageId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);
            obj.append(StaticFiledbKey.THUMBNAIL.URL, url);
            obj.append(StaticFiledbKey.THUMBNAIL.WIDTH, width);
            obj.append(StaticFiledbKey.THUMBNAIL.HEIGHT, height);
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getThumbnailUrl(String imageId) throws EazyException {
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
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);

            // command search
            DBObject dboject = coll.findOne(obj);
            if (dboject == null) {
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            url = (String) dboject.get(StaticFiledbKey.THUMBNAIL.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }

    public static Map<String, String> getThumbnailUrl(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listImageId){
            Boolean isVaild = ObjectId.isValid(id);
            if(isVaild){
                listid.add(new ObjectId(id));
            }
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.THUMBNAIL.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.THUMBNAIL.URL);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + url ;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, String> getThumbnailUrlWithInfo(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listImageId){
            Boolean isVaild = ObjectId.isValid(id);
            if(isVaild){
                listid.add(new ObjectId(id));
            }
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.THUMBNAIL.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.THUMBNAIL.URL);
            String width =  obj.get(StaticFiledbKey.IMAGE.WIDTH)+"";
            String height =  obj.get(StaticFiledbKey.IMAGE.HEIGHT)+"";
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE + url + " " + width + " " + height;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, FileData> getMapImageData(List<String> listImageId){
        Map<String, FileData> mapData = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listImageId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.IMAGE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.THUMBNAIL.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.THUMBNAIL.URL);
            
            FileData imgData = new FileData();
            imgData.fileId = id;
            imgData.thumbnailUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+url;
            imgData.thumbnailWidth = (Double) obj.get(StaticFiledbKey.IMAGE.WIDTH);
            imgData.thumbnailHeight = (Double) obj.get(StaticFiledbKey.IMAGE.HEIGHT);
            
            mapData.put(id, imgData);
        }
        return mapData;
    }
    
    public static Map<String, FileData> getMapStreamImageData(List<String> listImageId){
        Map<String, FileData> mapData = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listImageId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.IMAGE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.THUMBNAIL.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.THUMBNAIL.URL);
            
            FileData imgData = new FileData();
            imgData.fileId = id;
            imgData.thumbnailUrl = url;
            imgData.thumbnailWidth = (Double) obj.get(StaticFiledbKey.IMAGE.WIDTH);
            imgData.thumbnailHeight = (Double) obj.get(StaticFiledbKey.IMAGE.HEIGHT);
            
            mapData.put(id, imgData);
        }
        return mapData;
    }
    
    public static Map<String, String> getStreamThumbnailUrlWithInfo(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listImageId){
            Boolean isVaild = ObjectId.isValid(id);
            if(isVaild){
                listid.add(new ObjectId(id));
            }
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.THUMBNAIL.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.THUMBNAIL.URL);
            String width =  obj.get(StaticFiledbKey.IMAGE.WIDTH)+"";
            String height =  obj.get(StaticFiledbKey.IMAGE.HEIGHT)+"";
            url = url + " " + width + " " + height;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
}
