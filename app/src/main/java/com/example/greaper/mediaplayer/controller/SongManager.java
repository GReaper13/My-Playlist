package com.example.greaper.mediaplayer.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.greaper.mediaplayer.model.AddSongModel;
import com.example.greaper.mediaplayer.model.SongModel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by GReaper on 1/21/2018.
 */

public class SongManager {
    final String MEDIA_PATH = new String("/sdcard/Download/");

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ArrayList<AddSongModel> songList = new ArrayList<>();

    public ArrayList<AddSongModel> getSongList() {
        File home = new File(path + "/Download");

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                AddSongModel song = new AddSongModel(file.getName().substring(0, (file.getName().length() - 4)), file.getPath(), false);
                songList.add(song);
            }
        }
        return songList;
    }

    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File file, String name) {
            return (name.endsWith(".mp3") || (name.endsWith(".MP3")));
        }
    }

}
