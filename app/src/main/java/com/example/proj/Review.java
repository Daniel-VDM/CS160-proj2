package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Review extends AppCompatActivity {

    TextView NameReview;
    TextView AddressReview;
    Button Submit;
    FloatingActionButton floatingActionButton2;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        NameReview = findViewById(R.id.NameReview);
        AddressReview = findViewById(R.id.AddressReview);
        Submit = findViewById(R.id.Submit);
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);

        processIntent();
        updateInfo();
        setNextPageListeners();
    }

    private void setNextPageListeners() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateInfo() {
        try {
            NameReview.setText(jsonObject.getString("name"));
            AddressReview.setText(jsonObject.getString("formatted_address"));
        } catch (JSONException ignore) {
        }
    }

    private void processIntent() {
        try {
            Intent intent = getIntent();
            String jsonObjectString = intent.getStringExtra("JSON_OBJECT");
            jsonObject = new JSONObject(jsonObjectString);
        } catch (JSONException e) {
            Log.e("Review", e.toString());
        }
    }

}
