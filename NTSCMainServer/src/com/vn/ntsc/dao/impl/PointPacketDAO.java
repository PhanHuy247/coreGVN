/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
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
public class PointPacketDAO{

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getCashDB().getCollection( CashdbKey.POINT_PACKET_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }    

//    public static List<PointPacket> getAllPointPacket(int type) throws EazyException{
//        List<PointPacket> result = new ArrayList<>();
//         try{
//            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.TYPE, type);
//            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
//            DBCursor cur = coll.find(findObj);
//            while(cur.hasNext()){
//                BasicDBObject obj = (BasicDBObject) cur.next();
//                PointPacket pp = new PointPacket();
//                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
//                pp.id = id.toString();
//                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
//                pp.price = price;
//                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
//                pp.point = point;
//                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
//                pp.description = des;
//                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
//                pp.productId = proId;
//                result.add(pp);
//            }
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }
//
//    public static PointPacket getPointPacket(String packageId) throws EazyException{
//        PointPacket result = null;
//         try{
//            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.ID, new ObjectId(packageId));
//            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
//            DBCursor cur = coll.find(findObj);
//            while(cur.hasNext()){
//                result = new PointPacket();
//                BasicDBObject obj = (BasicDBObject) cur.next();
//                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
//                result.id = id.toString();
//                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
//                result.price = price;
//                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
//                result.point = point;
//                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
//                result.description = des;
//                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
//                result.productId = proId;
//            }
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
//    
//    public static PointPacket getPointPacketByProductId(String productId) throws EazyException{
//        PointPacket result = null;
//         try{
//            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.PRODUCTION_ID, productId);
//            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
//            DBCursor cur = coll.find(findObj);
//            while(cur.hasNext()){
//                result = new PointPacket();
//                BasicDBObject obj = (BasicDBObject) cur.next();
//                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
//                result.id = id.toString();
//                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
//                result.price = price;
//                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
//                result.point = point;
//                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
//                result.description = des;
//                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
//                result.productId = proId;
//            }
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
//    
//    public static int getPoint(String packetId) throws EazyException{
//        int result = 0;
//         try{
//            ObjectId id = new ObjectId(packetId);
//            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.ID, id);
//            DBObject obj = coll.findOne(findObj);
//
//            if(obj != null){
//                Integer point = (Integer) obj.get(CashdbKey.POINT_PACKET.POINT);
//                if(point != null)
//                    result = point;
//            }
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }
//
//    public static Double getPrice(String packetId) throws EazyException{
//        Double result = null;
//         try{
//            ObjectId id = new ObjectId(packetId);
//            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.ID, id);
//            DBObject obj = coll.findOne(findObj);
//
//            if(obj != null){
//                Double price = (Double) obj.get(CashdbKey.POINT_PACKET.PRICE);
//                result = price;
//            }
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }    
//    
//    public static String insertPacket(double price, int point, String des, int type, String productionId) throws EazyException{
//        String result = null;
//         try{
//             DBObject insertObj = new BasicDBObject(CashdbKey.POINT_PACKET.PRICE, price);
//             insertObj.put(CashdbKey.POINT_PACKET.POINT, point);
//             insertObj.put(CashdbKey.POINT_PACKET.DESCRIPTION, des);
//             insertObj.put(CashdbKey.POINT_PACKET.TYPE, type);
//             insertObj.put(CashdbKey.POINT_PACKET.PRODUCTION_ID, productionId);
//             insertObj.put(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
//             coll.insert(insertObj);
//             result = insertObj.get(CashdbKey.POINT_PACKET.ID).toString();
//        }catch( Exception ex ){
//            Util.addErrorLog(ex);            
//            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
//        }
//        return result;
//    }

}
