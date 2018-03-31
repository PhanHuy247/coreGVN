/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import data.provider.RegistAdminDataPro;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class RegistAdminApiTest {

    EntityRespond respond;

    public RegistAdminApiTest() {
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
    public void initData(String email, String pass, String name, String role_id, Long flag) {
        All.put(ParamKey.EMAIL, email);
        All.put(ParamKey.PASSWORD, pass);
        All.put(ParamKey.NAME, name);
        All.put(ParamKey.ROLE_ID, role_id);
        All.put(ParamKey.FLAG, flag);

        All.api = APIManager.getApi(API.REGISTER_ADMIN);
    }

    @Test(dataProviderClass = RegistAdminDataPro.class, dataProvider = RegistAdminDataPro.DATA_NAME)
    public void test_succ(String email, String pass, String name, String role_id, Long flag, int errorCode) {
        initData(email, pass, name, role_id, flag);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", errorCode, respond.code);
        if (errorCode == ErrorCode.SUCCESS) {
            InsertData data = (InsertData) respond.data;
            Actor.userId = data.id;
        }
    }
}
