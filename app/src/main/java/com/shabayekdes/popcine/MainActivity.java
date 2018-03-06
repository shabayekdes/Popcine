package com.shabayekdes.popcine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shabayekdes.popcine.adapters.MoviesAdapter;
import com.shabayekdes.popcine.models.Movie;
import com.shabayekdes.popcine.utilities.NetworkUtils;
import com.shabayekdes.popcine.utilities.OpenMovieJsonUtils;
import com.shabayekdes.popcine.utilities.PopcineUtilities;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private final static String SORT_TYPE_KEY = "sort_type";
    public final static String MOVIE_KEY = "movie";
    public final static String MOVIE_BUNDLE_KEY = "movie_bundle";
    private final static int MOVIES_LOADER_ID = 13;

    private String sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mRecyclerView = findViewById(R.id.movies_rv);

        int gridColumns = PopcineUtilities.calculateNoOfColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridColumns);


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        final TabLayout sort_type_tab_layout = findViewById(R.id.sort_type_tab_layout);
        sort_type_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String chosenSortType = tab.getText().toString();
                if (chosenSortType.equals(getString(R.string.popular))) {
                    sortType = NetworkUtils.POPULAR;
                } else if (chosenSortType.equals(getString(R.string.top_rated))) {
                    sortType = NetworkUtils.TOP_RATED;
                }
                mMoviesAdapter.setMovies(null);
                loadMovies(sortType);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String chosenSortType = tab.getText().toString();
                if (chosenSortType.equals(getString(R.string.popular))) {
                    if (sortType == null || !sortType.equals(NetworkUtils.POPULAR)) {
                        sortType = NetworkUtils.POPULAR;
                        mMoviesAdapter.setMovies(null);
                        loadMovies(sortType);
                    }
                } else if (chosenSortType.equals(getString(R.string.top_rated))) {
                    if (sortType == null || !sortType.equals(NetworkUtils.TOP_RATED)) {
                        sortType = NetworkUtils.TOP_RATED;
                        mMoviesAdapter.setMovies(null);
                        loadMovies(sortType);
                    }
                }

            }
        });
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        sort_type_tab_layout.getTabAt(0).select();
                    }
                }
                , 100
        );
    }

    private void loadMovies(String sortType) {
        showMoviesDataView();

        Bundle sortTypeBundle = new Bundle();
        sortTypeBundle.putString(SORT_TYPE_KEY, sortType);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> loader = loaderManager.getLoader(MOVIES_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, sortTypeBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, sortTypeBundle, this);
        }
    }


    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickHandler(Movie movie) {
        Toast.makeText(this, "Selected Movie is :" + movie.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_KEY, movie);
        intent.putExtra(MOVIE_BUNDLE_KEY, bundle);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> movies = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                if (movies != null) {
                    deliverResult(movies);
                } else {
                    forceLoad();
                }

            }

            @Override
            public List<Movie> loadInBackground() {
                String sortType = args.getString(SORT_TYPE_KEY);
                URL url = NetworkUtils.buildUrl(sortType);

                try {
                    String moviesJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    movies = OpenMovieJsonUtils.getSimpleMoviesData(moviesJsonResponse);
                    return movies;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return movies;
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showMoviesDataView();
            mMoviesAdapter.setMovies(movies);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}