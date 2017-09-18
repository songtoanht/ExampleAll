package com.zalologin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.zalologin.databingding.view.UserAdapter;
import com.zalologin.model.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * //Todo
 * <p>
 * Created by HOME on 7/6/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Date> mDates = new ArrayList<>();
    private int preOffset;
    private int nextOffset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        DateAdapter mAdapter = new DateAdapter(this, mDates);
        recyclerView.setAdapter(mAdapter);

        setDataDate();
    }

    private void setDataDate() {
        Calendar c = Calendar.getInstance();
        int summary = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        preOffset = c.get(Calendar.DAY_OF_WEEK) - 1;
        nextOffset = 42 - summary - preOffset;
        Log.d("ttt", "setDataDate: " + summary + " : " + preOffset + " : " + nextOffset);
        c.set(Calendar.DAY_OF_MONTH, -preOffset);
        for (int i = 0; i < 42; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);

            Log.d("ttt", "year: " + c.get(Calendar.MONTH) + " : " + c.get(Calendar.DAY_OF_MONTH));
            Date date = new Date(c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH),
                    convertDateToString(c.get(Calendar.DAY_OF_WEEK)));
            mDates.add(date);
        }
    }

    private String convertDateToString(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return Week.SUNDAY;
            case 2:
                return Week.MONDAY;
            case 3:
                return Week.TUESDAY;
            case 4:
                return Week.WEDNESDAY;
            case 5:
                return Week.THURSDAY;
            case 6:
                return Week.FRIDAY;
            case 7:
                return Week.SATURDAY;
            default:
                return Week.SUNDAY;
        }
    }
}
