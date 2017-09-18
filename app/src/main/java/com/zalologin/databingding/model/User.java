package com.zalologin.databingding.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.zalologin.BR;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/28/2017.
 */

public class User extends BaseObservable{
    private String name;
    private String age;

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
