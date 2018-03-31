/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import java.io.IOException;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.entity.impl.UploadFileData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.common.MD5Checksum;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.videocollector.VideoCollector;

/**
 *
 * @author HuyDX
 */
public class UploadFileByChatApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayFileInput = Helper.getInputArrayByte(request);
        if (arrayFileInput == null || request.getSum() == null  || request.getSum().isEmpty()) {
            Helper.returnFailedUploadPoint(request.getMessageId(), request.getIp());
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            // write original image
            String fileUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.FILE_EXTENSION);
            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;
            Helper.writeFile(fileUrl, arrayFileInput, FilesAndFolders.FOLDERS.FILES_FOLDER, time);
            String sum = MD5Checksum.getMD5Checksum(filePath);
            if(sum == null || !sum.equals(request.getSum())){
                Helper.returnFailedUploadPoint(request.getMessageId(), request.getIp());
                respond.code = ErrorCode.UPLOAD_FILE_ERROR;
                return respond;
            }

            
            String fileId;
            String dbPath = fileUrl;
            if(Helper.isVideoFile(filePath)){
                String resizeFileUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.FILE_EXTENSION);
                String resizeFilePath = FilesAndFolders.FOLDERS.FILES_FOLDER + resizeFileUrl;
                Helper.generateVideoFile(filePath, resizeFilePath);
                dbPath = resizeFileUrl;
                //Util.deleteFile(filePath);
                fileId = FileDAO.insertFile(dbPath, request.getUserId());         
                VideoCollector.put(fileId, new FileInfo(dbPath, System.currentTimeMillis()));                
            }
            else {
                fileId = FileDAO.insertFile(dbPath, request.getUserId());                
            }         

//            if (Config.DELETED_VIDEO_TEST_MODE){
//                    fileId = FileDAO.insertVideoFileInTestMode(dbPath, request.getUserId());
//                    VideoCollector.put(fileId, new FileInfo(dbPath, DateFormat.parse_yyyyMMdd(Config.DELETED_VIDEO_TEST_DATE).getTime()));
//                }
//                else {
//                    fileId = FileDAO.insertFile(dbPath, request.getUserId());
//                    VideoCollector.put(fileId, new FileInfo(dbPath, System.currentTimeMillis()));
//                }
//            } 
//            
//            else
//                fileId = FileDAO.insertFile(dbPath, request.getUserId());
            
            respond = new EntityRespond(ErrorCode.SUCCESS, new UploadFileData(fileId));
        } catch (IOException ex) {
            Helper.returnFailedUploadPoint(request.getMessageId(), request.getIp());
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Helper.returnFailedUploadPoint(request.getMessageId(), request.getIp());
            Util.addErrorLog(ex);
        }
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());         
        return respond;
    }

}   
    
