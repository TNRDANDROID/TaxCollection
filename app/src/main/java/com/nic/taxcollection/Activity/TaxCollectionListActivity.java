package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nic.taxcollection.Adapter.PaymentOptionAdapter;
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
    ArrayList<Tax> paymentTypeList;
    PaymentOptionAdapter paymentOptionAdapter;
    RecyclerView recyclerViewPayment;
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
                showBottomSheetDialog();
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

            if ("PaymentTypeList".equals(urlType) && response != null) {
                String key = response.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);

                if (jsonObject.getString("STATUS").equalsIgnoreCase("SUCCESS") && jsonObject.getString("RESPONSE").equalsIgnoreCase("SUCCESS")) {
                    new Insert_PaymentTypeList().execute(jsonObject);
                }
                Log.d("PaymentTypeList", "" + jsonObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void adapterClickedPosition(int pos){
        String payment_mode = paymentTypeList.get(pos).getPaymenttype();
        if(payment_mode.equals("Cash")){

        }
        else {
            Intent goto_online_pay = new Intent(TaxCollectionListActivity.this,PaymentActivity.class);
            startActivity(goto_online_pay);
            finish();
        }
    }

    public class Insert_PaymentTypeList extends AsyncTask<JSONObject, Void, ArrayList<Tax>> {

        @Override
        protected ArrayList<Tax> doInBackground(JSONObject... params) {

            if (params.length > 0) {
                paymentTypeList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray("DATA");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setPaymenttypeid(jsonArray.getJSONObject(i).getString("paymenttypeid"));
                        tax.setPaymenttype(jsonArray.getJSONObject(i).getString("paymenttype"));
                        paymentTypeList.add(tax);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


            return paymentTypeList;


        }

        @Override
        protected void onPostExecute(ArrayList<Tax> list) {
            super.onPostExecute(list);
            if(list.size()>0){
                ArrayList<Integer> paymentImgList = new ArrayList<>();
                paymentImgList.add(R.drawable.cash_payment);
                paymentImgList.add(R.drawable.qr_payment);
                paymentImgList.add(R.drawable.card_payment);

                recyclerViewPayment.setVisibility(View.VISIBLE);
                paymentOptionAdapter = new PaymentOptionAdapter(TaxCollectionListActivity.this,list,paymentImgList,TaxCollectionListActivity.this);
                recyclerViewPayment.setAdapter(paymentOptionAdapter);
            }else {

                recyclerViewPayment.setVisibility(View.GONE);
            }
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View contentView = View.inflate(getApplicationContext(), R.layout.bottom_sheet_dialog_layout, null);

        bottomSheetDialog.setContentView(contentView);
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ((View) contentView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setPeekHeight(700);
            contentView.requestLayout();
        }
        recyclerViewPayment =bottomSheetDialog.findViewById(R.id.recycler_pay);

        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerViewPayment.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPayment.setHasFixedSize(true);
        recyclerViewPayment.setNestedScrollingEnabled(false);
        recyclerViewPayment.setFocusable(false);

        if(Utils.isOnline()){
            getPaymentTypeList();
        }
        else {
            Utils.showAlert(TaxCollectionListActivity.this,"No Internet");
        }

        bottomSheetDialog.show();
    }

    public  void getPaymentTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("PaymentTypeList", Api.Method.POST, UrlGenerator.getAppMainServiceUrl(), paymentTypeEncParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject paymentTypeEncParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), paymentTypeNormalParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("PaymentTypeList", "" + dataSet);
        return dataSet;
    }
    public JSONObject paymentTypeNormalParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "PaymentTypeList");
        dataSet.put("role_code", prefManager.getRoleCode());
        dataSet.put("language_name", "en");
        Log.d("PaymentTypeList", "" + dataSet);
        return dataSet;
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

}
