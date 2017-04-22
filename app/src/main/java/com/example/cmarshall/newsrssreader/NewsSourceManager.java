package com.example.cmarshall.newsrssreader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NewsSourceManager {//Class designed to abstract and encapsulate details of the news sources such as saving and loading news sources.
    private String filePath;//A variable to hold file path of the configuration file.
    private ArrayList<NewsSource> sources;//An array of the news source entries.

    //Linear searching and linear processing is often used here due to the data set being small therefore using more complex algorithms would introduce
    //more overheads and as consequence unnecessarily slow down the program.
    //This assumes URL is the primary key of a source as URL is unique for each news source otherwise duplicated source data would be presented to the user.

    public NewsSourceManager(String filePath) {//Constructor for the news source manager.
        this.filePath = filePath;//Set the file path.
        this.sources = new ArrayList<>();//Create an empty list of sources.
        loadData();//Load existing data.
    }

    public ArrayList<NewsSource> getSourcesBinding() {
        return sources;
    }//Return the current state of sources.

    public boolean exists(String URL) {//A function designed to check whether or not a source exists.
        for(int i = 0; i < sources.size(); i++) {//Go through list of sources.
            if(sources.get(i).URL == URL) {//If the URL matches.
                return true;//Return true indicating a source with the URL exists.
            }
        } return false;//Return false indicating source with URL doesn't exist.
    }

    public void addNewsSource(String URL, String name, int r, int g, int b) {//Function to take news source metadata and construct a source object to store information about.
        if(exists(URL)) {//If a source with the same URL already exists.
            return;//Return to prevent data duplication.
        }
        NewsSource newsSource = new NewsSource();//Create a news source.
        newsSource.name = name;//Set the relevant details.
        newsSource.URL = URL;
        newsSource.r = r;
        newsSource.g = g;
        newsSource.b = b;
        this.sources.add(newsSource);//Add the new source to the internal collection of sources.
    }

    private void removeDuplicates() {//A function to remove duplicates from the system.
        ArrayList<String> currentList = new ArrayList<>();//An array to keep track of the current URLs in the system.
        boolean duplicateFound = false;//A flag to indicate whether or not a duplicate was found.
        for(int i = 0; i < this.sources.size(); i++) {//Iterate through the list of sources.
            if(currentList.contains(this.sources.get(i).URL)) {//If the current list of URLs contains the URL in the system before.
                this.sources.remove(i);//Remove the URL.
                duplicateFound = true;//Set the duplicate found.
                break;//Break the loop as the indexes will have shifted.
            } else {//If the current list of URLs doesn't have the URL indexed.
                currentList.add(this.sources.get(i).URL);//Add the URL.
            }
        }
        if(duplicateFound) {//If a duplicate was found.
            removeDuplicates();//Remove more duplicates.
        }
    }

    public void removeNewsSource(String URL) {//Function to remove a news source.
        for(int i = 0; i < this.sources.size(); i++) {//Go through the list of sources.
            if(this.sources.get(i).URL == URL) {//If the URL matches.
                this.sources.remove(i);//Remove the source.
                break;//Break to prevent further iteration.
            }
        }
    }

    public void addDefaultNewsSource(boolean guardian, boolean BBCNewsTech, boolean BBCNewsEducation) {//Function to handle adding the initial default news sources.
        if(guardian) {//If the user selected the guardian.
            addNewsSource("https://www.theguardian.com/uk/technology/rss", "The Guardian", 0, 0, 255);//Add the guardian news source.
        }
        if(BBCNewsTech) {//If the user selected BBC News technology
            addNewsSource("http://feeds.bbci.co.uk/news/technology/rss.xml?edition=uk", "BBC News technology", 255, 0, 0);//Add the BBC news source.
        }
        if(BBCNewsEducation) {//If the user selected BBC News education
            addNewsSource("http://feeds.bbci.co.uk/news/education/rss.xml?edition=uk", "BBC News education", 255, 0, 0);//Add the BBC news education source.
        }
    }

    public void loadData() {//Function to load the news source data.
        try {//Try and execute the code below and handle any errors.
            File outputFile = new File(filePath);//Create a file object to represent the configuration file.
            if(outputFile.exists()) {//If the configuration file exists.
                String json = getStringFromFile(filePath);//Get JSON string within the file.
                JSONArray jsonData = new JSONArray(json);//Construct a JSON data structure from the file.
                for (int i = 0; i < jsonData.length(); i++) {//Go through the list of JSON data.
                    JSONObject currentItem = jsonData.getJSONObject(i);//Get the current item within the JSON structure.
                    NewsSource source = new NewsSource();//Create a news source metadata entry.
                    source.URL = currentItem.getString("url");
                    source.name = currentItem.getString("name");
                    source.r = currentItem.getInt("r");
                    source.g = currentItem.getInt("g");
                    source.b = currentItem.getInt("b");
                    sources.add(source);//Add it to the internal list of sources.
                }
                this.removeDuplicates();//Remove duplicates from the system in case duplicates was found in the configuration file.
            }
        } catch(Exception e) {//If an exception has occurred.
            e.printStackTrace();//Print the stack trace.
        }
    }

    private String buildOutputString() {//Function to build a JSON string to save.
        try {//Try and execute the code below and handle any errors.
            JSONArray jsonData = new JSONArray();//Create a JSON array to hold the data.
            for (int i = 0; i < sources.size(); i++) {//Go through the list of sources.
                JSONObject outputSource = new JSONObject();//Create a JSON object containing news source metadata.
                outputSource.put("url", sources.get(i).URL);//Set relevant details.
                outputSource.put("name", sources.get(i).name);
                outputSource.put("r", sources.get(i).r);
                outputSource.put("g", sources.get(i).g);
                outputSource.put("b", sources.get(i).b);
                jsonData.put(outputSource);//Add the JSON object to an array
            }
            return jsonData.toString();//Convert the array to JSON.
        } catch(Exception e) {//If an exception has occurred.
            e.printStackTrace();//Print stack trace.
            return "";//Return blank.
        }
    }

    public void saveData() {//Function to save data.
        try {//Try and execute the code below and handle any errors.
            File outputFile = new File(filePath);//Create instance of file class which will load details about the configuration file..
            if(outputFile.exists()) {//If the configuration file exists.
                outputFile.delete();//Delete it.
            }
            FileWriter writer = new FileWriter(outputFile);//Create a new file writer for the configuration file.
            writer.append(buildOutputString());//Append the JSON data.
            writer.flush();//Ensure all relevant data is entered into file.
            writer.close();//Close the writer
        } catch(Exception e) {//If an exception occurs.
            e.printStackTrace();//Print the stack trace.
        }
    }

    private String convertStreamToString(InputStream is) throws Exception {//Convert a input stream to an output stream.
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));//Create a buffered reader to effectively hold temporary storage whilst reading the file.
        StringBuilder sb = new StringBuilder();//Create a string builder instance.
        String line = null;//Variable to hold the current line./
        while ((line = reader.readLine()) != null) {//Iterate through the list of lines until end of file.
            sb.append(line).append("\n");//Append the line in the file along with a new line.
        }
        reader.close();//Close the reader.
        return sb.toString();//Output the string.
    }

    private String getStringFromFile (String filePath) throws Exception {//Function to get a string from file.
        File fl = new File(filePath);//Create file object for configuration file.
        FileInputStream fin = new FileInputStream(fl);//Create a input stream to the configuration file.
        String ret = convertStreamToString(fin);//Convert the stream to a string.
        fin.close();//Close the stream.
        return ret;//Return the result.
    }


}
