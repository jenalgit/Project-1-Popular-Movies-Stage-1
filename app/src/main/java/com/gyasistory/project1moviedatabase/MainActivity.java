package com.gyasistory.project1moviedatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;


public class MainActivity extends Activity {

    final static String TAG = "Movie Results";
    List<MovieDb> movie;
    GridView mMainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainSync().execute();
        mMainGrid = (GridView) findViewById(R.id.topMovieGrid);

    }

    public class MainSync extends AsyncTask<Void, Void, Void> {
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
            if (movie != null) {

                CustomGridAdapter adapter = new CustomGridAdapter(MainActivity.this,
                         movie);
                mMainGrid.setAdapter(adapter);
            }

        }

        public class CustomGridAdapter extends BaseAdapter{
            Context context;
            List<MovieDb> movieDbList;

            public CustomGridAdapter(Context context, List<MovieDb> movieDbList) {
                this.context = context;
                this.movieDbList = movieDbList;
            }

            /**
             * How many items are in the data set represented by this Adapter.
             *
             * @return Count of items.
             */
            @Override
            public int getCount() {
                return movieDbList.size();
            }

            /**
             * Get the data item associated with the specified position in the data set.
             *
             * @param position Position of the item whose data we want within the adapter's
             *                 data set.
             * @return The data at the specified position.
             */
            @Override
            public MovieDb getItem(int position) {
                return movieDbList.get(position);
            }

            /**
             * Get the row id associated with the specified position in the list.
             *
             * @param position The position of the item within the adapter's data set whose row id we want.
             * @return The id of the item at the specified position.
             */
            @Override
            public long getItemId(int position) {
                return 123456000 + position;
            }

            /**
             * Get a View that displays the data at the specified position in the data set. You can either
             * create a View manually or inflate it from an XML layout file. When the View is inflated, the
             * parent View (GridView, ListView...) will apply default layout parameters unless you use
             * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
             * to specify a root view and to prevent attachment to the root.
             *
             * @param position    The position of the item within the adapter's data set of the item whose view
             *                    we want.
             * @param convertView The old view to reuse, if possible. Note: You should check that this view
             *                    is non-null and of an appropriate type before using. If it is not possible to convert
             *                    this view to display the correct data, this method can create a new view.
             *                    Heterogeneous lists can specify their number of view types, so that this View is
             *                    always of the right type (see {@link #getViewTypeCount()} and
             *                    {@link #getItemViewType(int)}).
             * @param parent      The parent that this view will eventually be attached to
             * @return A View corresponding to the data at the specified position.
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_row, parent,false);
                }
                MovieDb movieDb = getItem(position);



                ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.customImageView);
                Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + movieDb.getPosterPath())
                        .into(imageViewcustom);

                return convertView;
            }
        }
    }


}
