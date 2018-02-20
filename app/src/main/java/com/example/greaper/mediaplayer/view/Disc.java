package com.example.greaper.mediaplayer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greaper.mediaplayer.R;

public class Disc extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private OnFragmentInteractionListener mListener;

    public Disc() {
        // Required empty public constructor
    }

    private ImageView imgDisc;
    private ObjectAnimator objectAnimator;

    public static Disc newInstance(String param1, String param2) {
        Disc fragment = new Disc();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disc, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        imgDisc = v.findViewById(R.id.img_disc);
        objectAnimator = ObjectAnimator.ofFloat(imgDisc, "rotation", 0f, 360f);
        objectAnimator.setRepeatCount(1000);
        objectAnimator.setDuration(10000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void changeState(boolean isPlaying) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isPlaying) {
                if (objectAnimator.isRunning()) {
                    objectAnimator.pause();
                }
            } else {
                if (objectAnimator.isPaused()) {
                    objectAnimator.resume();
                }
            }
        }
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    //    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

}
