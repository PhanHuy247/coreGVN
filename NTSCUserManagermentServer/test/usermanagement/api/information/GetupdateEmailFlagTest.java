/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.information;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUpdateInfoFlagsData;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetupdateEmailFlagTest {

    public GetupdateEmailFlagTest() {
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
    @Test
    public void test_Female() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_UPDATE_INFO_FLAGS);

        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetUpdateInfoFlagsData data = (GetUpdateInfoFlagsData) respond.data;
        assertEquals("ok", new Integer(0), data.updateEmailFlag);
        assertEquals("ok", new Integer(0), data.finishRegisterFlag);
        assertEquals("ok", new Integer(1), data.verificationFlag);
    }
    
    @Test
    public void test_Male() throws IOException {
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_UPDATE_INFO_FLAGS);

        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetUpdateInfoFlagsData data = (GetUpdateInfoFlagsData) respond.data;
        assertEquals("ok", new Integer(0), data.updateEmailFlag);
        assertEquals("ok", new Integer(0), data.finishRegisterFlag);
        assertNull("ok", data.verificationFlag);
    }
}
