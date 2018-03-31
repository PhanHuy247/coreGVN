/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.account;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.Date;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.LoginDataPro;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author Admin
 */
public class LoginTest {

    EntityRespond res;

    public LoginTest() {
    }

    @BeforeMethod
    public void setUp() {
        All.initPrice();
        Setting.DISTANCE.add((double) 10);
        Setting.DISTANCE.add((double) 10);
        Setting.DISTANCE.add((double) 10);
        Setting.DISTANCE.add((double) 10);
        Setting.DISTANCE.add((double) 10);
        Actor.userId = null;
    }

    @AfterMethod
    public void tearDown() {
        Userdb.removeFavoristedById(Actor.userId);
        Userdb.removeCheckOutById(Actor.userId);
        Userdb.removeFavoristedById(Actor.userId);
        Userdb.removeUserById(Actor.userId);
    }

    public void initData(int gender, String fbId, Long lastOnline) throws IOException, EazyException {
        if (gender == Constant.GENDER.MALE && fbId == null) {
            Actor.userId = Userdb.registMale();
        } else if (gender == Constant.GENDER.FEMALE && fbId == null) {
            Actor.userId = Userdb.registFemale();
        } else {
            System.out.println("regist by face");
            Actor.userId = Userdb.registUserByFacebook(fbId);
        }
        if (lastOnline != null) {
            Date d = new Date();
            d.setDate(d.getDate() - 1);
//            UserActivityDAO.updateLastOnline(Actor.userId, System.currentTimeMillis() - 24 * 60 * 60 * 1000, d);
        }
        All.put(ParamKey.IP, "localhost:9001");
        All.put(ParamKey.LOGIN_TIME, "20140707");
        if (fbId == null) {
            All.put(ParamKey.EMAIL, Userdb.getObject(Actor.userId, UserdbKey.USER.EMAIL));
        }
        All.put(ParamKey.PASSWORD, Userdb.getPass(Actor.userId));
        All.put(UserdbKey.USER.FB_ID, fbId);
        All.api = APIManager.getApi(API.LOGIN);
    }

    public void doTest(int errorCode, boolean isVerify, Integer point, LoginData data) {
        assertEquals("Login successful", errorCode, res.code);
        assertEquals("ok", isVerify, data.isVerify);
        assertEquals("ok", point, data.addPoint);
        All.obj.clear();
    }

    public Integer getPointBonus(int gender, boolean isVerify) {
        if (isVerify) {
            if (gender == Constant.GENDER.MALE) {
                return ActionManager.get(ActionType.daily_bonus).malePrice;
            } else {
                return ActionManager.get(ActionType.daily_bonus).femalePrice;
            }
        } else {
            return null;
        }
    }

    @Test(dataProviderClass = LoginDataPro.class, dataProvider = LoginDataPro.UNFINISH_REGIST)
    public void test_UnfinishRegist(int gender, String fbId, Long lastOnline) throws EazyException, IOException {
        System.out.println("gender :" + gender);
        System.out.println("fbId :" + fbId);
        initData(gender, fbId, lastOnline);
        res = (EntityRespond) All.execute();
        LoginData data = (LoginData) res.data;
        doTest(ErrorCode.SUCCESS, false, getPointBonus(gender, false), data);
    }

    @Test(dataProviderClass = LoginDataPro.class, dataProvider = LoginDataPro.FINISHED_REGISTER)
    public void test_Male_Finished(int gender, String fbId, int flagType, boolean isVerify, Long lastOnline) throws IOException, EazyException {
        initData(gender, fbId, lastOnline);
        All.updateFlag(flagType, 1);
        res = (EntityRespond) All.execute();
        LoginData data = (LoginData) res.data;
        if (lastOnline != null) {
            doTest(ErrorCode.SUCCESS, isVerify, getPointBonus(gender, isVerify), data);
        } else {
            doTest(ErrorCode.SUCCESS, isVerify, null, data);
        }
    }
}
