/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetPointApi;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author Admin
 */
public class GetPointTest {

    EntityRespond respond;

    public GetPointTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    @Test
    public void test() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = new GetPointApi();
        respond = (EntityRespond) All.execute();

        Point pointObj = (Point) respond.data;
        assertEquals("OK", ErrorCode.SUCCESS, respond.code);
        Integer p = UserInforManager.getPoint(Actor.userId);
        assertEquals("OK", p, pointObj.point);
    }

}
