/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author RuAc0n
 */
public class Request {

    private static final String msgPattern = "[0-9a-f]{24}&[0-9a-f]{24}&[0-9]{17}";
    static final Pattern pattern = Pattern.compile(msgPattern);

    public static final String tokenKey = ParamKey.TOKEN_STRING;
    private String token;
    public static final String apiNameKey = ParamKey.API_NAME;
    private String apiName;
    public static final String iamgeCatKey = "img_cat";
    private String imageCat;
    public static final String imageIdKey = ParamKey.IMAGE_ID;
    private String imageId;
    public static final String imageKindKey = "img_kind";
    private String imageKind;
    public static final String fileIdKey = "file_id";
    private String fileId;
    public static final String withSizeKey = "width_size";
    private String widthSize;
    public static final String stickerIdKey = "stk_id";
    private String stickerId;
    public static final String stickerCatIdKey = "sticker_cat_id";
    private String stickerCatId;
    private static final String giftIdKey = ParamKey.GIFT_ID;
    private String giftId;
    private static final String sumKey = "sum";
    private String sum;
//    private static final String messageIdKey= "msg_id";
    private String messageId;

//    private static final String stickerCodeKey = ParamKey.STICKER_CODE;
//    private String stickerCode;
    private String userId;
    private String ip;
    private InputStream inputStream;
    public static final String is_freeKey = "is_free";
    private int is_free = 1;
    public Collection<Part> parts;
    
    public static final String listImgIdKey = "list_img_id";
    private List<String> listImgId;
    public static final String listGiftIdKey = "list_gift_id";
    private List<String> listGiftId;
    public static final String listVideoIdKey = "list_video_id";
    private List<String> listVideoId;
    public static final String listCoverIdKey = "list_cover_id";
    private List<String> listCoverId;
    public static final String listAudioIdKey = "list_audio_id";
    private List<String> listAudioId;
    public static final String listStreamIdKey = "list_stream_id";
    private List<String> listStreamId;
    
    public static final String buzzIdKey = "buzz_id";
    public String buzzId;
    
    private static final String fileSizeKey = "max_file_size";
    public Integer fileSize;
    
    private static final String totalFileSizeKey = "total_file_size";
    public Integer totalFileSize;
    
    private static final String imageNumberKey = "max_image_number";
    public Integer imageNumber;
    
    private static final String videoNumberKey = "max_video_number";
    public Integer videoNumber;
    
    private static final String audioNumberKey = "max_audio_number";
    public Integer audioNumber;
    
    private static final String totalFileUploadKey = "total_file_upload";
    public Integer totalFileUpload;
    
    private static final String defaultAudioImgKey = "default_audio_img";
    public String defaultAudioImg;
    
    private static final String shareHasDeletedImgKey = "share_has_deleted_img";
    public String shareHasDeletedImg;
    
    private static final String fileUrlKey = "file_url";
    public String fileUrl;
    
    private static final String imageUrlKey = "image_url";
    public String imageUrl;
    
    private static final String thumbnailUrlKey = "thumbnail_url";
    public String thumbnailUrl;
    
    public static final String imageWidthKey = "image_width";
    public Double imageWidth;
    
    public static final String imageHeightKey = "image_height";
    public Double imageHeight;
    
    public static final String thumbnailWidthKey = "thumbnail_width";
    public Double thumbnailWidth;
    
    public static final String thumbnailHeightKey = "thumbnail_height";
    public Double thumbnailHeight;
    
    private static final String viewNumberKey = "view_number";
    public Integer viewNumber;
    
    private static final String currentViewKey = "current_view";
    public Integer currentView;
    
    private static final String durationKey = "duration";
    public Integer duration;
    
    private static final String statusKey = "status";
    public String status;
    
    private static final String maxLengthBuzzKey = "max_length_buzz";
    public Integer maxLengthBuzz;
    
    private static final String maxFilePerAlbumKey = "max_file_per_album";
    public Integer maxFilePerAlbum;
    
