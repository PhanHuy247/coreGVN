/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;


/**
 *
 * @author duyetpt
 */
public class DeactiveTest {

    public DeactiveTest() {
    }

   @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() throws IOException {
        Actor.removeUser();
    }

    public void initData() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put("cmt", "good bye !");

        All.api = APIManager.getApi(API.DEACTIVATE);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void test() throws IOException {
        Respond respond = new Respond();
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Integer flag = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.FLAG);
        assertEquals("ok", new Integer(Constant.FLAG.OFF), flag);
    }
}
