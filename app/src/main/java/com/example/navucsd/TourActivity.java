package com.example.navucsd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.util.PriorityQueue;

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

    @Override
    public void onBackPressed() {
        int currPage = mainPager.getCurrentItem();
        if (currPage == USER_TYPE_FRAG) {
            super.onBackPressed();
        } else {
            lastPage(null);
        }
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
                    if (checked.valueAt(i)) {
                        selectedItems.add(LocationListAdapter.getItem(position));
                        //Log.d("TEST", selectedItems.get(i));
                    }
                }
                if (selectedItems.size() > 0) {
                    String[] outputStrArr = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        outputStrArr[i] = selectedItems.get(i);
                    }

                    Intent intent = new Intent(this, NavigationActivity.class);
                    intent.putExtra("LOCATIONS", outputStrArr);
                    startActivity(intent);

//                    String [] pathfind = new String[selectedItems.size()];
//                    pathfind = pathfinder(outputStrArr, new Pair(32.882261, -117.234064));
//                    for (int i = 0; i < selectedItems.size(); i++) {
//                        Log.d("PATHFIND", pathfind[i]);
//                    }

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
     * GLORIOUS GLORIOUS NP-HARD SOLUTION TO FIGURE OUT THE MOST OPTIMAL PATH HAHAHAAH
     *
     * @param locationList: should be just the list of location that the user selects in the spinner
     * @param startingPos:  starting position of the user, this should be a pair if
     */
    private String[] pathfinder(String[] locationList, Pair startingPos) {
        //uses the string length to parse the shit
        Landmark[] landmarkList = new Landmark[locationList.length];
        //creates the pq with the order of the location list
        PriorityQueue<Landmark> pq = new PriorityQueue<>(locationList.length,
                new LandmarkComparator());
        double minVal = Integer.MAX_VALUE;
        //starting location
        Landmark startingLoc = null;
        //parse initial the string
        for (int i = 0; i < locationList.length; i++) {
            Log.d("TEST", locationList[i]);
            //initializes the landmark object wit the necessary thing
            Landmark t = new Landmark(locationList[i]);
            double startX = (double) startingPos.first;
            double startY = (double) startingPos.second;
            double locX = (double) t.location.first;
            double locY = (double) t.location.second;
            Log.d("PATHFINDER", "before testing the thing ");
            //finds starting location based on the closest
            //using a trusty distance formula
            Log.d("PATHFINDER", "the distance found is " + Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2)));
            Log.d("PATHFINDER", "current min value is " + minVal);
            if (Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2)) < minVal) {
                Log.d("PATHFINDER", "what the fuck");
                minVal = Math.sqrt(Math.pow(startX - locX, 2) + Math.pow(startY - locY, 2));
                startingLoc = t;
            }
            //landmarkList[i] = t;
            //pushes it to the priority queue
            pq.add(t);
        }
        //prunes the list to find the optimal route
        //if it is middle of pq, fuck, start it on the side that has less
        //if it is first or last, fuck yea
        //TODO: change direction of collegeVal if it is fucked up
        //TODO: inner colleges need a more robust way of knowing which direction
        // the user previously came in
        int i = 0;
        int stop = 0;
        boolean reached = false;
        Landmark[] pre = new Landmark[locationList.length];
        Landmark[] post = new Landmark[locationList.length];
        //college vals for identifying clusters
        int preCollegeVal = pq.peek().collegeVal;
        //a count for how many clusters encountered thus far
        int count = 1;
        Log.d("PATHFINDER", "starting location is " + startingLoc.toString());
        //set up the pruning
        while (!pq.isEmpty()) {
            Log.d("PATHFINDER", "how many fucking times am I here");
            //just checking and then setting up the pre and post
            if (!reached) {
                Log.d("PATHFINDER", "In pre");
                pre[i] = pq.poll();
                Log.d("PATHFINDER", "popped is a " + pre[i].toString());
                if (pre[i].collegeVal != preCollegeVal) {
                    count++;
                    preCollegeVal = pre[i].collegeVal;
                }
            }
            if (!reached && pre[i].equals(startingLoc)) {
                reached = true;
                stop = i;
            }
            //last of pre is going to be equal to
            else if (reached) {
                Log.d("PATHFINDER", "in post");
                post[i] = pq.poll();
                Log.d("PATHFINDER", "popped is a " + post[i].toString());
                if (post[i].collegeVal != preCollegeVal) {
                    count++;
                    preCollegeVal = post[i].collegeVal;
                }
            }
            i++;
        }
        Landmark[] retVal = new Landmark[locationList.length];
        //prune the list
        retVal = prune(pre, post, pq, stop, locationList.length, retVal);
        Log.d("PATHFINDER", "outside of everything and retVal is" + retVal[0]);
        String[] ret = new String[locationList.length];
        for (int ind = 0; ind < retVal.length; ind++) {
            Log.d("PATHFINDER", "ind at " + ind + "is " + retVal[ind]);
            ret[ind] = retVal[ind].toString();
        }
        return ret;
    }

    /**
     * @param pre
     * @param post
     * @param pq
     * @param i
     * @param size
     */
    private Landmark[] prune(Landmark[] pre, Landmark[] post, PriorityQueue<Landmark> pq,
                             int i, int size, Landmark[] retVal) {
        ArrayList<Landmark> temp = new ArrayList<Landmark>();
        //checking the start of the loop first and everything before it
//        int collegeVal = pre[i].collegeVal;
//        int clusters = 1;
//        int preVal = pre[0].collegeVal;
//        //finds all the clusters before it
//        for(int x = 1; x < i; x++){
//            if(pre[x].collegeVal.intValue() != preVal){
//                //counting how many clusters it has before the starting point
//                preVal = pre[x].collegeVal.intValue();
//                clusters++;
//            }
//        }
        Log.d("PATHFINDER", "size is " + size + " and i is " + i);
        if (i > size / 2) {
            Log.d("PATHFINDER", "first if better be here");
            //Case 1: closest to left side and upwards, but there are inputs on right side
            //Meaning it has to be either in Marshall or in ERC, then go to the top left corner
            //first and then go down (basically reverse)
            reverseList(pre, post, size - i);
            //setting the return value of the return value;
            retVal = pre;
            Log.d("PATHFINDER", "1111" + retVal);
        } else {
            Log.d("PATHFINDER", "first else better be here");
            //Case 2: a bit more nuanced, where if the starting location is in the middle, then
            //force them to start at the bottom of the loop
            //IF THE FIRST LOCATION PICKED PICKED IN PQ IS BIOMED OR ABOVE
            if (pre[0].collegeVal.intValue() >= 2) {
                appendList(pre, post, i + 1);
                retVal = pre;
                return retVal;

            }
            //IF THE FIRST LOCATION PICKED PICKED IN PQ IS WARREN AND YOU'RE CLOSEST TO SIXTH
            else if (pre[0].collegeVal.intValue() == 0 && pre[i].collegeVal.intValue() == 1) {
                appendList(pre, post, i + 1);
                retVal = pre;
                return retVal;
            } else {
                appendList(pre, post, i + 1);
                retVal = pre;
                Log.d("PATHFINDER", "4444" + retVal[0].toString());
                return retVal;
            }
        }
        return null;
    }

    private void appendList(Landmark[] pre, Landmark[] post, int index) {
        for (; index < pre.length; index++) {
            pre[index] = post[index];
        }
    }

    private void reverseList(Landmark[] pre, Landmark[] post, int index) {
        Landmark[] temp = new Landmark[pre.length];
        for (int i = 0; i < pre.length; i++) {
            if (i < index) {
                temp[i] = post[post.length - i - 1];
            } else temp[i] = pre[pre.length - i - 1];
        }
        pre = temp;
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
