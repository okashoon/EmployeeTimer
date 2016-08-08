package com.okasha.employeetimer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;

public class ReportActivity extends AppCompatActivity {

    TableLayout table;
    String name;
    public static final String EXTRA_NAME = "com.ahmed.reportActivity.extraName";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        name = getIntent().getStringExtra(EXTRA_NAME);

        TextView title = (TextView) findViewById(R.id.report_employee_name);
        title.setText(name);


        table  = (TableLayout) findViewById(R.id.table_layout1);


        buildTable();

    }




    public void buildTable(){

        SQLiteOpenHelper helper = new EmployeeDataHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //update col_TOTAL
        db.execSQL("update " +name+" set col_TOTAL = col_TO - col_FROM ");
        db.execSQL("update " + name + " set col_TOTAL = (col_TOTAL + 86400000) where col_TOTAL < 0 ");
        db.execSQL("update " + name + " set col_TOTAL = 0 where col_FROM = 0 or col_TO = 0 ");




        Cursor c = db.query(name, new String[]{"_id", "col_from", "col_TO", "col_TOTAL"}, null, null, null, null, null);
        c.moveToFirst();

        int rows = c.getCount();


        c.moveToFirst();

        TableRow firstRow = new TableRow(this);
        firstRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        firstRow.setBackgroundColor(Color.rgb(47, 42, 97));

        TextView dayCell = new TextView(this);
        dayCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        dayCell.setText(getString(R.string.day));
        dayCell.setTextColor(Color.WHITE);
        dayCell.setTextSize(16);

        TextView fromCell = new TextView(this);
        fromCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        fromCell.setText(getString(R.string.from));
        fromCell.setTextColor(Color.WHITE);
        fromCell.setTextSize(16);

        TextView toCell = new TextView(this);
        toCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        toCell.setText(getString(R.string.to));
        toCell.setTextColor(Color.WHITE);
        toCell.setTextSize(16);

        TextView totalCell = new TextView(this);
        totalCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        totalCell.setText(getString(R.string.total));
        totalCell.setTextColor(Color.WHITE);
        totalCell.setTextSize(16);

        firstRow.addView(dayCell);
        firstRow.addView(fromCell);
        firstRow.addView(toCell);
        firstRow.addView(totalCell);
        table.addView(firstRow);




        //data rows and cells
        for(int i = 0; i<rows;i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(2,2,2,2);

            row.setLayoutParams(params);


            //adding data cells:day(no alteration)-timeFrom&timeTo(datefromat)-total(convert milliseconds to hours and minutes)
            TextView cellIndex = new TextView(this);
            cellIndex.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            cellIndex.setBackgroundColor(Color.rgb(16, 34, 142));
            cellIndex.setText(c.getString(0));
            cellIndex.setTextColor(Color.WHITE);
            cellIndex.setTextSize(16);
            row.addView(cellIndex,params);

            for (int j = 1; j < 3; j++) {
                TextView cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                cell.setBackgroundColor(Color.rgb(103, 116, 193));
                cell.setTextSize(16);
                long l = c.getLong(j);
                if(l==0){
                    cell.setText("X");
                } else {
                    Time t = new Time(l);
                    DateFormat df = DateFormat.getTimeInstance();
                    String s = df.format(t);
                    cell.setText(s);

                }
                    row.addView(cell,params);

            }

            //col_Total
            TextView cellTotal = new TextView(this);
            cellTotal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            cellTotal.setBackgroundColor(Color.rgb(103, 193, 192));
            cellTotal.setTextSize(16);

            long millis = c.getLong(3);
            if(millis == 0||c.getLong(1)==0||c.getLong(2)==0){
                cellTotal.setText("X");
            } else {
                long minute = (millis / (1000 * 60)) % 60;
                long hour = (millis / (1000 * 60 * 60)) % 24;
                String s = String.format("%02d:%02d", hour, minute);

                cellTotal.setText(s);
            }
            row.addView(cellTotal,params);

            c.moveToNext();
            table.addView(row,params);
        }

        //final sum total row
        Cursor sumCursor = db.rawQuery("SELECT SUM(col_TOTAL) FROM " + name ,null);
        long sum = 0;
        if(sumCursor.moveToFirst()){
           sum = sumCursor.getLong(0);
        }

        long minute = (sum / (1000 * 60)) % 60;
        long hour = (sum / (1000 * 60 * 60)) ;
        String s = String.format("%02d : %02d", hour, minute);




        TableRow finalTotalRow = new TableRow(this);
        finalTotalRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        finalTotalRow.setBackgroundColor(Color.rgb(47, 42, 97));

        TextView view = new TextView(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.span= 3;
        view.setLayoutParams(lp);
        view.setText(getString(R.string.total_hours_during_month));
        view.setTextColor(Color.WHITE);

        TextView finalTotalCell = new TextView(this);
        finalTotalCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        finalTotalCell.setText(s);
        finalTotalCell.setTextColor(Color.WHITE);
        finalTotalCell.setTextSize(16);


        finalTotalRow.addView(view);
        finalTotalRow.addView(finalTotalCell);
        table.addView(finalTotalRow);

        sumCursor.close();
        c.close();
        db.close();
    }

}
