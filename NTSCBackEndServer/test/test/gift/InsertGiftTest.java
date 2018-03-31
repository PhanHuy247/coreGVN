/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.gift;

import eazycommon.CommonCore;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.vn.ntsc.backend.dao.gift.GiftDAO;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.InsertGiftApi;
/**
 *
 * @author Admin
 */
public class InsertGiftTest {
    
    JSONObject obj ;
    @BeforeClass
    public static void tearUp(){
    }
    @BeforeMethod
    public void setup(){
        CommonCore.init();
        obj = new JSONObject();
    }
    
    @AfterMethod
    public void tearDown() throws EazyException{
        GiftDAO.deleteGift((String) obj.get(ParamKey.ID));
    }
    public void initData(String id, long price, String giftinfo, String enGiftName, String jpGiftName){
        obj.put(ParamKey.ID, id);
        obj.put(ParamKey.GIFT_PRICE, price);
        obj.put("gift_inf", giftinfo);
        obj.put(ParamKey.ENGLISH_NAME,  enGiftName);
        obj.put(ParamKey.JAPANESE_NAME,  jpGiftName);
        obj.put(ParamKey.IMAGE, "image");
    }
    
    @Test
    public void test(){
        initData("53b52d66c4e44a16508f3a0c", 10, "nhan", "ring", "環");
        InsertGiftApi ig = new InsertGiftApi();
        Respond respond = ig.execute(obj);
        assertEquals(ErrorCode.SUCCESS, respond.code);
    }
    
    @Test
    public void test1(){
        initData("53b52d66c4e44a16508f3a1c", 10, "nhan", "ring", "環");
        InsertGiftApi ig = new InsertGiftApi();
        Respond respond = ig.execute(obj);
        assertEquals(ErrorCode.SUCCESS, respond.code);
    }
    
    @Test
    public void test2(){
        initData("53b52d66c4e44a16508f3a2c", 10, "nhan", "ring", "環");
        InsertGiftApi ig = new InsertGiftApi();
        Respond respond = ig.execute(obj);
        assertEquals(ErrorCode.SUCCESS, respond.code);
    }
    
    @Test
    public void test3(){
        initData("53b52d66c4e44a16508f3a3c", 10, "nhan", "ring", "環");
        InsertGiftApi ig = new InsertGiftApi();
        Respond respond = ig.execute(obj);
        assertEquals(ErrorCode.SUCCESS, respond.code);
    }
}
