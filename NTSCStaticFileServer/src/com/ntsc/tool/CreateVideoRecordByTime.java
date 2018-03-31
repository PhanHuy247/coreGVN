/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntsc.tool;

import com.ntsc.tool.constants.Config;
import com.ntsc.tool.constants.Constant;
import com.ntsc.tool.DAO.FileDAO;
import com.ntsc.tool.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 *
 * @author HuyDX
 */
public class CreateVideoRecordByTime {
    public static void process(){
        if (Config.INSERT_TYPE==1)
            for (int i=0; i<Config.DAYS_TO_REPEAT; i++)
                insertFile(Config.DEFAULT_YEAR, Config.DEFAULT_MONTH, Config.DEFAULT_DAY);
        else if (Config.INSERT_TYPE==2)
            insertMultipleDates();
    }
    
    private static void insertMultipleDates(){
        for (int year =0; year<Config.NUMBER_OF_YEAR; year++)
            for (int month = 1; month<=Config.NUMBER_OF_MONTHS; month++)
                for (int day = 1; day<=Config.NUMBER_OF_DAYS; day++){
                    insertFile(year+Config.DEFAULT_YEAR, month, day);
                }
    }
    
    private static void insertFile(int year, int month, int day){
        try {
            StringBuilder date = new StringBuilder();
            date.append(year).append(Utils.format2DNumber(month)).append(Utils.format2DNumber(day));
            String fileUrl = createPhysicalFile();
            FileDAO.insertFileUrl(date.toString(), fileUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            
    }
    
    private static String createPhysicalFile() throws IOException{
        String fileName = createFileName();        
        String fileUrl = Constant.MOCKUP_FOLDER + fileName ;
        File source = new File(Constant.SAMPLE_FILE_PATH);
        File dest = new File(Constant.FILE_FOLDER_PATH + fileUrl);
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);    
        return fileUrl;
    }
    
    private static String createFileName(){
        String fileName = UUID.randomUUID().toString() + ".mp4";  
        return fileName;
    }
}
