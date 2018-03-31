/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.ChangePassCaseForgetDataPro;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class ChangePassCaseForgetTestNG {

    Respond respond;

    public ChangePassCaseForgetTestNG() {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        Actor.removeUser();
    }

    public void initData(int error_code, int flag, String key, String emailUp,
            String emailPass, String pass, String verifiCodeSet, String verifiCodePass) throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        Userdb.updateVerificationFlag(Actor.userId, 1);
        Userdb.updateEmailFlag(Actor.userId, 1);
        Userdb.updateFlag(Actor.userId, 1);
        Userdb.updateFinishFlag(Actor.userId, 1);
        //============================================
        if (key != null) {
            Userdb.updateFlagField(Actor.userId, flag, key);
        }
        Userdb.updateEmail(Actor.userId, emailUp);
        All.put(ParamKey.EMAIL, emailPass);
        All.put(ParamKey.NEW_PASSWORD, pass);
        All.put(ParamKey.ORIGINAL_PASSWORD, "original_pass");
        Userdb.setVerificationCode(Actor.userId, verifiCodeSet);
        All.put("vrf_code", verifiCodePass);
        All.put(ParamKey.IP, "localhost");
        All.put(ParamKey.LOGIN_TIME, All.getTime().toString());

        All.api = APIManager.getApi(API.CHANGE_PASS_CASE_FORGOT);
    }

    @Test(dataProviderClass = ChangePassCaseForgetDataPro.class, dataProvider = "data")
    public void test(int error_code, int flag, String key, String emailUp,
            String emailPass, String pass, String verifiCodeSet, String verifiCodePass) throws IOException, EazyException {
        
        initData(error_code, flag, key, emailUp, emailPass, pass, verifiCodeSet, verifiCodePass);
        respond = All.execute();
        assertEquals("ok", error_code, respond.code);
    }
}
