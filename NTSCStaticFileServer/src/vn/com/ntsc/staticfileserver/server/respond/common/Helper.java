/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.common;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import vn.com.ntsc.staticfileserver.dao.impl.StickerCategoryDAO;
import vn.com.ntsc.staticfileserver.dao.impl.StickerDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.dao.impl.NewsBannerDAO;
import vn.com.ntsc.staticfileserver.dao.impl.GiftDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.util.DateFormat;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import eu.medsea.mimeutil.MimeUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import eazycommon.constant.API;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiDAO;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.entity.impl.DownloadStickerData;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.entity.impl.FileUrl;
import vn.com.ntsc.staticfileserver.gabagecollector.GabageFile;
import vn.com.ntsc.staticfileserver.gabagecollector.GabageManager;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.videocollector.VideoCollector;

/**
 *
 * @author RuAc0n
 */
public class Helper {

    private static final String UnknowErrorMessage;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
        UnknowErrorMessage = obj.toJSONString();
    }

    public static byte[] getInputArrayByte(Request request) {
        InputStream in = request.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] result = null;
        if (in == null) {
            return null;
        }
        try {
//            byte[] buffer = new byte[1];
//            while ((in.read(buffer, 0, 1)) != -1) {
//                bos.write(buffer);
//            }
            byte[] buf = new byte[4096];
            while (true) {
                int n = in.read(buf);
                if (n < 0) {
                    break;
                }
                bos.write(buf, 0, n);
            }
            bos.flush();
            bos.close();
            result = bos.toByteArray();
        } catch (IOException ex) {
            Util.addErrorLog(ex);
            result = null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = null;
        }
        return result;
    }

    public static byte[] getInputArrayByte(InputStream in) {
//        InputStream in = request.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] result = null;
        if (in == null) {
            return null;
        }
        try {
//            byte[] buffer = new byte[1];
//            while ((in.read(buffer, 0, 1)) != -1) {
//                bos.write(buffer);
//            }
            byte[] buf = new byte[4096];
            while (true) {
                int n = in.read(buf);
                if (n < 0) {
                    break;
                }
                bos.write(buf, 0, n);
            }
            bos.flush();
            bos.close();
            result = bos.toByteArray();
        } catch (IOException ex) {
            Util.addErrorLog(ex);
            result = null;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = null;
        }
        return result;
    }

    public static String createUrlPath(boolean isThumbnail, String fileType) {
        String urlPath = "";
        String filename = UUID.randomUUID().toString() + "." + fileType;
        String dateString = DateFormat.format(Util.getGMTTime());
        StringBuilder monthYear = new StringBuilder();
        monthYear.append(dateString.substring(0, 4)).append(dateString.substring(4, 6));
//        String day = new SimpleDateFormat("dd").format(new Date());
        if (!isThumbnail) {
            urlPath = monthYear.toString() + File.separator + dateString.subSequence(6, 8) + File.separator + filename;
        } else {
            urlPath = monthYear.toString() + File.separator + dateString.subSequence(6, 8) + File.separator + filename;
        }
        return urlPath;

    }

    public static void writeFile(String urlImage, byte[] arrByteInput, String path,Date time) throws FileNotFoundException, IOException {
        File file = new File(path + urlImage);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream boss = new BufferedOutputStream(fos);
        boss.write(arrByteInput);
        boss.flush();
        boss.close();
    }
    
    public static void writeFile(String urlImage, BufferedImage bi, String path,Date time, String fileType) throws FileNotFoundException, IOException {
        File file = new File(path + urlImage);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        
        ImageIO.write(bi, fileType, file);
    }

    public static boolean isGifImage(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return true;
        }
        ImageReader is = ImageIO.getImageReadersBySuffix("GIF").next();
        ImageInputStream iis;
        int images = 0;
        try {
            iis = ImageIO.createImageInputStream(file);
            is.setInput(iis);
            images = is.getNumImages(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images <= 1;
    }

    public static void createImage(String urlImage, BufferedImage bi, Date time) throws EazyException{
        try{
            File destFile = new File(FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + urlImage);
            destFile.getParentFile().mkdirs();
            ImageIO.write(bi, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, destFile);
        }catch( Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ex);
        }
    }

    public static void createThumbnailImage(String urlImage, String urlThumbnail, Date time) throws EazyException {
        try {
            BufferedImage img = ImageIO.read(new File(FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + urlImage));
            Util.addDebugLog("urlImage " + urlImage);
            Util.addDebugLog("img " + img);
            double scale = (double) img.getWidth(null) / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            int height = new Double(img.getHeight(null) / scale).intValue();
            Image newImg = img.getScaledInstance(Constant.THUMBNAIL_WIDTH_SIZE, height, Image.SCALE_SMOOTH);
            BufferedImage bsi = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bsi.getGraphics().drawImage(newImg, 0, 0, null);
            File destFile = new File(FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE_FOLDER + urlThumbnail);
            destFile.getParentFile().mkdirs();
            ImageIO.write(bsi, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, destFile);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex);
        }
    }

    public static void createThumbnailImage(BufferedImage img, String urlThumbnail, Date time) throws EazyException {
        try {
            double scale = (double) img.getWidth(null) / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            int height = new Double(img.getHeight(null) / scale).intValue();
            Image newImg = img.getScaledInstance(Constant.THUMBNAIL_WIDTH_SIZE, height, Image.SCALE_SMOOTH);
            BufferedImage bsi = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bsi.getGraphics().drawImage(newImg, 0, 0, null);
            File destFile = new File(FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE_FOLDER + urlThumbnail);
            destFile.getParentFile().mkdirs();
            ImageIO.write(bsi, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, destFile);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex);
        }
    }


    public static void createThumbnailVideo(BufferedImage img, String urlThumbnail, Date time) throws EazyException {
        try {
            //for JDK (jcodec-javase)
//            BufferedImage img = AWTUtil.toBufferedImage(picture);

//            Util.addDebugLog("img " + img);
            double scale = (double) img.getWidth(null) / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            int height = new Double(img.getHeight(null) / scale).intValue();
            Image newImg = img.getScaledInstance(Constant.THUMBNAIL_WIDTH_SIZE, height, Image.SCALE_SMOOTH);
            BufferedImage bsi = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bsi.getGraphics().drawImage(newImg, 0, 0, null);
            File destFile = new File(FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE_FOLDER + urlThumbnail);
            destFile.getParentFile().mkdirs();
            ImageIO.write(bsi, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, destFile);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ex);
        }
    }

    public static String confirmUploadImage(String token, String imageId, String imageCat, String ip, Date time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CONFIRM_UPLOAD_IMAGE);
        jsonObject.put("img_cat", imageCat);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.IMAGE_ID, imageId);
        jsonObject.put(ParamKey.IP, ip);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String confirmStreamingVideo(String token, String buzzId, String videoId, String videoCat, String ip, Date time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CONFIRM_STREAMING_VIDEO);
        jsonObject.put("video_cat", videoCat);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.VIDEO_ID, videoId);
        jsonObject.put(ParamKey.BUZZ_ID, buzzId);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.TIME, time.getTime());
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String confirmRecordingVideo(String token, String buzzId, String videoId, String videoCat, String ip, Date time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CONFIRM_RECORDING_VIDEO);
        jsonObject.put("video_cat", videoCat);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.VIDEO_ID, videoId);
        jsonObject.put(ParamKey.BUZZ_ID, buzzId);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.TIME, time.getTime());
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String confirmUploadVideo(String token, String buzzId, String videoId, String videoCat, String ip, Date time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CONFIRM_UPLOAD_VIDEO);
        jsonObject.put("video_cat", videoCat);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.VIDEO_ID, videoId);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.TIME, time.getTime());
        jsonObject.put(ParamKey.BUZZ_ID, buzzId);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

     public static String confirmUploadAudio(String token, String audioId, String audioCat, String ip, Date time,String coverId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CONFIRM_UPLOAD_AUDIO);
        jsonObject.put("audio_cat", audioCat);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.AUDIO_ID, audioId);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.TIME, time.getTime());
        jsonObject.put(ParamKey.COVER_ID, coverId);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String addStatus(String token, String buzzVal, Integer buzzType, String ip, Date time, List<JSONObject> imgList, List<JSONObject> vidList, List<JSONObject> audioList, String buzzTag, Integer privacy, String streamId, String shareId,Integer buzzTypeOtherShare, Integer region){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_BUZZ);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.BUZZ_VALUE, buzzVal);
        jsonObject.put(ParamKey.BUZZ_TYPE, buzzType);
        jsonObject.put(ParamKey.BUZZ_TYPE_OTHER_SHARE, buzzTypeOtherShare);
        jsonObject.put(ParamKey.IMG_LIST, imgList);
        jsonObject.put(ParamKey.VID_LIST, vidList);
        jsonObject.put(ParamKey.AUDIO_LIST, audioList);
        jsonObject.put(ParamKey.TAG_LIST, buzzTag);
        jsonObject.put(ParamKey.PRIVACY, privacy);
        jsonObject.put(ParamKey.STREAM_ID, streamId);
        jsonObject.put(ParamKey.SHARE_ID, shareId);
        jsonObject.put(ParamKey.BUZZ_REGION, region);
        String inputString = jsonObject.toJSONString();
        Util.addDebugLog("inputString "+inputString);
        String result = requestAndG(inputString);
        return result;
    }

    public static String uploadStreamFile(String token, String buzzId, String ip, Date time, List<JSONObject> vidList, Integer privacy, Integer viewNumber, Integer currentView, Integer duration, String status){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.UPLOAD_STREAM_FILE);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.BUZZ_ID, buzzId);
        jsonObject.put(ParamKey.VID_LIST, vidList);
        jsonObject.put(ParamKey.PRIVACY, privacy);
        jsonObject.put(ParamKey.VIEW_NUMBER, viewNumber);
        jsonObject.put(ParamKey.CURRENT_VIEW, currentView);
        jsonObject.put(ParamKey.DURATION, duration);
        jsonObject.put(ParamKey.STREAM_STATUS, status);
        String inputString = jsonObject.toJSONString();
        Util.addDebugLog("inputString "+inputString);
        String result = requestAndG(inputString);
        return result;
    }

    public static String addBuzz(String token, String imageId, String ip, int isApp, Date time) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_BUZZ);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.IP, ip);
        jsonObject.put(ParamKey.BUZZ_VALUE, imageId);
        jsonObject.put(ParamKey.BUZZ_TYPE, Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ);
        jsonObject.put(ParamKey.IS_APPROVED_IMAGE, isApp);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String getBuzzDetail(String token, String buzzId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.GET_BUZZ_DETAIL);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.BUZZ_ID, buzzId);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String checkAlbumOwned(String token, String albumId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.CHECK_ALBUM_OWNED);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.ALBUM_ID, albumId);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String addAlbumImage(String token, String albumId, List<String> listImage){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_ALBUM_IMAGE);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.ALBUM_ID, albumId);
        jsonObject.put(ParamKey.LIST_IMAGE, listImage);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String addAlbum(String token, String albumName, String albumDes, Integer privacy){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_ALBUM);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.ALBUM_NAME, albumName);
        jsonObject.put(ParamKey.ALBUM_DES, albumDes);
        jsonObject.put(ParamKey.PRIVACY, privacy);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String addEmojiCategory(String token, String vnName, String vnDes, String enName, String enDes, String fileId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_EMOJI_CAT);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.VIET_NAME, vnName);
        jsonObject.put(ParamKey.VIET_DESCRIPTION, vnDes);
        jsonObject.put(ParamKey.ENGLISH_NAME, enName);
        jsonObject.put(ParamKey.ENGLISH_DESCRIPTION, enDes);
        jsonObject.put(ParamKey.FILE_ID, fileId);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String editEmojiCategory(String token, String id, String vnName, String vnDes, String enName, String enDes, String fileId, Integer flag){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.EDIT_EMOJI_CAT);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.ID, id);
        jsonObject.put(ParamKey.VIET_NAME, vnName);
        jsonObject.put(ParamKey.VIET_DESCRIPTION, vnDes);
        jsonObject.put(ParamKey.ENGLISH_NAME, enName);
        jsonObject.put(ParamKey.ENGLISH_DESCRIPTION, enDes);
        jsonObject.put(ParamKey.FILE_ID, fileId);
        jsonObject.put(ParamKey.FLAG, flag);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String addEmoji(String token, String categoryId, String code, String name, String fileId, String fileType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.ADD_EMOJI);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.CATEGORY_ID, categoryId);
        jsonObject.put(ParamKey.EMOJI_CODE, code);
        jsonObject.put(ParamKey.EMOJI_NAME, name);
        jsonObject.put(ParamKey.FILE_ID, fileId);
        jsonObject.put(ParamKey.FILE_TYPE, fileType);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String editEmoji(String token, String emojiId, String categoryId, String code, String name, String fileId, String fileType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, API.EDIT_EMOJI);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        jsonObject.put(ParamKey.ID, emojiId);
        jsonObject.put(ParamKey.CATEGORY_ID, categoryId);
        jsonObject.put(ParamKey.EMOJI_CODE, code);
        jsonObject.put(ParamKey.EMOJI_NAME, name);
        jsonObject.put(ParamKey.FILE_ID, fileId);
        jsonObject.put(ParamKey.FILE_TYPE, fileType);
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static String requestAndG(String inputString) {
        String result = " ";
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + Config.ANDG_IP + ":" + Config.ANDG_PORT + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(inputString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);
                return UnknowErrorMessage;
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return UnknowErrorMessage;
        }
        return result;

    }

    public static boolean checkImage(String imageId, String token) throws ParseException {
        boolean result = false;
        JSONObject requestAndG = new JSONObject();
        requestAndG.put(ParamKey.IMAGE_ID, imageId);
        requestAndG.put(ParamKey.API_NAME, API.CHECK_IMAGE);
        requestAndG.put(ParamKey.TOKEN_STRING, token);
        String andGrespond = requestAndG(requestAndG.toJSONString());
        Long code = (Long) ((JSONObject) (new JSONParser().parse(andGrespond))).get(ParamKey.ERROR_CODE);
        if (code == ErrorCode.SUCCESS) {
            result = true;
        }
        return result;
    }

    public static String getImageUrl(String imageId, String imageKind) throws EazyException {
        StringBuilder result = new StringBuilder();
        if (imageKind.equals(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE)) {
            String url = ImageDAO.getImageUrl(imageId);
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER).append(url);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.THUMBNAIL)) {
            String url = ThumbnailDAO.getThumbnailUrl(imageId);
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE_FOLDER).append(url);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.STICKER)) {
            String url = StickerDAO.getStickerURL(Long.parseLong(imageId));
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.STICKERS_FOLDER).append(url);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.GIFT)) {
            String url = GiftDAO.getGiftURL(imageId);
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.GIFTS_FOLDER).append(url);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.STICKER_CATEGORY)) {
            String url = StickerCategoryDAO.getCategoryAvaPath(imageId);
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.AVATAR_STICKER_CATEGORY_FOLDER).append(url);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.NEWS_BANNER)) {
            String url = NewsBannerDAO.getURL(imageId);
            if (url == null) {
                return null;
            }
            result.append(FilesAndFolders.FOLDERS.NEWS_BANNER_FOLDER).append(url);
        } else {
            return null;
        }
        return result.toString();
    }

    public static Map<String, String> getListFileUrl(List<String> listFileId, String imageKind) throws EazyException {
        Map<String, String> mapUrl = new HashMap<>();
        if (imageKind.equals(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE)) {
            mapUrl = ImageDAO.getImageUrl(listFileId);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.THUMBNAIL)) {
            mapUrl = ThumbnailDAO.getThumbnailUrl(listFileId);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.GIFT)) {
            mapUrl = GiftDAO.getGiftUrl(listFileId);
        }
        return mapUrl;
    }

    //add by Huy201709Oct
    public static Map<String, String> getListFileUrlWithInfo(List<String> listFileId, String imageKind) throws EazyException {
        Map<String, String> mapUrl = new HashMap<>();
        if (imageKind.equals(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE)) {
            mapUrl = ImageDAO.getImageUrlWithInfo(listFileId);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.THUMBNAIL)) {
            mapUrl = ThumbnailDAO.getThumbnailUrlWithInfo(listFileId);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.GIFT)) {
            mapUrl = GiftDAO.getGiftUrl(listFileId);
        }
        return mapUrl;
    }
    public static Map<String, String> getListVideoUrlWithInfo(List<String> listVideoId) {
        Map<String, String> mapUrl = FileDAO.getFileUrlWithInfo(listVideoId);
        return mapUrl;
    }
    public static Map<String, String> getListStreamUrlWithInfo(List<String> listFileId, String imageKind) throws EazyException {
        Map<String, String> mapUrl = new HashMap<>();
        if (imageKind.equals(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE)) {
            mapUrl = ImageDAO.getStreamImageUrlWithInfo(listFileId);
        } else if (imageKind.equals(Constant.IMAGE_KIND_VALUE.THUMBNAIL)) {
            mapUrl = ThumbnailDAO.getStreamThumbnailUrlWithInfo(listFileId);
        }
        return mapUrl;
    }
    public static Map<String, String> getListStreamUrlWithInfo(List<String> listVideoId) {
        Map<String, String> mapUrl = FileDAO.getStreamUrlWithInfo(listVideoId);
        return mapUrl;
    }

    public static Map<String, String> getListVideoUrl(List<String> listVideoId) {
        Map<String, String> mapUrl = FileDAO.getFileUrl(listVideoId);
        return mapUrl;
    }

    public static byte[] getFile(String url) {
        byte[] result = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            BufferedInputStream bis = new BufferedInputStream(fileInputStream);
            int bytesAvailable = bis.available();
            byte[] buffer = new byte[bytesAvailable];
            bis.read(buffer, 0, bytesAvailable);
            bis.close();
            fileInputStream.close();
            result = buffer;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = UnknowErrorMessage.getBytes();
        }
        return result;

    }

    public static String getListSticker(String token, String stickerCat, String api) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.API_NAME, api);
        jsonObject.put(ParamKey.TOKEN_STRING, token);
        if (stickerCat != null) {
            jsonObject.put("sticker_cat_id", stickerCat);
        }
        String inputString = jsonObject.toJSONString();
        String result = requestAndG(inputString);
        return result;
    }

    public static Respond createDataToDownSticker(JSONObject data) throws EazyException, Exception {
        String stickerCat;
        JSONObject dataObj = (JSONObject) data.get(ParamKey.DATA);
        List<String> listCode = Util.getListString(dataObj, ParamKey.LIST);
        stickerCat = Util.getStringParam(dataObj, ParamKey.CATEGORY_ID);
        Long tradablePoint = Util.getLongParam(dataObj, "tradable_point");
        Long untradablePoint = Util.getLongParam(dataObj, "untradable_point");
        Map<String, String> mapSticker = StickerDAO.getMapSticker(listCode);
        String fileString = null;
        if (!mapSticker.isEmpty()) {
            String file = FilesAndFolders.FOLDERS.STICKER_ZIP_FOLDER_ + UUID.randomUUID().toString() + ".zip";
            String catAva = StickerCategoryDAO.getCategoryAvaPath(stickerCat);
            if (catAva != null) {
                String path = FilesAndFolders.FOLDERS.AVATAR_STICKER_CATEGORY_FOLDER + catAva;
                mapSticker.put(stickerCat, path);
            }
            Util.createZipFile(file, mapSticker, FilesAndFolders.EXTENSIONS.STAMP_EXTENSION);
            byte[] arrayByte = Util.getFile(file);
            fileString = Base64.encode(arrayByte);
//            Util.deleteFile(file);
            // deleted file after some minutes
            GabageManager.add(new GabageFile(file));
        }
        EntityRespond result = new EntityRespond();
        result.code = ErrorCode.SUCCESS;
        result.data = new DownloadStickerData(fileString, stickerCat, listCode, tradablePoint, untradablePoint);
        return result;
    }

    public static boolean isVideoFile(String fileName) {
        boolean result = false;
        try {
            MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
            File f = new File(fileName);
            Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);
            if (mimeTypes != null && !mimeTypes.isEmpty()) {
                for (Object obj : mimeTypes) {
                    String extension = obj.toString().toLowerCase();
                    if (extension.contains("video")) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

//    public static final String FFMPEG_COMMAND_FORMAT = "ffmpeg -i %s -s qcif -vcodec h264 -acodec aac -ac 1 -ar 8000 -r 25 -ab 32 -y -strict -2 %s";
    //public static final String FFMPEG_COMMAND_FORMAT = "ffmpeg -i %s -s 480x320 -c:a copy %s";
    //public static final String FFMPEG_COMMAND_FORMAT = "ffmpeg -i %s -vf scale=480:trunc(ow/a/2)*2 -c:a copy %s";
    public static final String FFMPEG_COMMAND_FORMAT = "ffmpeg -i %s -vf scale=480:trunc(ow/a/2)*2 -c:a aac %s";

    public static void generateVideoFile(String inputFile, String outputFile) {
        try {
            String command = String.format(FFMPEG_COMMAND_FORMAT, inputFile, outputFile);
            Util.addInfoLog("generateVideoFile=========FFMPEG_COMMAND_FORMAT" + FFMPEG_COMMAND_FORMAT);
            Util.addInfoLog("generateVideoFile=========command" + command);
            Process pr = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            while (br.readLine() != null) {
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void returnFailedUploadPoint(String messageId, String ip) {

        //Send request to UMS to return failed upload point.
        JSONObject jo1 = new JSONObject();
        jo1.put(ParamKey.API_NAME, API.RETURN_FAILED_UPLOAD_POINT);
        jo1.put(ParamKey.MESSAGE_ID, messageId);
        jo1.put(ParamKey.IP, ip);

        String request = jo1.toJSONString();
        Util.sendRequest(request, Config.UMSServer_IP, Config.UMSServer_Port);

        //Send request to Chat to delete document by this message id
        JSONObject jo2 = new JSONObject();
        jo2.put(ParamKey.API_NAME, API.REMOVE_FAILED_UPLOAD_MESSAGE);
        jo2.put(ParamKey.MESSAGE_ID, messageId);
        request = jo2.toJSONString();
        Util.sendRequest(request, Config.ChatServer_IP, Config.ChatServer_Port);

    }

    public static FileData generateEmoji(byte[] arrayData, String fileType, Date time) throws EazyException{
        FileData result = new FileData();
        try {
            String imageUrl = createUrlPath(false, fileType);
            writeFile(imageUrl, arrayData, FilesAndFolders.FOLDERS.EMOJI_FOLDER,time);

            String imageId = EmojiDAO.insertEmoji(imageUrl);

            result = new FileData(imageId, imageUrl);

        } catch (IOException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static FileData generateEmoji(byte[] arrayData, String catId, String fileName, String fileType, Date time) throws EazyException{
        FileData result = new FileData();
        try {
            fileName = fileName.replaceAll("\\s+","");
            String imageUrl = catId + File.separator + fileName + "." + fileType;

            writeFile(imageUrl, arrayData, FilesAndFolders.FOLDERS.EMOJI_FOLDER,time);

            String imageId = EmojiDAO.insertEmoji(imageUrl);

            result = new FileData(imageId, imageUrl);
            result.fileType = fileType;

        } catch (IOException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static FileData generateImage(byte[] arrayData, String fileType, Date time) throws EazyException{
        FileData result = new FileData();
        try {
            String imageUrl = createUrlPath(false, fileType);

            BufferedImage img = ImageIO.read(new ByteArrayInputStream(arrayData));
            
            Integer orientation = getOrientation(new ByteArrayInputStream(arrayData));
            Util.addDebugLog("orientation========"+orientation);
            if(orientation != 0){
                img = rotateImage(orientation, img);
            }
            
            writeFile(imageUrl, img, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time,fileType);

            Double imgWidth = new Double(img.getWidth());
            Double imgHeight = new Double(img.getHeight());

            String imageId = ImageDAO.insertImagewithLengthWidth(imageUrl, imgWidth,imgHeight);

            Double widthThumnail = (double) imgWidth / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            Double heightThumnail = (double) imgHeight / (double) Constant.THUMBNAIL_WIDTH_SIZE;

            String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            //createThumbnailImage(imageUrl, thumbnailUrl, time);
            createThumbnailImage(img, thumbnailUrl, time);
            ThumbnailDAO.insertThumbnailWithWidthHeight(imageId, thumbnailUrl, widthThumnail, heightThumnail);

            result = new FileData(imageId, thumbnailUrl, widthThumnail, heightThumnail, imageUrl, imgWidth, imgHeight);

        } catch (Exception ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static FileData generateVideo(byte[] arrayData, String fileType, Date time) throws EazyException{
        FileData result = new FileData();
        try {
            String fileUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.VIDEO_MP4_EXTENSION);
            writeFile(fileUrl, arrayData, FilesAndFolders.FOLDERS.FILES_FOLDER,time);

            getVideoAtt(new File(FilesAndFolders.FOLDERS.FILES_FOLDER+fileUrl));
//            Integer aa = getOrientation(new File(FilesAndFolders.FOLDERS.FILES_FOLDER+fileUrl));
//            Util.addDebugLog("Orientation======"+aa);

            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File(filePath));
            grabber.start();
            Long fileDuration = grabber.getLengthInTime();
            Frame frame = grabber.grabImage();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage img = converter.getBufferedImage(frame);
            //get rotate
            Integer orientation = convertOrientation(grabber.getVideoMetadata("rotate"));
            if(orientation != 0){
                img = rotateImage(orientation, img);
            }
            grabber.stop();

            Double width = new Double(img.getWidth());
            Double height = new Double(img.getHeight());
            double widthThumnail = (double) width / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            Double heightThumnail = (double) height / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            Integer fileLength = fileDuration.intValue() / 1000000;

            String fileId = FileDAO.insertFile(fileUrl,width,height,fileLength);
            VideoCollector.put(fileId, new FileInfo(fileUrl, System.currentTimeMillis()));

            String originUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            createImage(originUrl, img, time);
            ImageDAO.insertVideoImage(fileId, originUrl, width, height);

            String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            createThumbnailVideo(img, thumbnailUrl, time);
            ThumbnailDAO.insertThumbnailWithWidthHeight(fileId, thumbnailUrl,widthThumnail,heightThumnail);

            result = new FileData(fileId, fileUrl, thumbnailUrl, widthThumnail, heightThumnail, originUrl, width, height, fileLength);

        } catch (IOException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static FileData generateAudio(byte[] arrayData, String fileType, Date time){
        FileData result = new FileData();
        try {
            String fileUrl = Helper.createUrlPath(false, fileType);
            writeFile(fileUrl, arrayData, FilesAndFolders.FOLDERS.FILES_FOLDER,time);

            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;
            AudioFile f = AudioFileIO.read(new File(filePath));
            Integer fileDuration = f.getAudioHeader().getTrackLength();
            Tag tag = f.getTag();
            Artwork artwork = tag.getFirstArtwork();

            String fileId = FileDAO.insertFile(fileUrl,0.0,0.0,fileDuration);
            if(artwork != null){
                byte[] imageRawData = artwork.getBinaryData();
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageRawData));
                Double imgWidth = new Double(img.getWidth(null));
                Double imgHeight = new Double(img.getHeight(null));
                String imageUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
                Helper.writeFile(imageUrl, imageRawData, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time);
                String imageId = ImageDAO.insertImagewithLengthWidth(imageUrl, imgWidth,imgHeight);
                result = new FileData("audio", fileId, fileUrl, imageUrl, imgWidth, imgHeight, imageUrl, imgWidth, imgHeight, fileDuration);
            }else{
                String imageId = Setting.default_audio_img;
                FileData imageData = ImageDAO.getImageData(imageId);
                result = new FileData("audio", fileId, fileUrl, imageData.originalUrl, imageData.originalWidth, imageData.originalHeight, imageData.originalUrl, imageData.originalWidth, imageData.originalHeight, fileDuration);
            }

        } catch (Exception ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            Util.addErrorLog(ex);
        }

        return result;
    }

    public static Integer getOrientation(File file){
        Integer result = 0;
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (exifIFD0Directory != null) {
                result = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }catch(ImageProcessingException | MetadataException | IOException ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static Integer getOrientation(InputStream inputStream){
        Integer result = 0;
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (exifIFD0Directory != null) {
                result = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }catch(ImageProcessingException | MetadataException | IOException ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static Integer convertOrientation(String value){
        Util.addDebugLog("convertOrientation==="+value);
        Integer degree = 0;
        Integer result = 0;
        if(value != null && !value.equals("")){
            degree = Integer.valueOf(value);
        }

        switch(degree){
            case 90:
                result = 6;
                break;
            default:
                break;
        }
        return result;
    }

    public static BufferedImage rotateImage(Integer orientation, BufferedImage img){
        BufferedImage result = null;
        try{
            switch (orientation) {
                case 1:
                    result = img;
                    break;
                case 2: // Flip X
                    result = Scalr.rotate(img, Rotation.FLIP_HORZ);
                    break;
                case 3: // PI rotation
                    result = Scalr.rotate(img, Rotation.CW_180);
                    break;
                case 4: // Flip Y
                    result = Scalr.rotate(img, Rotation.FLIP_VERT);
                    break;
                case 5: // - PI/2 and Flip X
                    result = Scalr.rotate(img, Rotation.CW_90);
                    result = Scalr.rotate(img, Rotation.FLIP_HORZ);
                    break;
                case 6: // -PI/2 and -width
                    result = Scalr.rotate(img, Rotation.CW_90);
                    break;
                case 7: // PI/2 and Flip
                    result = Scalr.rotate(img, Rotation.CW_90);
                    result = Scalr.rotate(img, Rotation.FLIP_VERT);
                    break;
                case 8: // PI / 2
                    result = Scalr.rotate(img, Rotation.CW_270);
                    break;
                default:
                    break;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static void getVideoAtt(File file){
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {

                for(com.drew.metadata.Tag item: directory.getTags()){
                    Util.addDebugLog("========="+directory.getName()+" - "+ item.getTagName()+" - "+item.getDescription());
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        Util.addDebugLog("ERROR: "+ error);
                    }
                }
            }
        }catch(ImageProcessingException | IOException ex){
            Util.addErrorLog(ex);
        }
    }

    public static void getAtt(String url){
        try{
            Path file = Paths.get(url);
            UserDefinedFileAttributeView att = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
            for(String item: att.list()){
                Util.addDebugLog(item);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }

    public static String getExtension(String fileName){
        String result = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            result = fileName.substring(i+1);
        }
        return result;
    }
}
