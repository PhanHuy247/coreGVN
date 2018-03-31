/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant.mongokey;

/**
 *
 * @author duyetpt
 */
public class ChatlogdbKey {

    public static String DB_NAME = "chatlog";
    public static String DB_EXTENSION = "chatlogextension";

    public static class Log {

        public static final String Field_MsgID = "msgid";
        public static final String Field_From = "from";
        public static final String Field_To = "to";
        public static final String Field_MsgType = "type";
        public static final String Field_Value = "value";
        public static final String Field_OriginTime = "ot";
        public static final String Field_ServerTime = "st";
        public static final String Field_DestTime = "dt";
        public static final String Field_IsDel = "del";

        public static final String Field_BlackList = "bl";
        public static final String ObjectID = "_id";
        public static final String Field_Ip = "ip";
    }
}
