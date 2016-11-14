package com.merlin.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Merlin on 13/11/2016.
 */

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler{

    private Snackbar shower;
    private Thread.UncaughtExceptionHandler oldHandler;
    private static StringBuffer log = new StringBuffer("");
    private static CustomExceptionHandler handler = null;

    public static CustomExceptionHandler getHandler(Thread.UncaughtExceptionHandler oldHandler){
        if(CustomExceptionHandler.handler == null){
            return new CustomExceptionHandler(oldHandler);
        }else{
            return CustomExceptionHandler.handler;
        }
    }
    public CustomExceptionHandler( Thread.UncaughtExceptionHandler oldHandler){
        this.oldHandler = oldHandler;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("Exception", "Logged Exception: " + throwable.toString() + throwable.getStackTrace(), throwable);
        log.append(throwable.toString());
        for (int i = 0; i < throwable.getStackTrace().length; i++){
            log.append("\n" + throwable.getStackTrace()[i].toString());
        }
        GeneralUtils.copyTextToClippboard(log.toString());
        if (oldHandler != null)
            oldHandler.uncaughtException(
                    thread,
                    throwable
            ); //Delegates to Android's error handling
        else
            System.exit(2); //Prevents the service/app from freezing
    }
}
