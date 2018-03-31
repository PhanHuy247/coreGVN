/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.OnlineAlert;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetOnlineAlertTest {

    EntityRespond result;

    public GetOnlineAlertTest() {
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

        All.api = APIManager.getApi(API.GET_ONLINE_ALERT);
    }

    @Test
    public void test_notAlert() throws IOException {
        initData();
        result = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, result.code);
        OnlineAlert data = (OnlineAlert) result.data;
        assertEquals("ok", null, data.alertNumber);
    }

    @Test
    public void test_Alert() throws IOException, EazyException {
        initData();
        OnlineAlertDAO.addAlert(Actor.partnerId, Actor.userId, 0, 0);
        result = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, result.code);
        OnlineAlert data = (OnlineAlert) result.data;
        assertEquals("ok", new Integer(0), data.alertNumber);
//        assertEquals("ok", new Integer(0), data.alertType);

        OnlineAlertDAO.removeAlert(Actor.partnerId, Actor.userId);
    }
}
