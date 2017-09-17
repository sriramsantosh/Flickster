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

    private String posterPath;
    private String originalTitle;
    private String overView;
    private String backdropPath;
    private int movieRating;
    private String movieDate;
    private int movieId;

    public Movie(JSONObject jsonObject) throws JSONException
    {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overView = jsonObject.getString("overview");
        this.movieRating = jsonObject.getInt("vote_average");
        this.movieDate = jsonObject.getString("release_date");
        this.movieId = jsonObject.getInt("id");
    }

    public String getImagePath(int orientation, int rating) {

        String portraitModeUrl = "https://image.tmdb.org/t/p/w342/%s";
        String landScapeModeUrl = "https://image.tmdb.org/t/p/w1280/%s";

        if(rating >= 5){
            portraitModeUrl = "https://image.tmdb.org/t/p/original/%s";
            landScapeModeUrl = "https://image.tmdb.org/t/p/original/%s";
        }

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

    public int getMovieRating() {
        return movieRating;
    }

    public String getMovieReleaseDate(){
        return movieDate;
    }

    public int getMovieId(){
        return movieId;
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
