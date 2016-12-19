package fr.arnaud_piriou.meetingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class ShowActivity extends FragmentActivity {


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private int NUM_PAGES = 5;
    private ArrayList<Meeting> meetingList;
    private Meeting meeting;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i = getIntent();
        meetingList = (ArrayList<Meeting>) i.getSerializableExtra("meetingList");
        NUM_PAGES = meetingList.size();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);



    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {

            super.onBackPressed();
        } else {

            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("meeting", meetingList.get(position));
            ShowFragment frag = new ShowFragment();
            frag.setArguments(bundle);

            return frag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
