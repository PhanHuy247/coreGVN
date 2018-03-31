/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.AddPointApi;

/**
 *
 * @author Admin
 */
public class AddPointTest {

    Respond respond;

    public AddPointTest() {
    }
    @BeforeMethod
    public void setUp() {
        All.initPrice();
    }
    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

  

    @Test
    public void test_PointPositive() throws IOException {
        Actor.userId = Userdb.registFemale();
        int pointB = All.getPoint(Actor.userId);
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, Long.valueOf(100));
        All.api = new AddPointApi();

        respond = All.execute();
        int pointA = All.getPoint(Actor.userId);
        assertEquals("Successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("Successfull", pointB + 100, pointA);
    }

    @Test
    public void test_PointNegative() throws IOException {
        Actor.userId = Userdb.registFemale();
        int pointB = All.getPoint(Actor.userId);
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, Long.valueOf(-100));
        All.api = new AddPointApi();

        respond = All.execute();
        int pointA = All.getPoint(Actor.userId);
        assertEquals("Successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("Successfull", 0, pointA);
    }

    @Test
    public void test_PointZero() throws IOException {
          Actor.userId = Userdb.registFemale();
        int pointB = All.getPoint(Actor.userId);
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, Long.valueOf(0));
        All.api = new AddPointApi();

        respond = All.execute();
        int pointA = All.getPoint(Actor.userId);
        assertEquals("Successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("Successfull", pointB , pointA);
    }
}
