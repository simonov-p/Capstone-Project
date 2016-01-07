package com.simonov.teamfan.objects;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by petr on 20-Dec-15.
 */
public class Site {
    @JsonProperty("capacity")
    public int capacity;

    @JsonProperty("surface")
    public String surface;

    @JsonProperty("name")
    public String name;

    @JsonProperty("city")
    public String city;

    @JsonProperty("state")
    public String state;


}
