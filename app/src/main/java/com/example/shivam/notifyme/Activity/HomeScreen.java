package com.example.shivam.notifyme.Activity;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shivam.notifyme.Data.TaskContract;
import com.example.shivam.notifyme.Data.TaskCursorAdapter;
import com.example.shivam.notifyme.R;
import com.stephentuso.welcome.WelcomeHelper;

public class HomeScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

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

    private static final int TASK_LOADER = 0;
    private TaskCursorAdapter taskCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        listView = findViewById(R.id.listview1);
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

        listView.setEmptyView(defaultTextView);
        taskCursorAdapter = new TaskCursorAdapter(this, null);
        listView.setAdapter(taskCursorAdapter);

        getSupportLoaderManager().initLoader(TASK_LOADER, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Log.d("twitter","sd");
                Intent intent = new Intent(getApplicationContext(), EditYourTask.class);
                Uri currentUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });*/
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

    private void delete_record(){
        int id = getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI, null, null);
        if (id == -1) {
            Toast.makeText(this, R.string.deletion_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.deletion_successful, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_data);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete_record();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_TYPE, TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_HOUR,
         TaskContract.TaskEntry.COLUMN_TASk_NOTIFICATION_TIME_MINUTE};

        return new CursorLoader(this, TaskContract.TaskEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        taskCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        taskCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteAllRecord:
                showDeleteConfirmationDialog();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


