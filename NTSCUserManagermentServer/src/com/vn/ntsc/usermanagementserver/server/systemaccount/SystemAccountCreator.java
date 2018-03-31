/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.systemaccount;

import eazycommon.util.Util;
import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;

/**
 *
 * @author RuAc0n
 */
public class SystemAccountCreator {

    private static final String password = "123456";
    private static final String birthday = "19901025";
    private static final String regiterDate = "20140708130200";
    private static final int gender = 0;
    private static final int location = 1;
    private static final int auto_location = 0;
    private static final String systemNews = "systemNew@gmail.com";
    private static final String systemSafety = "systemSafety@gmail.com";
    private static final String systemTips = "systemTips@gmail.com";
    private static final String systemadvertise = "systemAdvertise@gmail.com";
    private static final String system = "system@gmail.com";

    
    public static void addAdministratorAccount() {
        try {
            if (!UserDAO.checkSystemAccExist()) {
                 Util.addInfoLog("Start addAdministratorAccount");
                User user = createUser(system, "運営事務局", Constant.SYSTEM_ACCOUNT.SYSTEM);
                UserDAO.insertUser(user);
                
                user = createUser(systemNews, "System News", Constant.SYSTEM_ACCOUNT.SYSTEM_NEWS);
                UserDAO.insertUser(user);

                user = createUser(systemSafety, "System Safety", Constant.SYSTEM_ACCOUNT.SYSTEM_SAFETY);
                UserDAO.insertUser(user);

                user = createUser(systemTips, "System Tips", Constant.SYSTEM_ACCOUNT.SYSTEM_TIPS);
                UserDAO.insertUser(user);

                user = createUser(systemadvertise, "System Advs", Constant.SYSTEM_ACCOUNT.SYSTEM_ADVERTISE);
                UserDAO.insertUser(user);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }

    public static User createUser(String email, String name, int systemAcc) {
        User user = new User();
        user.email = email;
        user.username = name;
        user.sortName = name;
        user.password = password;
        user.birthday = birthday;
        user.gender = new Long(gender);
        user.systemAcc = new Long(systemAcc);
        user.region = new Long(location);
        user.autoRegion = new Long(auto_location);
        user.registerDate = regiterDate;
        user.flag = (long)1;
        user.finishRegisterFlag = (long)1;
        user.userType = (long)Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE;
        user.hobby = "";
        return user;
    }
}
