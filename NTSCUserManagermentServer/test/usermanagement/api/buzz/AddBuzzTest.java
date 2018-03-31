/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.buzz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationSettingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import static org.testng.AssertJUnit.assertEquals;
/**
 *
 * @author duyetpt
 */
public class AddBuzzTest {

    Respond respond;

    public AddBuzzTest() {
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
    public void initData(int buzzType) throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.TIME, All.getTime().getTime());
        All.put(ParamKey.IP, "127.0.0.1:9804");
        All.put(ParamKey.BUZZ_TYPE, Long.valueOf(buzzType));
        All.put(ParamKey.BUZZ_ID, "53d70398e4b04da163a59263");
        All.put(ParamKey.BUZZ_VALUE, "buzz_value");
        All.api = APIManager.getApi(API.ADD_BUZZ);
    }

    @Test
    public void test_StatusBuzz() throws IOException, EazyException {
        initData(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        String buzzValue = UserActivityDAO.getStatus(Actor.userId);
        assertEquals("ok", "buzz_value", buzzValue);
        UserActivityDAO.removeStatus(Actor.userId);
    }

    @Test
    public void test_ImageBuzz() throws IOException, EazyException {
        initData(Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ);
        All.put(ParamKey.IS_APPROVED_IMAGE, Long.valueOf(Constant.FLAG.ON));
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Boolean isExist = PbImageDAO.checkPbImageExist(Actor.userId, "buzz_value");
        PbImageDAO.removePublicImage(Actor.userId, "buzz_value");
        assertEquals("ok", Boolean.valueOf(true), isExist);
    }
    
     @Test
    public void test_NumberBuzz() throws IOException, EazyException {
        initData(Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ);
        All.put(ParamKey.IS_APPROVED_IMAGE, Long.valueOf(Constant.FLAG.ON));
        Integer befBuzzN = UserDAO.getBuzzNumber(Actor.userId);
        
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        Boolean isExist = PbImageDAO.checkPbImageExist(Actor.userId, "buzz_value");
        PbImageDAO.removePublicImage(Actor.userId, "buzz_value");
        assertEquals("ok", Boolean.valueOf(true), isExist);
        
          Integer afBuzzN = UserDAO.getBuzzNumber(Actor.userId);
          assertEquals("ok", new Integer(befBuzzN + 1), afBuzzN);
    }
    
    @Test
    public void test_pustNotiFavoristList() throws IOException, EazyException{
        initData(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ);
        Actor.partnerId = Userdb.registMale();
        FavoritedDAO.addFavourited(Actor.userId, Actor.partnerId, Long.MIN_VALUE);
        FavoritedDAO.addFavourited(Actor.partnerId, Actor.userId, Long.MIN_VALUE);
        // add noti setting
        NotificationSettingDAO.addNotificationSetting(Actor.partnerId, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.ON, Constant.FLAG.ON);
        
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        String buzzValue = UserActivityDAO.getStatus(Actor.userId);
        assertEquals("ok", "buzz_value", buzzValue);
        UserActivityDAO.removeStatus(Actor.userId);
        List listB = new ArrayList<String>();
        int list = NotificationDAO.getNotificationNumber(Actor.partnerId, Util.currentTime() - 100000000, listB);
        assertEquals(Integer.valueOf(1), Integer.valueOf(list));
    }
}
