package com.example.greaper.mediaplayer.Utils;


import android.Manifest;
import android.util.Log;

/**
 * Created by GReaper on 1/21/2018.
 */

public class Utils {

    public static String milliSecondsToTime(int miliSeconds) {
        String finalTimeString = "";
        String secondString = "";

        int hour = miliSeconds / (1000*60*60);
        int minute = miliSeconds % (1000*60*60) / (1000*60);
        int second = miliSeconds % (1000*60*60) % (1000*60) / (1000);

        if (hour > 0) {
            finalTimeString = hour + ":";
        }

        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = second + "";
        }

        finalTimeString = finalTimeString + minute + ":" + secondString;
        return finalTimeString;
    }

    public static int getProgressPercent(int currentDuration, int totalDuration) {
        int percent;

        float currentSeconds = (float) currentDuration;
        float totalSeconds = (float) totalDuration;
        percent = (int)((currentSeconds/totalSeconds)*100);
        return percent;
    }

    public static int progressToTime(int progress, int totalDuration) {
        int currentDuration = (int)(((float)progress / 100) * totalDuration);
        return currentDuration;
    }
}
