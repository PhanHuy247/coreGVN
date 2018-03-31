/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.buzz;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class LikeBuzzApiTest {

    Respond respond;

    public LikeBuzzApiTest() {
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
        All.put(ParamKey.BUZZ_OWNER_ID, Actor.partnerId);
        All.put(ParamKey.TIME, All.getTime().getTime());
        All.put(ParamKey.BUZZ_ID, "buzz_id");
        All.put(ParamKey.IP, "127.0.0.1:98");
        All.put(ParamKey.IS_NOTI, new Long(Constant.FLAG.ON));
        All.api = APIManager.getApi(API.LIKE_BUZZ);
    }

    @Test
    public void test_FF() throws IOException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }
}
