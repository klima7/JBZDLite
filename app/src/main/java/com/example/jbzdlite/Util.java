package com.example.jbzdlite;

import android.util.Log;

public class Util {
    private static final String TAG = "MyLog";

    public static void println(String text) {
        Log.i(TAG, text);
    }

    public static void println(int text) {
        Log.i(TAG, ""+text);
    }

    public static void println(double text) {
        Log.i(TAG, ""+text);
    }

    public static void println(boolean text) {
        Log.i(TAG, ""+text);
    }

    public static void println(float text) {
        Log.i(TAG, ""+text);
    }

    public static void println(Object text) {
        Log.i(TAG, ""+text);
    }

    public static void println(long text) {
        Log.i(TAG, ""+text);
    }

    public static void println(short text) {
        Log.i(TAG, ""+text);
    }

    public static void println(char text) {
        Log.i(TAG, ""+text);
    }
}
