/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.rolegroupmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import com.vn.ntsc.backend.dao.screen.ScreenAPIDAO;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ScreenAPIManager {
    public static TreeMap<String, List<String>> m = new TreeMap<String, List<String>>();

    static{
        try{
            m = ScreenAPIDAO.getAll();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }
    public static List<String> getScreen( String api ) {
        return m.get( api );
    }

    public static void addAPI( String api, String screen  ) {
        List<String> listScreen = m.get(api);
        if(listScreen == null)
            listScreen = new ArrayList<String>();
        listScreen.add(screen);
        m.put(api, listScreen);
    }

    public static void removeAPI (String api, String screen){
        List<String> listScreen = m.get(api);
        if(listScreen != null)
            listScreen.remove(screen);
        m.put(api, listScreen);
    }
    
    public static void reset(){
        m.clear();
        try{
            m = ScreenAPIDAO.getAll();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }        
    }
}
