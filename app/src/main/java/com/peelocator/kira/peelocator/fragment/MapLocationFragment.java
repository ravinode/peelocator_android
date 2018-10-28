package com.peelocator.kira.peelocator.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.peelocator.kira.peelocator.CustomInfoWindowGoogleMap;
import com.peelocator.kira.peelocator.MainActivity;
import com.peelocator.kira.peelocator.R;
import com.peelocator.kira.peelocator.pojo.InfoWindowData;
import com.peelocator.kira.peelocator.pojo.LatLongReq;
import com.peelocator.kira.peelocator.util.LoadProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapLocationFragment extends Fragment {



    MapView mMapView;
    private static GoogleMap googleMap;
    static Marker marker;

    private FusedLocationProviderClient client;

    ArrayList<LatLng> markerPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle                                                                          savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView= (MapView) rootView.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        checkLocationPermission();
        getFlush();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (checkLocationPermission()) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        googleMap.setMyLocationEnabled(true);

                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


                    }
                }


                markerPoints = new ArrayList<LatLng>();

                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);


//                LocationManager locationManager = (LocationManager)
//                        getContext().getSystemService(LOCATION_SERVICE);
//                Criteria criteria = new Criteria();
//                String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//
//                Location location = locationManager.getLastKnownLocation(locationManager
//                        .getBestProvider(criteria, false));
//                Location location = getLastKnownLocation();
//                Toast.makeText(getContext(), "Current location:"+location, Toast.LENGTH_LONG).show();
//                if (null != location) {
//                    Toast.makeText(getContext(), "Current location:2"+location, Toast.LENGTH_LONG).show();
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    LatLongReq req = new LatLongReq();
//                    req.setLat(latitude);
//                    req.setLog(longitude);
//
//                    Log.i("","++++++++++"+latitude);
//                    Log.i("","++++++++++"+longitude);
//                    System.out.print("Location:: "+latitude+" Long ::"+longitude);
//                    Toast.makeText(getContext(), "Current location:3"+longitude, Toast.LENGTH_LONG).show();
//
//                    getLocation(req, getContext());


            }
        });
        return rootView;
    }

    private void getFlush() {
        client = LocationServices.getFusedLocationProviderClient(getActivity());

//        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
//        List<String> providers = mLocationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            Toast.makeText(getContext(), "providers:\n" + providers, Toast.LENGTH_LONG).show();
//            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
//                l = mLocationManager.getLastKnownLocation(provider);
//            }
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                bestLocation = l;
//            }
//        }

        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    LatLongReq req = new LatLongReq();
                    req.setLat(location.getLatitude());
                    req.setLog(location.getLongitude());
                    getLocation(req, getActivity());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),new String[]
                                        {ACCESS_FINE_LOCATION},1);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{ACCESS_FINE_LOCATION},
                        1);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        googleMap.setMyLocationEnabled(true);
                        getFlush();

                    }

                }
                return;
            }

        }
    }
    public static void getLocation(LatLongReq latLong, final Context context) {
        try {
            String url = null;

            JSONObject json = new JSONObject();
            try {

                json.put("latitude", latLong.getLat().toString());
                json.put("longtitude", latLong.getLog().toString());
                json.put("distance", "40");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                url = LoadProperties.getProperty("GET_LOCATION", context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Toast.makeText(context, "This is my Toast message!" + url,
            //       Toast.LENGTH_LONG).show();
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST,
                    url, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("flushLoc");

                                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                                    Log.i("Length", "" + jsonArray.length());
                                    JSONObject objectInArray = jsonArray.getJSONObject(i);
                                    Log.i("VALUE", "" + objectInArray.getString("name"));
                                    Double lat = Double.parseDouble(objectInArray.getString("latitude"));
                                    Double log = Double.parseDouble(objectInArray.getString("longitude"));
                                    drawMarker(context, new LatLng(lat, log), objectInArray.getString("name"), objectInArray.getString("description"), objectInArray.getString("distance"));

                                    // Toast.makeText(context, "Hello" + objectInArray, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error"+error, Toast.LENGTH_LONG).show();


                }
            })

            {

                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return volleyError;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjRequest);
        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    private static void drawMarker(final Context context, LatLng point, final String text, final String description, final String distance) {

        marker = googleMap.addMarker(new MarkerOptions().position(point).title(text).alpha(0.7f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setPrice("Free");
        info.setDescription(description);
        info.setDistance("Distance :"+distance);
        //info.setTransport("Reach the site by bus, car and train.");

        marker.setTag(info);
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(context);
        googleMap.setInfoWindowAdapter(customInfoWindow);





        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.i("+++++","Ravi");



                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                final RatingBar rating = new RatingBar(context);
                InfoWindowData adata = (InfoWindowData) marker.getTag();
                rating.setNumStars(5);
                dialog.setTitle( marker.getTitle()).setView(rating)

                        .setMessage( adata.getDescription())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        }).show();
                rating.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

            }
        });

    }


}

