/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.ImageDB;
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class removeBackStageTest {

    Respond respond;

    public removeBackStageTest() {
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
    public void initData() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.REMOVE_BACKSTAGE);
    }

    @Test
    public void test_imgNotExist() throws IOException {
        initData();
        All.put(ParamKey.IMAGE_ID, "image_id");
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }

    @Test
    public void test() throws EazyException, IOException {
        initData();
        String img_id = ImageDB.insertImage(Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, null);
        BackstageDAO.addBackStage(Actor.userId, img_id, Constant.FLAG.ON);
        All.put(ParamKey.IMAGE_ID, img_id);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        ImageDB.remove(img_id, Actor.userId);
        BackstageDAO.removeBackStage(Actor.userId, img_id);
    }
}
