package com.example.a4reddit.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by George Kimutai on 3/28/2019.
 */


public class Post implements Serializable {
    private String text;
    private String group;
    private String title;
    private String author;
    private String updated;
    private List<String> postURL;
    private String thumbnailURL;
    private String comments;
    private List<String> ytLinks;


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

    public List<String> getYtLinks() {
        return ytLinks;
    }

    public void setYtLinks(List<String> ytLinks) {
        this.ytLinks = ytLinks;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public List<String> getPostURL() {
        return postURL;
    }

    public void setPostURL(List<String> postURL) {
        this.postURL = postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Post(String title, String author, String updated, List<String> postURL, String thumbnailURL, String comments, List<String> ytLinks, String group, String text) {
        this.text = text;
        this.group = group;
        this.ytLinks = ytLinks;
        this.comments = comments;
        this.title = title;
        this.author = author;
        this.updated = updated;
        this.postURL = postURL;
        this.thumbnailURL = thumbnailURL;
    }
}