    private static final String categoryIdKey = "cat_id";
    public String catId;
    
    private static final String listCategoryKey = "list_cat";
    public HashMap<String, Integer> listCategory;

    public static Request initRequest(InputStream inputStream, String inputString, HttpServletRequest request) {
        Request r = new Request();
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(inputString);
            
            for (Object key : json.keySet()){
                Object value = json.get(key);
                r.initRequestInfor(key.toString(), value);
            }
//            
//            String api = (String) json.get(apiNameKey);
//            r.apiName = api;
//            String token = (String) json.get(tokenKey);
//            r.token = token;
//            r.setInputStream(inputStream);
//            r.ip = Util.getClientIpAddr(request);
//            if (json.get(is_freeKey) != null) {
//                r.is_free = (Integer) json.get(is_freeKey);
//            } else {
//                r.is_free = 1;
//            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
        return r;
    }

    public static Request initRequest(String inputString, InputStream inputStream, HttpServletRequest request) {
        Request r = new Request();

        //Specified case for getting msg_Id, because msg_Id's format is msg_id=54c87ffbe4b04c9cc83f3f3e&54c3ssssddb04c9cc83f3f3e&20160226105306259
        String msg_id = parse(inputString);
        if (!msg_id.isEmpty()) {
            r.messageId = msg_id;
        }

        try {
            String[] element = inputString.split("&");
            for (int i = 0; i < element.length; i++) {
                String temp = element[i];
                String[] map = temp.split("=");
                if (map.length == 2) {
                    r.initRequestInfor(map[0], map[1]);
                } else {
                    r.initRequestInfor(map[0], null);
                }
            }
            r.setInputStream(inputStream);
            //JSONParser parser = new JSONParser();
            //JSONObject json = (JSONObject) parser.parse(inputString);
            //r.is_free = (Integer) json.get(is_freeKey);
            r.ip = Util.getClientIpAddr(request);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            return null;
        }
        return r;
    }

