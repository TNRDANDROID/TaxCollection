package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.taxcollection.Adapter.TaxCollectionListAdapter;
import com.nic.taxcollection.Interfcae.DemandItemClick;
import com.nic.taxcollection.R;
import com.nic.taxcollection.api.Api;
import com.nic.taxcollection.api.ApiService;
import com.nic.taxcollection.api.ServerResponse;
import com.nic.taxcollection.constant.AppConstant;
import com.nic.taxcollection.dataBase.DBHelper;
import com.nic.taxcollection.dataBase.dbData;
import com.nic.taxcollection.model.Tax;
import com.nic.taxcollection.session.PrefManager;
import com.nic.taxcollection.utils.UrlGenerator;
import com.nic.taxcollection.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaxCollectionListActivity extends AppCompatActivity implements Api.ServerResponseListener, DemandItemClick {


    Context context;

    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    public com.nic.taxcollection.dataBase.dbData dbData = new dbData(this);
    ArrayList<Tax> demandList;
    TaxCollectionListAdapter taxListAdapter;
    RecyclerView recyclerView;
    TextView no_data_found,payment,reset,total_amount_value;
    SnapHelper snapHelper;
    String property_available_advance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.tax_collection_activity);
        context=this;
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView back=findViewById(R.id.back);

        recyclerView=findViewById(R.id.recycler);
        no_data_found=findViewById(R.id.no_data_found);
        payment=findViewById(R.id.payment);
        reset=findViewById(R.id.reset);
        total_amount_value=findViewById(R.id.total_amount_value);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payment_option = new Intent(TaxCollectionListActivity.this,PaymentOption.class);
                startActivity(payment_option);
            }
        });
        property_available_advance = getIntent().getStringExtra("property_available_advance");
        setDemandAdapter();
    }



    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject response = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("TaxCollection".equals(urlType) && response != null) {
                String key = response.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);

                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    //new Insert_TaxCollection().execute(jsonObject);
                }
                Log.d("TaxCollection", "" + jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    private void setDemandAdapter(){
        ArrayList<Tax> myList = (ArrayList<Tax>) getIntent().getSerializableExtra("DemandList");
        demandList = new ArrayList<>();
        demandList.addAll(myList);
        if(demandList.size()>0){
            no_data_found.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            System.out.println("list size >>"+demandList.size());
            taxListAdapter = new TaxCollectionListAdapter(TaxCollectionListActivity.this,demandList,TaxCollectionListActivity.this,this::demandItemClicked);
            recyclerView.setAdapter(taxListAdapter);
        }
        else {
            no_data_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void demandItemClicked(ArrayList<Tax> clickedList) {
        try {
            int total_amount=0;
            for(int i=0;i<clickedList.size();i++){
                if(clickedList.get(i).getPayStatus()==1){
                    total_amount=total_amount+Math.round(Float.parseFloat(clickedList.get(i).getDemand()));
                }
            }
            //total_amount = total_amount+Math.round(Float.parseFloat(property_available_advance));
            total_amount_value.setText(Utils.indianMoney(String.valueOf(total_amount)));
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }

    }

    /*public class Insert_TaxCollection extends AsyncTask<JSONObject, Void, ArrayList<Tax>> {

        @Override
        protected ArrayList<Tax> doInBackground(JSONObject... params) {
            if (params.length > 0) {
                demandList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setId(jsonArray.getJSONObject(i).getString("id"));
                        tax.setFin(jsonArray.getJSONObject(i).getString("fin"));
                        tax.setName(jsonArray.getJSONObject(i).getString("name"));
                        tax.setAmount(jsonArray.getJSONObject(i).getString("amount"));
                        tax.setPayStatus(0);
                        demandList.add(tax);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


            return demandList;


        }

        @Override
        protected void onPostExecute(ArrayList<Tax> list) {
            super.onPostExecute(list);
            if(list.size()>0){
                no_data_found.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                System.out.println("list size >>"+list.size());
            taxListAdapter = new TaxCollectionListAdapter(TaxCollectionListActivity.this,list,TaxCollectionListActivity.this);
            recyclerView.setAdapter(taxListAdapter);
        }
            else {
                no_data_found.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }*/

}
