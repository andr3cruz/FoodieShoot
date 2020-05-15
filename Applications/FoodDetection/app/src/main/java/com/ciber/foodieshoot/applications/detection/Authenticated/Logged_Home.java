package com.ciber.foodieshoot.applications.detection.Authenticated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ciber.foodieshoot.applications.detection.Auxiliar.LayoutAuxiliarMethods;
import com.ciber.foodieshoot.applications.detection.Configs.Configurations;
import com.ciber.foodieshoot.applications.detection.DetectorActivity;
import com.ciber.foodieshoot.applications.detection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

public class Logged_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static Context app_context;
    private DrawerLayout drawer_layout;
    private NavigationView top_nav;
    private Toolbar toolbar;
    private BottomNavigationView bottom_nav;
    private LayoutAuxiliarMethods layout_auxiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged__home);
        Configurations.authenticate();
        app_context = this;
        //Initiate auxiliar
        layout_auxiliar = new LayoutAuxiliarMethods(this);

        //Set top nav
        drawer_layout = findViewById(R.id.drawer_home);
        top_nav = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Use toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Change view when tog
        toolbar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawer_layout.setClickable(true);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        top_nav.setNavigationItemSelectedListener(this);
        top_nav.setCheckedItem(R.id.nav_home);

        //Set bottom nav
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            boolean flag = false;
            Fragment selected = null;
            top_nav.setCheckedItem(menuItem.getItemId());
            switch (menuItem.getItemId()){
                case R.id.home:
                    selected = new HomeFragment();
                    top_nav.setCheckedItem(R.id.nav_home);
                    break;
                case R.id.bottom_camera:
                    flag = true;
                    layout_auxiliar.openActivity(DetectorActivity.class);
                    break;
                case R.id.bottom_posts:
                    selected = new PostFragment();
                    top_nav.setCheckedItem(R.id.nav_shots);
                    break;
            }
            if(!flag)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
            return true;
        }
    };


    /**
     * Click functionality for top nav bar
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        boolean flag = false;
        Fragment selected = null;
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                selected = new HomeFragment();
                bottom_nav.getMenu().findItem(R.id.home).setChecked(true);
                break;
            case R.id.nav_camera:
                flag = true;
                layout_auxiliar.openActivity(DetectorActivity.class);
                break;
            case R.id.nav_shots:
                selected = new PostFragment();
                bottom_nav.getMenu().findItem(R.id.bottom_posts).setChecked(true);
                break;
            case R.id.nav_logout:
                flag = true;
                Configurations.logoutRequest();
                break;
            case R.id.nav_open_site:
                flag = true;
                String endpoint = LayoutAuxiliarMethods.buildUrl(new String[]{Configurations.SERVER_URL});
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(endpoint));
                startActivity(browserIntent);
                break;
        }
        if(!flag)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
        drawer_layout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }

    public static Context getContextOfApplication(){
        return app_context;
    }

    public NavigationView getTopNav(){
        return top_nav;
    }
}
