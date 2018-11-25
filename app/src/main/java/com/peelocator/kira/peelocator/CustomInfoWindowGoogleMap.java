package com.peelocator.kira.peelocator;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.peelocator.kira.peelocator.pojo.InfoWindowData;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_info_window, null);

        TextView name_tv = view.findViewById(R.id.name);

        TextView price_v = view.findViewById(R.id.price);
        TextView distance_v = view.findViewById(R.id.distance);
        TextView status_txt = view.findViewById(R.id.status_txt);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        ImageView image  = (ImageView) view.findViewById(R.id.status);
        Resources res = view.getResources();


        name_tv.setText(marker.getTitle());


        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        price_v.setText(infoWindowData.getPrice());
        distance_v.setText(infoWindowData.getDistance());
        if("2".equalsIgnoreCase(infoWindowData.getStatus())) {
            image.setImageDrawable(res.getDrawable(R.drawable.green));
            status_txt.setText("Approved");
        }
        else
        {
            image.setImageDrawable(res.getDrawable(R.drawable.red));
            status_txt.setText("Not Verified");
        }
        //ratingBar.setRating(Float.parseFloat("2.0"));

        return view;
    }
}
