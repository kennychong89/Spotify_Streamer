package com.nano.kenny.spotifystreamer_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.kenny.spotifystreamer_final.R;
import com.nano.kenny.spotifystreamer_final.model.TrackParcel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kenny on 7/10/2015.
 */
public class TrackAdapter extends ArrayAdapter<TrackParcel> {
    private ArrayList<TrackParcel> trackParcels;
    private int MAX_ALBUM_IMAGE_WIDTH = 200;
    private int MAX_ALBUM_IMAGE_HEIGHT = 200;

    public TrackAdapter(Context context, int resourceID, ArrayList<TrackParcel> trackParcels) {
        super(context, resourceID, trackParcels);
        this.trackParcels = trackParcels;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_toptentracks, null);
        }

        ImageView albumCoverImageView = (ImageView)v.findViewById(R.id.album_image);
        TextView trackNameView = (TextView)v.findViewById(R.id.track_name);
        TextView albumNameView = (TextView)v.findViewById(R.id.album_name);

        TrackParcel track = trackParcels.get(position);

        if (track != null) {
            updateImageView(albumCoverImageView, track.getAlbumImageURL());
            updateTextView(trackNameView, track.getTrackName());
            updateTextView(albumNameView, track.getAlbumName());
        }

        return v;
    }

    private void updateTextView(TextView textView, String text) {
        if (textView != null);
            textView.setText(text);
    }

    private void updateImageView(ImageView imageView, String imgResLocation) {
        if (imageView != null) {
            if (imgResLocation != null) {
                Picasso.with(getContext()).
                        load(imgResLocation).

                        noFade().
                        centerCrop().
                        resize(MAX_ALBUM_IMAGE_WIDTH, MAX_ALBUM_IMAGE_HEIGHT).
                        into(imageView);
            } else {
                Picasso.with(getContext()).
                        load(R.drawable.selection).

                        noFade().
                        centerCrop().
                        resize(MAX_ALBUM_IMAGE_WIDTH, MAX_ALBUM_IMAGE_HEIGHT).
                        into(imageView);
            }
        }
    }
}
