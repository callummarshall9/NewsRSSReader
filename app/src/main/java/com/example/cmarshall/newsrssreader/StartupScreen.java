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

    public void setupButton(View view) {//Function to setup a button.
        CheckBox guardianTechnology = (CheckBox)findViewById(R.id.guardianTechnology);//Get the checkbox for the guardian.
        CheckBox bbcNewsTechnology = (CheckBox)findViewById(R.id.bbcNewsTechnology);//Get the checkbox for the BBC news technology entry.
        CheckBox bbcNewsEducation = (CheckBox)findViewById(R.id.bbcNewsEducation);//Get the checkbox for the BBC news education entry.
        Intent returnData = new Intent();//Create a new intent to hold the data.
        returnData.putExtra("addGuardian", guardianTechnology.isChecked());//Add checkbox value of guardian.
        returnData.putExtra("addBBCTech", bbcNewsTechnology.isChecked());//Add checkbox value of BBC news technology.
        returnData.putExtra("addBBCEducation", bbcNewsEducation.isChecked());//Add checkbox value of BBC news education.
        setResult(RESULT_OK, returnData);//Set the end result of activity to OK and associate the intent with the close.
        finish();//Close the activity.
    }
}
