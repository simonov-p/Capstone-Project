package com.simonov.teamfan.objects;

/**
 * Created by petr on 04-Jan-16.
 */
public class Player {
    String last_name;
    String first_name;
    public String display_name;
    String position;
    float minutes;
    public int points;
    int assists;
    int turnovers;
    int steals;
    int blocks;
    int rebounds;
    int field_goals_attempted;
    int field_goals_made;
    int three_point_field_attempted;
    int three_point_field_made;
    int free_throws_attempted;
    int free_throws_made;
    int defensive_rebounds;
    int offensive_rebounds;
    int personal_fauls;
    String team_abbreviation;
    boolean is_starter;
    float field_goal_percentage;
    float three_point_percentage;
    float free_point_percentage;

    @Override
    public String toString() {
        return "Player{" +
                "last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", display_name='" + display_name + '\'' +
                ", position='" + position + '\'' +
                ", minutes=" + minutes +
                ", points=" + points +
                ", assists=" + assists +
                ", turnovers=" + turnovers +
                ", steals=" + steals +
                ", blocks=" + blocks +
                ", rebounds=" + rebounds +
                ", field_goals_attempted=" + field_goals_attempted +
                ", field_goals_made=" + field_goals_made +
                ", three_point_field_attempted=" + three_point_field_attempted +
                ", three_point_field_made=" + three_point_field_made +
                ", free_throws_attempted=" + free_throws_attempted +
                ", free_throws_made=" + free_throws_made +
                ", defensive_rebounds=" + defensive_rebounds +
                ", offensive_rebounds=" + offensive_rebounds +
                ", personal_fauls=" + personal_fauls +
                ", team_abbreviation='" + team_abbreviation + '\'' +
                ", is_starter=" + is_starter +
                ", field_goal_percentage=" + field_goal_percentage +
                ", three_point_percentage=" + three_point_percentage +
                ", free_point_percentage=" + free_point_percentage +
                '}';
    }
}
