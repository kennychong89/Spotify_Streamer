package com.nano.kenny.spotifystreamer_final.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.nano.kenny.spotifystreamer_final.R;


public class SearchArtistActivity extends ActionBarActivity {
    private String savedQuery;
    private SearchView searchView;

    public static final String QUERY_BUNDLE = "query_bundle";
    public static final String QUERY_HINT = "Search Artist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        if (savedInstanceState != null)
            savedQuery = savedInstanceState.getString(QUERY_BUNDLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_artist, menu);

        initSearchView(menu);

        if (savedQuery != null)
            searchView.setQuery(savedQuery, false);

        return true;
    }

    private void initSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView)menu.findItem(R.id.artist_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(QUERY_HINT);
        searchView.setSubmitButtonEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        String query = searchView.getQuery().toString();
        bundle.putString(QUERY_BUNDLE, query);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent searchResultIntent) {
        if (Intent.ACTION_SEARCH.equals(searchResultIntent.getAction())) {
            SearchArtistFragment searchArtistFragment =
                    (SearchArtistFragment) getFragmentManager().
                            findFragmentById(R.id.search_artist_activity);

            String query = searchResultIntent.getStringExtra(SearchManager.QUERY);

            searchArtistFragment.searchForArtists(query);
        }
    }
}
