package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;
import com.example.flixster.databinding.ItemMovieBinding;


import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public interface OnClickListener {
        void onItemClicked (int position);
    }
    Context context;
    List<Movie> movies;
    OnClickListener onClickListener;
    ItemMovieBinding item_bind;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    //inflating a layout from the XML and returning the holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        item_bind = ItemMovieBinding.inflate(inflate,parent,false);
        //View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false);
        return new ViewHolder(item_bind);
    }

    @Override
    //Populates data into the item through the holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the movie at the passed position
        Movie movie = movies.get(position);
        //bind the movie data into the viewholder
        holder.bind(movie);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            tvTitle = binding.tvTitle;
            tvOverview= binding.tvOverview;
            ivPoster = binding.ivPoster;
            itemView.setOnClickListener(this);


        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }

        }

        public void bind(Movie movie) {

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            //render an image using Glide library
            String imageURL;
            //if phone is in landscape, image Url = backdrop
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL = movie.getBackdropPath();
                Glide.with(context)
                        .load(movie.getBackdropPath())
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .transform(new RoundedCornersTransformation(30, 10))
                        .into(ivPoster);
            }
            //else, image url = posterpath
            else {
                Glide.with(context)
                        .load(movie.getPosterPath())
                        .placeholder(R.drawable.flicks_movie_placeholder)
                        .transform(new RoundedCornersTransformation(30, 10))
                        .into(ivPoster);
            }



            //Glide.with(context).load(imageURL).into(ivPoster);

        }
    }

}
