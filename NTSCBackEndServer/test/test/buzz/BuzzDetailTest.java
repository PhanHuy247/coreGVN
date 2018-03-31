/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.buzz;

import java.io.IOException;
import eazycommon.constant.API;
import test.Actor;
import test.db.userdb.BuzzDetailDB;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class BuzzDetailTest {

    EntityRespond respond;
    String buzzId;

    public BuzzDetailTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
        BuzzDetailDB.remove(buzzId);
    }

    public void initData() throws EazyException, IOException {
        Actor.userId = Userdb.registMale();
        buzzId = BuzzDetailDB.addBuzz(Actor.userId, "buzz_value", 1, System.currentTimeMillis(), 1, "ip");

        All.put(ParamKey.BUZZ_ID, buzzId);
        All.api = APIManager.getApi(API.BUZZ_DETAIL);
    }
    
    @Test
    public void test() throws EazyException, IOException{
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Buzz buzz = (Buzz) respond.data;
        System.out.println(buzz.toJsonObject().toJSONString());
    }
}
