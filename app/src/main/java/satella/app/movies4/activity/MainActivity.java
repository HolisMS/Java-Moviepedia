package satella.app.movies4.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import satella.app.movies4.R;
import satella.app.movies4.favorite_page.FavoriteMovieFragment;
import satella.app.movies4.favorite_page.FavoriteTvShowFragment;
import satella.app.movies4.reminder.ReminderSettingActivity;

////// CREATE BY SATELLA ////////

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Fragment pageContent = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.main_drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        pageContent = new HomeFragment();
                        break;
                    case R.id.menu_fav_movie:
                        pageContent = new FavoriteMovieFragment();
                        break;
                    case R.id.menu_fav_tvshow:
                        pageContent = new FavoriteTvShowFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, pageContent).commit();
                toolbar.setTitle(getString(R.string.app_name));
                drawerLayout.closeDrawers();

                return true;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, pageContent).commit();
            toolbar.setTitle(getString(R.string.movies_name));
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent settingLang = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(settingLang);
                break;
            case R.id.action_menu_reminder:
                Intent reminder = new Intent(MainActivity.this, ReminderSettingActivity.class);
                startActivity(reminder);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean doubleTapParam = false;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (doubleTapParam){
            super.onBackPressed();
            return;
        }
        this.doubleTapParam = true;
        Snackbar snackbar = Snackbar.make(drawerLayout, R.string.back_press, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView tv = snackbarView.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(Color.BLACK);
        snackbar.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapParam = false;
            }
        }, 2000);
    }
}
