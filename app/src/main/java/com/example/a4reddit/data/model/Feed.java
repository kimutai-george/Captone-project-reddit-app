package com.example.a4reddit.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by George Kimutai on 3/28/2019.
 */

@Root(name = "feed", strict = false)
public class Feed implements Serializable {
    @Element(name = "updated")
    private String updated;
    @Element(name = "title")
    private String title;
    @Element(required = false, name = "subTitle")
    private String subTitle;
    @ElementList(inline = true, required = false, name = "entry")
    private List<Entry> entry;


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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<Entry> getEntires() {
        return entry;
    }

    public void setEntires(List<Entry> entires) {
        this.entry = entires;
    }

    @Override
    public String toString() {

        return "Feed: \n [Entries: " + entry + "]";
    }


}

