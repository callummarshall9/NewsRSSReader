package com.example.cmarshall.newsrssreader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsEntry> {//Custom adapter designed to handle news article feeds.

    public NewsAdapter(@NonNull Context context, ArrayList<NewsEntry> newsEntries) {//Constructor for the function.
        super(context, 0, newsEntries);//Call the inherited method.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//When a request for a view is made.
        NewsEntry newsPost = getItem(position);//Get the news post in the array associated.
        if(convertView == null) {//If the view hasn't been created.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
            //Inflate the view with the news list item.
        }
        TextView firstLine = (TextView)convertView.findViewById(R.id.firstLine);//Get the first line from the news article entry.
        firstLine.setTextSize(32);//Set the text size.
        TextView secondLine = (TextView)convertView.findViewById(R.id.secondLine);//Get the second line from the news article entry.
        secondLine.setTextSize(16);//Set the text size.
        firstLine.setText(newsPost.title);//Set the title to the first line.
        secondLine.setText(newsPost.description);//Set the description to the second line.
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.newsImage);//Get the image on the page.
        downloadTask asyncTask = (downloadTask) new downloadTask(imageView, new downloadTask.AsyncResponse(){

            public void processFinish(Bitmap output){
                imageView.setImageBitmap(output);
            }
        }).execute(newsPost.imageURL);//Perform an asynchronous request for the image and once task is completed set it as the news image.
        return convertView;//Return the image created.
    }
}

