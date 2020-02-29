package com.example.navucsd;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
        mustGoListView = (ListView) findViewById(R.id.mustGoListView);
        title = (TextView) findViewById(R.id.title);
        dontHaveMustGoBtn = (Button) findViewById(R.id.IDontHaveMustGoBtn);
        nextBtn = (Button) findViewById(R.id.NEXTBtn);

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
}
