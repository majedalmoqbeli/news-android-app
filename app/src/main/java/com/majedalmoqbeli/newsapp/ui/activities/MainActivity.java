package com.majedalmoqbeli.newsapp.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.majedalmoqbeli.newsapp.R;
import com.majedalmoqbeli.newsapp.ui.fragments.MapsFragment;
import com.majedalmoqbeli.newsapp.ui.fragments.NewsFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {


    public  static  Activity APP ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * to close app from any activity or fragment
         * call MainActivity.APP.finish();
         */
        APP = this;

        initBottomNavigationView();
    }


    private void initBottomNavigationView() {
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        Fragment startFragment = new NewsFragment();
        loadFragment(startFragment);

        navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {

                case R.id.nav_map: {
                    fragment = new MapsFragment();
                    break;
                }

                case R.id.nav_news: {
                    fragment = new NewsFragment();
                    break;
                }
            }
            return loadFragment(fragment);
        });

    }

    /**
     * to load fragment
     * @param fragment : your fragment
     * @return :
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}