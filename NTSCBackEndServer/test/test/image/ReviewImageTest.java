/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.image;

import data.provider.ReviewImageDataPro;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.userdb.BackStageDB;
import test.db.userdb.BuzzDetailDB;
import test.db.userdb.ImageDB;
import test.db.userdb.PbImageDB;
import test.db.userdb.UserBuzzDB;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class ReviewImageTest {

    Respond respond;
    Image image;
    String buzzId;

    public ReviewImageTest() {
    }

    @AfterMethod
    public void tearDown() {
        ImageDB.removeImage(image.imageId);
        PbImageDB.removeById(image.userId);
        BackStageDB.removeBackStage(image.userId);
        UserBuzzDB.remove(image.userId);
        if (buzzId != null) {
            BuzzDetailDB.remove(buzzId);
        }
    }

    public void initData(int type, int imageFlag, int reportFlag,
            int appFlag, int appearFlag, int imageType, int avatarFlag) throws IOException {
        Actor.userId = Userdb.registMale();
        image = new Image();
        image.userId = Actor.userId;
        image.imageId = All.createId();
        image.imageStatus = appFlag;
        image.avatarFlag = avatarFlag;
        image.appFlag = appFlag;
        image.appearFlag = appearFlag;
        image.reportFlag = reportFlag;
        image.imageType = imageType;
        image.flag = imageFlag;
        image.uploadTime = System.currentTimeMillis();
        image.reportTime = System.currentTimeMillis() + 1000;

        buzzId = ImageDB.insertImage(image);

        All.api = APIManager.getApi(API.REVIEW_IMAGE);
        All.put(ParamKey.IMAGE_ID, image.imageId);
        All.put(ParamKey.TYPE, (long) type);
    }

    @Test(dataProviderClass = ReviewImageDataPro.class, dataProvider = ReviewImageDataPro.DATA_NAME)
    public void test(int type, int imageFlag, int reportFlag, int appFlag,
            int appearFlag, int imageType, int avatarFlag, int errorCode) throws IOException, EazyException {
        // accept
        initData(type, imageFlag, reportFlag, appFlag, appearFlag, imageType, avatarFlag);
        Integer prePbImage = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.PB_IMAGE_NUMBER);
        Integer preBuzzNum = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.BUZZ_NUMBER);
        Integer preBackStage = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.BACKSTAGE_NUMBER);
        if (preBackStage == null) {
            preBackStage = 0;
        }
        if (preBuzzNum == null) {
            preBuzzNum = 0;
        }
        if (prePbImage == null) {
            prePbImage = 0;
        }
        respond = All.execute();
        assertEquals("ok", errorCode, respond.code);
        doTest(type, prePbImage, preBuzzNum, preBackStage);
    }

    private static boolean checkReportFlag(Image image) {
        if (image.reportFlag == null) {
            return true;
        }
        if (image.reportFlag == Constant.REPORT_STATUS_FLAG.GOOD) {
            return true;
        }
        if (image.reportFlag == Constant.REVIEW_STATUS_FLAG.PENDING && image.appearFlag == Constant.FLAG.ON) {
            return true;
        }
        return false;
    }

    public void doTest(int type, int prePbImage, int preBuzzNum, int preBackStage) throws EazyException {
        int changeNum = 0;
        int flag = 0;
        if (type == 1) {
            changeNum = 1;
            flag = Constant.FLAG.ON;
        } else if (type == -1) {
            changeNum = 0;
            flag = -1;
        }

        if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE && type == 1 && checkReportFlag(image) == true
                && image.imageStatus != Constant.REVIEW_STATUS_FLAG.APPROVED) {
            Image ima = ImageDAO.getImageInfor(image.imageId);
            assertEquals("ok", (Integer) flag, ima.imageStatus);
            Integer backStageNum = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.BACKSTAGE_NUMBER);
            assertEquals("ok", new Integer(preBackStage + changeNum), backStageNum);
            assertEquals("ok", (Integer) flag, BackStageDB.getFlag(Actor.userId, image.imageId));
        }

        if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC && checkReportFlag(image) == true
                && image.imageStatus != Constant.REVIEW_STATUS_FLAG.APPROVED) {
            Image ima = ImageDAO.getImageInfor(image.imageId);
            assertEquals("ok", (Integer) flag, ima.imageStatus);
            Integer pbImg = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.PB_IMAGE_NUMBER);
            pbImg = pbImg == null ? 0 : pbImg;
            assertEquals("ok", (Integer) (prePbImage + changeNum), pbImg);
            Integer buzzNum = (Integer) Userdb.getObject(Actor.userId, UserdbKey.USER.BUZZ_NUMBER);
            buzzNum = buzzNum == null ? 0 : buzzNum;
            assertEquals("ok", (Integer) (preBuzzNum + changeNum), buzzNum);

            assertEquals("ok", image.flag, PbImageDB.getFlag(Actor.userId, image.imageId));
            int buzzDAppFlag = type == 1 ? 1 : 0;
            assertEquals("ok", (Integer) buzzDAppFlag, BuzzDetailDB.getFlag(buzzId));
            int UserBuzzAppFlag = type ;
            assertEquals("ok", (Integer) UserBuzzAppFlag, UserBuzzDB.getFag(Actor.userId, buzzId));
            if (image.avatarFlag == Constant.FLAG.ON) {
                boolean isAvatar = Userdb.getObject(Actor.userId, UserdbKey.USER.AVATAR_ID).equals(image.imageId);
                String expectAvatar = isAvatar == true ? (String) Userdb.getObject(Actor.userId, UserdbKey.USER.AVATAR_ID) : null;
                if (type == 1) {
                    assertEquals("ok", image.imageId, expectAvatar);
                } else if (type == -1) {
                    assertEquals("ok", null, expectAvatar);
                }
            }
        }
    }

}
