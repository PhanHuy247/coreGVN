/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.RegistDataPro;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author Admin
 */
public class RegisterTest {

    EntityRespond res;

    public RegisterTest() {
    }

    @BeforeMethod
    public void setUp() {
        All.obj.clear();
        Setting.DISTANCE.add((double) 12);
        Setting.DISTANCE.add((double) 10);
        Setting.DISTANCE.add((double) 11);
        Setting.DISTANCE.add((double) 112);
        Setting.DISTANCE.add((double) 1123);
        All.initPrice();
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    public void initRegistUser(String fbId, Long gender, String bir) throws IOException {
        All.put("device_type", Constant.DEVICE_TYPE.IOS);
        All.put("bir", bir);
        All.put("notify_token", "APA91bFeXIuBU5gZq_oFwYtf_iCHDScGxdQsbPpdeDgDNA19IE7VgpKlSs7HI-JrNINHtd0g-6skobKC9nKrd5eo8X_ArFbgAJSdRN2zXqsZR58G0AjyRC9W0dYnMPr7R4hzr_vO73FObSORqE9T31_HBSGXDhdFAQ");
        All.put((ParamKey.LOGIN_TIME), "20140716100946");
        All.put("gender", gender);
        if (fbId != null) {
            All.put(UserdbKey.USER.FB_ID, fbId);
        }

        All.api = APIManager.getApi(API.REGISTER);
    }

    @Test(dataProviderClass = RegistDataPro.class, dataProvider = RegistDataPro.DATA_NAME)
    public void test(String fbId, int gender, String bir, int errorCode) throws IOException, EazyException {
        initRegistUser(fbId, (long) gender, bir);
        res = (EntityRespond) All.execute();
        Assert.assertEquals(res.code, errorCode);
        LoginData data = (LoginData) res.data;

        if (errorCode == ErrorCode.SUCCESS) {
            Assert.assertEquals(true, data.blackList.isEmpty());
            Assert.assertEquals(new Integer(0), data.checkoutNum);
            Assert.assertEquals(false, data.isVerify);
            Assert.assertEquals(Integer.valueOf(0), data.notiNum);
            User user = UserDAO.getUserInfor(data.user.userId);
            System.out.println("regist succ, get succ");
            Actor.userId = user.userId;
            if (fbId != null) {
                Assert.assertEquals(user.updateEmailFlag, new Long(1));
            } else {
                Assert.assertEquals(user.updateEmailFlag, new Long(0));
            }
            if (gender == Constant.GENDER.FEMALE) {
                Assert.assertEquals(Userdb.getverificationFlag(Actor.userId), new Integer(1));
            }
        }
    }

//    @Test
//    public void test_MaleRegist_FbId() throws IOException, DaoException {
//        initRegistUser(null, "12345678", null, Long.valueOf(Constant.MALE), Long.valueOf(1), null);
//        All.put("fb_id", "facebookId");
//        RegisterApi api = new RegisterApi();
//        Date time = Util.convertToGMT();
//        res = (EntityRespond) api.execute(All, time);
//        assertEquals("regist successfully !", ErrorCode.SUCCESS, res.code);
//        LoginData data = (LoginData) res.data;
//
//        System.out.println(data.toJsonObject());
//        assertEquals("blackList true", true, data.blackList.isEmpty());
//        assertEquals("checkOutNum true", new Integer(0), data.checkoutNum);
//        assertEquals("isVertify true", false, data.isVerify);
//        assertEquals(Integer.valueOf(0), data.notiNum);
//        User user = UserDAO.getUserInfor(data.user.userId);
//        id = user.userId;
//        assertEquals("ok", new Long(1), user.updateEmailFlag);
//        assertNull("ok", Userdb.getverificationFlag(id));
//    }
//
//    @Test
//    public void test_RegistByEmail_Male() throws IOException, DaoException {
//        initRegistUser("ntq123@gmail.com", "12345678", null, Long.valueOf(Constant.MALE), Long.valueOf(1), null);
//        RegisterApi api = new RegisterApi();
//        Date time = Util.convertToGMT();
//        res = (EntityRespond) api.execute(All, time);
//        assertEquals("regist successfully !", ErrorCode.SUCCESS, res.code);
//        LoginData data = (LoginData) res.data;
//
//        System.out.println(data.toJsonObject());
//        assertEquals("blackList true", true, data.blackList.isEmpty());
//        assertEquals("checkOutNum true", new Integer(0), data.checkoutNum);
//        assertEquals("isVertify true", false, data.isVerify);
//        assertEquals(Integer.valueOf(0), data.notiNum);
//        User user = UserDAO.getUserInfor(data.user.userId);
//        id = user.userId;
//        assertEquals("ok", new Long(0), user.updateEmailFlag);
//        assertNull("ok", Userdb.getverificationFlag(id));
//        
//    }
//
//     @Test
//    public void test_RegistByEmail_Female() throws IOException, DaoException {
//        initRegistUser("ntq123@gmail.com", "12345678", null, Long.valueOf(Constant.FEMALE), Long.valueOf(1), null);
//        RegisterApi api = new RegisterApi();
//        Date time = Util.convertToGMT();
//        res = (EntityRespond) api.execute(All, time);
//        assertEquals("regist successfully !", ErrorCode.SUCCESS, res.code);
//        LoginData data = (LoginData) res.data;
//
//        System.out.println(data.toJsonObject());
//        assertEquals("blackList true", true, data.blackList.isEmpty());
//        assertEquals("checkOutNum true", new Integer(0), data.checkoutNum);
//        assertEquals("isVertify true", false, data.isVerify);
//        assertEquals(Integer.valueOf(0), data.notiNum);
//        User user = UserDAO.getUserInfor(data.user.userId);
//        id = user.userId;
//        assertEquals("ok", new Long(0), user.updateEmailFlag);
//        assertEquals("ok", new Integer(1), Userdb.getverificationFlag(id));
//        
//    }
//    
//     @Test
//    public void test_Regist_AgeLess16() throws IOException, DaoException {
//        initRegistUser("ntq123@gmail.com", "12345678", null, Long.valueOf(Constant.FEMALE), Long.valueOf(1), null);
//         All.put("bir", "20000716");
//        RegisterApi api = new RegisterApi();
//        Date time = Util.convertToGMT();
//        res = (EntityRespond) api.execute(All, time);
//        assertEquals("regist successfully !", ErrorCode.INVALID_BIRTHDAY, res.code);
//    }
}
