package mbds.nfc.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;


import mbds.nfc.HomeFragment;
import mbds.nfc.MainActivity;
import mbds.nfc.adapter.NavDrawerListAdapter;
import mbds.nfc.modele.NavDrawerItem;
import java.util.ArrayList;

import mbds.nfc.R;
import mbds.nfc.utils.SessionManager;

public class NavigationDrawerActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    public static MenuItem iconeRcherche;
    public static Button genreButton;
    public static Button generationButton;
    public static ImageView imageRecherche;
    public boolean searchShown=false;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navigation_drawer);
        mGoogleApiClient =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();


        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People




        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);



        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
       //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout,
                 //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
               // invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);

                // calling onPrepareOptionsMenu() to hide action bar icons
                 invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item

            displayView(0);
        }


    }
//google
    @Override
    public void onConnected(Bundle bundle) {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    protected void onStop() {
        Log.d("deco", "DEconnection de google");
        super.onStop();
        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }
//fin google
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem itemBlog = menu.add(Menu.NONE, // Group ID
                R.id.action_search, // Item ID
                1, // Order
                "search"); // Title
        itemBlog.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); // ShowAsAction
        this.iconeRcherche=itemBlog;

        itemBlog.setIcon(R.drawable.ic_action_search); // Icon
        // add your item before calling the super method
        itemBlog.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                 if(!searchShown) {
                     genreButton.setVisibility(View.VISIBLE);
                     generationButton.setVisibility(View.VISIBLE);
                     imageRecherche.setVisibility(View.VISIBLE);
                     searchShown=true;
                 }
                else{
                genreButton.setVisibility(View.GONE);
                     generationButton.setVisibility(View.GONE);
                     imageRecherche.setVisibility(View.GONE);
                 searchShown=false;
                 }

                return true;
            }
        });
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        MenuItem itemBlog = menu.add(Menu.NONE, // Group ID
                R.id.action_search, // Item ID
                1, // Order
                "search");
        this.iconeRcherche=itemBlog;
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        boolean analySeFragment=true;
        Fragment fragment = null;

        switch (position) {
            case 0:
                HomeFragment.parcouru=false;
                //Parametres.compteurSelection=0;
                fragment = new HomeFragment();
                break;
            case 1:
               // FragmentSelectionListe.parcouru=false;
                //fragment = new FragmentSelectionListe();
                fragment = new HomeFragment();//new ExpandableSelectionsList();
                break;

            case 2:
                fragment = new HomeFragment();//new ExpandableSelectionsList();
                break;
            case 3:
                fragment = new HomeFragment();//new CommunityFragment();
                break;
            case 4:
                fragment = new HomeFragment();//new PagesFragment();
                break;
            case 5:
                fragment = new HomeFragment();//new WhatsHotFragment();
                break;
            //deconnection
            case 6:
                SessionManager session=new SessionManager(getApplicationContext());
                //session.setLogin(false);
                session.logoutUser();

                if(this.mGoogleApiClient!=null){
                    if(this.mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(this.mGoogleApiClient);
                      //  Plus.AccountApi.revokeAccessAndDisconnect(this.mGoogleApiClient);
                        this.mGoogleApiClient.disconnect();

                    }
                }
                analySeFragment=false;
                Intent intent = new Intent(NavigationDrawerActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();

                break;
            default:
                break;

        }

        if (fragment != null) {


            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            //transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if(!analySeFragment){
            Intent intent = new Intent(NavigationDrawerActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onBackPressed() {
        Log.e("Boutton backk", "boutton back drawer");
       if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
        //getFragmentManager().popBackStack();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}
