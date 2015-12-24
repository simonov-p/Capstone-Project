package simonov.teamfan.objects;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import simonov.teamfan.data.GamesContract;

/**
 * @see Event object
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Event {

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

    public void setTeamPointsScored(int teamPointsScored) {
        this.teamPointsScored = teamPointsScored;
    }

    public int getOpponentPointsScored() {
        return opponentPointsScored;
    }

    public void setOpponentPointsScored(int opponentPointsScored) {
        this.opponentPointsScored = opponentPointsScored;
    }

    @JsonProperty("opponent")

    private Team opponent;

    @JsonProperty("site")
    private Site site;

    public Event() { }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    public String getEventSeasonType() {
        return eventSeasonType;
    }

    public void setEventSeasonType(String eventSeasonType) {
        this.eventSeasonType = eventSeasonType;
    }

    public String getTeamEventNumberInSeason() {
        return teamEventNumberInSeason;
    }

    public void setTeamEventNumberInSeason(String teamEventNumberInSeason) {
        this.teamEventNumberInSeason = teamEventNumberInSeason;
    }

    public String getTeamEventLocationType() {
        return teamEventLocationType;
    }

    public void setTeamEventLocationType(String teamEventLocationType) {
        this.teamEventLocationType = teamEventLocationType;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getOpponent() {
        return opponent;
    }

    public void setOpponent(Team opponent) {
        this.opponent = opponent;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ContentValues eventToCV(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GamesContract.GamesEntry.COLUMN_GAME_NBA_ID, getEventId());
        contentValues.put(GamesContract.GamesEntry.COLUMN_DATE, getEventStartDateTime());
        contentValues.put(GamesContract.GamesEntry.COLUMN_HOME, getTeam().getFullName());
        contentValues.put(GamesContract.GamesEntry.COLUMN_AWAY, getOpponent().getFullName());
        contentValues.put(GamesContract.GamesEntry.COLUMN_HOME_SCORE, getTeamPointsScored());
        contentValues.put(GamesContract.GamesEntry.COLUMN_AWAY_SCORE, getOpponentPointsScored());
        return contentValues;
    }
}