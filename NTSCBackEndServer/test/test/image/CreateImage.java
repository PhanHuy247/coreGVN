/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.image;

import eazycommon.constant.Constant;
import com.vn.ntsc.backend.entity.impl.image.Image;

/**
 *
 * @author duyetpt
 */
public class CreateImage {
    
    public static Image createImage(){
        Image image = new Image();
        image.appFlag = Constant.FLAG.ON;
        image.appearFlag = Constant.FLAG.ON;
        image.avatarFlag = Constant.FLAG.ON;
        image.flag = Constant.FLAG.ON;
        image.imageId = "";
        image.imageStatus = Constant.FLAG.ON;
        image.imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC;
        image.reportFlag = Constant.FLAG.OFF;
        image.userId = "user_id";
        image.username = "userName";
        
        return image;
    }
}
