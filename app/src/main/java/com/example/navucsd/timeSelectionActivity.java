package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class timeSelectionActivity extends AppCompatActivity {

    /*
      TODO: this frontEnd activity should contain all the necessary components
          to select the time that the student/visitor wants to spend on UCSD
      */

    private TextView timeSelect;
    private Button next;
    private Toast toast;
    private Spinner timeList;

    public String time="SelectedTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selection);

        /*
            TODO: ADD ALL THE LAYOUT TO ACTIVITY_TIME_SELECTION.xml
         */

        /*
         TODO: Utilize SPINNERS in Android studio to create a drop down list to pick
            the time for the tour. For documentation please read:
                https://developer.android.com/guide/topics/ui/controls/spinner
            If you prefer a youtube video instead, please consider this:
            https://www.youtube.com/watch?v=urQp7KsQhW8
         */

        /*
        TODO: to verify if your SPINNER is correctly being used, please use a TEXTVIEW
            to display the time you have selected.
         */

        /*
         TODO: In the bottom right corner of this page, please include a BUTTON that
            redirects this page to the locationSelectionActivity. This functionality
            should have been covered in the tutorials we have sent you earlier
         */
        timeSelect = (TextView) findViewById(R.id.TimeSelect);
        timeList = (Spinner) findViewById(R.id.spinnerAtTimeSelect);
        ArrayAdapter<String> timeArray = new ArrayAdapter<String>(timeSelectionActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.time));

        timeArray.setDropDownViewResource(android.R.layout.simple_list_item_1);
        timeList.setAdapter(timeArray);
        timeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!String.valueOf(timeList.getSelectedItem()).equals("Please choose the time")){
                    timeSelect.setText("I need to finish this tour within " + String.valueOf(timeList.getSelectedItem()) + " hours");
                }
                else{
                    timeSelect.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        while (!String.valueOf(timeList.getSelectedItem()).equals("Please choose the time")){
//            timeSelect.setText("I need to finish this tour within " + String.valueOf(timeList.getSelectedItem()) + " hours");
//        }
        next = (Button) findViewById(R.id.NextButtoNAtTimeSelect);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(String.valueOf(timeList.getSelectedItem()).equals("Please choose the time")){
                    Context context = getApplicationContext();
                    CharSequence text = "Please, select the time.";
                    int duration = Toast.LENGTH_SHORT;
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    Intent intent = new Intent(timeSelectionActivity.this, locationSelectionActivity.class);
                    intent.putExtra(time, String.valueOf(timeList.getSelectedItem()));
                    startActivity(intent);
                }
            }
        });
    }
}
