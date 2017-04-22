package com.example.cmarshall.newsrssreader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NewsSourceManager {
    private String filePath;
    private ArrayList<NewsSource> sources;

    public NewsSourceManager(String filePath) {
        this.filePath = filePath;
        this.sources = new ArrayList<>();
        loadData();
    }

    public ArrayList<NewsSource> getSourcesBinding() {
        return sources;
    }

    boolean exists(String URL) {
        for(int i = 0; i < sources.size(); i++) {
            if(sources.get(i).URL == URL) {
                return true;
            }
        } return false;
    }

    public void addNewsSource(String URL, String name, int r, int g, int b) {
        NewsSource newsSource = new NewsSource();
        newsSource.name = name;
        newsSource.URL = URL;
        newsSource.r = r;
        newsSource.g = g;
        newsSource.b = b;
        this.sources.add(newsSource);
    }

    private void removeDuplicates() {
        ArrayList<String> currentList = new ArrayList<>();
        boolean duplicateFound = false;
        for(int i = 0; i < this.sources.size(); i++) {
            if(currentList.contains(this.sources.get(i).URL)) {
                this.sources.remove(i);
                duplicateFound = true;
                break;
            } else {
                currentList.add(this.sources.get(i).URL);
            }
        }
        if(duplicateFound) {
            removeDuplicates();
        }
    }

    public void removeNewsSource(String URL) {
        for(int i = 0; i < this.sources.size(); i++) {
            if(this.sources.get(i).URL == URL) {
                this.sources.remove(i);
                break;
            }
        }
    }

    public void addDefaultNewsSource(boolean guardian, boolean BBCNewsTech, boolean BBCNewsEducation) {
        if(guardian) {
            addNewsSource("https://www.theguardian.com/uk/technology/rss", "The Guardian", 0, 0, 255);
        }
        if(BBCNewsTech) {
            addNewsSource("http://feeds.bbci.co.uk/news/technology/rss.xml?edition=uk", "BBC News technology", 255, 0, 0);
        }
        if(BBCNewsEducation) {
            addNewsSource("http://feeds.bbci.co.uk/news/education/rss.xml?edition=uk", "BBC News education", 255, 0, 0);
        }
    }

    public void loadData() {
        try {
            File outputFile = new File(filePath);
            if(outputFile.exists()) {
                Log.d("Tag","Reading data");
                String json = getStringFromFile(filePath);
                Log.d("Tag", json);
                JSONArray jsonData = new JSONArray(json);
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject currentItem = jsonData.getJSONObject(i);
                    NewsSource source = new NewsSource();
                    source.URL = currentItem.getString("url");
                    source.name = currentItem.getString("name");
                    source.r = currentItem.getInt("r");
                    source.g = currentItem.getInt("g");
                    source.b = currentItem.getInt("b");
                    sources.add(source);
                }
                this.removeDuplicates();
            } else {
                Log.d("Tag", "Doesn't exist");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String buildOutputString() {
        try {
            JSONArray jsonData = new JSONArray();
            for (int i = 0; i < sources.size(); i++) {
                JSONObject outputSource = new JSONObject();
                outputSource.put("url", sources.get(i).URL);
                outputSource.put("name", sources.get(i).name);
                outputSource.put("r", sources.get(i).r);
                outputSource.put("g", sources.get(i).g);
                outputSource.put("b", sources.get(i).b);
                jsonData.put(outputSource);
            }
            return jsonData.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void saveData() {
        try {
            File outputFile = new File(filePath);
            if(outputFile.exists()) {
                outputFile.delete();
            }
            File gpxfile = new File(filePath);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(buildOutputString());
            writer.flush();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }


}
