package com.atinasoft.mucahitkambur.lolping;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muco on 26.05.2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{

    private final Context activity;
    private List<String> arrayList;

    public CustomSpinnerAdapter(Context activity, List<String> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER);
        txt.setText(arrayList.get(position));
        txt.setTextColor(Color.parseColor("#f9c129"));

        return txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setText(arrayList.get(position));
        txt.setTextColor(Color.parseColor("#f9c129"));

        return  txt;
    }

}
