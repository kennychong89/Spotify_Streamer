package com.nano.kenny.spotifystreamer_final.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nano.kenny.spotifystreamer_final.R;
import com.nano.kenny.spotifystreamer_final.adapters.TrackAdapter;
import com.nano.kenny.spotifystreamer_final.model.ArtistParcel;
import com.nano.kenny.spotifystreamer_final.model.TrackParcel;
import com.nano.kenny.spotifystreamer_final.utils.ImageUtil;
import com.nano.kenny.spotifystreamer_final.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTrackFragment extends Fragment {
    private TrackAdapter trackAdapter;
    private ArrayList<TrackParcel> trackParcels;
    private ListView topTenTracksListView;
    private ArtistParcel artistParcel;

    public static final String RETROFIT_ERROR_MSG = "Unable to connect to Spotify Services";
    public static final String NO_TRACKS_FROM_ARTIST_MSG = "No tracks found";
    public static final String TRACK_BUNDLE = "track_bundle";
    public static final String TRACK_PARCEL = "track_parcel";
    public static final String COUNTRY_CODE = "us";
    public static final int TRACK_SEARCHRESULT_LIMIT = 10;

    public TopTenTrackFragment() {
        trackParcels = new ArrayList<TrackParcel>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentFromSearchArtistActivity = getActivity().getIntent();

        if (savedInstanceState != null) {
            trackParcels = savedInstanceState.getParcelableArrayList(TRACK_BUNDLE);
            artistParcel = savedInstanceState.getParcelable(SearchArtistFragment.ARTIST_PARCEL);
        } else if (intentFromSearchArtistActivity != null){
            artistParcel = intentFromSearchArtistActivity.
                    getParcelableExtra(SearchArtistFragment.ARTIST_PARCEL);

            if (artistParcel != null)
                getTopTenTracks(artistParcel.getSpotifyID());
        }
    }

    private void getTopTenTracks(final String artistSpotifyID) {
        SpotifyService spotifyService = new SpotifyApi().getService();

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("limit", TRACK_SEARCHRESULT_LIMIT);
        options.put("country", COUNTRY_CODE);

        spotifyService.getArtistTopTrack(artistSpotifyID, options, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                getActivity().runOnUiThread(new LoadTopTenTracksTrack(tracks));
            }

            @Override
            public void failure(RetrofitError error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), RETROFIT_ERROR_MSG,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private class LoadTopTenTracksTrack implements Runnable {
        private List<Track> trackCollection;

        public LoadTopTenTracksTrack(Tracks trackList) {
            this.trackCollection = trackList.tracks;
        }

        @Override
        public void run() {
            if (trackCollection.size() == 0) {
                Toast.makeText(getActivity(),
                        NO_TRACKS_FROM_ARTIST_MSG, Toast.LENGTH_LONG).show();
            } else {
                trackParcels.clear();

                for(Track track : trackCollection) {
                    trackParcels.add(createTrackParcel(track));
                }

                trackAdapter.notifyDataSetChanged();
            }
        }

        private TrackParcel createTrackParcel(Track track) {
            String trackName = track.name;
            String albumName = track.album.name;
            String albumImageURL = getAlbumImageURL(track.album);

            return new TrackParcel(trackName, albumName, albumImageURL);
        }

        private String getAlbumImageURL(AlbumSimple album) {
            // null and not empty string due to Picasso not accepting
            // empty strings
            String albumImageURL = null;

            if (album.images.size() > 0) {
                albumImageURL = ImageUtil.searchForImageURLGivenSizeConstraints(
                        album.images,
                        ImageUtil.MIN_IMAGE_SEARCH_WIDTH, ImageUtil.MIN_IMAGE_SEARCH_HEIGHT,
                        ImageUtil.MAX_IMAGE_SEARCH_WIDTH, ImageUtil.MAX_IMAGE_SEARCH_HEIGHT
                );
            }

            return albumImageURL;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_ten_track, container, false);

        trackAdapter = new TrackAdapter(getActivity(),
                R.layout.list_toptentracks,
                trackParcels);

        initJumbotron(rootView);
        initTopTenTrackListView(rootView);

        return rootView;
    }

    private void initJumbotron(View rootView) {
        String artistImageURL;
        String artistName;

        if (artistParcel != null) {
            artistImageURL = artistParcel.getArtistImageURL();
            artistName = artistParcel.getArtistName();

            ImageView artistImageView =
                    (ImageView)rootView.findViewById(R.id.artist_image_jumbotron);
            TextView artistNameView =
                    (TextView)rootView.findViewById(R.id.artist_name_jumbotron);

            Picasso.with(getActivity()).
                    load(artistImageURL).
                    transform(new RoundedTransformation(200,0)).
                    placeholder(R.drawable.ic_spotify).
                    resize(300, 300).
                    centerCrop().
                    into(artistImageView);

            artistNameView.setText(artistName);
        }
    }

    private void initTopTenTrackListView(View rootView) {
        topTenTracksListView = (ListView)rootView.findViewById(R.id.toptentrack_list);
        topTenTracksListView.setAdapter(trackAdapter);
        topTenTracksListView.setOnItemClickListener(new TrackItemClickedListener());
    }

    private class TrackItemClickedListener implements AdapterView.OnItemClickListener {
        @Override
        public void  onItemClick(AdapterView<?> parent, View view,
                                 int trackItemPosition, long id) {
            TrackParcel trackParcel = trackParcels.get(trackItemPosition);
            startTrackDetailActivity(trackParcel);
        }

        private void startTrackDetailActivity(TrackParcel trackParcel) {
            Intent trackDetailActivityIntent =
                    new Intent(getActivity(), TrackDetailActivity.class);

            trackDetailActivityIntent.putExtra(TRACK_PARCEL, trackParcel);

            startActivity(trackDetailActivityIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(TRACK_BUNDLE, trackParcels);
        savedInstanceState.putParcelable(SearchArtistFragment.ARTIST_PARCEL, artistParcel);
        super.onSaveInstanceState(savedInstanceState);
    }

}
