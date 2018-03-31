/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.testng.Assert;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.BuyStickerApi;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInfor;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author Admin
 */
public class BuyStickerTest {

    Respond respond;

    public BuyStickerTest() {
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
    public void test_NotEnoughPoint() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, Long.valueOf(12));
        All.put(ParamKey.IP, "127.0.0.1:8239");

        All.api = new BuyStickerApi();
        respond = All.execute();
        Assert.assertEquals(ErrorCode.NOT_ENOUGHT_POINT, respond.code);
    }

    @Test
    public void test_EnoughPoint() throws IOException {
        Actor.userId = Userdb.registFemale();
        UserInforManager.userInforMap.put(Actor.userId, new UserInfor(Actor.userId, 100, Constant.GENDER.FEMALE, false));
        Integer befP = UserInforManager.getPoint(Actor.userId);
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.POINT, Long.valueOf(12));
        All.put(ParamKey.IP, "127.0.0.1:8239");

        All.api = new BuyStickerApi();
        respond = All.execute();
        Assert.assertEquals(ErrorCode.SUCCESS, respond.code);

        Integer afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("ok", new Integer(befP - 12), afP);
    }
}
