package com.okasha.employeetimer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmed on 21-May-16.
 */
public class PagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        ViewPager pager = (ViewPager) findViewById(R.id.pager_container);

        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                String employee = getIntent().getExtras().getString(EmployeeDetailFragment.EMPLOYEE_NAME_EXTRA);
                return EmployeeDetailFragment.newInstance(employee, position);

            }

            @Override
            public int getCount() {
                return 31;
            }
        });

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        int day =Integer.valueOf(df.format(date));


        pager.setCurrentItem(day-1);


    }
}
