package com.zalologin.databingding.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zalologin.R;
import com.zalologin.databinding.ActivityDataBinding;
import com.zalologin.databingding.model.User;
import com.zalologin.databingding.viewmodel.MyHandel;
import com.zalologin.databingding.viewmodel.MyHandelContract;

/**
 * DataActivity
 * <p>
 * Created by HOME on 8/28/2017.
 */

public class DataActivity extends AppCompatActivity implements MyHandelContract.View123 {
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User("Toan", "24");
        MyHandel handel = new MyHandel(getApplicationContext(), this);
        ActivityDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data);
        binding.setUser(user);
        binding.setHandler(handel);
    }


    @Override
    public void showData(String s) {
        user.setName(s);
        startActivity(new Intent(this, ListActivity.class));
    }
}
