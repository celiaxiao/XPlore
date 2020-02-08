package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class locationSelectionActivity extends AppCompatActivity {


    /*
      TODO: this frontEnd activity should contain all the necessary components
          to select the locations that the student/visitor wants to visit in UCSD
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        /*
             ADD ALL THE LAYOUT TO ACTIVITY_LOCATION_SELECTION.xml
         */

        Spinner firstSpinner = (Spinner) findViewById(R.id.firstLocationSpinner);
        Spinner secondSpinner = (Spinner) findViewById(R.id.SecondLocationSpinner);
        Spinner thirdSpinner = (Spinner) findViewById(R.id.thirdLocationSpinner);
        final TextView firstSelected= (TextView) findViewById(R.id.firstLocationSelected);
        final TextView secondSelected= (TextView) findViewById(R.id.secondLocationSelected);
        final TextView thirdSelected= (TextView) findViewById(R.id.thirdLocationSelected);
        /*
         Utilize three SPINNERS in Android studio to create a drop down list to pick
            the time for the tour.
         */


         ArrayAdapter<String> hardCodeAdapter=new ArrayAdapter<String>(
                locationSelectionActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.hard_code_location_name));
         hardCodeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        firstSpinner.setAdapter(hardCodeAdapter);
        secondSpinner.setAdapter(hardCodeAdapter);
        thirdSpinner.setAdapter(hardCodeAdapter);
        /*
         to verify if your SPINNER is correctly being used, please use a TEXTVIEW
            to display the locations you have selected.
         */
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);

                // Notify the selected item text
                firstSelected.setText("First Location selected: " +selectedItemText);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                // Notify the selected item text
                secondSelected.setText("Second Location selected: " +selectedItemText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                // Notify the selected item text
                thirdSelected.setText("Third Location selected: " +selectedItemText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
