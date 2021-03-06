package com.gautam.newsapp.data.model;

import java.util.HashMap;
import java.util.Map;

public class Source {

    private Object id;
    private String name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Source(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}