package com.fai.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fai on 22/05/2017.
 */

public class ObjectTrailer {
    @SerializedName("results")
    List<DataTrailer> results;

    public class DataTrailer {
        @SerializedName("name")
        public String name;

        @SerializedName("key")
        public String key;

        @SerializedName("type")
        public String type;
    }
}
