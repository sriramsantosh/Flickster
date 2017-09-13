package com.aripir.flickster.adapters;

import android.content.Context;
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
import com.aripir.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

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

        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if(convertView ==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTile  = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview  = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.movieImage);

            viewHolder.movieImage.setImageResource(0);

            viewHolder.tvTile.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverView());

            Picasso.with(getContext()).load(movie.getImagePath(this.orientation)).into(viewHolder.movieImage);

            convertView.setTag(viewHolder);
        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}
