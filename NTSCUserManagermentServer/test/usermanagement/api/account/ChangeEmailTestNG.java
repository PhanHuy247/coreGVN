/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import usermanagement.provider.ChangeEmailDataProvider;
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class ChangeEmailTestNG {

    Respond respond;
    User user;

    public ChangeEmailTestNG() {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        Actor.removeUser();
    }

    public void initData(String email, String newPass) throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        Userdb.updateFinishFlag(Actor.userId, 1);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.EMAIL, email);
        All.put(ParamKey.NEW_PASSWORD, newPass);
        All.put(ParamKey.ORIGINAL_PASSWORD, "12345678");

        All.api = APIManager.getApi(API.CHANGE_EMAIL);
    }

    @Test(dataProvider = "first", dataProviderClass = ChangeEmailDataProvider.class)
    public void firstChange(String email, String pass, int error_code) throws IOException, EazyException {
        initData(email, pass);
        if (error_code == ErrorCode.EMAIL_REGISTED) {
            Actor.partnerId = Userdb.registMale();
            Userdb.updateEmail(Actor.partnerId, email);
        }
        respond = All.execute();
        user = UserDAO.getUserInfor(Actor.userId);
        doTestFirst(error_code);
    }

    public void doTestFirst(int error_code) {
        assertEquals("ok", error_code, respond.code);
        if (error_code == ErrorCode.SUCCESS) {
            assertEquals("ok", new Long(1), user.updateEmailFlag);
            assertEquals("ok", All.obj.get(ParamKey.EMAIL), user.email);
            assertEquals("ok", All.obj.get(ParamKey.NEW_PASSWORD), Userdb.getPass(Actor.userId));
        } else {
            assertEquals("ok", new Long(0), user.updateEmailFlag);
        }
    }

    @Test(dataProvider = "second", dataProviderClass = ChangeEmailDataProvider.class)
    public void secondChange(String email, String email2, String pass, String oldPass, int error_code1, int error_code2) throws IOException, EazyException {
        initData(email, "123456789");
        respond = All.execute();
        user = UserDAO.getUserInfor(Actor.userId);
        doTestFirst(error_code1);
        //2
        if (error_code2 == ErrorCode.EMAIL_REGISTED) {
            Actor.partnerId = Userdb.registMale();
            Userdb.updateEmail(Actor.partnerId, email2);
        }
        if (oldPass == null) {
            All.put(ParamKey.OLD_PASSWORD, All.obj.get(ParamKey.NEW_PASSWORD));
        } else {
            All.put(ParamKey.OLD_PASSWORD, oldPass);
        }
        All.put(ParamKey.NEW_PASSWORD, pass);
        All.obj.put(ParamKey.EMAIL, email2);
        respond = All.execute();
        user = UserDAO.getUserInfor(Actor.userId);
        doTestSecond(error_code2);
    }

    public void doTestSecond(int error_code) {
        assertEquals("ok", error_code, respond.code);
        if (error_code == ErrorCode.SUCCESS) {
            assertEquals("ok", new Long(1), user.updateEmailFlag);
            assertEquals("ok", All.obj.get(ParamKey.EMAIL), user.email);
            assertEquals("ok", All.obj.get(ParamKey.NEW_PASSWORD), Userdb.getPass(Actor.userId));
        } else {
            assertEquals("ok", new Long(1), user.updateEmailFlag);
        }
    }
}
