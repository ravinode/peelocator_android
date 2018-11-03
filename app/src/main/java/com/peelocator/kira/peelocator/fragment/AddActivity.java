package com.peelocator.kira.peelocator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.peelocator.kira.peelocator.R;
import com.peelocator.kira.peelocator.pojo.AddFlush;
import com.peelocator.kira.peelocator.pojo.InfoWindowData;
import com.peelocator.kira.peelocator.pojo.LatLongReq;
import com.peelocator.kira.peelocator.util.LoadProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.peelocator.kira.peelocator.util.FlushUtil;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddActivity extends Fragment {

    private FusedLocationProviderClient client;

    public AddActivity() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.activity_add, container, false);
        Button button = (Button) view.findViewById(R.id.locationButton);
        final EditText name = (EditText)view.findViewById(R.id.name);
        final EditText desc = (EditText)view.findViewById(R.id.desc);
        RadioGroup service = (RadioGroup)view.findViewById(R.id.service);
        RadioGroup sex = (RadioGroup)view.findViewById(R.id.gender);
        final RatingBar cleaness = (RatingBar)view.findViewById(R.id.ratingBar);
        final RatingBar service_Level = (RatingBar)view.findViewById(R.id.servicelevel);
        final RatingBar overall = (RatingBar)view.findViewById(R.id.overall);
        Button submit = (Button) view.findViewById(R.id.submit);
        final RadioButton service_button = (RadioButton)view.findViewById(service.getCheckedRadioButtonId());
        final RadioButton sex_button = (RadioButton)view.findViewById(sex.getCheckedRadioButtonId());
        final TextView locationTextView = (TextView) view.findViewById(R.id.location);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                locationTextView.setText(addresses.get(0).getAddressLine(0));
                            } catch (Exception e) {

                            }
                        }
                    }
                });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                client = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            AddFlush addReq = new AddFlush();
                            addReq.setName(name.getText().toString());
                            addReq.setDescription(desc.getText().toString());
                            addReq.setServiceType(service_button.getText().toString());
                            addReq.setGender(sex_button.getText().toString());
                           // addReq.setC_rating(Integer.toString(cleaness.getNumStars()));
                           // addReq.setS_rating(Integer.toString(service_Level.getNumStars()));
                            //addReq.setRating(Integer.toString(overall.getNumStars()));
                            addReq.setLatitude(String.valueOf(location.getLatitude()));
                            addReq.setLongitude(String.valueOf(location.getLongitude()));
                            addFlush(addReq,getContext());
                        }
                    }
                });



            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                client = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
try {
    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

    locationTextView.setText(addresses.get(0).getAddressLine(0));
}
catch (Exception e)
{

}
                        }
                    }
                });
            }
        });

        return view;
    }

    public static void addFlush(AddFlush addFlush, final Context context) {
        try {
            FirebaseAuth auth;
            String url = null;
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(context, "User ID" + user.getEmail(), Toast.LENGTH_LONG).show();
            JSONObject json = new JSONObject();
            try {
                json.put("id",FlushUtil.getID());
                json.put("userid", user.getEmail());
                json.put("name", addFlush.getName());
                json.put("description", addFlush.getDescription());
                json.put("rating", addFlush.getRating());
                json.put("s_rating", addFlush.getS_rating());
                json.put("c_rating", addFlush.getC_rating());
                json.put("latitude", addFlush.getLatitude());
                json.put("longitude", addFlush.getLongitude());
                json.put("gender", addFlush.getGender());
                json.put("serviceType", addFlush.getServiceType());
                json.put("status","1");
                json.put("point","5");
                json.put("point_status","P");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                url = LoadProperties.getProperty("ADD_LOCATION", context);
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
                                JSONArray jsonArray = response.getJSONArray("pointResp");

                                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                                    Log.i("Length", "" + jsonArray.length());
                                    JSONObject objectInArray = jsonArray.getJSONObject(i);
                                    Log.i("VALUE", "" + objectInArray.getString("points"));

                                    Toast.makeText(context, "Hello" + objectInArray.getString("points"), Toast.LENGTH_LONG).show();

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                                    final RatingBar rating = new RatingBar(context);

                                    dialog.setTitle("Points")

                                            .setMessage( "Pending Points: "+ objectInArray.getString("points"))
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoginterface, int i) {
                                                }
                                            }).show();



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

}
