package com.nic.taxcollection.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.nic.taxcollection.Adapter.CommonAdapter;
import com.nic.taxcollection.Adapter.TaxListAdapter;
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

public class AssessmentActivity extends AppCompatActivity implements Api.ServerResponseListener{

    PrefManager prefManager;
    String tax_type_id,tax_type_name;
    Integer image;
    LinearLayout fin_year_layout;
    Spinner fin_year_spinner;
    ImageView submit,back,tax_img;
    TextView number_title_text,tax_name,view_details;
    RelativeLayout icon,details;
    EditText number;
    ArrayList<Tax> finYearList;
    ArrayList<Tax> assessmentDetailsList;
    ArrayList<Tax> demandCollectionDetailsList;
    String fin_year="";

    ///////Details View
    TextView district_name;
    TextView village_name;
    TextView assessment_number;
    TextView name;
    TextView door_number;
    TextView building_licence_number;
    TextView total_area;
    TextView ward_name;
    TextView habitation_name;
    TextView street_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_activity);

       initializeUI();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTaxCollectionDetailsActivity();
            }
        });

        fin_year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position>0){
                    fin_year = finYearList.get(position).getFin();
                }
                else {
                    fin_year="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public  void getFinYear() {
        try {
            new ApiService(this).makeJSONObjectRequest("FinYear", Api.Method.POST, UrlGenerator.getAppMainServiceUrl(), finYearEncryptParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject finYearEncryptParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), finYearNormalParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("FinYear", "" + dataSet);
        return dataSet;
    }
    public JSONObject finYearNormalParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "FinYearList");
        Log.d("FinYear", "" + dataSet);
        return dataSet;
    }
    private void initializeUI() {
        prefManager=new PrefManager(this);
        tax_type_id=getIntent().getStringExtra("tax_type_id");
        tax_type_name=getIntent().getStringExtra("tax_type_name");
        image=getIntent().getIntExtra("image",0);

        submit=findViewById(R.id.submit);
        tax_img=findViewById(R.id.tax_img);
        back=findViewById(R.id.back);
        tax_name=findViewById(R.id.tax_name);
        view_details=findViewById(R.id.view_details);
        icon=findViewById(R.id.icon);
        details=findViewById(R.id.details);
        fin_year_layout=findViewById(R.id.fin_year_layout);
        fin_year_spinner=findViewById(R.id.fin_year_spinner);
        number_title_text=findViewById(R.id.number_tv);
        number=findViewById(R.id.number);

        view_details.setVisibility(View.GONE);
        /////Details Text Views
        district_name=findViewById(R.id.district);
        village_name=findViewById(R.id.village);
        assessment_number=findViewById(R.id.assessment_number);
        name=findViewById(R.id.name);
        door_number=findViewById(R.id.door_no);
        building_licence_number=findViewById(R.id.building_licence_number);
        total_area=findViewById(R.id.total_area);
        ward_name=findViewById(R.id.ward_name);
        habitation_name=findViewById(R.id.habitation_name);
        street_name=findViewById(R.id.street_name);
        ////
        tax_name.setText(tax_type_name);
        tax_img.setImageResource(image);

        switch (tax_type_name){
            case "Property Tax":
                number_title_text.setText(getResources().getString(R.string.assesment_number));
                number.setHint(getResources().getString(R.string.enter_assesment_numbert));
                fin_year_layout.setVisibility(View.GONE);
                break;
            case "Water Charges":
                number_title_text.setText(getResources().getString(R.string.water_connection_number));
                number.setHint(getResources().getString(R.string.water_connection_number));
                fin_year_layout.setVisibility(View.GONE);
                break;
            case "Professional Tax":
                if(Utils.isOnline()){
                    getFinYear();
                }
                else {
                    Utils.showAlert(AssessmentActivity.this,"No Internet");
                }
                getFinYear();
                fin_year_layout.setVisibility(View.VISIBLE);
                number_title_text.setText(getResources().getString(R.string.water_connection_number));
                number.setHint(getResources().getString(R.string.water_connection_number));
                break;
            case "Non Tax":
                fin_year_layout.setVisibility(View.GONE);
                number_title_text.setText(getResources().getString(R.string.lessee_number));
                number.setHint(getResources().getString(R.string.lessee_number));
                break;
            case "Trade License":
                fin_year_layout.setVisibility(View.GONE);
                number_title_text.setText(getResources().getString(R.string.trade_number));
                number.setHint(getResources().getString(R.string.trade_number));
                break;

        }
    }

    private void checkValidation(){
        switch (tax_type_name){
            case "Property Tax":
                if(!number.getText().toString().equals("")){
                    if(Utils.isOnline()){
                        getTaxCollection();
                    }
                    else {
                        Utils.showAlert(AssessmentActivity.this,"No Internet");
                    }
                }
                else {
                    Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.assesment_number));
                }
                break;
            case "Water Charges":
                if(!number.getText().toString().equals("")){
                    if(Utils.isOnline()){
                        getTaxCollection();
                    }
                    else {
                       Utils.showAlert(AssessmentActivity.this,"No Internet");
                    }

                }
                else {
                    Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.water_connection_number));
                }
                break;
            case "Professional Tax":
                if(!fin_year.equals("")){
                    if(!number.getText().toString().equals("")){
                        if(Utils.isOnline()){
                            getTaxCollection();
                        }
                        else {
                            Utils.showAlert(AssessmentActivity.this,"No Internet");
                        }
                    }
                    else {
                        Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.assesment_number));
                    }
                }
                else {
                    Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.fin_year));
                }
                break;
            case "Non Tax":
                if(!number.getText().toString().equals("")){
                    if(Utils.isOnline()){
                        getTaxCollection();
                    }
                    else {
                        Utils.showAlert(AssessmentActivity.this,"No Internet");
                    }
                }
                else {
                    Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.lessee_number));
                }
                break;
            case "Trade License":
                if(!number.getText().toString().equals("")){
                    if(Utils.isOnline()){
                        getTaxCollection();
                    }
                    else {
                        Utils.showAlert(AssessmentActivity.this,"No Internet");
                    }
                }
                else {
                    Utils.showAlert(AssessmentActivity.this,getResources().getString(R.string.trade_number));
                }
                break;

        }
    }
    public  void getTaxCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("DemandSelectionList", Api.Method.POST, UrlGenerator.getAppMainServiceUrl(), taxCollectionJsonParams(), "not cache", this);
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
        JSONObject dataSet1 = new JSONObject();
        JSONObject dataSet2= new JSONObject();
        JSONArray number_array=new JSONArray();
        dataSet2.put("assessment_no",number.getText().toString());
        number_array.put(dataSet2);
        dataSet1.put("Assessment_Details",number_array);
        dataSet.put(AppConstant.KEY_SERVICE_ID, "DemandSelectionList");
        dataSet.put("role_code", prefManager.getRoleCode());
        dataSet.put("taxtypeid", tax_type_id);
        dataSet.put("language_name", "en");
        dataSet.put("assessment_no_list", dataSet1);
        Log.d("DemandSelectionList", "" + dataSet);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject response = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("FinYear".equals(urlType) && response != null) {
                String key = response.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);

                if (jsonObject.getString("STATUS").equalsIgnoreCase("SUCCESS") && jsonObject.getString("RESPONSE").equalsIgnoreCase("SUCCESS")) {
                    new Insert_FinYear().execute(jsonObject);
                }
                Log.d("FinYear", "" + jsonObject);
            }
            else if ("DemandSelectionList".equals(urlType) && response != null) {
                String key = response.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);

                if (jsonObject.getString("STATUS").equalsIgnoreCase("SUCCESS") && jsonObject.getString("RESPONSE").equalsIgnoreCase("SUCCESS")) {
                    new Insert_DemandCollectionList().execute(jsonObject);
                }
                Log.d("FinYear", "" + jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insert_FinYear extends AsyncTask<JSONObject, Void, ArrayList<Tax>> {

        @Override
        protected ArrayList<Tax> doInBackground(JSONObject... params) {

            if (params.length > 0) {
                finYearList = new ArrayList<>();
                Tax tax1 = new Tax();
                tax1.setFin(getResources().getString(R.string.select_fin_year));
                finYearList.add(tax1);
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray("DATA");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setFin(jsonArray.getJSONObject(i).getString("fin_year"));

                        finYearList.add(tax);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


            return finYearList;


        }

        @Override
        protected void onPostExecute(ArrayList<Tax> list) {
            super.onPostExecute(list);
            if(list.size()>0){
                fin_year_spinner.setAdapter(new CommonAdapter(AssessmentActivity.this,list,"fin_Year_List"));
            }

        }
    }
    public class Insert_DemandCollectionList extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            if (params.length > 0) {

                JSONObject data = new JSONObject();
                JSONObject other_payment_list = new JSONObject();
                JSONArray ASSESSMENT_DETAILS = new JSONArray();

                try {
                    data = params[0].getJSONObject("DATA");
                    ASSESSMENT_DETAILS = data.getJSONArray("ASSESSMENT_DETAILS");
                    other_payment_list=data.getJSONObject("other_payment_list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assessmentDetailsList = new ArrayList<>();
                for (int i = 0; i <ASSESSMENT_DETAILS.length(); i++) {

                    try {
                        Tax tax = new Tax();
                        tax.setTaxId(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("taxtypeid")));
                        tax.setStatecode(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("statecode")));
                        tax.setState_name(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("state_name")));
                        tax.setDistictCode(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("dcode")));
                        tax.setDistrict_name(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("district_name")));
                        tax.setLbcode(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("lbcode")));
                        tax.setLocalbody_name(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("localbody_name")));
                        tax.setBlockCode(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("bcode")));
                        tax.setLbtype(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("lbtype")));
                        tax.setAssessment_id(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("assessment_id")));
                        tax.setAssessment_no(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("assessment_no")));
                        tax.setName(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("name")));
                        tax.setDoor_no(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("door_no")));
                        tax.setAssessment_address(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("assessment_address")));
                        tax.setArea_in_sq_feet(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("area_in_sq_feet")));
                        tax.setSurveyno(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("surveyno")));
                        tax.setSubdivisionno(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("subdivisionno")));
                        tax.setWardid(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("wardid")));
                        tax.setWard_code(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("ward_code")));
                        tax.setWard_name_en(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("ward_name_en")));
                        tax.setStreetid(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("streetid")));
                        tax.setBuilding_licence_no(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("building_licence_no")));
                        tax.setStreet_name_en(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("street_name_en")));
                        tax.setStreet_name_ta(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("street_name_ta")));
                        tax.setHabitation_name_en(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("habitation_name_en")));
                        tax.setHabitation_name_ta(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("habitation_name_ta")));
                        tax.setHab_code(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("hab_code")));
                        tax.setProperty_available_advance(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("property_available_advance")));
                        tax.setSwm_available_advance(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("swm_available_advance")));
                        tax.setNo_of_demand_available(Utils.NotNullString(ASSESSMENT_DETAILS.getJSONObject(i).getString("no_of_demand_available")));

                        assessmentDetailsList.add(tax);

                        JSONArray DEMAND_DETAILS = ASSESSMENT_DETAILS.getJSONObject(i).getJSONArray("DEMAND_DETAILS");
                        demandCollectionDetailsList = new ArrayList<>();
                        for (int j=0;j<DEMAND_DETAILS.length();j++){
                            Tax demandDetails = new Tax();
                            demandDetails.setTaxId(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("taxtypeid")));
                            demandDetails.setAssessment_id(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("assesmentid")));
                            demandDetails.setFin(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("fin_year")));
                            demandDetails.setInstallmenttype_group_id(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("installmenttype_group_id")));
                            demandDetails.setInstallment_group_no(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("installment_group_no")));
                            demandDetails.setInstallment_group_name(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("installment_group_name")));
                            demandDetails.setFrom_month(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("from_month")));
                            demandDetails.setTo_month(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("to_month")));
                            demandDetails.setDemand(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("demand")));
                            demandDetails.setDemandid(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("demandid")));
                            demandDetails.setDemand_count(Utils.NotNullString(DEMAND_DETAILS.getJSONObject(i).getString("demand_count")));
                            demandDetails.setPayStatus(0);

                            demandCollectionDetailsList.add(demandDetails);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


            return null;


        }

        @Override
        protected void onPostExecute(Void void1) {
            super.onPostExecute(void1);
            details.setVisibility(View.VISIBLE);
            view_details.setVisibility(View.VISIBLE);

            if(assessmentDetailsList.size()>0){
                for(int i=0;i<assessmentDetailsList.size();i++){
                        district_name.setText(": "+assessmentDetailsList.get(i).getDistrict_name());
                        village_name.setText(": "+assessmentDetailsList.get(i).getLocalbody_name());
                        building_licence_number.setText(": "+assessmentDetailsList.get(i).getBuilding_licence_no());
                        assessment_number.setText(": "+assessmentDetailsList.get(i).getAssessment_no());
                        name.setText(": "+assessmentDetailsList.get(i).getName());
                        door_number.setText(": "+assessmentDetailsList.get(i).getDoor_no());
                        ward_name.setText(": "+assessmentDetailsList.get(i).getWard_name_en());
                        habitation_name.setText(": "+assessmentDetailsList.get(i).getHabitation_name_en());
                        street_name.setText(": "+assessmentDetailsList.get(i).getStreet_name_en());
                        total_area.setText(": "+assessmentDetailsList.get(i).getArea_in_sq_feet());
                }

            }

        }
    }

    private void gotoTaxCollectionDetailsActivity(){
        if(assessmentDetailsList.size()>0) {
            Intent intent = new Intent(AssessmentActivity.this, TaxCollectionListActivity.class);
            intent.putExtra("DemandList", demandCollectionDetailsList);
            intent.putExtra("property_available_advance", assessmentDetailsList.get(0).getProperty_available_advance());
            startActivity(intent);
        }
    }

}
