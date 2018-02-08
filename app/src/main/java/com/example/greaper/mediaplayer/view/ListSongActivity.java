package com.example.greaper.mediaplayer.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.controller.AddSongAdapter;
import com.example.greaper.mediaplayer.controller.ISong;
import com.example.greaper.mediaplayer.controller.ListSongAdapter;
import com.example.greaper.mediaplayer.controller.SongManager;
import com.example.greaper.mediaplayer.database.SongDataSource;
import com.example.greaper.mediaplayer.model.AddSongModel;
import com.example.greaper.mediaplayer.model.SongModel;

import java.util.ArrayList;

public class ListSongActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    private ListView lvListSong;
    private AddSongAdapter addSongAdapter;
    private ArrayList<AddSongModel> listSong;
    private SongManager songManager;
    private SongDataSource songDataSource;
    private ArrayList<SongModel> listSongInDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        initViews();
    }

    private void initViews() {
        initToolbar();
        songManager = new SongManager();
        listSong = songManager.getSongList();
        songManager = new SongManager();
        addSongAdapter = new AddSongAdapter(listSong, this);
        songDataSource = new SongDataSource(this);
        songDataSource.open();

        lvListSong = (ListView) findViewById(R.id.lv_list_song);
        listSongInDatabase = songDataSource.getCurrentSong();

        for (int i = 0; i < listSongInDatabase.size(); i++) {
            int index = getIndex(listSongInDatabase.get(i).getTitle());
            if (index != -1) {
                listSong.get(index).setSelect(true);
            } else {
                Toast.makeText(this, "Somethings was wrong", Toast.LENGTH_SHORT).show();
            }
        }
        lvListSong.setAdapter(addSongAdapter);
        lvListSong.setOnItemClickListener(this);
    }

    private int getIndex(String itemName)
    {
        for (int i = 0; i < listSong.size(); i++)
        {
            AddSongModel addSongModel = listSong.get(i);
            if (addSongModel.getTitle().equals(itemName))
            {
                return i;
            }
        }
        return -1;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toobar_list_song);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_music);
        toolbar.setTitle("List songs");
        setSupportActionBar(toolbar);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent();
//        intent.putExtra("id", i);
//        setResult(RESULT_OK, intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_song:
                songDataSource.deleteAllSong();
                for (int i = 0; i < listSong.size(); i++) {
                    if (listSong.get(i).isSelect()) {
                        AddSongModel add = listSong.get(i);
                        songDataSource.addNewSong(new SongModel(add.getTitle(), add.getPath()));
                    }
                }
                songDataSource.close();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        songDataSource.close();
        super.onDestroy();
    }
}
