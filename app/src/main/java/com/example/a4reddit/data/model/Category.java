package com.example.a4reddit.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by George Kimutai on 3/28/2019.
 */

@Root(name = "category", strict = false)
public class Category implements Serializable {
    @Attribute(name = "term")
    private String term;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Category() {

    }

    @Override
    public String toString() {
        return "Category{" +
                "term='" + term + '\'' +
                '}';
    }
}
