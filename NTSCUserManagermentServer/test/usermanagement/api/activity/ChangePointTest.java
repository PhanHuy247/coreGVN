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
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ChangPointApi;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;


/**
 *
 * @author Admin
 */
public class ChangePointTest {

    Respond respond;

    public ChangePointTest() {
    }

    @BeforeMethod
    public void setUp() {
        All.initPrice();
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    public void initUser(int point) throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registMale();
        All.put(ParamKey.POINT, Long.valueOf(point));
        All.put(ParamKey.IP, "127.0.0.1:3456");
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.RECIEVER, Actor.partnerId);
    }

    @Test
    public void test_videoCall() throws IOException {
        int point = 100;
        initUser(100);
        All.put(ParamKey.TYPE, Long.valueOf(16));
        int befP = UserInforManager.getPoint(Actor.userId);

        All.api = new ChangPointApi();
        respond = All.execute();

        int afP = All.getPoint(Actor.userId);
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", befP + point, afP);
    }

    @Test
    public void test_voiceCall() throws IOException {
        int point = 100;
        initUser(100);
        All.put(ParamKey.TYPE, Long.valueOf(15));
        int befP = UserInforManager.getPoint(Actor.userId);

        All.api = new ChangPointApi();
        respond = All.execute();

        int afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", befP + point, afP);
    }

    @Test
    public void test_tradeToMoney_notEnough() throws IOException {
        int point = 100;
        initUser(100);
        All.put(ParamKey.TYPE, Long.valueOf(38));
        int befP = UserInforManager.getPoint(Actor.userId);

        All.api = new ChangPointApi();
        respond = All.execute();

        int afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("successfull", ErrorCode.NOT_ENOUGHT_POINT, respond.code);
        assertEquals("ok", befP, afP);
    }

    @Test
    public void test_tradeToMoney_enough() throws IOException {
        int point = 100;
        initUser(100);
        All.put(ParamKey.TYPE, Long.valueOf(38));
        UserInforManager.get(Actor.userId).point = 1000;
        int befP = UserInforManager.getPoint(Actor.userId);

        All.api = new ChangPointApi();
        respond = All.execute();

        int afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", befP - point, afP);
    }
}
