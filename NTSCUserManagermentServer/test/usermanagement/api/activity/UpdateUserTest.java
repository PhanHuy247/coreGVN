/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.UpdateUserDataPro;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.UpdateUserApi;

/**
 *
 * @author Admin
 */
public class UpdateUserTest {

    Respond respond;

    public UpdateUserTest() {
    }

    @BeforeMethod
    public void setUp() {
        All.api = new UpdateUserApi();
        All.obj.clear();
    }

    @AfterMethod
    public void tearDown() {
    }

    @AfterClass
    public static void removeTest() {
        Actor.removeUser();
    }  
    
    public void doFirstTest(int errorCode, int UpdateEmailFlag, int finishFlag, User user){
         assertEquals("successfull", errorCode, respond.code);
        assertEquals("ok", new Long(UpdateEmailFlag), user.updateEmailFlag);
        assertEquals("ok", new Integer(finishFlag), Userdb.getFinishFlag(user.userId));
    }
    
    @Test(dataProviderClass = UpdateUserDataPro.class , dataProvider = UpdateUserDataPro.FIRST_UPDATE)
    public void test_FirstUpdate(Map<String, Object> maps, int errorCode, int updateEmailFlag, int finishFlag) throws EazyException{
        All.obj.putAll(maps);
        respond = All.execute();
        User user = UserDAO.getUserInfor((String) maps.get(ParamKey.USER_ID));
        doFirstTest(errorCode, updateEmailFlag, finishFlag, user);
        
    }

      @Test(dataProviderClass = UpdateUserDataPro.class , dataProvider = UpdateUserDataPro.UPDATE_MORE)
    public void test_2Update(String name1, String name2, Map<String, Object> maps, int errorCode, int updateEmailFlag, int finishFlag) throws IOException, EazyException {
        All.obj.putAll(maps);
        All.put(ParamKey.USER_NAME, name1);
        All.api = new UpdateUserApi();
        respond = All.execute();
        User user = UserDAO.getUserInfor((String) maps.get(ParamKey.USER_ID));
          doFirstTest(ErrorCode.SUCCESS, updateEmailFlag, finishFlag, user);
        //2
        All.obj.remove("pwd");
        All.obj.remove(ParamKey.ORIGINAL_PASSWORD);
        All.put(ParamKey.USER_NAME, name2);
        respond = All.execute();
        user = UserDAO.getUserInfor((String) maps.get(ParamKey.USER_ID));
          doFirstTest(errorCode, updateEmailFlag, finishFlag, user);
    }
    
      @Test(dataProviderClass = UpdateUserDataPro.class , dataProvider = UpdateUserDataPro.UPDATE_MORE_ADMIN)
    public void test_2Update_Admin(String name1, String nullKey, Map<String, Object> maps, int errorCode, int updateEmailFlag, int finishFlag) throws IOException, EazyException {
        All.obj.putAll(maps);
        All.put(ParamKey.USER_NAME, name1);
        All.api = new UpdateUserApi();
        respond = All.execute();
        User user = UserDAO.getUserInfor((String) maps.get(ParamKey.USER_ID));
          doFirstTest(ErrorCode.SUCCESS, updateEmailFlag, finishFlag, user);
        //2
        All.put(nullKey, null);
        respond = All.execute();
        user = UserDAO.getUserInfor((String) maps.get(ParamKey.USER_ID));
        doFirstTest(errorCode, updateEmailFlag, finishFlag, user);
    }
}
