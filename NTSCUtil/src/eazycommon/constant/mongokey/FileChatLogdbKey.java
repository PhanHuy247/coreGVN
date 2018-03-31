/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant.mongokey;

/**
 *
 * @author Phan Huy
 */
public class FileChatLogdbKey {
    public static String DB_NAME = "filechatlog";
    
    public static class Log {

        public static final String Field_MsgID = "msgid";
        public static final String Field_From = "from";
        public static final String Field_To = "to";
        public static final String Field_MsgType = "type";
        public static final String Field_OriginTime = "ot";
        public static final String Field_ServerTime = "st";
        public static final String Field_IsDel = "del";
        public static final String Field_ThumbnailUrl = "thumbnail_url";
        public static final String Field_OriginalUrl = "original_url";
        public static final String Field_MsgFileId = "msg_file_id";
        public static final String Field_Duration = "file_duration";
        public static final String Field_FileUrl = "file_url";
    }
}
