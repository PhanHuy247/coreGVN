/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server;

import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.dao.impl.ThumbnailDAO;
import com.vn.ntsc.jpns.dao.impl.UserDAO;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;

/**
 *
 * @author hoangnh
 */
public class Helper {
    
    public static Integer getGender(String fromUserid) throws EazyException{
        Integer gender = UserDAO.getGender(fromUserid);
        return gender;
    }
}
