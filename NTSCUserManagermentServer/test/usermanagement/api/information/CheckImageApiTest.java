/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.api.information;

import DAOTest.ImageDB;
import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class CheckImageApiTest {
    Respond respond;
    String imageId;
    public CheckImageApiTest() {
    }
    
    @BeforeMethod
    public void setUp() {
    }
    
    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    public void initData() throws IOException, EazyException{
        Actor.userId = Userdb.registMale();
        imageId = ImageDB.insertImage(Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Actor.userId);
        
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.IMAGE_ID, imageId);
        All.api = APIManager.getApi(API.CHECK_IMAGE);
    }
    
    @Test
    public void test_suscc() throws IOException, EazyException{
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }
}
