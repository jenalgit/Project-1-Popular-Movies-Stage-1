package com.gyasistory.project1moviedatabase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    final static String TAG = "Movie Results";

    GridView mMainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (NetworkConnections.networkcheck(MainActivity.this)) {
            new MainSync().execute();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.network_alert_title));
            dialog.setMessage(getString(R.string.network_alert_message));
            dialog.setCancelable(false);
            dialog.show();
        }
        mMainGrid = (GridView) findViewById(R.id.topMovieGrid);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MainSync extends AsyncTask<Void, Void, String> {
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
        protected String doInBackground(Void... params) {

            String results = ""; //Set up Variable for result

            String WebAddress = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
                    + PasscodeString.UserKey;
            try {
                URL url = new URL(WebAddress);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                results = IOUtils.toString(inputStream);
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                results = "N/A";

            } catch (IOException e) {
                e.printStackTrace();
                results = "N/A";

            }


            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayList<Movie> movies = new ArrayList<>();

            try {
                JSONObject mainObject = new JSONObject(s);

                JSONArray resultsArray = mainObject.getJSONArray("results");
                for (int i = 0; i< resultsArray.length(); i++){
                    JSONObject indexObject = resultsArray.getJSONObject(i);
                    Movie indexMovie = new Movie();
                    indexMovie.setBackdrop_path(indexObject.getString("backdrop_path"));
                    indexMovie.setId(indexObject.getInt("id"));
                    indexMovie.setOriginal_title(indexObject.getString("original_title"));
                    indexMovie.setOverview(indexObject.getString("overview"));
                    indexMovie.setRelease_date(indexObject.getString("release_date"));
                    indexMovie.setPoster_path(indexObject.getString("poster_path"));
                    indexMovie.setPopularity(indexObject.getDouble("popularity"));
                    indexMovie.setTitle(indexObject.getString("title"));

                    movies.add(indexMovie); // Add each item to the list
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("MainActivity", "JSON Error", e);
            }

            if (movies != null) {

                CustomGridAdapter adapter = new CustomGridAdapter(MainActivity.this,
                         movies);
                mMainGrid.setAdapter(adapter);

                mMainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie movie = (Movie) parent.getAdapter().getItem(position);

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("Movie", movie);
                        startActivity(intent);

                    }
                });
            }
            dialog.cancel();

        }

        public class CustomGridAdapter extends BaseAdapter{
            Context context;
            ArrayList<Movie> movieList;

            public CustomGridAdapter(Context context, ArrayList<Movie> movieDbList) {
                this.context = context;
                this.movieList = movieDbList;
            }

            /**
             * How many items are in the data set represented by this Adapter.
             *
             * @return Count of items.
             */
            @Override
            public int getCount() {
                return movieList.size();
            }

            /**
             * Get the data item associated with the specified position in the data set.
             *
             * @param position Position of the item whose data we want within the adapter's
             *                 data set.
             * @return The data at the specified position.
             */
            @Override
            public Movie getItem(int position) {
                return movieList.get(position);
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
                Movie movieDb = getItem(position);



                ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.customImageView);
                Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + movieDb.getPoster_path())
                        .into(imageViewcustom);

                return convertView;
            }
        }
    }


}
