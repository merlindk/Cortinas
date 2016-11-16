package com.merlin.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.merlin.cortina.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Merlin on 10/11/2016.
 */

public class Invoice implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Invoice> CREATOR = new Parcelable.Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };
    private String client;
    private Calendar creationDate;
    private ArrayList<Curtain> curtains;
    private Context context;
    private String id;


    public Invoice(String client, Calendar creationDate, ArrayList<Curtain> curtains, Context context) {
        this.client = client;
        this.creationDate = creationDate;
        this.curtains = curtains;
        this.context = context;
        this.id = UUID.randomUUID().toString();
    }

    protected Invoice(Parcel in) {
        client = in.readString();
        creationDate = (Calendar) in.readValue(Calendar.class.getClassLoader());
        if (in.readByte() == 0x01) {
            curtains = new ArrayList<>();
            in.readList(curtains, Curtain.class.getClassLoader());
        } else {
            curtains = null;
        }
        id = in.readString();
    }

    public String getFormattedForEmail(){
        StringBuilder sb = new StringBuilder("");
        String result;
        boolean areThereExtras = false;
        for (Curtain curtain: curtains) {
            sb.append(curtain.getFormattedText());
            if (curtain.isHasExtras()) {
                areThereExtras = true;
            }
        }
        if (areThereExtras) {
            result = String.format(context.getResources().getString(R.string.email), client, sb.toString());
        } else {
            result = String.format(context.getResources().getString(R.string.emailNoExtras), client, sb.toString());
        }
        return result;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        for (Curtain curt : curtains) {
            curt.setContext(context);
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client);
        dest.writeValue(creationDate);
        if (curtains == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(curtains);
        }
        dest.writeString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        return id.equals(invoice.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}