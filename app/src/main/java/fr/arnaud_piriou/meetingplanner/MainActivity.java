package fr.arnaud_piriou.meetingplanner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Meeting> meetingList;
    private Locale myLocale;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.mipmap.ic_background_meeting);

        meetingList = new ArrayList<Meeting>();
        Meeting meeting = new Meeting();
        meeting.setID(0);
        meeting.setPlace("ESIEA, Ivry sur Seine");
        meeting.setDescription("Raclette Party with friends :)");
        meeting.setPriority((float) 4.5);
        meeting.getCal().set(Calendar.YEAR, 2016);
        meeting.getCal().set(Calendar.MONTH, 13);
        meeting.getCal().set(Calendar.DAY_OF_MONTH, 19);
        meeting.getCal().set(Calendar.HOUR_OF_DAY, 18);
        meeting.getCal().set(Calendar.MINUTE, 10);
        meetingList.add(0, meeting);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_add_meeting) {

            Intent addScreen = new Intent(getApplicationContext(), MeetingCreationActivity.class);
            addScreen.putExtra("meetingList", meetingList);
            startActivityForResult(addScreen, 0);

        } else if (id == R.id.nav_show_meeting) {

            Intent showScreen = new Intent(getApplicationContext(), ShowActivity.class);
            showScreen.putExtra("meetingList", meetingList);
            startActivity(showScreen);

        } else if (id == R.id.nav_send) {

            new Thread(new Runnable() {
                public void run() {
                    try {
                        Boolean download = ftpdownload();
                        if(download){

                            Intent dlfinished = new Intent("fr.arnaud_piriou.meetingplanner.DOWNLOADCOMPLETE");
                            sendBroadcast(dlfinished);
                            jsonParser();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    meetingList = (ArrayList<Meeting>) data.getSerializableExtra("meetingList");
                }
                break;
            }
        }
    }

    public boolean ftpdownload() throws IOException {

        FTPClient ftp = null;

        try {

            ftp = new FTPClient();
            ftp.connect("ftp.nidras.pe.hu", 21);

            ftp.login("u366919413.nidras", "panini");

            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            ftp.enterLocalPassiveMode();

            boolean success = false;


                BufferedReader br = new BufferedReader(new InputStreamReader(ftp.retrieveFileStream("meeting.json")));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                FileWriter out = new FileWriter(new File(getFilesDir(), "\\res\\meeting.json"));
                out.write(sb.toString());
                out.close();
                BufferedReader in = new BufferedReader(new FileReader(new File(getFilesDir(), "\\res\\meeting.json")));
                sb = new StringBuilder();
                line = "";
                while ((line = in.readLine()) != null) sb.append(line);
                in.close();
                Log.d("FTP", "" + sb.toString());
                if(sb.toString().length() != 0) success = true;

            return success;

        } finally {

            if (ftp != null) {

                ftp.logout();
                ftp.disconnect();

            }

        }

    }


    public void jsonParser(){



        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(getFilesDir(), "\\res\\meeting.json")));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();
            Log.d("FTP", "" + sb.toString());

            JSONObject jsonObj = new JSONObject(sb.toString());
            JSONArray meeting = jsonObj.getJSONArray("meeting");

            for (int i = 0; i < meeting.length(); i ++) {

                JSONObject mt = meeting.getJSONObject(i);

                Meeting meetingtemp = new Meeting();



                meetingtemp.getCal().set(Calendar.YEAR, mt.getInt("year"));
                meetingtemp.getCal().set(Calendar.MONTH, mt.getInt("month")-1);
                meetingtemp.getCal().set(Calendar.DAY_OF_MONTH, mt.getInt("day"));
                meetingtemp.getCal().set(Calendar.HOUR_OF_DAY, mt.getInt("hour"));
                meetingtemp.getCal().set(Calendar.MINUTE, mt.getInt("minute"));

                meetingtemp.setPlace(mt.getString("place"));
                meetingtemp.setDescription(mt.getString("description"));
                meetingtemp.setPriority((float) mt.getDouble("rating"));

                meetingList.add(0, meetingtemp);


            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_reunion);
            builder.setContentTitle(getString(R.string.notif_title));
            builder.setContentText("" + meeting.length() + getString(R.string.notif_text));
            builder.setPriority(4);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
