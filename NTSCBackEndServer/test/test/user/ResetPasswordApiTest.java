/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class ResetPasswordApiTest {

    Respond respond;

    public ResetPasswordApiTest() {
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
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.PASSWORD, "12345678");
        All.put(ParamKey.ORIGINAL_PASSWORD, "original_PASSWORD");

        All.api = APIManager.getApi(API.RESET_PASSWORD);
    }

    @Test
    public void test_succ() throws IOException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", Userdb.getObject(Actor.userId, UserdbKey.USER.SIP_PASSWORD), "12345678");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void test_PASSWORDNull() throws IOException {
        initData();
        All.put(ParamKey.PASSWORD, null);
        respond = All.execute();
        assertEquals("ok", ErrorCode.INVALID_PASSWORD, respond.code);
        assertEquals("ok", Userdb.getObject(Actor.userId, UserdbKey.USER.SIP_PASSWORD), "12345678");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void test_originalPwdNull() throws IOException {
        initData();
        All.put(ParamKey.ORIGINAL_PASSWORD, null);
        respond = All.execute();
        assertEquals("ok", ErrorCode.INVALID_PASSWORD, respond.code);
        assertEquals("ok", Userdb.getObject(Actor.userId, UserdbKey.USER.SIP_PASSWORD), "12345678");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void test_originalPwdEmpty() throws IOException {
        initData();
        All.put(ParamKey.ORIGINAL_PASSWORD, "");
        respond = All.execute();
        assertEquals("ok", ErrorCode.INVALID_PASSWORD, respond.code);
        assertEquals("ok", Userdb.getObject(Actor.userId, UserdbKey.USER.SIP_PASSWORD), "12345678");
    }
}
