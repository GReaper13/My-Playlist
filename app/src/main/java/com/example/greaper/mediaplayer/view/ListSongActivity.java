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
import com.example.greaper.mediaplayer.model.AddSongModel;
import com.example.greaper.mediaplayer.model.SongModel;

import java.util.ArrayList;

public class ListSongActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    private ListView lvListSong;
    private AddSongAdapter addSongAdapter;
    private ArrayList<AddSongModel> listSong;
    private SongManager songManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        initViews();
    }

    private void initViews() {
        listSong = new ArrayList<>();
        songManager = new SongManager();
        listSong = songManager.getSongList();
        toolbar = (Toolbar) findViewById(R.id.toobar_list_song);
        lvListSong = (ListView) findViewById(R.id.lv_list_song);
        toolbar.setTitle("List songs");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_music);
        setSupportActionBar(toolbar);

        addSongAdapter = new AddSongAdapter(listSong, this);
        lvListSong.setAdapter(addSongAdapter);
        lvListSong.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("id", i);
        setResult(RESULT_OK, intent);
        finish();
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
                for (int i = 0; i < listSong.size(); i++) {
//                    listSongAdapter.getItem(i)
                }
        }
        return true;
    }
}
