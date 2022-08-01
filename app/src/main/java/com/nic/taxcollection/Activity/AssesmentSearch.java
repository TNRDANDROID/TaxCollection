package com.nic.taxcollection.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.taxcollection.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssesmentSearch extends AppCompatActivity {

    TextView tax_name_text;
    RelativeLayout search_btn;
    EditText assessment_number_et;
    ImageView back_img;

    ////Intent Values;
    String tax_type_id;
    String tax_type_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assesment_search);

        tax_name_text = findViewById(R.id.tax_name);
        search_btn = findViewById(R.id.search_btn);
        assessment_number_et = findViewById(R.id.assessment_number_et);
        back_img = findViewById(R.id.back_icon);


        getIntentValues();



        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssesmentSearch.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getIntentValues(){
        tax_type_id = getIntent().getStringExtra("tax_type_id");
        tax_type_name = getIntent().getStringExtra("tax_type_name");

        tax_name_text.setText(tax_type_name);
    }

    /*private void arrayList(){
        try {
            ArrayList<Integer> arrayList = new ArrayList<>();
            ArrayList<Integer> arrayList_count= new ArrayList<>();
            HashMap<String, Integer> hash_map = new HashMap<String, Integer>();
            arrayList.add(1);
            arrayList.add(3);
            arrayList.add(3);
            arrayList.add(3);
            arrayList.add(5);
            arrayList.add(3);
            arrayList.add(5);
            arrayList.add(5);
            arrayList.add(5);
            arrayList.add(5);
            arrayList.add(5);
            int size = arrayList.size();
            int check_value = 0;
            while (size!=0) {
                int count=0;
                String value = String.valueOf(arrayList.get(check_value));
                for (int i = 0; i < arrayList.size(); i++) {
                    if (value.equals(arrayList.get(i))) {
                        count=count+1;
                    }
                }
                arrayList_count.add(count);
                hash_map.put(value,count);
                size--;
                check_value++;
            }

            //int highestNumber = Collections.max(arrayList_count);



            int maxValueInMap=(Collections.max(hash_map.values()));  // This will return max value in the Hashmap
            for (Map.Entry<String, Integer> entry : hash_map.entrySet()) {  // Itrate through hashmap
                if (entry.getValue()==maxValueInMap) {
                    System.out.println(".....Max >>"+entry.getKey());     // Print the key with max value
                }
            }
        }
        catch (Exception e){

        }

    }*/
}
