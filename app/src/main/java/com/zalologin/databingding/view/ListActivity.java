package com.zalologin.databingding.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zalologin.R;
import com.zalologin.databingding.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/29/2017.
 */

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        UserAdapter mAdapter = new UserAdapter(this, testData());
        recyclerView.setAdapter(mAdapter);
    }

    private List<User> testData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User("Song Toan " + i, (i * 2 / 3 + 7) + ""));
        }
        return users;
    }
}
