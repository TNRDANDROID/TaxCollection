package com.nic.taxcollection.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;

import java.util.ArrayList;


public class TaxCollectionListAdapter extends RecyclerView.Adapter<TaxCollectionListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<Tax> taxArrayList;
    LayoutInflater mInflater;
    private Context context;

    public TaxCollectionListAdapter(Activity activity, ArrayList<Tax> taxArrayList, Context context) {
        this.activity=activity;
        this.taxArrayList=taxArrayList;
        this.context=context;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.tax_collection_item_view, viewGroup, false);
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
           /* if(position%2==0){
                holder.tax_layout.setBackground(activity.getResources().getDrawable(R.drawable.cerclebackgroundpurple));
            }
            else  {
                holder.tax_layout.setBackground(activity.getResources().getDrawable(R.drawable.cerclebackgroundyello));

            }*/
            holder.name.setText(taxArrayList.get(position).getName());
            holder.fin.setText(taxArrayList.get(position).getFin());
            holder.amount.setText(taxArrayList.get(position).getAmount());
            if(position==0){
                holder.layout_h.setVisibility(View.VISIBLE);
            }else {holder.layout_h.setVisibility(View.GONE);}

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   holder.check.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_accept));
                }
            });


        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return taxArrayList.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView fin,name,amount;
        ImageView check;
        LinearLayout layout_h;
        //RelativeLayout tax_layout;

        SummaryViewHolder(View view) {
            super(view);
            fin=(TextView)view.findViewById(R.id.fin);
            name=(TextView)view.findViewById(R.id.name);
            amount=(TextView)view.findViewById(R.id.amount);
            check=(ImageView)view.findViewById(R.id.check);
            layout_h=(LinearLayout)view.findViewById(R.id.layout_h);
            //tax_layout=(RelativeLayout)view.findViewById(R.id.tax_layout);

        }
    }



}
