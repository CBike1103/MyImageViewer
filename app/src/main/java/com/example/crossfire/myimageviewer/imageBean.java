package com.example.crossfire.myimageviewer;

/**
 * Created by CrossFire on 2016/7/11.
 */
public class ImageBean {



    private String imagePath;//The path of first image
    private String folderName;
    private int imageCounts;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts() {
        return imageCounts;
    }

    public void setImageCounts(int imageCounts) {
        this.imageCounts = imageCounts;
    }


}
