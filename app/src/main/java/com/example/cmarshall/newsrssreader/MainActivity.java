package com.example.cmarshall.newsrssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int addActivityRequestCode = 295;//Request code returned when add activity has finished.
    private final int startupActivityRequestCode = 150;//Request code returned when startup activity has finished.
    private NewsSourceManager manager = null;//Object to manage the news sources
    private String currentURL = null;//Holds the current URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initailiseSourcesFeed();//When the user clicks on initialise sources
                Intent intent = new Intent(getApplicationContext(), AddNewsSource.class);//Start the add sources activity.
                startActivityForResult(intent, addActivityRequestCode);//Start the activity and handle the result.
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//Get the drawer showing the news activites.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        manager = new NewsSourceManager(getExternalCacheDir() + "/newsSourceData.json");//Load the news source manager object.
        manager.loadData();//Load the news sources.
        if(manager.getSourcesBinding().size() == 0) {//If there are no news sources
            Intent newIntent = new Intent(this, StartupScreen.class);//Create an intent to call the startup screen.
            startActivityForResult(newIntent, startupActivityRequestCode);//Start the intent and handle the result of the screen.
        } else {//If there are news sources
            initailiseSourcesFeed();//Initialise the news sources.
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {//Function to handle the activity result.
        if (requestCode == addActivityRequestCode) {//If the result is from the add activity screen.
            if (resultCode == RESULT_OK) {//If the result returned was an okay flag indicating the result is successful
                String newsSourceName = data.getStringExtra("newsSourceName");//Get the news source name returned from adding a activity screen.
                String newsSourceURL = data.getStringExtra("newsSourceURL");//Get the news source URL returned from adding a activity URL
                int red = data.getIntExtra("r", 0);//Get the red the user entered for the new activity.
                int green = data.getIntExtra("g", 0);//Get the blue the user entered for the new activity.
                int blue = data.getIntExtra("b", 0);//Get the green the user entered for the new activity.
                manager.addNewsSource(newsSourceURL, newsSourceName, red, green, blue);//Add the news source.
                manager.saveData();//Save the changes.
            }
        } else if (requestCode == startupActivityRequestCode) {//If the result is from the startup activity screen.
            if(resultCode == RESULT_OK) {//If the result is from the starting screen.
                boolean addGuardian = data.getBooleanExtra("addGuardian",false);//Get whether or not the user selected the guardian news technology feed.
                boolean addBBCTech = data.getBooleanExtra("addBBCTech", false);//Get whether or not the user selected the BBC technology feed.
                boolean addBBCEducation = data.getBooleanExtra("addBBCEducation", false);//Get whether or not the user selected BBC education feed.
                manager.addDefaultNewsSource(addGuardian, addBBCTech, addBBCEducation);//Add the default news feeds through the manager feed.
                manager.saveData();//Save the data.
            }
        }
    }

    private void initailiseSourcesFeed() {//Initialise the sources feed.
        ListView sourceView = (ListView)findViewById(R.id.newsSourceListing);//Get the news source listing.
        try {//Try and execute the code below and handle any errors.
            if(sourceView != null) {//If the news source listing has loaded in the view.
                NewsSourceAdapter newsSourceAdapter = new NewsSourceAdapter(this, manager.getSourcesBinding());//Load the news source adapter.
                sourceView.setAdapter(newsSourceAdapter);//Set the list view adapter to the news source adapter
                sourceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NewsSource entry = (NewsSource) parent.getItemAtPosition(position);//Get the item the user selected.
                        currentURL = entry.URL;//Get the URL From the news source.
                        setTitle(entry.name);//Set the title of the app to the news feed name,
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//Get the drwaer
                        drawer.closeDrawers();//Close the drawer to prevent it distracting the user from the feed.
                        initailizeListView();//Reload the list of news articles.
                    }
                });
            }
        } catch(Exception e) {//Catch exceptions.
            e.printStackTrace();//Print the exception stack trace.
        }
    }

    private void initailizeListView() {//Function to load the list view.
        ListView listView = (ListView)findViewById(R.id.newsFeed);//Obtain the list view handle on the current activity.
        try {//Try and execute the code below and handle any errors.
            ArrayList<NewsEntry> entries = new NewsDataGatherer().execute(currentURL, getFilesDir().getAbsolutePath()).get();//Get the list of news entries
            NewsAdapter newsAdapter = new NewsAdapter(this, entries);//Create a custom list adapter to handle the feed.
            listView.setAdapter(newsAdapter);//Set the adapter of the list of news to the custom list adapter.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//Create a on click event handler.
                    NewsEntry entry = (NewsEntry) parent.getItemAtPosition(position);//Obtain the news article selected associated class.
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.URL));//Create an intent for the browser.
                    startActivity(browserIntent);//Start the browser and load the URL.
                }
            });
        } catch(Exception e) {//If an exception was encountered.
            e.printStackTrace();//Print the exception
        }
    }

    @Override
    public void onBackPressed() {//If the back button was pressed.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//Find the drawer layout in the activity.
        if (drawer.isDrawerOpen(GravityCompat.START)) {//If the drawer is open.
            drawer.closeDrawer(GravityCompat.START);//Close the drwaer.
        } else {//If drawer is closed.
            super.onBackPressed();//Handle the back press as normal.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//If the user selected an option from the options menu.
        int id = item.getItemId();//Get the ID of the item selected.
        if(id == R.id.action_refresh) {//If the user clicked refresh.
            initailiseSourcesFeed();//Initialise the sources feed.
            return true;//Return true.
        }

        return super.onOptionsItemSelected(item);//Handle the item click in the inherited class as well.
    }
}
