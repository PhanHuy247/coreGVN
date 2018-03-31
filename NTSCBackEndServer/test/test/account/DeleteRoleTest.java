/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import test.Actor;
import test.db.settingdb.AdminDB;
import test.db.settingdb.RoleDB;
import eazycommon.exception.EazyException;
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
public class DeleteRoleTest {

    String roleid;
    Respond respond;

    public DeleteRoleTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() throws EazyException {
        RoleDB.remove(roleid);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int count) throws EazyException {
        roleid = RoleDB.insert("admin");
        for (int i = 0; i < count; i++) {
            AdminDB.registAdmin("email", "name", "sajk", 1, 1, roleid);
        }
        Actor.userId = AdminDB.registAdmin("email", "name", "sajk", 1, 1, "LKJKVKVOUOWIURJDLK");
        All.api = APIManager.getApi(API.DELETE_ROLE);
        All.put(ParamKey.ID, roleid);
    }

    @Test
    public void test() throws EazyException {
        initData(10);
        respond = All.execute();
        assertEquals("ok", 0, respond.code);
        assertEquals("ok", 0, AdminDB.countRole(roleid));
    }

}
