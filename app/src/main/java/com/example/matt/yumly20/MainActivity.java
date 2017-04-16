package com.example.matt.yumly20;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeScreenFragment.OnFragmentInteractionListener,
        MyFridgeFragment.OnFragmentInteractionListener,
        MyRecipesFragment.OnFragmentInteractionListener,
        CookBookFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener{


    private static HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
    private static MyFridgeFragment myFridgeFragment = new MyFridgeFragment();
    private static MyRecipesFragment myRecipesFragment = new MyRecipesFragment();
    private static CookBookFragment cookBookFragment = new CookBookFragment();
    private static SettingsFragment settingsFragment = new SettingsFragment();
    private static Fragment currentFragment = homeScreenFragment;
    private CharSequence currentTitle = "Home";
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        setTitle(currentTitle);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();


        if (id == R.id.home_frag) {
            currentFragment = homeScreenFragment;
            currentTitle = "Home";
        } else if (id == R.id.fridge_frag) {
            currentFragment = myFridgeFragment;
            currentTitle = "My Fridge";
        } else if (id == R.id.my_recipes_frag) {
            currentFragment = myRecipesFragment;
            currentTitle = "Favorite Recipes";
        } else if (id == R.id.cookbook_frag) {
            currentFragment = cookBookFragment;
            currentTitle = "Cookbook";
        } else if (id == R.id.settings_frag) {
            currentFragment = settingsFragment;
            currentTitle = "Settings";
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
