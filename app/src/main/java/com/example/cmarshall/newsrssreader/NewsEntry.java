package com.example.cmarshall.newsrssreader;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;

import java.text.DateFormat;
import java.util.Date;

import static android.graphics.Color.BLUE;

/**
 * Created by cmarshall on 21/04/17.
 */

public class NewsEntry {//A class detailing news article metadata.
    public String title;
    public String description;
    public String URL;
    public String imageURL;
    public String date;

    public NewsEntry(String title, String description, String URL, String pubDate) {//Constructor for the news article entry.
        //Set the associated data.
        this.title = title;
        this.description = description;
        this.URL = URL;
        this.date = pubDate;
        this.imageURL = "";
    }
}
