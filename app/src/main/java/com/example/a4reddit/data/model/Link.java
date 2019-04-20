package com.example.a4reddit.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by George Kimutai on 3/28/2019.
 */


@Root(name = "link", strict = false)
public class Link implements Serializable {
    @Attribute(name = "href")
    String link;

    public Link() {

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Link{" +
                "link='" + link + '\'' +
                '}';
    }
}
