package com.aripir.flickster.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aripir.flickster.R;
import com.aripir.flickster.adapters.MovieArrayAdapter;
import com.aripir.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    public static ArrayList<Movie> movies;
    private MovieArrayAdapter movieArrayAdapter;
    private ListView lvItems;
    public static int moviesCount;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        context =this;

        int orientation = getResources().getConfiguration().orientation;

        lvItems = (ListView) findViewById(R.id.lvItems);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies, orientation);
        lvItems.setAdapter(movieArrayAdapter);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Error", e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                 String responseData = response.body().string();
                try {
                        JSONObject responseJSON = new JSONObject(responseData);
                        JSONArray movieJSONResults = new JSONArray();
                        movieJSONResults = responseJSON.getJSONArray("results");
                        moviesCount = movieJSONResults.length();
                        movies.addAll(Movie.fromJSONArray(movieJSONResults));
                        updateAdapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

        });

    }

    private void updateAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                movieArrayAdapter.notifyDataSetChanged();
            }
        });
    }


}
