/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author Admin
 */
public class UpdateLocationTest {

    Respond respond;

    public UpdateLocationTest() {

    }

    @BeforeMethod
    public void setUp() {
        All.api = APIManager.getApi(API.UPDATE_LOCATION);
    }

    @AfterMethod
    public void tearDown() {
        if (Actor.userId != null) {
            Userdb.removeUserById(Actor.userId);
        }
    }

    @Test
    public void testFemale() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.obj.put(ParamKey.LATITUDE, 100.2);
        All.obj.put(ParamKey.LONGITUDE, 100.3);
        All.obj.put(ParamKey.USER_ID, Actor.userId);
        respond = All.execute();
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
    }

    @Test
    public void testMale() throws IOException {
        Actor.userId = Userdb.registMale();
        All.obj.put(ParamKey.LATITUDE, 100.2);
        All.obj.put(ParamKey.LONGITUDE, 100.3);
        System.out.println("userId:" + Actor.userId);
        All.obj.put(ParamKey.USER_ID, Actor.userId);
        respond = All.execute();
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
    }

    @Test()
    public void test_OnlyLat() throws IOException {
        Actor.userId = Userdb.registMale();
        All.obj.put(ParamKey.LATITUDE, 100.2);
        All.obj.put(ParamKey.USER_ID, Actor.userId);
        respond = All.execute();
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
    }

}
