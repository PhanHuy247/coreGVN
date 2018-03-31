/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

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
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class AddPointApiTest {

    Respond respond;

    public AddPointApiTest() {
    }

    @BeforeMethod
    public void setUp() {

    }

    @AfterMethod
    public void after() {
        Actor.removeUser();
    }

    public void initdata(int point) throws IOException {
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, (long) point);

        All.api = APIManager.getApi(API.ADD_POINT);
    }

    @Test
    public void test_IncreasePoint() throws IOException {
        initdata(10);

        int befPoint = Userdb.getPoint(Actor.userId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        int afPoint = Userdb.getPoint(Actor.userId);
        assertEquals("ok", befPoint + 10, afPoint);
    }

    @Test
    public void test_decreasePoint() throws IOException {
        initdata(- 10);

        int befPoint = Userdb.getPoint(Actor.userId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        int afPoint = Userdb.getPoint(Actor.userId);
        assertEquals("ok", befPoint - 10, afPoint);
    }

    @Test
    public void test_decreasePointTo0() throws IOException {
        initdata(- 1000);

        int befPoint = Userdb.getPoint(Actor.userId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        int afPoint = Userdb.getPoint(Actor.userId);
        assertEquals("ok", 0, afPoint);
    }
}
