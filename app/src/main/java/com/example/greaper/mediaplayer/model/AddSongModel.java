package com.example.greaper.mediaplayer.model;

/**
 * Created by GReaper on 2/2/2018.
 */

public class AddSongModel {
    private String title;
    private boolean isSelect;
    private String path;

    public AddSongModel(String title, String path, boolean isSelect) {
        this.title = title;
        this.isSelect = isSelect;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
