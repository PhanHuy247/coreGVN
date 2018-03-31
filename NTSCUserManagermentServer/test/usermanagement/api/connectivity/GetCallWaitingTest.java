/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetCallWaitingData;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetCallWaiting;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.SettingCallWaiting;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import static org.testng.AssertJUnit.*;

/**
 *
 * @author Admin
 */
public class GetCallWaitingTest {

    public GetCallWaitingTest() {
    }

    @BeforeMethod
    public void setUp() throws IOException {
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_CALL_WAITING);
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }
    
    @Test
    public void test_Male() throws IOException {

        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("get successfully", ErrorCode.SUCCESS, respond.code);

        GetCallWaitingData data = (GetCallWaitingData) respond.data;
        assertEquals("", (Boolean)false, data.videoCall);
        assertEquals("", (Boolean)false, data.voiceCall);
    }
    
     @Test
    public void test_Female() throws IOException {
        
        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("get successfully", ErrorCode.SUCCESS, respond.code);

        GetCallWaitingData data = (GetCallWaitingData) respond.data;
        assertEquals((Boolean)true, data.videoCall);
        assertEquals("", (Boolean)true, data.voiceCall);
    }
    
    @Test
    public void test1() throws IOException {
        All.put(ParamKey.VIDEO_CALL_WAITING, false);
        All.put(ParamKey.VOICE_CALL_WAITING, false);
        SettingCallWaiting apiS = new SettingCallWaiting();
        Respond res = apiS.execute(All.obj, All.getTime());
        assertEquals(ErrorCode.SUCCESS, res.code);
        //get info
        GetCallWaiting api = new GetCallWaiting();
        EntityRespond respond = (EntityRespond) All.execute();
        assertEquals("get successfully", ErrorCode.SUCCESS, respond.code);

        GetCallWaitingData data = (GetCallWaitingData) respond.data;
        assertEquals("", new Boolean(false),data.videoCall);
        assertEquals("", (Boolean)false, data.voiceCall);
    }
}
