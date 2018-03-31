/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.csv;

import eazycommon.constant.FilesAndFolders;
import eazycommon.util.Util;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author RuAc0n
 */
public class HeaderCreator {

    private static void addList(String str, int key, List<String> japaneseHeader, List<String>englishHeader ) throws UnsupportedEncodingException {
        if(key == 0)
            CSVUtils.addList(str, japaneseHeader);
        else
            CSVUtils.addList(str, englishHeader);
    }    
    
    public static void createHeader(List<String> japaneseHeader, List<String>englishHeader, List<String> keys ){

//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
        try{
            String header;
            for(int i = 0; i < FilesAndFolders.FILES.LIST_HEADER_FILE.size(); i++){
                String file = FilesAndFolders.FILES.LIST_HEADER_FILE.get(i);
                FileInputStream fis = new FileInputStream( file );
                Properties prop = new Properties();
                prop.load( fis );
                
                for (String key : keys) {
                    Util.addDebugLog("createHeader ====================== KEY : " + key);
                    header = prop.getProperty(key);
                    addList(header, i, japaneseHeader, englishHeader);                    
                }
               
            }
        } catch( Exception ex ) {
            eazycommon.util.Util.addErrorLog(ex);            
        }
      
    }    
    
}
