package com.shabayekdes.popcine.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String postersUrlAuthority = "http://image.tmdb.org/t/p/";


    private static final String POPCINE_URL =
            "http://api.themoviedb.org/3/movie";

    private static final String API_KEY = "api_key";

    public static final String POPULAR = "popular";

    public static final String TOP_RATED = "top_rated";

    final static String api_key = "3fc8642732ddc232ce76bae2e5e9b038";


    public static URL buildUrl(String SORT_TYPE_URL_SEGMENT) {
        Uri buildUri = Uri.parse(POPCINE_URL).buildUpon()
                .appendPath(SORT_TYPE_URL_SEGMENT)
                .appendQueryParameter(API_KEY, api_key)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
