package com.nic.taxcollection.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.taxcollection.Activity.AssessmentActivity;
import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;
import java.util.ArrayList;


public class TaxListAdapter extends RecyclerView.Adapter<TaxListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<Tax> taxArrayList;
    private ArrayList<Integer> taxImageList;
    LayoutInflater mInflater;
    private Context context;

    public TaxListAdapter(Activity activity, ArrayList<Tax> taxArrayList, ArrayList<Integer> taxImageList, Context context) {
        this.activity=activity;
        this.taxArrayList=taxArrayList;
        this.taxImageList=taxImageList;
        this.context=context;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.tax_item_view_2, viewGroup, false);
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
            holder.name.setText(taxArrayList.get(position).getTaxName());
            holder.tax_img.setImageResource(taxImageList.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoAssessmentSearch = new Intent(context, AssessmentActivity.class);
                    gotoAssessmentSearch.putExtra("tax_type_id",taxArrayList.get(position).getTaxId());
                    gotoAssessmentSearch.putExtra("tax_type_name",taxArrayList.get(position).getTaxName());
                    gotoAssessmentSearch.putExtra("image",taxImageList.get(position));
                    context.startActivity(gotoAssessmentSearch);
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
        TextView name;
        ImageView tax_img;
        //RelativeLayout tax_layout;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.taxName);
            tax_img=(ImageView)view.findViewById(R.id.tax_img);
            //tax_layout=(RelativeLayout)view.findViewById(R.id.tax_layout);

        }
    }



}
