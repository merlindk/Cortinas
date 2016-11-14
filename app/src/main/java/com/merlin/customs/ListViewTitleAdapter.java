package com.merlin.customs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.merlin.cortina.R;
import com.merlin.entities.Curtain;

import java.util.ArrayList;
import java.util.List;

public class ListViewTitleAdapter extends BaseAdapter {

    public ArrayList<List<String>> productList;
    Activity activity;


    public ListViewTitleAdapter(Activity activity, ArrayList<List<String>> productList) {
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

        List<String> item = productList.get(position);
        holder.mFabric.setText(item.get(0));
        holder.mWidth.setText(item.get(1));
        holder.mHeight.setText(item.get(2));
        holder.mMnd.setText(item.get(3));
        holder.mSdo.setText(item.get(4));
        holder.mCdn.setText(item.get(5));
        holder.mAlto.setText(item.get(6));
        holder.mRoom.setText(item.get(7));

        return convertView;
    }

    public void remove(Curtain toRemove) {
        productList.remove(toRemove);
        notifyDataSetChanged();
    }
}
