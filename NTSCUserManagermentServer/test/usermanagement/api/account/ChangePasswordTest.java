/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.ChangePasswordDataPro;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;


/**
 *
 * @author duyetpt
 */
public class ChangePasswordTest {

    Respond respond;

    public ChangePasswordTest() {
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
    public void initData(String pass, String oldPass) throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        Userdb.updateEmailFlag(Actor.userId, 1);
        Userdb.updateFinishFlag(Actor.userId, 1);
        Userdb.updateFlag(Actor.userId, 1);
        Userdb.updateVerificationFlag(Actor.userId, 1);
        All.put(ParamKey.USER_ID, Actor.userId);
        if (oldPass == null) {
            All.put(ParamKey.OLD_PASSWORD, Userdb.getPass(Actor.userId));
        } else {
            All.put(ParamKey.OLD_PASSWORD, oldPass);
        }
        All.put(ParamKey.NEW_PASSWORD, pass);
        All.put(ParamKey.ORIGINAL_PASSWORD, "original_pass");

        All.api = APIManager.getApi(API.CHANGE_PASSWORD);
    }

    @Test(dataProviderClass = ChangePasswordDataPro.class, dataProvider = "data")
    public void test_succ(String pass, String oldPass, int errorCode, String flagKey, Integer flag) throws IOException, EazyException {
        initData(pass, oldPass);
        if (errorCode == ErrorCode.LOCKED_USER) {
            Userdb.updateFlagField(Actor.userId, flag, flagKey);
        }
        respond = All.execute();
        assertEquals("ok", errorCode, respond.code);
    }
}
