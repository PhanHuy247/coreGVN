/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import com.vn.ntsc.buzzserver.dao.DAO;

/**
 *
 * @author DuongLTD
 */
public class SeenDAO {

    private static DBCollection coll;
    static{
         try{
            coll = DAO.getBuzzDB().getCollection( BuzzdbKey.BUZZ_SEEN_COLLECTION );
        } catch( Exception ex ) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean checkSeen(String buzzId,String currentUserId ) throws EazyException{
        boolean result = false;
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_SEEN.ID, id);
            DBObject obj = coll.findOne(query);
            if(obj != null){
                BasicDBList listSeen = (BasicDBList) obj.get(BuzzdbKey.BUZZ_SEEN.SEEN_LIST);
                if(listSeen != null){
                    for (Object listSeen1 : listSeen) {
                        String userId = listSeen1.toString();
                        if(userId.equals(currentUserId)){
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static void addSeenBuzz(String buzzId,String currentUserId ) throws EazyException{
        try{
            ObjectId id = new ObjectId(buzzId);
            DBObject query = new BasicDBObject(BuzzdbKey.BUZZ_SEEN.ID, id);
            BasicDBObject obj = new BasicDBObject(BuzzdbKey.BUZZ_SEEN.SEEN_LIST , currentUserId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj );
            coll.update(query, updateCommand, true, false);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
     }
}

