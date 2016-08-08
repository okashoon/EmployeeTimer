package com.okasha.employeetimer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EmployeeDetailFragment extends Fragment {



    public static final String EMPLOYEE_NAME_EXTRA = "com.okasha.employeeNameExtra";
    public static final String DAY_EXTRA = "com.okasha.dayExtra";
    public static final String DIALOG_DATE = "date";
    public static final int REQUEST_TIMEFROM = 1;
    public static final int REQUEST_TIMETO = 2;

    String dayString;
    String dayOfWeek;
    String employeeName;
    Time fromTime;
    Time toTime;
    Button buttonFrom;
    Button buttonTo;


    public static EmployeeDetailFragment newInstance(String name,int num){

        Bundle b = new Bundle();
        b.putSerializable(EMPLOYEE_NAME_EXTRA,name);
        b.putInt(DAY_EXTRA, num);

        EmployeeDetailFragment employeeDetailFragment = new EmployeeDetailFragment();
        employeeDetailFragment.setArguments(b);
        return employeeDetailFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        employeeName = getArguments().getString(EMPLOYEE_NAME_EXTRA);
        dayString = Integer.toString(getArguments().getInt(DAY_EXTRA) + 1);

        //setting day of week from dayString
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dayString));
        SimpleDateFormat df = new SimpleDateFormat("EE");
        Date date = new Date(cal.getTimeInMillis());
        dayOfWeek = df.format(date);

        Log.d("aaa", ""+dayOfWeek);


        //setting times
        SQLiteOpenHelper helper = new EmployeeDataHelper(getActivity());
        SQLiteDatabase db =helper.getWritableDatabase();

        Cursor c =db.query(employeeName, new String[]{"_id", "col_FROM", "col_TO", "col_TOTAL"}, "_id = ?", new String[]{dayString}, null, null, null);

        if(c.moveToFirst()) {

            fromTime = new Time(Long.valueOf(c.getString(1)));
            toTime = new Time(Long.valueOf(c.getString(2)));

        }
        c.close();
        db.close();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee_detail, container, false);


        TextView title = (TextView) v.findViewById(R.id.title_employee);

        title.setText(employeeName);


        TextView day = (TextView) v. findViewById(R.id.day);
        day.setText(dayString + ", " +dayOfWeek);

        buttonFrom = (Button) v.findViewById(R.id.fromButton);
        if(fromTime.getTime()!=0){

            DateFormat df = DateFormat.getTimeInstance();
            String s =df.format(fromTime);
            buttonFrom.setText(s);

        } else {
            buttonFrom.setText("From");

        }


        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setTargetFragment(EmployeeDetailFragment.this, REQUEST_TIMEFROM);
                dialog.show(fm, DIALOG_DATE);

            }
        });

        buttonTo = (Button) v.findViewById(R.id.toButton);

        if(toTime.getTime()!=0){

            DateFormat df = DateFormat.getTimeInstance();
            String s =df.format(toTime);
            buttonTo.setText(s);

        } else {
            buttonTo.setText("To");

        }

        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setTargetFragment(EmployeeDetailFragment.this, REQUEST_TIMETO);
                dialog.show(fm, DIALOG_DATE);
            }
        });




        Button reportButton = (Button) v.findViewById(R.id.report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra(ReportActivity.EXTRA_NAME,employeeName);
                startActivity(intent);


            }
        });



        return v;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_TIMEFROM){
            fromTime = (Time) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            new updateFrom().execute(Long.valueOf(dayString), fromTime.getTime());


           getFragmentManager().beginTransaction().detach(this).attach(this).commit();


        }

        if(requestCode == REQUEST_TIMETO){
            toTime = (Time) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            new updateTo().execute(Long.valueOf(dayString),toTime.getTime());

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private  class updateFrom extends AsyncTask<Long,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Long... params) {
            Long row = params[0];
            Long from = params[1];
            SQLiteOpenHelper helper = new EmployeeDataHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("col_FROM", Long.toString(from));
            //cv.put("_id", Long.toString(f));
            db.update(employeeName,cv,"_id = ?", new String[]{Long.toString(row)});

            Log.d("aaa", "day " + dayString + " updated " + from);


            db.close();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    private  class updateTo extends AsyncTask<Long,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Long... params) {
            Long row = params[0];
            Long to = params[1];
            SQLiteOpenHelper helper = new EmployeeDataHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("col_TO", Long.toString(to));
            db.update(employeeName, cv, "_id = ?", new String[]{Long.toString(row)});



            db.close();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
