/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vn.ntsc.blacklist.DeactivateUserManager;

/**
 *
 * @author Rua
 */
public class Validator {
    
    private static final String ID_FORMAT = "^[a-f\\d]{24}$";
    private static final Pattern idPattern = Pattern.compile(ID_FORMAT);
    
    public static boolean isUser(String id) {
        if (!isValidParameter(id)) {
            return false;
        }
        Matcher matcher = idPattern.matcher(id);
        return matcher.matches() && !DeactivateUserManager.isDeactivateUser(id);
    }
     public static boolean isUser2(String id) {
        if (!isValidParameter(id)) {
            return false;
        }
        Matcher matcher = idPattern.matcher(id);
        return matcher.matches();
    }
    
    public static boolean isValidParameter(String param) {
        return ((param != null) && (!param.equals("")));
    }
    
}
