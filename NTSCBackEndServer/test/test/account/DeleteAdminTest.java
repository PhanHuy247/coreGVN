package test.account;


import eazycommon.constant.API;
import test.Actor;
import test.db.settingdb.AdminDB;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.Respond;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author duyetpt
 */
public class DeleteAdminTest {

    Respond respond;

    public DeleteAdminTest() {
    }
    
    @BeforeMethod
    public void setUp(){
        
    }
    
    @AfterMethod
    public void tearDown(){
        AdminDB.remove(Actor.userId);
    }
    
    public void initData(int specialFlag){
        Actor.userId = AdminDB.registAdmin("admin@gmail.com", "amdin", "hello", 1, specialFlag, "jsljl");
        All.put(ParamKey.ID, Actor.userId);
        All.api = APIManager.getApi(API.DELETE_ADMIN);
    }
    
    @Test
    public void test_succ(){
        initData(0);
        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", false, AdminDB.isExit(Actor.userId));
    }
    
    @Test
    public void test_supperAdmin(){
        initData(1);
        System.out.println("id :" + Actor.userId);
        respond = All.execute();
        assertEquals("ok", ErrorCode.UNKNOWN_ERROR, respond.code);
        assertEquals("ok", true, AdminDB.isExit(Actor.userId));
    }
}
