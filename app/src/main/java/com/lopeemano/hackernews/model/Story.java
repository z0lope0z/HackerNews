package com.lopeemano.hackernews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {
    public Integer id;
    public String title;

    public Story(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Story translate(HNItem hnItem) {
        return new Story(hnItem.getId(), hnItem.getTitle());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
    }

    protected Story(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
