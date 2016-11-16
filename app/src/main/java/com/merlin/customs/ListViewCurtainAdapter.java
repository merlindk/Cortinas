package com.merlin.customs;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.merlin.cortina.R;
import com.merlin.entities.Curtain;

import java.util.ArrayList;

public class ListViewCurtainAdapter extends BaseAdapter {

    public ArrayList<Curtain> productList;
    private Activity activity;


    public ListViewCurtainAdapter(Activity activity, ArrayList<Curtain> productList) {
        super();
        this.activity = activity;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.mFabric = (TextView) convertView.findViewById(R.id.cellFabric);
            holder.mWidth = (TextView) convertView.findViewById(R.id.cellWidth);
            holder.mHeight = (TextView) convertView.findViewById(R.id.cellHeight);
            holder.mMnd = (TextView) convertView.findViewById(R.id.cellMnd);
            holder.mSdo = (TextView) convertView.findViewById(R.id.cellSdo);
            holder.mCdn = (TextView) convertView.findViewById(R.id.cellCdn);
            holder.mAlto = (TextView) convertView.findViewById(R.id.cellAlto);
            holder.mRoom = (TextView) convertView.findViewById(R.id.cellRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Curtain item = productList.get(position);
        holder.mFabric.setText(item.getFabric());
        holder.mWidth.setText(String.valueOf(item.getWidth()));
        holder.mHeight.setText(String.valueOf(item.getHeight()));
        holder.mMnd.setText(item.getMnd());
        holder.mSdo.setText(item.getSdo());
        holder.mCdn.setText(item.getCdn());
        holder.mAlto.setText(String.valueOf(item.getAlto()));
        holder.mRoom.setText(item.getRoom());
        if (item.isOptional()) {
            holder.mFabric.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mWidth.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mHeight.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mMnd.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mSdo.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mCdn.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mAlto.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
            holder.mRoom.setTextColor(ContextCompat.getColor(activity, R.color.OPTIONAL));
        } else {
            holder.mFabric.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mWidth.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mHeight.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mMnd.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mSdo.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mCdn.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mAlto.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
            holder.mRoom.setTextColor(ContextCompat.getColor(activity, R.color.NORMAL));
        }
        return convertView;
    }

    public void remove(Curtain toRemove) {
        productList.remove(toRemove);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView mFabric;
        TextView mWidth;
        TextView mHeight;
        TextView mMnd;
        TextView mSdo;
        TextView mCdn;
        TextView mAlto;
        TextView mRoom;
    }
}
