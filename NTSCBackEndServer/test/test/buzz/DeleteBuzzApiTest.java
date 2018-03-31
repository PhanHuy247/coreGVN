/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.buzz;

import java.io.IOException;
import eazycommon.constant.Constant;
import eazycommon.exception.EazyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import test.Actor;
import test.Userdb;
import test.db.userdb.BuzzDetailDB;
import test.db.userdb.ImageDB;
import test.image.CreateImage;

/**
 *
 * @author duyetpt
 */
public class DeleteBuzzApiTest {

    public DeleteBuzzApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int buzzType) throws EazyException, IOException {
        Actor.userId = Userdb.registMale();
        String buzzValue = "buzz_value";
        if(buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ){
            buzzValue = ImageDB.insertImage(CreateImage.createImage());
        }
        BuzzDetailDB.addBuzz(Actor.userId, "buzz_value", buzzType, System.currentTimeMillis(), 1, "localhost");
        
    }
}
