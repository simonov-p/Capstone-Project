package com.simonov.teamfan.objects;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.simonov.teamfan.data.GamesContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see Event object
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Event implements Parcelable {

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
    private String teamEventLocationType;

    @JsonProperty("team_points_scored")
    private int teamPointsScored;

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
        return contentValues;
    }

    public Event (Cursor cursor){
        this.eventId = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID));
        this.eventStartDateTime = cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE));
        this.team = new Team(cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME)));
        this.opponent = new Team(cursor.getString(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME)));
        this.teamPointsScored = cursor.getInt(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE));
        this.opponentPointsScored = cursor.getInt(cursor.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));
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
    }
}