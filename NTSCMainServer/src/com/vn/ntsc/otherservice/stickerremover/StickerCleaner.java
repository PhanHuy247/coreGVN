/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.stickerremover;

import eazycommon.constant.Constant;
import com.vn.ntsc.dao.impl.UserStickerDAO;

/**
 *
 * @author tuannxv00804
 */
public class StickerCleaner extends Thread {

    public static void startStickerCleaner() {
        StickerCleaner nc = new StickerCleaner();
        nc.start();
    }

    @Override
    public void run() {
        while (true) {
            UserStickerDAO.removeSticker();
            try {
                Thread.sleep(Constant.FIVE_MINUTES);
            } catch (InterruptedException ex) {
            }
        }
    }
}
