package com.example.cmarshall.newsrssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class downloadTask extends AsyncTask<String, Void, Bitmap> {//A function to download images in the background to prevent blocking of the UI.
    public interface AsyncResponse {//An interface to allow for communication between the asynchronous task result and the other threads.
        void processFinish(Bitmap output);//The signal to be emitted.
    }

    public AsyncResponse delegate = null;//The delegate to call when the process is finished.

    public downloadTask(ImageView imageView, AsyncResponse delegate){//Constructor for downloading an image.
        this.delegate = delegate;//Set the delegate to call when the process is finished.
    }

    protected void onPostExecute(Bitmap result) {//When the task has finished executing.
        delegate.processFinish(result);//Call the process finish signal to indicate the task has finished executing.
    }
    protected Bitmap doInBackground(String... param) {//Called when the task begins executing.
        return getBitmapFromURL(param[0]);//Begin downloading the bitmap from the URL.
    }

    private Bitmap getBitmapFromURL(String src) {//A function to download a bitmap from a URL.
        try {//Try to execute the code below and handle any errors that occur.
            java.net.URL url = new java.net.URL(src);//Create a new URL class for the URL selected.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Open the connection to the image.
            connection.setDoInput(true);//Set the URL to open up the input stream.
            connection.connect();//Connect to the URL.
            InputStream input = connection.getInputStream();//Get the data received from the URL.
            Bitmap myBitmap = BitmapFactory.decodeStream(input);//Decode the stream into a bitmap.
            return myBitmap;//Return the bitmap created.
        } catch (IOException e) {//If an I/O exception has occurred.
            e.printStackTrace();//Print out the error.
            return null;//Return null indicating the bitmap could not be obtained.
        }
    }

}
