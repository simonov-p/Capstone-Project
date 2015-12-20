package simonov.teamfan.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by petr on 20-Dec-15.
 */
public class Site {
    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("surface")
    private String surface;

    @JsonProperty("name")
    private String name;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
