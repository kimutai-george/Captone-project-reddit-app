package com.example.a4reddit.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by George Kimutai on 4/17/2019.
 */
public class SendDataToWidget implements Parcelable {
    private String text;
    private String group;
    private String title;
    private String author;
    private String updated;
    private String postURL;
    private String thumbnailURL;
    private String comments;
    private String ytLinks;
    private boolean mBook;

    public SendDataToWidget(String text, String group, String title, String author, String updated, String postURL, String thumbnailURL, String comments, String ytLinks, boolean mBook) {
        this.mBook = mBook;
        this.text = text;
        this.group = group;
        this.title = title;
        this.author = author;
        this.updated = updated;
        this.postURL = postURL;
        this.thumbnailURL = thumbnailURL;
        this.comments = comments;
        this.ytLinks = ytLinks;
    }

    private SendDataToWidget(Parcel in) {
        this.author = in.readString();
        this.comments = in.readString();
        this.group = in.readString();
        this.postURL = in.readString();
        this.text = in.readString();
        this.thumbnailURL = in.readString();
        this.title = in.readString();
        this.updated = in.readString();
        this.ytLinks = in.readString();
    }

    public static final Creator<SendDataToWidget> CREATOR = new Creator<SendDataToWidget>() {
        @Override
        public SendDataToWidget createFromParcel(Parcel in) {
            return new SendDataToWidget(in);
        }

        @Override
        public SendDataToWidget[] newArray(int size) {
            return new SendDataToWidget[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.author);
        parcel.writeString(this.comments);
        parcel.writeString(this.group);
        parcel.writeString(this.postURL);
        parcel.writeString(this.text);
        parcel.writeString(this.thumbnailURL);
        parcel.writeString(this.title);
        parcel.writeString(this.updated);
        parcel.writeString(this.ytLinks);


    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getYtLinks() {
        return ytLinks;
    }

    public void setYtLinks(String ytLinks) {
        this.ytLinks = ytLinks;
    }

    public boolean getmBook() {
        return mBook;
    }

    public void setmBook(boolean mBook) {
        this.mBook = mBook;
    }
}

