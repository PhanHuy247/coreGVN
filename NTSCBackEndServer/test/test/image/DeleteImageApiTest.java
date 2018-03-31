/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.image;

import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.userdb.BackStageDB;
import test.db.userdb.BuzzDetailDB;
import test.db.userdb.ImageDB;
import test.db.userdb.PbImageDB;
import test.db.userdb.UserBuzzDB;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class DeleteImageApiTest {
    
    Respond respond;
    Image image;
    
    public DeleteImageApiTest() {
    }
    
    @BeforeMethod
    public void setup() {
        
    }
    
    @AfterMethod
    public void teardown() {
        Actor.removeUser();
        ImageDB.removeImage(image.imageId);
    }
    
    public void initData() throws IOException {
        All.api = APIManager.getApi(API.DELETE_IMAGE);
        
        Actor.userId = Userdb.registMale();
        image = CreateImage.createImage();
        image.userId = Actor.userId;
        image.imageId = All.createId();
        
        All.put(ParamKey.IMAGE_ID, image.imageId);
    }
    
    @Test
    public void test_flag_0() throws IOException, EazyException {
        initData();
        image.flag = 0;
        ImageDB.insertImage(image);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }
    
    @Test
    public void test_BachStage() throws IOException {
        initData();
        System.out.println("userId :" + Actor.userId);
        image.imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE;
        image.flag = 1;
        BackStageDB.addBackState(Actor.userId, image.imageId);
        ImageDB.insertImage(image);
        Userdb.updateNumberOf(Actor.userId, UserdbKey.USER.BACKSTAGE_NUMBER, 1);
        
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Integer backNum = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.BACKSTAGE_NUMBER);
        assertEquals("ok", new Integer(0), backNum);
        assertEquals("ok", new Integer(0), BackStageDB.getFlag(Actor.userId, image.imageId));
        BackStageDB.removeBackStage(Actor.userId);
    }
    
    @Test
    public void test_publicImage_notExist() throws IOException {
        initData();
        image.imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC;
        image.flag = 1;
        ImageDB.insertImage(image);
        All.put(ParamKey.IMAGE_ID, "sjfjflkjfsjflk");
        respond = All.execute();
        assertEquals("ok", ErrorCode.UNKNOWN_ERROR, respond.code);
    }
    
    @Test
    public void test_publicImage_Exist() throws IOException, EazyException {
        initData();
        image.imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC;
        image.flag = 1;
        ImageDB.insertImage(image);
        String buzzId = BuzzDetailDB.addBuzz(Actor.userId, "image", Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ, System.currentTimeMillis(), 1, "12345689");
        PbImageDB.addPublicImage(Actor.userId, image.imageId, buzzId, 1);
        UserBuzzDB.addUserBuzz(Actor.userId, buzzId, 1);
        
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", new Integer(0), PbImageDB.getFlag(Actor.userId, image.imageId));
        assertEquals("ok", new Integer(-1), BuzzDetailDB.getFlag(buzzId));
        assertEquals("ok", new Integer(-1), UserBuzzDB.getFag(Actor.userId, buzzId));
        
        PbImageDB.removeById(Actor.userId);
        BuzzDetailDB.remove(buzzId);
        UserBuzzDB.remove(Actor.userId);
    }
}
