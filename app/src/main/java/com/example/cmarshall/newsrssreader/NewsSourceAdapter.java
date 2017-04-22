package com.example.cmarshall.newsrssreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cmarshall on 22/04/17.
 */

public class NewsSourceAdapter extends ArrayAdapter<NewsSource> {//Custom adapter designed to display news sources.
        public NewsSourceAdapter(@NonNull Context context, ArrayList<NewsSource> sources) {//Constructor method.
            super(context, 0, sources);//Call inherited method.
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//When a request for the view is made.
        NewsSource newsPost = getItem(position);//Get the news source entered.
        if(convertView == null) {//If the news source entry view hasn't been created.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_source_item, parent, false);
            //Create the news source entry view.
        }
        TextView title = (TextView)convertView.findViewById(R.id.sourceTitle);//Get the text view responsible for displaying the source title.
        title.setTextSize(32);//Set the text size to large (Relative)
        title.setText(newsPost.name);//Set the title to the source name.
        return convertView;//Return the view created.
    }
}
