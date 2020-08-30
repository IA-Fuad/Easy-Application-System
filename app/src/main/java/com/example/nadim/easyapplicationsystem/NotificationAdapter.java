package com.example.nadim.easyapplicationsystem;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<String> noteArrayList;

    public NotificationAdapter(Context context, int layout, ArrayList<String> noteArrayList) {
        this.context = context;
        this.layout = layout;
        this.noteArrayList = noteArrayList;
    }

    @Override
    public int getCount() {
        return noteArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView notificationText, statusIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);

            viewHolder.notificationText = view.findViewById(R.id.notificationText);
            viewHolder.statusIcon = view.findViewById(R.id.statusIcon);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

}