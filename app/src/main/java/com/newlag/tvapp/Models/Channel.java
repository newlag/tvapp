package com.newlag.tvapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Channel implements Parcelable {

    private String streamUrl;
    private String name;
    private String description;
    private String imageUrl;

    public Channel() { }

    public Channel(String streamUrl, String name, String description, String imageUrl) {
        this.streamUrl = streamUrl;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    protected Channel(Parcel in) {
        streamUrl = in.readString();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public String getStreamUrl() {
        return streamUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(streamUrl);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageUrl);
    }
}
