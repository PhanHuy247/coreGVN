/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONArray;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.db.settingdb.RoleDB;
import test.db.settingdb.RoleGroupDB;
import com.vn.ntsc.backend.dao.admin.RoleDAO;
import com.vn.ntsc.backend.dao.admin.RoleGroupDAO;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class UpdateRoleTest {

    Respond respond;
    String id;
    String existRole;

    public UpdateRoleTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() throws EazyException {
        RoleGroupDAO.removeByRole(id);
        RoleDAO.remove(id);
        if (existRole != null) {
            RoleGroupDAO.removeByRole(existRole);
            RoleDAO.remove(existRole);
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData() throws EazyException {
        id = RoleDB.insert("abc");

        for (int i = 0; i < 5; i++) {
            RoleGroupDAO.update("group" + i, id);
        }

        All.put(ParamKey.ID, id);
        All.put(ParamKey.NAME, "name");
        JSONArray arr = new JSONArray();
        arr.add("g1");
        arr.add("g2");
        arr.add("g3");
        All.put("lst_group", arr);
        All.api = APIManager.getApi(API.UPDATE_ROLE);
    }

    @Test
    public void test_succ() throws EazyException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List<String> list = RoleGroupDB.getListGroupByRole(id);
        String[] groups = {"g1", "g2", "g3"};
        assertEquals("ok", groups, list.toArray());
    }

    @Test
    public void test_NameExist() throws EazyException {
        initData();
        existRole = RoleDB.insert("abcde");
        All.put(ParamKey.NAME, "abcde");

        respond = All.execute();
        assertEquals("ok", ErrorCode.EXISTS_DATA, respond.code);
    }
}
