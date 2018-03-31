/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.common;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.FilesAndFolders;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class FileNameCreator {

    private static final Map<String, String> map = new TreeMap<>();
    static{
        map.put(API.SEARCH_USER, "User List");
        map.put(API.SEARCH_LOG_BLOCK, "Block Log");
        map.put(API.SEARCH_REPORT_IMAGE, "Report Image Log");
        map.put(API.SEARCH_REPORT_USER, "Report User Log");
        map.put(API.SEARCH_LOG_ONLINE_ALERT, "Online Alert Log");
        map.put(API.SEARCH_LOG_SHAKE_CHAT, "Shake Chat Log");
        map.put(API.SEARCH_LOG_FAVOURIST, "Favorites Log");
        map.put(API.SEARCH_LOG_CHECK_OUT, "Checkout Profile Log");
        map.put(API.SEARCH_LOG_WINK, "Wink Log");
        map.put(API.SEARCH_LOG_CALL, "Voice Video Call Log");
        map.put(API.SEARCH_LOG_LOGIN, "Login Log");
        map.put(API.SEARCH_LOG_DEACTIVATE, "Deactivate Log");
        map.put(API.SEARCH_LOG_LOOK, "Look At Me Log");
        map.put(API.SEARCH_LOG_POINT, "Point Log");
        map.put(API.SEARCH_LOG_WINK_BOMB, "Wink Bomb Log");
        map.put(API.SEARCH_LOG_NOTIFICATION, "Notification Log");
        map.put(API.SEARCH_LOG_CHAT, "Chat Log");
        map.put(API.SEARCH_LOG_BUZZ, "Buzz Log");
        map.put(API.SEARCH_LOG_PURCHASE, "Purchase Log");
    }

    private static Date getDate(int timezone) throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.setTime(Util.getGMTTime());
        cal.add(Calendar.HOUR, timezone);
        return cal.getTime();
    }
    
    public static String getFileName(int timezone, String api) throws Exception{
        StringBuilder sb = new StringBuilder();
        Date d = getDate(timezone);
        String dateTime = DateFormat.formatCsvName(d);
        if(map.get(api) != null){
            sb.append(map.get(api));
            sb.append("-");
        }
        sb.append(dateTime);
        sb.append(FilesAndFolders.EXTENSIONS.CSV_FILE_EXTENSION);
        return sb.toString();
    }
}
