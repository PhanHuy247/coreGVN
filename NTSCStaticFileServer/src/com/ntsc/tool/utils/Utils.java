/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ntsc.tool.utils;

import com.ntsc.tool.constants.Constant;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rua
 */
public class Utils {
    
    public static boolean isVideoFile(String fileName){
        boolean result = false;
        try{
            MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
            File f = new File (fileName);
            Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);
            if(mimeTypes != null && !mimeTypes.isEmpty()){
                for(Object obj : mimeTypes){
                    String extension = obj.toString().toLowerCase();
                    if(extension.contains("video"))
                        return true;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    
    public static void deleteFile(String url){
        try {
            File file = new File(url);
            if(file.exists())
                file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }  
    
    public static Date parse(String yyyyMMddHHmmss) {
        if (yyyyMMddHHmmss == null || yyyyMMddHHmmss.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmss.substring(0, 4));
            year -= Constant.JAVA_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmss.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmss.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmss.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmss.substring(10, 12));
            int second = Integer.parseInt(yyyyMMddHHmmss.substring(12, 14));
            Date d = new Date(year, month, date, hour, min, second);
            return d;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    private static final String Zero = "0";   
    public static String format2DNumber(int n) {
        return n > 9 ? String.valueOf(n) : (Zero + n);
    }     
}
