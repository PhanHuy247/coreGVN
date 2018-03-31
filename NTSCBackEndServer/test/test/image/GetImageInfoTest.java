/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.image;

import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.userdb.ImageDB;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetImageInfoTest {
     Image image;
     EntityRespond respond;
    public GetImageInfoTest() {
    }
    
    @BeforeMethod
    public void setUp() {
    }
    
    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
        ImageDB.removeImage(image.imageId);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    public void initData() throws IOException {
       
        All.api = APIManager.getApi(API.GET_IMAGE_INFOR);
        
        Actor.userId = Userdb.registMale();
        image = CreateImage.createImage();
        image.userId = Actor.userId;
        image.imageId = All.createId();
        image.appFlag = 1;
        image.appearFlag = 1;
        image.avatarFlag=1 ;
        image.flag = 1;
        image.imageStatus = 1;
        image.imageType = 1;
        image.reportFlag = 0;
        ImageDB.insertImage(image);
        All.put(ParamKey.IMAGE_ID, image.imageId);
    }
    @Test
    public void test() throws IOException{
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Image ima = (Image) respond.data;
       
        System.out.println(ima.toJsonObject().toJSONString());
    }
}
