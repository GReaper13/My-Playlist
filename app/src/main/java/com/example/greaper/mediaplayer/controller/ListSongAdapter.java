package com.example.greaper.mediaplayer.controller;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.model.SongModel;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

/**
 * Created by GReaper on 2/20/2018.
 */

public class ListSongAdapter extends DragItemAdapter<SongModel, ListSongAdapter.ViewHolder> {
    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private boolean isEditSong;
    private ImpListSong impListSong;

    public ListSongAdapter(ArrayList<SongModel> list, int mLayoutId, int mGrabHandleId, boolean mDragOnLongPress, boolean isEditSong, ImpListSong impListSong) {
        this.mLayoutId = mLayoutId;
        this.mGrabHandleId = mGrabHandleId;
        this.mDragOnLongPress = mDragOnLongPress;
        this.isEditSong = isEditSong;
        this.impListSong = impListSong;
        setItemList(list);
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getPosition();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        String[] title_str = mItemList.get(position).getTitle().split("-");
        if (title_str.length < 2) {
            holder.txtSinger.setText("");
        } else {
            holder.txtSinger.setText(title_str[1]);
        }
        holder.txtName.setText(title_str[0]);
        holder.txtNumber.setText(String.valueOf(position+1));
        changeBackgroundItem(holder, position);
        holder.checkBox.setChecked(mItemList.get(position).isSelectToDelete());
        if (isEditSong) {
            holder.txtNumber.setVisibility(View.INVISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.txtNumber.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        final ViewHolder viewHolder1 = holder;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mItemList.get(position).setSelectToDelete(isChecked);
                changeBackgroundItem(viewHolder1, position);
                impListSong.checkCheckBoxSelectAll();
                impListSong.checkHasASongSelected();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impListSong.clickSong(position);
            }
        });
    }



    public class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView txtName, txtNumber, txtSinger;
        CheckBox checkBox;
        ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
        }

        public ViewHolder(View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            txtName = itemView.findViewById(R.id.txt_name_item_list_song);
            txtSinger = itemView.findViewById(R.id.txt_singer_item_list_song);
            txtNumber = itemView.findViewById(R.id.txt_number_item_list_song);
            checkBox = itemView.findViewById(R.id.cb_item_list_song);
            imageView = itemView.findViewById(R.id.img_change_song);
            linearLayout = itemView.findViewById(R.id.ll_item_list_song);
        }

    }

    public boolean isEditSong() {
        return isEditSong;
    }

    public void setEditSong(boolean editSong) {
        isEditSong = editSong;
    }

    private void changeBackgroundItem(ViewHolder viewHolder, int i) {
        if (mItemList.get(i).isSelectToDelete()) {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#FF10E7D5"));
        } else {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }


}
