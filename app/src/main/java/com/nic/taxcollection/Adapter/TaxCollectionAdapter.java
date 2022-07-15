package com.nic.taxcollection.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.taxcollection.R;
import com.nic.taxcollection.dataBase.DBHelper;
import com.nic.taxcollection.dataBase.dbData;
import com.nic.taxcollection.model.Tax;

import java.util.List;

public class TaxCollectionAdapter extends RecyclerView.Adapter<TaxCollectionAdapter.MyViewHolder> {

    private List<Tax> taxList;
    private Context context;
    private LayoutInflater layoutInflater;
    com.nic.taxcollection.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public TaxCollectionAdapter(List<Tax> taxList, Context context,dbData dbData) {
        this.taxList = taxList;
        this.context = context;
        this.dbData=dbData;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public TaxCollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TaxCollectionAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return taxList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
