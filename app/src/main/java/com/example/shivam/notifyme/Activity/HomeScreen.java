package com.example.shivam.notifyme.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shivam.notifyme.Data.DatabaseHelper;
import com.example.shivam.notifyme.Data.TaskContract;
import com.example.shivam.notifyme.R;
import com.stephentuso.welcome.WelcomeHelper;

public class HomeScreen extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private WelcomeHelper welcomeScreen;
    private ListView listView;
    private TextView defaultTextView;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private static final String TAG_HOME = "home";
    private static final String TAG_PERFORMANCE = "performance";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_ABOUT_US = "about us";
    public static String CURRENT_TAG = TAG_HOME;

    // flag to load home activity when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        listView = findViewById(R.id.listview);
        defaultTextView = findViewById(R.id.defaultTextView);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        welcomeScreen = new WelcomeHelper(this, WelcomeScreenActivity.class);
        welcomeScreen.show(savedInstanceState);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddYourTask.class);
                startActivity(intent);
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHome();
        }

        displayDatabaseInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    private void loadNavHeader() {
        // showing dot next to notifications label
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected activity that user
     * selected from navigation menu
     */
    private void loadHome() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        //Closing drawer on item click
        drawer.closeDrawers();

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Intent intent;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        // home
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_performance:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PERFORMANCE;
                        intent = new Intent(getApplicationContext(), Performance.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        intent = new Intent(getApplicationContext(),NotificationActivity.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        intent = new Intent(getApplicationContext(),Settings.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_about_us:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ABOUT_US;
                        intent = new Intent(getApplicationContext(),AboutUs.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        break;
                    default:
                        navItemIndex = 0;
                        drawer.closeDrawers();
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHome();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHome();
                return;
            }
        }

        super.onBackPressed();
    }

    private void displayDatabaseInfo() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TaskContract.TaskEntry.TABLE_NAME, null);
        try
        {
            Log.e("twitter"," SD "+cursor.getCount());
        }
        finally {
            cursor.close();
        }
    }
}