    public void initRequestInfor(String key, Object value) {
        switch (key) {
            case tokenKey:
                this.setToken(value.toString());
                break;
            case apiNameKey:
                this.apiName = value.toString();
                break;
            case imageKindKey:
                this.setImageKind(value.toString());
                break;
            case imageIdKey:
                this.setImageId(value.toString());
                break;
            case iamgeCatKey:
                this.setImageCat(value.toString());
                break;
            case fileIdKey:
                this.setFileId(value.toString());
                break;
            case withSizeKey:
                this.setWidthSize(value.toString());
                break;
            case stickerIdKey:
                this.setStickerId(value.toString());
                break;
            case stickerCatIdKey:
                this.setStickerCatId(value.toString());
                break;
            case giftIdKey:
                this.setGiftId(value.toString());
                break;
            case sumKey:
                this.sum = value.toString();
                break;
            //HUNGDT add
            case is_freeKey:
                if (value != null) {
                    Util.addDebugLog("case is_freeKey 0 : " + value);
                    this.setIsFree(Integer.parseInt(value.toString()));
                } else {
                    Util.addDebugLog("case is_freeKey 1 : " + 1);
                    this.setIsFree(1);

                }
                break;
            case listImgIdKey:
                listImgId = new LinkedList<>();
                JSONArray listId1 = (JSONArray) value;
                for(Object id : listId1){
                    if(id != null){
                        listImgId.add(id.toString());
                    }
                }
                break;
            case listGiftIdKey:
                listGiftId = new LinkedList<>();
                JSONArray listId2 = (JSONArray) value;
                for(Object id : listId2){
                    listGiftId.add(id.toString());
                }
                break;
            case listVideoIdKey:
                listVideoId = new LinkedList<>();
                JSONArray listId3 = (JSONArray) value;
                for(Object id : listId3){
                    if(id != null){
                        listVideoId.add(id.toString());
                    }
                }
                break;
            case listCoverIdKey:
                listCoverId = new LinkedList<>();
                JSONArray listId4 = (JSONArray) value;
                for(Object id : listId4){
                    if(id != null){
                        listCoverId.add(id.toString());
                    }
                }
                break;
            case listAudioIdKey:
                listAudioId = new LinkedList<>();
                JSONArray listId5 = (JSONArray) value;
                for(Object id : listId5){
                    if(id != null){
                        listAudioId.add(id.toString());
                    }
                }
                break;
            case listStreamIdKey:
                listStreamId = new LinkedList<>();
                JSONArray listId6 = (JSONArray) value;
                for(Object id : listId6){
                    if(id != null){
                        listStreamId.add(id.toString());
                    }
                }
                break;
            case fileSizeKey:
                Integer maxFileSize = Integer.valueOf(value.toString());
                this.setFileSize(maxFileSize);
                break;
            case totalFileSizeKey:
                Integer maxTotalFileSize = Integer.valueOf(value.toString());
                this.setTotalFileSize(maxTotalFileSize);
                break;
            case imageNumberKey:
                Integer maxImageNumber = Integer.valueOf(value.toString());
                this.setImageNumber(maxImageNumber);
                break;
            case videoNumberKey:
                Integer maxVideoNumber = Integer.valueOf(value.toString());
                this.setVideoNumber(maxVideoNumber);
                break;
            case audioNumberKey:
                Integer maxAudioNumber = Integer.valueOf(value.toString());
                this.setAudioNumber(maxAudioNumber);
                break;
            case totalFileUploadKey:
                Integer maxTotalFileUpload = Integer.valueOf(value.toString());
                this.setTotalFileUpload(maxTotalFileUpload);
                break;
            case defaultAudioImgKey:
                String imgId = value.toString();
                this.setDefaultAudioImg(imgId);
                break;
            case shareHasDeletedImgKey:
                String shareImg = value.toString();
                this.setShareHasDeletedImg(shareImg);
                break;
            case fileUrlKey:
                this.fileUrl = value.toString();
                break;
            case imageUrlKey:
                this.imageUrl = value.toString();
                break;
            case thumbnailUrlKey:
                this.thumbnailUrl = value.toString();
                break;
            case buzzIdKey:
                this.buzzId = value.toString();
                break;
            case imageWidthKey:
                this.imageWidth = Double.valueOf(value.toString());
                break;
            case imageHeightKey:
                this.imageHeight = Double.valueOf(value.toString());
                break;
            case thumbnailWidthKey:
                this.thumbnailWidth = Double.valueOf(value.toString());
                break;
            case thumbnailHeightKey:
                this.thumbnailHeight = Double.valueOf(value.toString());
                break;
            case viewNumberKey:
                this.viewNumber = Integer.valueOf(value.toString());
                break;
            case currentViewKey:
                this.currentView = Integer.valueOf(value.toString());
                break;
            case durationKey:
                this.duration = Integer.valueOf(value.toString());
                break;
            case statusKey:
                this.status = value.toString();
                break;
            case maxLengthBuzzKey:
                this.maxLengthBuzz = Integer.valueOf(value.toString());
                break;
            case maxFilePerAlbumKey:
                this.maxFilePerAlbum = Integer.valueOf(value.toString());
                break;
            case categoryIdKey:
                this.catId = value.toString();
                break;
            case listCategoryKey:
                HashMap<String, Integer> hmap = new HashMap<String, Integer>();
                JSONArray listCategoryVer = (JSONArray) value;
                
                for(int i = 0; i<listCategoryVer.size(); i++){
                    JSONObject obj = (JSONObject) listCategoryVer.get(i);
                    String categoryId = (String) obj.get("cat_id");
                    Long version = (Long) obj.get("version");
                    hmap.put(categoryId, version.intValue());
                }
                this.listCategory = hmap;
                break;
        }
    }

    public static String parse(String msgStr) {

        Matcher m = pattern.matcher(msgStr);
        String element = "";

        while (m.find()) {
            element = m.group();
        }
        return element;
    }

