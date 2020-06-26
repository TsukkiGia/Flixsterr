package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_trailer);
        ActivityMovieTrailerBinding act_trailer = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        setContentView(act_trailer.getRoot());

        // temporary test video id -- TODO replace with movie trailer video id

        final String temp_id = getIntent().getStringExtra("TRAILERID");

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) act_trailer.player;

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(temp_id);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tvTitle = (TextView) act_trailer.tvTitle;
            tvOverview = (TextView) act_trailer.tvOverview;
            rbVoteAverage = (RatingBar) act_trailer.rbVoteAverage;
            releaseDate = (TextView) act_trailer.releaseDate;
            movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra((Movie.class.getSimpleName())));
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            releaseDate.setText(movie.getRelDate());
            float voteAverage = movie.getVoteAverage().floatValue();
            rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        }
    }
}