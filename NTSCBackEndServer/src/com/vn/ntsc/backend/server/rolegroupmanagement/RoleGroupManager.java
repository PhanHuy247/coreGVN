/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.rolegroupmanagement;

import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import java.util.List;
import java.util.TreeMap;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class RoleGroupManager {
    public static TreeMap<String, List<String>> m = new TreeMap<String, List<String>>();

    static{
        try{
            m = RoleGroupDAO.getAll();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }
    public static List<String> getListGroup( String role ) {
        return m.get(role);
    }

    public static void add( String role, List<String> set  ) {
       m.put(role, set);
    }

    public static void remove (String role){
            m.remove(role);
    }
    
    public static boolean checkRole (String group, String role){
        boolean result = false;
        List<String> groups = m.get(role);
        if(groups != null && groups.contains(group))
            result = true;
        return result;
    }
    
    public static void reset(){
        m.clear();
        try{
            m = RoleGroupDAO.getAll();
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }
}
