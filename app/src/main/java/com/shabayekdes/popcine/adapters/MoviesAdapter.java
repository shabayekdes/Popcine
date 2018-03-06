package com.shabayekdes.popcine.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shabayekdes.popcine.R;
import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mk_25 on 27/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context mContext;

    private List<Movie> mMovies;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler mClickHandler) {
        this.mContext = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String poster_path = movie.getPoster_path();
        Uri poster_url = Uri.parse(NetworkUtils.postersUrlAuthority + "w185" + poster_path).buildUpon()
                .build();
        Picasso.with(mContext).load(poster_url).into(holder.poster);
        holder.title_tv.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        } else {
            return mMovies.size();
        }
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        TextView title_tv;
        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_iv);
            title_tv = itemView.findViewById(R.id.item_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);
            mClickHandler.onClickHandler(movie);
        }
    }

    public interface MoviesAdapterOnClickHandler {
        void onClickHandler(Movie movie);
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
