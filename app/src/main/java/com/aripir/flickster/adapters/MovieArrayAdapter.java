package com.aripir.flickster.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.flickster.R;
import com.aripir.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by saripirala on 9/11/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie>{
    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if(convertView ==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movieImage);

        imageView.setImageResource(0);

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView textViewOriginalOverview = (TextView) convertView.findViewById(R.id.tvOverview);


        textViewTitle.setText(movie.getOriginalTitle());
        textViewOriginalOverview.setText(movie.getOverView());

        Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);

        return convertView;
    }
}
