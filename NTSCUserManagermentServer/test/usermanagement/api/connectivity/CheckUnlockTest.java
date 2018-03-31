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
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckUnlockBackstageData;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class CheckUnlockTest {

    EntityRespond respond;

    public CheckUnlockTest() {
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
        Actor.partnerId = Userdb.registFemale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.api = APIManager.getApi(API.CHECK_UNLOCK);
    }

    @Test
    public void test_Lock() throws IOException {
        initData();

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        CheckUnlockBackstageData data = (CheckUnlockBackstageData) respond.data;
        assertEquals("ok", new Long(Constant.FLAG.OFF), data.is_unlck);
    }

    @Test
    public void test_Unlock() throws IOException, EazyException {
        initData();
        String id = UnlockDAO.addUnlockBackstage(Actor.userId, All.getTime().getTime(), Actor.partnerId);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        CheckUnlockBackstageData data = (CheckUnlockBackstageData) respond.data;
        assertEquals("ok", new Long(Constant.FLAG.ON), data.is_unlck);
        UnlockDAO.removeUnlock(id);
    }

    @Test
    public void test_User_not_exist() throws IOException, EazyException {
        initData();
        All.put(ParamKey.REQUEST_USER_ID, "53b52d4fc4e44a16508f3a26");
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.USER_NOT_EXIST, respond.code);
    }

    @Test
    public void test_User_Null() throws IOException, EazyException {
        initData();
        All.put(ParamKey.REQUEST_USER_ID, "");
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }
}
