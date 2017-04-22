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

/**
 * Created by cmarshall on 21/04/17.
 */

public class NewsAdapter extends ArrayAdapter<NewsEntry> {

    public NewsAdapter(@NonNull Context context, ArrayList<NewsEntry> newsEntries) {
        super(context, 0, newsEntries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsEntry newsPost = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        TextView firstLine = (TextView)convertView.findViewById(R.id.firstLine);
        firstLine.setTextSize(32);
        TextView secondLine = (TextView)convertView.findViewById(R.id.secondLine);
        secondLine.setTextSize(16);

        firstLine.setText(newsPost.title);
        secondLine.setText(newsPost.description);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.newsImage);
        downloadTask asyncTask = (downloadTask) new downloadTask(imageView, new downloadTask.AsyncResponse(){

            public void processFinish(Bitmap output){
                imageView.setImageBitmap(output);
            }
        }).execute(newsPost.imageURL);
        return convertView;
    }
}

