package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

//import static com.example.flixster.MainActivity.NOW_PLAYING_URL;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView movieBackdrop;
    TextView releaseDate;
    ImageView playButton;
    ImageView exitDetails;
    TextView popularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding act_details = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(act_details.getRoot());
        tvTitle = (TextView) act_details.tvTitle;
        tvOverview = (TextView) act_details.tvOverview;
        rbVoteAverage = (RatingBar) act_details.rbVoteAverage;
        releaseDate = (TextView) act_details.releaseDate;
        playButton = (ImageView) act_details.playButton;
        exitDetails = (ImageView) act_details.exitDetails;
        popularity = (TextView) act_details.Popularity;

        exitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        movieBackdrop = act_details.movieBackdrop;

        //unwrapping the movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra((Movie.class.getSimpleName())));
        Log.d("MovieDetailsActivity","Showing details of movie "+movie.getTitle());
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        releaseDate.setText(movie.getRelDate());
        popularity.setText(String.valueOf(movie.getPopularity()));
        if (movie.getPopularity()>80) {
            popularity.setTextColor(Color.GREEN);
        }
        else if (movie.getPopularity()<50) {
            popularity.setTextColor(Color.RED);
        }
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        Glide.with(this)
                .load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(30, 10))
                .into(movieBackdrop);
        Glide.with(this)
                .load(R.drawable.playbutton)
                .into(playButton);

        movieBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://api.themoviedb.org/3/movie/"+String.valueOf(movie.getId())+"/videos?api_key=1bd69aa220d0d18df5e880ec220f9cf9&language=en-US", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {

                                JSONObject jsonObject = json.jsonObject;
                                try {
                                    String ref = "YouTube";
                                    Log.d("ACT",String.valueOf(movie.getId()));
                                    String newid = String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getString("key"));
                                    String site = String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getString("site"));
                                    Log.d("ACT",String.valueOf(movie.getId()));
                                    Log.d("ACT",site);


                                    if (ref.equals(site) == false ) {
                                        Toast.makeText(getApplicationContext(),"This video is not available on YouTube",Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                        i.putExtra("TRAILERID", newid);
                                        i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                                        startActivityForResult(i, 100);
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),"This video is not available on YouTube",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                //JSONArray results = jsonObject.getJSONArray("results");


                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                }

        });
    }
}