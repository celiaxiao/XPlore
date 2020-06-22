package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToursAdapter extends BaseAdapter {

    private static int PLACE_NUMBER = 6;

    private String[] nameSet;
    private int[] timeSet;
    private int[] stopsSet;
    private int[] pictures;

    private LayoutInflater mInflater;

    public ToursAdapter(Context c, String[] n, int[] t, int[] s, int[] p){
        nameSet = n;
        timeSet = t;
        stopsSet = s;
        pictures = p;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return PLACE_NUMBER;
    }

    @Override
    public Object getItem(int i) {
        return nameSet[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.tours_item, null);
        TextView nameTV = (TextView) v.findViewById(R.id.main_place_name);
        TextView timeTV = (TextView) v.findViewById(R.id.tours_time);
        TextView stopsTV = (TextView) v.findViewById(R.id.tours_stops);
        ImageView photoIV = (ImageView) v.findViewById(R.id.main_place_photo);

        String name = nameSet[i];
        String time = timeSet[i] + "min";
        String stops = stopsSet[i] + " Stops";
        int photo = pictures[i];

        nameTV.setText(name);
        timeTV.setText(time);
        stopsTV.setText(stops);
        photoIV.setImageResource(photo);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });

        return v;
    }
}
