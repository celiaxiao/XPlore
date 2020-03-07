package com.example.navucsd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import androidx.fragment.app.Fragment;

public class TimeSelectionFragment extends Fragment implements NumberPicker.OnValueChangeListener{

    private NumberPicker hoursP;
    private NumberPicker minuteP;
    private String[] timeChoices;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_time_selection, container, false);
        hoursP = getView().findViewById(R.id.hoursPicker);
        hoursP.setMaxValue(6);
        hoursP.setMinValue(0);
        minuteP = getView().findViewById(R.id.minutesPicker);
        minuteP.setMaxValue(1);
        minuteP.setMinValue(0);
        timeChoices = new String[]{"00", "30"};
        minuteP.setDisplayedValues(timeChoices);
        minuteP.setOnValueChangedListener((NumberPicker.OnValueChangeListener) this);
        // Click after user selected the time
        Button timeSelection = getView().findViewById(R.id.callTimeSelect);
        timeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });

        // Click if user want to use the recommend route
        Button onConstraint = getView().findViewById(R.id.noConstraint);
        onConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noTimeSelect();
            }
        });
        return rootView;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if(i1 == 1 && hoursP.getValue() == 6){
            hoursP.setValue(0);
        }
    }

    public void selectTime() {
        if( minuteP.getValue() == 0 && hoursP.getValue() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "Houres and Minutes can't 0.", Toast.LENGTH_LONG).show();
        }
        else{
            // Go to Locations Selection Fragment



//            Intent intent = new Intent(this, LocationSelectionFragment.class);
//            String h = String.valueOf(hoursP.getValue());
//            String m = String.valueOf(minuteP.getValue()*30);
//            intent.putExtra("HOURS", h);
//            intent.putExtra("MINUTES", m);
//            startActivity(intent);
        }
    }


    public void noTimeSelect() {
        String m = "We will just give you our recommended route regardless of how long it will take. Confirm?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        builder.setMessage(m).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String h = String.valueOf(0);
                String m = String.valueOf(0);

                // Go to Locations Selection Fragment
                LocationSelectionFragment lsFragment = (LocationSelectionFragment)getFragmentManager().findFragmentById(R.id.gl_loc_frag);
                Bundle args = new Bundle();


//                Intent intent = new Intent(timeSelectionActivity.this, locationSelectionActivity.class);
//                String h = String.valueOf(0);
//                String m = String.valueOf(0);
//                intent.putExtra("HOURS", h);
//                intent.putExtra("MINUTES", m);
//                startActivity(intent);
            }
        }).setNegativeButton("Cancel", null);

        AlertDialog alter = builder.create();
        alter.show();
    }


}
