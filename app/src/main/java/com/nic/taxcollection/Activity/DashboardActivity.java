package com.nic.taxcollection.Activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.taxcollection.Adapter.TaxCollectionAdapter;
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

public class DashboardActivity extends AppCompatActivity implements Api.ServerResponseListener{


    Context context;

    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    public com.nic.taxcollection.dataBase.dbData dbData = new dbData(this);
    ArrayList<Tax> taxList;
    TaxCollectionAdapter taxCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.dashboard);
        context=this;
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);


        getTaxCollection();

        taxList = new ArrayList<>();
        dbData.open();
        taxList.addAll(dbData.get_tax_type());
        Log.d("Size",""+taxList.size());

        taxCollectionAdapter = new TaxCollectionAdapter(taxList,context,dbData);


    }

    public  void getTaxCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("TaxCollection", Api.Method.POST, UrlGenerator.getAppMainServiceUrl(), taxCollectionJsonParams(), "not cache", this);
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
//            JSONObject response = serverResponse.getJsonResponse();

            JSONObject response = new JSONObject();
            String json = "{\"STATUS\":\"OK\",\"RESPONSE\":\"OK\",\"JSON_DATA\":[{\"id\":1,\"tax\":\"property\"},{\"id\":2,\"tax\":\"nontax\"}]}";
            try {  response = new JSONObject(json); } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + json + "\""); }

            String urlType = serverResponse.getApi();

            if ("TaxCollection".equals(urlType) && response != null) {
                String key = response.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_TaxCollection().execute(jsonObject);
                }
                Log.d("TaxCollection", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insert_TaxCollection extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();

            dbData.deleteTaxTable(); ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setTaxId(jsonArray.getJSONObject(i).getString("id"));
                        tax.setTaxName(jsonArray.getJSONObject(i).getString("tax"));
                        dbData.insert_tax_type(tax);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
