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

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursPageFragment extends Fragment {

    ListViewForScrollView v;
    ScrollView sv;

    private String[] nameSet = {"UCSD's Landmark Tour", "Most Popular Restaurants at UCSD", "Intro to Brutalism - \nArchitecture of UCSD", "60 Years by the Beach - \nHistorical Sites at UCSD", "Let's Get Punky - \nHidden Artworks on Campus", "Revelle College"};
    private int[] timeSet = {90, 30, 40, 50, 60, 70};
    private int[] stopsSet = {5, 3, 2, 3, 4, 5};
    private int[] pictures = {R.drawable.tours_landmark, R.drawable.tours_restaurants, R.drawable.tours_brutalism, R.drawable.tours_historical_sites, R.drawable.tours_hidden_artworks, R.drawable.tours_revelle};

    /**
     * If this page has been clicked, used to prevent multiple clicks.
     */
    private boolean clicked;

    public ToursPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tours_page, container, false);
    }

    /**
     * Called on resume of this fragment and resets the {@code clicked} attribute
     */
    @Override
    public void onResume() {
        super.onResume();
        clicked = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = (ListViewForScrollView) getView().findViewById(R.id.tours_lv);
        ToursAdapter a = new ToursAdapter(this.getActivity(), nameSet, timeSet, stopsSet, pictures);
        v.setAdapter(a);

        // Adjustment for ListViewForScrollView
        sv = (ScrollView) getView().findViewById(R.id.tours_sv);
        sv.smoothScrollTo(0, 0);

        getView()
            .findViewById(R.id.cardViewCustomizeTour)
            .setOnClickListener(getOnClickListener(TourOverviewPage.class));
    }

    /**
     * Get a {@code OnClickListener} that starts a specified activity.
     *
     * @param target the activity to be started
     * @return a {@code OnClickListener} that starts {@code target}
     */
    private View.OnClickListener getOnClickListener(Class<?> target) {
        return view -> {
            if (!clicked) {
                clicked = true;
                startActivity(new Intent(getActivity(), target));
            }
        };
    }
}
