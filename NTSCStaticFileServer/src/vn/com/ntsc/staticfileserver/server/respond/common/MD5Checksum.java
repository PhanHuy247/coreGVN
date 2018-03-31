/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import eazycommon.util.Util;

/**
 *
 * @author Rua
 */
public class MD5Checksum {

    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

   // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(String filename) {
        String result = null;
        try{
            byte[] b = createChecksum(filename);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                builder.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
            }
            result = builder.toString();
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

}
