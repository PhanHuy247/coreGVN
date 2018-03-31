/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.gift;

import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import test.All;
import test.db.stampdb.Giftdb;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.entity.impl.gift.Gift;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * 
 * @author duyetpt
 */
public class UpdateGiftApiTest {
    
    String id;
    Respond respond;
    public UpdateGiftApiTest() {
    }
    
    @BeforeMethod
    public void setUp() {
    }
    
    @AfterMethod
    public void tearDown() throws EazyException {
        GiftDAO.deleteGift(id);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData() throws EazyException{
        id = All.createId();
        Giftdb.insertGift(id);
        
        All.put(ParamKey.GIFT_ID, id);
        All.put("gift_inf", "funny");
        All.put(ParamKey.GIFT_PRICE, (long)100);
        All.put(ParamKey.ENGLISH_NAME, "smile");
        All.put(ParamKey.JAPANESE_NAME, "笑顔");
        All.api = APIManager.getApi(API.UPDATE_GIFT);
    }
    
    @Test
    public void test_succ() throws EazyException{
        initData();
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        
        Gift gift = Giftdb.getGift(id);
        assertEquals(gift.enGiftName, "smile");
        assertEquals(gift.giftInfor, "funny");
        assertEquals(gift.giftPrice, new Integer(100));
        assertEquals(gift.jpGiftName, "笑顔");
    }
    
     @Test
    public void test_price_negative() throws EazyException{
        initData();
        All.put(ParamKey.GIFT_PRICE, -1);
        respond = All.execute();
        assertEquals("ok", 4, respond.code);
    }
    
     @Test
    public void test_price_null() throws EazyException{
        initData();
        All.put(ParamKey.GIFT_PRICE, null);
        respond = All.execute();
        assertEquals("ok", 4, respond.code);
    }
    
     @Test
    public void test_name_notEng() throws EazyException{
        initData();
        All.put(ParamKey.ENGLISH_NAME, null);
        respond = All.execute();
        assertEquals("ok", 5, respond.code);
    }
    
     @Test
    public void test_name_notJa() throws EazyException{
        initData();
        All.put(ParamKey.JAPANESE_NAME, null);
        respond = All.execute();
        assertEquals("ok", 6, respond.code);
    }
}
