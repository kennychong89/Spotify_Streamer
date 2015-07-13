package com.nano.kenny.spotifystreamer_final.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kenny on 7/10/2015.
 */
public class ArtistParcel implements Parcelable {
    private String name;
    private String imageURL;
    private String spotifyID;

    public ArtistParcel(String artistName, String artistSpotifyID, String artistImageURL) {
        this.name = artistName;
        this.spotifyID = artistSpotifyID;
        this.imageURL = artistImageURL;
    }

    public String getArtistName() {
        return name;
    }

    public String getArtistImageURL() {
        return imageURL;
    }

    public String getSpotifyID() {
        return spotifyID;
    }

    private ArtistParcel(Parcel in) {
        name = in.readString();
        spotifyID = in.readString();
        imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(spotifyID);
        out.writeString(imageURL);
    }

    public static final Parcelable.Creator<ArtistParcel> CREATOR =
            new Parcelable.Creator<ArtistParcel>() {
                @Override
                public ArtistParcel createFromParcel(Parcel in) {
                  return new ArtistParcel(in);
                }

                @Override
                public ArtistParcel[] newArray(int size) {
                    return new ArtistParcel[size];
                }
            };
}
