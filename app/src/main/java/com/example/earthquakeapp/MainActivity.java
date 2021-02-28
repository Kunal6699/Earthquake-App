package com.example.earthquakeapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.NetworkInfo;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private EarthquakeAdapter mAdapter;
    private ProgressBar mProgressBar;

    private static final String LOG_TAG = MainActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=20";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"Test: Main Activity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager  = getLoaderManager();

            Log.i(LOG_TAG,"Test: Calling Init Loader()");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        mEmptyStateTextView  = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
//        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });



    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG,"Creation of Loader");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);


    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {


        Log.i(LOG_TAG,"Test: onLoadFinished() called.");

        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        if(earthquakes == null || earthquakes.isEmpty())
            return;
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

            mAdapter.addAll(earthquakes);
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG,"Test: Loader Reset called.");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }



}