/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.SystemAccount;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogNotification;

/**
 *
 * @author RuAc0n
 */
public class LogNotificationDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_NOTIFICATION_COLLECTION);
            coll.createIndex(new BasicDBObject(LogdbKey.LOG_NOTIFICATION.TO_USER_ID, 1));
            coll.createIndex(new BasicDBObject(LogdbKey.LOG_NOTIFICATION.TIME, 1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    public static SizedListData listLog(List<String> listId, String fromTime, String toTime,
            Long type, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (type != null) {
                findObject.append(LogdbKey.LOG_NOTIFICATION.TYPE, type.intValue());
            }
            if (listId != null) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < listId.size(); i++) {
                    list.add(listId.get(i).toString());
                }
                BasicDBObject inObj = new BasicDBObject("$in", list);
                findObject.append(LogdbKey.LOG_NOTIFICATION.TO_USER_ID, inObj);
            }
            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_NOTIFICATION.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_NOTIFICATION.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_NOTIFICATION.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_NOTIFICATION.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_NOTIFICATION.TIME, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if(!isCsv)
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            List<IEntity> list = new ArrayList<IEntity>();
            
            List<IEntity> listSysAcc = UserDAO.getAllSystemAcc();
            IEntity user =null;
            SystemAccount account =null;
            
            while (cursor.hasNext()) {
                LogNotification lp = new LogNotification();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String uId = dbO.getString(LogdbKey.LOG_NOTIFICATION.TO_USER_ID);
                lp.userId = uId;
                Integer tpe = dbO.getInt(LogdbKey.LOG_NOTIFICATION.TYPE);
                lp.type = tpe;
                String partnerId = dbO.getString(LogdbKey.LOG_NOTIFICATION.FROM_USER_ID);
                lp.partnerId = partnerId;
                //thanhdd edit 17/11/2016
                int isAdmin=0;
                for (int i =0; i<listSysAcc.size(); i++){
                    user = listSysAcc.get(i);
                    if(user instanceof SystemAccount)
                        account = (SystemAccount)user;
                    if (lp.userId !=null && account.id!=null){
                        if (lp.userId.equals(account.id)){
                            isAdmin=1;
                            break;
                        }
                    }
                }
                lp.isAdmin = isAdmin;
                
                String time = dbO.getString(LogdbKey.LOG_NOTIFICATION.TIME);
                lp.time = time;
                String ip = dbO.getString(LogdbKey.LOG_NOTIFICATION.IP);
                lp.ip = ip; 
                
                list.add(lp);
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
