/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.userdb.ImageDB;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class ListImageApiTest {

    List<String> listImg;
    EntityRespond respond;

    public ListImageApiTest() {
    }

    @BeforeMethod
    public void setUp() {
        listImg = new ArrayList<>();
    }

    @AfterMethod
    public void tearDown() {
//        Actor.removeUser();
//        for (String id : listImg) {
//            ImageDB.removeImage(id);
//        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int countGet, int type) throws IOException, InterruptedException {
        Actor.userId = Userdb.registMale();
        String img_id = "";
        Image image = CreateImage.createImage();
        image.flag = 1;
        image.imageStatus = type;
        for (int i = 0; i < countGet; i++) {
            img_id = All.createId();
            image.imageId = img_id;
            System.out.println("img_id :" + img_id );
            image.uploadTime = System.currentTimeMillis();
            image.reviewTime = System.currentTimeMillis() + 100;
            image.userId = Actor.userId;
            image.imageType = type;
            ImageDB.insertImage(image);
            listImg.add(img_id);
            Thread.sleep(2);
        }

        for (int i = 0; i < countGet + 1; i++) {
            img_id = All.createId();
            image.imageId = img_id;
            image.uploadTime = System.currentTimeMillis();
            image.reviewTime = System.currentTimeMillis() + 100;
            image.userId = Actor.userId;
            image.imageType = Constant.IMAGE_TYPE_VALUE.IMAGE_CHAT;
            ImageDB.insertImage(image);
            listImg.add(img_id);
        }

        All.api = APIManager.getApi(API.LIST_IMAGE);
        All.put(ParamKey.TYPE, (long) type);
        All.put(ParamKey.SKIP, (long) 0);
        All.put(ParamKey.TAKE, (long) countGet);
        All.put(ParamKey.SORT, (long) 1);
        All.put(ParamKey.ORDER, (long) 1);
    }

    @Test
    public void test() throws InterruptedException, IOException {
        initData(10, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        SizedListData list = (SizedListData) respond.data;
        assertEquals("ok", 10, list.ll.size());
    }

}
