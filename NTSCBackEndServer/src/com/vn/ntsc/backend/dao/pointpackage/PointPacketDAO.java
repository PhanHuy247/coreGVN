/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.pointpackage;

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
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.pointpackage.PointPacket;

/**
 *
 * @author RuAc0n
 */
public class PointPacketDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getCashDB().getCollection(CashdbKey.POINT_PACKET_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<PointPacket> getPointPacketByProductId(String productId) throws EazyException {
        List<PointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.PRODUCTION_ID, productId);
            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                PointPacket res = new PointPacket();
                BasicDBObject obj = (BasicDBObject) cur.next();
                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
                res.id = id.toString();
                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
                res.price = price;
                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
                res.point = point;
                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
                res.description = des;
                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
                res.productId = proId;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<PointPacket> get(int type) throws EazyException {
        List<PointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                PointPacket pp = new PointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
                pp.id = id.toString();
                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
                pp.price = price;
                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
                pp.point = point;
                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
                pp.description = des;
                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
                pp.productId = proId;
                result.add(pp);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<PointPacket> getMultiapp(int type, String applicationId) throws EazyException {
        List<PointPacket> result = new ArrayList<>();
        try {
            BasicDBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.TYPE, type);
            findObj.append(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
            findObj.append(CashdbKey.POINT_PACKET.APPLICATION_ID, applicationId);
            DBCursor cur = coll.find(findObj);
            while (cur.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cur.next();
                PointPacket pp = new PointPacket();
                ObjectId id = obj.getObjectId(CashdbKey.POINT_PACKET.ID);
                pp.id = id.toString();
                Double price = obj.getDouble(CashdbKey.POINT_PACKET.PRICE);
                pp.price = price;
                Integer point = obj.getInt(CashdbKey.POINT_PACKET.POINT);
                pp.point = point;
                String des = obj.getString(CashdbKey.POINT_PACKET.DESCRIPTION);
                pp.description = des;
                String proId = obj.getString(CashdbKey.POINT_PACKET.PRODUCTION_ID);
                pp.productId = proId;
                result.add(pp);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String insert(double price, int point, String des, int type, String productionId) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CashdbKey.POINT_PACKET.PRICE, price);
            insertObj.put(CashdbKey.POINT_PACKET.POINT, point);
            insertObj.put(CashdbKey.POINT_PACKET.DESCRIPTION, des);
            insertObj.put(CashdbKey.POINT_PACKET.TYPE, type);
            insertObj.put(CashdbKey.POINT_PACKET.PRODUCTION_ID, productionId);
            insertObj.put(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
            coll.insert(insertObj);
            result = insertObj.get(CashdbKey.POINT_PACKET.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String insertMultiapp(String applicationId,double price, int point, String des, int type, String productionId) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(CashdbKey.POINT_PACKET.PRICE, price);
            insertObj.put(CashdbKey.POINT_PACKET.POINT, point);
            insertObj.put(CashdbKey.POINT_PACKET.DESCRIPTION, des);
            insertObj.put(CashdbKey.POINT_PACKET.TYPE, type);
            insertObj.put(CashdbKey.POINT_PACKET.PRODUCTION_ID, productionId);
            insertObj.put(CashdbKey.POINT_PACKET.FLAG, Constant.FLAG.ON);
            insertObj.put(CashdbKey.POINT_PACKET.APPLICATION_ID, applicationId);
            coll.insert(insertObj);
            result = insertObj.get(CashdbKey.POINT_PACKET.ID).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id, double price, int point, String des, String productionId) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.ID, new ObjectId(id));
            DBObject updateObject = new BasicDBObject(CashdbKey.POINT_PACKET.PRICE, price);
            updateObject.put(CashdbKey.POINT_PACKET.POINT, point);
            updateObject.put(CashdbKey.POINT_PACKET.DESCRIPTION, des);
            updateObject.put(CashdbKey.POINT_PACKET.PRODUCTION_ID, productionId);
            DBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean delete(String id) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(CashdbKey.POINT_PACKET.ID, new ObjectId(id));
            DBObject updateObject = new BasicDBObject(CashdbKey.POINT_PACKET.FLAG, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
            DBObject setObj = new BasicDBObject("$set", updateObject);
            coll.update(findObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
