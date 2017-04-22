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

    private final int addActivityRequestCode = 295;
    private final int startupActivityRequestCode = 150;
    private NewsSourceManager manager = null;
    private String currentURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initailiseSourcesFeed();
                Intent intent = new Intent(getApplicationContext(), AddNewsSource.class);
                startActivityForResult(intent, addActivityRequestCode);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        manager = new NewsSourceManager(getExternalCacheDir() + "/newsSourceData.json");
        manager.loadData();
        if(manager.getSourcesBinding().size() == 0) {
            Intent newIntent = new Intent(this, StartupScreen.class);
            startActivityForResult(newIntent, startupActivityRequestCode);
        } else {
            initailiseSourcesFeed();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == addActivityRequestCode) {
            if (resultCode == RESULT_OK) {
                String newsSourceName = data.getStringExtra("newsSourceName");
                String newsSourceURL = data.getStringExtra("newsSourceURL");
                int red = data.getIntExtra("r", 0);
                int green = data.getIntExtra("g", 0);
                int blue = data.getIntExtra("b", 0);
                manager.addNewsSource(newsSourceURL, newsSourceName, red, green, blue);
                manager.saveData();
            }
        } else if (requestCode == startupActivityRequestCode) {
            if(resultCode == RESULT_OK) {
                boolean addGuardian = data.getBooleanExtra("addGuardian",false);
                boolean addBBCTech = data.getBooleanExtra("addBBCTech", false);
                boolean addBBCEducation = data.getBooleanExtra("addBBCEducation", false);
                manager.addDefaultNewsSource(addGuardian, addBBCTech, addBBCEducation);
                manager.saveData();
            }
        }
    }

    private void initailiseSourcesFeed() {
        ListView sourceView = (ListView)findViewById(R.id.newsSourceListing);
        try {
            if(sourceView != null) {
                NewsSourceAdapter newsSourceAdapter = new NewsSourceAdapter(this, manager.getSourcesBinding());
                sourceView.setAdapter(newsSourceAdapter);
                sourceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NewsSource entry = (NewsSource) parent.getItemAtPosition(position);
                        currentURL = entry.URL;
                        setTitle(entry.name);
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        initailizeListView();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initailizeListView() {
        ListView listView = (ListView)findViewById(R.id.newsFeed);
        try {
            ArrayList<NewsEntry> entries = new NewsDataGatherer().execute(currentURL, getFilesDir().getAbsolutePath()).get();
            NewsAdapter newsAdapter = new NewsAdapter(this, entries);
            listView.setAdapter(newsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsEntry entry = (NewsEntry) parent.getItemAtPosition(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.URL));
                    startActivity(browserIntent);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            initailiseSourcesFeed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
