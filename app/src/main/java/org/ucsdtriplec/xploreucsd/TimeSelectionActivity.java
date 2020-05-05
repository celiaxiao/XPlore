package org.ucsdtriplec.xploreucsd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TimeSelectionActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    /*
      TODO: this frontEnd activity should contain all the necessary components
          to select the time that the student/visitor wants to spend on UCSD
      */


    private NumberPicker hoursP;
    private NumberPicker minuteP;
    private String[] timeChoices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selection);

//        timeChoices = new String[]{"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30","05:00", "05:30", "06:00"};
//        hourP.setDisplayedValues(timeChoices);

        hoursP = findViewById(R.id.hourPicker);
        hoursP.setMaxValue(6);
        hoursP.setMinValue(0);

        minuteP = findViewById(R.id.minutesPicker);
        minuteP.setMaxValue(1);
        minuteP.setMinValue(0);
        timeChoices = new String[]{"00", "30"};
        minuteP.setDisplayedValues(timeChoices);

        minuteP.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if (i1 == 1 && hoursP.getValue() == 6) {
            hoursP.setValue(0);
        }
    }

    public void selectTime(View view) {
        if (minuteP.getValue() == 0 && hoursP.getValue() == 0) {
            Toast.makeText(getApplicationContext(), "Houres and Minutes can't 0.", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, LocationSelectionActivity.class);
            String h = String.valueOf(hoursP.getValue());
            String m = String.valueOf(minuteP.getValue() * 30);
            intent.putExtra("HOURS", h);
            intent.putExtra("MINUTES", m);
            startActivity(intent);
        }
    }


    public void noConstraint(View view) {
        String m = "We will just give you our recommended route regardless of how long it will take. Confirm?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(m).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(TimeSelectionActivity.this, LocationSelectionActivity.class);
                String h = String.valueOf(0);
                String m = String.valueOf(0);
                intent.putExtra("HOURS", h);
                intent.putExtra("MINUTES", m);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", null);

        AlertDialog alter = builder.create();
        alter.show();
    }
}
