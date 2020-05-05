package com.example.navucsd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TourOverviewPageFragment extends Fragment {
    ArrayList<Object> items = new ArrayList<>();

    public TourOverviewPageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_overview_page, container, false);
        for (int i = 0; i < 5; i++) {
            items.add("item " + i);
        }

        ListView TourOverviewPageListView = (ListView) view.findViewById(R.id.TourOverviewPageListView);
        ListViewAdapterTourOverViewPage listViewAdapter = new ListViewAdapterTourOverViewPage(getContext(), items);
        TourOverviewPageListView.setAdapter(listViewAdapter);



        return view;
    }
}
