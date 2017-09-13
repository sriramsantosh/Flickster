package com.aripir.flickster.models;

import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by saripirala on 9/11/17.
 */

public class Movie {

    String posterPath;
    String originalTitle;
    String overView;
    String backdropPath;

    public Movie(JSONObject jsonObject) throws JSONException
    {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overView = jsonObject.getString("overview");
    }


    public String getImagePath(int orientation) {
        String portraitModeUrl = "https://image.tmdb.org/t/p/w342/%s";
        String landScapeModeUrl = "https://image.tmdb.org/t/p/w1280/%s";

        String url = null;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            url = portraitModeUrl;
            return String.format(url, posterPath );
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            url = landScapeModeUrl;
            return String.format(url, backdropPath );
        }

        return String.format(url, posterPath ); // Can't figure out the orientation
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverView() {
        return overView;
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){
        ArrayList<Movie> results = new ArrayList<>();


        for(int i =0;i<array.length(); i++){
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  results;
    }
}
