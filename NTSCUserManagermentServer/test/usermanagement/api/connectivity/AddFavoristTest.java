/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.AddFavoristDataPro;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.AddFavouristApi;

/**
 *
 * @author Admin
 */
public class AddFavoristTest {

    Respond respond;
    User user;

    public AddFavoristTest() {
    }

    @BeforeClass
    public static void tearUp() {
        DatabaseLoader.init();
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    public void initData(int genderU, int genderP) throws IOException {
        Actor.initCouple(genderU, genderP);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REQUEST_USER_ID, Actor.partnerId);
        All.api = new AddFavouristApi();
    }

    @Test(dataProviderClass = AddFavoristDataPro.class, dataProvider = AddFavoristDataPro.DATA)
    public void test(int genderU, int genderF, int errorCode, int isFavoristed, int numObFav) throws IOException, EazyException {
        initData(genderU, genderF);
        respond = All.execute();
        assertEquals("successfull", errorCode, respond.code);
        // user
        user = UserDAO.getUserInfor(Actor.userId);
        assertEquals("ok", new Long(numObFav), user.favouristNumber);
        Long isF = FavoristDAO.checkFavourist(Actor.userId, Actor.partnerId);
        assertEquals("ok", isFavoristed, isF.intValue());
        // friend
        if (Actor.partnerId != null) {
            user = UserDAO.getUserInfor(Actor.partnerId);
            Long isFed = FavoritedDAO.checkFavourited(Actor.partnerId, Actor.userId);
            assertEquals("ok", isFavoristed, isFed.intValue());
            FavoristDAO.removeFavorist(Actor.userId, Actor.partnerId);
            FavoritedDAO.removeFavourited(Actor.partnerId, Actor.userId);
        }
    }

    @Test(dataProviderClass = AddFavoristDataPro.class, dataProvider = AddFavoristDataPro.DATA1)
    public void test_addF_Blocked(int genderU, int genderF, int errorCode) throws IOException, EazyException {
        initData(genderU, genderF);
        if (errorCode == ErrorCode.BLOCK_USER) {
            BlockDAO.addBlockList(Actor.partnerId, Actor.userId);
            BlockDAO.addBlockedList(Actor.userId, Actor.partnerId);
            respond = All.execute();
            assertEquals("successfull", errorCode, respond.code);
            BlockDAO.removeBlock(Actor.partnerId, Actor.userId);
            BlockDAO.removeBlocked(Actor.userId, null);
        } else {
            FavoristDAO.addFavorist(Actor.partnerId, Actor.userId);
            FavoritedDAO.addFavourited(Actor.userId, Actor.partnerId, Long.MIN_VALUE);
            respond = All.execute();
            assertEquals("ok", errorCode, respond.code);
            FavoristDAO.removeFavorist(Actor.userId, Actor.partnerId);
            FavoritedDAO.removeFavourited(Actor.partnerId, Actor.userId);
        }
    }

}
