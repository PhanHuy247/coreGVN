/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server;

import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.dao.screen.ScreenGroupDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.screen.ScreenGroup;

/**
 *
 * @author RuAc0n
 */
public class DatabaseCreator {

    private static final String password = "123456789";
    private static final String email = "administrator";
    private static final String name = "Administrator";
    private static final String roleName = "Super Admin";
    
    public static void createSupperAdminAccount (){
        try{
            if(!AdminDAO.collectionExists()){
                String roleId = RoleDAO.insert(roleName);
                AdminDAO.insert(email, Util.encodeString(password), password, roleId, name, Constant.FLAG.ON);
                RoleDAO.addSpecialFlag(roleName, Constant.FLAG.ON);
                AdminDAO.addSpecialFlag(email, Constant.FLAG.ON);
                List<IEntity> groups = ScreenGroupDAO.getAll(null);
                for(IEntity entity: groups){
                    ScreenGroup group = (ScreenGroup) entity;
                    RoleGroupDAO.update(group.id, roleId);
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
    }
    
}
