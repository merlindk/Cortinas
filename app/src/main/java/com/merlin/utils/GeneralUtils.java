package com.merlin.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.merlin.cortina.MainActivity;
import com.merlin.entities.Invoice;

/**
 * Created by Merlin on 12/11/2016.
 */

public class GeneralUtils {
    private static Context context = MainActivity.getContext();

    public static void copyInvoiceToClippboard(Invoice invoice){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Pedido", invoice.getFormattedForEmail());
        clipboard.setPrimaryClip(clip);
    }

    public static void copyTextToClippboard(String s){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Pedido", s);
        clipboard.setPrimaryClip(clip);
    }
}
