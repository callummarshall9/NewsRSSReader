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

public class NewsSourceAdapter extends ArrayAdapter<NewsSource> {
        public NewsSourceAdapter(@NonNull Context context, ArrayList<NewsSource> sources) {
            super(context, 0, sources);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsSource newsPost = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_source_item, parent, false);
        }
        TextView title = (TextView)convertView.findViewById(R.id.sourceTitle);
        title.setTextSize(32);
        title.setText(newsPost.name);
        return convertView;
    }
}
