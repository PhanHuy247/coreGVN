/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.entity.file;

import eazycommon.constant.FilesAndFolders;

/**
 *
 * @author HuyDX
 */
public class FileInfo {
    private String filePath;
    private long dateCreated;

    public FileInfo(String url, long dateCreated){
        if (url!=null)
            this.filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + url;
        this.dateCreated = dateCreated;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
