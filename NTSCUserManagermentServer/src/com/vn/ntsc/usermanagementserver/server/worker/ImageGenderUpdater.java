/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.worker;

import eazycommon.constant.Constant;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;

/**
 *
 * @author Administrator
 */
public class ImageGenderUpdater extends Thread{
    
    public static void startImageGenderUpdater(){
        Util.addDebugLog("Start update image");
        ImageGenderUpdater updater = new ImageGenderUpdater();
        updater.start();
        
    }
    
    @Override
    public void run(){
        try {
            boolean isRun = true;
            while (isRun){
                for (int i = 0; i < 10; i++){
                    Image img = ImageDAO.getImageWithoutGender();
                    if (img != null){
                        Integer gender = (Integer) UserDAO.getUserInfor(img.userId, "gender");
                        ImageDAO.updateImageGender(img.imageId, gender);
                    }
                    else {
                        isRun = false;
                    }
                }
                Thread.sleep(2000);                
            }
        } catch (InterruptedException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
}
