package com.chanchal.sindhubhawanshaadi.newsfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int Book_LOADER_ID = 1;

    private static final String News_api = "https://content.guardianapis.com/search";

    private static ListView list;

    private static NewsAdapter adapter;

    private static ProgressBar progressBar;

    private static TextView emptyScreen;

    private static TextView searchBar;

    private static Button search;

    private static String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();

//        AsyncTaskNews asyncTaskNews = new AsyncTaskNews();
//        asyncTaskNews.execute(News_api);

        list = (ListView) findViewById(R.id.list);

        adapter = new NewsAdapter(MainActivity.this , new ArrayList<News>());

        list.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        emptyScreen = findViewById(R.id.emptyScreen);

        list.setEmptyView(emptyScreen);

        if(netInfo!=null && netInfo.isConnectedOrConnecting())
        {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(Book_LOADER_ID , null , this);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            emptyScreen.setText("Please Check Your Internet Connection");

        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = adapter.getItem(i);
                Uri url = Uri.parse(news.getWebUrl());
                Intent newsDetail = new Intent(Intent.ACTION_VIEW , url );
                startActivity(newsDetail);
            }
        });

        searchBar = (TextView) findViewById(R.id.searchBar);

        search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchBar.clearFocus();
                emptyScreen.setText("");
                QueryUtils.hideSoftKeyboard(MainActivity.this);
                adapter.clear();
                searchQuery = searchBar.getText().toString();

                if(TextUtils.isEmpty(searchQuery))
                {
                    searchBar.setError("Please write Something");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getLoaderManager().restartLoader(Book_LOADER_ID , null , MainActivity.this);
                }
            }
        });

    }

    @NonNull
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tag = sharedPref.getString(
                getString(R.string.settings_sectionName_key),
                getString(R.string.settings_sectionName_default)
        );
        String page_size = sharedPref.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_value)
        );

        String from_date = sharedPref.getString(
                getString(R.string.settings_from_date_key),
                getString(R.string.settings_from_date_value)
        );

        Uri baseUri = Uri.parse(News_api);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q" , searchQuery);
        uriBuilder.appendQueryParameter("tag" , tag);
        uriBuilder.appendQueryParameter("from-date" , from_date);
        uriBuilder.appendQueryParameter("page-size",page_size);
        uriBuilder.appendQueryParameter("api-key","1b6fbfef-4f9e-48f5-969c-a771acd42137");

        return new AsyncNewsLoader(this , uriBuilder.toString());
    }

    @Override
    public void onLoadFinished( Loader<ArrayList<News>> loader, ArrayList<News> data) {

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        emptyScreen.setText("No News updates Found");

        adapter.clear();

        if(data!=null && !data.isEmpty())
        {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<News>> loader) {
        adapter.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.settings)
        {
            Intent settingsIntent = new Intent(this , SettingsNews.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    private class AsyncTaskNews extends AsyncTask<String , Void  , ArrayList<News>>{
//
//        @Override
//        protected ArrayList<News> doInBackground(String... urls) {
//
//            if(urls == null || urls.length == 0)
//            {
//                return null;
//            }
//            else{
//                ArrayList<News> news = QueryUtils.fetchNewsApi(urls[0]);
//                return news;
//            }
//        }
//
//        public  void onPostExecute(ArrayList<News> news)
//        {
//            adapter.clear();
//            if(news!=null && !news.isEmpty())
//            {
//                adapter.addAll(news);
//            }
//        }
//    }

    private static class AsyncNewsLoader extends AsyncTaskLoader<ArrayList<News>>{

        private String Url;
        public AsyncNewsLoader( Context context , String Url) {
            super(context);
            this.Url = Url;
        }

        @Nullable
        @Override
        public ArrayList<News> loadInBackground() {
           if(Url == null)
           {
               return null;
           }
           ArrayList<News> news = QueryUtils.fetchNewsApi(Url);
            return news;
        }

        public void onStartLoading(){
            forceLoad();
        }
    }

}



