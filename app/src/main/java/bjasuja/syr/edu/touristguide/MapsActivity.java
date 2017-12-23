package bjasuja.syr.edu.touristguide;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bjasuja.syr.edu.touristguide.POJO2.Example;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng origin;
    LatLng dest;
    ArrayList<LatLng> MarkerPoints;
    TextView ShowDistanceDuration;
    TextView ShowDuration;
    Polyline line;
    int maptype[]=new int[4];
    int i=0;
    String latitude,longitude;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar );
        setSupportActionBar ( myToolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        maptype[0]=GoogleMap.MAP_TYPE_SATELLITE;
        maptype[1]=GoogleMap.MAP_TYPE_TERRAIN;
        maptype[2]=GoogleMap.MAP_TYPE_HYBRID;
        maptype[3]=GoogleMap.MAP_TYPE_NORMAL;

        ShowDistanceDuration = (TextView) findViewById(R.id.show_distance_time);
        ShowDuration = (TextView) findViewById(R.id.via);
        startService(new Intent(this, GPSTracker.class));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        MarkerPoints = new ArrayList<>();

        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);

        Explode exitTransaction = new Explode();
        exitTransaction.setDuration(1000);

        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransaction);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps2 service = retrofit.create(RetrofitMaps2.class);

        Call<Example> call = service.getDistanceDuration("imperial", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, type);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                        ShowDistanceDuration.setText("Duration: Not Available");
                        ShowDuration.setText("Distance: Not Available");
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        String via = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();

                        ShowDistanceDuration.setText("Duration: " + time);
                        ShowDuration.setText("Distance: "+distance);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(9)
                                .color(Color.RED)
                                .geodesic(true)
                        );
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    // Checking if Google Play Services Available or not
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_type)
        {
        mMap.setMapType(maptype[i]);
            i++;
            if(i==4)
            {
                i=0;
            }
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,
                        new IntentFilter("location"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            mMap.clear();
            MarkerPoints.clear();
            MarkerPoints = new ArrayList<>();
            ShowDistanceDuration.setText("Duration: Not Available");
            ShowDuration.setText("Distance: Not Available");


            //String destination=getIntent().getStringExtra("destination");
            //Double latitude2=MovieFragment.latitude2;
            //Double longitude2=MovieFragment.longitude2;
            //Log.d("latitude5", String.valueOf(latitude2));
            final LatLng Model_Town = new LatLng(43.0391534,-76.1351158);
            final LatLng Dest_Town = new LatLng(43.0391534,-76.1351158);

            MarkerOptions options = new MarkerOptions();
            MarkerOptions options2 = new MarkerOptions();
            options.position(Model_Town).title("Your Location");
            options2.position(Dest_Town).title("Destination");

            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.addMarker(options);
            mMap.addMarker(options2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Model_Town));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            final Button btnDriving = (Button) findViewById(R.id.button);
            final Button btnWalk = (Button) findViewById(R.id.button2);
            final Button btnCycle = (Button) findViewById(R.id.button3);
            final Button btntransit = (Button) findViewById(R.id.button4);

            origin = Model_Town;
            dest = Dest_Town;
            build_retrofit_and_get_response("driving");

            btnDriving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDriving.setAlpha(0.7f);
                    btnWalk.setAlpha(1.0f);
                    btnCycle.setAlpha(1.0f);
                    btntransit.setAlpha(1.0f);

                    origin = Model_Town;
                    dest = Dest_Town;
                    build_retrofit_and_get_response("driving");
                }
            });


            btnWalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDriving.setAlpha(1.0f);
                    btnWalk.setAlpha(0.5f);
                    btnCycle.setAlpha(1.0f);
                    btntransit.setAlpha(1.0f);

                    origin = Model_Town;
                    dest = Dest_Town;
                    build_retrofit_and_get_response("walking");
                }
            });


            btnCycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnDriving.setAlpha(1.0f);
                    btnWalk.setAlpha(1.0f);
                    btnCycle.setAlpha(0.5f);
                    btntransit.setAlpha(1.0f);

                    origin = Model_Town;
                    dest = Dest_Town;
                    build_retrofit_and_get_response("bicycling");
                }
            });

            btntransit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDriving.setAlpha(1.0f);
                    btnWalk.setAlpha(1.0f);
                    btnCycle.setAlpha(1.0f);
                    btntransit.setAlpha(0.5f);
                    origin = Model_Town;
                    dest = Dest_Town;
                    build_retrofit_and_get_response("bus_station");
                }
            });
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}
