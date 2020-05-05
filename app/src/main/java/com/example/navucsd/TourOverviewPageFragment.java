package com.example.navucsd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TourOverviewPageFragment extends Fragment {
    Object[] items = {"item 1", "item 2", "item 3", "item 4", "item 5"};

    public TourOverviewPageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_overview_page, container, false);
        ListView TourOverviewPageListView = (ListView) view.findViewById(R.id.TourOverviewPageListView);
        ListViewAdapterTourOverViewPage listViewAdapter = new ListViewAdapterTourOverViewPage(getContext(), items);
        TourOverviewPageListView.setAdapter(listViewAdapter);



        return view;
    }
}
