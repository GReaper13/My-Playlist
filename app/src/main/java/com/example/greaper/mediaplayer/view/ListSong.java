package com.example.greaper.mediaplayer.view;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.controller.ISong;
import com.example.greaper.mediaplayer.controller.ImpListSong;
import com.example.greaper.mediaplayer.controller.ListSongAdapter;
import com.example.greaper.mediaplayer.controller.SongManager;
import com.example.greaper.mediaplayer.database.SongDataSource;
import com.example.greaper.mediaplayer.model.SongModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ListSong extends Fragment implements ISong, AdapterView.OnItemClickListener, ImpListSong {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<SongModel> listSong;

    private OnFragmentInteractionListener mListener;
    private ListSongAdapter adapter;
    private ListView listView;
    private SongDataSource songDataSource;

    private boolean isEditSong = false;

    public ListSong() {
        // Required empty public constructor
    }


    public static ListSong newInstance(String param1, String param2) {
        ListSong fragment = new ListSong();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        songDataSource = new SongDataSource(getActivity());
        songDataSource.open();

        listSong = songDataSource.getCurrentSong();

        if (listSong == null) {
            listSong = new ArrayList<>();
        } else {
            Collections.sort(listSong, new Comparator<SongModel>() {
                @Override
                public int compare(SongModel o1, SongModel o2) {
                    if (o1.getPosition() > o2.getPosition()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        adapter = new ListSongAdapter(listSong, getActivity(), this, false, this);

        listView = view.findViewById(R.id.lv_current_song);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void updateListCurrentSong(boolean isEditSong) {
        listSong = songDataSource.getCurrentSong();
        adapter = new ListSongAdapter(listSong, getContext(), this, false, this);
        listView.setAdapter(adapter);
        this.isEditSong = isEditSong;
//        adapter.notifyDataSetChanged();
    }

    public void editSong(boolean isEditSong) {
        adapter.setEditSong(true);
        adapter.notifyDataSetChanged();
        this.isEditSong = isEditSong;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        songDataSource.close();
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showSong(SongModel songModel) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!isEditSong) {
            mListener.clickSong(i);
        } else {
            listSong.get(i).setSelectToDelete(!listSong.get(i).isSelectToDelete());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void checkCheckBoxSelectAll() {
        mListener.checkCheckBox(isSelectingAll());
    }

    @Override
    public void checkHasASongSelected() {
        mListener.hasSongSelected(hasASongSelected());
    }


    public interface OnFragmentInteractionListener {
        void getListSong(ArrayList<SongModel> arrayList);
        void clickSong(int position);
        void checkCheckBox(boolean isSelectingAll);
        void hasSongSelected(boolean hasSongSelected);
    }

    // check all checkbox is selecting
    private boolean isSelectingAll() {
        for (SongModel song : listSong) {
            if (!song.isSelectToDelete()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasASongSelected() {
        for (SongModel song: listSong) {
            if (song.isSelectToDelete()) {
                return true;
            }
        }
        return false;
    }

    public void clickSelectAll(boolean isChecked) {
        for (SongModel songModel : listSong) {
            songModel.setSelectToDelete(isChecked);
        }
        adapter.notifyDataSetChanged();
    }

    public boolean deleteSong(String titleIsPlaying) {
        boolean isDeleteSongIsPlaying = false;
        for (int i = listSong.size()-1; i > -1; i--) {
            String title = listSong.get(i).getTitle();
            String path = listSong.get(i).getPath();
            int position = listSong.get(i).getPosition();
            if (listSong.get(i).isSelectToDelete()) {
                if (title.equals(titleIsPlaying)) {
                    isDeleteSongIsPlaying = true;
                }
                songDataSource.deleteSong(new SongModel(title, path, position));
                listSong.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        return isDeleteSongIsPlaying;
    }
}
