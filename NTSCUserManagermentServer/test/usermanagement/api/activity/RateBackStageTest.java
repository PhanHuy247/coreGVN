/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class RateBackStageTest {

    EntityRespond respond;

    public RateBackStageTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("UserID :" + Actor.userId);
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int point) throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.put("rate_point", (long) point);
        All.api = APIManager.getApi(API.RATE_BACKSTAGE);
    }

    @Test
    public void test_notUnlockFeture() throws IOException {
        initData(10);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.LOCK_FEATURE, respond.code);
    }

    @Test
    public void test_blockUser() throws IOException, EazyException {
        initData(10);
        BlockDAO.addBlockList(Actor.userId, Actor.partnerId);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.BLOCK_USER, respond.code);
    }

    @Test
    public void test_UserNotExist() throws IOException, EazyException {
        initData(10);
        All.put(ParamKey.REQUEST_USER_ID, "53c740284b0ab4a31118827d");
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.USER_NOT_EXIST, respond.code);
    }

    @Test
    public void test() throws IOException, EazyException {
        initData(10);
//        UnlockDAO.addUnlock(Actor.partnerId, Userdb.getTime().getTime() + Long.valueOf(24 * 60 * 60 * 60), Actor.userId);
        UnlockDAO.addUnlockBackstage(Actor.userId, All.getTime().getTime() + (long) (24 * 60 * 60 * 60), Actor.partnerId);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }
}
