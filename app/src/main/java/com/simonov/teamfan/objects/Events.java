package com.simonov.teamfan.objects;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @see Events
 */
public class Events {

    @JsonProperty("event")
    private ArrayList<Event> eventList;
}