/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.provider;

import org.testng.annotations.DataProvider;
import test.All;

/**
 *
 * @author duyetpt
 */
public class SearchUserDataPro {
    
    public static final String DATA = "data";
    @DataProvider(name = DATA)
    public static Object[][] provideData(){
        return new Object[][]{
          All.$(),
        };
    }
}
