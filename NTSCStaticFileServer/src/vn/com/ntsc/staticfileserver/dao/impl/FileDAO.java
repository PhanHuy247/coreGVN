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
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.DAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;

/**
 *
 * @author RuAc0n
 */
public class FileDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getStatisticDB().getCollection(StaticFiledbKey.FILE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertFile(String url, String userId) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.URL, url);
            obj.append(StaticFiledbKey.FILE.USER_ID, userId);
            coll.insert(obj);
            ObjectId id = obj.getObjectId(StaticFiledbKey.FILE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String insertFile(String url, Double width, Double height, Integer fileDuration) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.URL, url);
            obj.append(StaticFiledbKey.FILE.HEIGHT, height);
            obj.append(StaticFiledbKey.FILE.WIDTH, width);
            obj.append(StaticFiledbKey.FILE.FILE_DURATION, fileDuration);
            coll.insert(obj);
            ObjectId id = obj.getObjectId(StaticFiledbKey.FILE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String insertFileWithWidthHeight(String url, String userId,Double width,Double height, Date time,String userName,String email,Long videoStatus, Integer fileLength) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.URL, url);
            obj.append(StaticFiledbKey.FILE.USER_ID, userId);
            obj.append(StaticFiledbKey.FILE.HEIGHT, height);
            obj.append(StaticFiledbKey.FILE.WIDTH, width);
            obj.append(StaticFiledbKey.FILE.TIME, time);
            obj.append(StaticFiledbKey.FILE.USER_NAME, userName);
            obj.append(StaticFiledbKey.FILE.EMAIL, email);
            obj.append(StaticFiledbKey.FILE.FLAG, 1);
            obj.append(StaticFiledbKey.FILE.VIDEO_STATUS, videoStatus);
            obj.append(StaticFiledbKey.FILE.FILE_DURATION, fileLength);
            coll.insert(obj);
            ObjectId id = obj.getObjectId(StaticFiledbKey.FILE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void updateFile(String fileId, String fileUrl){
        try{
            ObjectId id = new ObjectId(fileId);
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(StaticFiledbKey.FILE.ID, id);
            
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append("$set", new BasicDBObject(StaticFiledbKey.FILE.URL, fileUrl));
            
            coll.update(findObj, findObj);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }

    public static String insertFile2(String url, String userId, int is_free) throws EazyException {
        String result = null;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.URL, url);
            obj.append(StaticFiledbKey.FILE.USER_ID, userId);
            obj.append(StaticFiledbKey.FILE.IS_FREE, is_free);
            coll.insert(obj);
            ObjectId id = obj.getObjectId(StaticFiledbKey.FILE.ID);
            result = id.toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean removeDocument(String _id) {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(_id);
            BasicDBObject dbObject = new BasicDBObject(StaticFiledbKey.FILE.ID, id);
            coll.remove(dbObject);
        } catch (Exception ex){
            Util.addErrorLog(ex);       
        }
        
        return result;
    }
    
    public static boolean removeDocuments(List<String> ids) {
        boolean result = false;
        try {
            List<ObjectId> obIds = new ArrayList<>();
            for(String id : ids){
                if(ObjectId.isValid(id)){
                    obIds.add(new ObjectId(id));
                }
            }
            DBObject query = QueryBuilder.start(StaticFiledbKey.FILE.ID).in(obIds).get();
            coll.remove(query);
        } catch (Exception ex){
            Util.addErrorLog(ex);       
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
        return FilesAndFolders.FOLDERS.FILE + url;
    }
    
    public static Map<String, FileInfo> getAll() throws EazyException{
        Map<String, FileInfo> result = new HashMap<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()){
                DBObject obj = cursor.next();
                ObjectId fileId = (ObjectId) obj.get(StaticFiledbKey.FILE.ID);
                long dateCreated = fileId.getTime();
                String fileUrl = obj.get(StaticFiledbKey.FILE.URL).toString();
                fileUrl = fileUrl.replace(".sh", ".mp4");
                result.put(fileId.toString(), new FileInfo(fileUrl, dateCreated));
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }        
        return result;
    }

//    public static String insertVideoFileInTestMode(String url, String userId) throws EazyException {
//        Util.addInfoLog("Insert Video File In Test Mode!");
//        String result = null;
//        try {
//            ObjectId _id = new ObjectId(DateFormat.parse_yyyyMMdd(Config.DELETED_VIDEO_TEST_DATE));
//            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.ID, _id);
//            obj.append(StaticFiledbKey.FILE.URL, url);
//            obj.append(StaticFiledbKey.FILE.USER_ID, userId);
//            coll.insert(obj);
//            result = _id.toString();
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }

    public static Map<String, String> getFileUrl(List<String> listVideoId) {
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listVideoId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }            
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.FILE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.FILE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.FILE.URL);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE + url;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, String> getFileUrlWithInfo(List<String> listVideoId) {
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listVideoId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }            
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.FILE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.FILE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.FILE.URL);
            String duration = obj.get(StaticFiledbKey.FILE.FILE_DURATION)+"";
            String width = obj.get(StaticFiledbKey.FILE.WIDTH)+"";
            String height = obj.get(StaticFiledbKey.FILE.HEIGHT)+"";
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE + url+" "+width+" "+height+" "+duration;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, String> getStreamUrlWithInfo(List<String> listVideoId) {
        Map<String, String> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listVideoId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }            
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.FILE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.FILE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.FILE.URL);
            String width = obj.get(StaticFiledbKey.FILE.WIDTH)+"";
            String height = obj.get(StaticFiledbKey.FILE.HEIGHT)+"";
            url = url+" "+width+" "+height;
            mapUrl.put(id, url);
        }
        return mapUrl;
    }
    
    public static Map<String, FileData> getMapStreamData(List<String> listFileId){
        Map<String, FileData> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listFileId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }            
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.FILE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.FILE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.FILE.URL);
            
            FileData fileData = new FileData();
            fileData.fileId = id;
            fileData.fileUrl = url;
            fileData.fileDuration = (Integer) obj.get(StaticFiledbKey.FILE.FILE_DURATION);

            mapUrl.put(id, fileData);
        }
        return mapUrl;
    }
    
    public static Map<String, FileData> getMapFileData(List<String> listFileId){
        Map<String, FileData> mapUrl = new HashMap<>();
        BasicDBList listid = new BasicDBList();
        for (String id : listFileId){
            Boolean isValid = ObjectId.isValid(id);
            if(isValid){
                listid.add(new ObjectId(id));
            }            
        }
        BasicDBObject query = new BasicDBObject("$in", listid);
        BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.FILE.ID, query);
        DBCursor cursor = coll.find(findObj);
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            String id = obj.get(StaticFiledbKey.FILE.ID).toString();
            String url = (String) obj.get(StaticFiledbKey.FILE.URL);
            url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE + url;
            
            FileData fileData = new FileData();
            fileData.fileId = id;
            fileData.fileUrl = url;
            fileData.fileDuration = (Integer) obj.get(StaticFiledbKey.FILE.FILE_DURATION);

            mapUrl.put(id, fileData);
        }
        return mapUrl;
    }
}
