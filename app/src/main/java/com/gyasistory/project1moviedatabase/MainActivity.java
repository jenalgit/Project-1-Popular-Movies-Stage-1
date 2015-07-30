package com.gyasistory.project1moviedatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;


public class MainActivity extends Activity {

    final static String TAG = "Movie Results";
     List<MovieDb> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainSync().execute();

    }

    public class MainSync extends AsyncTask<Void, Void, Void>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.setTitle("Loading...");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                TmdbMovies movies = new TmdbApi("dde7202e494003aa6febda923f3a58c5").getMovies();
                Log.i(TAG, movies.getLatestMovie().getTitle());

                movie = movies.getTopRatedMovies("en", 15).getResults();
                Log.i(TAG, movie.get(0).getTitle());
            } catch (Exception e) {
               e.printStackTrace();
                Log.e(TAG, "Something is Wrong", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.cancel();
            //if (movieList != null) {
            for (MovieDb movieI: movie) {
                Toast.makeText(MainActivity.this, movieI.getHomepage() + " \n" + movieI.getOriginalTitle() + " \n" +
                                movieI.getReleaseDate() + '\n' + "https://image.tmdb.org/t/p/w185" + movieI.getPosterPath(),
                        Toast.LENGTH_LONG).show();

                Log.i(TAG, movieI.getHomepage() + " \n" + movieI.getOriginalTitle() + " \n" +
                        movieI.getReleaseDate() + '\n' + "https://image.tmdb.org/t/p/w185" + movieI.getPosterPath());
            }
            //}

        }
    }


}
