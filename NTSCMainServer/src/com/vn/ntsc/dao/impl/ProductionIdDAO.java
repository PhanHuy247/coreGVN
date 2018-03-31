/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.PointPacket;

/**
 *
 * @author RuAc0n
 */
public class ProductionIdDAO{

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getCashDB().getCollection( CashdbKey.PRODUCTION_ID_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }    

    public static void getProductionId(Map <String, PointPacket> mapPacket, int type) throws EazyException{
         try{
            List<ObjectId> listId = new ArrayList<>();
            Collection<PointPacket> cPacket = mapPacket.values();
            Iterator<PointPacket> it = cPacket.iterator();
            while (it.hasNext()) {
                PointPacket pp = it.next();
                listId.add(new ObjectId(pp.id));
            }
            DBObject query = QueryBuilder.start(CashdbKey.PRODUCTION_ID.ID).in(listId).get();
            DBCursor cur = coll.find(query);
            while (cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                String id = obj.getString(CashdbKey.PRODUCTION_ID.ID);
                PointPacket pp = mapPacket.get(id);
                if(type == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION){
                    String proId = obj.getString(CashdbKey.PRODUCTION_ID.APPLE_PRODUCTION_ID);
                    pp.productId = proId;
                    mapPacket.put(id, pp);
                }else if(type == Constant.PURCHASE_PRODUCTION_TYPE.GOOLE_PRODUCTION){
                    String proId = obj.getString(CashdbKey.PRODUCTION_ID.GOOGLE_PRODUCTION_ID);
                    pp.productId = proId;
                    mapPacket.put(id, pp);
                }
            }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static boolean updateAppleProductId(String packetId, String appleId) throws EazyException{
        boolean result = false;
         try{
             ObjectId id = new ObjectId(packetId);
             DBObject findObj = new BasicDBObject(CashdbKey.PRODUCTION_ID.ID, id);
             DBObject updateObj = new BasicDBObject(CashdbKey.PRODUCTION_ID.APPLE_PRODUCTION_ID, appleId);
             DBObject setObj = new BasicDBObject("$set", updateObj);
             coll.update(findObj, setObj, true, false);
             result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateGoogleProductId(String packetId, String googleId) throws EazyException{
        boolean result = false;
         try{
             ObjectId id = new ObjectId(packetId);
             DBObject findObj = new BasicDBObject(CashdbKey.PRODUCTION_ID.ID, id);
             DBObject updateObj = new BasicDBObject(CashdbKey.PRODUCTION_ID.GOOGLE_PRODUCTION_ID, googleId);
             DBObject setObj = new BasicDBObject("$set", updateObj);
             coll.update(findObj, setObj, true, false);
             result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
