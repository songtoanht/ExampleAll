package com.zalologin.animator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.zalologin.R;
import com.zalologin.recyclerview.animators.SlideInLeftAnimator;

import java.util.ArrayList;
import java.util.List;

import service.FloatingService;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/21/2017.
 */

public class AnimatorActivity extends AppCompatActivity implements AnimAdapter.OnHandelItem {
    private static String[] data = new String[]{
            "Apple", "Ball", "Camera", "Day", "Egg", "Foo", "Google", "Hello", "Iron", "Japan", "Coke",
            "Dog", "Cat", "Yahoo", "Sony", "Canon", "Fujitsu", "USA", "Nexus", "LINE", "Haskell", "C++",
            "Java", "Go", "Swift", "Objective-c", "Ruby", "PHP", "Bash", "ksh", "C", "Groovy", "Kotlin",
            "Chip", "Japan", "U.S.A", "San Francisco", "Paris", "Tokyo", "Silicon Valley", "London",
            "Spain", "China", "Taiwan", "Asia", "New York", "France", "Kyoto", "Android", "Google",
            "iPhone", "iPad", "iPod", "Wasabeef"
    };
    private List<TextModel> mTextModels = new ArrayList<>();
    private AnimAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        dummyData();

        adapter = new AnimAdapter(this, mTextModels, this);
        recyclerView.setAdapter(adapter);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (AnimatorSampleActivity.Type type : AnimatorSampleActivity.Type.values()) {
            spinnerAdapter.add(type.name());
        }
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setItemAnimator(AnimatorSampleActivity.Type.values()[position].getAnimator());
                recyclerView.getItemAnimator().setAddDuration(500);
                recyclerView.getItemAnimator().setRemoveDuration(500);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimatorActivity.this, FloatingService.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startService(intent);
            }
        });
    }

    private void dummyData() {
        for (int i = 0; i < data.length; i++) {
            TextModel textModel = new TextModel();
            textModel.setName(data[i]);
            textModel.setSelected(false);
            mTextModels.add(textModel);
        }
    }

    @Override
    public void addItem(int position) {
        adapter.add(mTextModels.get(position), position);
    }

    @Override
    public void delItem(int position) {
        adapter.remove(position);
    }
}
