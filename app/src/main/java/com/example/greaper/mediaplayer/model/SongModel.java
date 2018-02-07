package com.example.greaper.mediaplayer.model;

/**
 * Created by GReaper on 1/20/2018.
 */

public class SongModel {
    private String title;
    private String path;

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

    public SongModel(String title, String path) {
        this.title = title;
        this.path = path;
    }
}
