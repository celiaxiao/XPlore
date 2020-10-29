package com.UCSDTripleC.XPloreUCSD;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursPageFragment extends Fragment {

    ListViewForScrollView v;
    ScrollView sv;
    ToursAdapter a;

    private String[] nameSetSignatureTour = {"UC San Diego’s Landmarks Tour"};
    private String[] nameSetJourneyThruArt = {"The Stuart Collection Tour"};
    private String[] nameSetAlumniTours = {"What’s new since 2000 Tour"};
    private String[] nameSetCollegeTours = {"Revelle College Tour", "Marshall College Tour", "Warren College Tour", "Muir College Tour", "Eleanor Roosevelt College Tour",  "Sixth College Tour"};
    private String[] nameSetAcademicSpots={"Study Spaces On Campus Tour"};
    private String[] nameSetCampusLife={"Triton’s Campus Life Tour"};
    private String[] timeSetSignatureTour = {"1 Hour 30 Min"};
    private String[] timeSetJourneyThruArt = {"1 Hour"};
    private String[] timeSetAlumniTours = {"1 Hour 30 Min"};
    private String[] timeSetCollegeTours = {"30 Min", "15 Min", "55 Min", "45 Min", "45 Min", "10 Min"};
    private String[] timeSetAcademicSpots={"45 Min"};
    private String[] timeSetCampusLife={"1 Hour 50 Min"};
    private int[] stopsSetSignatureTour = {15};
    private int[] stopsSetJourneyThruArt = {9};
    private int[] stopsSetAlumniTours = {12};
    private int[] stopsSetCollegeTours = {5, 2, 7, 5, 5, 2};
    private int[] stopsSetAcademicSpots = {5};
    private int[] stopsSetCampusLife = {16};
    private int[] picturesSignatureTour = {R.drawable.tour_signature_landmark};
    private int[] picturesJourneyThruArt = {R.drawable.tour_journey_stuart};
    private int[] picturesAlumniTours = {R.drawable.tour_alumni_2000};
    private int[] picturesCollegeTours = {R.drawable.tour_college_revelle, R.drawable.tour_college_marshall, R.drawable.tour_college_warren, R.drawable.tour_college_muir, R.drawable.tour_college_erc, R.drawable.tour_college_six};
    private int[] picturesAcademicSpots={R.drawable.tour_academic_studyspace};
    private int[] picturesCampusLife={R.drawable.tour_campus_life};
    // Tracks if this page has been clicked; used to prevent multiple clicks.
    private ClickTracker clickTracker;

    /**
     * Required empty public constructor.
     */
    public ToursPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        clickTracker = new ClickTracker();
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

        v = (ListViewForScrollView) getView().findViewById(R.id.signature_tour_lv);
        a = new ToursAdapter(this.getActivity(), nameSetSignatureTour, timeSetSignatureTour, stopsSetSignatureTour, picturesSignatureTour);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.alumniTours_lv);
        a = new ToursAdapter(this.getActivity(), nameSetAlumniTours, timeSetAlumniTours, stopsSetAlumniTours, picturesAlumniTours);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.academic_spots_lv);
        a = new ToursAdapter(this.getActivity(), nameSetAcademicSpots, timeSetAcademicSpots, stopsSetAcademicSpots, picturesAcademicSpots);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.campus_life_lv);
        a = new ToursAdapter(this.getActivity(), nameSetCampusLife, timeSetCampusLife, stopsSetCampusLife, picturesCampusLife);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.journey_through_arts_lv);
        a = new ToursAdapter(this.getActivity(), nameSetJourneyThruArt, timeSetJourneyThruArt, stopsSetJourneyThruArt, picturesJourneyThruArt);
        v.setAdapter(a);
        v = (ListViewForScrollView) getView().findViewById(R.id.ucsd_special_lv);
        a = new ToursAdapter(this.getActivity(), nameSetCollegeTours, timeSetCollegeTours, stopsSetCollegeTours, picturesCollegeTours);
        v.setAdapter(a);

        // Adjustment for ListViewForScrollView
        sv = (ScrollView) getView().findViewById(R.id.tours_sv);
        sv.smoothScrollTo(0, 0);

//        Intent featureComingSoonIntent = new Intent(getContext(), FeatureComingSoonActivity.class);
        Intent featureComingSoonIntent = new Intent(getContext(), FeatureComingSoonActivity.class);
        getView()
            .findViewById(R.id.cardViewCustomizeTour)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
        getView()
            .findViewById(R.id.start_button)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
    }
}
