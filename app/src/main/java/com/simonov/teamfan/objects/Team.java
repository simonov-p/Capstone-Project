package com.simonov.teamfan.objects;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see Team object
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Team {

    @JsonProperty("team_id")
    private String team_id;

    @JsonProperty("full_name")
    private String full_name;

    public Team() { }

    public String getTeamId() {
        return team_id;
    }

    public String getFullName() {
        return full_name;
    }

    public Team(String full_name) {
        this.full_name = full_name;
    }
}