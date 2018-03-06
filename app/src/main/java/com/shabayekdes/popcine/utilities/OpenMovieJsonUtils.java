package com.shabayekdes.popcine.utilities;


import android.os.Parcel;

import com.shabayekdes.popcine.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class OpenMovieJsonUtils {




    public static List<Movie> getSimpleMoviesData(String moviesJsonResponse)
            throws JSONException {
        final String MOVIES_RESULTS_LIST = "results";

        final String ERROR_MESSAGE_CODE = "code";
        final String POSTER_PATH = "poster_path";
        final String ID = "id";
        final String VOTE_AVERAGE = "vote_average";
        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesDataObject = new JSONObject(moviesJsonResponse);
        if (moviesDataObject.has(ERROR_MESSAGE_CODE)) {
            int errorCode = moviesDataObject.getInt(ERROR_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }


        JSONArray moviesArray = moviesDataObject.getJSONArray(MOVIES_RESULTS_LIST);

        List<Movie> parsedMoviesData = new ArrayList<>();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject = moviesArray.getJSONObject(i);

            int id = movieObject.optInt(ID);
            String title = movieObject.optString(TITLE);
            String voteAverage = movieObject.optString(VOTE_AVERAGE);
            String posterPath = movieObject.optString(POSTER_PATH);
            String overview = movieObject.optString(OVERVIEW);
            String releaseDate = movieObject.optString(RELEASE_DATE);

            Movie movie = new Movie();
            movie.setId(id);
            movie.setTitle(title);
            movie.setVote_average(voteAverage);
            movie.setPoster_path(posterPath);
            movie.setOverview(overview);
            movie.setRelease_date(releaseDate);

            parsedMoviesData.add(movie);
        }


        return parsedMoviesData;
    }



}
