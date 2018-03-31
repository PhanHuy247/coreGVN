/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.provider;

import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class GetNotificationNumberData {

    public static final String DATA = "data";

    @DataProvider(name = DATA)
    public static Object[][] provideData() {
        return new Object[][]{
            All.$(0),
            All.$(10),
            All.$(20),
        };
    }
}
