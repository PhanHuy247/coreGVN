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
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumImageData;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class AlbumImageDAO {
    private static DBCollection coll;
    private static DB db;

    static {
        coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.ALBUM_IMAGE_COLLECTION);
    }
    
    public static List<AlbumData> getLastestImage(List<AlbumData> data){
        List<AlbumData> result = new ArrayList<>();
        try {
            for(AlbumData album: data){
                String albumId = album.albumId;
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
                
                BasicDBObject sortObj = new BasicDBObject();
                sortObj.append(UserdbKey.ALBUM_IMAGE.TIME, -1);
                
                DBCursor cursor = coll.find(findObj).sort(sortObj).limit(1);
                
                List<AlbumImageData> imageList = new ArrayList<>();
                while (cursor.hasNext()){
                    DBObject obj = cursor.next();
                    
                    AlbumImageData image = new AlbumImageData();
                    image.imageId = (String) obj.get(UserdbKey.ALBUM_IMAGE.IMAGE_ID);
                    image.time = (Long) obj.get(UserdbKey.ALBUM_IMAGE.TIME);
                    
                    imageList.add(image);
                }
                album.imageList = imageList;
                album.numberImage = getImageNumber(albumId);
                result.add(album);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static AlbumData getLastestImageAlbum(AlbumData album){
        try {
                String albumId = album.albumId;
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
                
                BasicDBObject sortObj = new BasicDBObject();
                sortObj.append(UserdbKey.ALBUM_IMAGE.TIME, -1);
                
                DBCursor cursor = coll.find(findObj).sort(sortObj).limit(1);
                
                List<AlbumImageData> imageList = new ArrayList<>();
                while (cursor.hasNext()){
                    DBObject obj = cursor.next();
                    
                    AlbumImageData image = new AlbumImageData();
                    image.imageId = (String) obj.get(UserdbKey.ALBUM_IMAGE.IMAGE_ID);
                    image.time = (Long) obj.get(UserdbKey.ALBUM_IMAGE.TIME);
                    
                    imageList.add(image);
                }
                album.imageList = imageList;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return album;
    }
    
    
    public static void addAlbumImage(String albumId, List<String> listImage, Long time) throws EazyException{
        try{
            for(String imageId: listImage){
                BasicDBObject insertObj = new BasicDBObject();
                insertObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
                insertObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, imageId);
                insertObj.append(UserdbKey.ALBUM_IMAGE.TIME, time);
                
                coll.insert(insertObj);
//                ObjectId id = (ObjectId) obj.get(StaticFiledbKey.IMAGE.ID);
//                result = id.toString();
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static void delAlbumImage(String albumId, List<String> listImage) throws EazyException{
        try{
            BasicDBObject deleteObj = new BasicDBObject();
            deleteObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
            deleteObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, new BasicDBObject("$in", listImage));
            coll.remove(deleteObj);
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static List<AlbumImageData> getListImage(String albumId) throws EazyException{
        List<AlbumImageData> result = new ArrayList<>();
        try{
            BasicDBList value = new BasicDBList();
            value.add(null);
            value.add(Constant.FLAG.ON);
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
            findObj.append(UserdbKey.ALBUM_IMAGE.FLAG, new BasicDBObject("$in", value));
            
            BasicDBObject sortObj = new BasicDBObject();
            sortObj.append(UserdbKey.ALBUM_IMAGE.TIME, -1);
            DBCursor cursor = coll.find(findObj).sort(sortObj);
            Integer numberImage = coll.find(findObj).count();
            while (cursor.hasNext()){
                DBObject obj = cursor.next();
                
                AlbumImageData image = new AlbumImageData();
                image.imageId = (String) obj.get(UserdbKey.ALBUM_IMAGE.IMAGE_ID);
                image.time = (Long) obj.get(UserdbKey.ALBUM_IMAGE.TIME);
                image.numberImage = numberImage;
                result.add(image);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Integer getImageNumber(String albumId) throws EazyException{
        Integer result = 0;
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.ALBUM_ID, albumId);
            result = coll.find(findObj).count();
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getAlbumId(String imgId){
        String result = "";
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, imgId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String albumId = (String) obj.get(UserdbKey.ALBUM_IMAGE.ALBUM_ID);
                result = albumId;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static void updateFlag(String imgId, Integer flag){
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, imgId);

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(UserdbKey.ALBUM_IMAGE.FLAG, flag);

            coll.update(findObj, new BasicDBObject("$set", updateObj));
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
