package org.ucsdtriplec.xploreucsd;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// classes needed to add the location component
// classes to calculate a route
// classes needed to launch navigation UI

public class NavigationActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {


    private static final String TAG = "DirectionsActivity";
    public String LOCATIONS = "LOCATIONS";
    public Intent intent;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Button button;
    private List<Point> list;
    private Pair current;
    private Point origin;
    private String[] locationsSelections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main_mapbox);
        intent = getIntent();
        locationsSelections = intent.getStringArrayExtra(LOCATIONS);
        list = new ArrayList<>();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);

                        String[] pathfind = pathfinder(locationsSelections, current);

                        Log.d("PathSize", pathfind[0]);
                        for (int i = 0; i < pathfind.length; i++) {
                            getPoint(pathfind[i], list);
                        }

                        getRoute(list);
                        button = findViewById(R.id.startButton);
                        button.setEnabled(true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean simulateRoute = false;
                                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                        .directionsRoute(currentRoute)
                                        .shouldSimulateRoute(false)
                                        .build();
                                // Call this method with Context from within an Activity
                                NavigationLauncher.startNavigation(NavigationActivity.this, options);
                            }
                        });
                    }
                });
    }

    private void getRoute(List<Point> l) {

        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin);
        if (l.size() > 1) {
            for (int i = 0; i < l.size() - 1; i++) {
                builder.addWaypoint(l.get(i));
            }
        }
        builder.destination(l.get(l.size() - 1))
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });

    }

    // Get the user locations
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();


            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Get the current location information(Longitude and latitude)
            Location location = locationComponent.getLastKnownLocation();
            try {
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();
                current = new Pair(latitude, longitude);
                origin = Point.fromLngLat(longitude, latitude);
            } catch (NullPointerException e) {
            }

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getPoint(String s, List<Point> list) {
        Landmark t = new Landmark(s);
        Double latitude = (double) t.location.first;
        Double longitude = (double) t.location.second;
        list.add(Point.fromLngLat(longitude, latitude));
    }


    /**
     * GLORIOUS GLORIOUS NP-HARD SOLUTION TO FIGURE OUT THE MOST OPTIMAL PATH HAHAHAAH
     *
     * @param locationList: should be just the list of location that the user selects in the spinner
     * @param startingPos:  starting position of the user, this should be a pair if
     */
    private String[] pathfinder(String[] locationList, Pair startingPos) {
        //uses the string length to parse the shit
        Landmark[] landmarkList = new Landmark[locationList.length];
        //creates the pq with the order of the location list
        PriorityQueue<Landmark> pq = new PriorityQueue<>(locationList.length,
                new LandmarkComparator());
        double minVal = Integer.MAX_VALUE;
        //starting location
        Landmark startingLoc = null;
        //parse initial the string
        for (int i = 0; i < locationList.length; i++) {
            Log.d("TEST", locationList[i]);
            //initializes the landmark object wit the necessary thing
            Landmark t = new Landmark(locationList[i]);
            double startX = (double) startingPos.first;
            double startY = (double) startingPos.second;
            double locX = (double) t.location.first;
            double locY = (double) t.location.second;
            Log.d("PATHFINDER", "before testing the thing ");
            //finds starting location based on the closest
            //using a trusty distance formula
            Log.d("PATHFINDER", "the distance found is " + Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2)));
            Log.d("PATHFINDER", "current min value is " + minVal);
            if (Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2)) < minVal) {
                Log.d("PATHFINDER", "what the fuck");
                minVal = Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2));
                startingLoc = t;
            }
            //landmarkList[i] = t;
            //pushes it to the priority queue
            pq.add(t);
        }
        //prunes the list to find the optimal route
        //if it is middle of pq, fuck, start it on the side that has less
        //if it is first or last, fuck yea
        //TODO: change direction of collegeVal if it is fucked up
        //TODO: inner colleges need a more robust way of knowing which direction
        // the user previously came in
        int i = 0;
        int stop = 0;
        boolean reached = false;
        Landmark[] pre = new Landmark[locationList.length];
        Landmark[] post = new Landmark[locationList.length];
        //college vals for identifying clusters
        int preCollegeVal = pq.peek().collegeVal;
        //a count for how many clusters encountered thus far
        int count = 1;
        Log.d("PATHFINDER", "starting location is " + startingLoc.toString());
        //set up the pruning
        while (!pq.isEmpty()) {
            Log.d("PATHFINDER", "how many fucking times am I here");
            //just checking and then setting up the pre and post
            if (!reached) {
                Log.d("PATHFINDER", "In pre");
                pre[i] = pq.poll();
                Log.d("PATHFINDER", "popped is a " + pre[i].toString());
                if (pre[i].collegeVal != preCollegeVal) {
                    count++;
                    preCollegeVal = pre[i].collegeVal;
                }
            }
            if (!reached && pre[i].equals(startingLoc)) {
                reached = true;
                stop = i;
            }
            //last of pre is going to be equal to
            else if (reached) {
                Log.d("PATHFINDER", "in post");
                post[i] = pq.poll();
                Log.d("PATHFINDER", "popped is a " + post[i].toString());
                if (post[i].collegeVal != preCollegeVal) {
                    count++;
                    preCollegeVal = post[i].collegeVal;
                }
            }
            i++;
        }
        Landmark[] retVal = new Landmark[locationList.length];
        //prune the list
        retVal = prune(pre, post, pq, stop, locationList.length, retVal);
        Log.d("PATHFINDER", "outside of everything and retVal is" + retVal[0]);
        String[] ret = new String[locationList.length];
        for (int ind = 0; ind < retVal.length; ind++) {
            Log.d("PATHFINDER", "ind at " + ind + "is " + retVal[ind]);
            ret[ind] = retVal[ind].toString();
        }
        return ret;
    }


    /**
     * @param pre
     * @param post
     * @param pq
     * @param i
     * @param size
     */
    private Landmark[] prune(Landmark[] pre, Landmark[] post, PriorityQueue<Landmark> pq,
                             int i, int size, Landmark[] retVal) {
        ArrayList<Landmark> temp = new ArrayList<Landmark>();
        //checking the start of the loop first and everything before it
//        int collegeVal = pre[i].collegeVal;
//        int clusters = 1;
//        int preVal = pre[0].collegeVal;
//        //finds all the clusters before it
//        for(int x = 1; x < i; x++){
//            if(pre[x].collegeVal.intValue() != preVal){
//                //counting how many clusters it has before the starting point
//                preVal = pre[x].collegeVal.intValue();
//                clusters++;
//            }
//        }
        Log.d("PATHFINDER", "size is " + size + " and i is " + i);
        if (i > size / 2) {
            Log.d("PATHFINDER", "first if better be here");
            //Case 1: closest to left side and upwards, but there are inputs on right side
            //Meaning it has to be either in Marshall or in ERC, then go to the top left corner
            //first and then go down (basically reverse)
            reverseList(pre, post, size - i);
            //setting the return value of the return value;
            retVal = pre;
            Log.d("PATHFINDER", "1111" + retVal);
        } else {
            Log.d("PATHFINDER", "first else better be here");
            //Case 2: a bit more nuanced, where if the starting location is in the middle, then
            //force them to start at the bottom of the loop
            //IF THE FIRST LOCATION PICKED PICKED IN PQ IS BIOMED OR ABOVE
            if (pre[0].collegeVal.intValue() >= 2) {
                appendList(pre, post, i + 1);
                retVal = pre;
                return retVal;

            }
            //IF THE FIRST LOCATION PICKED PICKED IN PQ IS WARREN AND YOU'RE CLOSEST TO SIXTH
            else if (pre[0].collegeVal.intValue() == 0 && pre[i].collegeVal.intValue() == 1) {
                appendList(pre, post, i + 1);
                retVal = pre;
                return retVal;
            } else {
                appendList(pre, post, i + 1);
                retVal = pre;
                Log.d("PATHFINDER", "4444" + retVal[0].toString());
                return retVal;
            }
        }
        return null;
    }

    private void appendList(Landmark[] pre, Landmark[] post, int index) {
        for (; index < pre.length; index++) {
            pre[index] = post[index];
        }
    }

    private void reverseList(Landmark[] pre, Landmark[] post, int index) {
        Landmark[] temp = new Landmark[pre.length];
        for (int i = 0; i < pre.length; i++) {
            if (i < index) {
                temp[i] = post[post.length - i - 1];
            } else temp[i] = pre[pre.length - i - 1];
        }
        pre = temp;
    }
}
