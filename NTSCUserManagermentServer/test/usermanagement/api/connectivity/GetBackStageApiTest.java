/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

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
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListStringRespond;

/**
 *
 * @author duyetpt
 */
public class GetBackStageApiTest {

    ListStringRespond respond;

    public GetBackStageApiTest() {
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
    public void initData(int skip, int take) throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registMale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.put(ParamKey.SKIP, new Long(skip));
        All.put(ParamKey.TAKE, new Long(take));

        All.api = APIManager.getApi(API.LIST_BACKSTAGE);
    }

    @Test
    public void test_lockFeature() throws IOException {
        initData(0, 1);
        respond = (ListStringRespond) All.execute();
        assertEquals("ok", ErrorCode.LOCK_FEATURE, respond.code);
    }

    @Test
    public void test() throws IOException, EazyException {
        initData(0, 1);
        UnlockDAO.addUnlockBackstage(Actor.userId, All.getTime().getTime(), Actor.partnerId);
        respond = (ListStringRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List list = respond.data;
        assertEquals("ok", 0, list.size());
    }

    @Test
    public void test1() throws IOException, EazyException {
        initData(0, 1);
        UnlockDAO.addUnlockBackstage(Actor.userId, All.getTime().getTime(), Actor.partnerId);
        BackstageDAO.addBackStage(Actor.partnerId, "image_if", Constant.FLAG.ON);
        respond = (ListStringRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List list = respond.data;

        assertEquals("ok", 1, list.size());
    }
}
