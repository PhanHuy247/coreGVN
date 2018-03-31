/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.ExtraPage;


/**
 *
 * @author RuAc0n
 */
public class ExtraPageDAO {
    private static DBCollection coll;


    static{
         try{
             coll = DBLoader.getSettingDB().getCollection( SettingdbKey.EXTRA_PAGE_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
 
    public static List<ExtraPage> getList() throws EazyException {
        List<ExtraPage>  result = new ArrayList<>();
        try {
            BasicDBObject sort = new BasicDBObject(SettingdbKey.EXTRA_PAGE.ORDER, 1);
            DBCursor cursor = coll.find().sort(sort).limit(5);
            while(cursor.hasNext()){
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.EXTRA_PAGE.ID).toString();
                String title = obj.getString(SettingdbKey.EXTRA_PAGE.PAGE_TITLE);
                String url = obj.getString(SettingdbKey.EXTRA_PAGE.URL);
                result.add(new ExtraPage(id, title, url));
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }       
}
