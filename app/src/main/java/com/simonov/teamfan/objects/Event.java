package com.simonov.teamfan.objects;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.simonov.teamfan.data.GamesContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see Event object
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Event implements Parcelable {

    //https://erikberg.com/api/methods/team-results

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_status")
    private String eventStatus;

    @JsonProperty("event_start_date_time")
    private String eventStartDateTime;

    @JsonProperty("event_season_type")
    private String eventSeasonType;

    @JsonProperty("team_event_number_in_season")
    private String teamEventNumberInSeason;

    @JsonProperty("team_event_location_type")
    public String teamEventLocationType;

    @JsonProperty("team_points_scored")
    private int teamPointsScored;

    @JsonProperty("team_events_won")
    public String team_events_won;

    @JsonProperty("team_events_lost")
    public String team_events_lost;

    @JsonProperty("opponent_events_lost")
    public String opponent_events_lost;

    @JsonProperty("opponent_events_won")
    public String opponent_events_won;

    @JsonProperty("opponent_points_scored")
    private int opponentPointsScored;

    @JsonProperty("team")
    private Team team;

    public int getTeamPointsScored() {
        return teamPointsScored;
    }

    public int getOpponentPointsScored() {
        return opponentPointsScored;
    }

    @JsonProperty("opponent")

    private Team opponent;

    @JsonProperty("site")
    private Site site;

    public Event() { }

    public String getEventId() {
        return eventId;
    }


    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public Team getTeam() {
        return team;
    }

    public Team getOpponent() {
        return opponent;
    }

    public ContentValues eventToCV(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID, getEventId());
        contentValues.put(GamesContract.GamesEntry.COLUMN_DATE, getEventStartDateTime());
        contentValues.put(GamesContract.GamesEntry.COLUMN_TEAM_NAME, getTeam().getFullName());
        contentValues.put(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME, getOpponent().getFullName());
        contentValues.put(GamesContract.GamesEntry.COLUMN_TEAM_SCORE, getTeamPointsScored());
        contentValues.put(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE, getOpponentPointsScored());
        contentValues.put(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON, this.team_events_won);
        contentValues.put(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST, this.team_events_lost);
        contentValues.put(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON, this.opponent_events_won);
        contentValues.put(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST, this.opponent_events_lost);
        contentValues.put(GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_TYPE, this.teamEventLocationType);
        contentValues.put(GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_NAME, String.format(("%s, %s, %s"),site.name, site.city, site.state));
        return contentValues;
    }

    public Event (Cursor cursor){
        this.eventId = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID));
        this.eventStartDateTime = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));
        this.team = new Team(cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME)));
        this.opponent = new Team(cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME)));
        this.teamPointsScored = cursor.getInt(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE));
        this.opponentPointsScored = cursor.getInt(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));
        this.team_events_won = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON));
        this.team_events_lost = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST));
        this.opponent_events_won = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON));
        this.opponent_events_lost = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST));
        this.teamEventLocationType = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_TYPE));
        this.eventLocationName = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_NAME));
        this.eventLocationNameTeam = teamEventLocationType.equals("h") ? cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME))
                : cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
        this.teamName = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
        this.opponentName = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
    }

    // parcelable data
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(eventId);
        out.writeString(eventStartDateTime);
        out.writeInt(teamPointsScored);
        out.writeInt(opponentPointsScored);
        out.writeString(teamName);
        out.writeString(opponentName);
        out.writeString(team_events_won);
        out.writeString(team_events_lost);
        out.writeString(opponent_events_won);
        out.writeString(opponent_events_lost);
        out.writeString(teamEventLocationType);
        out.writeString(eventLocationName);
        out.writeString(eventLocationNameTeam);

    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) {
        eventId = in.readString();
        eventStartDateTime = in.readString();
        teamPointsScored = in.readInt();
        opponentPointsScored = in.readInt();
        teamName = in.readString();
        opponentName = in.readString();
        team_events_won = in.readString();
        team_events_lost = in.readString();
        opponent_events_won = in.readString();
        opponent_events_lost = in.readString();
        teamEventLocationType = in.readString();
        eventLocationName = in.readString();
        eventLocationNameTeam = in.readString();
    }
    public String teamName;
    public String opponentName;
    public String eventLocationName;
    public String eventLocationNameTeam;

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventStatus='" + eventStatus + '\'' +
                ", eventStartDateTime='" + eventStartDateTime + '\'' +
                ", eventSeasonType='" + eventSeasonType + '\'' +
                ", teamEventNumberInSeason='" + teamEventNumberInSeason + '\'' +
                ", teamEventLocationType='" + teamEventLocationType + '\'' +
                ", teamPointsScored=" + teamPointsScored +
                ", team_events_won='" + team_events_won + '\'' +
                ", team_events_lost='" + team_events_lost + '\'' +
                ", opponent_events_lost='" + opponent_events_lost + '\'' +
                ", opponent_events_won='" + opponent_events_won + '\'' +
                ", opponentPointsScored=" + opponentPointsScored +
                ", team=" + team +
                ", opponent=" + opponent +
                ", site=" + site +
                ", teamName='" + teamName + '\'' +
                ", opponentName='" + opponentName + '\'' +
                ", eventLocationName='" + eventLocationName + '\'' +
                ", eventLocationNameTeam='" + eventLocationNameTeam + '\'' +
                '}';
    }
}