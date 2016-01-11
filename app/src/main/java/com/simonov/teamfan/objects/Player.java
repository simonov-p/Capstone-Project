package com.simonov.teamfan.objects;

/**
 * Created by petr on 04-Jan-16.
 */
public class Player {
    String last_name;
    String first_name;
    public String display_name;
    String position;
    public float minutes;
    public int points;
    public int assists;
    public int turnovers;
    public int steals;
    public int blocks;
    public int rebounds;
    public int field_goals_attempted;
    public int field_goals_made;
    public int three_point_field_goals_attempted;
    public int three_point_field_goals_made;
    public int free_throws_attempted;
    public int free_throws_made;
    public int defensive_rebounds;
    public int offensive_rebounds;
    public int personal_fouls;
    String team_abbreviation;
    boolean is_starter;
    public float field_goal_percentage;
    public float three_point_percentage;
    public float free_throw_percentage;

    @Override
    public String toString() {
        return "Player{" + display_name +
                "three_point_field_goals_attempted=" + three_point_field_goals_attempted +
                ", three_point_field_goals_made=" + three_point_field_goals_made +
                ", free_throws_attempted=" + free_throws_attempted +
                ", free_throw_percentage=" + free_throw_percentage +
                ", three_point_percentage=" + three_point_percentage +
                ", field_goal_percentage=" + field_goal_percentage +
                ", free_throws_made=" + free_throws_made +
                ", personal_fouls=" + personal_fouls +
                '}';
    }
}
