package com.nano.kenny.spotifystreamer_final.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kenny on 7/10/2015.
 */
public class TrackParcel implements Parcelable{
    private String trackName;
    private String albumName;
    private String albumImageURL;

    public TrackParcel(String trackName, String albumName,
                       String albumImageURL) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumImageURL = albumImageURL;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumImageURL() {
        return albumImageURL;
    }


    private TrackParcel(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        albumImageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(trackName);
        out.writeString(albumName);
        out.writeString(albumImageURL);
    }

    public static final Parcelable.Creator<TrackParcel> CREATOR =
            new Parcelable.Creator<TrackParcel>() {
                public TrackParcel createFromParcel(Parcel in) {
                    return new TrackParcel(in);
                }

                public TrackParcel[] newArray(int size) {
                    return new TrackParcel[size];
                }
            };
}
