/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author hoangnh
 */
public class BuzzDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getBuzzDB().getCollection(BuzzdbKey.BUZZ_DETAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static String getBuzzFileId(String buzzId) throws EazyException {
        String fileId = null;
        try {
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                DBObject dboject = coll.findOne(obj);
                if (dboject != null) {
                    fileId = (String) dboject.get(BuzzdbKey.BUZZ_DETAIL.FILE_ID);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return fileId;
    }
     public static Integer getBuzzType(String buzzId) throws EazyException {
        Integer buzzType = null;
        try {
            Boolean isValid = ObjectId.isValid(buzzId);
            if(isValid){
                ObjectId id = new ObjectId(buzzId);
                BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_DETAIL.ID, id);
                DBObject dboject = coll.findOne(obj);
                if (dboject != null) {
                    buzzType = (Integer) dboject.get(BuzzdbKey.BUZZ_DETAIL.BUZZ_TYPE);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return buzzType;
    }
    
    public static String updateStreamStatus(String buzzId,String status) throws EazyException {
        String fileId = null;
        try {
           BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("stream_status", status));

           BasicDBObject searchQuery = new BasicDBObject().append(BuzzdbKey.BUZZ_DETAIL.ID, new ObjectId(buzzId));

           coll.update(searchQuery, newDocument);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return fileId;
    }
}
