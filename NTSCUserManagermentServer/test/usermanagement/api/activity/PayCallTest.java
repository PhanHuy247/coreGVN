/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class PayCallTest {

    EntityRespond respond;

    public PayCallTest() {
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
    public void initData(int point, int type) throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registMale();
        All.put(ParamKey.CALLER_ID, Actor.userId);
        All.put(ParamKey.RECIEVER, Actor.partnerId);
        All.put(ParamKey.POINT, Long.valueOf(point));
        All.put(ParamKey.IP, "127.0.0.1:89");
        All.put(ParamKey.TYPE, Long.valueOf(type));
        All.api = APIManager.getApi(API.CALL_PAYMENT);

    }

    @Test
    public void test_PositivePoint() throws IOException {
        initData(10, Constant.CALL_TYPE_VALUE.VIDEO_CALL);
        respond = (EntityRespond) All.execute();
        Point p = (Point) respond.data;
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", new Integer(10), p.point);
    }

    @Test
    public void test_NegativePoint() throws IOException {
        initData(- 10, Constant.CALL_TYPE_VALUE.VIDEO_CALL);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.NOT_ENOUGHT_POINT, respond.code);
    }
}
