package com.nic.taxcollection.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;
import java.util.ArrayList;


public class TaxCollectionAdapter extends RecyclerView.Adapter<TaxCollectionAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<Tax> taxArrayList;
    LayoutInflater mInflater;


    public TaxCollectionAdapter( Activity activity, ArrayList<Tax> taxArrayList) {
        this.activity=activity;
        this.taxArrayList=taxArrayList;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.tax_item_view, viewGroup, false);
        SummaryViewHolder mainHolder = new SummaryViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }
    @Override
    public void onBindViewHolder(final SummaryViewHolder holder,final int position) {

        try {
            if(position%2==0){
                holder.tax_layout.setBackground(activity.getResources().getDrawable(R.drawable.cerclebackgroundpurple));
            }else  {
                holder.tax_layout.setBackground(activity.getResources().getDrawable(R.drawable.cerclebackgroundyello));

            }
            holder.name.setText(taxArrayList.get(position).getTaxName());


        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return taxArrayList.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout tax_layout;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.taxName);
            tax_layout=(RelativeLayout)view.findViewById(R.id.tax_layout);

        }
    }



}
