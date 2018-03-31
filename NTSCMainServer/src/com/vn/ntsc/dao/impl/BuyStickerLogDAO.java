/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StampdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;

/**
 *
 * @author RuAc0n
 */
public class BuyStickerLogDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBLoader.getStampDB().getCollection( StampdbKey.BUY_STICKER_LOG_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean add(String userId, String time, String identifier, String transactionId, int productType, long timePurchase) throws EazyException{
        boolean result = false;
         try{
            BasicDBObject insertObj =  new BasicDBObject();
            insertObj.append(StampdbKey.BUY_STICKER_LOG.USER_ID, userId);
            insertObj.append(StampdbKey.BUY_STICKER_LOG.PRODUCTION_TYPE, productType);
            insertObj.append(StampdbKey.BUY_STICKER_LOG.TIME_PURCHASE, timePurchase);
            insertObj.append(StampdbKey.BUY_STICKER_LOG.UNIQUE_IDENTIFIER, identifier);
            if(productType == Constant.PURCHASE_PRODUCTION_TYPE.APPLE_PRODUCTION){
                insertObj.append(StampdbKey.BUY_STICKER_LOG.TRANSACTION_ID, transactionId);
            }
            insertObj.append(StampdbKey.BUY_STICKER_LOG.TIME, time);
            coll.insert(insertObj);
            result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }


}
