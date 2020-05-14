package com.example.navucsd.archive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navucsd.archive.LandmarkComparator;
import com.example.navucsd.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.PriorityQueue;

//import androidx.appcompat.app.AppCompatActivity;

public class LocationSelectionActivity extends AppCompatActivity {

    /*
      TODO: this frontEnd activity should contain all the necessary components
          to select the locations that the student/visitor wants to visit in UCSD
    */
    TextView title;
    ArrayAdapter<String> LocationListAdapter;
    ListView mustGoListView;
    Button dontHaveMustGoBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        /*
             ADD ALL THE LAYOUT TO ACTIVITY_LOCATION_SELECTION.xml
         */
        mustGoListView = findViewById(R.id.mustGoListView);
        title = findViewById(R.id.title);
//         dontHaveMustGoBtn=(Button)findViewById(R.id.IDontHaveMustGoBtn);
//        nextBtn=(Button)findViewById(R.id.NEXTBtn);

        //set up the list view choices
        String[] mustGoList = getResources().getStringArray(R.array.list_of_must_go);
        LocationListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, mustGoList);
        mustGoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mustGoListView.setAdapter(LocationListAdapter);

        //if next bottom is clicked, return the selection list
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    alertDialog();
                }
            }

        });
        String[] test = {"canyonViewPool", "JSOE", "sunGodStatue"};
        String[] hehe = pathfinder(test, new Pair(32.882261, -117.234064));
        for (int i = 0; i < hehe.length; i++) {
            Log.d("TEST", "at index " + i + " is location " + hehe[i]);
        }

        dontHaveMustGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

    }

    private void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("We will just give you our recommended tour, confirm?");
        dialog.setTitle("Don't have must-go's?");
        dialog.setPositiveButton("Comfirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
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
}
