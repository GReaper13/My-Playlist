package com.example.greaper.mediaplayer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.Utils.Utils;
import com.example.greaper.mediaplayer.controller.PageAdapter;
import com.example.greaper.mediaplayer.database.SongDataSource;
import com.example.greaper.mediaplayer.model.SongModel;

import java.io.IOException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, ListSong.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    public static final int SELECT_SONG_REQUEST = 0;
    public static final int SEEK_FORWARD_TIME = 5000;
    public static final int SEEK_BACKWARD_TIME = 5000;
    final int MY_PERMISSION_REQUEST_READ_STORAGE = 0;
    private boolean isUsingSeekBar = false;
    private boolean isEditSong = false;

    private Toolbar toolbar;

    public ArrayList<SongModel> listSong;
    private int currentSongIndex = 0;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private SongDataSource songDataSource;

    private Button btnPlay, btnToNext, btnNext, btnToPrev, btnPrev;
    private SeekBar seekbar;
    private TextView txtCurrent, txtTotal, txtCurrentSong;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private CircleIndicator circleIndicator;
    private CheckBox checkBoxAll;

    private Menu menu;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       if (Build.VERSION.SDK_INT >= 23) {
           check();
       } else {
           initCurrentSong();
           initViews();
       }


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        mediaPlayer = new MediaPlayer();
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnToNext = (Button) findViewById(R.id.btn_to_next);
        btnToPrev = (Button) findViewById(R.id.btn_to_prev);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        txtCurrent = (TextView) findViewById(R.id.txt_current);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        circleIndicator = findViewById(R.id.indicator);
        txtCurrentSong = toolbar.findViewById(R.id.txt_current_song);
        checkBoxAll = toolbar.findViewById(R.id.cb_all_list_song);

        FragmentManager fragmentManager = getSupportFragmentManager();
        pageAdapter = new PageAdapter(fragmentManager);
        viewPager.setAdapter(pageAdapter);
        circleIndicator.setViewPager(viewPager);
        setSupportActionBar(toolbar);

        btnPlay.setOnClickListener(this);
        btnToNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnToPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);

        seekbar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);

        checkBoxAll.setVisibility(View.INVISIBLE);
        checkBoxAll.setOnClickListener(this);
        playSong(currentSongIndex);
    }

    private void playSong(int currentSongIndex) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (listSong.size() == 0 || listSong == null) {
                return;
            }
            mediaPlayer.setDataSource(listSong.get(currentSongIndex).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            txtCurrentSong.setText(listSong.get(currentSongIndex).getTitle());
            seekbar.setProgress(0);
            seekbar.setMax(100);
            btnPlay.setBackgroundResource(R.drawable.pause);

            updateProress();

            buildNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateProress() {

        mHandler.postDelayed(mUpdateTimeTask, 100);

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            int totalDuration = mediaPlayer.getDuration();
            int currentDuration = mediaPlayer.getCurrentPosition();

            txtTotal.setText(Utils.milliSecondsToTime(totalDuration));

            int progress = Utils.getProgressPercent(currentDuration, totalDuration);
            if (!isUsingSeekBar) {
                seekbar.setProgress(progress);
                txtCurrent.setText(Utils.milliSecondsToTime(currentDuration));
            }
            mHandler.postDelayed(this, 100);
        }
    };

    private void buildNotification() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        this.menu = menu;
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_play) {
            if (mediaPlayer.isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.play);
                    ((Disc)pageAdapter.getRegisteredFragment(0)).changeState(true);
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    ((Disc)pageAdapter.getRegisteredFragment(0)).changeState(false);
                }
            }
        }

        if (id == R.id.btn_to_prev) {
            int currentDuaration = mediaPlayer.getCurrentPosition();
            if (currentDuaration - SEEK_BACKWARD_TIME >= 0) {
                mediaPlayer.seekTo(currentDuaration - SEEK_BACKWARD_TIME);
            } else {
                mediaPlayer.seekTo(0);
            }
        }

        if (id == R.id.btn_to_next) {
            int currentDuration = mediaPlayer.getCurrentPosition();
            if (currentDuration + SEEK_FORWARD_TIME <= mediaPlayer.getDuration()) {
                mediaPlayer.seekTo(currentDuration + SEEK_FORWARD_TIME);
            } else {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }

        if (id == R.id.btn_next) {
            if (currentSongIndex < (listSong.size() -1)) {
                playSong(++currentSongIndex);
            } else {
                playSong(0);
                currentSongIndex = 0;
            }
            buildNotification();
        }

        if (id == R.id.btn_prev) {
            if (currentSongIndex != 0) {
                playSong(--currentSongIndex);
            } else {
                playSong(listSong.size() - 1);
                currentSongIndex = listSong.size() - 1;
            }
            buildNotification();
        }

        if (id == R.id.cb_all_list_song) {
            ((ListSong)pageAdapter.getRegisteredFragment(1)).clickSelectAll(checkBoxAll.isChecked());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_song:
                if (!isEditSong) {
                    startEditSong();
                    ((ListSong)pageAdapter.getRegisteredFragment(1)).editSong(isEditSong);
                } else {
                    endEditSong();
                    ((ListSong)pageAdapter.getRegisteredFragment(1)).updateListCurrentSong(isEditSong);
                }
                //initCurrentSong();
                break;
            case R.id.add_song:
                Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
                startActivityForResult(intent, SELECT_SONG_REQUEST);
                endEditSong();
                break;
            case R.id.delete_song:
                boolean isDeleteSongIsPlaying = ((ListSong)pageAdapter.getRegisteredFragment(1)).deleteSong(listSong.get(currentSongIndex).getTitle());
                initCurrentSong();
                if (isDeleteSongIsPlaying) {
                    playSong(0);
                    currentSongIndex = 0;
                    // TODO
                }
                endEditSong();
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (isUsingSeekBar) {
            int time = Utils.progressToTime(i, mediaPlayer.getDuration());
            txtCurrent.setText(Utils.milliSecondsToTime(time));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        int time = Utils.progressToTime(seekBar.getProgress(), mediaPlayer.getDuration());
        txtCurrent.setText(Utils.milliSecondsToTime(time));
        isUsingSeekBar = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int time = Utils.progressToTime(seekBar.getProgress(), mediaPlayer.getDuration());
        txtCurrent.setText(Utils.milliSecondsToTime(time));
        mediaPlayer.seekTo(time);
        isUsingSeekBar = false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (currentSongIndex < (listSong.size() - 1)) {
            playSong(++currentSongIndex);
        } else {
            playSong(0);
            currentSongIndex = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_SONG_REQUEST && resultCode == RESULT_OK) {
            ((ListSong)pageAdapter.getRegisteredFragment(1)).updateListCurrentSong(isEditSong);
            initCurrentSong();
            playSong(0);
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        songDataSource.close();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    private void check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // deny before
                Toast.makeText(this, "ERROR, Your must give me a read_external permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_STORAGE);
            }
        } else {
            initCurrentSong();
            initViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCurrentSong();
                    initViews();
                } else {
                    onDestroy();
                }
                break;
        }
        return;
    }


    @Override
    public void getListSong(ArrayList<SongModel> arrayList) {

    }

    @Override
    public void clickSong(int position) {
        playSong(position);
        currentSongIndex = position;
    }

    @Override
    public void checkCheckBox(boolean isSelectingAll) {
        checkBoxAll.setChecked(isSelectingAll);
    }

    @Override
    public void hasSongSelected(boolean hasSongSelected) {
        menu.getItem(0).setVisible(hasSongSelected);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //initViewsIcon();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initCurrentSong() {
        songDataSource = new SongDataSource(this);
        songDataSource.open();
        listSong = songDataSource.getCurrentSong();
        if (listSong == null) {
            listSong = new ArrayList<>();
        }
    }

    private void startEditSong() {
        isEditSong = !isEditSong;
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setIcon(R.drawable.check);
        checkBoxAll.setVisibility(View.VISIBLE);
        checkBoxAll.setChecked(false);
        txtCurrentSong.setVisibility(View.INVISIBLE);
    }

    private void endEditSong() {
        isEditSong = !isEditSong;
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setIcon(R.drawable.edit);
        checkBoxAll.setVisibility(View.INVISIBLE);
        txtCurrentSong.setVisibility(View.VISIBLE);
    }
}
