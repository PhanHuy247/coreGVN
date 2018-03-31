/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class TradePointToMoneyTest {
    Respond respond;
    public TradePointToMoneyTest() {
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
    public void initData(int point) throws IOException {
        Actor.userId = Userdb.registFemale();
        All.api = APIManager.getApi(API.POINT_TO_MONEY);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.POINT, new Long(point));
        All.put(ParamKey.IP, "127.0.0.1:8990");
    }
    
    @Test
    public void test_notEnoughtPoint() throws IOException{
        initData(100);
        Integer befP = UserInforManager.getPoint(Actor.userId);
        respond = All.execute();
        Integer afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("ok", ErrorCode.NOT_ENOUGHT_POINT, respond.code);
        assertEquals("ok", befP, afP);
    }
    
    @Test
    public void test_enoughtPoint() throws IOException{
        initData(100);
        UserInforManager.get(Actor.userId).point = 100;
        Integer befP = UserInforManager.getPoint(Actor.userId);
        respond = All.execute();
        Integer afP = UserInforManager.getPoint(Actor.userId);
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", new Integer(befP - 100), afP);
    }
    
}
