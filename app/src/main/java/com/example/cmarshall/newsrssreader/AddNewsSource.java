package com.example.cmarshall.newsrssreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.skydoves.colorpickerview.ColorPickerView;

public class AddNewsSource extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news_source);//Set content to the add news source layout.
    }

    public void addNewsSourceButton(View view) {//A function that is called when the user clicks the button to add a new news source.
        EditText newsSourceNameInput = (EditText)findViewById(R.id.newsSourceName);//Get the element responsible for entering the name.
        EditText newsSourceURLInput = (EditText)findViewById(R.id.newsSourceURL);//Get the element responsible for entering the URL.
        ColorPickerView colorPickerView = (ColorPickerView)findViewById(R.id.colorPickerView);//Get the colour picker element on the page.
        int[] rgb = colorPickerView.getColorRGB();//Get the RGB values selected from the colour picker.
        Intent data = new Intent();//Create a new intent to pass data back to the main activity.
        data.putExtra("newsSourceName", newsSourceNameInput.getText().toString());//Get the news source name entered.
        data.putExtra("newsSourceURL", newsSourceURLInput.getText().toString());//Get the news source URL entered.
        data.putExtra("r", rgb[0]);//Get the red selected.
        data.putExtra("g", rgb[1]);//Get the green selected.
        data.putExtra("b", rgb[2]);//Get the blue selected.
        setResult(RESULT_OK, data);//Set the result of the closing activity to okay to indicate result was okay.
        finish();//End the activity.
    }

}
