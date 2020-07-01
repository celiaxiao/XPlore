package com.example.navucsd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navucsd.utils.ClickTracker;
import com.example.navucsd.utils.ClickTrackerUsingIntent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursPageFragment extends Fragment {

    ListViewForScrollView v;
    ScrollView sv;
    ToursAdapter a;

    private String[] nameSetToursForEveryone = {"UCSD's Landmark Tour", "Intro to Brutalism - Architectures at UCSD", "Art Student Tour"};
    private String[] nameSetJourneyThruArt = {"The Stuart Collection Tour"};
    private String[] nameSetAlumniTours = {"Things That Changed Since 2000"};
    private String[] nameSetCollegeTours = {"Revelle College Tour", "Marshall College Tour", "Muir College Tour", "Eleanor Roosevelt College Tour", "Warren College Tour", "Sixth College Tour"};
    private String[] timeSetToursForEveryone = {"1 Hour 30 Mins", "50 Mins", "40Mins"};
    private String[] timeSetJourneyThruArt = {"2 Hours"};
    private String[] timeSetAlumniTours = {"1 Hour 20 Mins"};
    private String[] timeSetCollegeTours = {"1 Hour", "45 Mins", "50 Mins", "45 Mins", "40 Mins", "45 Mins"};
    private int[] stopsSetToursForEveryone = {7, 6, 7};
    private int[] stopsSetJourneyThruArt = {20};
    private int[] stopsSetAlumniTours = {10};
    private int[] stopsSetCollegeTours = {6, 5, 7, 6, 5, 6};
    private int[] picturesToursForEveryone = {R.drawable.tour_landmark, R.drawable.tour_brutalism, R.drawable.tour_art};
    private int[] picturesJourneyThruArt = {R.drawable.tour_stuart};
    private int[] picturesAlumniTours = {R.drawable.tour_alumni};
    private int[] picturesCollegeTours = {R.drawable.tour_revelle, R.drawable.tour_marshall, R.drawable.tour_muir, R.drawable.tour_erc, R.drawable.tour_warren, R.drawable.tour_sixth};

    // Tracks if this page has been clicked; used to prevent multiple clicks.
    private ClickTrackerUsingIntent clickTracker;

    /**
     * Required empty public constructor.
     */
    public ToursPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        clickTracker = new ClickTrackerUsingIntent();
        return inflater.inflate(R.layout.fragment_tours_page, container, false);
    }

    /**
     * Called on resume of this fragment and resets the {@code clicked} attribute
     */
    @Override
    public void onResume() {
        super.onResume();
        clickTracker.reset();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = (ListViewForScrollView) getView().findViewById(R.id.toursForEveryone_lv);
        a = new ToursAdapter(this.getActivity(), nameSetToursForEveryone, timeSetToursForEveryone, stopsSetToursForEveryone, picturesToursForEveryone);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.journeyThruArt_lv);
        a = new ToursAdapter(this.getActivity(), nameSetJourneyThruArt, timeSetJourneyThruArt, stopsSetJourneyThruArt, picturesJourneyThruArt);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.alumniTours_lv);
        a = new ToursAdapter(this.getActivity(), nameSetAlumniTours, timeSetAlumniTours, stopsSetAlumniTours, picturesAlumniTours);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.collegeTours_lv);
        a = new ToursAdapter(this.getActivity(), nameSetCollegeTours, timeSetCollegeTours, stopsSetCollegeTours, picturesCollegeTours);
        v.setAdapter(a);

        // Adjustment for ListViewForScrollView
        sv = (ScrollView) getView().findViewById(R.id.tours_sv);
        sv.smoothScrollTo(0, 0);

        Intent featureComingSoonIntent = new Intent(getContext(), FeatureComingSoonActivity.class);
        getView()
            .findViewById(R.id.cardViewCustomizeTour)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
        getView()
            .findViewById(R.id.start_button)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
    }
}
