package com.UCSDTripleC.XPloreUCSD;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.UCSDTripleC.XPloreUCSD.database.Tour;
import com.UCSDTripleC.XPloreUCSD.database.TourDatabase;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

import java.io.IOException;
import java.io.InputStream;

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

    private String[][] tours_nameSet={nameSetSignatureTour,nameSetJourneyThruArt,nameSetCampusLife,nameSetAcademicSpots,
                                        nameSetAlumniTours,nameSetCollegeTours};
    private int[] recyclerviewId={R.id.signature_tour_lv,R.id.journey_through_arts_lv,R.id.campus_life_lv,
                                        R.id.academic_spots_lv,R.id.alumniTours_lv,R.id.ucsd_special_lv};
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
        for(int k=0;k<tours_nameSet.length;k++) {
            String[] nameset=tours_nameSet[k];
            String[] timeset = new String[nameset.length];
            int[] stopsset = new int[nameset.length];
            Drawable[] graphicWithBg = new Drawable[nameset.length];

            for (int i = 0; i < nameset.length; i++) {
                Tour tour = tourDatabase.getByName(nameset[i]);
                stopsset[i] = tour.getPlaces().size();
                timeset[i] = convertTime(stopsset[i]);
                String graphic = tour.getGraphicsWithBackground();
                try {
                    // get input stream
                    //InputStream ims = getAssets().open(currLandmark.getOtherPhotos().get(position));
                    InputStream ims = getContext().getAssets().open(graphic);
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    graphicWithBg[i]=d;
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
            v = (ListViewForScrollView) getView().findViewById(recyclerviewId[k]);
            a = new ToursAdapter(this.getActivity(), nameset, timeset, stopsset, graphicWithBg);
            v.setAdapter(a);
        }


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
    static String convertTime(int numStop){
        String time;
        if(numStop<3) time= 20*numStop+" Min";
        else if(numStop<6) {
            if(numStop==3) {
                time=20*numStop/60+" Hour";
            }
            else time=20*numStop/60+" Hour "+20*numStop%60+" Min";
        }
        else {
            if(numStop%3==0){
                time=20*numStop/60+" Hours";
            }
            else time=20*numStop/60+" Hours "+20*numStop%60+" Min";
        }
        return time;
    }

}
