package com.merlin.utils;

import android.content.Context;

/**
 * Created by Merlin on 13/11/2016.
 */

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler{

    private Thread.UncaughtExceptionHandler oldHandler;
    private StringBuffer log = new StringBuffer("");
    private CustomExceptionHandler handler = null;
    private Context context;

    public CustomExceptionHandler(Thread.UncaughtExceptionHandler oldHandler, Context context) {
        this.oldHandler = oldHandler;
        this.context = context;
    }

    public CustomExceptionHandler getHandler(Thread.UncaughtExceptionHandler oldHandler, Context context) {
        if (handler == null) {
            return new CustomExceptionHandler(oldHandler, context);
        }else{
            return handler;
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        log.append(throwable.toString());
        for (int i = 0; i < throwable.getStackTrace().length; i++){
            log.append("\n" + throwable.getStackTrace()[i].toString());
        }
        GeneralUtils.copyTextToClippboard(log.toString(), this.context);
        if (oldHandler != null)
            oldHandler.uncaughtException(
                    thread,
                    throwable
            ); //Delegates to Android's error handling
        else
            System.exit(2); //Prevents the service/app from freezing
    }
}
