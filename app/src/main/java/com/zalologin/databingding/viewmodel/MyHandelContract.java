package com.zalologin.databingding.viewmodel;

import android.view.View;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/28/2017.
 */

public class MyHandelContract {
    public interface Presenter {
        void onHandelClicked(View view);
    }

    public interface View123 {
        void showData(String s);
    }
}
