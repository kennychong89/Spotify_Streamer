package com.nano.kenny.spotifystreamer_final.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.kenny.spotifystreamer_final.R;
import com.nano.kenny.spotifystreamer_final.model.TrackParcel;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Image;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackDetailActivityFragment extends Fragment {
    private ImageView albumCoverView;
    private TextView trackNameView;
    private TextView albumNameView;

    public static final int MAX_ALBUM_IMAGE_WIDTH = 600;
    public static final int MAX_ALBUM_IMAGE_HEIGHT = 600;

    public TrackDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_track_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            TrackParcel trackParcel = intent.getParcelableExtra(TopTenTrackFragment.TRACK_PARCEL);

            initViews(v, trackParcel);
        }

        return v;
    }

    private void initViews(View v, TrackParcel trackParcel) {
        albumCoverView = (ImageView)v.findViewById(R.id.album_cover_track_detail);
        trackNameView = (TextView)v.findViewById(R.id.track_title_track_detail);
        albumNameView = (TextView)v.findViewById(R.id.album_name_track_detail);

        String albumImageURL = trackParcel.getAlbumImageURL();
        String trackName = trackParcel.getTrackName();
        String albumName = trackParcel.getAlbumName();

        updateImageView(albumCoverView, albumImageURL);
        updateTextView(trackNameView, trackName);
        updateTextView(albumNameView, albumName);
    }

    private void updateImageView(ImageView imageView, String imageURL) {
        Picasso.with(getActivity()).load(imageURL).
                resize(MAX_ALBUM_IMAGE_WIDTH, MAX_ALBUM_IMAGE_HEIGHT).
                placeholder(R.drawable.ic_music_note).
                noFade().
                centerCrop().
                into(imageView);
    }

    private void updateTextView(TextView textView, String text) {
        if (textView != null)
            textView.setText(text);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
