/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.ImageDB;
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.SaveImageDataPro;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class SaveImageTest {

    EntityRespond respond;

    public SaveImageTest() {
    }

    @BeforeMethod
    public static void setUpClass() {
        All.initPrice();
    }

    @AfterMethod
    public void teardown() {
        Actor.removeUser();
    }

    public void initData(int genderU, int genderP) throws IOException, EazyException {
        if (genderU == Constant.GENDER.FEMALE) {
            Actor.userId = Userdb.registFemale();
        } else {
            Actor.userId = Userdb.registMale();
        }

        if (genderP == Constant.GENDER.FEMALE) {
            Actor.partnerId = Userdb.registFemale();
        } else {
            Actor.partnerId = Userdb.registMale();
        }

        All.put(ParamKey.USER_ID, Actor.userId);
        String imageId = ImageDB.insertImage(Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Actor.partnerId);
        All.put(ParamKey.IMAGE_ID, imageId);
        All.api = APIManager.getApi(API.SAVE_IMAGE);
    }

    @Test(dataProviderClass = SaveImageDataPro.class, dataProvider = SaveImageDataPro.DATA_NAME)
    public void test(int genderU, int genderP, int errorCode) throws IOException, EazyException {
        initData(genderU, genderP);
        Integer befU = UserInforManager.getPoint(Actor.userId);
        Integer befP = UserInforManager.getPoint(Actor.partnerId);

        int decreaseP, increaseP;
        if (genderU == Constant.GENDER.FEMALE) {
            decreaseP = ActionManager.get(ActionType.save_image).femalePrice;
        } else {
            decreaseP = ActionManager.get(ActionType.save_image).malePrice;
        }
        if (genderP == Constant.GENDER.FEMALE) {
            increaseP = ActionManager.get(ActionType.save_image).femalePartnerPrice;
        } else {
            increaseP = ActionManager.get(ActionType.save_image).malePartnerPrice;
        }

        respond = (EntityRespond) All.execute();
        All.doTestPoint(Actor.userId, befU, decreaseP, Actor.partnerId, befP, increaseP, errorCode, respond.code);
        ImageDB.remove((String) All.obj.get(ParamKey.IMAGE_ID), Actor.partnerId);
    }

    @Test(dataProviderClass = SaveImageDataPro.class, dataProvider = SaveImageDataPro.DATA_NOT_ENOUGHT)
    public void test_notEnoughPoint(int genderU, int genderP, int errorCode) throws IOException, EazyException {

        initData(genderU, genderP);
        UserInforManager.get(Actor.userId).point = 0;
        Integer befU = UserInforManager.getPoint(Actor.userId);
        Integer befP = UserInforManager.getPoint(Actor.partnerId);

        respond = (EntityRespond) All.execute();
         All.doTestPoint(Actor.userId, befU, 0, Actor.partnerId, befP, 0, errorCode, respond.code);
        ImageDB.remove((String) All.obj.get(ParamKey.IMAGE_ID), Actor.partnerId);
    }
}
