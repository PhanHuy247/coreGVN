/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import eazycommon.constant.API;
import test.Actor;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.entity.impl.user.extend.Female;
import com.vn.ntsc.backend.entity.impl.user.extend.Male;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class DetailUserApiTest {

    EntityRespond respond;

    public DetailUserApiTest() {
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
    @Test
    public void test_Female() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.ID, Actor.userId);
        System.out.println("id :" + Actor.userId);
        All.api = APIManager.getApi(API.DETAIL_USER);

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        Female female = (Female) respond.data;
        System.out.println("Female" + female.toJsonObject().toJSONString());
    }

    @Test
    public void test_Male() throws IOException {
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.ID, Actor.userId);
        System.out.println("id :" + Actor.userId);
        All.api = APIManager.getApi(API.DETAIL_USER);

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        Male male = (Male) respond.data;
        System.out.println("Male :" + male.toJsonObject().toJSONString());
    }
}
