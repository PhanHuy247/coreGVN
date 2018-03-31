/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.api.information;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BlackListData;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetBlackListTest {
    
    EntityRespond respond;
    List<String> listBlock;
    List<String> listBlack;
    
    public GetBlackListTest() {
    }
    
    @BeforeMethod
    public void setUp() {
        listBlock = new ArrayList<String>();
        listBlack = new ArrayList<String>();
    }
    
    @AfterMethod
    public void tearDown() {
        for(String id : listBlock){
            Userdb.removeUserById(id);
        }
        Userdb.removeBlockListById(Actor.userId);
        Actor.removeUser();
        for(String id : listBlack){
            Userdb.removeUserById(id);
            DeactivateUserManager.remove(id);
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    public void initData() throws IOException, EazyException{
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.GET_BLACK_LIST);
        
        String id = "";
        // block list
        for(int i =0; i < 10; i++){
            id = Userdb.registMale();
            listBlock.add(id);
            BlockDAO.addBlockList(Actor.userId, id);
        }
        
        //deactive list
        for(int i = 0; i < 7; i++){
            id = Userdb.registMale();
            listBlack.add(id);
            DeactivateUserManager.add(id);
        }
    }
    
    @Test
    public void test_succ() throws IOException, EazyException{
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        BlackListData data = (BlackListData) respond.data;
        
        assertEquals("ok", 10, data.block_lst.size());
        assertEquals("ok", 7, data.deactiveList.size());
    }
}
