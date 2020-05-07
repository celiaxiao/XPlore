package com.example.navucsd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This front-end activity displays the selection between a visitor and a UCSD student.
 */
public class StudentSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_selection);

        // sets up the spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // connect the testChoiceTextView with the spinner
        TextView testChoiceTextView = findViewById(R.id.testChoiceTextView);

        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());

        // sets up the button used to go to the next screen (TimeSelectionActivity)
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> startActivity(
                new Intent(getApplicationContext(), TimeSelectionActivity.class)
        ));
    }

    /**
     * Listens for the spinner selected event and sets the test text.
     */
    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        private TextView testChoiceTextView = findViewById(R.id.testChoiceTextView);

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            testChoiceTextView.setText((String) parent.getItemAtPosition(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            testChoiceTextView.setText(R.string.test_nothing_selected);
        }
    }
}
