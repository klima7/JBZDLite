package com.example.jbzdlite;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class PreparationTask extends AsyncTask {

    private MainActivity activity;

    public PreparationTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        boolean success = false;
        while(!success) {
            try {
                Categories.get().init();
                success = true;
            } catch (IOException e) { }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        // fill drawer with categories
        NavigationView navView = activity.findViewById(R.id.navigation_view);
        navView.setItemIconTintList(null);

        Menu menu = navView.getMenu();

        for(Category category : Categories.get()) {
            MenuItem item = menu.add(R.id.menu_group, Menu.NONE, Menu.NONE, category.getName());
            item.setIcon(R.drawable.item_decoration);

            if(category == Categories.MAIN_CATEGORY)
                item.setChecked(true);
        }

        menu.setGroupCheckable(R.id.menu_group, true, true);
    }
}