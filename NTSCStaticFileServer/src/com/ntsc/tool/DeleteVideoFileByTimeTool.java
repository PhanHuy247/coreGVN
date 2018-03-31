/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ntsc.tool;

import com.ntsc.tool.constants.Config;
import com.ntsc.tool.constants.Constant;
import com.ntsc.tool.DAO.FileDAO;
import com.ntsc.tool.DAO.FileInfo;
import com.ntsc.tool.utils.Utils;
import java.util.List;

/**
 *
 * @author Rua
 */
public class DeleteVideoFileByTimeTool {
    
    public static void process(){
        String date = Config.VIDEO_DELETE_TIME;
        processByDate(date);
    }
    
    private static void processByDate(String date) {
        try{
            long timeStamp = Utils.parse(date).getTime();
            List<FileInfo> files = FileDAO.getFileByTimeStamp(timeStamp);
            for (FileInfo fileInfo: files){
                System.out.println(fileInfo.path);
            }
            processFiles(files);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void processFiles(List<FileInfo> files) {
        for(FileInfo file : files){
            processFile(file);
        }
    }

    private static void processFile(FileInfo file) {
        String path = Constant.FILE_FOLDER_PATH + file.path;
        path = path.replace(".sh", "mp4");
        if(Utils.isVideoFile(path)){
            System.out.println(path);
            FileDAO.deleteById(file.id);
            Utils.deleteFile(path);
        }
    }
}
