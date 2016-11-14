package com.merlin.entities;

import android.content.Context;

import com.merlin.cortina.MainActivity;
import com.merlin.cortina.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Merlin on 10/11/2016.
 */

public class Invoice implements Serializable {

    private String client;
    private Calendar creationDate;
    private ArrayList<Curtain> curtains;

    public Invoice(String client, Calendar creationDate, ArrayList<Curtain> curtains) {
        this.client = client;
        this.creationDate = creationDate;
        this.curtains = curtains;
    }



    public String getFormattedForEmail(){
        Context context = MainActivity.getContext();
        StringBuffer sb = new StringBuffer();
        for (Curtain curtain: curtains) {
            sb.append(curtain.getFormattedText());
        }
        return String.format(context.getResources().getString(R.string.email), client,  sb);
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public ArrayList<Curtain> getCurtains() {
        return curtains;
    }

    public void setCurtains(ArrayList<Curtain> curtains) {
        this.curtains = curtains;
    }

    @Override
    public String toString(){
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        return "Cliente: " + client + "\n" + "Creado: " + df.format(creationDate.getTime()) + ", CT: " + curtains.size();
    }
}
