package com.example.greaper.mediaplayer.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.model.AddSongModel;

import java.util.ArrayList;

/**
 * Created by GReaper on 2/2/2018.
 */

public class AddSongAdapter extends BaseAdapter {

    private ArrayList<AddSongModel> listAddSong = new ArrayList<>();
    private Context context;

    public AddSongAdapter(ArrayList<AddSongModel> listAddSong, Context context) {
        this.listAddSong = listAddSong;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listAddSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listAddSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView txtTitle;
        CheckBox checkBox;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.song_item_add, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = view.findViewById(R.id.txt_item_add_list_song);
            viewHolder.checkBox = view.findViewById(R.id.cb_add_song);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtTitle.setText(listAddSong.get(i).getTitle());
        viewHolder.checkBox.setChecked(listAddSong.get(i).isSelect());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listAddSong.get(i).setSelect(b);
            }
        });
        return view;
    }
}
