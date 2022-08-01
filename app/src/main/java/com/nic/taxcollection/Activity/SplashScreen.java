package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.nic.taxcollection.R;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    Button sign_in,sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initializeUI();
    }

    private void initializeUI() {
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);

        sign_in.setOnClickListener(this::onClick);
        sign_up.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in:
                gotoLoginScreen();
                break;
            case R.id.sign_up:
                break;
        }
    }

    private void gotoLoginScreen() {
        Intent intent = new Intent(SplashScreen.this,LoginScreen.class);
        intent.putExtra("Home","No");
        startActivity(intent);
    }
}
