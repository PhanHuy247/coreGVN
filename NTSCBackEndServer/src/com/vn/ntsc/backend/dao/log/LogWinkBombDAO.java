/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogWinkBomb;

/**
 *
 * @author RuAc0n
 */
public class LogWinkBombDAO {

    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.LOG_WINK_BOMB_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static SizedListData listLog(List<String> listId, String fromTime, String toTime, Long fromPoint, Long toPoint,
            String message, Long bombNumber, Long sort, Long order, Long skip, Long take, boolean isCsv) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if (bombNumber != null) {
                findObject.append(LogdbKey.LOG_WINK_BOM.BOMB_NUMBER, bombNumber.intValue());
            }

            if (fromPoint != null && toPoint != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromPoint);
                BasicDBObject lte = new BasicDBObject("$lte", toPoint);
                ands[0] = new BasicDBObject(LogdbKey.LOG_WINK_BOM.POINT, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_WINK_BOM.POINT, gte);
                findObject.append("$and", ands);
            } else {
                if (fromPoint != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromPoint);
                    findObject.append(LogdbKey.LOG_WINK_BOM.POINT, gte);
                }
                if (toPoint != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toPoint);
                    findObject.append(LogdbKey.LOG_WINK_BOM.POINT, lte);
                }
            }

            if (message != null) {
                findObject.append(LogdbKey.LOG_WINK_BOM.MESSAGE, message);
            }

            if (listId != null) {
                BasicDBObject inObj = new BasicDBObject("$in", listId);
                findObject.append(LogdbKey.LOG_WINK_BOM.USER_ID, inObj);
            }

            if (fromTime != null && toTime != null) {
                BasicDBObject[] ands = new BasicDBObject[2];
                BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                BasicDBObject lte = new BasicDBObject("$lte", toTime);
                ands[0] = new BasicDBObject(LogdbKey.LOG_WINK_BOM.TIME, lte);
                ands[1] = new BasicDBObject(LogdbKey.LOG_WINK_BOM.TIME, gte);
                findObject.append("$and", ands);
            } else {
                if (fromTime != null) {
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    findObject.append(LogdbKey.LOG_WINK_BOM.TIME, gte);
                }
                if (toTime != null) {
                    BasicDBObject lte = new BasicDBObject("$lte", toTime);
                    findObject.append(LogdbKey.LOG_WINK_BOM.TIME, lte);
                }
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.LOG_WINK_BOM.TIME, or);
            } else if (sort == 2) {
                sortObj.append(LogdbKey.LOG_WINK_BOM.POINT, or);
            } else if (sort == 3) {
                sortObj.append(LogdbKey.LOG_WINK_BOM.BOMB_NUMBER, or);
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
            while (cursor.hasNext()) {
                LogWinkBomb log = new LogWinkBomb();
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String id = dbO.getObjectId(LogdbKey.LOG_WINK_BOM.ID).toString();
                log.id = id;

                String uId = dbO.getString(LogdbKey.LOG_WINK_BOM.USER_ID);
                log.userId = uId;

                String time = dbO.getString(LogdbKey.LOG_WINK_BOM.TIME);
                log.time = time;

                Integer bombNum = dbO.getInt(LogdbKey.LOG_WINK_BOM.BOMB_NUMBER);
                log.bombNumber = bombNum;

                Integer pnt = dbO.getInt(LogdbKey.LOG_WINK_BOM.POINT);
                log.point = pnt;

                String mess = dbO.getString(LogdbKey.LOG_WINK_BOM.MESSAGE);
                log.message = mess;

                String ip = dbO.getString(LogdbKey.LOG_WINK_BOM.IP);
                log.ip = ip;                
                
                list.add(log);
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getDetailWinkBomb(String id) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            ObjectId oId = null;
            try {
                oId = new ObjectId(id);
            } catch (Exception ex) {
               
            }
            if (oId != null) {
                BasicDBObject findObj = new BasicDBObject(LogdbKey.LOG_WINK_BOM.ID, oId);
                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    BasicDBList list = (BasicDBList) obj.get(LogdbKey.LOG_WINK_BOM.LIST_RECIVER);
                    if (list != null && !list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            result.add(list.get(i).toString());
                        }
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
