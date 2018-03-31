/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.csv;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;

/**
 *
 * @author RuAc0n
 */
public class CSVUtils {

    public static void addList(String str, List<String>list ) throws UnsupportedEncodingException {
        String string = new String(str.getBytes(Constant.ENCODING_VALUE.ISO), Constant.ENCODING_VALUE.UTF);
        list.add(string);
    }    
 
    public static void addMap(int key, String str, Map map ) throws UnsupportedEncodingException {
        String string = new String(str.getBytes(Constant.ENCODING_VALUE.ISO), Constant.ENCODING_VALUE.UTF);
        map.put(key, string);
    }    

    public static void addMap(String key, String str, Map map ) throws UnsupportedEncodingException {
        String string = new String(str.getBytes(Constant.ENCODING_VALUE.ISO), Constant.ENCODING_VALUE.UTF);
        map.put(key, string);
    }    
    
}
