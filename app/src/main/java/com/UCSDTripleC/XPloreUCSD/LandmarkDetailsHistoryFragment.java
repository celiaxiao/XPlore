package com.UCSDTripleC.XPloreUCSD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.UCSDTripleC.XPloreUCSD.database.History;
import com.UCSDTripleC.XPloreUCSD.database.Landmark;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandmarkDetailsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandmarkDetailsHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Landmark currLandmark;

    private String[] titleSet = {"Great Research Starts Here", "A Brutalist Icon"};
    private String[] descriptionSet = {"Geisel Library’s Foundation ・3 Min Read", "Architecture・2 Min Read"};
    private String[] storySet = {"Story 1", "Story 2"};
    private int currItem = 0;

    private TextView textViewTitle, textViewDescription, textViewNumber, textViewContent;

    private ImageButton upButton, downButton;
    private View seperator;

    public LandmarkDetailsHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currLandmark Current landmark.
     * @return A new instance of fragment LandmarkDetailsOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandmarkDetailsHistoryFragment newInstance(Landmark currLandmark) {
        LandmarkDetailsHistoryFragment fragment = new LandmarkDetailsHistoryFragment();
        fragment.setLocation(currLandmark);
        return fragment;
    }

    private void setLocation(Landmark currLandmark) {
        this.currLandmark = currLandmark;
        ArrayList<History> histories = currLandmark.getHistory();
        titleSet = new String[histories.size()];
        descriptionSet = new String[histories.size()];
        storySet = new String[histories.size()];
        for (int i = 0; i < histories.size(); i++) {
            titleSet[i] = histories.get(i).getTitle();
            descriptionSet[i] = histories.get(i).getArticleType();
            storySet[i] = histories.get(i).getContent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landmark_detail_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = view.findViewById(R.id.history_title);
        textViewDescription = view.findViewById(R.id.history_description);
        textViewNumber = view.findViewById(R.id.history_number);
        textViewContent = view.findViewById(R.id.history_content);

        upButton = view.findViewById(R.id.history_up);
        downButton = view.findViewById(R.id.history_down);
        seperator = view.findViewById(R.id.history_up_down_divider);

        setContent(0);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContent(--currItem);
                updateButtons();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContent(++currItem);
                updateButtons();
            }
        });
    }

    private void updateButtons() {
        if (currItem == 0) {
            upButton.setVisibility(View.GONE);
            seperator.setVisibility(View.GONE);
            downButton.setVisibility(View.VISIBLE);
        }
        else if (currItem == storySet.length - 1) {
            upButton.setVisibility(View.VISIBLE);
            seperator.setVisibility(View.GONE);
            downButton.setVisibility(View.GONE);
        }
        else {
            upButton.setVisibility(View.VISIBLE);
            downButton.setVisibility(View.VISIBLE);
            seperator.setVisibility(View.VISIBLE);
        }
    }

    private void setContent(int index) {
        textViewTitle.setText(titleSet[index]);
        textViewDescription.setText(descriptionSet[index]);
        textViewNumber.setText(Integer.toString(index + 1));
        textViewContent.setText(storySet[index]);
    }

}
