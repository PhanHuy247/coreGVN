/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import java.io.IOException;
import eazycommon.constant.API;
import test.Actor;
import test.db.settingdb.AdminDB;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class LoginTest {

    Respond respond;

    public LoginTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        AdminDB.remove(Actor.userId);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData() throws IOException {
        String EMAIL = "abcd@gmail.com";
        String pass = "1234567890";
        Actor.userId = AdminDB.registAdmin(EMAIL, pass, "admin", 1, 1, "role1");
        All.put(ParamKey.EMAIL, EMAIL);
        All.put(ParamKey.PASSWORD, pass);

        All.api = APIManager.getToolApi(API.LOGIN_ADMINISTRATOR);
    }

    @Test
    public void test_succ() throws IOException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }

    @Test
    public void test_EmailNotFound() throws IOException {
        initData();
        All.put(ParamKey.EMAIL, "abc@gmail.com");
        respond = All.execute();
        assertEquals("ok", ErrorCode.EMAIL_NOT_FOUND, respond.code);
    }

    @Test
    public void test_IncorrectPwd() throws IOException {
        initData();
        All.put(ParamKey.PASSWORD, "abc@gmail.com");
        respond = All.execute();
        assertEquals("ok", ErrorCode.INCORRECT_PASSWORD, respond.code);
    }
}
