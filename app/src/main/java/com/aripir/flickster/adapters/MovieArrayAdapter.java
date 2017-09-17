package com.aripir.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.flickster.R;
import com.aripir.flickster.activities.MovieActivity;
import com.aripir.flickster.activities.MovieDetailsActivity;
import com.aripir.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by saripirala on 9/11/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie>{

    private int orientation;

    private static class ViewHolder {
        TextView tvTile;
        TextView tvOverview;
        ImageView movieImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies, int orientation){
        super(context, android.R.layout.simple_list_item_1, movies);
        this.orientation = orientation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Movie movie = getItem(position);
        final int type = getItemViewType(position);

        ViewHolder viewHolder;

        if(convertView ==null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();

            if(type <1) {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder.tvTile = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.movieImage);

                convertView.setTag(viewHolder);
            }else{
                convertView = inflater.inflate(R.layout.image_movie, parent, false);
                viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.movieImage);
                convertView.setTag(viewHolder);
            }

        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.movieImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MovieActivity.context, MovieDetailsActivity.class);
                intent.putExtra("movie_id", movie.getMovieId());
                intent.putExtra("movie_rating", movie.getMovieRating());


                if(type<1){
                    intent.putExtra("movie_overview", movie.getOverView());
                    intent.putExtra("movie_title", movie.getOriginalTitle());
                    intent.putExtra("movie_releasedate", movie.getMovieReleaseDate());
                }

                view.getContext().startActivity(intent);

            }
        });

        if(type <1) {
            viewHolder.tvTile.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverView());
            Picasso.with(getContext()).load(movie.getImagePath(this.orientation, type)).placeholder(getDrawable())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .resize(500,700)
                    .into(viewHolder.movieImage);

        }else{

            Picasso.with(getContext()).load(movie.getImagePath(this.orientation, type)).placeholder(getDrawable())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .resize(780,800)
                    .into(viewHolder.movieImage);

        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted

        return 2;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        if(getItem(position).getMovieRating()<5)
            return 0;
        else
            return 1;
    }

    private int getDrawable() {

        if (this.orientation == Configuration.ORIENTATION_PORTRAIT)
            return R.drawable.placeholder_portrait;
         else if (this.orientation == Configuration.ORIENTATION_LANDSCAPE)
            return  R.drawable.placeholder_landscape;

        return R.drawable.placeholder_portrait; // All cases where the orientation is not determined.
    }
}
