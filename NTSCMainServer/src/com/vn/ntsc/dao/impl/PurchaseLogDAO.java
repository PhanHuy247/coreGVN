/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.CashdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class PurchaseLogDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getCashDB().getCollection(CashdbKey.PURCHASE_LOG_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String add(String userId, long time, String packetId, int point, String applicationId) throws EazyException{
        String result = null;
         try{
            BasicDBObject insertObj =  new BasicDBObject();
            insertObj.append(CashdbKey.PURCHASE_LOG.USER_ID, userId);
            insertObj.append(CashdbKey.PURCHASE_LOG.POINT, point);
            insertObj.append(CashdbKey.PURCHASE_LOG.TIME, time);
            insertObj.append(CashdbKey.PURCHASE_LOG.PACKET_ID, packetId);
            insertObj.append(CashdbKey.PURCHASE_LOG.APPLICATION_ID, applicationId);
            coll.insert(insertObj);
            result = insertObj.get(CashdbKey.PURCHASE_LOG.ID).toString();
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
