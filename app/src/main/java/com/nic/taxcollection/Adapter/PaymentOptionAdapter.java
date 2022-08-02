package com.nic.taxcollection.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nic.taxcollection.Activity.PaymentOption;
import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;

import java.util.ArrayList;

public class PaymentOptionAdapter extends RecyclerView.Adapter<PaymentOptionAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<Tax> paymentList;
    private ArrayList<Integer> paymentIconList;
    LayoutInflater mInflater;
    private Context context;

    public PaymentOptionAdapter(Activity activity, ArrayList<Tax> paymentList, ArrayList<Integer> paymentIconList, Context context) {
        this.activity=activity;
        this.paymentList = paymentList;
        this.paymentIconList = paymentIconList;
        this.context=context;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public PaymentOptionAdapter.SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.payment_option_item_view, viewGroup, false);
        PaymentOptionAdapter.SummaryViewHolder mainHolder = new PaymentOptionAdapter.SummaryViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }
    @Override
    public void onBindViewHolder(final PaymentOptionAdapter.SummaryViewHolder holder, final int position) {

        try {

            holder.payment_name.setText(paymentList.get(position).getPaymenttype());
            holder.payment_icon.setImageResource(paymentIconList.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PaymentOption)context).adapterClickedPosition(position);
                }
            });


        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return paymentList.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView payment_name;
        ImageView payment_icon;


        SummaryViewHolder(View view) {
            super(view);
            payment_name =(TextView)view.findViewById(R.id.payment_name);
            payment_icon =(ImageView)view.findViewById(R.id.payment_img);

        }
    }



}
