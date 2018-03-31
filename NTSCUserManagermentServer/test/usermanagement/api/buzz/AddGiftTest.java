/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.api.buzz;

import java.io.IOException;

import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class AddGiftTest {
    
    Respond respond;
    
    public AddGiftTest() {
    }
    
    @AfterMethod
    public void tearDown(){
        Actor.removeUser();
    }
    
    public void initData() throws IOException{
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registFemale();
        
        All.put(ParamKey.GIFT_ID, "gift_ID");
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.SENDER, Actor.partnerId);
        All.put(ParamKey.TIME, All.getTime().getTime());
        
        All.api = APIManager.getApi(API.SEND_GIFT);
    }
    
    @Test
    public void test() throws IOException, EazyException{
        initData();
        Integer befN = UserDAO.getGiftNumber(Actor.userId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Integer afN = UserDAO.getGiftNumber(Actor.userId);
        
        assertEquals("ok", new Integer(befN + 1), afN);
       /// GiftDAO.removeGift(Actor.userId, "gift_ID");
    }
}
