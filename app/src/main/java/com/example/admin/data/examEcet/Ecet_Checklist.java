package com.example.admin.data.examEcet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.data.R;

public class Ecet_Checklist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecet__checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
