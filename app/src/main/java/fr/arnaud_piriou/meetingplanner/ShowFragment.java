package fr.arnaud_piriou.meetingplanner;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class ShowFragment extends Fragment {


    private Meeting meeting;
    private DatePicker dp;
    private TimePicker tp;
    private Button map;
    private TextView description;
    private TextView place;
    private RatingBar ratingBar;

    public ShowFragment() {



    }

    public static ShowFragment newInstance(Meeting meeting) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_show, container, false);


        meeting = (Meeting) getArguments().getSerializable("meeting");

        dp = (DatePicker) rootView.findViewById(R.id.frag_datePicker);
        tp = (TimePicker) rootView.findViewById(R.id.frag_timePicker);
        description = (TextView) rootView.findViewById(R.id.frag_Description);
        place = (TextView) rootView.findViewById(R.id.frag_Place);
        map = (Button) rootView.findViewById(R.id.frag_map);
        ratingBar = (RatingBar) rootView.findViewById(R.id.frag_RatingBar);

        dp.updateDate(meeting.getCal().get(Calendar.YEAR), meeting.getCal().get(Calendar.MONTH), meeting.getCal().get(Calendar.DAY_OF_MONTH));
        dp.setMinDate(meeting.getCal().getTime().getTime());
        dp.setMaxDate(meeting.getCal().getTime().getTime());
        dp.setEnabled(false);
        tp.setCurrentHour(meeting.getCal().get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(meeting.getCal().get(Calendar.MINUTE));
        tp.setEnabled(false);

        description.setText("Description :\n" + meeting.getDescription());
        place.setText("Place :\n" + meeting.getPlace());

        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri IntentUri = Uri.parse("geo:0,0?q=" + meeting.getPlace());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        ratingBar.setRating(meeting.getPriority());



        return rootView;
    }

}
