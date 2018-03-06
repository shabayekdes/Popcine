package com.shabayekdes.popcine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    ImageView poster_iv;
    TextView overview_tv;
    TextView release_date_tv;
    TextView vote_average_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        poster_iv = findViewById(R.id.poster_iv);
        overview_tv = findViewById(R.id.overview_tv);
        release_date_tv = findViewById(R.id.release_date_tv);
        vote_average_tv = findViewById(R.id.vote_average_tv);

        Intent intent = getIntent();
        Bundle movieBundle = intent.getBundleExtra(MainActivity.MOVIE_BUNDLE_KEY);
        Movie movie = (Movie) movieBundle.getParcelable(MainActivity.MOVIE_KEY);

        String title = movie.getTitle();
        String voteAverage = movie.getVote_average();
        String posterPath = movie.getPoster_path();
        String overview = movie.getOverview();
        String releaseDate = movie.getRelease_date();

        String screenWidth = getString(R.string.screen_width_photo);
        String posterUrl = NetworkUtils.postersUrlAuthority + screenWidth + posterPath;

        Picasso.with(this).load(posterUrl).into(poster_iv);

        actionBar.setTitle(title);
        vote_average_tv.setText(voteAverage);
        overview_tv.setText(overview);
        release_date_tv.setText(releaseDate);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
