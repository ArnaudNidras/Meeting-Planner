package fr.arnaud_piriou.meetingplanner;


import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by arnau on 09/11/2016.
 */

public class Meeting implements Serializable{


    private int id;
    private float priority;
    private Calendar cal;
    private String place;
    private String description;
    private Boolean accepted;


    public Meeting(/*int id, int priority, Date date, String place, String description, Boolean accepted*/){

        this.id = 123;
        this.id = id;
        this.priority = 0;
        this.priority = priority;
        this.cal = Calendar.getInstance();
        Log.d("CAL", String.valueOf(cal.getTime()));
        //this.date = date;
        this.place = "";
        this.place = place;
        this.description = "";
        this.description = description;
        this.accepted = false;
        this.accepted = accepted;

    }

    public int getID(){

        return id;

    }

    public float getPriority(){

        return priority;

    }

    public Calendar getCal(){

        return cal;

    }

    public String getPlace(){

        return place;

    }

    public String getDescription(){

        return description;

    }

    public Boolean getAccepted(){

        return accepted;

    }

    public void setID(int id){

        this.id = id;

    }

    public void setPriority(float priority){

        this.priority = priority;

    }

    public void setPlace(String place){

        this.place = place;

    }

    public void setDescription(String description){

        this.description = description;

    }

    public void setAccepted(boolean accepted){

        this.accepted = accepted;

    }

}
