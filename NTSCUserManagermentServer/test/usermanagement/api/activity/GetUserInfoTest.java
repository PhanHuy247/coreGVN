/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import java.util.Date;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.CheckOutDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUserInfoData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetUserInfoTest {

    EntityRespond respond;

    public GetUserInfoTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Userdb.removeCheckOutById(Actor.userId);
        Userdb.removeFavoristedById(Actor.userId);
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData() throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);

        All.api = APIManager.getApi(API.GET_USER_INFOR);
    }

    @Test
    public void test_getOfOther() throws IOException, EazyException {
        initData();

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetUserInfoData data = (GetUserInfoData) respond.data;
        assertEquals("ok", new Integer(Constant.FLAG.OFF), data.unlockBackstage);
        assertEquals("ok", new Long(Constant.FLAG.OFF), data.isAlert);
        System.out.println(data.user.toJson());
        Date date = CheckOutDAO.getCheckOutTime(Actor.partnerId, Actor.userId);
        System.out.println("date : " + date);
    }

    @Test
    public void test_getOfOwner() throws IOException, EazyException {
        initData();
        All.put(ParamKey.REQUEST_USER_ID, Actor.userId);
        // add favoristed and checkout
        for (int i = 0; i < 10; i++) {
            CheckOutDAO.addCheckOut(Actor.userId, "friendId " + i, new Date());
        }

        for (int i = 0; i < 10; i++) {
            FavoritedDAO.addFavourited(Actor.userId, "friendId " + i, (new Date()).getTime());
        }

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetUserInfoData data = (GetUserInfoData) respond.data;
        assertNull("ok", data.isAlert);
        assertNull("ok", data.unlockBackstage);
        assertNotNull(data.user.finishRegisterFlag);
        assertNotNull(data.user.updateEmailFlag);

        if (data.user instanceof Female) {
            assertNotNull(((Female) data.user).verificationFlag);
        }

        assertEquals("ok", new Integer(10), data.checkoutNum);
        assertEquals("ok", new Long(10), data.user.favouritedNumber);

        //------------------------------------------// add favoristed and checkout
        for (int i = 100; i < 110; i++) {
            CheckOutDAO.addCheckOut(Actor.userId, "friendId " + i, new Date());
        }
        for (int i = 100; i < 110; i++) {
            FavoritedDAO.addFavourited(Actor.userId, "friendId " + i, (new Date()).getTime());
        }

        respond = (EntityRespond) All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        data = (GetUserInfoData) respond.data;
        assertEquals("ok", new Integer(20), data.checkoutNum);
        assertEquals("ok", new Long(20), data.user.favouritedNumber);
    }
}
