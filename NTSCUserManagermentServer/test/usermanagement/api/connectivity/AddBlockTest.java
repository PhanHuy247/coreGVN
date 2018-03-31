/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

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
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class AddBlockTest {

    Respond respond;

    public AddBlockTest() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    public void initData() throws IOException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registFemale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.api = APIManager.getApi(API.ADD_BLOCK);
    }

    @Test
    public void test() throws IOException, EazyException {
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        boolean result = BlockDAO.isBlock(Actor.userId, Actor.partnerId);
        assertEquals("ok", true, result);
        BlockDAO.removeBlocked(Actor.userId, Actor.partnerId);
    }
    
    @Test
    public void test_Blocked() throws IOException, EazyException{
        initData();
        BlockDAO.addBlockList(Actor.userId, Actor.partnerId);
        BlockDAO.addBlockedList(Actor.userId, Actor.partnerId);
        
        respond = All.execute();
        assertEquals("ok", ErrorCode.BLOCK_USER, respond.code);
        
    }
}
