package com.okasha.employeetimer;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.sql.Time;
import java.util.Calendar;

import android.util.Log;
import android.widget.TimePicker;



public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


   Time time;

    public static final String EXTRA_TIME ="com.ahmed.employeeTimer.timeFrom";



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute,false);



    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        time = new Time(hourOfDay,minute,0);
        Log.d("aaa", "timefrom = " + time);

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, time);

        if(getTargetRequestCode() == EmployeeDetailFragment.REQUEST_TIMEFROM) {
            getTargetFragment().onActivityResult(EmployeeDetailFragment.REQUEST_TIMEFROM, Activity.RESULT_OK, i);
        }
        if (getTargetRequestCode() == EmployeeDetailFragment.REQUEST_TIMETO) {

            getTargetFragment().onActivityResult(EmployeeDetailFragment.REQUEST_TIMETO, Activity.RESULT_OK, i);
        }


    }
}
