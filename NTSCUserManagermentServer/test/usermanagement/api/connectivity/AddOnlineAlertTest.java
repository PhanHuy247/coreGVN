/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import DAOTest.logdb.LogOnlineAlertDB;
import java.io.IOException;
import java.util.List;
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
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class AddOnlineAlertTest {

    Respond respond;

    public AddOnlineAlertTest() {
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
        Actor.partnerId = Userdb.registMale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.put(ParamKey.IP, "127.0.0.1");
        All.put(ParamKey.ALERT_FREQUENCY, (long)10);
        All.api = APIManager.getApi(API.ADD_ONLINE_ALERT);
    }

    @Test
    public void test_Add() throws EazyException, IOException {
        initData();
        All.put("is_alt", 1L);
        respond = All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List list = OnlineAlertDAO.getAlertList(Actor.userId);
        assertEquals("ok", true, list.contains(Actor.partnerId));
        OnlineAlertDAO.removeAlert(Actor.userId, Actor.partnerId);
        LogOnlineAlertDB.remove(Actor.userId);
    }

    @Test
    public void test_Remove() throws IOException, EazyException {
        initData();
        All.put("is_alt", 0L);
        OnlineAlertDAO.addAlert(Actor.userId, Actor.partnerId, 1000, 1);
        respond = All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }

    @Test
    public void test_Add_Blocked() throws IOException, EazyException {
        initData();
        All.put("is_alt", 1L);
        BlockDAO.addBlockList(Actor.userId, Actor.partnerId);
        respond = All.execute();

        assertEquals("ok", ErrorCode.BLOCK_USER, respond.code);
        List list = OnlineAlertDAO.getAlertList(Actor.userId);
        assertEquals("ok", false, list.contains(Actor.partnerId));
        BlockDAO.removeBlock(Actor.userId, Actor.partnerId);
    }
}
