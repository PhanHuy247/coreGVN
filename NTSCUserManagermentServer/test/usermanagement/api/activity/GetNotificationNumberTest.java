/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.NotificationColl;
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.GetNotificationNumberData;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.NotificationNumber;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetNotificationNumberApi;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Admin
 */
public class GetNotificationNumberTest {
    
    EntityRespond respond;
    
    public GetNotificationNumberTest() {
    }
    
    @BeforeMethod
    public void setUp() {
        All.initPrice();
    }
    
    @AfterMethod
    public void tearDown() {
        NotificationColl.remove(Actor.userId);
        Actor.removeUser();
    }
    
    @Test(dataProviderClass = GetNotificationNumberData.class, dataProvider = GetNotificationNumberData.DATA)
    public void test(int count) throws IOException {
        Actor.userId = Userdb.registFemale();
        addNoti(Actor.userId, count);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = new GetNotificationNumberApi();
        
        respond = (EntityRespond) All.execute();
        assertEquals("successfull", ErrorCode.SUCCESS, respond.code);
        NotificationNumber data = (NotificationNumber) respond.data;
        assertEquals("Ok", count, data.noti);
    }
    
    public void addNoti(String toUser, int amount) {
        String fromUser = "from";
        String id = null;
        for (int i = 0; i < amount; i++) {
            id = NotificationColl.insert(Actor.userId, fromUser + i);
            Actor.listPartnerId.add(id);
        }
    }
}
