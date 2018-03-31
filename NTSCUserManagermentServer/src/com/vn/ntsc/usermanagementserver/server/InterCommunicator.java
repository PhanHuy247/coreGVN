/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class InterCommunicator {

    public static String sendRequest(String requestString, String serverIP, int serverPort) {
        String result = null;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);                 
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return result;

    }
    
    public static String sendRequestToAdjust(String requestString, String serverIP) {
        String result = null;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "https://" + serverIP;
            Util.addInfoLog("=========urlStr link=========="+urlStr);
            URL u = new URL(urlStr);
            Util.addInfoLog("=========URL=========="+u);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
            String encodedData = postData.toString();
            Util.addDebugLog("sendRequestToAdjust " + encodedData);
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);                 
            }
            //get data from server
            Util.addInfoLog("=========URL 111111=========="+u);
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            Util.addInfoLog("=========URL 222222=========="+u);
            BufferedReader buf = new BufferedReader(isr);
            Util.addInfoLog("=========URL 33333=========="+u);
            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return result;

    }

    public static byte[] sendCSVRequest(String requestString, String serverIP, int serverPort) {
        byte[] result = null;
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
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
            }
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (in == null) {
                return null;
            }
            byte[] buffer = new byte[1];
            while ((in.read(buffer, 0, 1)) != -1) {
                bos.write(buffer);
            }
            bos.flush();
            bos.close();
            result = bos.toByteArray();

        } catch (Exception ex) {
            Util.addErrorLog(ex);             
        }
        return result;

    }

}
