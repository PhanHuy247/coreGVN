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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.entity.impl.FileUrl;

/**
 *
 * @author RuAc0n
 */
public class ImageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.IMAGE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertImage(String url) throws EazyException {
        String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            coll.insert(obj);
            ObjectId id = (ObjectId) obj.get(StaticFiledbKey.IMAGE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    //add by Huy20171005
    public static String insertImagewithLengthWidth(String url,Double width, Double length) throws EazyException{
         String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.HEIGHT, length);
            obj.append(StaticFiledbKey.IMAGE.WIDTH, width);
            coll.insert(obj);
            ObjectId id = (ObjectId) obj.get(StaticFiledbKey.IMAGE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Boolean insertVideoImage(String imageId, String url,Double width, Double height) throws EazyException{
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
    
    public static String insertImage2(String url, int is_free) throws EazyException {
        String result = "";
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.IS_FREE, is_free);
            coll.insert(obj);
            ObjectId id = (ObjectId) obj.get(StaticFiledbKey.IMAGE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean insertImage(String url, String fileId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.ID, id);
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
     public static boolean insertImage2(String url, String fileId, int is_free) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(fileId);
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.IMAGE.URL, url);
            obj.append(StaticFiledbKey.IMAGE.ID, id);
            obj.append(StaticFiledbKey.IMAGE.IS_FREE, is_free);
            coll.insert(obj);
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
                throw new EazyException(ErrorCode.UNKNOWN_ERROR);
            }
            url = (String) dboject.get(StaticFiledbKey.IMAGE.URL);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return url;
    }
    
    public static FileData getImageData(String imageId){
        FileData imgData = new FileData();
        BasicDBList listid = new BasicDBList();
        Boolean isValid = ObjectId.isValid(imageId);
        if(isValid){
            listid.add(new ObjectId(imageId));
        }
        
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.IMAGE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            
            imgData.fileId = id;
            imgData.originalUrl = url;
            imgData.originalWidth = (Double) obj.get(StaticFiledbKey.IMAGE.WIDTH);
            imgData.originalHeight = (Double) obj.get(StaticFiledbKey.IMAGE.HEIGHT);
        }
        return imgData;
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
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            
            FileData imgData = new FileData();
            imgData.fileId = id;
            imgData.originalUrl = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE+url;
            imgData.originalWidth = (Double) obj.get(StaticFiledbKey.IMAGE.WIDTH);
            imgData.originalHeight = (Double) obj.get(StaticFiledbKey.IMAGE.HEIGHT);
            
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
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            
            FileData imgData = new FileData();
            imgData.fileId = id;
            imgData.originalUrl = url;
            imgData.originalWidth = (Double) obj.get(StaticFiledbKey.IMAGE.WIDTH);
            imgData.originalHeight = (Double) obj.get(StaticFiledbKey.IMAGE.HEIGHT);
            
            mapData.put(id, imgData);
        }
        return mapData;
    }
    
    public static Map<String, String> getImageUrl(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
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
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + url ;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    //add by Huy201709Oct
    public static Map<String, String> getImageUrlWithInfo(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
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
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            String width = obj.get(StaticFiledbKey.IMAGE.WIDTH)+"";
            String height = obj.get(StaticFiledbKey.IMAGE.HEIGHT)+"";
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.ORIGINAL_IMAGE + url +" "+width+" "+height;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, String> getStreamImageUrlWithInfo(List<String> listImageId){
        Map<String, String> mapUrl = new HashMap<>();
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
            String id = obj.get(StaticFiledbKey.IMAGE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.IMAGE.URL);
            String width = obj.get(StaticFiledbKey.IMAGE.WIDTH)+"";
            String height = obj.get(StaticFiledbKey.IMAGE.HEIGHT)+"";
            url = url +" "+width+" "+height;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
}
