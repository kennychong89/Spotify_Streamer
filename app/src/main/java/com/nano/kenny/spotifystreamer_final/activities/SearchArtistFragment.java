package com.nano.kenny.spotifystreamer_final.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nano.kenny.spotifystreamer_final.R;
import com.nano.kenny.spotifystreamer_final.adapters.ArtistAdapter;
import com.nano.kenny.spotifystreamer_final.model.ArtistParcel;
import com.nano.kenny.spotifystreamer_final.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

import kaaes.spotify.webapi.android.models.Image;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kenny on 7/9/2015.
 */
public class SearchArtistFragment extends Fragment {
    private ArtistAdapter artistAdapter;
    private ArrayList<ArtistParcel> artistParcels;
    private ListView searchArtistListView;

    public static final int ARTIST_SEARCHRESULTS_LIMIT = 20;
    public static final String ARTIST_PARCEL = "artist_parcel";
    public static final String ARTIST_BUNDLE = "artist_bundle";
    public static final String ARTIST_SPOTIFY_ID = "artist_id";
    public static final String RETROFIT_ERROR_MSG = "Unable to connect to Spotify Services";
    public static final String CANNOT_FIND_ARTIST_MSG = "No results found for: ";

    public SearchArtistFragment() {
        artistParcels = new ArrayList<ArtistParcel>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            artistParcels = savedInstanceState.getParcelableArrayList(ARTIST_BUNDLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_artist, container, false);

        artistAdapter = new ArtistAdapter(getActivity(),
                R.layout.list_searchartists,
                artistParcels);

        initSearchArtistListView(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(ARTIST_BUNDLE, artistParcels);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void initSearchArtistListView(View rootView) {
        searchArtistListView = (ListView)rootView.findViewById(R.id.artists_list);
        searchArtistListView.setAdapter(artistAdapter);
        searchArtistListView.setOnItemClickListener(new ArtistItemClickedListener());
    }

    private class ArtistItemClickedListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int artistItemPosition, long id) {
            ArtistParcel artist = artistParcels.get(artistItemPosition);
            startTopTenTrackActivity(artist);
        }

        private void startTopTenTrackActivity(ArtistParcel artistParcel) {
            Intent topTenTrackActivityIntent =
                    new Intent(getActivity(), TopTenTrackActivity.class);

            topTenTrackActivityIntent.putExtra(ARTIST_PARCEL, artistParcel);
            startActivity(topTenTrackActivityIntent);
        }
    }

    public void searchForArtists(String artistName) {
        getArtists(artistName);
    }

    private void getArtists(final String artistName) {
        SpotifyService spotifyService = new SpotifyApi().getService();
        Map<String, Object> options = new HashMap<String, Object>();

        // limit is 20 names
        options.put("limit", ARTIST_SEARCHRESULTS_LIMIT);

        spotifyService.searchArtists(artistName, options, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager pager, Response response) {
                getActivity().runOnUiThread(new LoadArtistsTask(pager, artistName));
            }

            @Override
            public void failure(RetrofitError error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),RETROFIT_ERROR_MSG
                                ,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private class LoadArtistsTask implements Runnable {
        private ArtistsPager artistsPager;
        private String artistName;

        public LoadArtistsTask(ArtistsPager pager, String artistName) {
            this.artistsPager = pager;
            this.artistName = artistName;
        }

        @Override
        public void run() {
            List<Artist> artists = artistsPager.artists.items;

            if (artists.size() == 0) {
                Toast.makeText(getActivity(),
                        CANNOT_FIND_ARTIST_MSG + artistName, Toast.LENGTH_SHORT).show();
            } else {
                artistParcels.clear();

                for (Artist artist: artists) {
                    artistParcels.add(createArtistParcel(artist));
                }

                artistAdapter.notifyDataSetChanged();
            }
        }

        private ArtistParcel createArtistParcel(Artist artist) {
            String artistName = artist.name;
            String artistSpotifyID = artist.id;
            String artistImageURL = null;

            if (artist.images.size() > 0) {
                artistImageURL = ImageUtil.searchForImageURLGivenSizeConstraints(artist.images,
                        ImageUtil.MIN_IMAGE_SEARCH_WIDTH, ImageUtil.MIN_IMAGE_SEARCH_HEIGHT,
                        ImageUtil.MAX_IMAGE_SEARCH_WIDTH, ImageUtil.MAX_IMAGE_SEARCH_HEIGHT);
            }

            return new ArtistParcel(artistName, artistSpotifyID, artistImageURL);
        }
    }
}
