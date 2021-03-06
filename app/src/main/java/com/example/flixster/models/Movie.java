package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    int id;
    String relDate;
    int popularity;
    //creating a movie object from a JSONobject

    public Movie (){}

    public Double getVoteAverage() {
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getRelDate() {
        return relDate;
    }

    public int getPopularity() {
        return popularity;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        relDate = jsonObject.getString("release_date");
        popularity = jsonObject.getInt("popularity");
    }
    //returns a list of movies objects by adding each movie in the jsonarray as a new Movie object to a list, which is then returned
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for( int i = 0; i< movieJsonArray.length();i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public String getPosterPath() {
            return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
