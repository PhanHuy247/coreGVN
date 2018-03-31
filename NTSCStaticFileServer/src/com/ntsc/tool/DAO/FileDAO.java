/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntsc.tool.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.ntsc.tool.constants.Config;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author HuyDX
 */
public class FileDAO {
    
    private static Mongo mongo;
    private static DBCollection coll;

    static {
        try {
            mongo = new Mongo(Config.DB_SERVER);
            coll = mongo.getDB("staticfiledb").getCollection("file");
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
        }
    }
    
    public static String insertFileUrl(String yyyymmdd, String url) {
        String result = null;
        try {
            ObjectId _id = new ObjectId(DateFormat.parse_yyyyMMdd(yyyymmdd));
            BasicDBObject obj = new BasicDBObject(StaticFiledbKey.FILE.ID, _id);
            obj.append(StaticFiledbKey.FILE.URL, url);
            coll.insert(obj);
            result = _id.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void deleteById(String id){
        try {
            BasicDBObject obj = new BasicDBObject("_id", new ObjectId(id));
            coll.remove(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static List<FileInfo> getFileByTimeStamp(long timestamp){
        List<FileInfo> files = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()){
                DBObject obj = cur.next();
                ObjectId id = (ObjectId) obj.get("_id");
                long fileTime = id.getTime();
                if(fileTime < timestamp){
                    String url = (String) obj.get("url");
                    FileInfo file = new FileInfo(id.toString(), url);
                    files.add(file);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return files;
    }    
    
    public static int count(){
        return coll.find().count();
    }
}
   
