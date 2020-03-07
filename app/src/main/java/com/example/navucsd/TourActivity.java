package com.example.navucsd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class TourActivity extends AppCompatActivity {

    private static final int USER_TYPE_FRAG = 0, TIME_SELECTION_FRAG = 1, LOCATION_SELECTION_FRAG = 2;
    // Location selection fragment components
    ArrayAdapter<String> LocationListAdapter;
    ListView mustGoListView;
    private int NUM_PAGES = 3;
    private ViewPager mainPager;
    private PagerAdapter mainPagerAdapter;
    // Activity components
    private Button backBtn, nextBtn, skipBtn;
    // User type fragment components
    private boolean isTourist = false;
    // Time selection fragment components
    private NumberPicker hourP;
    private NumberPicker minuteP;
    private String[] timeChoices;
    private boolean isTimeFragSet = false, isLocationFragSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        // Instantiate a ViewPager and a PagerAdapter.
        mainPager = findViewById(R.id.main_pager);
        mainPagerAdapter = new MainpageAdapter(getSupportFragmentManager());
        mainPager.setAdapter(mainPagerAdapter);
        backBtn = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.next_btn);
        skipBtn = findViewById(R.id.skip_btn);

    }

    public void touristSelected(View view) {
        isTourist = true;
        nextPage(view);
    }

    public void freshmanSelected(View view) {
        isTourist = false;
        nextPage(view);
    }

    public void nextPage(View view) {
        int currPage = mainPager.getCurrentItem();
        switch (currPage) {
            case USER_TYPE_FRAG:
                setButtonVisibility(View.VISIBLE);
                mainPager.setCurrentItem(TIME_SELECTION_FRAG, true);
                if (!isTimeFragSet) {
                    setUpTimeFrag();
                    isTimeFragSet = true;
                }
                break;
            case TIME_SELECTION_FRAG:
                if (minuteP.getValue() == 0 && hourP.getValue() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.time_cannot_be_zero), Toast.LENGTH_LONG).show();
                } else {
                    String h = String.valueOf(hourP.getValue());
                    String m = String.valueOf(minuteP.getValue() * 30);
                    mainPager.setCurrentItem(LOCATION_SELECTION_FRAG, true);
                    if (!isLocationFragSet) {
                        setUpLocationFrag();
                        isLocationFragSet = true;
                    }
                }
                break;
            case LOCATION_SELECTION_FRAG:
                SparseBooleanArray checked = mustGoListView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add location if it is checked i.e.) == TRUE
                    if (checked.valueAt(i))
                        selectedItems.add(LocationListAdapter.getItem(position));
                }
                if (selectedItems.size() > 0) {
                    String[] outputStrArr = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        outputStrArr[i] = selectedItems.get(i);
                    }
                } else {
                    //pop up alerts, nothing selected
                    locationAlertDialog();
                }
                break;
            default:
                break;
        }
    }

    public void lastPage(View view) {
        int currPage = mainPager.getCurrentItem();
        switch (currPage) {
            case TIME_SELECTION_FRAG:
                mainPager.setCurrentItem(USER_TYPE_FRAG, true);
                setButtonVisibility(View.INVISIBLE);
                break;
            case LOCATION_SELECTION_FRAG:
                mainPager.setCurrentItem(TIME_SELECTION_FRAG, true);
                break;
            default:
                break;
        }
    }

    public void skipPage(View view) {
        int currPage = mainPager.getCurrentItem();
        switch (currPage) {
            case TIME_SELECTION_FRAG:
                timeAlertDialog();
                break;
            case LOCATION_SELECTION_FRAG:
                locationAlertDialog();
                break;
            default:
                break;
        }
    }

    private void setUpTimeFrag() {
        hourP = findViewById(R.id.hourPicker);
        hourP.setWrapSelectorWheel(false);
        hourP.setMaxValue(6);
        hourP.setMinValue(0);

        minuteP = findViewById(R.id.minutesPicker);
        minuteP.setWrapSelectorWheel(false);
        minuteP.setMaxValue(1);
        minuteP.setMinValue(0);

        timeChoices = new String[]{"00", "30"};
        minuteP.setDisplayedValues(timeChoices);

        minuteP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 1 && hourP.getValue() == 6) {
                    hourP.setValue(0);
                }
            }
        });
    }

    private void setUpLocationFrag() {
        mustGoListView = findViewById(R.id.mustGoListView);

        //set up the list view choices
        String[] mustGoList = getResources().getStringArray(R.array.list_of_must_go);
        LocationListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, mustGoList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                CheckedTextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                tv.setCheckMarkDrawable(R.drawable.check_mark);

                // Return the view
                return view;
            }
        };
        ;
        mustGoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mustGoListView.setAdapter(LocationListAdapter);
        Log.d("Location Setup", "finished setup");
    }

    private void timeAlertDialog() {
        String m = "We will just give you our recommended route regardless of how long it will take. Confirm?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Don't have a time limit?");
        builder.setMessage(m).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String h = String.valueOf(0);
                String m = String.valueOf(0);
                mainPager.setCurrentItem(LOCATION_SELECTION_FRAG, true);
                if (!isLocationFragSet) {
                    setUpLocationFrag();
                    isLocationFragSet = true;
                }
            }
        }).setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void locationAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We will just give you our recommended tour, confirm?");
        builder.setTitle("Don't have must-go's?");
        builder.setPositiveButton("Comfirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setButtonVisibility(int visibility) {
        backBtn.setVisibility(visibility);
        nextBtn.setVisibility(visibility);
        skipBtn.setVisibility(visibility);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class MainpageAdapter extends FragmentStatePagerAdapter {

        private UserTypeFragment userTypeFragment;
        private TimeSelectionFragment timeSelectionFragment;
        private LocationSelectionFragment locationSelectionFragment;

        public MainpageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case USER_TYPE_FRAG:
                    if (userTypeFragment == null) {
                        userTypeFragment = new UserTypeFragment();
                    }
                    return userTypeFragment;
                case TIME_SELECTION_FRAG:
                    if (timeSelectionFragment == null) {
                        timeSelectionFragment = new TimeSelectionFragment();
                    }
                    return timeSelectionFragment;
                case LOCATION_SELECTION_FRAG:
                    if (locationSelectionFragment == null) {
                        locationSelectionFragment = new LocationSelectionFragment();
                    }
                    return locationSelectionFragment;
                default:
                    return new UserTypeFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
