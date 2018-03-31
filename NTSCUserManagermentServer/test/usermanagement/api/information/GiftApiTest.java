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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class GiftApiTest {

    EntityRespond respond;

    public GiftApiTest() {
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
    public void initData() throws IOException {
        Actor.userId = Userdb.registMale();
        Actor.partnerId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.RECEIVER_ID, Actor.partnerId);
        All.put(ParamKey.GIFT_PRICE, (long) 100);

        All.api = APIManager.getApi(API.GIFT);
    }

    @Test
    public void test_succ() throws IOException {
        initData();
        UserInforManager.get(Actor.userId).point = 1000;
        int befPP = UserInforManager.get(Actor.partnerId).point;
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        
        Integer afPP = UserInforManager.get(Actor.partnerId).point;
        Point pnt = (Point) respond.data;
        assertEquals("ok", new Integer(1000 - 100), pnt.point);
        assertEquals("ok", new Integer(befPP + 1), afPP);
    }
    
    @Test
    public void test_NotEnoughtPoint() throws IOException{
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.NOT_ENOUGHT_POINT, respond.code);
    }
    
    @Test
    public void test_UserNotFound() throws IOException{
        initData();
        All.put(ParamKey.RECEIVER_ID, "21321122123");
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.USER_NOT_EXIST, respond.code);
    }
}
