package com.example.greaper.mediaplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
    private ImpAddSong impAddSong;

    public AddSongAdapter(ArrayList<AddSongModel> listAddSong, Context context, ImpAddSong impAddSong) {
        this.listAddSong = listAddSong;
        this.context = context;
        this.impAddSong = impAddSong;
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

    public class ViewHolder {
        TextView txtTitle;
        CheckBox checkBox;
        LinearLayout linearLayout;
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
            viewHolder.linearLayout = view.findViewById(R.id.ll_item_add_song);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtTitle.setText(listAddSong.get(i).getTitle());
        viewHolder.checkBox.setChecked(listAddSong.get(i).isSelect());
        changeBackgroundItem(viewHolder, i);
        final ViewHolder viewHolder1 = viewHolder;
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listAddSong.get(i).setSelect(b);
                changeBackgroundItem(viewHolder1, i);
                impAddSong.checkCheckBoxSelectAll();
            }
        });
        return view;
    }
    private void changeBackgroundItem(ViewHolder viewHolder, int i) {
        if (listAddSong.get(i).isSelect()) {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#FF10E7D5"));
        } else {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }
}