    public String getBuzzId() {
        return buzzId;
    }

    public void setBuzzId(String buzzId) {
        this.buzzId = buzzId;
    }

    public String getSum() {
        return sum;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiName() {
        return apiName;
    }

    public String getImageCat() {
        return imageCat;
    }

    public void setImageCat(String imageCat) {
        this.imageCat = imageCat;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageKind() {
        return imageKind;
    }

    public void setImageKind(String imageKind) {
        this.imageKind = imageKind;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(String widthSize) {
        this.widthSize = widthSize;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerCatId() {
        return stickerCatId;
    }

    public void setStickerCatId(String stickerCatId) {
        this.stickerCatId = stickerCatId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getIp() {
        return ip;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getIsFree() {
        return is_free;
    }

    public void setIsFree(int is_free) {
        this.is_free = is_free;
    }

    public List<String> getListImgId() {
        return listImgId;
    }

    public void setListImgId(List<String> listImgId) {
        this.listImgId = listImgId;
    }

    public List<String> getListGiftId() {
        return listGiftId;
    }

    public void setListGiftId(List<String> listGiftId) {
        this.listGiftId = listGiftId;
    }

    public List<String> getListVideoId() {
        return listVideoId;
    }

    public void setListVideoId(List<String> listVideoId) {
        this.listVideoId = listVideoId;
    }
    
    public List<String> getListCoverId() {
        return listCoverId;
    }

    public void setListCoverId(List<String> listCoverId) {
        this.listCoverId = listCoverId;
    }

    public List<String> getListAudioId() {
        return listAudioId;
    }

    public void setListAudioId(List<String> listAudioId) {
        this.listAudioId = listAudioId;
    }

    public List<String> getListStreamId() {
        return listStreamId;
    }

    public void setListStreamId(List<String> listStreamId) {
        this.listStreamId = listStreamId;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(Integer totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public Integer getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(Integer imageNumber) {
        this.imageNumber = imageNumber;
    }

    public Integer getVideoNumber() {
        return videoNumber;
    }

    public void setVideoNumber(Integer videoNumber) {
        this.videoNumber = videoNumber;
    }

    public Integer getAudioNumber() {
        return audioNumber;
    }

    public void setAudioNumber(Integer audioNumber) {
        this.audioNumber = audioNumber;
    }

    public Integer getTotalFileUpload() {
        return totalFileUpload;
    }

    public void setTotalFileUpload(Integer totalFileUpload) {
        this.totalFileUpload = totalFileUpload;
    }

    public String getDefaultAudioImg() {
        return defaultAudioImg;
    }

    public void setDefaultAudioImg(String defaultAudioImg) {
        this.defaultAudioImg = defaultAudioImg;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Double getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Double getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Double imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Double getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(Double thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public Double getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(Double thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public Integer getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(Integer viewNumber) {
        this.viewNumber = viewNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxLengthBuzz() {
        return maxLengthBuzz;
    }

    public void setMaxLengthBuzz(Integer maxLengthBuzz) {
        this.maxLengthBuzz = maxLengthBuzz;
    }

    public Integer getMaxFilePerAlbum() {
        return maxFilePerAlbum;
    }

    public void setMaxFilePerAlbum(Integer maxFilePerAlbum) {
        this.maxFilePerAlbum = maxFilePerAlbum;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public HashMap<String, Integer> getListCategory() {
        return listCategory;
    }

    public void setListCategory(HashMap<String, Integer> listCategory) {
        this.listCategory = listCategory;
    }

    public String getShareHasDeletedImg() {
        return shareHasDeletedImg;
    }

    public void setShareHasDeletedImg(String shareHasDeletedImg) {
        this.shareHasDeletedImg = shareHasDeletedImg;
    }

    public Integer getCurrentView() {
        return currentView;
    }

    public void setCurrentView(Integer currentView) {
        this.currentView = currentView;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
}
