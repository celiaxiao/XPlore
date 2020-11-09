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

import com.UCSDTripleC.XPloreUCSD.database.Tour;
import com.UCSDTripleC.XPloreUCSD.database.TourDatabase;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursPageFragment extends Fragment {

    //tour database
    TourDatabase tourDatabase;

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

        //set up tour database
        tourDatabase=new TourDatabase(getContext());

        //dynamically set up tour overview details
        String[] timeset=new String[nameSetSignatureTour.length];
        int[] stopsset=new int[nameSetSignatureTour.length];
        for(int i=0;i<nameSetSignatureTour.length;i++){
            Tour tour=tourDatabase.getByName(nameSetSignatureTour[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.signature_tour_lv);
        a = new ToursAdapter(this.getActivity(), nameSetSignatureTour, timeset, stopsset, picturesSignatureTour);
        v.setAdapter(a);
         timeset=new String[nameSetAlumniTours.length];
         stopsset=new int[nameSetAlumniTours.length];
        for(int i=0;i<nameSetAlumniTours.length;i++){
            Tour tour=tourDatabase.getByName(nameSetAlumniTours[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.alumniTours_lv);
        a = new ToursAdapter(this.getActivity(), nameSetAlumniTours, timeset, stopsset, picturesAlumniTours);
        v.setAdapter(a);

        timeset=new String[nameSetAcademicSpots.length];
        stopsset=new int[nameSetAcademicSpots.length];
        for(int i=0;i<nameSetAcademicSpots.length;i++){
            Tour tour=tourDatabase.getByName(nameSetAcademicSpots[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.academic_spots_lv);
        a = new ToursAdapter(this.getActivity(), nameSetAcademicSpots, timeset, stopsset, picturesAcademicSpots);
        v.setAdapter(a);

        timeset=new String[nameSetCampusLife.length];
        stopsset=new int[nameSetCampusLife.length];
        for(int i=0;i<nameSetCampusLife.length;i++){
            Tour tour=tourDatabase.getByName(nameSetCampusLife[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.campus_life_lv);
        a = new ToursAdapter(this.getActivity(), nameSetCampusLife, timeset, stopsset, picturesCampusLife);
        v.setAdapter(a);

        timeset=new String[nameSetJourneyThruArt.length];
        stopsset=new int[nameSetJourneyThruArt.length];
        for(int i=0;i<nameSetJourneyThruArt.length;i++){
            Tour tour=tourDatabase.getByName(nameSetJourneyThruArt[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.journey_through_arts_lv);
        a = new ToursAdapter(this.getActivity(), nameSetJourneyThruArt, timeset, stopsset, picturesJourneyThruArt);
        v.setAdapter(a);

        timeset=new String[nameSetCollegeTours.length];
        stopsset=new int[nameSetCollegeTours.length];
        for(int i=0;i<nameSetCollegeTours.length;i++){
            Tour tour=tourDatabase.getByName(nameSetCollegeTours[i]);
            stopsset[i]= tour.getPlaces().size();
            timeset[i]=convertTime(stopsset[i]);
        }
        v = (ListViewForScrollView) getView().findViewById(R.id.ucsd_special_lv);
        a = new ToursAdapter(this.getActivity(), nameSetCollegeTours, timeset, stopsset, picturesCollegeTours);
        v.setAdapter(a);

        // Adjustment for ListViewForScrollView
        sv = (ScrollView) getView().findViewById(R.id.tours_sv);
        sv.smoothScrollTo(0, 0);

//        Intent featureComingSoonIntent = new Intent(getContext(), FeatureComingSoonActivity.class);
        Intent featureComingSoonIntent = new Intent(getContext(), TourOverviewPage.class);
        getView()
            .findViewById(R.id.cardViewCustomizeTour)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
        getView()
            .findViewById(R.id.start_button)
            .setOnClickListener(clickTracker.getOnClickListener(featureComingSoonIntent));
    }
    String convertTime(int numStop){
        String time;
        if(numStop<3) time= 20*numStop+" Min";
        else if(numStop<6) time=20*numStop/60+" Hour "+20*numStop%60+" Min";
        else time=20*numStop/60+" Hours "+20*numStop%60+" Min";
        return time;
    }
}
