/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.SentImage;

/**
 *
 * @author RuAc0n
 */
public class ChatImageTransactionDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.CHAT_IMAGE_TRANSACTION_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean isDocumentExist(String from, String to, String imageId){
        DBObject checkObj = new BasicDBObject().
                append(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM, from).
                append(UserdbKey.CHAT_IMAGE_TRANSACTION.TO, to).
                append(UserdbKey.CHAT_IMAGE_TRANSACTION.IMAGE_ID, imageId);
        DBObject checkResult = coll.findOne(checkObj);
        return checkResult != null;
    }
    
    public static boolean add(String from, String to, String imageId, long time, String ip) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM, from);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.USER_ID, from);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.TO, to);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.IMAGE_ID, imageId);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.IP, ip);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.SEND_TIME, time);
            coll.insert(insertObj);
            
            insertObj = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM, from);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.USER_ID, to);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.TO, to);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.IMAGE_ID, imageId);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.IP, ip);
            insertObj.put(UserdbKey.CHAT_IMAGE_TRANSACTION.SEND_TIME, time);
            coll.insert(insertObj);
           
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean deleteByUserId(String userId, String partnerId) throws EazyException {
        boolean result = false;
        try {
            BasicDBObject deleteObj = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.USER_ID, userId);
            BasicDBObject or1 = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM, partnerId);
            BasicDBObject or2 = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.TO, partnerId);
            BasicDBObject[] ors = new BasicDBObject[2];
            ors[0] = or1;
            ors[1] = or2;
            deleteObj.append("$or", ors);
            coll.remove(deleteObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static final BasicDBObject OrderBy_SeverTime_Asc = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.SEND_TIME, 1);
    public static final BasicDBObject OrderBy_ServerTime_Desc = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.SEND_TIME, -1);

    public static SizedListData list(String from, String to, int skip, int take) throws EazyException {
        Set<SentImage> set = new TreeSet<>();
        
        SizedListData result = new SizedListData();
        try {
            BasicDBObject query1 = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.USER_ID, from);
            BasicDBObject or1 = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM, to);
            BasicDBObject or2 = new BasicDBObject(UserdbKey.CHAT_IMAGE_TRANSACTION.TO, to);
            BasicDBObject[] ors = new BasicDBObject[2];
            ors[0] = or1;
            ors[1] = or2;

            query1.append("$or", ors);

            List<IEntity> respondList = new ArrayList<IEntity>();
            DBCursor cursor = coll.find(query1).sort(OrderBy_ServerTime_Desc);
            int startIndex = skip;
            if(startIndex < cursor.size() ){
                while (cursor.hasNext()) {
                    DBObject obj = cursor.next();
                    String imageId = (String) obj.get(UserdbKey.CHAT_IMAGE_TRANSACTION.IMAGE_ID);
                    String fr = (String) obj.get(UserdbKey.CHAT_IMAGE_TRANSACTION.FROM);
                    boolean isOwn = from.equals(fr);
                    SentImage image = new SentImage(imageId, isOwn);
                    boolean uniqueResult = set.add(image);
                    if (uniqueResult)
                        respondList.add(image);
                }
                int endIndex = startIndex + take;
                if (endIndex > respondList.size()) {
                    endIndex = respondList.size();
                }
                respondList = respondList.subList(startIndex, endIndex);
            }
            result = new SizedListData(respondList, cursor.size());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
