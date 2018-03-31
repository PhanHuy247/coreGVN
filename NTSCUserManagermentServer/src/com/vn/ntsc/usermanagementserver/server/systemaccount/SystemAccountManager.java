/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.systemaccount;

import java.util.ArrayList;
import java.util.List;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;

/**
 *
 * @author Rua
 */
public class SystemAccountManager {
    private static final List<String> systemAccounts = new ArrayList<>();

    public static void init() {
        try {
            List<String> list = UserDAO.getAllSystemAcc();
            systemAccounts.addAll(list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static List<String> toList() {
        return systemAccounts;
    }    
    
}
