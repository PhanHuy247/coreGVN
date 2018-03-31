/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;

/**
 *
 * @author duyetpt
 */
public class Actor {

    public static String userId;
    public static String partnerId;
    public static List<String> listPartnerId;
    public static List<String> listBlack;

    static {
        listPartnerId = new ArrayList<String>();
        listBlack = new ArrayList<String>();
    }

    public static void resetUser() {
        userId = null;
        partnerId = null;
        listBlack = new ArrayList<String>();
        listPartnerId = new ArrayList<String>();
    }

    public static void removeUser() {
        if (userId != null) {
            Userdb.removeUserById(userId);
        }
        if (partnerId != null) {
            Userdb.removeUserById(partnerId);
        }
        if (!listPartnerId.isEmpty()) {
            for (String id : listPartnerId) {
                Userdb.removeUserById(id);
            }
        }
        if (!listBlack.isEmpty()) {
            for (String id : listPartnerId) {
                Userdb.removeUserById(id);
            }
        }
        resetUser();
    }

    public static void initCouple(int genderU, int genderP) throws IOException {
        userId = genderU == Constant.GENDER.FEMALE ? Userdb.registFemale() : genderU == Constant.GENDER.MALE ? Userdb.registMale() : null;
        partnerId = genderP == Constant.GENDER.FEMALE ? Userdb.registFemale() : genderP == Constant.GENDER.MALE ? Userdb.registMale() : null;
    }
}
