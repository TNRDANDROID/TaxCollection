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

import com.nic.taxcollection.Interfcae.DemandItemClick;
import com.nic.taxcollection.R;
import com.nic.taxcollection.model.Tax;
import com.nic.taxcollection.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;


public class TaxCollectionListAdapter extends RecyclerView.Adapter<TaxCollectionListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<Tax> taxArrayList;
    LayoutInflater mInflater;
    private Context context;
    DemandItemClick demandItemClick;

    public TaxCollectionListAdapter(Activity activity, ArrayList<Tax> taxArrayList, Context context,DemandItemClick demandItemClick) {
        this.activity=activity;
        this.taxArrayList=taxArrayList;
        this.context=context;
        this.demandItemClick=demandItemClick;
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
            String year_name= taxArrayList.get(position).getFrom_month()+"-"+taxArrayList.get(position).getTo_month()+"-"
                    +taxArrayList.get(position).getInstallment_group_name();
            holder.name.setText(year_name);
            holder.fin.setText(taxArrayList.get(position).getFin());
            holder.amount.setText(taxArrayList.get(position).getDemand());
            if(position==0){
                holder.layout_h.setVisibility(View.VISIBLE);
            }else {holder.layout_h.setVisibility(View.GONE);}

            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == 0){
                        if(taxArrayList.get(position).getPayStatus()== 0){
                            Tax tax = taxArrayList.get(position);
                            tax.setPayStatus(1);
                            taxArrayList.set(position,tax);
                            notifyDataSetChanged();
                        }
                        else if(taxArrayList.get(position).getPayStatus()== 1){
                            if(taxArrayList.get(position+1).getPayStatus()== 0){
                                Tax tax = taxArrayList.get(position);
                                tax.setPayStatus(0);
                                taxArrayList.set(position,tax);
                                notifyDataSetChanged();
                            }else {
                                Utils.showAlert(activity,"You can't able to pay with pending year");
                            }

                        }
                    }
                    else if(position > 0 && position != taxArrayList.size()-1){
                        if(taxArrayList.get(position).getPayStatus()== 0){
                            if(taxArrayList.get(position-1).getPayStatus()== 1){
                                Tax tax = taxArrayList.get(position);
                                tax.setPayStatus(1);
                                taxArrayList.set(position,tax);
                                notifyDataSetChanged();
                            }else {
                                Utils.showAlert(activity,"Please select previous year");
                            }

                        }
                        else if(taxArrayList.get(position).getPayStatus()== 1){
                            if(taxArrayList.get(position+1).getPayStatus()== 0){
                                Tax tax = taxArrayList.get(position);
                                tax.setPayStatus(0);
                                taxArrayList.set(position,tax);
                                notifyDataSetChanged();
                            }else {
                                Utils.showAlert(activity,"You can't able to pay with pending year");
                            }
                        }

                    }
                    else if(position > 0 && position == taxArrayList.size()-1){

                        if(taxArrayList.get(position).getPayStatus()== 0){
                            if(taxArrayList.get(position-1).getPayStatus()== 1){
                                Tax tax = taxArrayList.get(position);
                                tax.setPayStatus(1);
                                taxArrayList.set(position,tax);
                                notifyDataSetChanged();
                            }else {
                                Utils.showAlert(activity,"You can't able to pay with pending year");
                            }

                        }
                        else if(taxArrayList.get(position).getPayStatus()== 1){
                                Tax tax = taxArrayList.get(position);
                                tax.setPayStatus(0);
                                taxArrayList.set(position,tax);
                                notifyDataSetChanged();
                        }
                    }

                    demandItemClick.demandItemClicked(taxArrayList);

                }
            });

            if(taxArrayList.get(position).getPayStatus()== 1){
                holder.check.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_accept));
            }else
            {
                holder.check.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_choose));
            }

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

    public String indianMoney(String amount){
        String amount_value="";
        try{
            Locale locale = new Locale("en","IN");
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
            dfs.setCurrencySymbol("\u20B9");
            decimalFormat.setDecimalFormatSymbols(dfs);
            System.out.println(decimalFormat.format(Integer.parseInt(amount)));
            amount_value = decimalFormat.format(Integer.parseInt(amount));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return amount_value;
    }


}
