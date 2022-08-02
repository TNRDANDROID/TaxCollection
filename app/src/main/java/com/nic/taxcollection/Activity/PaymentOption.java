package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.nic.taxcollection.Adapter.PaymentOptionAdapter;
import com.nic.taxcollection.Adapter.TaxCollectionListAdapter;
import com.nic.taxcollection.R;
import com.nic.taxcollection.api.Api;
import com.nic.taxcollection.api.ApiService;
import com.nic.taxcollection.api.ServerResponse;
import com.nic.taxcollection.constant.AppConstant;
import com.nic.taxcollection.model.Tax;
import com.nic.taxcollection.session.PrefManager;
import com.nic.taxcollection.utils.UrlGenerator;
import com.nic.taxcollection.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentOption extends AppCompatActivity implements Api.ServerResponseListener{
    private PrefManager prefManager;
    ArrayList<Tax> paymentTypeList;
    PaymentOptionAdapter paymentOptionAdapter;
    RecyclerView recyclerView;
    ImageView back_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        prefManager=new PrefManager(this);
        back_icon = findViewById(R.id.back_icon);
        recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        if(Utils.isOnline()){
            getPaymentTypeList();
        }
        else {
            Utils.showAlert(PaymentOption.this,"No Internet");
        }
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

                recyclerView.setVisibility(View.VISIBLE);
                paymentOptionAdapter = new PaymentOptionAdapter(PaymentOption.this,list,paymentImgList,PaymentOption.this);
                recyclerView.setAdapter(paymentOptionAdapter);
            }else {

                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public void adapterClickedPosition(int pos){
        String payment_mode = paymentTypeList.get(pos).getPaymenttype();
        if(payment_mode.equals("Cash")){

        }
        else {
            Intent goto_online_pay = new Intent(PaymentOption.this,PaymentActivity.class);
            startActivity(goto_online_pay);
            finish();
        }
    }
}
