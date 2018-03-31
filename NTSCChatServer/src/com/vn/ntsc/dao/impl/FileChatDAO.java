/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.chatserver.pojos.message.Message;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Phan Huy
 */
public class FileChatDAO {
    private static DBCollection coll;
    private static DB dbstatics;
    private static final String Field_ServerTime = "time_sender";
    private static final String File_Type_Video = "video";
    private static final String File_Type_Audio = "audio";
    private static final String File_Type_Image = "image";
    private static final String Size_Image = "size_image";
    private static final String Size_Audio = "size_audio";
    private static final String Size_Video = "size_video";
    private static final String Size_All_File = "size_all_file";
    
    static {
        try {
            dbstatics = CommonDAO.mongo.getDB(StaticFiledbKey.DB_NAME);
            coll = dbstatics.getCollection(StaticFiledbKey.FILE_CHAT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    private static final BasicDBObject OrderBy_ServerTime_Desc = new BasicDBObject(Field_ServerTime, -1);
    
    public static boolean insertFileChat(Message msg) throws EazyException, ParseException {
        JSONArray array = msg.files;
        JSONObject jsonObject = (JSONObject)array.get(0);
        boolean result = false;
        try {
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE_CHAT.FROM, msg.from);
            String fileType = (String)jsonObject.get("file_type");
            if(fileType.equals(File_Type_Video) || fileType.equals(File_Type_Audio)){
                obj.append(StaticFiledbKey.FILE_CHAT.TO, msg.to);
                obj.append(StaticFiledbKey.FILE_CHAT.FROM, msg.from);
                obj.append(StaticFiledbKey.FILE_CHAT.TIME_SENDER, msg.serverTime);
//                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Long)jsonObject.get("thumbnail_height"));
                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String)jsonObject.get("thumbnail_url"));
//                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double)jsonObject.get("thumbnail_width"));
//                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Long)jsonObject.get("original_height"));
//                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Long)jsonObject.get("original_width"));
                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String)jsonObject.get("original_url"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_ID, (String)jsonObject.get("file_id"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_TYPE, (String)jsonObject.get("file_type"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_DURATION, (Long)jsonObject.get("file_duration"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_URL, (String)jsonObject.get("file_url"));
            }
            if(fileType.equals(File_Type_Image)){
                obj.append(StaticFiledbKey.FILE_CHAT.TO, msg.to);
                obj.append(StaticFiledbKey.FILE_CHAT.FROM, msg.from);
                obj.append(StaticFiledbKey.FILE_CHAT.TIME_SENDER, msg.serverTime);
//                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Long)jsonObject.get("thumbnail_height"));
                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String)jsonObject.get("thumbnail_url"));
//                obj.append(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double)jsonObject.get("thumbnail_width"));
//                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Long)jsonObject.get("original_height"));
//                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Long)jsonObject.get("original_width"));
                obj.append(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String)jsonObject.get("original_url"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_ID, (String)jsonObject.get("file_id"));
                obj.append(StaticFiledbKey.FILE_CHAT.FILE_TYPE, (String)jsonObject.get("file_type"));
            }
            Util.addDebugLog("obj============================="+obj.toString());
            coll.insert(obj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static String getFileChat(String from, String to,Long take,Long skip,Long type) throws EazyException, ParseException {
        JSONObject jsonObject = new JSONObject();
        JSONArray arrayImage = new JSONArray();
        JSONArray arrayVideo = new JSONArray();
        JSONArray arrayAudio = new JSONArray();
        try {
            BasicDBObject objFrom = new BasicDBObject(StaticFiledbKey.FILE_CHAT.FROM, from);
            objFrom.append("to", to);
            
            BasicDBObject objTo = new BasicDBObject(StaticFiledbKey.FILE_CHAT.TO, from);
            objTo.append("from", to);
            
            BasicDBList fromOrTo = new BasicDBList();
            fromOrTo.add(objFrom);
            fromOrTo.add(objTo);
            
            DBObject obj = new BasicDBObject( "$or", fromOrTo );
            
            DBCursor find = null;
            if(take == null && skip == null){
                find = coll.find(obj).sort(OrderBy_ServerTime_Desc);
            }else{
                find = coll.find(obj).sort(OrderBy_ServerTime_Desc).skip(skip.intValue()).limit(take.intValue());
            }
            while(find.hasNext()){
                DBObject fileChat  = find.next();
                String fileType = (String)fileChat.get("file_type");
                if(File_Type_Audio.equals(fileType)){
                    JSONObject audio = new JSONObject();
                    createJsonArrayVideo(audio,arrayAudio,fileChat);
                }
                if(File_Type_Image.equals(fileType)){
                    JSONObject image = new JSONObject();
                    createJsonArrayImage(image,arrayImage,fileChat);
                }
                if(File_Type_Video.equals(fileType)){
                    JSONObject video = new JSONObject();
                    createJsonArrayVideo(video,arrayVideo,fileChat);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        if(type == null || type == 0){
            jsonObject.put(File_Type_Image, arrayImage);
            jsonObject.put(Size_Image, arrayImage.size());
            jsonObject.put(File_Type_Audio, arrayAudio);
            jsonObject.put(Size_Audio, arrayAudio.size());
            jsonObject.put(File_Type_Video, arrayVideo);
            jsonObject.put(Size_Video, arrayVideo.size());
            jsonObject.put(Size_All_File, arrayVideo.size() + arrayAudio.size() + arrayImage.size());
        }else{
            if(type == 1){
                jsonObject.put(File_Type_Image, arrayImage);
                jsonObject.put(Size_Image, arrayImage.size());
            }else if(type == 2){
                jsonObject.put(File_Type_Video, arrayVideo);
                jsonObject.put(Size_Video, arrayVideo.size());
            }else{
                jsonObject.put(File_Type_Audio, arrayAudio);
                jsonObject.put(Size_Audio, arrayAudio.size());
            }
        }
        jsonObject.put("to", to);
        jsonObject.put("from", from);
        return jsonObject.toJSONString();
    }
    
    private static void createJsonArrayImage(JSONObject image, JSONArray array,DBObject fileChat){
//        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Double) fileChat.get("thumbnail_height"));
        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String) fileChat.get("thumbnail_url"));
//        image.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double) fileChat.get("THUMBNAIL_WIDTH"));
//        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Double) fileChat.get("ORIGINAL_HEIGHT"));
//        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Double) fileChat.get("ORIGINAL_WIDTH"));
        image.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String) fileChat.get("original_url"));
        image.put(StaticFiledbKey.FILE_CHAT.FILE_ID, (String) fileChat.get("file_id"));
        image.put(StaticFiledbKey.FILE_CHAT.TIME_SENDER, (Long) fileChat.get(StaticFiledbKey.FILE_CHAT.TIME_SENDER));
        array.add(image);
    }
    private static void createJsonArrayVideo(JSONObject video, JSONArray array,DBObject fileChat){
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_HEIGHT, (Double) fileChat.get("thumbnail_height"));
        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_URL, (String) fileChat.get("thumbnail_url"));
//        video.put(StaticFiledbKey.FILE_CHAT.THUMBNAIL_WIDTH, (Double) fileChat.get("THUMBNAIL_WIDTH"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_HEIGHT, (Double) fileChat.get("ORIGINAL_HEIGHT"));
//        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_WIDTH, (Double) fileChat.get("ORIGINAL_WIDTH"));
        video.put(StaticFiledbKey.FILE_CHAT.ORIGINAL_URL, (String) fileChat.get("original_url"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_ID, (String) fileChat.get("file_id"));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_DURATION, (Long) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_DURATION));
        video.put(StaticFiledbKey.FILE_CHAT.FILE_URL, (String) fileChat.get(StaticFiledbKey.FILE_CHAT.FILE_URL));
        video.put(StaticFiledbKey.FILE_CHAT.TIME_SENDER, (Long) fileChat.get(StaticFiledbKey.FILE_CHAT.TIME_SENDER));
        array.add(video);
    }
}
