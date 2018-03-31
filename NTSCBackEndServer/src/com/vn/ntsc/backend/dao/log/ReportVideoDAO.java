/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.file.FileDAO;
import com.vn.ntsc.backend.dao.thumbnail.ThumbNailDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.VideoDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogFileReport;
import com.vn.ntsc.backend.entity.impl.log.LogImageReport;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoangnh
 */
public class ReportVideoDAO {
    private static DBCollection coll;
    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.VIDEO_REPORT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static SizedListData listLog(String fileId, List<String> listRequestId, List<String> listPartnerId, String fromTime, String toTime, Long reportType,
            Long sort, Long order, Long skip, Long take, boolean isCsv,Long privacy) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            BasicDBObject findObject = new BasicDBObject();
            if(fileId != null){
                findObject.append(LogdbKey.VIDEO_REPORT.VIDEO_ID, fileId);
            }else{ 
                if (listRequestId != null) {
                    BasicDBObject inObj = new BasicDBObject("$in", listRequestId);
                    findObject.append(LogdbKey.VIDEO_REPORT.REPORT_ID, inObj);
                }
                if (listPartnerId != null) {
                    BasicDBObject inObj = new BasicDBObject("$in", listPartnerId);
                    findObject.append(LogdbKey.VIDEO_REPORT.USER_ID, inObj);
                }
                if (fromTime != null && toTime != null) {
                    BasicDBObject[] ands = new BasicDBObject[2];
                    BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                    BasicDBObject lte = new BasicDBObject("$lt", toTime);
                    ands[0] = new BasicDBObject(LogdbKey.VIDEO_REPORT.REPORT_TIME, lte);
                    ands[1] = new BasicDBObject(LogdbKey.VIDEO_REPORT.REPORT_TIME, gte);
                    findObject.append("$and", ands);
                } else {
                    if (fromTime != null) {
                        BasicDBObject gte = new BasicDBObject("$gte", fromTime);
                        findObject.append(LogdbKey.VIDEO_REPORT.REPORT_TIME, gte);
                    }
                    if (toTime != null) {
                        BasicDBObject lte = new BasicDBObject("$lte", toTime);
                        findObject.append(LogdbKey.VIDEO_REPORT.REPORT_TIME, lte);
                    }
                }
                if(reportType != null)
                    findObject.append(LogdbKey.VIDEO_REPORT.REPORT_TYPE, reportType);
                if(privacy != null)
                    findObject.append(LogdbKey.VIDEO_REPORT.VIDEO_TYPE, privacy);
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(LogdbKey.VIDEO_REPORT.REPORT_TIME, or);
            }
            DBCursor cursor = null;
            if (findObject.isEmpty()) {
                cursor = coll.find().sort(sortObj);
            } else {
                cursor = coll.find(findObject).sort(sortObj);
            }
            int size = cursor.size();
            if(!isCsv)
//                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
                cursor = cursor.limit(take.intValue()+skip.intValue());
            List<IEntity> list = new ArrayList<IEntity>();
            while (cursor.hasNext()) {
                BasicDBObject dbO = (BasicDBObject) cursor.next();
                String iId = dbO.getString(LogdbKey.VIDEO_REPORT.VIDEO_ID);
                String partnerIdId = (String)dbO.getString(LogdbKey.VIDEO_REPORT.USER_ID);
                LogFileReport log = new LogFileReport();
                log.fileId = iId;
                log.partnerId = partnerIdId;
                String reqId = dbO.getString(LogdbKey.VIDEO_REPORT.REPORT_ID);
                log.reqId = reqId;
                String time = dbO.getString(LogdbKey.VIDEO_REPORT.REPORT_TIME);
                log.time = time;
                Integer type = dbO.getInt(LogdbKey.VIDEO_REPORT.REPORT_TYPE);
                log.reportType = type;
                String ip = dbO.getString(LogdbKey.VIDEO_REPORT.REPORT_IP);
                Integer videoType = (Integer)dbO.get(LogdbKey.VIDEO_REPORT.VIDEO_TYPE);
                log.privacy = videoType;
                log.ip = ip;
                String url = FileDAO.getFileUrl(iId);
                if(url == null) continue;
                if(url.contains("http"))
                    log.url = url;
                else 
                    log.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE +url;
                
                if(url.contains("mp3")){
                    String thumnail = ImageDAO.getImageUrl(iId);
                    if(thumnail != null){
                        log.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                    }
                }
                if(url.contains("mp4")){
                    String thumnail = ThumbNailDAO.getThumbnailUrl(iId);
                    if(thumnail != null){
                        log.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                    }
                }
                
                list.add(log);

            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
}
