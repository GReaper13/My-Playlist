package com.example.greaper.mediaplayer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greaper.mediaplayer.R;
import com.example.greaper.mediaplayer.controller.ISong;
import com.example.greaper.mediaplayer.controller.ImpListSong;
import com.example.greaper.mediaplayer.controller.ListSongAdapter;
import com.example.greaper.mediaplayer.database.SongDataSource;
import com.example.greaper.mediaplayer.model.SongModel;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListSong extends Fragment implements ISong, ImpListSong {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<SongModel> listSong;

    private OnFragmentInteractionListener mListener;
    //private ListView listView;
    private DragListView dragListView;
    private SongDataSource songDataSource;
    private ListSongAdapter listSongAdapter;

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
        }
        listSongAdapter = new ListSongAdapter(listSong,R.layout.song_item, R.id.img_change_song, false, false, this);

        dragListView = view.findViewById(R.id.dlv_list_song);
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {

            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    for (int i = 0; i < listSong.size(); i++) {
                        songDataSource.updateSong(listSong.get(i).getTitle(), i);
                    }
                    mListener.dragSongComplete();
                }
            }
        });
        dragListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dragListView.setAdapter(listSongAdapter, true);
        dragListView.setCanDragHorizontally(false);
    }

    public void updateListCurrentSong(boolean isEditSong) {
        listSong = songDataSource.getCurrentSong();
        listSongAdapter = new ListSongAdapter(listSong,R.layout.song_item, R.id.img_change_song, false, false, this);
        dragListView.setAdapter(listSongAdapter, true);
        this.isEditSong = isEditSong;
//        adapter.notifyDataSetChanged();
    }

    public void editSong(boolean isEditSong) {
        listSongAdapter.setEditSong(true);
        listSongAdapter.notifyDataSetChanged();
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
    public void checkCheckBoxSelectAll() {
        mListener.checkCheckBox(isSelectingAll());
    }

    @Override
    public void checkHasASongSelected() {
        mListener.hasSongSelected(hasASongSelected());
    }

    @Override
    public void clickSong(int position) {
        if (!isEditSong) {
            mListener.clickSong(position);
        } else {
            listSong.get(position).setSelectToDelete(!listSong.get(position).isSelectToDelete());
            listSongAdapter.notifyDataSetChanged();
        }
    }


    public interface OnFragmentInteractionListener {
        void getListSong(ArrayList<SongModel> arrayList);
        void clickSong(int position);
        void checkCheckBox(boolean isSelectingAll);
        void hasSongSelected(boolean hasSongSelected);
        void dragSongComplete();
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
        listSongAdapter.notifyDataSetChanged();
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
        listSongAdapter.notifyDataSetChanged();
        return isDeleteSongIsPlaying;
    }

    private void sortListSong() {
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
}
