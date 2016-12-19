package fr.arnaud_piriou.meetingplanner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MeetingCreationActivity extends AppCompatActivity {

    private Calendar calendar;
    private Button setDateButton;
    private Button setTimeButton;
    private Button addMeetingButton;
    private RatingBar ratingBar;
    private EditText addPlace;
    private EditText addDescription;
    private TextView dateLabel;
    private TextView timeLabel;
    private int year, month, day, hour, minute;
    private ArrayList<Meeting> meetingList;
    private Meeting meeting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_creation);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        meetingList = (ArrayList<Meeting>) i.getSerializableExtra("meetingList");
        meeting = new Meeting();


        dateLabel = (TextView) findViewById(R.id.dateText);
        timeLabel = (TextView) findViewById(R.id.timeText);
        setDateButton = (Button) findViewById(R.id.setDateButton);
        setTimeButton = (Button) findViewById(R.id.setTimeButton);
        addMeetingButton = (Button) findViewById(R.id.addMeetingButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        addPlace = (EditText) findViewById(R.id.addPlace);
        addDescription = (EditText) findViewById(R.id.addDescription);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        month ++;

        dateLabel.setText(getResources().getString(R.string.date_text) + " " + day + "/" + month + "/" + year);
        timeLabel.setText(getResources().getString(R.string.time_text) + " " + hour + ":" + minute);

        setDateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                showDialog(1);

            }
        });

        setTimeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                showDialog(2);

            }
        });

        addMeetingButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                meeting.setDescription(addDescription.getText().toString());
                meeting.setPlace(addPlace.getText().toString());
                meeting.setPriority(ratingBar.getRating());
                meeting.setID(meetingList.size());

                meetingList.add(0, meeting);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("meetingList", meetingList);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                onBackPressed();

            }
        });

    }



    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 1) {
            return new DatePickerDialog(this, dateSetListener, year, month-1, day);
        }
        if (id == 2) {
            return new TimePickerDialog(this,timeSetListener, hour, minute, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

                    meeting.getCal().set(Calendar.YEAR, arg1);
                    meeting.getCal().set(Calendar.MONTH, arg2);
                    meeting.getCal().set(Calendar.DAY_OF_MONTH, arg3);

                    int temp = arg2 + 1;

                    String date = "" + arg3 + "/" + temp + "/" + arg1;

                    dateLabel.setText(getResources().getString(R.string.date_text) + " " + date);
                    Toast.makeText(getApplicationContext(), getString(R.string.date_text) + date, Toast.LENGTH_SHORT).show();

                }
            };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new
            TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

                    meeting.getCal().set(Calendar.HOUR_OF_DAY, arg1);
                    meeting.getCal().set(Calendar.MINUTE, arg2);

                    String time = "" + arg1 + ":" + arg2;

                    timeLabel.setText(getResources().getString(R.string.time_text) + " " + time);
                    Toast.makeText(getApplicationContext(), getString(R.string.time_text) + time, Toast.LENGTH_SHORT).show();

                }
            };

}
