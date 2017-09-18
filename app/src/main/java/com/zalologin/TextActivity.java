package com.zalologin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/14/2017.
 */

public class TextActivity extends AppCompatActivity{
    private CustomTextView customTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        customTextView = (CustomTextView) findViewById(R.id.customText);

        String str = "When Google announced Data Binding Library at last year Google I/O, I was thinking “oh man, this"/* +
                "And yeah, the hype was real (for the next two weeks), and it all kinda disappeared."*/;
        String str1 = "Haha";


        customTextView.setTvTitle(str);
        customTextView.setTvLabel(str1);
    }
}

