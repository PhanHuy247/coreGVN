/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.api.information;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Point;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class ConfirmPurchaseTest {
    
    public ConfirmPurchaseTest() {
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
    public void test_succ() throws IOException{
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.IP, "localhost : 80");
        All.put(ParamKey.POINT, (long)100);
        All.api = APIManager.getApi(API.CONFIRM_PURCHASE_IOS);
        int point = UserInforManager.getPoint(Actor.userId);
        
        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Point pnt = (Point) respond.data;
        assertEquals("ok", new Integer(point + 100),pnt.point);
    }
}
