/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import data.provider.RegistAdminDataPro;
import java.util.Scanner;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.settingdb.AdminDB;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class UpdateAdminApiTest {

    Respond respond;

    public UpdateAdminApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        AdminDB.remove(Actor.userId);
    }


    public void initData(String email, String pass, String name, String role_id, Long flag) {
        Actor.userId = AdminDB.registAdmin("email", "pwd", "admind", 1, 0, "role_id");
        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.EMAIL, email);
        All.put(ParamKey.PASSWORD, pass);
        All.put(ParamKey.NAME, name);
        All.put(ParamKey.ROLE_ID, role_id);
        All.put(ParamKey.FLAG, flag);

        All.api = APIManager.getApi(API.UPDATE_ADMIN);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test(dataProviderClass = RegistAdminDataPro.class, dataProvider = RegistAdminDataPro.DATA_UPDATE)
    public void test_succ(String email, String pass, String name, String role_id, Long flag, int errorCode) {
        initData(email, pass, name, role_id, flag);
        String id = null;
        if (errorCode == ErrorCode.EMAIL_REGISTED) {
            System.out.println("regist admin");
            id = AdminDB.registAdmin(email.toLowerCase(), "pwd", name, 1, 0, role_id);
        }
        respond = All.execute();
        assertEquals("ok", errorCode, respond.code);
        if (id != null) {
            AdminDB.remove(id);
        }
    }
}
