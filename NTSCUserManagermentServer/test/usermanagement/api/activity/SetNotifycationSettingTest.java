/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.NotificationSettingDB;
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.testng.AssertJUnit;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.NotificationSetting;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class SetNotifycationSettingTest {

    Respond respond;

    public SetNotifycationSettingTest() {
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
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put("andg_alt", (long) Constant.FLAG.ON);
        All.put("chat", (long) Constant.FLAG.ON);
        All.put("noti_buzz", (long) Constant.FLAG.OFF);
        All.put("noti_chk_out", (long) Constant.FLAG.OFF);
        All.api = APIManager.getApi(API.NOTIFICATION_SETTING);
    }

    @Test
    public void test() throws IOException, EazyException {
        initData();
        respond = All.execute();
        AssertJUnit.assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        NotificationSetting set = NotificationSettingDAO.getNotificationSetting(Actor.userId);
        Integer yes = Constant.FLAG.ON;
        Integer no = Constant.FLAG.OFF;
        assertEquals(set.andgAlert, yes);
        assertEquals(set.chat, yes);
        assertEquals(set.notiCheckOut, no);
        assertEquals(set.notiBuzz, no);
        NotificationSettingDB.remove(Actor.userId);
    }
}
