package com.example.jbzdlite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // set drawer listener
        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(MainActivity.this);
        disableNavigationViewScrollbars(navView);


        // set toggle button
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setCategory(Categories.MAIN_CATEGORY.getName());

        new PreparationTask(this).execute();
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // change category
        String category = menuItem.getTitle().toString();
        setCategory(category);

        // close drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);

        return true;
    }

    public void setCategory(String category) {
        // change toolbar title
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        String title = getString(R.string.toolbar_prefix) + "  -  " + category;
        toolbar.setTitle(title);

        // change fragment
        MemesFragment fragment = MemesFragment.newInstance(category);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }
}