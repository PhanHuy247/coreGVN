/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.account;

import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.db.settingdb.RoleDB;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class InsertRoleTest {

    String roleNAME;
    Respond respond;

    public InsertRoleTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        RoleDB.removeByName(roleNAME);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(String roleNAME) {
        this.roleNAME = roleNAME;
        All.put(ParamKey.NAME, roleNAME);
        All.put("lst_group", new JSONArray());
        
        All.api = APIManager.getApi(API.INSERT_ROLE);
    }
    
    @Test
    public void test_succ(){
        initData("role_NAME");
        respond = All.execute();
        assertEquals("ok", 0, respond.code);
    }
    
    @Test
    public void test_Name_exist(){
        initData("role_NAME");
        respond = All.execute();
        assertEquals("ok", 0, respond.code);
        
        respond = All.execute();
        assertEquals("ok", 75, respond.code);
    }
    
    @Test
    public void test_groupList_null(){
        initData("role_NAME");
        All.put("lst_group", null);
        respond = All.execute();
        assertEquals("ok", 0, respond.code);
    }
}
