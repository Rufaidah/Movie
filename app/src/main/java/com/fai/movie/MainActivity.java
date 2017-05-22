package com.fai.movie;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String API_Key = "426ba8a9574c2c4d837f613ea78e87a4";
    TabLayout tabLayout;
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private String FilmCategory;
    private SearchView mnSearch;
    private ItemObject a;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Sliding Drawer
        mNavItems.add(new NavItem("Setting", "Setting Application", R.drawable.icon));
        mNavItems.add(new NavItem("About", "", R.drawable.icon));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.test);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adp = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adp);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext().getApplicationContext(),
                                "Ini Setting",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext().getApplicationContext(),
                                "Developer by.\n" + "Rufaidah Taqiyyah",
                                Toast.LENGTH_LONG).show();
                        break;
                }
                mDrawerList.setItemChecked(position, true);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLAyout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("Popular"));
        tabLayout.addTab(tabLayout.newTab().setText("Now Showing"));
        tabLayout.addTab(tabLayout.newTab().setText("Coming Soon"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getText();
                switch (tab.getPosition()){
                    case 0:
                        requestJsonObject(0);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        requestJsonObject(1);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        requestJsonObject(2);
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void requestJsonObject(int i){
        if (i == 0) {
            setTitle("Popular Movie");
            FilmCategory = "popular";
        } else if (i == 1) {
            setTitle("Top Rated Movie");
            FilmCategory = "top_rated";
        } else if (i == 2) {
            setTitle("Coming Soon");
            FilmCategory = "upcoming";
        }
        String FullURL = "http://api.themoviedb.org/3/movie/" +
                FilmCategory +
                "?api_key=" + API_Key;
        MyParsingGson(FullURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        mnSearch = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mnSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplication(), "Searching" ,Toast.LENGTH_LONG).show();
                String url = "";
                url = "http://api.themoviedb.org/3/search/movie?" +
                        FilmCategory +
                        "&query=" + query +
                        "&api_key=" + API_Key;
                MyParsingGson(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Toast.makeText(getApplicationContext().getApplicationContext(),
                        "Ini Setting",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.mnAbout:
                Toast.makeText(getApplicationContext().getApplicationContext(),
                        "Developer by.\n" + "Rufaidah Taqiyyah",
                        Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (tabLayout.getSelectedTabPosition()){
            case 0:
                requestJsonObject(0);
                break;
            case 1:
                requestJsonObject(1);
                break;
            case 2:
                requestJsonObject(2);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void MyParsingGson(String url) {
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            ;

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                a = mGson.fromJson(response, ItemObject.class);
                adapter = new RecyclerViewAdapter(MainActivity.this, a.results);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Error Response", Toast.LENGTH_SHORT).show();
            }
        });
//         If Need Header Response
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> test = new HashMap<String, String>();
//                test.put("X-Mashape-Key","C0BHO4cIsBmshBlylonaV0Vhb28lp1kKh2xjsn3JP2hFHaO7Dm");
//                test.put("Accept","text/plain");
//                return test;
////                return super.getHeaders();
//            }
//        };
        queue.add(stringRequest);
    }
}