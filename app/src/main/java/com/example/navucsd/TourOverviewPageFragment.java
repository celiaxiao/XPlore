package com.example.navucsd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<Object> items = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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


//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewTourOverviewPage);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mAdapter = new RecyclerViewAdapterTourOverviewPage(getContext(), items);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);


        ListView TourOverviewPageListView = (ListView) view.findViewById(R.id.TourOverviewPageListView);
        ListViewAdapterTourOverViewPage listViewAdapter = new ListViewAdapterTourOverViewPage(getContext(), items);
        TourOverviewPageListView.setAdapter(listViewAdapter);

        return view;
    }
}
