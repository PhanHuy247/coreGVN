/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import DAOTest.userdb.NotificationColl;
import java.io.IOException;
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
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.server.pointaction.Price;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class NotificationLoginTest {

    Respond respond;
    Price price;

    public NotificationLoginTest() {
    }

    @BeforeMethod
    public void setUp() throws EazyException {
        DatabaseLoader.init();
        All.initPrice();
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
    public void initData() throws EazyException {
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.NOTI_LOGIN);
        Userdb.updateUserName(Actor.userId, "duyetpt");
    }

    public void initData_blocked() throws IOException, EazyException {
        Actor.userId = Userdb.registMale();
        initData();
        // LIST BLOCK
        String id = null;
        for (int i = 0; i < 10; i++) {
            id = Userdb.registMale();
            Actor.listBlack.add(id);
            BlockDAO.addBlockList(Actor.userId, id);
            OnlineAlertDAO.addAlert(Actor.userId, id, 0, 1);
            NotificationSettingDAO.addNotificationSetting(id, 1, 1, 1, 1, 1);
        }
        //List noti
        for (int i = 0; i < 10; i++) {
            id = Userdb.registMale();
            Actor.listPartnerId.add(id);
            OnlineAlertDAO.addAlert(Actor.userId, id, 0, 1);
            NotificationSettingDAO.addNotificationSetting(id, 1, 1, 1, 1, 1);
        }
    }

    public void initData_NotEnoughtMoney() throws IOException, EazyException {
        Actor.userId = Userdb.registMale();
        initData();
        // LIST BLOCK
        String id = null;
        for (int i = 0; i < 20; i++) {
            id = Userdb.registMale();
            Actor.listBlack.add(id);
            UserInforManager.get(id).point = 0;
            OnlineAlertDAO.addAlert(Actor.userId, id, 0, 1);
            NotificationSettingDAO.addNotificationSetting(id, 1, 1, 1, 1,1);
        }
        //List not
        for (int i = 0; i < 20; i++) {
            id = Userdb.registMale();
            Actor.listPartnerId.add(id);
            OnlineAlertDAO.addAlert(Actor.userId, id, 0, 1);
            NotificationSettingDAO.addNotificationSetting(id, 1, 1, 1, 1,1);
        }
    }

    @Test
    public void test_Blocked_EnoughtMoney() throws IOException, EazyException {
        initData_blocked();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        // list block
        for (String id : Actor.listBlack) {
            assertEquals("ok", UserInforManager.getPoint(id), 2);
            assertEquals("ok", false, NotificationColl.isNoti(Actor.userId, id));
        }
        // listNot
        for (String id : Actor.listPartnerId) {
            assertEquals("ok", UserInforManager.getPoint(id), 0);
            assertEquals("ok", true, NotificationColl.isNoti(Actor.userId, id));
        }
    }

    @Test
    public void test_BeBlocked_EnoughtMoney() throws IOException, EazyException, InterruptedException {
        initData_NotEnoughtMoney();
        
        long start = System.currentTimeMillis();
        System.out.println("start time :" + start);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        // list block
        for (String id : Actor.listBlack) {
            assertEquals("ok", 0, UserInforManager.getPoint(id));
            assertEquals("ok", false, NotificationColl.isNoti(Actor.userId, id));
        }
        // listNot
        int i = 0;
        for (String id : Actor.listPartnerId) {
            i++;
            assertEquals("ok", UserInforManager.getPoint(id), 0);
            assertEquals("ok :" + i + " ,id :" + id, true, NotificationColl.isNoti(Actor.userId, id.trim()));
        }
    }
}
