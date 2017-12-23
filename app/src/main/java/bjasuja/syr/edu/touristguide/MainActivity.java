package bjasuja.syr.edu.touristguide;

import android.*;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pollfish.main.PollFish;
import com.pollfish.main.PollFish.ParamsBuilder;
import com.pollfish.constants.Position;

import com.apptracker.android.listener.AppModuleListener;
import com.apptracker.android.track.AppTracker;

import static bjasuja.syr.edu.touristguide.RecyclerviewFragment.mShowingBack;

public class MainActivity extends AppCompatActivity implements RecyclerviewFragment.CustomOnClickListener, GoogleApiClient.OnConnectionFailedListener,
        RecyclerviewFragment2.CustomOnClickListener,Recyclerviewfragment3.CustomOnClickListener,RecyclerviewFragment4.CustomOnClickListener,
        RecyclerviewFragment5.CustomOnClickListener,RecyclerviewFragment6.CustomOnClickListener,RecyclerviewFragment7.CustomOnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener{
        int movieno,pageno;
        private NavigationView navigationView;
        static DrawerLayout drawer;
        private View navHeader;
        static  ImageView imgNavHeaderBg, imgProfile;
        private TextView txtName, txtWebsite;
        static  public Toolbar toolbar,bottomT;
        private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
        private static final String urlProfileImg = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/p/7/005/085/301/0c1ca8b.jpg";
        public static int navItemIndex = 0;
        private static final String TAG_HOME = "Home";
        private static final String TAG_Aboutus = "MY_FRAGMENT";
        private static final String TAG_Task2 = "Task 2";
        private static final String TAG_Task3 = "Task 3";
        private static final String TAG_Task1 = "Task 1";
        private static final String TAG_Task4 = "Task 4";
        public static String CURRENT_TAG = TAG_HOME;
        private String[] activityTitles;

        private boolean shouldLoadHomeFragOnBackPress = true;
        private Handler mHandler;
        static  TextView mTitle;
        ImageView mImage;
        Fragment fragment,fragment2;
        private static final String TWITTER_KEY = "hBghpsxHYutSTKBl1EYJvntxw";
        private static final String TWITTER_SECRET = "85mURLOXbEJAJGn6MGzuPYrYwMDDDVWXEXjKbu0Sji29iFPdJK";
        public static String username,state,email,mobile,gender,rating,password,logout,fbid,fbname,fbemail,twid,twname,pic,glid,glname,twemail,glemail;
        public static int ID;
        public static Bitmap image,bitmap;
        static MyBillin b1;
        static String id;
        static Boolean addisabled=true,surveydisabled=true,fabhidden=true;
        FloatingActionButton fab=null;
        private GoogleApiClient mGoogleApiClient;
        int PLACE_PICKER_REQUEST = 1;
        ActionBarDrawerToggle actionBarDrawerToggle;
        List<Movie> movieList;
        static String latlong;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            super.onCreate(savedInstanceState);

            FacebookSdk.sdkInitialize(getApplicationContext());
            TwitterConfig config = new TwitterConfig.Builder(this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET))
                    .debug(true)
                    .build();
            Twitter.initialize(config);

            setContentView(R.layout.activity_main);
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);

            startService(new Intent(this, GPSTracker.class));

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            bottomT = (Toolbar) findViewById (R . id . bot_toolbar );
            //bottomT . inflateMenu ( R. menu . bottom_toolbar );
            EnhancedMenuInflater.inflate(getMenuInflater(),bottomT.getMenu(), false);
            setupBottomToolbarItemSelected () ;

            getSupportActionBar().setDisplayShowTitleEnabled ( false );

            mTitle = (TextView)toolbar . findViewById (R. id . toolbar_title );

            mHandler = new Handler();

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);

            navHeader = navigationView.getHeaderView(0);
            txtName = (TextView) navHeader.findViewById(R.id.name);
            txtWebsite = (TextView) navHeader.findViewById(R.id.website);
            imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
            imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
            mTitle.setText(activityTitles[navItemIndex]);
            loadNavHeader();
            setUpNavigationView();

            if (savedInstanceState == null) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }

            b1=new MyBillin(this);
            b1.onCreate();

            AppTracker.startSession(getApplicationContext(), "iZWEEkX5gzTJ0Ic7GEjmGgtEvho54av5", AppTracker.ENABLE_AUTO_CACHE);
            AppTracker.loadModuleToCache(getApplicationContext(), "reward");
            AppTracker.loadModuleToCache(getApplicationContext(), "inapp");

            fab = (FloatingActionButton) findViewById(R.id.fab);
            final GestureDetector gestureDetector=new GestureDetector(this,new SingleTapDetector());
            fab.setOnTouchListener(new View.OnTouchListener() {
                float dX=0;
                float dY=0;
                int lastAction=-1;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {

                    } else {

                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                dX = v.getX() - event.getRawX();
                                dY = v.getY() - event.getRawY();
                                lastAction = MotionEvent.ACTION_DOWN;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                v.setY(event.getRawY() + dY);
                                v.setX(event.getRawX() + dX);
                                lastAction = MotionEvent.ACTION_MOVE;
                                break;

                            case MotionEvent.ACTION_UP:
                                if (lastAction == MotionEvent.ACTION_DOWN) {
                                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                    try {
                                        startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                                    } catch (GooglePlayServicesRepairableException e) {
                                        e.printStackTrace();
                                    } catch (GooglePlayServicesNotAvailableException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            default:
                                return false;
                        }
                    }
                    return true;
                }
            });

            fab.hide();

            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }

       ;
    }

    private class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e1) {
                e1.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e1) {
                e1.printStackTrace();
            }
            return true;
        }

    }
        private void setupBottomToolbarItemSelected () {
            bottomT . setOnMenuItemClickListener (new Toolbar. OnMenuItemClickListener () {
                @Override
                public boolean onMenuItemClick ( MenuItem item ) {
                    int id = item . getItemId () ;
                    switch ( id ){
                        case R. id . bottom_action3 :
                            if(fragment instanceof ViewPagerFragment)
                                if(ViewPagerFragment.pageno==0)
                                {
                                    Movie.flag = false;
                                    Collections.sort(RecyclerviewFragment.movieList);
                                    RecyclerviewFragment.mAdapter.notifyDataSetChanged();

                                }
                                else if(ViewPagerFragment.pageno==1)
                                {
                                    Movie.flag = false;
                                    Collections.sort(RecyclerviewFragment2.movieList);
                                    RecyclerviewFragment2.mAdapter.notifyDataSetChanged();
                                }
                                else if(ViewPagerFragment.pageno==2)
                                {
                                    Movie.flag = false;
                                    Collections.sort(Recyclerviewfragment3.movieList);
                                    Recyclerviewfragment3.mAdapter.notifyDataSetChanged();
                                }
                                else if(ViewPagerFragment.pageno==3)
                                {
                                    Movie.flag = false;
                                    Collections.sort(RecyclerviewFragment4.movieList);
                                    RecyclerviewFragment4.mAdapter.notifyDataSetChanged();
                                }
                            else if(ViewPagerFragment.pageno==4)
                            {
                                Movie.flag = false;
                                Collections.sort(RecyclerviewFragment5.movieList);
                                RecyclerviewFragment5.mAdapter.notifyDataSetChanged();
                            }
                            else if(ViewPagerFragment.pageno==5)
                            {
                                Movie.flag = false;
                                Collections.sort(RecyclerviewFragment6.movieList);
                                RecyclerviewFragment6.mAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Movie.flag = false;
                                Collections.sort(RecyclerviewFragment7.movieList);
                                RecyclerviewFragment7.mAdapter.notifyDataSetChanged();
                            }
                            return true ;

                        case R. id . bottom_action2 :
                            if(fragment instanceof ViewPagerFragment)
                            {
                                if(addisabled) {
                                    stopad();
                                    addisabled = false;
                                    final Snackbar snackbar = Snackbar
                                            .make((CoordinatorLayout) findViewById((R.id.CoordinatorLayout)), "Ads Disabled", Snackbar.LENGTH_LONG);
                                    snackbar.setAction("Done", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                                else
                                {
                                    AppTracker.startSession(getApplicationContext(), "iZWEEkX5gzTJ0Ic7GEjmGgtEvho54av5", AppTracker.ENABLE_AUTO_CACHE);
                                    AppTracker.loadModuleToCache(getApplicationContext(), "reward");
                                    AppTracker.loadModuleToCache(getApplicationContext(), "inapp");
                                    addisabled = true;
                                    final Snackbar snackbar = Snackbar
                                            .make((CoordinatorLayout) findViewById((R.id.CoordinatorLayout)), "Ads Enabled", Snackbar.LENGTH_LONG);
                                    snackbar.setAction("Done", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                            }

                            return true ;

                        case R. id . bottom_action1 :
                            if(fragment instanceof ViewPagerFragment)
                            {
                                if(surveydisabled) {
                                    PollFish.hide();
                                    surveydisabled = false;
                                    final Snackbar snackbar = Snackbar
                                            .make((CoordinatorLayout) findViewById((R.id.CoordinatorLayout)), "Survey Disabled", Snackbar.LENGTH_LONG);
                                    snackbar.setAction("Done", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                                else
                                {
                                    PollFish.show();
                                    surveydisabled = true;
                                    final Snackbar snackbar = Snackbar
                                            .make((CoordinatorLayout) findViewById((R.id.CoordinatorLayout)), "Survey Enabled", Snackbar.LENGTH_LONG);
                                    snackbar.setAction("Done", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                            }
                            return true ;
                        case R. id . bottom_action4 :
                           // if(fragment instanceof ViewPagerFragment)
                            {
                                if(fabhidden) {
                                    fabhidden=false;
                                    fab.show();
                                }
                                else
                                {
                                    fabhidden=true;
                                    fab.hide();
                                }
                            }
                            return true ;
                    }
                    return false ;
                }
            }) ;
            bottomT . setNavigationIcon (R.drawable.ic_visibility_off_black_24dp);
            bottomT . setNavigationOnClickListener ( new View . OnClickListener () {
                @Override
                public void onClick ( View v ) {
                    bottomT . setVisibility ( View . GONE );
                }
            }) ;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClicked(View v,int position,List<Movie> movielist) {
            movieno=position;
            this.movieList=movielist;
            pageno=ViewPagerFragment.pageno;
            FragmentManager fragmentManager = getSupportFragmentManager();
            ViewPagerFragment masterFragment = (ViewPagerFragment) fragmentManager.findFragmentByTag(CURRENT_TAG);
            FragmentManager fm = masterFragment.getFragmentManager();
            MovieFragment detailFragment=(MovieFragment)fm.findFragmentByTag("DetailFragment");
            FragmentTransaction ft = fm.beginTransaction();
            if (detailFragment == null)
            {
                detailFragment = new MovieFragment();
            }
            fragment2=detailFragment;

            Bundle bundle = new Bundle();
            bundle.putString("transitionName", "transition" + position+pageno);
            detailFragment.setArguments(bundle);
            masterFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
            masterFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
            detailFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
            detailFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            ft.replace(R.id.frame, detailFragment, "DetailFragment");
            ft.addToBackStack(null);
            ft.addSharedElement(v.findViewById(R.id.imageView10), "transition"+movieno+pageno);
            ft.commit();
        }

        private void loadNavHeader() {
            Glide.with(this).load(urlNavHeaderBg)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgNavHeaderBg);
        }

        private void loadHomeFragment() {
            selectNavMenu();
            if(navItemIndex==3 || getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) == null)
            {}
            else {
                mTitle.setText(activityTitles[navItemIndex]);
            }
            if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
                drawer.closeDrawers();
                return;
            }

            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    fragment = getHomeFragment();
                    if(fragment!=null) {
                        if(navItemIndex==3 )
                        {}
                        else {
                            mTitle.setText(activityTitles[navItemIndex]);
                        }
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                        //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }
            };

            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }

            drawer.closeDrawers();
            invalidateOptionsMenu();
        }

         Fragment getHomeFragment() {

            switch (navItemIndex) {
                case 0:
                    ViewPagerFragment fragment=new ViewPagerFragment();
                    bottomT.setVisibility(View.GONE);
                    return fragment;

                case 1:
                    UpdateProfile fr1=new UpdateProfile();
                    bottomT.setVisibility(View.GONE);

                    if(SplashScreen.logout==true)
                    {
                        if (mShowingBack) {
                            getFragmentManager().popBackStack();
                            mShowingBack=false;
                        }
                        return fr1;
                    }
                    else
                    {
                        return null;
                    }

                case 2:
                    ContactUs fr2=new ContactUs();
                    bottomT.setVisibility(View.GONE);
                    if (mShowingBack) {
                        getFragmentManager().popBackStack();
                        mShowingBack=false;

                    }
                    return fr2;

                case 3:
                    bottomT.setVisibility(View.GONE);
                    if(SplashScreen.logout==true) {
                        if (mShowingBack) {
                            getFragmentManager().popBackStack();
                            mShowingBack=false;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.rating,null);
                        builder.setView(dialogView);
                        final AlertDialog alert = builder.create();
                        Button b=(Button)dialogView.findViewById(R.id.button7);
                        final  RatingBar r=(RatingBar) dialogView.findViewById(R.id.rateapp);
                        r.setRating(Float.parseFloat(rating));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rating= String.valueOf(r.getRating());
                                new JSONP2().execute(rating);
                                alert.dismiss();
                            }
                        });
                        alert.show();
                        return null;
                    }
                    else
                    {
                        return  null;
                    }


                case 4:
                    AboutusFragment fr = new AboutusFragment();
                    bottomT.setVisibility(View.GONE);
                    if (mShowingBack) {
                        getFragmentManager().popBackStack();
                        mShowingBack=false;
                    }
                    return fr;

                case 5:
                    PlaceHistory fr5=new PlaceHistory();
                    bottomT.setVisibility(View.GONE);
                    if(SplashScreen.logout==true) {
                        if (mShowingBack) {
                            getFragmentManager().popBackStack();
                            mShowingBack=false;
                        }
                        return fr5;
                    }
                    else
                    {
                        return  null;
                    }

                default:
                    return null;
            }
        }


        private void selectNavMenu() {
            navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        }

        private void setUpNavigationView() {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME;
                            break;
                        case R.id.nav_aboutus:
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_Aboutus;
                            break;
                        case R.id.nav_task2:
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_Task2;
                            break;
                        case R.id.nav_task3:
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_Task3;
                            break;
                        case R.id.nav_task1:
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_Task1;
                            break;
                        case R.id.nav_task4:
                            navItemIndex = 5;
                            CURRENT_TAG = TAG_Task4;
                            break;
                        default:
                            navItemIndex = 0;
                    }

                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    } else {
                        menuItem.setChecked(true);
                    }
                    menuItem.setChecked(true);
                    loadHomeFragment();

                    return true;
                }
            });

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawer.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }

        @Override
        public void onBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers();
                return;
            }

            else if (shouldLoadHomeFragOnBackPress) {
                if (navItemIndex != 0) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;

                    loadHomeFragment();
                    return;
                }
                else if (mShowingBack) {
                    getFragmentManager().popBackStack();
                    mShowingBack=false;
                    return;
                }
                else if(navItemIndex == 0 && (fragment2!=null))
                {
                      fragment2=null;
                      mTitle.setText(activityTitles[navItemIndex]);
                      super.onBackPressed();

                }
                else {
                    AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Do you really want to close this app?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog b3 = b2.create();
                    b3.setTitle("Exit");
                    b3.show();
                }
            }
        }



    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(MainActivity.this);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("location"));

        if(SplashScreen.logout==true)
        {
            DatabaseHelper dh=new DatabaseHelper(MainActivity.this);
            Cursor b10=dh.getalldata();
            if(b10.getCount()>0)
            {
                while(b10.moveToNext())
                {
                    ID= b10.getInt(0);
                    username= b10.getString(1);
                    password= b10.getString(2);
                    logout= b10.getString(3);
                }

                AvatarPlaceholder av=new AvatarPlaceholder(username);
                pic="www.example.com";
                Picasso.with(this)
                        .load(pic)
                        .placeholder(av)
                        .fit()
                        .into(imgProfile);
                txtName.setText(username);
                txtName.setTextSize(13);

                new JSONP().execute();
            }
        }

        else if(SplashScreen.fblogout==true)
        {
            FbDatabaseHelper dh1=new FbDatabaseHelper(MainActivity.this);
            dh1.getalldata();

            Cursor b10=dh1.getalldata();
            if(b10.getCount()>0)
            {
                while(b10.moveToNext())
                {
                    ID= b10.getInt(0);
                    logout= b10.getString(1);
                    fbid= b10.getString(2);
                    fbname= b10.getString(3);
                    pic= b10.getString(4);
                    fbemail=b10.getString(5);
                }
                txtName.setText(fbname);
                txtWebsite.setText(fbemail);
                AvatarPlaceholder av=new AvatarPlaceholder(fbname);

                Picasso.with(this)
                        .load(pic)
                        .placeholder(av)
                        .fit()
                        .into(imgProfile);
            }
        }

        else if(SplashScreen.twlogout==true)
        {
            TwDatabaseHelper dh2=new TwDatabaseHelper(MainActivity.this);
            Cursor b10=dh2.getalldata();
            if(b10.getCount()>0)
            {
                while(b10.moveToNext())
                {
                    ID= b10.getInt(0);
                    logout= b10.getString(1);
                    twid= b10.getString(2);
                    twname= b10.getString(3);
                    pic= b10.getString(4);
                    twemail=b10.getString(5);
                }
                txtName.setText(twname);
                txtWebsite.setText(twemail);
                AvatarPlaceholder av=new AvatarPlaceholder(twname);

                Picasso.with(this)
                        .load(pic)
                        .placeholder(av)
                        .fit()
                        .into(imgProfile);
            }
        }
        else if(SplashScreen.gllogout==true)
        {
            GlDatabaseHelper dh2=new GlDatabaseHelper(MainActivity.this);
            Cursor b10=dh2.getalldata();
            if(b10.getCount()>0)
            {
                while(b10.moveToNext())
                {
                    ID= b10.getInt(0);
                    logout= b10.getString(1);
                    glid= b10.getString(2);
                    glname= b10.getString(3);
                    pic= b10.getString(4);
                    glemail=b10.getString(5);
                }
                txtName.setText(glname);
                txtWebsite.setText(glemail);
                AvatarPlaceholder av=new AvatarPlaceholder(glname);

                Picasso.with(this)
                        .load(pic)
                        .placeholder(av)
                        .fit()
                        .into(imgProfile);
            }
        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final LatLng Model_Town = new LatLng(GPSTracker.mLastLocation.getLatitude(),GPSTracker.mLastLocation.getLongitude());

            if(GPSTracker.mLastLocation.getLatitude()!=0 && GPSTracker.mLastLocation.getLongitude()!=0)
            {
                 latlong=GPSTracker.mLastLocation.getLatitude()+","+GPSTracker.mLastLocation.getLongitude();

                ((ViewPagerFragment)fragment).masterFragment.setJSON(ViewPagerFragment.s);
                ((ViewPagerFragment)fragment).masterFragment2.setJSON(ViewPagerFragment.s2);
                ((ViewPagerFragment)fragment).masterFragment3.setJSON(ViewPagerFragment.s3);
                ((ViewPagerFragment)fragment).masterFragment4.setJSON(ViewPagerFragment.s4);
                ((ViewPagerFragment)fragment).masterFragment5.setJSON(ViewPagerFragment.s5);
                ((ViewPagerFragment)fragment).masterFragment6.setJSON(ViewPagerFragment.s6);
                ((ViewPagerFragment)fragment).masterFragment7.setJSON(ViewPagerFragment.s7);
            }
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        b1.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getPlaceTypes());

                id=place.getId();

                FragmentManager fragmentManager = getSupportFragmentManager();
                ViewPagerFragment masterFragment = (ViewPagerFragment) fragmentManager.findFragmentByTag(CURRENT_TAG);
                FragmentManager fm = masterFragment.getFragmentManager();
                MovieFragment detailFragment=(MovieFragment)fm.findFragmentByTag("DetailFragment");
                FragmentTransaction ft = fm.beginTransaction();
                if (detailFragment == null)
                {
                    detailFragment = new MovieFragment();
                }
                fragment2=detailFragment;

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                ft.replace(R.id.frame, detailFragment, "DetailFragment");
                ft.addToBackStack(null);
                ft.commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        b1.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class JSONP extends AsyncTask<Void,Void,Bitmap>
    {
        String jsonurl,jsonstring;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            jsonurl="http://www.touristguide.gq/pic.php";
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            String data="";
            try {
                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                String postdata= URLEncoder.encode("username")+"="+URLEncoder.encode(MainActivity.username)+ "&" + URLEncoder.encode("password") + "=" + URLEncoder.encode(password);

                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder  stringBuilder=new StringBuilder();
                while((jsonstring= bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(jsonstring+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONObject reader = new JSONObject(stringBuilder.toString());
                JSONArray sys = reader.optJSONArray("server_response");
                for(int i=0; i < sys.length(); i++) {
                    JSONObject jsonObject = sys.getJSONObject(i);
                    email = jsonObject.getString("email");
                    state = jsonObject.getString("state");
                    mobile = jsonObject.getString("mobile");
                    gender = jsonObject.getString("gender");
                    rating = jsonObject.getString("rating");

                    image = decodeBase64(jsonObject.getString("pic"));
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  image;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            if(s != null)
            {
                bitmap=s;
                imgProfile.setImageBitmap(s);
                txtWebsite.setText(email);
            }
        }


        public  Bitmap decodeBase64(String input)
        {
            byte[] decodedBytes = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    class JSONP2 extends AsyncTask<String,Void,String>
    {
        String jsonurl="http://www.touristguide.gq/rating.php",jsonstring;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String feedback=params[0];

            String data="";
            try {
                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                String postdata= URLEncoder.encode("rating")+"="+URLEncoder.encode(feedback)+ "&" + URLEncoder.encode("username")+"="+URLEncoder.encode(MainActivity.username)+ "&" + URLEncoder.encode("password") + "=" + URLEncoder.encode(password);

                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder  stringBuilder=new StringBuilder();
                while((jsonstring= bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(jsonstring+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return  data;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void stopad()
    {
        AppTracker.closeSession(getApplicationContext(),true);
    }

    }


