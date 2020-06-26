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
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView movieBackdrop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        ActivityMovieDetailsBinding act_details = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        tvTitle = (TextView) act_details.tvTitle;
        tvOverview = (TextView) act_details.tvOverview;
        rbVoteAverage = (RatingBar) act_details.rbVoteAverage;
         */
        setContentView(R.layout.activity_movie_details);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        movieBackdrop = (ImageView) findViewById(R.id.movieBackdrop);

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
                if (String.valueOf(movie.getId()) != null) {
                    Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    i.putExtra("TRAILERID", String.valueOf(movie.getId()));
                    startActivityForResult(i, 100);
                }
            }
        });
    }
}