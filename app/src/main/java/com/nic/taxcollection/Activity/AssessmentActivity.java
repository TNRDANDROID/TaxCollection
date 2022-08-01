package com.nic.taxcollection.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nic.taxcollection.R;

public class AssessmentActivity extends AppCompatActivity {

    String tax_type_id,tax_type_name;
    Integer image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_activity);

        tax_type_id=getIntent().getStringExtra("tax_type_id");
        tax_type_name=getIntent().getStringExtra("tax_type_name");
        image=getIntent().getIntExtra("image",0);
        ImageView submit=findViewById(R.id.submit);
        ImageView tax_img=findViewById(R.id.tax_img);
        ImageView back=findViewById(R.id.back);
        TextView tax_name=findViewById(R.id.tax_name);
        TextView view_details=findViewById(R.id.view_details);
        RelativeLayout icon=findViewById(R.id.icon);
        RelativeLayout details=findViewById(R.id.details);
        icon.setVisibility(View.GONE);
        details.setVisibility(View.GONE);
        tax_name.setText(tax_type_name);
        tax_img.setImageResource(image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setVisibility(View.GONE);
                details.setVisibility(View.VISIBLE);
            }
        });
        view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentActivity.this,TaxCollectionListActivity.class);
                startActivity(intent);
            }
        });


    }
}
