/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon.constant;

import eazycommon.util.Util;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rua
 */
public class FilesAndFolders {
    
    public static class FOLDERS{
        
        public static final String ORIGINAL_IMAGE_FOLDER;
        public static final String THUMBNAIL_IMAGE_FOLDER;
        public static final String FILES_FOLDER;
        public static final String EMOJI_FOLDER;
        
        public static final String ORIGINAL_IMAGE;
        public static final String THUMBNAIL_IMAGE;
        public static final String FILE;
        public static final String GIFT;
        public static final String STICKER_IMAGE;
        public static final String STICKER_CATEGORY_IMAGE;
        public static final String EMOJI_IMAGE;

        public static final String STICKERS_FOLDER;
        public static final String GIFTS_FOLDER;    
        public static final String CSV_FILES_FOLDER;

        public static final String STICKER_ZIP_FOLDER_;
        public static final String AVATAR_STICKER_CATEGORY_FOLDER;
        public static final String NEWS_BANNER_FOLDER;
        public static final String STICKER_CATEGORY_FOLDER;
        public static final String GIFT_CATEGORY_FOLDER;
        
        static{
            if (Util.isWindows()) {
                STICKER_ZIP_FOLDER_ = "C:\\sticker\\zip\\";
                AVATAR_STICKER_CATEGORY_FOLDER = "C:\\sticker\\category_avatar\\";
                NEWS_BANNER_FOLDER = "C:\\news_banner\\";
                STICKER_CATEGORY_FOLDER = "C:\\sticker\\";
                GIFT_CATEGORY_FOLDER = "C:\\gift\\";
                ORIGINAL_IMAGE_FOLDER =  "C:\\image\\original_image\\";
                THUMBNAIL_IMAGE_FOLDER = "C:\\image\\thumbnail\\";
                EMOJI_FOLDER = "C:\\emoji\\";
                FILES_FOLDER = "C:\\file\\";
                STICKERS_FOLDER = "C:\\sticker\\sticker\\";
                GIFTS_FOLDER = "C:\\gift\\"; 
                CSV_FILES_FOLDER = "C:\\eazy\\backend\\";
                
                ORIGINAL_IMAGE = "image\\original_image\\";  
                THUMBNAIL_IMAGE = "image\\thumbnail\\";
                STICKER_IMAGE = "sticker\\sticker\\";
                STICKER_CATEGORY_IMAGE = "sticker\\category_avatar\\";
                FILE = "file\\";
                GIFT = "gift\\";
                EMOJI_IMAGE = "emoji\\";
            } else {
                STICKER_ZIP_FOLDER_ = "/opt/sticker/zip/";
                AVATAR_STICKER_CATEGORY_FOLDER = "/opt/sticker/category_avatar/";
                STICKER_CATEGORY_FOLDER = "/opt/sticker/";
                GIFT_CATEGORY_FOLDER = "/opt/gift/";
                ORIGINAL_IMAGE_FOLDER =  "/opt/image/original_image/";
                THUMBNAIL_IMAGE_FOLDER = "/opt/image/thumbnail/";
                FILES_FOLDER = "/opt/file/";
                EMOJI_FOLDER = "/opt/emoji/";
                STICKERS_FOLDER = "/opt/sticker/sticker/";
                GIFTS_FOLDER = "/opt/gift/";            
                NEWS_BANNER_FOLDER = "/opt/news_banner/";  
                CSV_FILES_FOLDER = "/var/www/html/eazy/";
                
                ORIGINAL_IMAGE = "image/original_image/";  
                THUMBNAIL_IMAGE = "image/thumbnail/";
                STICKER_IMAGE = "sticker/sticker/";
                STICKER_CATEGORY_IMAGE = "sticker/category_avatar/";
                FILE = "file/";
                GIFT = "gift/";
                EMOJI_IMAGE = "emoji/";
            }
        }
    }
    
    public static class FILES{
        
        public static final String JAPANESE_HEADER_FILE = "JapaneseHeaders.properties";
        public static final String ENGLISH_HEADER_FILE = "EnglishHeaders.properties";
        public static final String TRANSLATE_FILE = "Translate.properties";
        public static List<String> LIST_HEADER_FILE = Arrays.asList(JAPANESE_HEADER_FILE, ENGLISH_HEADER_FILE);
    
        public static final String TERM_OF_USE_FILE;
        public static final String PRIVACY_POLICY_FILE;
        public static final String SAFETY_TIPS_FILE;
        
        static {
            if (Util.isWindows()) {
                TERM_OF_USE_FILE = "C:\\jambo\\main\\term.txt";
                PRIVACY_POLICY_FILE = "C:\\jambo\\main\\policy.txt";
                SAFETY_TIPS_FILE = "C:\\jambo\\main\\tips.txt";
            } else {
                TERM_OF_USE_FILE = "term.txt";
                PRIVACY_POLICY_FILE = "policy.txt";
                SAFETY_TIPS_FILE = "tips.txt";
            }
        }
    }
    

    public static class EXTENSIONS{
        
        public static final String STAMP_EXTENSION = ".png";
        public static final String IMAGE_JPG_EXTENSION = "jpg";
    //    public static final String FILE_EXTENSION = "sh";
        public static final String FILE_EXTENSION = "mp4";
        public static final String CSV_FILE_EXTENSION = ".csv";
        public static final String IMAGE_PNG_EXTENSION = ".png";
        public static final String VIDEO_MP4_EXTENSION = "mp4";
        public static final String AUDIO_MP3_EXTENSION = "mp3";
    }
    

}
