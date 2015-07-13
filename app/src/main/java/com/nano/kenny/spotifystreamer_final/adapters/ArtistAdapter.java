package com.nano.kenny.spotifystreamer_final.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.kenny.spotifystreamer_final.R;
import com.nano.kenny.spotifystreamer_final.model.ArtistParcel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kenny on 7/10/2015.
 */
public class ArtistAdapter extends ArrayAdapter<ArtistParcel> {
    private ArrayList<ArtistParcel> artistParcels;
    private int MAX_ARTIST_IMAGE_WIDTH = 200;
    private int MAX_ARTIST_IMAGE_HEIGHT = 200;

    public ArtistAdapter(Context context, int resourceID, ArrayList<ArtistParcel> artistParcels) {
        super(context, resourceID, artistParcels);
        this.artistParcels = artistParcels;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_searchartists, null);
        }

        TextView artistNameView = (TextView)v.findViewById(R.id.artist_name);
        ImageView artistImageView = (ImageView)v.findViewById(R.id.artist_image);

        ArtistParcel artist = artistParcels.get(position);

        if (artist != null) {
            updateTextView(artistNameView, artist.getArtistName());
            updateImageView(artistImageView, artist.getArtistImageURL());
        }

        return v;
    }

    private void updateTextView(TextView textView, String text) {
        if (textView != null)
            textView.setText(text);
    }

    private void updateImageView(ImageView imageView, String imgResLocation) {
        if (imageView != null) {
            if (imgResLocation != null) {
                Picasso.with(getContext()).
                        load(imgResLocation).
                        resize(MAX_ARTIST_IMAGE_WIDTH, MAX_ARTIST_IMAGE_HEIGHT).
                        noFade().
                        centerCrop().
                        into(imageView);
            } else {
                Picasso.with(getContext()).
                        load(R.drawable.selection).
                        resize(MAX_ARTIST_IMAGE_WIDTH, MAX_ARTIST_IMAGE_HEIGHT).
                        noFade().
                        centerCrop().
                        into(imageView);
            }
        }
    }
}
