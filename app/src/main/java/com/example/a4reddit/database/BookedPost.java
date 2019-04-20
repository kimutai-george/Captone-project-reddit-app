package com.example.a4reddit.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by George Kimutai on 4/08/2019.
 */

@Entity
public class BookedPost implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private final String text;
    private final String group;
    private final String title;
    private final String author;
    private final String updated;
    private final String postURL;
    private final String thumbnailURL;
    private final String comments;
    private final String ytLinks;


    public BookedPost(String text, String group, String title, String author, String updated, String postURL, String thumbnailURL, String comments, String ytLinks) {
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

    public String getText() {
        return text;
    }

    public String getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUpdated() {
        return updated;
    }

    public String getPostURL() {
        return postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getComments() {
        return comments;
    }

    public String getYtLinks() {
        return ytLinks;
    }
}
