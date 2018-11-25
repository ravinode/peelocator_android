package com.peelocator.kira.peelocator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.peelocator.kira.peelocator.pojo.Point;
import com.peelocator.kira.peelocator.util.LoadProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveActivity extends AppCompatActivity {

    String id = null;
    String nameTxt = null;
    String descTxt = null;
    String distance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#746E66'>      Pee Locator</font>"));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_approve);
        TextView name = (TextView) findViewById(R.id.pending);
        TextView desc = (TextView) findViewById(R.id.desc);
        TextView dista = (TextView) findViewById(R.id.distance);

        Button submit = (Button) findViewById(R.id.approve);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            id = extras.getString("id");
            nameTxt = extras.getString("name");
            descTxt = extras.getString("desc");
            distance = extras.getString("distance");

            name.setText("Name :"+nameTxt);
            desc.setText("Description :"+descTxt);
            dista.setText("Distance :"+distance);
        //    Toast.makeText(getApplicationContext(), "Current location:"+extras.get("id"), Toast.LENGTH_LONG).show();
//                if (null != location) {




        }

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                addFlush(id,getApplicationContext());
            }
        });

    }

    public  void addFlush(String id, final Context context) {
        try {

            String url = null;

            // Toast.makeText(context, "User ID" + user.getEmail(), Toast.LENGTH_LONG).show();
            JSONObject json = new JSONObject();
            try {
                json.put("id",id);
                json.put("point", "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                url = LoadProperties.getProperty("APPROVE", context);
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
                            List<Point> pointList = new ArrayList<>();

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
