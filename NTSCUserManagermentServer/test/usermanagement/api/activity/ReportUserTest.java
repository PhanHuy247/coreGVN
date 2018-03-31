/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.ImageDB;
import DAOTest.ReportImageDB;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class ReportUserTest {

    Respond respond;

    public ReportUserTest() {
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
    public void initData(int reportType, int subjectType) throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.REPORT_TYPE, (long) reportType);
        All.put("subject_type", (long) subjectType);
        All.api = APIManager.getApi(API.REPORT);
    }

    @Test
    public void test_reportImg() throws IOException, EazyException {
        initData(1, Constant.REPORT_TYPE_VALUE.IMAGE_REPORT);
        String imageId = ImageDB.insertImage(Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, null);
        All.put("subject_id", imageId);

        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        ImageDB.remove(imageId, Actor.userId);
        ReportImageDB.removeImgR(Actor.userId, imageId, Constant.REPORT_TYPE_VALUE.IMAGE_REPORT);
    }

    @Test
    public void test_reportBuzz() throws IOException, EazyException {
        initData(1, Constant.REPORT_TYPE_VALUE.BUZZ_REPORT);
        String BuzzID = All.createId();
        UserDAO.addBuzz(BuzzID);
        UserBuzzDAO.add(BuzzID, Actor.userId);
        System.out.println("actor :" + Actor.userId);
        All.put("subject_id", BuzzID);

        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        ReportImageDB.removeImgR(Actor.userId, BuzzID, Constant.REPORT_TYPE_VALUE.IMAGE_REPORT);
    }
}
