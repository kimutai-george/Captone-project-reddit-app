package com.example.a4reddit.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by George Kimutai on 3/28/2019.
 */


@Root(name = "entry", strict = false)
public class Entry implements Serializable {
    @Element(required = false, name = "author")
    private Author author;
    @Element(name = "content")
    private String content;
    @Element(required = false, name = "category")
    private Category category;
    @Element(required = false, name = "link")
    private Link link;
    @Element(name = "updated")
    private String updated;
    @Element(name = "title")
    private String title;

    public Entry() {

    }

    public Entry(Author author, String content, Link link, String updated, String title, Category category) {
        this.category = category;
        this.author = author;
        this.content = content;
        this.link = link;
        this.updated = updated;
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", category=" + category +
                ", link='" + link + '\'' +
                ", updated='" + updated + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
