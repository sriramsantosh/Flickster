package com.aripir.flickster.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aripir.flickster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by saripirala on 9/15/17.
 */

public class MovieDetailsActivity extends YouTubeBaseActivity {

    public static final String YT_API_KEY = "AIzaSyCquLzX0c-TJl-hPzWqFZBER2_UdY93iSA";
    private String key;
    private boolean isPopular;

    private @BindView(R.id.movieTitle) TextView movieTitleTV;
    private @BindView(R.id.movieOverview)TextView movieOverviewTV;
    private @BindView(R.id.releaseDate)TextView movieReleaseDateTV;
    private @BindView(R.id.movieRating) RatingBar movieRatingRB;
    private @BindView(R.id.player) YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        float movieRating = getIntent().getFloatExtra("movie_rating", 0);
        String movieTitle = getIntent().getStringExtra("movie_title");
        String movieOverview = getIntent().getStringExtra("movie_overview");
        String movieReleaseDate = getIntent().getStringExtra("movie_releasedate");

        isPopular = false;

        if(movieRating >=5) {
            isPopular = true;
            movieRatingRB.setVisibility(View.GONE);
        }

        if(!isPopular) {
                movieTitleTV.setText(movieTitle);
                movieOverviewTV.setText(movieOverview);
                movieReleaseDateTV.setText("Release Date: " + movieReleaseDate);
                movieRatingRB.setRating(movieRating / 2);
                movieRatingRB.setVisibility(View.VISIBLE);
        }

        int movieId = getIntent().getIntExtra("movie_id", 0);

        String url = "https://api.themoviedb.org/3/movie/"+ movieId+ "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

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
                    JSONArray ytJSONResults = new JSONArray();
                    ytJSONResults = responseJSON.getJSONArray("results");

                    for(int i =0;i<ytJSONResults.length(); i++){
                        try {
                            JSONObject ytJson = ytJSONResults.getJSONObject(i);
                            if(ytJson.has("site") && ytJson.has("key"))
                                if(ytJson.getString("site").equalsIgnoreCase("YouTube")) {
                                    key = ytJson.getString("key");
                                    break;
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    updateView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


    }


    private void updateView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                youTubePlayerView.initialize(YT_API_KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {

                                if(key ==null){
                                    Toast.makeText(MovieDetailsActivity.this, "Couldn't find a Youtube trailer!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(isPopular)
                                    youTubePlayer.loadVideo(key);
                                else
                                    youTubePlayer.cueVideo(key);
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {
                                Toast.makeText(MovieDetailsActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
