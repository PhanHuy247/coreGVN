/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon;

import eazycommon.backlist.BannedWordManger;
import eazycommon.dao.CommonDAO;

/**
 *
 * @author duyetpt
 */
public class CommonCore {
    
    public static void init(){
        CommonConfig.initConfig();
        CommonDAO.init();
        BannedWordManger.init();
    }
}
