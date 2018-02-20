package com.example.greaper.mediaplayer.model;

/**
 * Created by GReaper on 1/20/2018.
 */

public class SongModel {
    private String title;
    private String path;
    private boolean isSelectToDelete;
    private int position;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelectToDelete() {
        return isSelectToDelete;
    }

    public void setSelectToDelete(boolean selectToDelete) {
        isSelectToDelete = selectToDelete;
    }

    public SongModel(String title, String path, int position) {
        this.title = title;
        this.path = path;
        this.isSelectToDelete = false;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
