package com.okasha.employeetimer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddEmployeeFragment.EditNameDialogListener{

    ArrayList<String> employeeNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView mainEmployeeList;

    @Override
    public void onFinishEditDialog(String name) {

        EmployeeDataHelper helper = new EmployeeDataHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select name from sqlite_master where type = 'table' and name = ?", new String[]{name});
        if(!c.moveToFirst()) {


            db.execSQL("CREATE TABLE " + name + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "col_FROM TIME," +
                    "col_TO TIME," +
                    "col_TOTAL TIME);");
            for(int i = 0; i <31; i++){
                ContentValues values = new ContentValues();
                values.put("col_FROM", 0);
                values.put("col_TO", 0);
                values.put("col_TOTAL", 0);
                db.insert(name,null,values);
            }

        } else {
            Toast.makeText(this,"employee already exists",Toast.LENGTH_LONG).show();
        }
        c.close();
        db.close();
        loadEmployees();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //getApplicationContext().deleteDatabase("EmployeeTimer");


        loadEmployees();

        mainEmployeeList = (ListView) findViewById(R.id.main_list);

        registerForContextMenu(mainEmployeeList);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,employeeNames);

        mainEmployeeList.setAdapter(adapter);



       mainEmployeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               TextView text = (TextView) view;
               String employee = text.getText().toString();

               Intent intent = new Intent(MainActivity.this, PagerActivity.class);
               intent.putExtra(EmployeeDetailFragment.EMPLOYEE_NAME_EXTRA, employee);

               startActivity(intent);
           }
       });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_employee){
            FragmentManager fm = getSupportFragmentManager();
            AddEmployeeFragment dialog = new AddEmployeeFragment();

            dialog.show(fm,"dialogAddEmployee");

            return true;
        }
        return false;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.employee_list_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo adapter =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TextView tv = (TextView)adapter.targetView;
        String name = tv.getText().toString();

        switch (item.getItemId()){
            case(R.id.context_report):

                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra(ReportActivity.EXTRA_NAME,name);
                startActivity(intent);
                return true;

            case (R.id.context_delete_employee):
                EmployeeDataHelper helper = new EmployeeDataHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("drop table "+name);
                db.close();
                loadEmployees();
                this.adapter.notifyDataSetChanged();


        }
        return super.onContextItemSelected(item);
    }

    public void loadEmployees(){

        employeeNames.clear();
        EmployeeDataHelper helper = new EmployeeDataHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();


        Cursor c = db.rawQuery("SELECT NAME FROM sqlite_master WHERE TYPE = 'table' and name != 'sqlite_sequence' and name != 'android_metadata'",null);

        if(c.moveToFirst()) {

            do {
                employeeNames.add(c.getString(0));
            } while (c.moveToNext());
        }


        c.close();
        db.close();


    }






}
