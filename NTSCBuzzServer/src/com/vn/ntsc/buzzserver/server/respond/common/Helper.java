/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.server.respond.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import eazycommon.util.Util;
import com.vn.ntsc.buzzserver.Config;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.UserInteractionDAO;

/**
 *
 * @author RuAc0n
 */
public class Helper {
    
    public static String sendPostRequest(String inputString) {
        String result;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + Config.MAIN_IP + ":" + Config.MAIN_PORT + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(inputString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);                
               
                return null;
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            return null;
        }
        return result;

    }    

    public static void removeBlockUserId(List<String> list, List<String> blockList) {
//        for (String id : deactiveList) {
//            if (list.contains(id)) {
//                list.remove(id);
//            }
//        }
//        
//        for (String id : blockList) {
//            if (list.contains(id)) {
//                list.remove(id);
//            }
//        }
        List<String> removeList = new ArrayList<>();
        for (String id : list) {
            if (BlackListManager.isDeactivateUser(id) || blockList.contains(id)) {
                removeList.add(id);
            }
        }
        
        list.removeAll(removeList);
        
    }
    
}
