package com.zalologin.databingding.viewmodel;

import android.content.Context;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/28/2017.
 */

public class MyHandel implements MyHandelContract.Presenter {
    private Context context;
    private MyHandelContract.View123 v;
    private List<String> randomList = Arrays.asList("Song Toan", "Tu Oanh", "Van Linh", "Huong Giang", "Mi tom", "Me miu");

    public MyHandel(Context context, MyHandelContract.View123 v) {
        this.context = context;
        this.v = v;
    }

    @Override
    public void onHandelClicked(View view) {
        v.showData(getRandomList());
    }

    private String getRandomList() {
        Collections.shuffle(randomList);
        return randomList.get(0);
    }
}
