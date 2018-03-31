/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntsc.tool;

import com.ntsc.tool.constants.Config;
import com.ntsc.tool.utils.Utils;
import com.ntsc.tool.DAO.FileDAO;
import java.io.IOException;

/**
 *
 * @author HuyDX
 */
public class Main {
 
    public static void main(String[] args) throws IOException {
        Config.initConfig();
        int before = FileDAO.count();
        if (Config.MOCKUP_TYPE==1)
            CreateVideoRecordByTime.process();
        else if (Config.MOCKUP_TYPE==2)
            DeleteVideoFileByTimeTool.process();
        int after = FileDAO.count();
        System.out.println("Type: " + Config.MOCKUP_TYPE + ". Number of File changed: " + (after-before));
        
    }
    
    

}
