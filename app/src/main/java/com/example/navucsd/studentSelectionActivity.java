package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class studentSelectionActivity extends AppCompatActivity {

    /*
   TODO: this frontEnd activity should display the selection between a visitor
        and a student of UCSD
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_selection);

        /*
            TODO: ADD ALL THE LAYOUT TO ACTIVITY_STUDENT_SELECTION.xml
         */

        /*
        TODO: Include a TEXTVIEW that prompts "Are you a" with a SPINNER underneath it that
            offers the user to pick from either a visitor or a student.For documentation
            please read:
                https://developer.android.com/guide/topics/ui/controls/spinner
            If you prefer a youtube video instead, please consider this:
            https://www.youtube.com/watch?v=urQp7KsQhW8

            (A spinner is a drop down menu)
         */

        /*
        TODO: to verify if your SPINNER is correctly being used, please use a TEXTVIEW
            to display the option you have selected.
         */

        /*
         TODO: In the bottom right corner of this page, please include a BUTTON that
            redirects this page to the timeSelectionActivity. This functionality
            should have been covered in the tutorials we have sent you earlier
         */
    }
}
