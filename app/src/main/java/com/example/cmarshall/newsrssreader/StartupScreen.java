package com.example.cmarshall.newsrssreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class StartupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
    }

    public void setupButton(View view) {
        CheckBox guardianTechnology = (CheckBox)findViewById(R.id.guardianTechnology);
        CheckBox bbcNewsTechnology = (CheckBox)findViewById(R.id.bbcNewsTechnology);
        CheckBox bbcNewsEducation = (CheckBox)findViewById(R.id.bbcNewsEducation);
        Intent returnData = new Intent();
        returnData.putExtra("addGuardian", guardianTechnology.isChecked());
        returnData.putExtra("addBBCTech", bbcNewsTechnology.isChecked());
        returnData.putExtra("addBBCEducation", bbcNewsEducation.isChecked());
        setResult(RESULT_OK, returnData);
        finish();
    }
}
