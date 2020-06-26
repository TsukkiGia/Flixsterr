package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

import static com.example.flixster.MainActivity.NOW_PLAYING_URL;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView movieBackdrop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding act_details = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(act_details.getRoot());
        tvTitle = (TextView) act_details.tvTitle;
        tvOverview = (TextView) act_details.tvOverview;
        rbVoteAverage = (RatingBar) act_details.rbVoteAverage;
         /*
        setContentView(R.layout.activity_movie_details);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);*/

        movieBackdrop = act_details.movieBackdrop;

        //unwrapping the movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra((Movie.class.getSimpleName())));
        Log.d("MovieDetailsActivity","Showing details of movie "+movie.getTitle());
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
        Glide.with(this)
                .load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(30, 10))
                .into(movieBackdrop);
        movieBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://api.themoviedb.org/3/movie/"+String.valueOf(movie.getId())+"/videos?api_key=1bd69aa220d0d18df5e880ec220f9cf9&language=en-US", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {

                                JSONObject jsonObject = json.jsonObject;
                                try {
                                    Log.d("ACT", String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getString("key")));
                                    String newid = String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getString("key"));
                                    Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                    i.putExtra("TRAILERID",newid);
                                    startActivityForResult(i, 100);

                                } catch (JSONException e) {
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