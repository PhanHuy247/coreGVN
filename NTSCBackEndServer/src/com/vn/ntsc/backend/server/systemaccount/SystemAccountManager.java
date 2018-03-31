/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.systemaccount;

import java.util.ArrayList;
import java.util.List;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author Rua
 */
public class SystemAccountManager {
    private static final List<IEntity> systemAccounts = new ArrayList<>();

    static {
        try {
            List<IEntity> list = UserDAO.getAllSystemAcc();
            systemAccounts.addAll(list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static List<IEntity> toList() {
        List<IEntity> result = new ArrayList<>();
        result.addAll(systemAccounts);
        return result;
    }    
    
}
