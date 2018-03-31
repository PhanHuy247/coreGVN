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
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class UnlockApiTest {

    Respond respond;

    public UnlockApiTest() {
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
        All.api = APIManager.getApi(API.UNLOCK);
    }

    @Test
    public void test_BackStage_0() throws IOException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.EMPTY_DATA, respond.code);
    }

    @Test
    public void test_BackStage() throws IOException, EazyException {
        initData();
        UserDAO.addBackstage(Actor.partnerId);
        Integer befUP = UserInforManager.getPoint(Actor.userId);
        Integer befPP = UserInforManager.getPoint(Actor.partnerId);

        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Integer afUP = UserInforManager.getPoint(Actor.userId) + ActionManager.get(ActionType.unlock_backstage).femalePrice;
        Integer afPP = UserInforManager.getPoint(Actor.partnerId) - ActionManager.get(ActionType.unlock_backstage).femalePartnerPrice;
        assertEquals("ok", befUP, afUP);
        assertEquals("ok", befPP, afPP);
    }

    @Test
    public void test_BackStage_Unlocked() throws IOException, EazyException {
        initData();
        UserDAO.addBackstage(Actor.partnerId);
        Integer befUP = UserInforManager.getPoint(Actor.userId);
        Integer befPP = UserInforManager.getPoint(Actor.partnerId);
        UnlockDAO.addUnlockBackstage(Actor.userId, All.getTime().getTime() + 10000, Actor.partnerId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Integer afUP = UserInforManager.getPoint(Actor.userId);
        Integer afPP = UserInforManager.getPoint(Actor.partnerId);
        assertEquals("ok", befUP, afUP);
        assertEquals("ok", befPP, afPP);

        UnlockDAO.removeUnlock(Actor.userId);
    }

    @Test
    public void test_BlockFriend() throws IOException, EazyException {
        initData();
        UserDAO.addBackstage(Actor.partnerId);
        Integer befUP = UserInforManager.getPoint(Actor.userId);
        Integer befPP = UserInforManager.getPoint(Actor.partnerId);
        BlockDAO.addBlockList(Actor.userId, Actor.partnerId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.BLOCK_USER, respond.code);
        Integer afUP = UserInforManager.getPoint(Actor.userId);
        Integer afPP = UserInforManager.getPoint(Actor.partnerId);
        assertEquals("ok", befUP, afUP);
        assertEquals("ok", befPP, afPP);

        UnlockDAO.removeUnlock(Actor.userId);
    }
}
