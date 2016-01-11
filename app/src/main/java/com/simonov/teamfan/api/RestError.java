package com.simonov.teamfan.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by petr on 06-Jan-16.
 */
public class RestError {
    @SerializedName("error")
    public RestInnerError error;

    public class RestInnerError{
        @SerializedName("code")
        public String code;
        @SerializedName("description")
        public String description;
    }
}
