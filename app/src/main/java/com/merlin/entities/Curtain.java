package com.merlin.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.merlin.cortina.R;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Merlin on 9/11/2016.
 */

public class Curtain implements Parcelable, Serializable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Curtain> CREATOR = new Parcelable.Creator<Curtain>() {
        @Override
        public Curtain createFromParcel(Parcel in) {
            return new Curtain(in);
        }

        @Override
        public Curtain[] newArray(int size) {
            return new Curtain[size];
        }
    };
    private int height;
    private int width;
    private String fabric;
    private String room;
    private String cdn;
    private String mnd;
    private String sdo;
    private int alto;
    private String ops;
    private boolean counter;
    private boolean inter;
    private boolean doble;
    private String moto;
    private int glat;
    private int ginf;
    private String supl;
    private int cenef;
    private String sup;
    private String prod;
    private boolean optional;
    private transient Context context;
    private String id;
    private boolean hasExtras;

    public Curtain(int height, int width, String fabric, String room, String cdn, String mnd, String sdo, int alto, String ops, boolean counter, boolean inter, boolean doble, String moto, int glat, int ginf, String supl, int cenef, String sup, String prod, boolean optional, boolean hasExtras, Context context) {
        this.height = height;
        this.width = width;
        this.fabric = fabric;
        this.room = room;
        this.cdn = cdn;
        this.mnd = mnd;
        this.sdo = sdo;
        this.alto = alto;
        this.ops = ops;
        this.counter = counter;
        this.inter = inter;
        this.doble = doble;
        this.moto = moto;
        this.glat = glat;
        this.ginf = ginf;
        this.supl = supl;
        this.cenef = cenef;
        this.sup = sup;
        this.prod = prod;
        this.optional = optional;
        this.hasExtras = hasExtras;
        this.context = context;
        this.id = UUID.randomUUID().toString();
    }

    protected Curtain(Parcel in) {
        height = in.readInt();
        width = in.readInt();
        fabric = in.readString();
        room = in.readString();
        cdn = in.readString();
        mnd = in.readString();
        sdo = in.readString();
        alto = in.readInt();
        ops = in.readString();
        counter = in.readByte() != 0x00;
        inter = in.readByte() != 0x00;
        doble = in.readByte() != 0x00;
        moto = in.readString();
        glat = in.readInt();
        ginf = in.readInt();
        supl = in.readString();
        cenef = in.readInt();
        sup = in.readString();
        prod = in.readString();
        optional = in.readByte() != 0x00;
        hasExtras = in.readByte() != 0x00;
        id = in.readString();
    }

    public boolean isHasExtras() {
        return hasExtras;
    }

    public void setHasExtras(boolean hasExtras) {
        this.hasExtras = hasExtras;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }

    public boolean isInter() {
        return inter;
    }

    public void setInter(boolean inter) {
        this.inter = inter;
    }

    public boolean isDoble() {
        return doble;
    }

    public void setDoble(boolean doble) {
        this.doble = doble;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public int getGlat() {
        return glat;
    }

    public void setGlat(int glat) {
        this.glat = glat;
    }

    public int getGinf() {
        return ginf;
    }

    public void setGinf(int ginf) {
        this.ginf = ginf;
    }

    public String getSupl() {
        return supl;
    }

    public void setSupl(String supl) {
        this.supl = supl;
    }

    public int getCenef() {
        return cenef;
    }

    public void setCenef(int cenef) {
        this.cenef = cenef;
    }

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public String getMnd() {
        return mnd;
    }

    public void setMnd(String mnd) {
        this.mnd = mnd;
    }

    public String getSdo() {
        return sdo;
    }

    public void setSdo(String sdo) {
        this.sdo = sdo;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public String getOps() {
        return ops;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getArea(){
        return this.width * this.height;
    }

    @Override
    public String toString()
    {
        String base = context.getResources().getString(R.string.invoiceListFormat);
        return String.format(base, fabric, width, height, mnd, sdo, cdn, alto, room);

    }

    public String getFormattedText(){
        String result;

        String mando;
        String sentido;
        String opcional;
        String interValue;
        String dobleValue;
        String counterValue;
        if("Derecho".equalsIgnoreCase(mnd)){
            mando = "der";
        }else if("Izquierdo".equalsIgnoreCase(mnd)){
            mando = "izq";
        }else{
            mando = mnd;
        }
        if("Normal".equalsIgnoreCase(sdo)){
            sentido = "nor";
        }else if("Invertida".equalsIgnoreCase(sdo)){
            sentido = "inv";
        }else{
            sentido = sdo;
        }
        if(optional){
            opcional = context.getResources().getString(R.string.yes);
        }else{
            opcional = context.getResources().getString(R.string.no);
        }
        if(inter){
            interValue = context.getResources().getString(R.string.yes);
        }else{
            interValue = context.getResources().getString(R.string.no);
        }
        if(doble){
            dobleValue = context.getResources().getString(R.string.yes);
        }else{
            dobleValue = context.getResources().getString(R.string.no);
        }
        if(counter){
            counterValue = context.getResources().getString(R.string.yes);
        }else{
            counterValue = context.getResources().getString(R.string.no);
        }
        if (hasExtras) {
            result = String.format(context.getResources().getString(R.string.curtainText), room, fabric, width, height, mando, sentido, cdn, alto, counterValue, interValue, dobleValue, ops, moto, glat, ginf, supl, cenef, sup, opcional);
        } else {
            result = String.format(context.getResources().getString(R.string.curtainTextNoExtras), room, fabric, width, height, mando, sentido, cdn, alto, ops, opcional);
        }
        return result;
    }

    @Override
    public Curtain clone(){
        return new Curtain(height, width, fabric, room, cdn, mnd, sdo, alto, ops, counter, inter, doble, moto, glat, ginf, supl, cenef, sup, prod, optional, hasExtras, context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(fabric);
        dest.writeString(room);
        dest.writeString(cdn);
        dest.writeString(mnd);
        dest.writeString(sdo);
        dest.writeInt(alto);
        dest.writeString(ops);
        dest.writeByte((byte) (counter ? 0x01 : 0x00));
        dest.writeByte((byte) (inter ? 0x01 : 0x00));
        dest.writeByte((byte) (doble ? 0x01 : 0x00));
        dest.writeString(moto);
        dest.writeInt(glat);
        dest.writeInt(ginf);
        dest.writeString(supl);
        dest.writeInt(cenef);
        dest.writeString(sup);
        dest.writeString(prod);
        dest.writeByte((byte) (optional ? 0x01 : 0x00));
        dest.writeByte((byte) (hasExtras ? 0x01 : 0x00));
        dest.writeString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Curtain curtain = (Curtain) o;

        return id.equals(curtain.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}