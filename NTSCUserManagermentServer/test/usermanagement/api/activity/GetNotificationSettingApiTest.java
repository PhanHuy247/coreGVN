/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
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
import org.testng.annotations.Test;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.GetNotificationSettingData;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetNotificationSettingApiTest {

    EntityRespond respond;

    public GetNotificationSettingApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
        respond = null;
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void test() throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        NotificationSettingDAO.addNotificationSetting(Actor.userId, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.OFF);

        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_NOTIFICATION_SETTING);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetNotificationSettingData data = (GetNotificationSettingData) respond.data;
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.andgAlert);
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.chat);
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.notiBuzz);
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.notiCheckOut);

    }

    @Test
    public void test_null() throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_NOTIFICATION_SETTING);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetNotificationSettingData data = (GetNotificationSettingData) respond.data;
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.andgAlert);
        assertEquals("ok", new Integer(Constant.FLAG.OFF), data.setting.chat);
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.notiBuzz);
        assertEquals("ok", new Integer(Constant.FLAG.ON), data.setting.notiCheckOut);

    }
}
