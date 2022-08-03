package com.nic.taxcollection.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<Tax> taxCollections;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<Tax> pmgsySurvey, String type) {
        this.taxCollections = pmgsySurvey;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return taxCollections.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        Tax pmgsySurvey = taxCollections.get(position);

        if (type.equalsIgnoreCase("fin_Year_List")) {
            tv_type.setText(pmgsySurvey.getFin());
        }

        return view;
    }

}