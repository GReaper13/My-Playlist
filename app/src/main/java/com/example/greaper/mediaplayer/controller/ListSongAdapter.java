package com.example.greaper.mediaplayer.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.model.SongModel;

import java.util.ArrayList;

/**
 * Created by GReaper on 1/21/2018.
 */

public class ListSongAdapter extends BaseAdapter {

    private ArrayList<SongModel> list = new ArrayList<>();
    private Context context;
    private ISong iSong;

    public ListSongAdapter(ArrayList<SongModel> list, Context context, ISong iSong) {
        this.list = list;
        this.context = context;
        this.iSong = iSong;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.song_item_add, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.txtItem = view.findViewById(R.id.txt_item_list_song);
            viewHolder.imgItemSong = view.findViewById(R.id.img_item_song);
            viewHolder.checkBox = view.findViewById(R.id.cb_add_song);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txtItem.setText(list.get(i).getTitle());
        viewHolder.imgItemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSong.showSong(list.get(i));
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView txtItem;
        ImageView imgItemSong;
        CheckBox checkBox;
    }
}
