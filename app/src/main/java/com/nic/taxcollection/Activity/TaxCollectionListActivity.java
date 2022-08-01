package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.taxcollection.Adapter.TaxCollectionListAdapter;
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

public class TaxCollectionListActivity extends AppCompatActivity implements Api.ServerResponseListener{


    Context context;

    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    public com.nic.taxcollection.dataBase.dbData dbData = new dbData(this);
    ArrayList<Tax> taxList;
    TaxCollectionListAdapter taxListAdapter;
    RecyclerView recyclerView;
    TextView no_data_found;
    SnapHelper snapHelper;

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

        recyclerView=findViewById(R.id.recycler);
        no_data_found=findViewById(R.id.no_data_found);
       /* RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        //snapHelper = new PagerSnapHelper();
        //snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

//        getTaxCollection();

        JSONObject jsonObject = new JSONObject();
        taxList = new ArrayList<>();
        String json = "{\"STATUS\":\"OK\",\"RESPONSE\":\"OK\",\"JSON_DATA\":[{\"id\":1,\"fin\":\"2014-2015\",\"name\":\"first\",\"amount\":\"1000\"},{\"id\":2,\"fin\":\"2015-2016\",\"name\":\"sec\",\"amount\":\"3000\"},{\"id\":3,\"fin\":\"2016-2017\",\"name\":\"third\",\"amount\":\"6000\"},{\"id\":4,\"fin\":\"2017-2018\",\"name\":\"fourth\",\"amount\":\"8000\"}]}";
        try {  jsonObject = new JSONObject(json); } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\""); }


        try {
            if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                JSONArray jsonArray = new JSONArray();
                jsonArray=jsonObject.getJSONArray("JSON_DATA");
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setId(jsonArray.getJSONObject(i).getString("id"));
                        tax.setFin(jsonArray.getJSONObject(i).getString("fin"));
                        tax.setName(jsonArray.getJSONObject(i).getString("name"));
                        tax.setAmount(jsonArray.getJSONObject(i).getString("amount"));

                        taxList.add(tax);
                        Log.d("Size",""+taxList.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                taxListAdapter = new TaxCollectionListAdapter(TaxCollectionListActivity.this,taxList,TaxCollectionListActivity.this);
                recyclerView.setAdapter(taxListAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  void getTaxCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("TaxCollection", Api.Method.POST, UrlGenerator.getLoginUrl(), taxCollectionJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject taxCollectionJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), taxCollectionParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TaxCollection", "" + dataSet);
        return dataSet;
    }
    public JSONObject taxCollectionParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TaxCollection");
        Log.d("TaxCollection", "" + dataSet);
        return dataSet;
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
                    new Insert_TaxCollection().execute(jsonObject);
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

    public class Insert_TaxCollection extends AsyncTask<JSONObject, Void, ArrayList<Tax>> {

        @Override
        protected ArrayList<Tax> doInBackground(JSONObject... params) {
            dbData.open();

            dbData.deleteTaxTable(); ;

            if (params.length > 0) {
                taxList = new ArrayList<>();
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
                        taxList.add(tax);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


            return taxList;


        }

        @Override
        protected void onPostExecute(ArrayList<Tax> list) {
            super.onPostExecute(list);
            if(list.size()>0){
                no_data_found.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            taxListAdapter = new TaxCollectionListAdapter(TaxCollectionListActivity.this,list,TaxCollectionListActivity.this);
            recyclerView.setAdapter(taxListAdapter);
        }else {
                no_data_found.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

}
