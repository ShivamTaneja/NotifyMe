package com.example.shivam.notifyme.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivam.notifyme.Data.TaskContract;
import com.example.shivam.notifyme.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Performance extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    int showNext = 0;
    Button next;
    PieChartView pieChartView;
    int outputDays = 0, inputDays = 0;
    String taskName="dummy data";
    private static final int EXISTING_LOADER = 0;
    TextView performance;
    String performanceString="0/0";
    Cursor cursor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pieChartView = findViewById(R.id.chart);
        next = findViewById(R.id.nextButton);
        performance = findViewById(R.id.performance);

        getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showNext == 0) {
                    next.setText(R.string.next1);
                    showNext = 1;
                }
                if (showNext == 1 || showNext == 2) {

                    if(showNext == 2)
                    {
                        cursorData();
                    }
                    List<SliceValue> pieData = new ArrayList<>();
                    //just to see pie chart i am adding +51
                    SliceValue xsliceValue = new SliceValue(inputDays+51, Color.rgb(120,187,0)).setLabel(getString(R.string.output));
                    SliceValue ysliceValue = new SliceValue(outputDays, Color.rgb(52,81,0)).setLabel(taskName);
                    pieData.add(xsliceValue);
                    pieData.add(ysliceValue);
                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                    pieChartView.setPieChartData(pieChartData);
                    showNext=2;

                }
            }
        });
    }

    private void cursorData() {

        if (cursor1.moveToNext()) {
            int inputDaysColumnIndex = cursor1.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED);
            int outputDaysColumnIndex = cursor1.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS);
            int taskNameColumnIndex = cursor1.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);

            inputDays = cursor1.getInt(inputDaysColumnIndex);
            outputDays = cursor1.getInt(outputDaysColumnIndex);
            taskName = cursor1.getString(taskNameColumnIndex);

            performanceString = inputDays + "/" + outputDays;
            performance.setText(performanceString);
             }
        else {
            Toast.makeText(getApplicationContext(), "No more items to display", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED,
                TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS, TaskContract.TaskEntry.COLUMN_TASK_NAME};

        return new CursorLoader(this, TaskContract.TaskEntry.CONTENT_URI, projection,
                null, null, null);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            Toast.makeText(getApplicationContext(), "No item to display", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if(cursor.moveToFirst()) {
                int inputDaysColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_NUMBER_OF_DAYS_PERFORMED);
                int outputDaysColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASk_TOTAL_NUMBER_OF_DAYS);
                int taskNameColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);

                inputDays = cursor.getInt(inputDaysColumnIndex);
                outputDays = cursor.getInt(outputDaysColumnIndex);
                taskName = cursor.getString(taskNameColumnIndex);

                performanceString = inputDays + "/" + outputDays;
                performance.setText(performanceString);

                cursor1 = cursor;
            }
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader < Cursor > loader)
    {
        performance.setText("");
    }
}
