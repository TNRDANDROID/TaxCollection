package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.taxcollection.R;
import com.nic.taxcollection.api.Api;
import com.nic.taxcollection.api.ApiService;
import com.nic.taxcollection.api.ServerResponse;
import com.nic.taxcollection.constant.AppConstant;
import com.nic.taxcollection.session.PrefManager;
import com.nic.taxcollection.utils.UrlGenerator;
import com.nic.taxcollection.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements Api.ServerResponseListener {

    EditText user_name;
    EditText user_password;
    Button sign_in;
    ImageView visible_icon;
    private int setPType;
    private PrefManager prefManager;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        user_name=findViewById(R.id.user_name);
        user_password=findViewById(R.id.user_password);
        sign_in=findViewById(R.id.sign_in);
        visible_icon=findViewById(R.id.visible_icon);

        prefManager = new PrefManager(this);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLoginScreen();
            }
        });
        setPType=1;
        visible_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword(view);
            }
        });

        user_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        /*user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    visible_icon.setVisibility(View.VISIBLE);
                }
                else {
                    visible_icon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    visible_icon.setVisibility(View.VISIBLE);
                }
                else {
                    visible_icon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    visible_icon.setVisibility(View.VISIBLE);
                }
                else {
                    visible_icon.setVisibility(View.VISIBLE);
                }
            }
        });*/

    }
    public void showPassword(View view) {
        if (setPType == 1) {
            setPType = 0;
            user_password.setTransformationMethod(null);
            if (user_password.getText().length() > 0) {
                user_password.setSelection(user_password.getText().length());
                visible_icon.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24px);
            }
        } else {
            setPType = 1;
            user_password.setTransformationMethod(new PasswordTransformationMethod());
            if (user_password.getText().length() > 0) {
                user_password.setSelection(user_password.getText().length());
                visible_icon.setBackgroundResource(R.drawable.ic_baseline_visibility_24px);
            }
        }

    }
    public boolean validate() {
        boolean valid = true;
        String username = user_name.getText().toString().trim();
        prefManager.setUserName(username);
        String password = user_password.getText().toString().trim();
        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, this.getApplicationContext().getResources().getString(R.string.please_enter_the_username));
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, this.getApplicationContext().getResources().getString(R.string.please_enter_the_password));
        }
        return valid;
    }

    public void checkLoginScreen() {
        user_name.setText("vpsec1@gmail.com");
        user_password.setText("test123#$");
        final String username = user_name.getText().toString().trim();
        final String password = user_password.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, this.getApplicationContext().getResources().getString(R.string.please_enter_your_username_and_password));
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage(getApplicationContext().getResources().getString(R.string.internet_connection_not_available));
            ab.setPositiveButton(getApplicationContext().getResources().getString(R.string.settings),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            /*ab.setNegativeButton(getApplicationContext().getResources().getString(R.string.continue_with_off_line),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            //offline_mode(username, password);
                        }
                    });*/
            ab.show();
        }
    }


   /* public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");


        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + user_name.getText().toString().trim());





        String encryptUserPass = Utils.getSHA512(user_password.getText().toString().trim());
        String encryptUserPassword = Utils.md5(encryptUserPass);
        prefManager.setEncryptPass(encryptUserPassword);
        Log.d("SHA512", "" + encryptUserPass);


        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha256", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);
        params.put(AppConstant.KEY_APP_CODE,"VP");



        Log.d("user", "" + user_name.getText().toString().trim());

        Log.d("params", "" + params);
        return params;
    }*/
   public Map<String, String> loginParams() {
       Map<String, String> params = new HashMap<>();
       params.put(AppConstant.KEY_SERVICE_ID, "login");


       String random = Utils.randomChar();

       params.put(AppConstant.USER_LOGIN_KEY, random);
       Log.d("randchar", "" + random);

       params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
       Log.d("user", "" + user_name.getText().toString().trim());

       /* String encryptUserPass = Utils.md5(loginScreenBinding.password.getText().toString().trim());
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("md5", "" + encryptUserPass);*/



       String encryptUserPass = Utils.getSHA512(user_password.getText().toString().trim());
       String encryptUserPassword = Utils.md5(encryptUserPass);
       prefManager.setEncryptPass(encryptUserPassword);
       Log.d("SHA512", "" + encryptUserPass);


       String userPass = encryptUserPass.concat(random);
       Log.d("userpass", "" + userPass);
       String sha256 = Utils.getSHA(userPass);
       Log.d("sha256", "" + sha256);

       params.put(AppConstant.KEY_USER_PASSWORD, sha256);
       params.put(AppConstant.KEY_APP_CODE,"TP");
//        params.put(AppConstant.KEY_DEVICE_ID,android_id);


       Log.d("user", "" + user_name.getText().toString().trim());

       Log.d("params", "" + params);
       return params;
   }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = loginResponse.getString(AppConstant.KEY_STATUS);
            String response = loginResponse.getString(AppConstant.KEY_RESPONSE);

            if ("LoginScreen".equals(urlType)) {
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key =  Utils.NotNullString(loginResponse.getString(AppConstant.KEY_USER));
                        String user_data =  Utils.NotNullString(loginResponse.getString(AppConstant.USER_DATA));
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(decryptedKey, user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);
                        Log.d("userdatadecry", "" + jsonObject);
                        prefManager.setStateCode( Utils.NotNullString(jsonObject.getString(AppConstant.STATE_CODE)));
                        prefManager.setDistrictCode( Utils.NotNullString(jsonObject.getString(AppConstant.DISTRICT_CODE)));
                        prefManager.setBlockCode( Utils.NotNullString(jsonObject.getString(AppConstant.BLOCK_CODE)));
                        prefManager.setPvCode( Utils.NotNullString(jsonObject.getString(AppConstant.PV_CODE)));
                        prefManager.setLbType( Utils.NotNullString(jsonObject.getString("lbtype")));

                        prefManager.setDistrictName( Utils.NotNullString(jsonObject.getString("district_name_en")));
                        prefManager.setLbodyNameEn( Utils.NotNullString(jsonObject.getString("lbody_name_en")));
                        prefManager.setRoleCode( Utils.NotNullString(jsonObject.getString("role_code")));
                        prefManager.setRoleName( Utils.NotNullString(jsonObject.getString("role_name")));
                        prefManager.setUserFname( Utils.NotNullString(jsonObject.getString("user_first_name")));
                        prefManager.setUserLname( Utils.NotNullString(jsonObject.getString("user_last_name")));

                        Log.d("userdata", "" + prefManager.getStateCode() + prefManager.getDistrictCode() +
                                prefManager.getLbType() + prefManager.getTpCode() + prefManager.getLbodyNameEn() +
                                prefManager.getRoleCode()+prefManager.getRoleName()+prefManager.getUserFname()+prefManager.getUserLname());
                        prefManager.setUserPassKey(decryptedKey);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showHomeScreen();
                            }
                        }, 1000);

                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
        System.out.println(volleyError.toString());
    }

    private void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, DashboardActivity.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
