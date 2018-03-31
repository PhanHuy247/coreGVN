/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import data.provider.ChangePasswordDataPro;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.settingdb.AdminDB;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

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
        AdminDB.remove(Actor.userId);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(String registPass, String oldPass, String newPass) {
        Actor.userId = AdminDB.registAdmin("admin@gmail.com", registPass, "adminntq", 1,1, "admin_role_id");
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.OLD_PASSWORD, oldPass);
        All.put(ParamKey.NEW_PASSWORD, newPass);

        All.api = APIManager.getToolApi(API.CHAGE_PASSWORD_ADMIN);
    }

    @Test(dataProviderClass = ChangePasswordDataPro.class, dataProvider = ChangePasswordDataPro.DATA_NAME)
    public void test_succ(String registPass, String oldPass, String newPass, int errorCode) {
        initData(registPass, oldPass, newPass);
        respond = All.execute();
        assertEquals("ok", errorCode, respond.code);
        if (errorCode == ErrorCode.SUCCESS) {
            assertEquals("ok", true, AdminDB.comparePass(newPass, Actor.userId));
        }else{
            assertEquals("ok", true, AdminDB.comparePass(registPass, Actor.userId));
        }
    }
}
